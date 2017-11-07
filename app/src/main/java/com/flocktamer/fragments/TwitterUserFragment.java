package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.flocktamer.R;
import com.flocktamer.adapters.TwitterUserAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.childmodel.Feed;
import com.flocktamer.model.childmodel.TwitterUser;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.google.gson.JsonArray;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment to show List of Added Twitter accounts.
 * Add new Twitter Account.
 */
public class TwitterUserFragment extends BaseFragment implements Callback<ResponseBody> {

    private static boolean mIsMyTweet;
    @BindView(R.id.rv_twitter_users)
    RecyclerView mUsersList;
    @BindView(R.id.ctv_twitter_user_selectall)
    AppCompatCheckedTextView mSelectAll;
    Dialog dialog;
    private List<TwitterUser> mTwitterAccount;
    private TwitterUserAdapter mUserAdapter;
    private int clickType;
    private Feed feedData;

    public TwitterUserFragment() {
        // Required empty public constructor
    }

    public static TwitterUserFragment newInstance(List<TwitterUser> twitterAccounts, Feed feed, int clickCode, boolean isMyTweets) {
        TwitterUserFragment fragment = new TwitterUserFragment();
        fragment.mTwitterAccount = twitterAccounts;
        fragment.feedData = feed;
        fragment.clickType = clickCode;
        mIsMyTweet = isMyTweets;
        return fragment;
    }

