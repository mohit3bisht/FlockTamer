package com.flocktamer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.fragments.DefaultTabFragment;
import com.flocktamer.interfaces.TwitterItemClickListener;
import com.flocktamer.model.childmodel.Feed;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amandeepsingh on 12/8/16.
 * <p/>
 * Adapter for list of Tweets in recyclerView of {@link DefaultTabFragment} under ViewPager.
 */
public class TabsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_AD = 2;
    private static final int VIEW_TWEET = 1;
    private final TwitterItemClickListener mCallback;
    private final boolean mIsGoogleAd;
    //    private final boolean mIsMyTwitter;
    List<Feed> mList;
    private NativeAd mAd;
    private Context mContext;
    private ImageLoader imageLoader;

    public TabsAdapter(@NonNull List<Feed> list, TwitterItemClickListener callback, boolean isGoogleAd) {
        mList = list;
        mCallback = callback;
        mIsGoogleAd = isGoogleAd;
        imageLoader = ImageLoader.getInstance();

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        if (viewType == VIEW_AD) {
//            View view = inflater.inflate(R.layout.native_ad, parent, false);
//            return new AdViewHolder(view);
//        } else {
        View view = inflater.inflate(R.layout.view_tweet_item, parent, false);
        return new TabsViewHolder(view);
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 0 && position != 0) {
            return VIEW_AD;
        }

        return VIEW_TWEET;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

//        if (holder.getItemViewType() == VIEW_TWEET) {
        final TabsViewHolder tabsHolder = (TabsViewHolder) holder;

        final Feed feedData = mList.get(position);

        if ("true".equalsIgnoreCase(feedData.getIsLiked())) {
            tabsHolder.mFavoriteBtn.setImageResource(R.drawable.ic_favorite_red);
//            DrawableCompat.setTint(tabsHolder.mFavoriteBtn.getDrawable(), Color.RED);
        } else {
            tabsHolder.mFavoriteBtn.setImageResource(R.drawable.ic_favorite);
//            DrawableCompat.setTint(tabsHolder.mFavoriteBtn.getDrawable(), Color.DKGRAY);
        }

        tabsHolder.mTimeAgo.setText(getTimesAgo(feedData.getTwitterCreatedAt()));
        tabsHolder.mTweetImage.setImageBitmap(null);

        String screenName = "";
        String imageClickUrl = "";
        if (feedData.getTwitterRetweetUserName() != null && !feedData.getTwitterRetweetUserName().trim().isEmpty()) {
            tabsHolder.mName.setText(feedData.getTwitterRetweetUserName());
            screenName = feedData.getTwitterRetweetUserScreenName();

            imageClickUrl = feedData.getTwitterRetweetTweetUrl();

            tabsHolder.mDetailText.setText(setTweetText(feedData.getTwitterRetweetText()));

            tabsHolder.mRetweetUser.setVisibility(View.VISIBLE);
            tabsHolder.mRetweetUser.setText(feedData.getTwitterUserName() + " Retweeted");
            imageLoader.displayImage(feedData.getTwitterRetweetProfileImageUrl(), tabsHolder.mProfile, getDisplayImageOption());

        } else {
            screenName = feedData.getTwitterUserScreenName();
            tabsHolder.mRetweetUser.setVisibility(View.GONE);
            tabsHolder.mName.setText(feedData.getTwitterUserName());
            imageClickUrl = mList.get(position).getTwitterTweetUrl();

            tabsHolder.mDetailText.setText(setTweetText(feedData.getTwitterText()));
            tabsHolder.mDetailText.setMovementMethod(LinkMovementMethod.getInstance());

            imageLoader.displayImage(feedData.getTwitterUserProfileImageUrl(), tabsHolder.mProfile, getDisplayImageOption());
        }

        tabsHolder.mScreenName.setText(getUnderLineScreenName(screenName));

        tabsHolder.mDetailText.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(tabsHolder.mDetailText, Linkify.WEB_URLS);


        // Check Images url array. If there is some image URL then show first image from Array
        if (feedData.getTwitterMediaUrl() != null && !feedData.getTwitterMediaUrl().isEmpty()) {
            tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
            try {

                tabsHolder.mTweetImage.getLayoutParams().height = Integer.valueOf(mList.get(position).getImageHeight());
                tabsHolder.mTweetImage.getLayoutParams().width = Integer.valueOf(mList.get(position).getImageWidth());

            } catch (Exception e) {

            }

            String[] imageLink = mList.get(position).getTwitterMediaUrl().split(",");

            imageLoader.displayImage(imageLink[0], tabsHolder.mTweetImage, new DisplayImageOptions.Builder().cacheInMemory(true).build(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
                    tabsHolder.mTweetImage.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
        } else {
            tabsHolder.mTweetImage.setVisibility(View.GONE);
        }

        if (!feedData.getTwitterQuoteUserName().isEmpty()) {
            tabsHolder.mQuoteCard.setVisibility(View.VISIBLE);
            tabsHolder.mQuoteImage.setVisibility(View.GONE);
            if (!feedData.getTwitterQuoteMediaUrl().isEmpty()) {
                String[] mediaLink = feedData.getTwitterQuoteMediaUrl().split(",");
                imageLoader.displayImage(mediaLink[0], tabsHolder.mQuoteImage, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
                        tabsHolder.mQuoteImage.setVisibility(View.VISIBLE);
                        tabsHolder.mQuoteImage.setImageBitmap(loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }

            tabsHolder.mQuoteName.setText(feedData.getTwitterQuoteUserName());

            imageLoader.displayImage(feedData.getTwitterQuoteProfileImageUrl(), tabsHolder.mQuoteProfileImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        tabsHolder.mTweetImage.setVisibility(View.VISIBLE);
                    tabsHolder.mQuoteProfileImage.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });

            tabsHolder.mQuoteScreenName.setText(getUnderLineScreenName(feedData.getTwitterQuoteUserScreenName()));
            tabsHolder.mQuoteDetail.setText(setTweetText(feedData.getTwitterQuoteText()));
            tabsHolder.mQuoteDetail.setMovementMethod(LinkMovementMethod.getInstance());
            Linkify.addLinks(tabsHolder.mQuoteDetail, Linkify.WEB_URLS);
        } else {
            tabsHolder.mQuoteCard.setVisibility(View.GONE);
        }

        final String finalImageClickUrl = imageClickUrl;

        tabsHolder.mTweetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Image Url", finalImageClickUrl);
                if (finalImageClickUrl != null && !finalImageClickUrl.isEmpty()) {
                    Uri url = Uri.parse(finalImageClickUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);

                    if (intent.resolveActivity(mContext.getPackageManager()) != null)
                        mContext.startActivity(intent);
                }

            }
        });

        tabsHolder.mRetweebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onRetweetClick(mList.get(position));
            }
        });
        tabsHolder.mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DrawableCompat.setTint(tabsHolder.mFavoriteBtn.getDrawable(), Color.RED);
