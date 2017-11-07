package com.flocktamer.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.flocktamer.Flock;
import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.ColumnModel;
import com.flocktamer.model.childmodel.Column;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabsInitActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_init);
        callForTabs();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void callForTabs() {
        Call<ColumnModel> service = RetrofitBuilder.getService().columnList(getAppPref().getAccessToken());
        final Dialog dialog = showProgress();
        FlockTamerLogger.createLog("Access Token : "+getAppPref().getAccessToken());
        service.enqueue(new Callback<ColumnModel>() {

            @Override
            public void onResponse(Call<ColumnModel> call, Response<ColumnModel> response) {
                if (dialog.isShowing()) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int responseCode = response.code();
                if (responseCode == 200) {
                  //  FlockTamerLogger.createLog("Resssssss:"+response.body().toString());
                  /*  try {
                        FlockTamerLogger.createLog("Response:::: "+response.body().getData().getColumns());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    FlockTamerLogger.createLog("Response:::: "+response.body().getMessage());
                   FlockTamerLogger.createLog("Response:::: "+response);
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                        ArrayList<Column> columns = response.body().getData().getColumns();
                       // if (columns != null && columns.size() > 0) {
                            Intent tabsIntent = new Intent(TabsInitActivity.this, UserSelectionActivity.class);
                            tabsIntent.putParcelableArrayListExtra(Constants.COLUMN_LIST, columns);
                            tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(tabsIntent);
                      /*  } else {
                            Intent dashboardIntent = new Intent(TabsInitActivity.this, DashboardActivity.class);
                            dashboardIntent.putExtra("isFromNavigation", false);
                            dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dashboardIntent);
                        }*/
                    } else {
                        Intent dashboardIntent = new Intent(TabsInitActivity.this, DashboardActivity.class);
                        dashboardIntent.putExtra("isFromNavigation", false);
                        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dashboardIntent);
                    }
                } else if (responseCode == 401) {
                    getAppPref().clearSession();
                    Intent dashboardIntent = new Intent(TabsInitActivity.this, WelcomeActivity.class);
                    dashboardIntent.putExtra("isFromNavigation", false);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboardIntent);
                    Toast.makeText(getApplicationContext(), getString(R.string.access_token_expire), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ColumnModel> call, Throwable t) {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                FlockTamerLogger.createLog("Errroeee: "+t.getMessage());
                if (t instanceof UnknownHostException) {
                    Util.noInternetAlert(TabsInitActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          TabsInitActivity.this.finish();
                        }
                    });
                }
            }
        });
    }
}
