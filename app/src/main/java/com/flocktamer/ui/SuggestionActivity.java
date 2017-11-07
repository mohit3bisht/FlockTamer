package com.flocktamer.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.http.RetrofitService;
import com.flocktamer.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionActivity extends BaseActivity {

    @BindView(R.id.et_suggestion)
    EditText mText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_suggestion);
        initToolBar("Suggestions", true);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initManagers() {

    }

    @OnClick(R.id.btn_suggestions)
    void onClick() {
        String text = mText.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Please write Suggestions", Toast.LENGTH_SHORT).show();
        } else {
            RetrofitService service = RetrofitBuilder.getService();
            HashMap<String, String> data = new HashMap<>();
            data.put("content", text);
            Call<ResponseBody> call = service.suggestion(getAppPref().getAccessToken(), data);
            final Dialog dialog = showProgress();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (response.code() == 200) {
                        try {
                            String data = response.body().string();
                            JSONObject obj = new JSONObject(data);
                            String status = obj.getString("status");
                            Toast.makeText(getBaseContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            if (status.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                                onBackPressed();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 403) {
                        logoutApp();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
