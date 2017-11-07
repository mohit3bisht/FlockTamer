package com.flocktamer.model.childmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amandeepsingh on 2/9/16.
 */
public class TwitterUser implements Parcelable {
    public static final Creator<TwitterUser> CREATOR = new Creator<TwitterUser>() {
        @Override
        public TwitterUser createFromParcel(Parcel source) {
            return new TwitterUser(source);
        }

        @Override
        public TwitterUser[] newArray(int size) {
            return new TwitterUser[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("twitter_user_id")
    @Expose
    private String twitterUserId;
    @SerializedName("twitter_oauth_token")
    @Expose
    private String twitterOauthToken;
    @SerializedName("twitter_oauth_token_secret")
    @Expose
    private String twitterOauthTokenSecret;
    @SerializedName("twitter_x_oauth_expires")
    @Expose
    private String twitterXOauthExpires;
    @SerializedName("twitter_screen_name")
    @Expose
    private String twitterScreenName;
    @SerializedName("twitter_name")
    @Expose
    private String twitterName;
    @SerializedName("twitter_profile_image_url")
    @Expose
    private String twitterProfileImageUrl;
    @SerializedName("twitter_profile_image_url_https")
    @Expose
    private String twitterProfileImageUrlHttps;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    private boolean isChecked;

    public TwitterUser() {
    }

    protected TwitterUser(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.twitterUserId = in.readString();
        this.twitterOauthToken = in.readString();
        this.twitterOauthTokenSecret = in.readString();
        this.twitterXOauthExpires = in.readString();
        this.twitterScreenName = in.readString();
        this.twitterName = in.readString();
        this.twitterProfileImageUrl = in.readString();
        this.twitterProfileImageUrlHttps = in.readString();
        this.isActive = in.readString();
        this.status = in.readString();
        this.created = in.readString();
        this.modified = in.readString();
        this.isChecked = in.readByte() != 0;
    }

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
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The twitterUserId
     */
    public String getTwitterUserId() {
        return twitterUserId;
    }

    /**
     * @param twitterUserId The twitter_user_id
     */
    public void setTwitterUserId(String twitterUserId) {
        this.twitterUserId = twitterUserId;
    }

    /**
     * @return The twitterOauthToken
     */
    public String getTwitterOauthToken() {
        return twitterOauthToken;
    }

    /**
     * @param twitterOauthToken The twitter_oauth_token
     */
    public void setTwitterOauthToken(String twitterOauthToken) {
        this.twitterOauthToken = twitterOauthToken;
    }

    /**
     * @return The twitterOauthTokenSecret
     */
    public String getTwitterOauthTokenSecret() {
        return twitterOauthTokenSecret;
    }

    /**
     * @param twitterOauthTokenSecret The twitter_oauth_token_secret
     */
    public void setTwitterOauthTokenSecret(String twitterOauthTokenSecret) {
        this.twitterOauthTokenSecret = twitterOauthTokenSecret;
    }

    /**
     * @return The twitterXOauthExpires
     */
    public String getTwitterXOauthExpires() {
        return twitterXOauthExpires;
    }

    /**
     * @param twitterXOauthExpires The twitter_x_oauth_expires
     */
    public void setTwitterXOauthExpires(String twitterXOauthExpires) {
        this.twitterXOauthExpires = twitterXOauthExpires;
    }

    /**
     * @return The twitterScreenName
     */
    public String getTwitterScreenName() {
        return twitterScreenName;
    }

    /**
     * @param twitterScreenName The twitter_screen_name
     */
    public void setTwitterScreenName(String twitterScreenName) {
        this.twitterScreenName = twitterScreenName;
    }

    /**
     * @return The twitterName
     */
    public String getTwitterName() {
        return twitterName;
    }

    /**
     * @param twitterName The twitter_name
     */
    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    /**
     * @return The twitterProfileImageUrl
     */
    public String getTwitterProfileImageUrl() {
        return twitterProfileImageUrl;
    }

    /**
     * @param twitterProfileImageUrl The twitter_profile_image_url
     */
    public void setTwitterProfileImageUrl(String twitterProfileImageUrl) {
        this.twitterProfileImageUrl = twitterProfileImageUrl;
    }

    /**
     * @return The twitterProfileImageUrlHttps
     */
    public String getTwitterProfileImageUrlHttps() {
        return twitterProfileImageUrlHttps;
    }

    /**
     * @param twitterProfileImageUrlHttps The twitter_profile_image_url_https
     */
    public void setTwitterProfileImageUrlHttps(String twitterProfileImageUrlHttps) {
        this.twitterProfileImageUrlHttps = twitterProfileImageUrlHttps;
    }

    /**
     * @return The isActive
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The is_active
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.twitterUserId);
        dest.writeString(this.twitterOauthToken);
        dest.writeString(this.twitterOauthTokenSecret);
        dest.writeString(this.twitterXOauthExpires);
        dest.writeString(this.twitterScreenName);
        dest.writeString(this.twitterName);
        dest.writeString(this.twitterProfileImageUrl);
        dest.writeString(this.twitterProfileImageUrlHttps);
        dest.writeString(this.isActive);
        dest.writeString(this.status);
        dest.writeString(this.created);
        dest.writeString(this.modified);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }
}