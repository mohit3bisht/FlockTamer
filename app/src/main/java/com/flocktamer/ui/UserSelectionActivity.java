package com.flocktamer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.adapters.NavigationAdapter;
import com.flocktamer.adapters.TabsPagerAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.fragments.DefaultTabFragment;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.http.RetrofitService;
import com.flocktamer.interfaces.TwitterItemClickListener;
import com.flocktamer.model.BaseModel;
import com.flocktamer.model.childmodel.Column;
import com.flocktamer.model.childmodel.Feed;
import com.flocktamer.utils.AppPreference;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.twitter.sdk.android.Twitter;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Home Activity to Display Data with colorful tabs
 */
public class UserSelectionActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, TwitterItemClickListener {

    @BindView(R.id.tabLayout)
    TabLayout tabs;
    @BindView(R.id.vp_tabs)
    ViewPager mViewPager;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCordinatorLayout;
    @BindView(R.id.view_bottom_tablayout)
    View tabsBottom;
    @BindView(R.id.rv_navigation)
    RecyclerView mNavigation;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.iv_menu_icon)
    ImageView menuIcon;
    @BindView(R.id.iv_compose_icon)
    ImageView composeIcon;

    private ArrayList<Column> mColumns;
    private TabsPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        setMyContentView(R.layout.activity_user_selection);
        ButterKnife.bind(this);
        initViews();
        logUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpNavigation();

      /*  Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showNotificationDialog("");
            }
        }, 4000);*/
    }

    @Override
    protected void initViews() {
        if (getIntent().getExtras() != null) {
            mColumns = getIntent().getExtras().getParcelableArrayList(Constants.COLUMN_LIST);
        }
        setupViewPager();

        try {
            mCordinatorLayout.setBackgroundColor(Color.parseColor(mColumns.get(tabs.getSelectedTabPosition()).getMainDivColour()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        composeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSelectionActivity.this, TwitterAccountListActivity.class);

                startActivity(intent);

            }
        });
    }

    /**
     * Navigation initialization
     */
    private void setUpNavigation() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mNavigation.setLayoutManager(manager);

        mNavigation.setAdapter(new NavigationAdapter(getAppPref().getFirstName(), new NavigationAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mDrawerLayout.closeDrawers();
                navigationItemClick(position);
            }
        }));
    }

    /**
     * side Slide menu Click position's action.
     *
     * @param position
     */
 /*   private void navigationItemClick(int position) {
        switch (position) {
            case 1:
                break;
       *//*     case 2:
                Intent intent = new Intent(UserSelectionActivity.this, TwitterAccountListActivity.class);

                startActivity(intent);

                break;*//*

            case 2:
                Intent intentDashboard = new Intent(UserSelectionActivity.this, DashboardActivity.class);
                intentDashboard.putExtra("isFromNavigation", true);
                startActivity(intentDashboard);
                break;

            case 3:
                Intent intent = new Intent(UserSelectionActivity.this, ManageSkimActivity.class);
                startActivity(intent);

                break;
            case 4:
                start(SortActivity.class);
                break;
            case 5:
                start(AddAccountActivity.class);
                break;
            case 6:
                Intent editProfile = new Intent(this, TopicsActivity.class);
                editProfile.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_EDIT_PROFILE);
                startActivity(editProfile);
                break;
            case 7:
                Intent topics = new Intent(this, TopicsActivity.class);
                topics.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_TOPICS_FRAGMENT);
                startActivity(topics);
                break;
            case 8:
                Intent faq = new Intent(this, TopicsActivity.class);
                faq.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_FAQ_FRAGMENT);
                startActivity(faq);
                break;
            case 9:
                start(MeetEditorActivity.class);
//                Intent about = new Intent(this, TopicsActivity.class);
//                about.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_ABOUT_US);
//                startActivity(about);
                break;
            case 10:
                start(SuggestionActivity.class);
                break;

            case 11:
                start(ContactUsActivity.class);
                break;

            case 12:
                callLogout();
                break;

            case Constants.CLICK_CLOSE_NAVIGATION:
                mDrawerLayout.closeDrawers();
                break;
            default:
                // do nothing
                break;
        } */
        //custom news hidden
        private void navigationItemClick(int position) {
        switch (position) {
            case 1:
                break;
       /*     case 2:
                Intent intent = new Intent(UserSelectionActivity.this, TwitterAccountListActivity.class);

                startActivity(intent);

                break;*/

            case 2:
                Intent intentDashboard = new Intent(UserSelectionActivity.this, DashboardActivity.class);
                intentDashboard.putExtra("isFromNavigation", true);
                startActivity(intentDashboard);
                break;

           /* case 3:
                Intent intent = new Intent(UserSelectionActivity.this, ManageSkimActivity.class);
                startActivity(intent);

                break;*/
            case 3:
                start(SortActivity.class);
                break;
            case 4:
                start(AddAccountActivity.class);
                break;
            case 5:
                Intent editProfile = new Intent(this, TopicsActivity.class);
                editProfile.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_EDIT_PROFILE);
                startActivity(editProfile);
                break;
            case 6:
                Intent topics = new Intent(this, TopicsActivity.class);
                topics.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_TOPICS_FRAGMENT);
                startActivity(topics);
                break;
            case 7:
                Intent faq = new Intent(this, TopicsActivity.class);
                faq.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_FAQ_FRAGMENT);
                startActivity(faq);
                break;
            case 8:
                start(MeetEditorActivity.class);
