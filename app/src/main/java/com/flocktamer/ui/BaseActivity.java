package com.flocktamer.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.interfaces.OnEmailClickPermission;
import com.flocktamer.utils.AppPreference;

import java.util.regex.PatternSyntaxException;

/**
 * Base activity for all the passenger UI designs to implement the common functionality
 * Created by amandeep on 9/10/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public FrameLayout activityContent = null;
    //    private boolean doubleBackToExitPressedOnce;
    private AppPreference mAppPref;
    private Toolbar mToolbar = null;
    private TextView mTitleText;
    private CustomTextView mSortText;
    private int mLayoutId = 0;
    private SparseIntArray mErrorString= new SparseIntArray();
    private OnEmailClickPermission mOnEmailPermission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (TextView) findViewById(R.id.tv_toolbar);
        activityContent = (FrameLayout) this.findViewById(R.id.activity_content);
        mAppPref = AppPreference.newInstance(this);
        mSortText = (CustomTextView) mToolbar.findViewById(R.id.tv_toolbar_sort);

        // find the actionbarSize
        final TypedArray styledAttributes = getApplicationContext().getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
//        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

    }

    public void setBaseContentView(int layoutId) {
        getLayoutInflater().inflate(layoutId, getParentView());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                // do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * initialize Toolbar
     *
     * @param title        : title to set to Toolbar
     * @param isHomeEnable : check is Back Button Enable or not
     */
    public Toolbar initToolBar(String title, boolean isHomeEnable) {
//
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(title);
        mTitleText.setTypeface(Typeface.DEFAULT_BOLD);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() == null) {
            return null;
        }
        if (isHomeEnable) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setTitle(null);

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);
        }

