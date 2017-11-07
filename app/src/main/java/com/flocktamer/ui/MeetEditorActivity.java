package com.flocktamer.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.flocktamer.R;

public class MeetEditorActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_meet_editor);
//        getToolBar().setVisibility(View.VISIBLE);
        initToolBar("Meet The Editor",true);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }
}
