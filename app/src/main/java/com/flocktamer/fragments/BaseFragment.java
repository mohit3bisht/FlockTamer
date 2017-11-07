package com.flocktamer.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.ui.BaseActivity;
import com.flocktamer.utils.AppPreference;

/**
 * Created by amandeep on 13/10/15.
 * Base Fragment for all Fragments
 */
public abstract class BaseFragment extends Fragment {
    private BaseActivity mActivity;
    private AppPreference mAppPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        mAppPref = AppPreference.newInstance(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mAppPref = AppPreference.newInstance(context);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mAppPref = AppPreference.newInstance(context);
        }
    }


    /**
     * Replace current view to given fragment from fragment
     *
     * @param fragment Fragment which is to be replaced.
     */
    protected void replaceFragment(Fragment fragment) {
        mActivity.replaceFragment(fragment, 10);
    }

    protected void replaceFragment(Fragment fragment, boolean addToBackStack) {
        mActivity.replaceFragment(fragment, 10, addToBackStack);
    }

    /**
     * Replace current view to given fragment from fragment
     *
     * @param fragment Fragment which is to be replaced.
     */
    protected void addFragment(Fragment fragment) {
        mActivity.replaceFragment(fragment, 10, true, true);
    }

    /**
     * initialize all views of activity under this method
     */
    protected abstract void initViews(View view);

    @Override
    public void onResume() {
        super.onResume();
        mAppPref = AppPreference.newInstance(getActivity());
        mActivity = (BaseActivity) getActivity();
    }


    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }

    public AppPreference getAppPref() {
        if (mAppPref == null) {
            mAppPref = AppPreference.newInstance(getActivity());
        }
        return mAppPref;
    }

    public Dialog showProgress() {
//        ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setMessage("Loading...");
//        mProgressDialog.setCanceledOnTouchOutside(false);
        if (isAdded()) {

            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.view_progress);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_dialog);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_logo);

            RotateAnimation rotate = new RotateAnimation(0, 25000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(50000);
            rotate.setRepeatMode(Animation.INFINITE);

            rotate.setInterpolator(new LinearInterpolator());

            imageView.startAnimation(rotate);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    // Prevent dialog close on back press button
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });

            if(!mActivity.isFinishing()){
            dialog.show();

            }

            return dialog;
        }
        return null;
    }
}
