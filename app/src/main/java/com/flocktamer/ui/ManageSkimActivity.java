package com.flocktamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.fragments.ConfirmPhoneFragment;
import com.flocktamer.fragments.ManageSkimFragment;

public class ManageSkimActivity extends BaseActivity {
private ImageView mRightToolImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_manage_skim);
        replaceFragment(ManageSkimFragment.newInstance(), R.id.frame_manage_skim);
        Toolbar toolbar =  initToolBar("Custom News Alerts",true);
        mRightToolImage = (ImageView)toolbar.findViewById(R.id.iv_right_tool);
        mRightToolImage.setVisibility(View.VISIBLE);
        mRightToolImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageSkimActivity.this,EditAlertKeywordsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }

}
