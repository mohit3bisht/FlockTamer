package com.flocktamer.http;

import android.util.Log;

import com.flocktamer.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amandeepsingh on 23/8/16.
 */
public class RetrofitBuilder {
//staging
//        public static final String BASE_URL = "http://34.194.150.242/apis/";
    //    public static final String BASE_URL = "http://stage.flocktamer.com/apis/";
////    public static final String BASE_URL = "http://52.52.147.225/apis/";

//    public static final String BASE_URL = "https://www.flocktamer.com/apis/";
//    public static final String BASE_URL = "http://34.194.153.104/apis/";

    public static RetrofitService getService() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(RetrofitService.class);
    }
}
