package com.flocktamer.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;
import com.flocktamer.adapters.TwitterUserAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.interfaces.OnTwitterAccountDelete;
import com.flocktamer.model.TwitterUserModel;
import com.flocktamer.model.childmodel.TwitterUser;
import com.flocktamer.ui.SplashActivity;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link AddAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountFragment extends BaseFragment implements OnTwitterAccountDelete {

    @BindView(R.id.rv_twitter_users)
    RecyclerView mUsersList;

    @BindView(R.id.twitter_login_button)
    TwitterLoginButton mTwitterLogin;

    @BindView(R.id.tv_add_account_nodata)
    CustomTextView mNoDataText;

    public AddAccountFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddAccountFragment.
     */
    public static AddAccountFragment newInstance() {
        return new AddAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);
        ButterKnife.bind(this, view);
        getBaseActivity().initToolBar("Add Account", true);
        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {
        getTwitterAccounts();

        mTwitterLogin.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                final TwitterSession session = result.data;

                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(false, false).enqueue(new com.twitter.sdk.android.core.Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        HashMap<String, String> data = new HashMap<>();
                        data.put("twitter_user_id", user.getId() + "");
                        data.put("twitter_oauth_token", session.getAuthToken().token);
                        data.put("twitter_x_auth_expires", "");
                        data.put("twitter_oauth_token_secret", session.getAuthToken().secret);
                        data.put("twitter_screen_name", user.screenName);
                        data.put("twitter_name", user.name);

                        data.put("twitter_profile_image_url", user.profileImageUrl);
                        data.put("twitter_profile_image_url_https", user.profileImageUrlHttps);
//
                        addTwitterAccount(data);
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    private void getTwitterAccounts() {
        final Dialog dialog =  getBaseActivity().showProgress();
        Call<TwitterUserModel> callApi = RetrofitBuilder.getService().getTwitterAccounts(getAppPref().getAccessToken());
        callApi.enqueue(new Callback<TwitterUserModel>() {
            @Override
            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    TwitterUserModel data = response.body();
                    if (Constants.STATUS_SUCCESS.equalsIgnoreCase(data.getStatus())) {
                        showAccounts(data.getData());
                    } else {
                        hideRecyclerView();
//                        Util.showErrorSnackBar(getView(), data.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<TwitterUserModel> call, Throwable t) {

                if (dialog != null) {
                    dialog.dismiss();
                }

                if (t instanceof UnknownHostException) {
                    Util.noInternetAlert( getBaseActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getBaseActivity().finish();
                        }
                    });

                }
            }
        });
    }

    private void showAccounts(ArrayList<TwitterUser> data) {
        if (data != null && !data.isEmpty()) {
            showRecyclerView();
            TwitterUserAdapter adatper = new TwitterUserAdapter(data, true, AddAccountFragment.this,false);
            mUsersList.setAdapter(adatper);
            mUsersList.setLayoutManager(new LinearLayoutManager( getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        } else {
            hideRecyclerView();
        }
    }

    private void addTwitterAccount(HashMap<String, String> data) {

        final Dialog dialog =  getBaseActivity().showProgress();
        Call<TwitterUserModel> callApi = RetrofitBuilder.getService().addTwitterAccount(getAppPref().getAccessToken(), data);

        callApi.enqueue(new retrofit2.Callback<TwitterUserModel>() {
            @Override
            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {

                    Util.showToast( getBaseActivity(), response.body().getMessage());
                    Intent intent = new Intent(getActivity(), TabsInitActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                   // getBaseActivity().finish();
                } else if (response.code() == 401) {
                    getBaseActivity().logoutApp();
                }
            }

            @Override
            public void onFailure(Call<TwitterUserModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                if (t instanceof UnknownHostException) {
                    Util.noInternetAlert( getBaseActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getBaseActivity().finish();
                        }
                    });

                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDeleteClick(final TwitterUser user) {

        new AlertDialog.Builder( getBaseActivity())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete this Twitter account?")
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccountAPI(user);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    private void deleteAccountAPI(TwitterUser user) {

        HashMap<String, String> data = new HashMap<>();
        data.put("id", user.getId());
        Call<TwitterUserModel> callApi = RetrofitBuilder.getService().removeTwitterAccount(getAppPref().getAccessToken(), data);
        callApi.enqueue(new Callback<TwitterUserModel>() {
            @Override
            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
                if (response.code() == 200) {
                    if (Constants.STATUS_SUCCESS.equalsIgnoreCase(response.body().getStatus())) {
                        if (response.body().getData() == null || response.body().getData().size() < 1) {
                            Util.showToast( getBaseActivity(), response.body().getMessage());
                          //  getBaseActivity().finish();
                            Intent intent = new Intent(getActivity(), TabsInitActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return;
                        }
                        showAccounts(response.body().getData());
                        Util.showSucessSnackBar(getView(), response.body().getMessage());
                    } else {
                        Util.showErrorSnackBar(getView(), response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<TwitterUserModel> call, Throwable t) {

            }
        });
    }

    private void showRecyclerView() {

        mNoDataText.setVisibility(View.GONE);
        mUsersList.setVisibility(View.VISIBLE);
    }

    private void hideRecyclerView() {
        mNoDataText.setVisibility(View.VISIBLE);
        mUsersList.setVisibility(View.GONE);
    }
}
