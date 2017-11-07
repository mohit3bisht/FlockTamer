package com.flocktamer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.flocktamer.R;
import com.flocktamer.fragments.AddAccountFragment;

public class AddAccountActivity extends BaseActivity {

    private AddAccountFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_add_account);
        initToolBar("Add Account", true);
        fragment = AddAccountFragment.newInstance();
        replaceFragment(fragment, R.id.content_add_account);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
