package com.flocktamer.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.fragments.EditAlertFragment;
import com.flocktamer.fragments.ManageSkimFragment;

public class EditAlertKeywordsActivity extends BaseActivity {
private ImageView mRightToolImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_alert_keywords);
        replaceFragment(EditAlertFragment.newInstance(), R.id.frame_manage_skim);
        Toolbar toolbar =  initToolBar("Edit Alert Keywords",true);
        mRightToolImage = (ImageView)toolbar.findViewById(R.id.iv_right_tool);
       // mRightToolImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }

}