return mToolbar;
    }


    /**
     * initialize Toolbar
     *
     * @param title                 : title to set to Toolbar
     * @param isHomeEnable          : check is Back Button Enable or not
     * @param customDrawable;custom back button
     */

    public void initToolBar(String title, boolean isHomeEnable, int customDrawable) {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(title);
        mTitleText.setTypeface(Typeface.DEFAULT_BOLD);
        setSupportActionBar(mToolbar);
        if (isHomeEnable) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(customDrawable);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);
        }

    }


    public void removeToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    protected FrameLayout getParentView() {
        return activityContent;
    }

    protected void setMyContentView(int layout) {
        getLayoutInflater().inflate(layout, getParentView());
    }

    /**
     * replace given fragment to layout
     *
     * @param fragment        fragment to replace
     * @param layoutToReplace layout which hold fragment/
     */
    public void replaceFragment(Fragment fragment, int layoutToReplace) {
        replaceFragment(fragment, layoutToReplace, true);
    }

    /**
     * initialize common methods and utilities
     */
    /**
     * initialize all views of activity under this method
     */
    protected abstract void initViews();


    /**
     * initialize manager classes and user program under this method
     */
    protected abstract void initManagers();


    /**
     * override onBackPressed to maintain Fragment Stack OnBackPress
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
//            if (doubleBackToExitPressedOnce) {
//                finish();
//                return;
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
////            finish();
            return;
        }

        super.onBackPressed();
    }

    public void setText(TextView view, String data) {
        if (view != null && data != null) {
            view.setText(data);
        }
    }

    public String getViewText(TextView view) {
        return view.getText().toString();
    }

    public Toolbar getToolBar() {
        if (mToolbar == null) {
            initToolBar("", true);
        }
        return mToolbar;
    }

    /**
     * logout from facebook if user any access token is stored in app.
     */
    public void logOutFb() {
//        AccessToken token = AccessToken.getCurrentAccessToken();
//        if (token != null) {
//            LoginManager manager = LoginManager.getInstance();
//            manager.logOut();
//        }
    }


    /**
     * @param fragment        fragment to replace
     * @param layoutToReplace layout to replace
     * @param storeInStack    boolean store in back stack
     */
    public void replaceFragment(Fragment fragment, int layoutToReplace, Boolean storeInStack) {

        try {
            View view = findViewById(layoutToReplace);
            if (mLayoutId == 0) {
                if (view != null) {
                    mLayoutId = layoutToReplace;
                }
            }
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(mLayoutId, fragment);
            if (storeInStack) {
                transaction.addToBackStack(fragment.getClass().getCanonicalName());
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param fragment        fragment to replace
     * @param layoutToReplace layout to replace
     * @param storeInStack    boolean store in back stack
     * @param isAdd           boolean add or replace
     */
    public void replaceFragment(Fragment fragment, int layoutToReplace, boolean storeInStack, boolean isAdd) {

        try {
            View view = findViewById(layoutToReplace);
            if (mLayoutId == 0) {
                if (view != null) {
                    mLayoutId = layoutToReplace;
                }
            }
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            if (isAdd) {
//                transaction.setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up, 0, R.anim.slide_in_up);
                transaction.add(mLayoutId, fragment);
            } else {
                transaction.replace(mLayoutId, fragment);
            }

            if (storeInStack) {
                transaction.addToBackStack(fragment.getClass().getCanonicalName());
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

//        switch (requestCode) {
//            case Constants.REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                       /* mlocationData.setonLocationListener(null);
//                        mlocationData.disconnect();
//                        initiateLocation();*/
//                        break;
//                    case Activity.RESULT_CANCELED:
//
//                        break;
//                    default:
//                        break;
//                }
//                break;
//        }
    }

    /**
     * @param message Message to show in SnackBar
     * @param length  TSnackbar.LengthShort ,TSnackbar.LengthLong
     */
    public void showSnackbar(String title, String message, int length) {


//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.custom_toast,
//                (ViewGroup) findViewById(R.id.toast_layout_root));
//
//        TextView text = (TextView) layout.findViewById(R.id.text_error_subtitle);
//        text.setText(message);
//        TextView textTitle = (TextView) layout.findViewById(R.id.text_error_title);
//        text.setText(message);
//        textTitle.setText(title);
//        if(title.isEmpty()){
//            textTitle.setVisibility(View.GONE);
//        }else{
//            textTitle.setVisibility(View.VISIBLE);
//        }

    }

    /**
     * @param serviceClass (name of service to check)
     * @return is service running or not
     */
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param content    content to be splitted
     * @param expression expression like @,# etc
     * @return return String's 1st element
     * @throws NullPointerException
     * @throws PatternSyntaxException
     */
    public String splitString(String content, String expression) throws NullPointerException, PatternSyntaxException {

        String[] arr = content.split(expression);
        return arr[0];
    }

    public void start(Class<? extends BaseActivity> activity) {
        startActivity(new Intent(this, activity));
    }

    public void start(Class<? extends BaseActivity> activity, Bundle data) {
        startActivity(new Intent(this, activity));
    }


    public Dialog showProgress() {
//        ProgressDialog mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setMessage("Loading...");
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.show();
//        return mProgressDialog;

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_progress);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_dialog);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate_logo);

        RotateAnimation rotate = new RotateAnimation(0, 25000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(50000);
        rotate.setRepeatMode(Animation.INFINITE);

        rotate.setInterpolator(new LinearInterpolator());


        imageView.startAnimation(rotate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

        return dialog;

    }

    public void logoutApp() {
        if (getAppPref().isUserLogin()) {
            logOutFb();
            getAppPref().clearSession();
            Intent dashboardIntent = new Intent(this, WelcomeActivity.class);
            dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dashboardIntent);
            Toast.makeText(this, R.string.access_token_expire, Toast.LENGTH_SHORT).show();
        }

    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppPref = AppPreference.newInstance(this);
    }

    public AppPreference getAppPref() {
        if (mAppPref == null) {
            mAppPref = AppPreference.newInstance(this);
        }
        return mAppPref;
    }

    public CustomTextView getSortTextView() {
        if (mSortText == null) {
            mSortText = (CustomTextView) mToolbar.findViewById(R.id.tv_toolbar_sort);
        }
        return mSortText;
    }

    public void setOnEmailPermissionInstance(OnEmailClickPermission permissionInstance)
    {
        mOnEmailPermission = permissionInstance;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            onPermissionsGranted(requestCode);
                for(String emailPermission:permissions)
                {
                    if(emailPermission.equalsIgnoreCase(Manifest.permission.READ_CONTACTS) && mOnEmailPermission!=null)
                    {
                        mOnEmailPermission.onEmailPermissionClick();
                    }
                }

        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }


    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BaseActivity.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
//            onPermissionsGranted(requestCode);
        }
    }


//    public android.app.FragmentManager.OnBackStackChangedListener getBackStackChangedListener() {
//
//        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
//            public void onBackStackChanged() {
//                FragmentManager manager = getFragmentManager();
//                if (manager != null) {
//                    int backStackEntryCount = manager.getBackStackEntryCount();
//                    if (backStackEntryCount == 0) {
//                        finish();
//                    }
//
//                    Fragment fragment = manager.findFragmentByTag(ConfirmPhoneFragment.class.getCanonicalName());
//                    fragment.onResume();
//                }
//            }
//        };
//        return result;
//    }
}