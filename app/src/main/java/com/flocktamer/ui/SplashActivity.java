package com.flocktamer.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.ads.NativeAd;
import com.flocktamer.Flock;
import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.utils.AppPreference;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    private NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        generateKeyHash();
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
    protected void onResume() {
        super.onResume();
        if (checkPlayService(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this))) {

//            Log.e("token", FirebaseInstanceId.getInstance().getToken());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callNextActivity();
                }
            }, Constants.SPLASH_TIME);
        } else {

        }
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
//            Logger.logStackTrace(TAG,e);
        }
        return "";
    }

    private void callNextActivity() {

//        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        String deviceId = md5(android_id).toUpperCase();
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {

        if (!AppPreference.newInstance(SplashActivity.this).getAccessToken().isEmpty()) {
            Intent intent = new Intent(SplashActivity.this, TabsInitActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        SplashActivity.this.finish();
//            }
//        });
//        alertDialogBuilder.setTitle("Device ID");
//        alertDialogBuilder.setMessage("" + deviceId);
//
//        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.show();


    }

    private boolean checkPlayService(int PLAY_SERVICE_STATUS) {
        boolean status = false;
        switch (PLAY_SERVICE_STATUS) {
            case ConnectionResult.API_UNAVAILABLE:
                //API is not available
                Log.e("PlayService", "unavailable");
                break;
            case ConnectionResult.NETWORK_ERROR:
                //Network error while connection
                Log.e("PlayService", "network error");
                break;
            case ConnectionResult.RESTRICTED_PROFILE:
                //Profile is restricted by google so can not be used for play services
                Log.e("PlayService", "restricted ");
                break;
            case ConnectionResult.SERVICE_MISSING:
                //service is missing
                Log.e("PlayService", "missing service");
                break;
            case ConnectionResult.SIGN_IN_REQUIRED:
//                Log.e("PlayService", "sign in required");
                //service available but user not signed in
                break;
            case ConnectionResult.SUCCESS:

                status = true;
                break;
        }

        if (!status) {
            DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SplashActivity.this.finish();
                }
            };
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, PLAY_SERVICE_STATUS, PLAY_SERVICE_STATUS, cancelListener);
            dialog.show();
            dialog.setOnCancelListener(cancelListener);
        }
        return status;
    }
    public void generateKeyHash() {
        try {
          String  unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            FlockTamerLogger.createLog("Device Id:  "+unique_id);
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.flocktamer",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                FlockTamerLogger.createLog("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

}
