package com.flocktamer.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.flocktamer.R;
import com.flocktamer.adapters.CompanyTabAdapter;
import com.flocktamer.adapters.TabsAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.interfaces.OnEmailClickPermission;
import com.flocktamer.interfaces.TwitterItemClickListener;
import com.flocktamer.model.CompanyModel;
import com.flocktamer.model.RefreshCompanyModel;
import com.flocktamer.model.TwitterFeedModel;
import com.flocktamer.model.TwitterUserModel;
import com.flocktamer.model.UserModel;
import com.flocktamer.model.childmodel.Column;
import com.flocktamer.model.childmodel.CompanyFeed;
import com.flocktamer.model.childmodel.Feed;
import com.flocktamer.model.childmodel.TwitterUser;
import com.flocktamer.ui.TwitterUsersActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;
import com.flocktamer.utils.Validation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mopub.mobileads.MoPubView;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static android.app.Activity.RESULT_OK;

//import com.google.android.gms.ads.AdView;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link DefaultTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultTabFragment extends BaseFragment implements TwitterItemClickListener, Callback<ResponseBody>, OnEmailClickPermission {

    public static final int ACCOUNT_SELECT_INTENT = 44;
    private RecyclerView mList;

    private View mView;
    @BindView(R.id.tv_load_new_tweets)
    CustomTextView mNewTweetText;
    @BindView(R.id.ll_tab_sponsored_layout)
    LinearLayout sponsoredLayout;
    @BindView(R.id.tv_tab_sponsored_name)
    CustomTextView mSponsoredName;

    LinearLayout mLimitExceed;
    RelativeLayout twitterLoginLayout;

    @BindView(R.id.rl_progress)
    RelativeLayout mLowerProgress;

    @BindView(R.id.iv_bottom_progress)
    ImageView mBottomProgress;
    @BindView(R.id.adView_banner)
    AdView mBannerAd;

    @BindView(R.id.rl_ad_container)
    RelativeLayout adContainer;

    private boolean isFromBackground;

    private Column mColumn;

    //    private ProgressDialog dialog;
    private TwitterAuthClient mTwitterAuthClient;
    private String mAutoRefreshMaxId = "";
    private String mLoadMoreMaxId = "0";
    private List<Feed> mFeeds = new ArrayList<>();
    private List<CompanyFeed> mCompanyFeed = new ArrayList<>();
    private Handler handler;
    private Runnable runnable;
    private TabsAdapter mTweetsAdapter;
    private LinearLayoutManager manager;
    private int limit = 30;
    private int totalItemCount;
    private int pastVisiblesItems;
    private boolean loading;
    private boolean isNotFirstAttenpt;
    private MoPubRecyclerAdapter adAdapter;
    private Feed favoriteFeed;
    private Dialog dialog;
    private com.facebook.ads.AdView adView;
    private Feed mEmailFeed;
    private RecyclerView mStockListView;
    private MoPubView moPubView;

    public DefaultTabFragment() {
        // Required empty public constructor
    }

    public static DefaultTabFragment newInstance(Column column) {
        DefaultTabFragment fragment = new DefaultTabFragment();
        fragment.mColumn = column;

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroy() {
//        adAdapter.destroy();
        super.onDestroy();
        isFromBackground = false;
        moPubView.destroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data", mColumn);
    }

    @Override
    protected void initViews(View view) {
        MobileAds.initialize(getActivity(), "ca-app-pub-1441688703455715~2550028384");
//        mNativeAds = new NativeAdsManager(getBaseActivity(), "1239838622744569_1239838876077877", 5);
//        mNativeAds.loadAds();

//        MMAdViewSDK.logLevel = 2;

        mLimitExceed = (LinearLayout) view.findViewById(R.id.ll_limit_exceed);
        mList = (RecyclerView) view.findViewById(R.id.rv_tabs_list);
        mStockListView = (RecyclerView) view.findViewById(R.id.rv_tabs_stock);
        twitterLoginLayout = (RelativeLayout) view.findViewById(R.id.rl_tab_twitterbtn);

        moPubView = (MoPubView) view.findViewById(R.id.adview_mopub_banner);

        loadBannerAd();
        handler = new Handler();

        if (mColumn.getSponsored() != null && !mColumn.getSponsored().isEmpty()) {
            mSponsoredName.setText(mColumn.getSponsored());
            sponsoredLayout.setVisibility(View.VISIBLE);
            sponsoredLayout.setBackgroundColor(Color.parseColor(mColumn.getSponsoredCompanyColour()));
        } else {
            sponsoredLayout.setVisibility(View.GONE);
        }

        callApi(Constants.API_INIT);
        if (!mColumn.getColType().equalsIgnoreCase("2")) {
            mList.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!mColumn.getId().equalsIgnoreCase("25")) {
                        LinearLayoutManager manager = (LinearLayoutManager) mList.getLayoutManager();
                        if (manager != null) {
                            if (manager.findFirstCompletelyVisibleItemPosition() == 0) {
                                mNewTweetText.setVisibility(View.GONE);
                            }
                        }

                        if (dy > 0 && !mLoadMoreMaxId.equalsIgnoreCase("-1")) //check for scroll down
                        {
                            totalItemCount = manager.getItemCount();
                            pastVisiblesItems = manager.findLastCompletelyVisibleItemPosition();

                            if (mColumn.getColType().equalsIgnoreCase("1")) {
                                return;
                            }
                            if (totalItemCount == pastVisiblesItems + 1) {

                                mLowerProgress.setVisibility(View.VISIBLE);
//                            Animation anim = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.rotate_logo);

                                RotateAnimation rotate = new RotateAnimation(0, 25000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                rotate.setDuration(50000);
                                rotate.setRepeatMode(Animation.INFINITE);
                                rotate.setInterpolator(new LinearInterpolator());

                                mBottomProgress.startAnimation(rotate);
                            }

                            if (!loading) {

                                if (totalItemCount >= 10 && manager.findFirstVisibleItemPosition() == totalItemCount - 7) {
                                    loading = true;
                                    limit = 50;
//                                Log.e("Scroll", "Last Item Wow !");
                                    if (!mColumn.getColType().equalsIgnoreCase("1")) {
                                        callApi(Constants.API_LOADMORE);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * call API for current selected API when fragment is created/ when user press Refresh button from toolbar.
     *
     * @param typeOfApi :- type of API  AutoRefresh/Refresh/LoadMore
     */
    private void callApi(final int typeOfApi) {
        if (mColumn == null) {
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("column_id", mColumn.getId());
        data.put("max_record_id", mLoadMoreMaxId);
        data.put("limit", limit + "");
        FlockTamerLogger.createLog("Params: "+data);
        Dialog dialog = null;
        if (mColumn.getColType().trim().equalsIgnoreCase("2")) {

            Call<CompanyModel> callApi = RetrofitBuilder.getService().companyColumnInfo(getAppPref().getAccessToken(), data);

            if (typeOfApi == Constants.API_REFRESH) {
                dialog = showProgress();
            }


            final Dialog finalDialog = dialog;
            callApi.enqueue(new Callback<CompanyModel>() {
                @Override
                public void onResponse(Call<CompanyModel> call, Response<CompanyModel> response) {
                    if (finalDialog != null) {
                        finalDialog.dismiss();
                    }
                    if (response.code() == 200 && response.body().getData() != null) {
                        mAutoRefreshMaxId = response.body().getData().getMaxId();
                        mCompanyFeed = response.body().getData().getFeeds();
                        CompanyTabAdapter adapter = new CompanyTabAdapter(mCompanyFeed);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                        mList.setAdapter(adapter);
                        mList.setLayoutManager(manager);

                        mStockListView.setVisibility(View.GONE);
                       /* ScrapDataAdapter scrapDataAdapter = new ScrapDataAdapter(response.body().getData().getScrapedData());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                        mStockListView.setAdapter(scrapDataAdapter);
                        mStockListView.setLayoutManager(layoutManager);
*/
                        initHandler();
                    } else if (response.code() == 401) {
                        getBaseActivity().logoutApp();
                    }
                }

                @Override
                public void onFailure(Call<CompanyModel> call, Throwable t) {
                    if (finalDialog != null) {
                        finalDialog.dismiss();
                    }
                }
            });
        } else {
            FlockTamerLogger.createLog("Elseeee");
            mTweetsAdapter = new TabsAdapter(mFeeds, DefaultTabFragment.this, !isFacebookAvailable());

            Call<TwitterFeedModel> callApi = RetrofitBuilder.getService().columnInfo(getAppPref().getAccessToken(), data);
            FlockTamerLogger.createLog("mFeeds size: "+mFeeds.size());
            if (typeOfApi == Constants.API_REFRESH) {
                mFeeds.clear();
                if (mTweetsAdapter != null) {

                    mTweetsAdapter.notifyDataSetChanged();
                }

                limit = 30;
                dialog = showProgress();
            }
            final Dialog finalDialog1 = dialog;
            callApi.enqueue(new Callback<TwitterFeedModel>() {
                @Override
                public void onResponse(Call<TwitterFeedModel> call, Response<TwitterFeedModel> response) {
                    if (finalDialog1 != null) {
                        finalDialog1.dismiss();
                    }

                    if (typeOfApi == Constants.API_LOADMORE) {
                        loading = false;
                        mLowerProgress.setVisibility(View.GONE);
                        Log.e("LoadMore", "OnResponse");
                        mLoadMoreMaxId = "-1";
                    }

                    if (response.code() == 200) {


                        if (response.body().getData() == null || response.body().getData().getFeeds() == null) {
                            return;
                        }

                        if (!response.body().getData().getFeeds().isEmpty()) {
                            manager = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false);
                            if (mList != null) {
                                mList.setLayoutManager(manager);
                            }
                        }


                        if (mColumn.getColType().trim().equalsIgnoreCase("1")) {
                            isNotFirstAttenpt = true;
                            if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_FAIL)) {
                                showTwitterBtn();
                                return;
                            } else if (response.body().getData().getFeeds().isEmpty()) {
                                showLimitExceed();
                                return;
                            }
                        }

                        showList();
                        if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_FAIL)) {
                            loading = false;
//                            Util.showErrorSnackBar(getView(), response.body().getMessage());
                            return;
                        }
                        if (response.body().getData().getFeeds() == null) {
                            loading = false;
                            return;
                        }

                        mAutoRefreshMaxId = response.body().getData().getMaxId();
//                        if (mColumn.getColType().equalsIgnoreCase("0")) {
                        initHandler();
//                        }

                        if (typeOfApi == Constants.API_REFRESH) {
                            if (!response.body().getData().getFeeds().isEmpty()) {
                                try {
                                    mList.stopScroll();
                                    mFeeds.clear();
                                    mFeeds = response.body().getData().getFeeds();
                                    mLoadMoreMaxId = "0";
                                    mTweetsAdapter = new TabsAdapter(mFeeds, DefaultTabFragment.this, !isFacebookAvailable());
                                    mList.setAdapter(mTweetsAdapter);
                                } catch (NullPointerException e) {

                                }
                            }

                        } else if (typeOfApi == Constants.API_LOADMORE) {

                            loading = false;
                            mLowerProgress.setVisibility(View.GONE);
                            List<Feed> newFeeds = response.body().getData().getFeeds();
                            if (response.body().getData().getFeeds().isEmpty()) {
                                mLoadMoreMaxId = "-1";
//                                mTweetsAdapter.notifyDataSetChanged();
//                                mList.scrollToPosition(manager.getItemCount());
                            } else {
                                int feedsize = manager.getItemCount();
                                mFeeds.addAll(newFeeds);
                                mTweetsAdapter.notifyDataSetChanged();
                                mList.scrollToPosition(feedsize - 2);
                                if (mColumn.getColType().equalsIgnoreCase("4")) {
                                    mLoadMoreMaxId = mFeeds.get(mFeeds.size() - 1).getFeedLikeId();
                                } else {
                                    mLoadMoreMaxId = mFeeds.get(mFeeds.size() - 1).getTwitterFeedId();
                                }
//                                limit = mFeeds.size();
                            }


                        } else if (typeOfApi == Constants.API_INIT) {
                            mFeeds.addAll(response.body().getData().getFeeds());
                            if (mFeeds.size() > 0) {
                                if (mColumn.getColType().equalsIgnoreCase("4")) {
                                    mLoadMoreMaxId = mFeeds.get(mFeeds.size() - 1).getFeedLikeId();
                                } else {
                                    mLoadMoreMaxId = mFeeds.get(mFeeds.size() - 1).getTwitterFeedId();
                                }
                            }
//                            limit = limit + 50;

                            //TODO Hooman Account
//                            1130925926972921_1130984680300379
//                            NativeAdsManager ads = new NativeAdsManager(mActivity, "1130925926972921_1130984680300379", 5);
                            //TODO my app Id
//                            AdSettings.addTestDevice("884d66b4905e7e5fb805da2657a34440");

//                            if (mColumn.getColType().equalsIgnoreCase("0"))
                            FlockTamerLogger.createLog("Feed Size:::: " + mFeeds.size());
                            mTweetsAdapter = new TabsAdapter(mFeeds, DefaultTabFragment.this, !isFacebookAvailable());

//                            AdSettings.addTestDevice("c6a39cca79b594a0103618bc96b0c1fd");
//                            MoPubNativeAdPositioning.MoPubClientPositioning adPositioning =
//                                    MoPubNativeAdPositioning.clientPositioning();
//                            adPositioning.addFixedPosition(5);
//                            adPositioning.enableRepeatingPositions(6);

                            //TODO comment code for hiding Mopub Ads.  Uncomment  mList.setAdapter(adAdapter); and comment  mList.setAdapter(mTweetsAdapter); when uncomment this code.
//
                            if (isFacebookAvailable()) {
                                adAdapter = new MoPubRecyclerAdapter(getBaseActivity(), mTweetsAdapter);

                                ViewBinder viewBinder = new ViewBinder.Builder(R.layout.my_ad_view)
                                        .titleId(R.id.native_title)
                                        .textId(R.id.native_text)
                                        .mainImageId(R.id.native_main_image)
                                        .iconImageId(R.id.native_icon_image)
                                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                                        .callToActionId(R.id.native_cta)
                                        .build();

                                adAdapter.registerAdRenderer(new MoPubStaticNativeAdRenderer(viewBinder));
                                RequestParameters parameters = new RequestParameters.Builder().build();

                                adAdapter.loadAds(Constants.AD_ID, parameters);

                                mList.setAdapter(adAdapter);
                            } else {

                                mList.setAdapter(mTweetsAdapter);
                            }
                        }

                    } else if (response.code() == 401) {
                        getBaseActivity().logoutApp();
                    } else {
//                        if (response.body().getMessage() != null)
//                            Util.showErrorSnackBar(getView(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<TwitterFeedModel> call, Throwable t) {
                    Log.e("LoadMore", "OnResponse");

                    if (typeOfApi == Constants.API_LOADMORE) {
                        loading = false;
                        mLowerProgress.setVisibility(View.GONE);
                        Log.e("LoadMore", "OnResponse");
                        mLoadMoreMaxId = "-1";
                    }

                    if (finalDialog1 != null) {
                        finalDialog1.dismiss();
                    }

                }
            });
        }

    }


    /**
     * show Limit Exceed text in My Twitter tab and hide RecyclerView nad Login Twitter button
     */
    private void showLimitExceed() {
        try {
            mLimitExceed.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            Crashlytics.log("showLimitExceed :-" + " Limit Exceed is null");
        }
        try {
            mList.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Crashlytics.log("showLimitExceed :-" + "mList is null");
        }
        try {
            twitterLoginLayout.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Crashlytics.log("showLimitExceed :-" + "Twitter Login is null");
        }
    }

    /**
     * show RecyclerView and hide the other Items from screen like Limit exceed and Login button of Twitter for My Twitter Tab.
     */
    private void showList() {
        try {
            mLimitExceed.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Crashlytics.log("ShowList :-" + " Limit Exceed is null");
        }
        try {
            mList.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            Crashlytics.log("ShowList :-" + "mList is null");
        }
        try {
            twitterLoginLayout.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Crashlytics.log("ShowList :-" + "Twitter Login is null");
        }

    }

    private void showTwitterBtn() {

        try {
            mLimitExceed.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Crashlytics.log("ShowTwitterBtn :-" + " Limit Exceed is null");
        }
        try {
            mList.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Crashlytics.log("ShowTwitterBtn :-" + "mList is null");
        }
        try {
            twitterLoginLayout.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            Crashlytics.log("ShowTwitterBtn :-" + "Twitter Login is null");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab, container, false);
            ButterKnife.bind(this, mView);
            if (savedInstanceState != null) {
                //probably orientation change
                mColumn = savedInstanceState.getParcelable("data");
            }
            initViews(mView);
        }

        return mView;
    }

    @Override
    public void onFavoriteClick(Feed feed) {
        favoriteFeed = feed;
        getTwitterAccounts(Constants.CLICK_FAVORITE, feed);
    }

    @Override
    public void onReplyClick(Feed feed) {
        getTwitterAccounts(Constants.CLICK_REPLY, feed);
    }

    @Override
    public void onRetweetClick(Feed feed) {
        getTwitterAccounts(Constants.CLICK_RETWEET, feed);
    }

    @Override
    public void onProfileClick(String screenName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + screenName)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + screenName)));
        }
    }

    @Override
    public void onEmailClick(final Feed feed) {

        if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            mEmailFeed = feed;
            getBaseActivity().setOnEmailPermissionInstance(this);
            getBaseActivity().requestAppPermissions(new String[]{Manifest.permission.READ_CONTACTS}, R.string.request, 101);


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        final AppCompatDialog dialog = new AppCompatDialog(getBaseActivity());
        dialog.setContentView(R.layout.dialog_send_email);
        dialog.setTitle("SHARE BY EMAIL");
        dialog.setCanceledOnTouchOutside(false);
        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.btn_dialog_close);
        final AutoCompleteTextView email = (AutoCompleteTextView) dialog.findViewById(R.id.et_dialog_email);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseActivity(), android.R.layout.simple_list_item_1, getAccounts());
        email.setAdapter(adapter);

        AppCompatButton cancelBtn = (AppCompatButton) dialog.findViewById(R.id.btn_dialog_cancel);
        AppCompatButton sendBtn = (AppCompatButton) dialog.findViewById(R.id.btn_dialog_send);

        View.OnClickListener cancelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };

        closeBtn.setOnClickListener(cancelClick);
        cancelBtn.setOnClickListener(cancelClick);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);

                String emailText = email.getText().toString().trim();
                if (Validation.isValidEmail(emailText)) {
                    if (mColumn.getColType().equalsIgnoreCase("0")) {
                        dialog.dismiss();
                        callEmailApi(emailText, feed.getId(), false);
                    } else {
                        dialog.dismiss();
                        callEmailApi(emailText, feed.getTwitterFeedId(), true);
                    }
                } else {
                    email.setError("Invalid email address");
                }
            }
        });

        dialog.show();
    }

    /**
     * Method to call APi for sharing Tweet via Email.
     *
     * @param emailText : -email enter by user.
     * @param tweetid   :- Tweet Id which is click by User.
     */
    private void callEmailApi(String emailText, String tweetid, boolean isMyTweet) {
        HashMap<String, String> data = new HashMap<>();
        data.put("email", emailText);
        data.put("twitter_feed_id", tweetid);
        data.put("twitter_type", isMyTweet + "");
        Call<ResponseBody> service = RetrofitBuilder.getService().emailTweet(getAppPref().getAccessToken(), data);

        final Dialog dialog = showProgress();

        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    if (response.body() != null) {
                        String respo = null;
                        try {
                            respo = response.body().string();
                            JSONObject obj = new JSONObject(respo);
                            Util.showToast(getBaseActivity(), obj.getString("message"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }

    private void getTwitterAccounts(final int clickCode, final Feed feed) {
        Call<TwitterUserModel> service = RetrofitBuilder.getService().getTwitterAccounts(getAppPref().getAccessToken());

        Dialog dialogLocal = null;
        if (clickCode != Constants.CLICK_FAVORITE) {
            dialogLocal = showProgress();
        }

        final Dialog finalDialogLocal = dialogLocal;
        service.enqueue(new Callback<TwitterUserModel>() {
                            @Override
                            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
                                if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
                                    finalDialogLocal.dismiss();
                                }
                                if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {

                                    ArrayList<TwitterUser> twitterAccounts = response.body().getData();
                                    if (twitterAccounts.size() > 1) {
                                        Intent intent = new Intent(getBaseActivity(), TwitterUsersActivity.class);
                                        intent.putParcelableArrayListExtra(Constants.TWITTER_USERS, twitterAccounts);
                                        intent.putExtra(Constants.TWITTER_FEED, feed);
                                        intent.putExtra(Constants.TWITTER_CLICK_TYPE, clickCode);
                                        if (mColumn.getColType().equalsIgnoreCase("1")) {
                                            intent.putExtra(Constants.TWITTER_IS_MY_TWEET, true);
                                        } else {
                                            intent.putExtra(Constants.TWITTER_IS_MY_TWEET, false);
                                        }
                                        startActivityForResult(intent, ACCOUNT_SELECT_INTENT);
                                    } else if (twitterAccounts.size() == 1) {
                                        JsonArray arr = new JsonArray();
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
                                    }
                                } else {
                                    if (clickCode == Constants.CLICK_FAVORITE) {

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
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<TwitterUserModel> call, Throwable t) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
//                                Util.showErrorSnackBar(getView(), t.getMessage());
                            }
                        }
        );
    }

    /**
     * show Dialog to user that There is not Twitter account linked with FlockTamer account.
     *
     * @param clickCode
     * @param feed
     */
    private void showNoTwitterAccountDialog(final int clickCode, final Feed feed) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getBaseActivity());

        alertDialogBuilder.setTitle("Sign in with Twitter");
        alertDialogBuilder.setMessage("In order to perform Twitter actions, first login with your Twitter account.");

        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Add Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loginToTwitter(clickCode, feed);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", null);

        if (!(getBaseActivity()).isFinishing()) {
            alertDialogBuilder.show();
            //show dialog
        } else {
            Log.e("error", "activity is finishing");
        }
    }

    private void loginToTwitter(final int clickCode, final Feed feed) {
        mTwitterAuthClient = new TwitterAuthClient();
        mTwitterAuthClient.authorize(getBaseActivity(), new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                final TwitterSession session = result.data;

                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(false, false).enqueue(new com.twitter.sdk.android.core.Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {
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
                        addTwitterAccount(data, clickCode, feed);
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    /**
     * Call API to store login account data.
     * Post Login in Twitter account Database.
     *
     * @param :         information of user
     * @param clickCode
     * @param feed
     */
    private void addTwitterAccount(HashMap<String, String> data, final int clickCode, final Feed feed) {

        final Dialog dialog = showProgress();
        Call<TwitterUserModel> callApi = RetrofitBuilder.getService().addTwitterAccount(getAppPref().getAccessToken(), data);

        callApi.enqueue(new retrofit2.Callback<TwitterUserModel>() {
            @Override
            public void onResponse(Call<TwitterUserModel> call, Response<TwitterUserModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                if (response.code() == 200) {
                    JsonArray arr = new JsonArray();
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {

                        ArrayList<TwitterUser> users = response.body().getData();
                        if (users.size() != 0) {
                            for (int i = 0; i < users.size(); i++) {
                                arr.add(users.get(i).getId());
                            }

                        }
                        if (clickCode == Constants.CLICK_FAVORITE) {
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("twitter_account_id", arr);
                            data.put("twitter_feed_id", feed.getTwitterFeedId());
                            callFavoriteApi(feed, data);
                        } else if (clickCode == Constants.CLICK_LOGIN) {
                            callApi(Constants.API_INIT);
                        } else {
                            showDialogForTweet(clickCode, feed, arr);
                        }

                    } else {
                        Util.showErrorSnackBar(getView(), response.body().getMessage());
                    }

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
                    Util.noInternetAlert(getBaseActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getBaseActivity().finish();
                        }
                    });
                }
            }
        });
    }


    /**
     * Method to show dialog for Tweet when user click on Retweet/Reply button
     *
     * @param clickType : - Click type Tetweet/Reply
     * @param feedData  :- Data of Tweet
     * @param arr       " params to be sent to server
     */
    private void showDialogForTweet(final int clickType, final Feed feedData, final JsonArray arr) {
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
        final LinearLayout twitterDialogComment = (LinearLayout) dialog.findViewById(R.id.ll_twitter_dialog_comment);
        LinearLayout twitterDialogReply = (LinearLayout) dialog.findViewById(R.id.ll_twitter_reply);
        final TextView replyCounter = (TextView) dialog.findViewById(R.id.tv_reply_counter);
        final TextView commentCounter = (TextView) dialog.findViewById(R.id.tv_comment_counter);

        retweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                commentCounter.setText(retweetText.getText().toString().length() + "/80");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        replyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                replyCounter.setText(replyText.getText().toString().length() + "/280");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rg_retweet);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int id = radioGroup.indexOfChild(radioButton);
                if (id == 0) {
                    retweetText.setVisibility(View.GONE);
                    twitterDialogComment.setVisibility(View.GONE);

                } else {
                    retweetText.setVisibility(View.VISIBLE);
                    twitterDialogComment.setVisibility(View.VISIBLE);
                }
            }
        });

        AppCompatButton btnOk = (AppCompatButton) dialog.findViewById(R.id.btn_tweeter_post_dialog_ok);
        if (clickType == Constants.CLICK_RETWEET) {
            btnOk.setText("Retweet");
            mTitleText.setText("Retweet to your followers?");
            radioGroup.setVisibility(View.VISIBLE);

        } else if (clickType == Constants.CLICK_REPLY) {
            btnOk.setText("Tweet");
            mTitleText.setText("Twitter Reply");
            replyText.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            twitterDialogReply.setVisibility(View.VISIBLE);
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
                                         postTweetApi(quote, feedData, clickType, arr);
                                         dialog.dismiss();
                                     }
                                 }

        );

        btnCancel.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             dialog.cancel();
                                         }
                                     }
        );

        dialog.show();
    }


    private void postTweetApi(String quote, Feed feed, int clickType, JsonArray arr) {

        if (arr.size() > 0) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("twitter_account_id", arr);
            data.put("twitter_feed_id", feed.getTwitterFeedId());

            if (clickType == Constants.CLICK_FAVORITE) {
                callFavoriteApi(feed, data);
            } else if (clickType == Constants.CLICK_RETWEET) {
                data.put("twitter_user_screen_name", feed.getTwitterUserScreenName());
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

    /**
     * call for mark Tweet to favorite
     *
     * @param data
     */
    private void callFavoriteApi(final Feed feed, HashMap<String, Object> data) {
//        dialog =  showProgress();

        if (mColumn.getColType().equalsIgnoreCase("1")) {
            data.put("is_myconversation", "true");
        } else {
            data.put("is_myconversation", "false");
        }
        Call<ResponseBody> service = RetrofitBuilder.getService().likeTweet(getAppPref().getAccessToken(), data);
        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String respo = response.body().string();
                        JSONObject obj = new JSONObject(respo);
                        if (Constants.STATUS_SUCCESS.equalsIgnoreCase(obj.getString("status"))) {
//                            feed.setIsLiked("true");
                            mTweetsAdapter.notifyItemChanged(mFeeds.indexOf(feed));
                        }
//                Util.showToast( getBaseActivity(), obj.getString("message"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    getBaseActivity().logoutApp();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * call Reply  API when user click on Reply icon
     *
     * @param data for posted.
     */
    void callReplyApi(HashMap<String, Object> data) {
//        ProgressDialog dialog = showProgress();
        if (mColumn.getColType().equalsIgnoreCase("1")) {
            data.put("is_myconversation", "true");
        } else {
            data.put("is_myconversation", "false");
        }
        Call<ResponseBody> service = RetrofitBuilder.getService().replyTweet(getAppPref().getAccessToken(), data);
        dialog = showProgress();
        service.enqueue(this);
    }

    /**
     * call Retweet API when user click on Retweet icon
     *
     * @param data for posted.
     */
    private void callRetweetAPI(HashMap<String, Object> data) {
        dialog = showProgress();
        if (mColumn.getColType().equalsIgnoreCase("1")) {
            data.put("is_myconversation", "true");
        } else {
            data.put("is_myconversation", "false");
        }
        Call<ResponseBody> service = RetrofitBuilder.getService().retweet(getAppPref().getAccessToken(), data);

        service.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (dialog != null) {
            dialog.dismiss();
        }
//        if (response.code() == 200) {
//            try {
//                String respo = response.body().string();
//                JSONObject obj = new JSONObject(respo);
//                Util.showToast( getBaseActivity(), obj.getString("message"));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * call API of current Fragment to tap of Refresh icon from Toolbar
     */
    public void refreshCalled() {
//        mFeeds.clear();
//        mCompanyFeed.clear();
        mLoadMoreMaxId = "0";

        callApi(Constants.API_REFRESH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCOUNT_SELECT_INTENT) {
            if (resultCode == RESULT_OK) {
                Feed fe = data.getParcelableExtra("feed");
                for (Feed feed : mFeeds) {
                    if (feed.getId().equalsIgnoreCase(fe.getId())) {
                        feed.setIsLiked("true");
                        mTweetsAdapter.notifyItemChanged(mFeeds.indexOf(feed));
                        break;
                    }
                }
            }
        } else {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void callAutoRefreshAPI() {
        if (mAutoRefreshMaxId.isEmpty()) {
            return;
        }
        HashMap<String, String> data = new HashMap<>();
//        if (mColumn.getColType().equalsIgnoreCase("2")) {
//            mAutoRefreshMaxId = (Integer.valueOf(mAutoRefreshMaxId) - 10) + "";
//        }
        data.put("old_max_id", mAutoRefreshMaxId);

        if (!mColumn.getColType().equalsIgnoreCase("2")) {
            data.put("column_id", mColumn.getId());
            data.put("start_limit", "10");
            data.put("type", mColumn.getColType());
            getRefreshTweetList(data);
        } else {
            getRssRefreshFeeds(data);
        }
    }

    private void getRssRefreshFeeds(HashMap<String, String> data) {
        Call<ResponseBody> service = RetrofitBuilder.getService().getRefreshCompanyFeed(getAppPref().getAccessToken(), data);
        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {

                    try {
                        String data = response.body().string();
                        RefreshCompanyModel myObj = new Gson().fromJson(data,
                                new TypeToken<RefreshCompanyModel>() {
                                }.getType());
                        if (myObj.getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                            Map<String, CompanyFeed> feeds = myObj.getData().getFeeds();

                            for (Map.Entry<String, CompanyFeed> item : feeds.entrySet()) {
                                checkAndAddStockFeed(item);
                            }
                            //TODO notify company adapter here
//                            adAdapter.notifyDataSetChanged();
//                            visibleLoadNewFeed();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initHandler();
                } else if (response.code() == 401) {
                    getBaseActivity().logoutApp();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    /**
     *
     */
    private void visibleLoadNewFeed() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) mList.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePosition != 0) {
            mNewTweetText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * check Company RSS id from key of Map Entry and add Feed to its RssFeed list.
     *
     * @param item
     */
    private void checkAndAddStockFeed(Map.Entry<String, CompanyFeed> item) {
        for (int i = 0; i < mCompanyFeed.size(); i++) {
            if (mCompanyFeed.get(i).getRssUrl().getId().equalsIgnoreCase(item.getKey())) {
                mCompanyFeed.get(i).getRssFeed().addAll(item.getValue().getRssFeed());
                return;
            }
        }
    }

    private void getRefreshTweetList(HashMap<String, String> data) {
        Call<TwitterFeedModel> service = RetrofitBuilder.getService().getLatestTwitterFeed(getAppPref().getAccessToken(), data);
        service.enqueue(new Callback<TwitterFeedModel>() {
            @Override
            public void onResponse(Call<TwitterFeedModel> call, Response<TwitterFeedModel> response) {

                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS) && !loading) {
                        if (response.body().getData().getFeeds() == null || response.body().getData().getFeeds().isEmpty()) {
                            initHandler();
                            return;
                        }
                        mAutoRefreshMaxId = response.body().getData().getMaxId();
                        if (response.body().getData().getFeeds() != null && !loading) {

                            if (mColumn.getColType().equalsIgnoreCase("1")) {
                                mFeeds.clear();
                            }

                            mFeeds.addAll(0, response.body().getData().getFeeds());
//                            if (mTweetsAdapter != null) {
                            mTweetsAdapter.notifyDataSetChanged();
//                            } else {
//                                mTweetsAdapter = new TabsAdapter(mFeeds, DefaultTabFragment.this, !isFacebookAvailable());
//                                mList.setAdapter(mTweetsAdapter);
//                            }
                            visibleLoadNewFeed();

                        }
                    }
                    initHandler();
                } else if (response.code() == 401) {
                    getBaseActivity().logoutApp();
                }
            }

            @Override
            public void onFailure(Call<TwitterFeedModel> call, Throwable t) {
            }
        });
    }

    /**
     * initialize new handler and Hit AutoRefresh API with time interval given from Backend.
     */
    void initHandler() {
        long time = Long.getLong(mColumn.getAjaxRunningTime(), 200);

        runnable = new Runnable() {
            @Override
            public void run() {
                callAutoRefreshAPI();
            }
        };
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(runnable, time * 1000);
    }

    /**
     * When auto refresh called and there are new Tweets available for that then Load new Tweets Text is appeared.
     * This method will move RecyclerView to Top and done visibility of new Tweet text to gone.
     */
    @OnClick(R.id.tv_load_new_tweets)
    void onNewTweetsClick() {
        mList.smoothScrollToPosition(0);
        mNewTweetText.setVisibility(View.GONE);
    }

    @OnClick(R.id.ll_tab_sponsored_layout)
    void onSponsoredClick() {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mColumn.getSponsoredUrl()));
        if (i.resolveActivity(getBaseActivity().getPackageManager()) != null) {
            startActivity(i);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public LinearLayoutManager getManager() {
        return manager;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isFromBackground) {
            callAutoRefreshAPI();
        }

        if (mColumn.getColType().equalsIgnoreCase("1") && isNotFirstAttenpt) {
            //TODO change for My Twitter
//            refreshCalled();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        isFromBackground = true;
    }


    @OnClick(R.id.twitter_login_button)
    void twitterBtnClick() {
        loginToTwitter(Constants.CLICK_LOGIN, null);
    }

    public void onReSelectTab() {
        if (mList != null) {
            mList.scrollToPosition(0);
        }
    }

    private boolean isFacebookAvailable() {
        try {
            ApplicationInfo info = getBaseActivity().getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void loadBannerAd() {

//        moPubView.setAdUnitId("da1dc4e72dbd4f59afe9a5076123725e");
//        moPubView.loadAd();
//        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
//            @Override
//            public void onBannerLoaded(MoPubView banner) {
//                Log.e("Mopub Ad ","loaded");
//            }
//
//            @Override
//            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
//                Log.e("Mopub Ad ","fail"+ errorCode);
//
//            }
//
//            @Override
//            public void onBannerClicked(MoPubView banner) {
//
//            }
//
//            @Override
//            public void onBannerExpanded(MoPubView banner) {
//
//            }
//
//            @Override
//            public void onBannerCollapsed(MoPubView banner) {
//
//            }
//        });

        if (isFacebookAvailable()) {
            adView = new com.facebook.ads.AdView(getBaseActivity(), "1239838622744569_1338393049555792", AdSize.BANNER_HEIGHT_50);
            adView.setAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("Facebook Ad", adError.getErrorMessage() + " " + adError.getErrorCode());
                    AdRequest adRequest = new AdRequest.Builder()
                            .build();
                    mBannerAd.loadAd(adRequest);
                    mBannerAd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    adContainer.setVisibility(View.VISIBLE);
                    mBannerAd.setVisibility(View.GONE);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }
            });
            adContainer.addView(adView);

            adView.loadAd();

        } else {
            adContainer.setVisibility(View.GONE);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mBannerAd.loadAd(adRequest);
            mBannerAd.setAdListener(new com.google.android.gms.ads.AdListener() {

                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Log.e("Error Ad", "add loaded");

                    mBannerAd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    Log.e("Error Ad", errorCode + "");
//                    showToast(errorCode);
                    Toast.makeText(getBaseActivity(), "Error Code:- " + errorCode, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    Log.e("Error Ad", "add opened");
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                    Log.e("Error Ad", "app left");

                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the user is about to return
                    // to the app after tapping on an ad.
                    Log.e("Error Ad", "add close");

                }
            });
        }


    }

    private void showToast(int errorCode) {
        switch (errorCode) {

            case AdRequest.ERROR_CODE_INTERNAL_ERROR:

                Toast.makeText(getBaseActivity(), "Something happened internally; for instance, an invalid response was received from the ad server.", Toast.LENGTH_SHORT).show();
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                Toast.makeText(getBaseActivity(), "The ad request was invalid; for instance, the ad unit ID was incorrect.", Toast.LENGTH_SHORT).show();

                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                Toast.makeText(getBaseActivity(), "The ad request was unsuccessful due to network connectivity.", Toast.LENGTH_SHORT).show();

                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                Toast.makeText(getBaseActivity(), "The ad request was successful, but no ad was returned due to lack of ad inventory.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBannerAd.destroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    public String[] getAccounts() {
//        AccountManager manager = (AccountManager) getBaseActivity().getSystemService(getBaseActivity().ACCOUNT_SERVICE);
        String[] emailsArr = {};
        if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return emailsArr;
        }

        HashSet<String> emails = new HashSet<>();

        ArrayList<String> names = new ArrayList<>();
        ContentResolver cr = getBaseActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
//                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if (email != null) {
                        emails.add(email);
                    }
                }
                cur1.close();
            }

        }
//        Account[] list = manager.getAccounts();
//
//        if (list.length > 0) {
//            HashSet<String> emails = new HashSet<>();
//            for (int i = 0; i < list.length; i++) {
//                if (Validation.isValidEmail(list[i].name)) {
//                    emails.add(list[i].name);
//                }
//            }
        emailsArr = emails.toArray(new String[emails.size()]);
//        }

        return emailsArr;
    }


    @Override
    public void onEmailPermissionClick() {
        Log.e("OnEmail Click", "Click");
        if (mEmailFeed != null) {
            onEmailClick(mEmailFeed);
        }
    }
}
