package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.UserModel;
import com.flocktamer.model.childmodel.User;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.flocktamer.utils.Validation;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FacebookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacebookDetailFragment extends BaseFragment implements TextWatcher {

    @BindView(R.id.til_fblogin_email)
    TextInputLayout mEmail;

    @BindView(R.id.til_fblogin_phone)
    TextInputLayout mPhone;

    @BindView(R.id.sp_fblogin_ccode)
    AppCompatSpinner mCountryCode;
    private String email;
    private String id;
    private String firstName;
    private String lastName;
    private String[] countryCodeArr;


    public FacebookDetailFragment() {
        // Required empty public constructor
    }

    public static FacebookDetailFragment newInstance(String id, String first_name, String last_name, String email) {
        FacebookDetailFragment fragment = new FacebookDetailFragment();
        fragment.id = id;
        fragment.firstName = first_name;
        fragment.lastName = last_name;
        fragment.email = email;
        return fragment;
    }

    @Override
    protected void initViews(View view) {

        getBaseActivity().initToolBar("Facebook Login",false);
        mEmail.getEditText().addTextChangedListener(this);
        mPhone.getEditText().addTextChangedListener(this);
        countryCodeArr = getResources().getStringArray(R.array.array_countrycode);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.tv_spinner, countryCodeArr);
        mCountryCode.setAdapter(adapter);

        mEmail.getEditText().setText(email);
        if (!email.isEmpty()) {
            mEmail.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", id);
        outState.putString("fname", firstName);
        outState.putString("lname", lastName);
        outState.putString("email", email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facebook_detail, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            //probably orientation change
            id = savedInstanceState.getString("id");
            firstName = savedInstanceState.getString("fname");
            lastName = savedInstanceState.getString("lname");
            email = savedInstanceState.getString("email");
        }
        initViews(view);
        return view;
    }


    @OnClick(R.id.btn_fblogin_continue)
    void onContinueClick() {

        String email = mEmail.getEditText().getText().toString().trim();
        String phone = mPhone.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setErrorEnabled(true);
            mEmail.setError("Enter Email");
            return;
        }


        if (!Validation.isValidEmail(email)) {
            mEmail.setErrorEnabled(true);
            mEmail.setError("Invalid email");
            return;
        }

        if (phone.isEmpty()) {
            mPhone.setErrorEnabled(true);
            mPhone.setError("Phone number Required");
            return;
        }

        if (!Validation.isValidMobile(phone)) {
            mPhone.setErrorEnabled(true);
            mPhone.setError("Invalid Mobile number");
            return;
        }

        HashMap<String, String> data = new HashMap();
        data.put("fb_id", id);
        data.put("phone", countryCodeArr[mCountryCode.getSelectedItemPosition()] + phone);
        data.put("first_name", firstName);
        data.put("last_name", lastName);
        data.put("email", email);
        data.put("device_type", "android");
        data.put("device_token", FirebaseInstanceId.getInstance().getToken());

        callAPI(data);
    }

    private void callAPI(HashMap<String, String> data) {
        Call<UserModel> call = RetrofitBuilder.getService().facebookRegister(data);
        final Dialog progressDialog =  getBaseActivity().showProgress();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if(response.code()==200)
                {
                    if(response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS))
                    {
                        getAppPref().setUserLoginDetails(response.body().getUser());
                        getBaseActivity().start(TabsInitActivity.class);
                    }else{
                        Util.showErrorSnackBar(getView(), response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void removeErrors() {
        mEmail.setError(null);
        mEmail.setErrorEnabled(false);
        mPhone.setError(null);
        mPhone.setErrorEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        removeErrors();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
