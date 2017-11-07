package com.flocktamer.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.flocktamer.Flock;
import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;
import com.millennialmedia.InlineAd;
import com.millennialmedia.MMException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity {

    @BindView(R.id.et_contactus_title)
    EditText mTitle;

    @BindView(R.id.et_contactus_message)
    EditText mMessage;
    FrameLayout frameLayout;
    InlineAd inlineAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar("Contact FlockTamer", true);
        setBaseContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        initViews();
        initManagers();

        try {
            // NOTE: The ad container argument passed to the createInstance call should be the
            // view container that the ad content will be injected into.

            inlineAd = InlineAd.createInstance("238155", (ViewGroup) frameLayout);

            inlineAd.setListener(new InlineAd.InlineListener() {
                @Override
                public void onRequestSucceeded(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("333Inline Ad loaded.");
                }


                @Override
                public void onRequestFailed(InlineAd inlineAd, InlineAd.InlineErrorStatus errorStatus) {

                    FlockTamerLogger.createLog("333Error1: " + errorStatus.toString());
                }


                @Override
                public void onClicked(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("333Clicked: " + "Inline Ad clicked.");
                }


                @Override
                public void onResize(InlineAd inlineAd, int width, int height) {

                    FlockTamerLogger.createLog("333start: " + "Inline Ad starting resize.");
                }


                @Override
                public void onResized(InlineAd inlineAd, int width, int height, boolean toOriginalSize) {

                    FlockTamerLogger.createLog("333resize: " + "Inline Ad resized.");
                }


                @Override
                public void onExpanded(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("333expanded: " + "Inline Ad expanded.");
                }


                @Override
                public void onCollapsed(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("333collapsed: " + "Inline Ad collapsed.");
                }


                @Override
                public void onAdLeftApplication(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("333left: " + "Inline Ad left application.");
                }
            });

        } catch (MMException e) {
            FlockTamerLogger.createLog("333Error1 Error creating inline ad" + e);
            // abort loading ad
        }
      /*  if (inlineAd != null) {
            // set a refresh rate of 30 seconds that will be applied after the first request
            // inlineAd.setRefreshInterval(30000);

            // The InlineAdMetadata instance is used to pass additional metadata to the server to
            // improve ad selection
            FlockTamerLogger.createLog("Not null");
            final InlineAd.InlineAdMetadata inlineAdMetadata = new InlineAd.InlineAdMetadata().
                    setAdSize(InlineAd.AdSize.BANNER);

            inlineAd.request(inlineAdMetadata);
        } else {
            FlockTamerLogger.createLog("null");
        }*/


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

    @OnClick(R.id.btn_contact_us_submit)
    void onSubmitClick() {

        callApi(mTitle.getText().toString(), mMessage.getText().toString());
//
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jilly@flocktamer.com"});
//        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "jilly@flocktamer.com");
//        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle.getText().toString());
//        sendIntent.setType("plain/text");
//        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, mMessage.getText().toString());
//
//        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jilly@flocktamer.com"});
//        startActivity(sendIntent);
    }

    private void callApi(String title, String message) {

        HashMap<String, String> data = new HashMap<>();
        data.put("subject", title);
        data.put("content", message);
        Call<ResponseBody> call = RetrofitBuilder.getService().contactUs(getAppPref().getAccessToken(), data);
        FlockTamerLogger.createLog("Params: " + data.toString());
        final Dialog dialog = showProgress();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 401) {
                    logoutApp();
                } else if (response.code() == 200) {
                    try {
                        FlockTamerLogger.createLog("response: " + response.raw().toString());
                        JSONObject obj = new JSONObject(response.body().string());
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        if (obj.getString("status").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                            onBackPressed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Util.showErrorSnackBar(null, "Something went wrong.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (t instanceof UnknownHostException) {
                    Util.noInternetAlert(ContactUsActivity.this, null);
                }
            }
        });
    }

}
