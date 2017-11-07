package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.UserModel;
import com.flocktamer.ui.DashboardActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPhoneFragment extends BaseFragment {


    private static String mPhoneNumber;
    @BindView(R.id.til_confirmcode)
    TextInputLayout mCode;

    @BindView(R.id.tv_phone_number)
    CustomTextView mCurrentPhone;

    private String mUserId;

    public ConfirmPhoneFragment() {
        // Required empty public constructor

    }

    public static ConfirmPhoneFragment newInstance(String userId, String phoneNumber) {
        ConfirmPhoneFragment fragment = new ConfirmPhoneFragment();
        fragment.mUserId = userId;
        mPhoneNumber = phoneNumber;
        return fragment;
    }


    @Override
    protected void initViews(View view) {

        mCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCode.setError(null);
                mCode.setErrorEnabled(false);
//                mCode.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        view.findViewById(R.id.tv_confirm_resendcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callResendCodeApi();
            }
        });


        view.findViewById(R.id.rl_confirm_phone_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callVerifyCodeAPI();
            }
        });

        mCurrentPhone.setText("A text message with a verification code has been sent to " + mPhoneNumber);
    }

    private void callResendCodeApi() {
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", mUserId);
        Call<ResponseBody> callAPI = RetrofitBuilder.getService().resendCode(data);
        final Dialog dialog = getBaseActivity().showProgress();
        callAPI.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    String data = response.body().string();
                    JSONObject obj = new JSONObject(data);
                    String status = obj.getString("status").trim();
                    if (status.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                        Util.showSucessSnackBar(getView(), obj.getString("message"));
                    } else {
                        Util.showErrorSnackBar(getView(), obj.getString("message"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void callVerifyCodeAPI() {

        String code = mCode.getEditText().getText().toString().trim();
        if (code.length() < 4) {
            mCode.setErrorEnabled(true);
            mCode.setError("Code is Invalid");
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", mUserId);
        data.put("verification_code", code);

        Call<UserModel> callAPI = RetrofitBuilder.getService().verifyCode(data);
        final Dialog dialog = getBaseActivity().showProgress();
        callAPI.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                UserModel model = response.body();
                if (model.getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                    getAppPref().setUserLoginDetails(model.getUser());

                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    Util.showErrorSnackBar(getView(), model.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getBaseActivity().initToolBar("Confirm Phone", false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.fragment_confirm_phone, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.tv_confirm_renter_phone)
    void onReEnterPhone() {
        replaceFragment(ChangePhoneFragment.newInstance(mUserId, this));
    }

    public void updatePhoneNumber(String phoneNumber) {
        mCurrentPhone.setText("A text message with a verification code has been sent to " + phoneNumber);
    }
}
