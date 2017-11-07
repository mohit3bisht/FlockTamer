package com.flocktamer.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.flocktamer.R;
import com.flocktamer.adapters.TwitterUserComposeAdapter;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.interfaces.KnownSelectedItemComposeInterface;
import com.flocktamer.model.TwitterUserModel;
import com.flocktamer.model.childmodel.Feed;
import com.flocktamer.model.childmodel.TwitterUser;
import com.flocktamer.ui.AddAccountActivity;
import com.flocktamer.ui.ComposeActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Keys;
import com.flocktamer.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterAccountListFragment extends BaseFragment implements Callback<ResponseBody>, KnownSelectedItemComposeInterface, View.OnClickListener {


    public TwitterAccountListFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.ctv_twitter_user_selectall)
    AppCompatCheckedTextView mSelectAll;
    Dialog dialog;
    private RecyclerView mListUsers;
    private LinearLayoutManager mLinearLayoutManager;

    private TwitterUserComposeAdapter mUserAdapter;
    private int clickType;
    private Feed feedData;
    private int mSelectedPosition = -1;
    private KnownSelectedItemComposeInterface knownSelectedItemComposeInterface;
    private Button mContinueBtn;
    private boolean isSelected;
    private boolean isTwitterAccountLinked;

    public static Fragment newInstance(Activity activity) {
        Fragment fragment = new TwitterAccountListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        FlockTamerLogger.createLog("11111111");
        mListUsers = (RecyclerView) view.findViewById(R.id.rv_twitter_users);
        mContinueBtn = (Button) view.findViewById(R.id.btn_twitter_accounts_post);
        mContinueBtn.setOnClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        knownSelectedItemComposeInterface = this;
        mListUsers.setLayoutManager(mLinearLayoutManager);
        getTwitterAccounts();
        return view;
    }

    private ArrayList<TwitterUser> twitterAccounts = new ArrayList<TwitterUser>(

    );

    private void getTwitterAccounts() {
        Call<TwitterUserModel> service = RetrofitBuilder.getService().getTwitterAccounts(getAppPref().getAccessToken());

        Dialog dialogLocal = null;

        dialogLocal = showProgress();

        twitterAccounts.clear();
        final Dialog finalDialogLocal = dialogLocal;
        service.enqueue(new Callback<TwitterUserModel>() {
                            @Override
                            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
                                if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
                                    finalDialogLocal.dismiss();
                                }
                                if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                                    isTwitterAccountLinked = true;
                                    twitterAccounts = response.body().getData();

                                    FlockTamerLogger.createLog("Size:::::: " + twitterAccounts.size());

                                    if (twitterAccounts.size() > 0) {

                                        mUserAdapter = new TwitterUserComposeAdapter(twitterAccounts, false, null, true, knownSelectedItemComposeInterface);
                                        mListUsers.setAdapter(mUserAdapter);
                                    } else {
                                    }

                                  /*      Intent intent = new Intent(getBaseActivity(), TwitterUsersActivity.class);
                                        intent.putParcelableArrayListExtra(Constants.TWITTER_USERS, twitterAccounts);
                                        intent.putExtra(Constants.TWITTER_FEED, feed);
                                        intent.putExtra(Constants.TWITTER_CLICK_TYPE, clickCode);
                                        if (mColumn.getColType().equalsIgnoreCase("1")) {
                                            intent.putExtra(Constants.TWITTER_IS_MY_TWEET, true);
                                        } else {
                                            intent.putExtra(Constants.TWITTER_IS_MY_TWEET, false);
                                        }
                                        startActivityForResult(intent, ACCOUNT_SELECT_INTENT);*/

                                   /*     JsonArray arr = new JsonArray();
                                        arr.add(twitterAccounts.get(0).getId());
                                        if (clickCode == Constants.CLICK_FAVORITE) {

                                            if (feed.getIsLiked().equalsIgnoreCase("true")) {
                                                feed.setIsLiked("false");

                                            } else {
                                                feed.setIsLiked("true");
                                            }
                                            mTweetsAdapter.notifyItemChanged(mFeeds.indexOf(feed));

                                            HashMap<String, Object> dataFavorite = new HashMap<>();
                                            dataFavorite.put("twitter_account_id", arr);
                                            dataFavorite.put("twitter_feed_id", feed.getTwitterFeedId());
                                            callFavoriteApi(feed, dataFavorite);
                                        } else {
                                            showDialogForTweet(clickCode, feed, arr);
                                        }
                                    }*/
                                } else {
                                    isTwitterAccountLinked = false;
                                    FlockTamerLogger.createLog("Failure:: ");
                                    if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
                                        finalDialogLocal.dismiss();
                                    }
                                    callAddtwitterAccountDialog();

                                   /* if (clickCode == Constants.CLICK_FAVORITE) {

                                        if (feed.getIsLiked().equalsIgnoreCase("true")) {
                                            feed.setIsLiked("false");

                                        } else {
                                            feed.setIsLiked("true");
                                        }
                                        mTweetsAdapter.notifyItemChanged(mFeeds.indexOf(feed));
                                        JsonArray arr = new JsonArray();
                                        arr.add("0");

                                        HashMap<String, Object> dataFavorite = new HashMap<>();
                                        dataFavorite.put("twitter_account_id", arr);
                                        dataFavorite.put("twitter_feed_id", feed.getTwitterFeedId());
                                        callFavoriteApi(feed, dataFavorite);
                                    } else {
                                        showNoTwitterAccountDialog(clickCode, feed);
                                    }*/
                                }
                            }

                            @Override
                            public void onFailure(Call<TwitterUserModel> call, Throwable t) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                isTwitterAccountLinked = false;
//                                Util.showErrorSnackBar(getView(), t.getMessage());
                            }
                        }
        );
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    @Override
    public void getPositionOfSelectedItem(int position, boolean isSelected) {
        mSelectedPosition = position;
        this.isSelected = isSelected;
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.btn_twitter_accounts_post:
                if (isTwitterAccountLinked) {


                    if (isSelected) {

                        Intent intent = new Intent(getActivity(), ComposeActivity.class);

                        FlockTamerLogger.createLog("Position: " + mSelectedPosition);
                        FlockTamerLogger.createLog("Id : " + twitterAccounts.get(mSelectedPosition).getTwitterUserId() + "   \n Position:: " + mSelectedPosition);
                        intent.putExtra(Keys.TWITTER_ID, twitterAccounts.get(mSelectedPosition).getTwitterUserId());
                        intent.putExtra(Keys.USER_ID, twitterAccounts.get(mSelectedPosition).getUserId());
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    }
                } else {
                    callAddtwitterAccountDialog();
                }
        }
    }

    private void callAddtwitterAccountDialog() {

        DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                if (which == -1) {
                    Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        };

        Util.onCreateDialog(getActivity(), onclick, getString(R.string.app_name), getString(R.string.please_add_a_twitter_account), getString(R.string.ok), getString(R.string.cancel));


    }

}
