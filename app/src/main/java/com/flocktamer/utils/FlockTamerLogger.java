package com.flocktamer.utils;

import android.util.Log;

/**
 * Show log if flag is true
 */
public class FlockTamerLogger {
    public static String TAG = "FlockTamer";


    static boolean flag = false;

    /**
     * Factory method that display logs of this application
     *
     * @param message log that you want to show
     */
    public static void createLog(String message) {
        if (flag) {

            Log.v(TAG, message);
        }
    }
}
