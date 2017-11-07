package com.flocktamer.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.interfaces.WelcomeScreenInterface;
import com.flocktamer.ui.MeetEditorActivity;
import com.flocktamer.utils.FlockTamerLogger;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends BaseFragment {

    private WelcomeScreenInterface mCallback;
    @BindView(R.id.tv_header_3)
    CustomTextView mHeader3;
    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param callback
     * @return A new instance of fragment SplashFragment.
     */
    public static SplashFragment newInstance(WelcomeScreenInterface callback) {
        SplashFragment fragment = new SplashFragment();
        fragment.mCallback = callback;
        return fragment;
    }

    @Override
    protected void initViews(View view) {

        FirebaseInstanceId.getInstance().getToken();
        generateKeyHash();
        view.findViewById(R.id.rl_splash_getstart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mActivity.start(SignupActivity.class);
                mCallback.onGetStarted();
            }
        });

        Spanned string = Html.fromHtml(getString(R.string.splash_humans));
        SpannableString ss = new SpannableString(string);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
            startActivity(new Intent(getActivity(), MeetEditorActivity.class));
            }
        };

            int startIndex = string.toString().indexOf("Meet the Editor");
            int lastIndex = startIndex+"Meet the Editor".length();
        ss.setSpan(clickableSpan,startIndex , lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//where 22 and 27 are the starting and ending index of the String. Now word stack is clickable
// onClicking stack it will open NextActiivty

        mHeader3.setText(ss);
        mHeader3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    public void generateKeyHash() {
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
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
