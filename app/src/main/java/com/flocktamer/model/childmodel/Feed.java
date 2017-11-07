package com.flocktamer.model.childmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amandeepsingh on 25/8/16.
 */
public class Feed implements Parcelable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("home_page_column_id")
    @Expose
    private String homePageColumnId;
    @SerializedName("twitter_feed_id")
    @Expose
    private String twitterFeedId;
    @SerializedName("twitter_feed_id_str")
    @Expose
    private String twitterFeedIdStr;
    @SerializedName("twitter_text")
    @Expose
    private String twitterText;
    @SerializedName("twitter_mentioned_users")
    @Expose
    private String twitterMentionedUsers;
    @SerializedName("twitter_tweet_url")
    @Expose
    private String twitterTweetUrl;
    @SerializedName("twitter_user_url")
    @Expose
    private String twitterUserUrl;
    @SerializedName("twitter_user_name")
    @Expose
    private String twitterUserName;
    @SerializedName("twitter_user_screen_name")
    @Expose
    private String twitterUserScreenName;
    @SerializedName("twitter_user_profile_image_url")
    @Expose
    private String twitterUserProfileImageUrl;
    @SerializedName("twitter_user_profile_image_https_url")
    @Expose
    private String twitterUserProfileImageHttpsUrl;
    @SerializedName("twitter_media_url")
    @Expose
    private String twitterMediaUrl;
    @SerializedName("twitter_media_https_url")
    @Expose
    private String twitterMediaHttpsUrl;
    @SerializedName("twitter_created_at")
    @Expose
    private String twitterCreatedAt;
    @SerializedName("twitter_timestamp")
    @Expose
    private String twitterTimestamp;
    @SerializedName("followers_count")
    @Expose
    private String followersCount;
    @SerializedName("friends_count")
    @Expose
    private String friendsCount;
    @SerializedName("favourites_count")
    @Expose
    private String favouritesCount;
    @SerializedName("twitter_retweet_text")
    @Expose
    private String twitterRetweetText;
    @SerializedName("twitter_retweet_mentioned_users")
    @Expose
    private String twitterRetweetMentionedUsers;
    @SerializedName("twitter_retweet_tweet_url")
    @Expose
    private String twitterRetweetTweetUrl;
    @SerializedName("twitter_retweet_user_url")
    @Expose
    private String twitterRetweetUserUrl;
    @SerializedName("twitter_retweet_user_name")
    @Expose
    private String twitterRetweetUserName;
    @SerializedName("twitter_retweet_user_screen_name")
    @Expose
    private String twitterRetweetUserScreenName;
    @SerializedName("twitter_retweet_profile_image_url")
    @Expose
    private String twitterRetweetProfileImageUrl;
    @SerializedName("twitter_retweet_user_profile_image_https_url")
    @Expose
    private String twitterRetweetUserProfileImageHttpsUrl;
    @SerializedName("twitter_quote_text")
    @Expose
    private String twitterQuoteText;
    @SerializedName("twitter_quote_mentioned_users")
    @Expose
    private String twitterQuoteMentionedUsers;
    @SerializedName("twitter_quote_tweet_url")
    @Expose
    private String twitterQuoteTweetUrl;
    @SerializedName("twitter_quote_user_url")
    @Expose
    private String twitterQuoteUserUrl;
    @SerializedName("twitter_quote_user_name")
    @Expose
    private String twitterQuoteUserName;
    @SerializedName("twitter_quote_user_screen_name")
    @Expose
    private String twitterQuoteUserScreenName;
    @SerializedName("twitter_quote_profile_image_url")
    @Expose
    private String twitterQuoteProfileImageUrl;
    @SerializedName("twitter_quote_user_profile_image_https_url")
    @Expose
    private String twitterQuoteUserProfileImageHttpsUrl;
    @SerializedName("twitter_quote_media_url")
    @Expose
    private String twitterQuoteMediaUrl;
    @SerializedName("twitter_quote_media_https_url")
    @Expose
    private String twitterQuoteMediaHttpsUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("is_liked")
    @Expose
    private String isLiked;
    @SerializedName("time_lapsed")
    @Expose
    private String timeLapsed;

    @SerializedName("image_info_width")
    @Expose
    private String imageWidth;
    @SerializedName("image_info_height")
    @Expose
    private String imageHeight;

    @SerializedName("UserTwitterFeedLike_id")
    @Expose
    private String feedLikeId;


    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The homePageColumnId
     */
    public String getHomePageColumnId() {
        return homePageColumnId;
    }

    /**
     * @param homePageColumnId The home_page_column_id
     */
    public void setHomePageColumnId(String homePageColumnId) {
        this.homePageColumnId = homePageColumnId;
    }

    /**
     * @return The twitterFeedId
     */
    public String getTwitterFeedId() {
        return twitterFeedId;
    }

    /**
     * @param twitterFeedId The twitter_feed_id
     */
    public void setTwitterFeedId(String twitterFeedId) {
        this.twitterFeedId = twitterFeedId;
    }

    /**
     * @return The twitterFeedIdStr
     */
    public String getTwitterFeedIdStr() {
        return twitterFeedIdStr;
    }

    /**
     * @param twitterFeedIdStr The twitter_feed_id_str
     */
    public void setTwitterFeedIdStr(String twitterFeedIdStr) {
        this.twitterFeedIdStr = twitterFeedIdStr;
    }

    /**
     * @return The twitterText
     */
    public String getTwitterText() {
        return twitterText;
    }

    /**
     * @param twitterText The twitter_text
     */
    public void setTwitterText(String twitterText) {
        this.twitterText = twitterText;
    }

    /**
     * @return The twitterMentionedUsers
     */
    public String getTwitterMentionedUsers() {
        return twitterMentionedUsers;
    }

    /**
     * @param twitterMentionedUsers The twitter_mentioned_users
     */
    public void setTwitterMentionedUsers(String twitterMentionedUsers) {
        this.twitterMentionedUsers = twitterMentionedUsers;
    }

    /**
     * @return The twitterTweetUrl
     */
    public String getTwitterTweetUrl() {
        return twitterTweetUrl;
    }

    /**
     * @param twitterTweetUrl The twitter_tweet_url
     */
    public void setTwitterTweetUrl(String twitterTweetUrl) {
        this.twitterTweetUrl = twitterTweetUrl;
    }

    /**
     * @return The twitterUserUrl
     */
    public String getTwitterUserUrl() {
        return twitterUserUrl;
    }

    /**
     * @param twitterUserUrl The twitter_user_url
     */
    public void setTwitterUserUrl(String twitterUserUrl) {
        this.twitterUserUrl = twitterUserUrl;
    }

    /**
     * @return The twitterUserName
     */
    public String getTwitterUserName() {
        return twitterUserName;
    }

    /**
     * @param twitterUserName The twitter_user_name
     */
    public void setTwitterUserName(String twitterUserName) {
        this.twitterUserName = twitterUserName;
    }

    /**
     * @return The twitterUserScreenName
     */
    public String getTwitterUserScreenName() {
        return twitterUserScreenName;
    }

    /**
     * @param twitterUserScreenName The twitter_user_screen_name
     */
    public void setTwitterUserScreenName(String twitterUserScreenName) {
        this.twitterUserScreenName = twitterUserScreenName;
    }

    /**
     * @return The twitterUserProfileImageUrl
     */
    public String getTwitterUserProfileImageUrl() {
        return twitterUserProfileImageUrl;
    }

    /**
     * @param twitterUserProfileImageUrl The twitter_user_profile_image_url
     */
    public void setTwitterUserProfileImageUrl(String twitterUserProfileImageUrl) {
        this.twitterUserProfileImageUrl = twitterUserProfileImageUrl;
    }

    /**
     * @return The twitterUserProfileImageHttpsUrl
     */
    public String getTwitterUserProfileImageHttpsUrl() {
        return twitterUserProfileImageHttpsUrl;
    }

    /**
     * @param twitterUserProfileImageHttpsUrl The twitter_user_profile_image_https_url
     */
    public void setTwitterUserProfileImageHttpsUrl(String twitterUserProfileImageHttpsUrl) {
        this.twitterUserProfileImageHttpsUrl = twitterUserProfileImageHttpsUrl;
    }

    /**
     * @return The twitterMediaUrl
     */
    public String getTwitterMediaUrl() {
        return twitterMediaUrl;
    }

    /**
     * @param twitterMediaUrl The twitter_media_url
     */
    public void setTwitterMediaUrl(String twitterMediaUrl) {
        this.twitterMediaUrl = twitterMediaUrl;
    }

    /**
     * @return The twitterMediaHttpsUrl
     */
    public String getTwitterMediaHttpsUrl() {
        return twitterMediaHttpsUrl;
    }

    /**
     * @param twitterMediaHttpsUrl The twitter_media_https_url
     */
    public void setTwitterMediaHttpsUrl(String twitterMediaHttpsUrl) {
        this.twitterMediaHttpsUrl = twitterMediaHttpsUrl;
    }

    /**
     * @return The twitterCreatedAt
     */
    public String getTwitterCreatedAt() {
        return twitterCreatedAt;
    }

    /**
     * @param twitterCreatedAt The twitter_created_at
     */
    public void setTwitterCreatedAt(String twitterCreatedAt) {
        this.twitterCreatedAt = twitterCreatedAt;
    }

    /**
     * @return The twitterTimestamp
     */
    public String getTwitterTimestamp() {
        return twitterTimestamp;
    }

    /**
     * @param twitterTimestamp The twitter_timestamp
     */
    public void setTwitterTimestamp(String twitterTimestamp) {
        this.twitterTimestamp = twitterTimestamp;
    }

    /**
     * @return The followersCount
     */
    public String getFollowersCount() {
        return followersCount;
    }

    /**
     * @param followersCount The followers_count
     */
    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * @return The friendsCount
     */
    public String getFriendsCount() {
        return friendsCount;
    }

    /**
     * @param friendsCount The friends_count
     */
    public void setFriendsCount(String friendsCount) {
        this.friendsCount = friendsCount;
    }

    /**
     * @return The favouritesCount
     */
    public String getFavouritesCount() {
        return favouritesCount;
    }

    /**
     * @param favouritesCount The favourites_count
     */
    public void setFavouritesCount(String favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    /**
     * @return The twitterRetweetText
     */
    public String getTwitterRetweetText() {
        return twitterRetweetText;
    }

    /**
     * @param twitterRetweetText The twitter_retweet_text
     */
    public void setTwitterRetweetText(String twitterRetweetText) {
        this.twitterRetweetText = twitterRetweetText;
    }

    /**
     * @return The twitterRetweetMentionedUsers
     */
    public String getTwitterRetweetMentionedUsers() {
        return twitterRetweetMentionedUsers;
    }

    /**
     * @param twitterRetweetMentionedUsers The twitter_retweet_mentioned_users
     */
    public void setTwitterRetweetMentionedUsers(String twitterRetweetMentionedUsers) {
        this.twitterRetweetMentionedUsers = twitterRetweetMentionedUsers;
    }

    /**
     * @return The twitterRetweetTweetUrl
     */
    public String getTwitterRetweetTweetUrl() {
        return twitterRetweetTweetUrl;
    }

    /**
     * @param twitterRetweetTweetUrl The twitter_retweet_tweet_url
     */
    public void setTwitterRetweetTweetUrl(String twitterRetweetTweetUrl) {
        this.twitterRetweetTweetUrl = twitterRetweetTweetUrl;
    }

    /**
     * @return The twitterRetweetUserUrl
     */
    public String getTwitterRetweetUserUrl() {
        return twitterRetweetUserUrl;
    }

    /**
     * @param twitterRetweetUserUrl The twitter_retweet_user_url
     */
    public void setTwitterRetweetUserUrl(String twitterRetweetUserUrl) {
        this.twitterRetweetUserUrl = twitterRetweetUserUrl;
    }

    /**
     * @return The twitterRetweetUserName
     */
    public String getTwitterRetweetUserName() {
        return twitterRetweetUserName;
    }

    /**
     * @param twitterRetweetUserName The twitter_retweet_user_name
     */
    public void setTwitterRetweetUserName(String twitterRetweetUserName) {
        this.twitterRetweetUserName = twitterRetweetUserName;
    }

    /**
     * @return The twitterRetweetUserScreenName
     */
    public String getTwitterRetweetUserScreenName() {
        return twitterRetweetUserScreenName;
    }

    /**
     * @param twitterRetweetUserScreenName The twitter_retweet_user_screen_name
     */
    public void setTwitterRetweetUserScreenName(String twitterRetweetUserScreenName) {
        this.twitterRetweetUserScreenName = twitterRetweetUserScreenName;
    }

    /**
     * @return The twitterRetweetProfileImageUrl
     */
    public String getTwitterRetweetProfileImageUrl() {
        return twitterRetweetProfileImageUrl;
    }

    /**
     * @param twitterRetweetProfileImageUrl The twitter_retweet_profile_image_url
     */
    public void setTwitterRetweetProfileImageUrl(String twitterRetweetProfileImageUrl) {
        this.twitterRetweetProfileImageUrl = twitterRetweetProfileImageUrl;
    }

    /**
     * @return The twitterRetweetUserProfileImageHttpsUrl
     */
    public String getTwitterRetweetUserProfileImageHttpsUrl() {
        return twitterRetweetUserProfileImageHttpsUrl;
    }

    /**
     * @param twitterRetweetUserProfileImageHttpsUrl The twitter_retweet_user_profile_image_https_url
     */
    public void setTwitterRetweetUserProfileImageHttpsUrl(String twitterRetweetUserProfileImageHttpsUrl) {
        this.twitterRetweetUserProfileImageHttpsUrl = twitterRetweetUserProfileImageHttpsUrl;
    }

    /**
     * @return The twitterQuoteText
     */
    public String getTwitterQuoteText() {
        return twitterQuoteText;
    }

    /**
     * @param twitterQuoteText The twitter_quote_text
     */
    public void setTwitterQuoteText(String twitterQuoteText) {
        this.twitterQuoteText = twitterQuoteText;
    }

    /**
     * @return The twitterQuoteMentionedUsers
     */
    public String getTwitterQuoteMentionedUsers() {
        return twitterQuoteMentionedUsers;
    }

    /**
     * @param twitterQuoteMentionedUsers The twitter_quote_mentioned_users
     */
    public void setTwitterQuoteMentionedUsers(String twitterQuoteMentionedUsers) {
        this.twitterQuoteMentionedUsers = twitterQuoteMentionedUsers;
    }

    /**
     * @return The twitterQuoteTweetUrl
     */
    public String getTwitterQuoteTweetUrl() {
        return twitterQuoteTweetUrl;
    }

    /**
     * @param twitterQuoteTweetUrl The twitter_quote_tweet_url
     */
    public void setTwitterQuoteTweetUrl(String twitterQuoteTweetUrl) {
        this.twitterQuoteTweetUrl = twitterQuoteTweetUrl;
    }

    /**
     * @return The twitterQuoteUserUrl
     */
    public String getTwitterQuoteUserUrl() {
        return twitterQuoteUserUrl;
    }

    /**
     * @param twitterQuoteUserUrl The twitter_quote_user_url
     */
    public void setTwitterQuoteUserUrl(String twitterQuoteUserUrl) {
        this.twitterQuoteUserUrl = twitterQuoteUserUrl;
    }

    /**
     * @return The twitterQuoteUserName
     */
    public String getTwitterQuoteUserName() {
        return twitterQuoteUserName;
    }

    /**
     * @param twitterQuoteUserName The twitter_quote_user_name
     */
    public void setTwitterQuoteUserName(String twitterQuoteUserName) {
        this.twitterQuoteUserName = twitterQuoteUserName;
    }

    /**
     * @return The twitterQuoteUserScreenName
     */
    public String getTwitterQuoteUserScreenName() {
        return twitterQuoteUserScreenName;
    }

    /**
     * @param twitterQuoteUserScreenName The twitter_quote_user_screen_name
     */
    public void setTwitterQuoteUserScreenName(String twitterQuoteUserScreenName) {
        this.twitterQuoteUserScreenName = twitterQuoteUserScreenName;
    }

    /**
     * @return The twitterQuoteProfileImageUrl
     */
    public String getTwitterQuoteProfileImageUrl() {
        return twitterQuoteProfileImageUrl;
    }

    /**
     * @param twitterQuoteProfileImageUrl The twitter_quote_profile_image_url
     */
    public void setTwitterQuoteProfileImageUrl(String twitterQuoteProfileImageUrl) {
        this.twitterQuoteProfileImageUrl = twitterQuoteProfileImageUrl;
    }

    /**
     * @return The twitterQuoteUserProfileImageHttpsUrl
     */
    public String getTwitterQuoteUserProfileImageHttpsUrl() {
        return twitterQuoteUserProfileImageHttpsUrl;
    }

    /**
     * @param twitterQuoteUserProfileImageHttpsUrl The twitter_quote_user_profile_image_https_url
     */
    public void setTwitterQuoteUserProfileImageHttpsUrl(String twitterQuoteUserProfileImageHttpsUrl) {
        this.twitterQuoteUserProfileImageHttpsUrl = twitterQuoteUserProfileImageHttpsUrl;
    }

    /**
     * @return The twitterQuoteMediaUrl
     */
    public String getTwitterQuoteMediaUrl() {
        return twitterQuoteMediaUrl;
    }

    /**
     * @param twitterQuoteMediaUrl The twitter_quote_media_url
     */
    public void setTwitterQuoteMediaUrl(String twitterQuoteMediaUrl) {
        this.twitterQuoteMediaUrl = twitterQuoteMediaUrl;
    }

    /**
     * @return The twitterQuoteMediaHttpsUrl
     */
    public String getTwitterQuoteMediaHttpsUrl() {
        return twitterQuoteMediaHttpsUrl;
    }

    /**
     * @param twitterQuoteMediaHttpsUrl The twitter_quote_media_https_url
     */
    public void setTwitterQuoteMediaHttpsUrl(String twitterQuoteMediaHttpsUrl) {
        this.twitterQuoteMediaHttpsUrl = twitterQuoteMediaHttpsUrl;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return The modified
     */
    public String getModified() {
        return modified;
    }

    /**
     * @param modified The modified
     */
    public void setModified(String modified) {
        this.modified = modified;
    }

    /**
     * @return The isLiked
     */
    public String getIsLiked() {
        return isLiked;
    }

    /**
     * @param isLiked The is_liked
     */
    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    /**
     * @return The timeLapsed
     */
    public String getTimeLapsed() {
        return timeLapsed;
    }

    /**
     * @param timeLapsed The time_lapsed
     */
    public void setTimeLapsed(String timeLapsed) {
        this.timeLapsed = timeLapsed;
    }

    public Feed() {
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.homePageColumnId);
        dest.writeString(this.twitterFeedId);
        dest.writeString(this.twitterFeedIdStr);
        dest.writeString(this.twitterText);
        dest.writeString(this.twitterMentionedUsers);
        dest.writeString(this.twitterTweetUrl);
        dest.writeString(this.twitterUserUrl);
        dest.writeString(this.twitterUserName);
        dest.writeString(this.twitterUserScreenName);
        dest.writeString(this.twitterUserProfileImageUrl);
        dest.writeString(this.twitterUserProfileImageHttpsUrl);
        dest.writeString(this.twitterMediaUrl);
        dest.writeString(this.twitterMediaHttpsUrl);
        dest.writeString(this.twitterCreatedAt);
        dest.writeString(this.twitterTimestamp);
        dest.writeString(this.followersCount);
        dest.writeString(this.friendsCount);
        dest.writeString(this.favouritesCount);
        dest.writeString(this.twitterRetweetText);
        dest.writeString(this.twitterRetweetMentionedUsers);
        dest.writeString(this.twitterRetweetTweetUrl);
        dest.writeString(this.twitterRetweetUserUrl);
        dest.writeString(this.twitterRetweetUserName);
        dest.writeString(this.twitterRetweetUserScreenName);
        dest.writeString(this.twitterRetweetProfileImageUrl);
        dest.writeString(this.twitterRetweetUserProfileImageHttpsUrl);
        dest.writeString(this.twitterQuoteText);
        dest.writeString(this.twitterQuoteMentionedUsers);
        dest.writeString(this.twitterQuoteTweetUrl);
        dest.writeString(this.twitterQuoteUserUrl);
        dest.writeString(this.twitterQuoteUserName);
        dest.writeString(this.twitterQuoteUserScreenName);
        dest.writeString(this.twitterQuoteProfileImageUrl);
        dest.writeString(this.twitterQuoteUserProfileImageHttpsUrl);
        dest.writeString(this.twitterQuoteMediaUrl);
        dest.writeString(this.twitterQuoteMediaHttpsUrl);
        dest.writeString(this.status);
        dest.writeString(this.created);
        dest.writeString(this.modified);
        dest.writeString(this.isLiked);
        dest.writeString(this.timeLapsed);
        dest.writeString(this.imageHeight);
        dest.writeString(this.imageWidth);
        dest.writeString(this.feedLikeId);
    }


    public String getFeedLikeId() {
        return feedLikeId;
    }

    public void setFeedLikeId(String feedLikeId) {
        this.feedLikeId = feedLikeId;
    }

    protected Feed(Parcel in) {
        this.id = in.readString();
        this.homePageColumnId = in.readString();
        this.twitterFeedId = in.readString();
        this.twitterFeedIdStr = in.readString();
        this.twitterText = in.readString();
        this.twitterMentionedUsers = in.readString();
        this.twitterTweetUrl = in.readString();
        this.twitterUserUrl = in.readString();
        this.twitterUserName = in.readString();
        this.twitterUserScreenName = in.readString();
        this.twitterUserProfileImageUrl = in.readString();
        this.twitterUserProfileImageHttpsUrl = in.readString();
        this.twitterMediaUrl = in.readString();
        this.twitterMediaHttpsUrl = in.readString();
        this.twitterCreatedAt = in.readString();
        this.twitterTimestamp = in.readString();
        this.followersCount = in.readString();
        this.friendsCount = in.readString();
        this.favouritesCount = in.readString();
        this.twitterRetweetText = in.readString();
        this.twitterRetweetMentionedUsers = in.readString();
        this.twitterRetweetTweetUrl = in.readString();
        this.twitterRetweetUserUrl = in.readString();
        this.twitterRetweetUserName = in.readString();
        this.twitterRetweetUserScreenName = in.readString();
        this.twitterRetweetProfileImageUrl = in.readString();
        this.twitterRetweetUserProfileImageHttpsUrl = in.readString();
        this.twitterQuoteText = in.readString();
        this.twitterQuoteMentionedUsers = in.readString();
        this.twitterQuoteTweetUrl = in.readString();
        this.twitterQuoteUserUrl = in.readString();
        this.twitterQuoteUserName = in.readString();
        this.twitterQuoteUserScreenName = in.readString();
        this.twitterQuoteProfileImageUrl = in.readString();
        this.twitterQuoteUserProfileImageHttpsUrl = in.readString();
        this.twitterQuoteMediaUrl = in.readString();
        this.twitterQuoteMediaHttpsUrl = in.readString();
        this.status = in.readString();
        this.created = in.readString();
        this.modified = in.readString();
        this.isLiked = in.readString();
        this.timeLapsed = in.readString();
        this.imageHeight=in.readString();
        this.imageWidth=in.readString();
        this.feedLikeId=in.readString();
    }

    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel source) {
            return new Feed(source);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}
