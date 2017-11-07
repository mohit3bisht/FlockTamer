package com.flocktamer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.flocktamer.model.childmodel.User;

import java.io.File;


public class AppPreference {

    private static final String KEY_DIGIT_ID_STR = "digit_id_str";
    private static final String KEY_USER_ACCOUNT_STATUS = "account_status";
    private static final String KEY_DIGIT_ISVERIFIED = "digit_is_verified";
    private static final String KEY_USER_STATUS = "user_status";
    private static final String mPREF_NAME = "APP_PREF";
    private static final String KEY_ACESSTOKEN = "access_token";
    private static final String KEY_USER_IMAGE = "user_image";
    private static final String KEY_USER_F_NAME = "user_fname";
    private static final String KEY_USER_L_NAME = "user_lname";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_FB_KEY = "fb_login";
    private static final String KEY_USERID = "id";
    private static final String KEY_USERTYPE = "user_type";
    private static final String KEY_GCM_REG_TOKEN = "gcm_reg_token";
    private static final String KEY_DIGIT_ID = "digit_id";
    static AppPreference sessionObj;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;


    public AppPreference(@NonNull Context context) {
        int mPRIVATE_MODE = 0;
        mPref = context.getSharedPreferences(mPREF_NAME, mPRIVATE_MODE);
        mEditor = mPref.edit();
    }

    public static AppPreference newInstance(@NonNull Context context) {
        if (sessionObj == null)
            sessionObj = new AppPreference(context);
        return sessionObj;
    }

    public String getUserID() {
        return mPref.getString(KEY_USERID, "");
    }

    public void setUserID(String id) {

        mEditor.putString(KEY_USERID, id);
        mEditor.commit();
    }

    public String getAccessToken() {
        return mPref.getString(KEY_ACESSTOKEN, "");
    }

    public String getFirstName() {
        return mPref.getString(KEY_USER_F_NAME, "");
    }

    public void setName(String fname, String lname) {
        mEditor.putString(KEY_USER_F_NAME, fname);
        mEditor.putString(KEY_USER_L_NAME, lname);
        mEditor.commit();
    }

    public String getLastName() {
        return mPref.getString(KEY_USER_L_NAME, "");
    }

    public void setUserLoginDetails(User userLogin) {

        mEditor.putString(KEY_USERID, userLogin.getId());
        mEditor.putString(KEY_USERTYPE, userLogin.getUserType());
//        mEditor.putString(KEY_DIGIT_ID, userLogin.getDigitId());
//        mEditor.putString(KEY_DIGIT_ID_STR, userLogin.getDigitIdStr());

        mEditor.putString(KEY_USER_F_NAME, userLogin.getFirstName());
        mEditor.putString(KEY_USER_L_NAME, userLogin.getLastName());
        mEditor.putString(KEY_USER_EMAIL, userLogin.getEmail());
        mEditor.putString(KEY_USER_ACCOUNT_STATUS, userLogin.getAccountStatus());
        mEditor.putString(KEY_ACESSTOKEN, userLogin.getAccessToken());
        mEditor.putString(KEY_DIGIT_ISVERIFIED, userLogin.getDigitIsVerified());

        mEditor.putString(KEY_USER_STATUS, userLogin.getStatus());
        mEditor.commit();
    }

    public User getUserDetail() {
        User login = new User();
        login.setId(mPref.getString(KEY_USERID, ""));
        login.setAccessToken(mPref.getString(KEY_ACESSTOKEN, ""));
        login.setUserType(mPref.getString(KEY_USERTYPE, ""));
        login.setFirstName(mPref.getString(KEY_USER_F_NAME, ""));
        login.setLastName(mPref.getString(KEY_USER_L_NAME, ""));
        login.setEmail(mPref.getString(KEY_USER_EMAIL, ""));
        login.setAccountStatus(mPref.getString(KEY_USER_ACCOUNT_STATUS, ""));
        login.setDigitIsVerified(mPref.getString(KEY_DIGIT_ISVERIFIED, ""));
        login.setStatus(mPref.getString(KEY_USER_STATUS, ""));

        return login;
    }


    public String getUserEmail() {
        return mPref.getString(KEY_USER_EMAIL, "");
    }

    public boolean isUserLogin() {
        return !mPref.getString(KEY_ACESSTOKEN, "").isEmpty();
    }

//    public void setLoginFromFb(boolean loginFromFb) {
//        mEditor.putBoolean(KEY_FB_LOGIN, loginFromFb);
//        mEditor.commit();
//    }

//    public boolean isFbLogin() {
//        return mPref.getBoolean(KEY_FB_LOGIN, false);
//    }

    public void clearSession() {
        mEditor.clear().commit();
    }


    public void setGcmRegsitration(String gcm_token) {
        mEditor.putString(KEY_GCM_REG_TOKEN, gcm_token);
        mEditor.commit();
    }

    public String getGcmregistrationId() {
        String data = mPref.getString(KEY_GCM_REG_TOKEN, "");
        return data;
    }

}
