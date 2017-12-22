package com.flocktamer;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.flocktamer.ui.SkimArticleActivity;
import com.flocktamer.ui.SplashActivity;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Keys;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by amandeepsingh on 23/8/16.
 */
public class FcmMessagingService extends FirebaseMessagingService {

    public static boolean isBackgroundRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        //If your app is the process in foreground, then it's not in running in background
                        return false;
                    }
                }
            }
        }


        return true;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // FlockTamerLogger.createLog("Message::::: " + remoteMessage.getData().get("message"));
        Log.e("Message", remoteMessage.getData().get("message"));
        FlockTamerLogger.createLog("Message Notification : " + remoteMessage.getData().get("message"));
        FlockTamerLogger.createLog("data : " + remoteMessage.getData());
        FlockTamerLogger.createLog("data : " + remoteMessage.toString());
        JSONObject dataJSON = new JSONObject(remoteMessage.getData());

       /* try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("feed"));
            if (jsonObject.has("type")){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if (isBackgroundRunning(this)) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.dog_loginpage);
            mBuilder.setContentTitle("FlockTamer");
            mBuilder.setContentText(remoteMessage.getData().get("message"));
            mBuilder.setAutoCancel(true);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(remoteMessage.getData().get("message")));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            if (dataJSON.has("feed")) {
                FlockTamerLogger.createLog("Has feed");
                try {
                    JSONObject feedJsonObject = new JSONObject(dataJSON.get("feed").toString());
                    if (feedJsonObject.get("type").toString().equals("newsalert")) {
                        FlockTamerLogger.createLog("NewsAlert");
                        Intent resultIntent = new Intent(this, SkimArticleActivity.class);
                        resultIntent.putExtra(Keys.URL, feedJsonObject.get("uri").toString());
                        resultIntent.putExtra(Keys.TITLE, feedJsonObject.get("title").toString());
//                        resultIntent.setAction(Intent.ACTION_MAIN);
//                        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        stackBuilder = TaskStackBuilder.create(this);
                        stackBuilder.addParentStack(SkimArticleActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                    } else {
                        FlockTamerLogger.createLog("Twitter");
                        Intent resultIntent = new Intent(this, SplashActivity.class);
                        resultIntent.setAction(Intent.ACTION_MAIN);
                        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        stackBuilder.addParentStack(SplashActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                    }
                } catch (JSONException e) {
                    FlockTamerLogger.createLog("Error: " + e.getMessage());
                    e.printStackTrace();
                }
// Adds the Intent that starts the Activity to the top of the stack
            } else {
                Intent resultIntent = new Intent(this, SplashActivity.class);
                resultIntent.setAction(Intent.ACTION_MAIN);
                resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                stackBuilder.addParentStack(SplashActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);

                FlockTamerLogger.createLog("fgfkfhfjkfghjf");
            }
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(111, mBuilder.build());
        }

    }
}
