package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.UserModel;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.flocktamer.utils.Validation;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Use the {@link EmailLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailLoginFragment extends BaseFragment implements TextWatcher {

    @BindView(R.id.til_login_email)
    TextInputLayout mEmail;
    @BindView(R.id.til_login_password)
    TextInputLayout mPassword;

    public EmailLoginFragment() {
        // Required empty public constructor
    }

    public static EmailLoginFragment newInstance() {
        return new EmailLoginFragment();
    }

    @Override
    protected void initViews(View view) {

        mEmail.getEditText().addTextChangedListener(this);
        mPassword.getEditText().addTextChangedListener(this);

        view.findViewById(R.id.rl_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getBaseActivity().hideKeyboard(getView());
                String email = mEmail.getEditText().getText().toString().trim();
                String pass = mPassword.getEditText().getText().toString().trim();

                if (isValidData(email, pass)) {
                    callLoginAPI(email, pass);
                }
            }
        });
    }

    private void callLoginAPI(String email, String pass) {

        HashMap<String, String> object = new HashMap<>();
        object.put("email", email);
        object.put("password", pass);
        object.put("device_type", "android");
        object.put("device_token", FirebaseInstanceId.getInstance().getToken());

        Call<UserModel> service = RetrofitBuilder.getService().emailLogin(object);
        final Dialog dialog =  getBaseActivity().showProgress();
        service.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                    if (response.body().getUser().getStatus().trim().equalsIgnoreCase("1")) {
                        getAppPref().setUserLoginDetails(response.body().getUser());
                        getBaseActivity().start(TabsInitActivity.class);
                    } else {
                        replaceFragment(ConfirmPhoneFragment.newInstance(response.body().getUser().getId(),response.body().getUser().getPhone()));
                    }
                } else {
                    Util.showErrorSnackBar( getBaseActivity().activityContent, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Util.showErrorSnackBar( getBaseActivity().activityContent, t.getMessage());
            }
        });

    }


    public boolean isValidData(String email, String pass) {
        if (!Validation.isValidEmail(email)) {
            mEmail.setErrorEnabled(true);
            mEmail.setError("Email is Invalid");
            return false;
        } else if (!Validation.isValidPassword(pass)) {
            mPassword.setErrorEnabled(true);
            mPassword.setError("Password length is too short");
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_login, container, false);
        getBaseActivity().initToolBar("Login", true);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void removeErrors() {
        mEmail.setError(null);
        mEmail.setErrorEnabled(false);
        mPassword.setError(null);
        mPassword.setErrorEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getBaseActivity().onBackPressed();
                break;
            default:
                // do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        removeErrors();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
