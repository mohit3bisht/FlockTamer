package com.flocktamer;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.utils.AppPreference;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amandeepsingh on 23/8/16.
 */
public class InstanceIdService extends FirebaseInstanceIdService {

    private String TAG = getClass().getCanonicalName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        AppPreference appPref = AppPreference.newInstance(this);
        if (appPref != null) {
            if (!appPref.getGcmregistrationId().equalsIgnoreCase(refreshedToken)) {
                appPref.setGcmRegsitration(refreshedToken);
                HashMap<String, String> data = new HashMap<>();
                data.put("device_type", "android");
                data.put("device_token", refreshedToken);

                RetrofitBuilder.getService().updateToken(appPref.getAccessToken(), data).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        }
        Log.e(TAG, refreshedToken);
    }
}
