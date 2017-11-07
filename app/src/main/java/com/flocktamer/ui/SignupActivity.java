package com.flocktamer.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.flocktamer.R;
import com.flocktamer.fragments.ConfirmPhoneFragment;
import com.flocktamer.fragments.EmailLoginFragment;
import com.flocktamer.fragments.SignupFragment;
import com.flocktamer.utils.Constants;

public class SignupActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
        setBaseContentView(R.layout.activity_signup);

        if (getIntent().getExtras() != null) {
            int code = getIntent().getIntExtra(Constants.INTENT_SIGNUP_FRAGMENT, 0);

            if (code == Constants.LOGIN_FRAGMENT) {
                replaceFragment(EmailLoginFragment.newInstance(), R.id.content_signup);
            } else if (code == Constants.CONFIRM_FRAGMENT) {
                String userId = getIntent().getStringExtra(Constants.INTENT_USER_ID);
                String phoneNumber = getIntent().getStringExtra(Constants.INTENT_MOBILE);
                replaceFragment(ConfirmPhoneFragment.newInstance(userId, phoneNumber), R.id.content_signup);
            } else {
                replaceFragment(SignupFragment.newInstance(), R.id.content_signup);
            }
        } else {
            replaceFragment(SignupFragment.newInstance(), R.id.content_signup);
        }
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
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }


}
