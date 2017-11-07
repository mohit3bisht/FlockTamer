package com.flocktamer.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.fragments.DigitEmailFragment;
import com.flocktamer.fragments.FacebookDetailFragment;
import com.flocktamer.utils.Constants;

import java.util.HashMap;

public class DigitActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_digit);
        initToolBar("Digit Login", true);
        if (getIntent() != null) {

            if (getIntent().getIntExtra(Constants.DIGIT_FRAGMENT_KEY, Constants.KEY_DIGIT_FRAGMENT) == Constants.KEY_DIGIT_FRAGMENT) {
                HashMap<String, String> data = (HashMap<String, String>) getIntent().getExtras().get(Constants.DIGIT_INFO);
                if (data != null) {
                    replaceFragment(DigitEmailFragment.newInstance(data, Constants.KEY_DIGIT_FRAGMENT), R.id.content_digit_email);
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else if(getIntent().getIntExtra(Constants.DIGIT_FRAGMENT_KEY, Constants.KEY_DIGIT_FRAGMENT) == Constants.KEY_FACEBOOK_FRAGMENT){
                String id = getIntent().getStringExtra("id");
                String fName = getIntent().getStringExtra("fname");
                String lName = getIntent().getStringExtra("lname");
                String email = getIntent().getStringExtra("email");
                replaceFragment(FacebookDetailFragment.newInstance(id, fName, lName, email), R.id.content_digit_email);
            }else{
                HashMap<String, String> data = (HashMap<String, String>) getIntent().getExtras().get(Constants.DIGIT_INFO);
                if (data != null) {
                    replaceFragment(DigitEmailFragment.newInstance(data,Constants.KEY_TWITTER_FRAGMENT), R.id.content_digit_email);
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
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
}
