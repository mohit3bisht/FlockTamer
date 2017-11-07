package com.flocktamer;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSettings;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by amandeepsingh on 12/8/16.
 */
public class Flock extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    //Staging Server.
//    private static final String TWITTER_KEY = "iFIMgPcbEo5X0wyFCN8ods00d";
//    private static final String TWITTER_SECRET = "vZjesSHVuw2H7Y2rUswVwG0ffB2pUIDsFFHOdcjgcnUv6qWkzL";


    // Live Server
//    private static final String TWITTER_KEY = "56lkb1I92LFA6AojJsNWKFoUR";
//    private static final String TWITTER_SECRET = "Wy2IW3c2i2oYgutWLS9DtcRPEJ5MV471guWNAKsoD2eYHUOKNN";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("BuildType", BuildConfig.BUILD_TYPE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AdSettings.addTestDevice("1e3f67b3113c5525d5813e8a9e8caf98");
        try {
            MMSDK.initialize(this);
        } catch (MMException e) {
            e.printStackTrace();
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build(), new Twitter(authConfig), new Crashlytics());
//        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build(), new Twitter(authConfig));

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