//                Intent about = new Intent(this, TopicsActivity.class);
//                about.putExtra(Constants.TOPIC_ACTIVITY_FRAGMENT, Constants.OPEN_ABOUT_US);
//                startActivity(about);
                break;
            case 9:
                start(SuggestionActivity.class);
                break;

            case 10:
                start(ContactUsActivity.class);
                break;

            case 11:
                callLogout();
                break;

            case Constants.CLICK_CLOSE_NAVIGATION:
                mDrawerLayout.closeDrawers();
                break;
            default:
                // do nothing
                break;
        }
    }

    private void callLogout() {

        RetrofitService service = RetrofitBuilder.getService();
        service.logout(getAppPref().getAccessToken()).enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
//               {"status":"success","message":"You've logged-out successfully."}

                if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                    Util.showErrorSnackBar(null, response.body().getMessage());

//                    if (getAppPref().isUserLogin()) {
                    getAppPref().clearSession();
                    LoginManager.getInstance().logOut();
                    Twitter.logOut();
                    Intent intent = new Intent(UserSelectionActivity.this, WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
//                    }
                } else {
                    Util.showErrorSnackBar(null, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    Util.noInternetAlert(UserSelectionActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            UserSelectionActivity.this.finish();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void initManagers() {
    }

    /**
     * setUp View Pager with Tabs
     */
    private void setupViewPager() {
        adapter = new TabsPagerAdapter(getFragmentManager());
        for (int i = 0; i < mColumns
                .size(); i++) {
            adapter.addFragment(DefaultTabFragment.newInstance(mColumns.get(i)), "");
        }
        mViewPager.setOffscreenPageLimit(100);
        mViewPager.setAdapter(adapter);

        tabs.setSelectedTabIndicatorHeight(0);
        tabs.setupWithViewPager(mViewPager);
        tabs.addOnTabSelectedListener(this);

        /*
        check if Tabs count is less then 5 then change mode of Tabs.
         */
        if (tabs.getTabCount() >= 4) {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabs.setTabMode(TabLayout.MODE_FIXED);
        }

        int tabCount = tabs.getTabCount();
        int currentTab = tabs.getSelectedTabPosition();

        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            View tabView = ((ViewGroup) tabs.getChildAt(0)).getChildAt(i);
            tabView.requestLayout();

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
            if (i != currentTab) {
                p.setMargins(-1, 10, -1, -1);
            } else {
                p.setMargins(-1, -1, -1, -1);
            }

            View view = LayoutInflater.from(this).inflate(R.layout.blue, null);
            CustomTextView tv = (CustomTextView) view.findViewById(R.id.tv_tabtitle);

            tv.setTextColor(Color.parseColor(mColumns.get(i).getH2DivColour()));
            CardView background = (CardView) view.findViewById(R.id.cv_tab_bg);
            background.setCardBackgroundColor(Color.parseColor(mColumns.get(i).getMainDivColour()));

            if (tabs.getTabCount() >= 4) {
                String[] split = mColumns.get(i).getColumnName().split("\\s+");
                StringBuffer text = new StringBuffer();
                /*
                check space of Tabs title and replace 1st space with new line.
                 */
                for (int k = 0; k < split.length; k++) {
                    if (k == 1) {
                        text.append("\n");
                    } else {
                        text.append(" ");
                    }
                    text.append(split[k]);
                }
                tv.setText(text.toString().trim());
            } else {
                tv.setText(mColumns.get(i).getColumnName());
                background.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            tab.setCustomView(view);
            tab.setText(i + "");
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tabSelected) {
        mCordinatorLayout.setBackgroundColor(Color.parseColor(mColumns.get(tabSelected.getPosition()).getMainDivColour()));
        tabsBottom.setBackgroundColor(Color.parseColor(mColumns.get(tabSelected.getPosition()).getMainDivColour()));

        int tabCount = tabs.getTabCount();
        for (int i = 0; i < tabCount; i++) {

            View tabView = ((ViewGroup) tabs.getChildAt(0)).getChildAt(i);
            tabView.requestLayout();

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
            if (i != tabSelected.getPosition()) {
                p.setMargins(-1, 10, -1, -1);
            } else {
                p.setMargins(-1, -1, -1, -1);
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.e("Tab Reslected", "tre");
        DefaultTabFragment fragment = adapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.onReSelectTab();
        }
    }

    /**
     * method to recall API when user click on Refresh button from Toolbar
     */
    @OnClick(R.id.iv_userselection_reload)
    void onRefreshClick() {
        if (mColumns.size() > 0) {
            DefaultTabFragment fragment = adapter.getItem(mViewPager.getCurrentItem());
            fragment.refreshCalled();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        DefaultTabFragment fragment = adapter.getItem(mViewPager.getCurrentItem());
        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private void logUser() {
        // You can call any combination of these three methods
        Crashlytics.setUserEmail(AppPreference.newInstance(this).getUserEmail());
        Crashlytics.setUserName(AppPreference.newInstance(this).getFirstName());
    }


//    public void showNotificationDialog(String id) {
//
//        final ImageLoader imageLoader = ImageLoader.getInstance();
//
//        HashMap<String, String> data = new HashMap<>();
//        data.put("twitter_feed_id", "855015652347830273");
//        new RetrofitBuilder().getService().tweetByFeedId(getAppPref().getAccessToken(), data).enqueue(new Callback<TwitterFeedModel>() {
//            @Override
//            public void onResponse(Call<TwitterFeedModel> call, Response<TwitterFeedModel> response) {
//
////                Log.e("Data", response.body().getData());
//
//                final Feed feedData = response.body().getData().getFeeds().get(0);
//                final Dialog dialog = new Dialog(UserSelectionActivity.this);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.view_tweet_item);
//
//                ImageView mProfile = (ImageView) dialog.findViewById(R.id.iv_tweet_profile);
//                ImageView mReplyBtn = (ImageView) dialog.findViewById(R.id.iv_twitter_reply);
//                final ImageView mTweetImage = (ImageView) dialog.findViewById(R.id.iv_tweet_image);
//                ImageView mEmailBtn = (ImageView) dialog.findViewById(R.id.iv_twitter_mail);
//                ImageView mFavoriteBtn = (ImageView) dialog.findViewById(R.id.iv_twitter_favorite);
//                ImageView mRetweebtn = (ImageView) dialog.findViewById(R.id.iv_twitter_retweet);
//                final ImageView mQuoteImage = (ImageView) dialog.findViewById(R.id.iv_quote_image);
//                final ImageView mQuoteProfileImage = (ImageView) dialog.findViewById(R.id.iv_quote_profile_image);
//
//                CardView mQuoteCard = (CardView) dialog.findViewById(R.id.cv_quote);
//
//
//                CustomTextView mName = (CustomTextView) dialog.findViewById(R.id.tv_tweet_username);
//
//                CustomTextView mRetweetUser = (CustomTextView) dialog.findViewById(R.id.tv_retweeted_username);
//                CustomTextView mTimeAgo = (CustomTextView) dialog.findViewById(R.id.tv_twitter_time_ago);
//                CustomTextView mScreenName = (CustomTextView) dialog.findViewById(R.id.tv_tweet_userid);
//                CustomTextView mDetailText = (CustomTextView) dialog.findViewById(R.id.tv_tweeter_maintext);
//                CustomTextView mQuoteName = (CustomTextView) dialog.findViewById(R.id.tv_quote_name);
//                CustomTextView mQuoteScreenName = (CustomTextView) dialog.findViewById(R.id.tv_quote_screen_name);
//                CustomTextView mQuoteDetail = (CustomTextView) dialog.findViewById(R.id.tv_quote_detail);
//
//                if ("true".equalsIgnoreCase(feedData.getIsLiked())) {
//                    mFavoriteBtn.setImageResource(R.drawable.ic_favorite_red);
////            DrawableCompat.setTint(tabsHolder.mFavoriteBtn.getDrawable(), Color.RED);
//                } else {
//                    mFavoriteBtn.setImageResource(R.drawable.ic_favorite);
////            DrawableCompat.setTint(tabsHolder.mFavoriteBtn.getDrawable(), Color.DKGRAY);
//                }
//
//                mTimeAgo.setText(getTimesAgo(feedData.getTwitterCreatedAt()));
//                mTweetImage.setImageBitmap(null);
//
//                String screenName = "";
//                String imageClickUrl = "";
//                if (feedData.getTwitterRetweetUserName() != null && !feedData.getTwitterRetweetUserName().trim().isEmpty()) {
//                    mName.setText(feedData.getTwitterRetweetUserName());
//                    screenName = feedData.getTwitterRetweetUserScreenName();
//
//                    imageClickUrl = feedData.getTwitterRetweetTweetUrl();
//
//                    mDetailText.setText(setTweetText(feedData.getTwitterRetweetText()));
//
//                    mRetweetUser.setVisibility(View.VISIBLE);
//                    mRetweetUser.setText(feedData.getTwitterUserName() + " Retweeted");
//                    imageLoader.displayImage(feedData.getTwitterRetweetProfileImageUrl(), mProfile, getDisplayImageOption());
//
//                } else {
//                    screenName = feedData.getTwitterUserScreenName();
//                    mRetweetUser.setVisibility(View.GONE);
//                    mName.setText(feedData.getTwitterUserName());
//                    imageClickUrl = feedData.getTwitterTweetUrl();
//
//                    mDetailText.setText(setTweetText(feedData.getTwitterText()));
//                    mDetailText.setMovementMethod(LinkMovementMethod.getInstance());
//
//                    imageLoader.displayImage(feedData.getTwitterUserProfileImageUrl(), mProfile, getDisplayImageOption());
//                }
//
//                mScreenName.setText(getUnderLineScreenName(screenName));
//
//                mDetailText.setMovementMethod(LinkMovementMethod.getInstance());
//                Linkify.addLinks(mDetailText, Linkify.WEB_URLS);
//
//
//                // Check Images url array. If there is some image URL then show first image from Array
//                if (feedData.getTwitterMediaUrl() != null && !feedData.getTwitterMediaUrl().isEmpty()) {
//                    mTweetImage.setVisibility(View.VISIBLE);
//                    try {
//
//                        mTweetImage.getLayoutParams().height = Integer.valueOf(feedData.getImageHeight());
//                        mTweetImage.getLayoutParams().width = Integer.valueOf(feedData.getImageWidth());
//
//                    } catch (Exception e) {
//
//                    }
//
//                    String[] imageLink = feedData.getTwitterMediaUrl().split(",");
//
//                    imageLoader.displayImage(imageLink[0], mTweetImage, new DisplayImageOptions.Builder().cacheInMemory(true).build(), new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                        tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
//                            mTweetImage.setImageBitmap(loadedImage);
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//                        }
//                    });
//                } else {
//                    mTweetImage.setVisibility(View.GONE);
//                }
//
//                if (!feedData.getTwitterQuoteUserName().isEmpty()) {
//                    mQuoteCard.setVisibility(View.VISIBLE);
//                    mQuoteImage.setVisibility(View.GONE);
//                    if (!feedData.getTwitterQuoteMediaUrl().isEmpty()) {
//                        String[] mediaLink = feedData.getTwitterQuoteMediaUrl().split(",");
//                        imageLoader.displayImage(mediaLink[0], mQuoteImage, new ImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                        tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
//                                mQuoteImage.setVisibility(View.VISIBLE);
//                                mQuoteImage.setImageBitmap(loadedImage);
//                            }
//
//                            @Override
//                            public void onLoadingCancelled(String imageUri, View view) {
//                            }
//                        });
//                    }
//
//                    mQuoteName.setText(feedData.getTwitterQuoteUserName());
//
//                    imageLoader.displayImage(feedData.getTwitterQuoteProfileImageUrl(), mQuoteProfileImage, new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                        tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
//                            mQuoteProfileImage.setImageBitmap(loadedImage);
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//                        }
//                    });
//
//                    mQuoteScreenName.setText(getUnderLineScreenName(feedData.getTwitterQuoteUserScreenName()));
//                    mQuoteDetail.setText(setTweetText(feedData.getTwitterQuoteText()));
//                    mQuoteDetail.setMovementMethod(LinkMovementMethod.getInstance());
//                    Linkify.addLinks(mQuoteDetail, Linkify.WEB_URLS);
//                } else {
//                    mQuoteCard.setVisibility(View.GONE);
//                }
//
//                final String finalImageClickUrl = imageClickUrl;
//
//                mTweetImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                Log.e("Image Url", finalImageClickUrl);
//                        if (finalImageClickUrl != null && !finalImageClickUrl.isEmpty()) {
//                            Uri url = Uri.parse(finalImageClickUrl);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, url);
//
//                            if (intent.resolveActivity(getPackageManager()) != null)
//                                startActivity(intent);
//                        }
//
//                    }
//                });
//
//                mRetweebtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onRetweetClick(feedData);
//                    }
//                });
//                mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////
//                        onFavoriteClick(feedData);
//                    }
//                });
//                mReplyBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onReplyClick(feedData);
//                    }
//                });
//                final String finalScreenName = screenName;
//                mScreenName.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onProfileClick(finalScreenName);
//                    }
//                });
//                mName.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onProfileClick(finalScreenName);
//                    }
//                });
//                mEmailBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onEmailClick(feedData);
//                    }
//                });
//
//                mProfile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onProfileClick(finalScreenName);
//                    }
//                });
//
//                View.OnClickListener quoteUserListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onProfileClick(feedData.getTwitterQuoteUserScreenName());
//                    }
//                };
//
//                mQuoteImage.setOnClickListener(quoteUserListener);
//                mQuoteName.setOnClickListener(quoteUserListener);
//                mQuoteScreenName.setOnClickListener(quoteUserListener);
//
//
//                //dialog.show();
//
//
////                    JSONObject obj = new JSONObject(response.body().string());
//
//            }
//
//            @Override
//            public void onFailure(Call<TwitterFeedModel> call, Throwable t) {
//
//            }
//        });
//
//
//    }

    public SpannableStringBuilder setTweetText(String tweetText) {
        String[] splitString = Html.fromHtml(tweetText).toString().split("\\s+");

        SpannableStringBuilder span = new SpannableStringBuilder();
        for (int i = 0; i < splitString.length; i++) {
            final String text = splitString[i];
            span.append(text);

            span.append(" ");
            if (text.startsWith("@")) {
                span.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + text.replace("@", ""))));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + text.replace("@", ""))));
                        }

                    }
                }, span.toString().indexOf(text), span.toString().length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return span;
    }

    private SpannableString getUnderLineScreenName(String screenName) {
        String fullScreenName = "@" + screenName;
        SpannableString content = new SpannableString(fullScreenName);
        content.setSpan(new UnderlineSpan(), 0, fullScreenName.length(), 0);
        return content;
    }

    public String getTimesAgo(String twitter_created_at) {
        try {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE MMM dd kk:mm:ss Z yyyy");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("utc"));

            Date gmtTime = null;
            try {
                gmtTime = dateFormatGmt.parse(dateFormatGmt.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            Log.e("current time", gmtTime.toString());

            Date timemiles = Calendar.getInstance().getTime();
            getDateInMillis(timemiles.toString(), dateFormatGmt);


            DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

            String formattedDate = dateFormatGmt.format(new Date());  // 20120821

            String aa = DateUtils.getRelativeTimeSpanString(getDateInMillis(twitter_created_at, dateFormatGmt), getDateInMillis(formattedDate, dateFormatGmt), DateUtils.MINUTE_IN_MILLIS).toString();
            return aa;

        } catch (Exception r) {
            r.printStackTrace();
            return "";
        }
    }

    public static long getDateInMillis(String srcDate, SimpleDateFormat dateFormatGmt) {

        long dateInMillis = 0;
        try {
            Date date = dateFormatGmt.parse(srcDate.trim());

            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private DisplayImageOptions getDisplayImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
                .cacheInMemory(true)
                .build();

        return options;
    }

    @Override
    public void onFavoriteClick(Feed feed) {

    }

    @Override
    public void onReplyClick(Feed feed) {

    }

    @Override
    public void onRetweetClick(Feed feed) {

    }

    @Override
    public void onProfileClick(String screenName) {

    }

    @Override
    public void onEmailClick(Feed tweetid) {

    }


//    private void getTwitterAccounts(final int clickCode, final Feed feed) {
//        Call<TwitterUserModel> service = RetrofitBuilder.getService().getTwitterAccounts(getAppPref().getAccessToken());
//
//        Dialog dialogLocal = null;
//        if (clickCode != Constants.CLICK_FAVORITE) {
//            dialogLocal = showProgress();
//        }
//
//        final Dialog finalDialogLocal = dialogLocal;
//        service.enqueue(new Callback<TwitterUserModel>() {
//                            @Override
//                            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
//                                if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
//                                    finalDialogLocal.dismiss();
//                                }
//                                if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
//
//                                    ArrayList<TwitterUser> twitterAccounts = response.body().getData();
//                                    if (twitterAccounts.size() > 1) {
//                                        Intent intent = new Intent(UserSelectionActivity.this, TwitterUsersActivity.class);
//                                        intent.putParcelableArrayListExtra(Constants.TWITTER_USERS, twitterAccounts);
//                                        intent.putExtra(Constants.TWITTER_FEED, feed);
//                                        intent.putExtra(Constants.TWITTER_CLICK_TYPE, clickCode);
////                                        if (mColumn.getColType().equalsIgnoreCase("1")) {
////                                            intent.putExtra(Constants.TWITTER_IS_MY_TWEET, true);
////                                        } else {
//                                        intent.putExtra(Constants.TWITTER_IS_MY_TWEET, false);
////                                        }
//                                        startActivityForResult(intent, ACCOUNT_SELECT_INTENT);
//                                    } else if (twitterAccounts.size() == 1) {
//                                        JsonArray arr = new JsonArray();
//                                        arr.add(twitterAccounts.get(0).getId());
//                                        if (clickCode == Constants.CLICK_FAVORITE) {
//
//                                            if (feed.getIsLiked().equalsIgnoreCase("true")) {
//                                                feed.setIsLiked("false");
//
//                                            } else {
//                                                feed.setIsLiked("true");
//                                            }
//
//                                            HashMap<String, Object> dataFavorite = new HashMap<>();
//                                            dataFavorite.put("twitter_account_id", arr);
//                                            dataFavorite.put("twitter_feed_id", feed.getTwitterFeedId());
//                                            callFavoriteApi(feed, dataFavorite);
//                                        } else {
//                                            showDialogForTweet(clickCode, feed, arr);
//                                        }
//                                    }
//                                } else {
//                                    if (clickCode == Constants.CLICK_FAVORITE) {
//
//                                        if (feed.getIsLiked().equalsIgnoreCase("true")) {
//                                            feed.setIsLiked("false");
//
//                                        } else {
//                                            feed.setIsLiked("true");
//                                        }
//
//                                        JsonArray arr = new JsonArray();
//                                        arr.add("0");
//
//                                        HashMap<String, Object> dataFavorite = new HashMap<>();
//                                        dataFavorite.put("twitter_account_id", arr);
//                                        dataFavorite.put("twitter_feed_id", feed.getTwitterFeedId());
//                                        callFavoriteApi(feed, dataFavorite);
//                                    } else {
//                                        showNoTwitterAccountDialog(clickCode, feed);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<TwitterUserModel> call, Throwable t) {
//                                if (dialog != null && dialog.isShowing()) {
//                                    dialog.dismiss();
//                                }
////                                Util.showErrorSnackBar(getView(), t.getMessage());
//                            }
//                        }
//        );
//    }

}
