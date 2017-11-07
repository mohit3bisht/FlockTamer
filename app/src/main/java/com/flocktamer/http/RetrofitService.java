package com.flocktamer.http;

import com.flocktamer.model.BaseModel;
import com.flocktamer.model.ColumnModel;
import com.flocktamer.model.CompanyModel;
import com.flocktamer.model.GetKeywordsModel;
import com.flocktamer.model.KeywordsModel;
import com.flocktamer.model.PickupModel;
import com.flocktamer.model.StockModel;
import com.flocktamer.model.TwitterFeedModel;
import com.flocktamer.model.TwitterUserModel;
import com.flocktamer.model.UserModel;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by amandeepsingh on 23/8/16.
 */
public interface RetrofitService {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<UserModel> emailLogin(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("promocode_valid")
    Call<ResponseBody> checkPromocode(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("facebook_login")
    Call<UserModel> facebookLogin(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("twitter_login")
    Call<UserModel> twitterLogin(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("change_phone")
    Call<ResponseBody> reEnterPhone(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("facebook_registration")
    Call<UserModel> facebookRegister(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("digit_login")
    Call<UserModel> digitLogin(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("digit_registration")
    Call<UserModel> digitRegistration(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("twitter_registration")
    Call<UserModel> twitterRegistration(@Body HashMap<String, String> data);


    @Headers("Content-Type: application/json")
    @POST("user_column_list")
    Call<ColumnModel> columnList(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("signup")
    Call<UserModel> signUp(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("verify")
    Call<UserModel> verifyCode(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("resendcode")
    Call<ResponseBody> resendCode(@Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("get_column_info")
    Call<TwitterFeedModel> columnInfo(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("get_column_info")
    Call<CompanyModel> companyColumnInfo(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("save_keyword_by_user_id")
    Call<KeywordsModel> saveKeywords(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("skim_details_by_user_id")
    Call<GetKeywordsModel> getKeywords(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("get_column_list")
    Call<PickupModel> companyColumnList(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("get_rss_company_list")
    Call<StockModel> companyStockList(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("save_user_selected_columns")
    Call<ResponseBody> saveUserData(@Header("Authorization") String header, @Body HashMap<String, Object> data);

    @Headers("Content-Type: application/json")
    @POST("get_user_twitter_accounts")
    Call<TwitterUserModel> getTwitterAccounts(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("save_user_twitter_accounts")
    Call<TwitterUserModel> addTwitterAccount(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("suggestion")
    Call<ResponseBody> suggestion(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("remove_user_twitter_accounts")
    Call<TwitterUserModel> removeTwitterAccount(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("twitter_tweet_like")
    Call<ResponseBody> likeTweet(@Header("Authorization") String header, @Body HashMap<String, Object> data);

    @Headers("Content-Type: application/json")
    @POST("twitter_tweet_share")
    Call<ResponseBody> retweet(@Header("Authorization") String header, @Body HashMap<String, Object> data);

    @Headers("Content-Type: application/json")
    @POST("twitter_tweet_reply")
    Call<ResponseBody> replyTweet(@Header("Authorization") String header, @Body HashMap<String, Object> data);

    @Headers("Content-Type: application/json")
    @POST("share_email")
    Call<ResponseBody> emailTweet(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("getLatestTwitterFeed")
    Call<TwitterFeedModel> getLatestTwitterFeed(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("getLatestRssFeed")
    Call<ResponseBody> getRefreshCompanyFeed(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("edit_profile")
    Call<UserModel> editProfile(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("update_device_info")
    Call<ResponseBody> updateToken(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("contact_us")
    Call<ResponseBody> contactUs(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("get_user_key_topic_list")
    Call<ResponseBody> getKeyTopics(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("tweet_details_by_feedid\n")
    Call<TwitterFeedModel> tweetByFeedId(@Header("Authorization") String header, @Body HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("logout")
    Call<BaseModel> logout(@Header("Authorization") String header);
    @Multipart
    @POST("compose_tweet")
    Call<BaseModel> uploadPostImage(@Header("Authorization") String header, @PartMap HashMap<String, Object> nameValuePairs, @Part MultipartBody.Part file);

}
