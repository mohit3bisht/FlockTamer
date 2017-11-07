package com.flocktamer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.flocktamer.R;

/**
 * Created by amandeepsingh on 24/8/16.
 */
public class Util {

    public static void showErrorSnackBar(View view, String text) {
        snackBar(view, text, Color.RED);
    }

    public static void showSucessSnackBar(View view, String text) {
        snackBar(view, text, Color.GREEN);
    }

    private static void snackBar(View view, String text, int color) {
        if (view == null || text == null) {
            return;
        }

        showToast(view.getContext(), text);

//        Snackbar snackbar = Snackbar
//                .make(view, text, Snackbar.LENGTH_LONG);
//
//        View snackbarView = snackbar.getView();
////        snackbarView.setBackgroundColor(Color.RED);
//        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextSize(16);
//        textView.setTextColor(color);
//        snackbar.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void noInternetAlert(Context context, DialogInterface.OnClickListener listener) {

        if (!((Activity) context).isFinishing()) {
            //show dialog
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setNeutralButton("OK", listener);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder.setMessage("There is no internet connection. Please check your internet connection.");

            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.show();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }
    }
    /**
     * Hide keyboard.
     *
     * @param activity the activity
     */
    public static void hideKeyboard(Activity activity) {
        //start with an 'always hidden' command for the activity's window
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //now tell the IMM to hide the keyboard FROM whatever has focus in the activity
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);
        }
    }
    /**
     * Gets path.
     *
     * @param contentUri     the uri
     * @param context the context
     * @return the path
     */
    public static String getPath(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            if(cursor==null){
                Log.e("pic url",contentUri.getPath());
                return contentUri.getPath();
            }
            else {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                Log.e("pic url",contentUri.getPath());
                String path =cursor.getString(column_index);
                if(path!=null && !TextUtils.isEmpty(path)) {
                    return cursor.getString(column_index);
                }
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    /**
     * Decode sampled bitmap from file bitmap.
     *
     * @param path      the path
     * @param reqWidth  the req width
     * @param reqHeight the req height
     * @return the bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * On create dialog.
     *
     * @param context            the context
     * @param onclickListener    the onclick listener
     * @param title              the title
     * @param message            the message
     * @param postiveButtonText  the postive button text
     * @param negativeButtonText the negative button text
     */
    public static void onCreateDialog(Context context, DialogInterface.OnClickListener onclickListener, String title, String message, String postiveButtonText, String negativeButtonText) {

        if (context != null) {
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
            if (title != null && message != null) {

                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                if (postiveButtonText != null) {
                    alertDialog.setPositiveButton(postiveButtonText, onclickListener);
                }
                if (negativeButtonText != null) {
                    alertDialog.setNegativeButton(negativeButtonText, onclickListener);

                }
                if (postiveButtonText == null && negativeButtonText == null) {
                    alertDialog.setPositiveButton("OK", null);
                }
                alertDialog.show();

            }
        }


    }
    /**
     * Is network available boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
        //return false;
    }
    /**
     * Check valid user name boolean.
     *
     * @param userEditText the user edit text
     * @param context2     the context 2
     * @return the boolean
     */
    public static boolean checkValidUserName(EditText userEditText, Context context2) {
        if (TextUtils.isEmpty(userEditText.getText().toString())) {
            userEditText.setError(context2.getString(R.string.empty_filed));
            userEditText.requestFocus();
            return false;
        } else return true;
    }
}
