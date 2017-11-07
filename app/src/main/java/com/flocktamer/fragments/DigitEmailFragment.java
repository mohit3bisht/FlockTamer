package com.flocktamer.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DigitEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DigitEmailFragment extends BaseFragment implements TextWatcher {

    private static int mKeyFragment;
    @BindView(R.id.til_digit_fname)
    TextInputLayout mName;
    @BindView(R.id.til_digit_lname)
    TextInputLayout mLastName;
    @BindView(R.id.til_digit_email)
    TextInputLayout mEmail;
    @BindView(R.id.til_digit_phone)
    TextInputLayout mPhone;
    @BindView(R.id.sp_digit_ccode)
    AppCompatSpinner mCountryCode;
    @BindView(R.id.ll_phone)
    LinearLayout mCountryLayout;

    private HashMap<String, String> mData;
    private String[] countryCodeArr;

    public DigitEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data
     * @param keyFragment
     * @return A new instance of fragment DigitEmailFragment.
     */
    public static DigitEmailFragment newInstance(HashMap<String, String> data, int keyFragment) {
        DigitEmailFragment fragment = new DigitEmailFragment();
        fragment.mData = data;
        mKeyFragment = keyFragment;
        return fragment;
    }

    @Override
    protected void initViews(View view) {

        if (mKeyFragment == Constants.KEY_DIGIT_FRAGMENT) {
            mCountryLayout.setVisibility(View.GONE);
        } else {
            getBaseActivity().initToolBar("Twitter Login", false);
        }
        countryCodeArr = getResources().getStringArray(R.array.array_countrycode);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.tv_spinner, countryCodeArr);
        mCountryCode.setAdapter(adapter);
        mName.getEditText().addTextChangedListener(this);
        mLastName.getEditText().addTextChangedListener(this);
        mEmail.getEditText().addTextChangedListener(this);
        mPhone.getEditText().addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_digit_email, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    @OnClick(R.id.tv_digit_continue)
    void onContinueClick() {
        String fName = mName.getEditText().getText().toString().trim();
        String lName = mLastName.getEditText().getText().toString().trim();
        String email = mEmail.getEditText().getText().toString().trim();

        String phone = countryCodeArr[mCountryCode.getSelectedItemPosition()] + mPhone.getEditText().getText().toString().trim();

        if (checkValidation(fName, lName, email, phone)) {
            mData.put("email", email);
            mData.put("first_name", fName);
            mData.put("last_name", lName);

            if (mKeyFragment == Constants.KEY_TWITTER_FRAGMENT) {
                mData.put("phone", phone);
            }
            callDigitRegistrationApi(mData);
        }
    }

    private void callDigitRegistrationApi(HashMap<String, String> mData) {

        Call<UserModel> service = null;
        if (mKeyFragment == Constants.KEY_TWITTER_FRAGMENT) {
            service = RetrofitBuilder.getService().twitterRegistration(mData);
        } else {
            service = RetrofitBuilder.getService().digitRegistration(mData);
        }


        final Dialog dialog = getBaseActivity().showProgress();
        service.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
//                        getAppPref().setUserLoginDetails(response.body().getUser());
//                        getBaseActivity().start(TabsInitActivity.class);
                        if(mKeyFragment == Constants.KEY_TWITTER_FRAGMENT) {
                            getAppPref().setUserLoginDetails(response.body().getUser());
                            getBaseActivity().start(TabsInitActivity.class);
//                            replaceFragment(ConfirmPhoneFragment.newInstance(response.body().getUser().getId(), response.body().getUser().getPhone()));
                        }else{
                            getAppPref().setUserLoginDetails(response.body().getUser());
                            getBaseActivity().start(TabsInitActivity.class);
                        }

                    } else {
                        Util.showErrorSnackBar(getView(), response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private boolean checkValidation(String fname, String lname, String email, String number) {

        if (fname.isEmpty()) {
            mName.setErrorEnabled(true);
            mName.setError("First name not found");
            return false;
        } else if (lname.isEmpty()) {
            mLastName.setErrorEnabled(true);
            mLastName.setError("Last name not found");
            return false;
        }
        if (mKeyFragment == Constants.KEY_TWITTER_FRAGMENT) {
            if (number.isEmpty()) {
                mPhone.setErrorEnabled(true);
                mPhone.setError("Enter Phone Number");
                return false;
            }
        }

        if (email.isEmpty()) {
            mEmail.setErrorEnabled(true);
            mEmail.setError("Email not found");
            return false;
        } else if (!Validation.isValidEmail(email)) {
            mEmail.setErrorEnabled(true);
            mEmail.setError("Email is not Valid");
            return false;
        }

        return true;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mName.setError(null);
        mName.setErrorEnabled(false);
        mLastName.setError(null);
        mLastName.setErrorEnabled(false);
        mEmail.setError(null);
        mEmail.setErrorEnabled(false);
        mPhone.setError(null);
        mPhone.setErrorEnabled(false);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