    @Override
    protected void initViews(View view) {
        mUserAdapter = new TwitterUserAdapter(mTwitterAccount, false, null,false);
        mUsersList.setAdapter(mUserAdapter);
        mUsersList.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_twitter_user, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    @OnClick(R.id.ctv_twitter_user_selectall)
    void onSelectAllClick() {
        mSelectAll.setChecked(!mSelectAll.isChecked());

        for (int i = 0; i < mTwitterAccount.size(); i++) {
            mTwitterAccount.get(i).setChecked(mSelectAll.isChecked());
        }
        mUserAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_twitter_accounts_post)
    void onContinueClick() {
        if (clickType == Constants.CLICK_FAVORITE) {
            apiCall("");
        } else {
            final AppCompatDialog dialog = new AppCompatDialog(getBaseActivity());
            dialog.setContentView(R.layout.view_tweet_post);
            dialog.setTitle("Tweet Confirmation");
            dialog.setCanceledOnTouchOutside(false);
            ImageView profile = (ImageView) dialog.findViewById(R.id.iv_tweet_profile);
            CustomTextView mName = (CustomTextView) dialog.findViewById(R.id.tv_tweet_username);
            CustomTextView mScreenName = (CustomTextView) dialog.findViewById(R.id.tv_tweet_userid);
            CustomTextView mDetailText = (CustomTextView) dialog.findViewById(R.id.tv_tweeter_maintext);
            CustomTextView mTitleText = (CustomTextView) dialog.findViewById(R.id.tv_twitter_dialog_title);
            final EditText retweetText = (EditText) dialog.findViewById(R.id.et_twitter_dialog_comment);
            final EditText replyText = (EditText) dialog.findViewById(R.id.et_twitter_dialog_reply);
            RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rg_retweet);
            radioGroup.check(radioGroup.getChildAt(0).getId());
            retweetText.setVisibility(View.GONE);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int id = radioGroup.indexOfChild(radioButton);
                    if (id == 0) {
                        retweetText.setVisibility(View.GONE);
                    } else {
                        retweetText.setVisibility(View.VISIBLE);
                    }
                }
            });


            AppCompatButton btnOk = (AppCompatButton) dialog.findViewById(R.id.btn_tweeter_post_dialog_ok);
            if (clickType == Constants.CLICK_FAVORITE) {
                btnOk.setText("Like");
                mTitleText.setText("Like this Tweet?");
            } else if (clickType == Constants.CLICK_RETWEET) {
                btnOk.setText("Retweet");
                mTitleText.setText("Retweet to your followers?");
                retweetText.setVisibility(View.GONE);
                radioGroup.setVisibility(View.VISIBLE);

            } else if (clickType == Constants.CLICK_REPLY) {
                btnOk.setText("Tweet");
                mTitleText.setText("Twitter Reply");
                replyText.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.GONE);

            }
            ImageView btnCancel = (ImageView) dialog.findViewById(R.id.btn_tweeter_post_dialog_cancel);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
                    .cacheInMemory(true)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();

            if (feedData.getTwitterRetweetUserName() != null && !feedData.getTwitterRetweetUserName().trim().isEmpty()) {
                mName.setText(feedData.getTwitterRetweetUserName());
                mScreenName.setText("@" + feedData.getTwitterRetweetUserScreenName());
                mDetailText.setText(feedData.getTwitterRetweetText());
                replyText.setText("@" + feedData.getTwitterRetweetUserScreenName() + " " + "@" + feedData.getTwitterUserScreenName());
                replyText.setSelection(replyText.getText().length());
                imageLoader.displayImage(feedData.getTwitterRetweetProfileImageUrl(), profile, options);

            } else {
//            mRetweetUser.setVisibility(View.GONE);
                mName.setText(feedData.getTwitterUserName());
                mScreenName.setText("@" + feedData.getTwitterUserScreenName());
                mDetailText.setText(feedData.getTwitterText());
                replyText.setText("@" + feedData.getTwitterUserScreenName());
                replyText.setSelection(replyText.getText().length());
                imageLoader.displayImage(feedData.getTwitterUserProfileImageUrl(), profile, options);
            }

            Linkify.addLinks(mDetailText, Linkify.WEB_URLS);

            btnOk.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             String quote = "";
                                             if (clickType == Constants.CLICK_RETWEET) {
                                                 quote = retweetText.getText().toString().trim();
                                             } else if (clickType == Constants.CLICK_REPLY) {
                                                 quote = replyText.getText().toString().trim();
                                             }
                                             apiCall(quote);
                                             dialog.dismiss();
                                         }
                                     }

            );

            btnCancel.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View view) {
                                                 dialog.cancel();
                                             }
                                         }

            );

            dialog.show();
        }
    }


    private void apiCall(String quote) {
        JsonArray arr = mUserAdapter.getCheckedList();
        if (arr.size() > 0) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("twitter_account_id", arr);
            data.put("twitter_feed_id", feedData.getTwitterFeedId());
            data.put("is_myconversation", mIsMyTweet + "");

            if (clickType == Constants.CLICK_FAVORITE) {
                callFavoriteApi(data);
            } else if (clickType == Constants.CLICK_RETWEET) {
                data.put("twitter_user_screen_name", feedData.getTwitterUserScreenName());
                data.put("type", "retweet");
                data.put("content", quote);
                callRetweetAPI(data);
            } else if (clickType == Constants.CLICK_REPLY) {
                if (!quote.isEmpty()) {
                    data.put("content", quote);
                    callReplyApi(data);
                } else {
                    Util.showToast(getBaseActivity(), "Can't post empty tweet");
                }
            }
        }
    }

    private void callRetweetAPI(HashMap<String, Object> data) {
        dialog = getBaseActivity().showProgress();
        Call<ResponseBody> service = RetrofitBuilder.getService().retweet(getAppPref().getAccessToken(), data);
        service.enqueue(this);
    }

    private void callFavoriteApi(HashMap<String, Object> data) {
        dialog = getBaseActivity().showProgress();
        Call<ResponseBody> service = RetrofitBuilder.getService().likeTweet(getAppPref().getAccessToken(), data);
        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    try {
                        String respo = response.body().string();
                        JSONObject obj = new JSONObject(respo);
                        if (Constants.STATUS_SUCCESS.equalsIgnoreCase(obj.getString("status"))) {
                            feedData.setIsLiked("true");
                            Intent intent = new Intent();
                            intent.putExtra("feed", feedData);
                            getBaseActivity().setResult(RESULT_OK, intent);
                        }
                        getBaseActivity().finish();
//                        Util.showToast(getBaseActivity(), obj.getString("message"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    getBaseActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    void callReplyApi(HashMap<String, Object> data) {
        dialog = getBaseActivity().showProgress();
        Call<ResponseBody> service = RetrofitBuilder.getService().replyTweet(getAppPref().getAccessToken(), data);
        service.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (response.code() == 200) {
            try {
                String respo = response.body().string();
                JSONObject obj = new JSONObject(respo);
                Util.showToast(getBaseActivity(), obj.getString("message"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getBaseActivity().onBackPressed();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