//                feedData.setIsLiked("true");
//                if (mList.get(position).getIsLiked().equalsIgnoreCase("true")) {
//                    mList.get(position).setIsLiked("false");
//                    tabsHolder.mFavoriteBtn.setImageResource(R.drawable.ic_favorite);
//                }

                mCallback.onFavoriteClick(mList.get(position));
            }
        });
        tabsHolder.mReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onReplyClick(mList.get(position));
            }
        });
        final String finalScreenName = screenName;
        tabsHolder.mScreenName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onProfileClick(finalScreenName);
            }
        });
        tabsHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onProfileClick(finalScreenName);
            }
        });
        tabsHolder.mEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onEmailClick(mList.get(position));
            }
        });

        tabsHolder.mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onProfileClick(finalScreenName);
            }
        });

        View.OnClickListener quoteUserListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onProfileClick(feedData.getTwitterQuoteUserScreenName());
            }
        };

//            tabsHolder.mQuoteImage.setOnClickListener(quoteUserListener);
        tabsHolder.mQuoteName.setOnClickListener(quoteUserListener);
        tabsHolder.mQuoteScreenName.setOnClickListener(quoteUserListener);

//        }

        // Check condition of Add View
        if (holder.getItemViewType() == VIEW_AD && mIsGoogleAd) {
            //TODO remove google ads because of suspention.

            tabsHolder.mAdView.setVisibility(View.VISIBLE);

            AdRequest request = new AdRequest.Builder()
                    .build();
            tabsHolder.mAdView.loadAd(request);

        } else {
            tabsHolder.mAdView.setVisibility(View.GONE);
        }
    }

    private SpannableString getUnderLineScreenName(String screenName) {
        String fullScreenName = "@" + screenName;
        SpannableString content = new SpannableString(fullScreenName);
        content.setSpan(new UnderlineSpan(), 0, fullScreenName.length(), 0);
        return content;
    }

    private void downloadAndDisplayImage(String url, final ImageView imageview) {
        ImageLoader imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
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
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + text.replace("@", ""))));
                        } catch (Exception e) {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + text.replace("@", ""))));
                        }

                    }
                }, span.toString().indexOf(text), span.toString().length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return span;
    }

    class TabsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_tweet_profile)
        ImageView mProfile;
        @BindView(R.id.tv_tweet_username)
        CustomTextView mName;
        @BindView(R.id.tv_tweet_userid)
        CustomTextView mScreenName;
        @BindView(R.id.tv_tweeter_maintext)
        CustomTextView mDetailText;
        @BindView(R.id.tv_twitter_time_ago)
        CustomTextView mTimeAgo;
        @BindView(R.id.tv_retweeted_username)
        CustomTextView mRetweetUser;
        @BindView(R.id.iv_twitter_retweet)
        ImageView mRetweebtn;
        @BindView(R.id.iv_twitter_favorite)
        ImageView mFavoriteBtn;
        @BindView(R.id.iv_twitter_reply)
        ImageView mReplyBtn;
        @BindView(R.id.iv_twitter_mail)
        ImageView mEmailBtn;
        @BindView(R.id.iv_tweet_image)
        ImageView mTweetImage;

        @BindView(R.id.cv_quote)
        CardView mQuoteCard;
        @BindView(R.id.iv_quote_image)
        ImageView mQuoteImage;

        @BindView(R.id.iv_quote_profile_image)
        ImageView mQuoteProfileImage;


        @BindView(R.id.tv_quote_name)
        CustomTextView mQuoteName;
        @BindView(R.id.tv_quote_screen_name)
        CustomTextView mQuoteScreenName;
        @BindView(R.id.tv_quote_detail)
        CustomTextView mQuoteDetail;

        @BindView(R.id.adView)
        NativeExpressAdView mAdView;
        // Ads view content
//        @BindView(R.id.ad_tweet_profile)
//        ImageView adIcon;
//        @BindView(R.id.ad_tweet_name)
//        TextView adTitle;
//        @BindView(R.id.ad_tweeter_maintext)
//        TextView adBody;
//
//        @BindView(R.id.ad_tweeter_context_value)
//        TextView adSocialContext;
//        @BindView(R.id.ad_main_image)
//        ImageView adMainImage;
//        @BindView(R.id.ll_ad_view)
//        LinearLayout mAdView;

        public TabsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mScreenName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ad_tweet_profile)
        ImageView adIcon;
        @BindView(R.id.ad_tweet_name)
        TextView adTitle;
        @BindView(R.id.ad_tweeter_maintext)
        TextView adBody;

        @BindView(R.id.ad_tweeter_context_value)
        TextView adSocialContext;
        @BindView(R.id.ad_main_image)
        ImageView adMainImage;

        public AdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private DisplayImageOptions getDisplayImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
                .cacheInMemory(true)
                .build();

        return options;
    }

}
