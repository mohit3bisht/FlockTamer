package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.flocktamer.Flock;
import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.UserModel;
import com.flocktamer.ui.DashboardActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;
import com.flocktamer.utils.Validation;
import com.google.firebase.iid.FirebaseInstanceId;

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
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends BaseFragment implements TextWatcher {

    @BindView(R.id.til_signup_fname)
    TextInputLayout tilFname;
    @BindView(R.id.til_signup_lname)
    TextInputLayout tilLname;
    @BindView(R.id.til_signup_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_signup_password)
    TextInputLayout tilPassword;

    @BindView(R.id.til_signup_rpassword)
    TextInputLayout tilRetypePassword;
    @BindView(R.id.til_signup_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.til_signup_promo)
    TextInputLayout mPromoCode;
    @BindView(R.id.btn_promo_apply)
    Button mApplyBtn;

    @BindView(R.id.sp_signup_ccode)
    AppCompatSpinner spCountryCode;
    String countryCode[];

    public SignupFragment() {
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    protected void initViews(View view) {

        String token = FirebaseInstanceId.getInstance().getToken();

        tilFname.getEditText().addTextChangedListener(this);
        tilLname.getEditText().addTextChangedListener(this);
        tilEmail.getEditText().addTextChangedListener(this);
        tilPhone.getEditText().addTextChangedListener(this);
        tilPassword.getEditText().addTextChangedListener(this);
        mPromoCode.getEditText().addTextChangedListener(this);
        tilRetypePassword.getEditText().addTextChangedListener(this);
        countryCode = getResources().getStringArray(R.array.array_countrycode);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.tv_spinner, countryCode);
        spCountryCode.setAdapter(adapter);
        view.findViewById(R.id.rl_signup_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaseActivity().hideKeyboard(getView());
                String fname = tilFname.getEditText().getText().toString().trim();
                String lname = tilLname.getEditText().getText().toString().trim();
                String email = tilEmail.getEditText().getText().toString().trim();
                final String phone = tilPhone.getEditText().getText().toString().trim();
                String password = tilPassword.getEditText().getText().toString().trim();
                String rPassword = tilRetypePassword.getEditText().getText().toString().trim();
                String promoCode = mPromoCode.getEditText().getText().toString().trim();

                if (isDataValid(fname, lname, email, phone, password, rPassword)) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("first_name", fname);
                    data.put("last_name", lname);
                    data.put("email", email);
                    data.put("phone", countryCode[spCountryCode.getSelectedItemPosition()] + phone);
                    data.put("password", password);
                    data.put("device_type", "android");
                    data.put("device_token", FirebaseInstanceId.getInstance().getToken());
                    data.put("promo_name", promoCode);
                    FlockTamerLogger.createLog("Sign Up Param: "+data);
                    Call<UserModel> webCall = RetrofitBuilder.getService().signUp(data);
                    final Dialog dialog = getBaseActivity().showProgress();
                    webCall.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            FlockTamerLogger.createLog("Response1: " + response.raw().toString());

                            UserModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                                getAppPref().setUserLoginDetails(model.getUser());

                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {
                                Util.showErrorSnackBar(getView(), model.getMessage());
                            }


//                            if (response.code() == 200) {
//                                try {
//                                    String responseString = response.body().string();
//                                    JSONObject object = new JSONObject(responseString);
//                                    String status = object.getString("status");
//
//                                    if (status.equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
////                                        JSONObject dataObj = object.getJSONObject("data");
////                                        String userId = dataObj.getString("id");
////
////                                        replaceFragment(ConfirmPhoneFragment.newInstance(userId,countryCode[spCountryCode.getSelectedItemPosition()] + phone));
//
//                                        } else {
//                                        Util.showErrorSnackBar(getView(), object.getString("message"));
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }


                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isDataValid(String fname, String lname, String email, String phone, String password, String rPassword) {

        if (fname.isEmpty()) {
            tilFname.setErrorEnabled(true);
            tilFname.setError("First Name can't be empty");
            return false;
        } else if (lname.isEmpty()) {
            tilLname.setErrorEnabled(true);
            tilLname.setError("Last Name can't be empty");
            return false;
        } else if (email.isEmpty()) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Email can't be empty");
            return false;
        } else if (!Validation.isValidEmail(email)) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Email is not valid");
            return false;
        } else if (phone.isEmpty()) {
            tilPhone.setErrorEnabled(true);
            tilPhone.setError("Phone number can't be empty");
            return false;
        } else if (!Validation.isValidMobile(phone)) {
            tilPhone.setErrorEnabled(true);
            tilPhone.setError("Phone number is not valid");
            return false;
        } else if (!Validation.isValidPassword(password)) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Minimum Password length be 6");
            return false;
        } else if (!Validation.isMatchedPassword(password, rPassword)) {
            tilRetypePassword.setErrorEnabled(true);
            tilRetypePassword.setError("Password did not matched with Retype Password");
            return false;
        } else if (mPromoCode.isEnabled() && !mPromoCode.getEditText().getText().toString().trim().isEmpty()) {
            mPromoCode.setErrorEnabled(true);
            mPromoCode.setError("Promo code invalid.");
            return false;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getBaseActivity().initToolBar("Sign Up", true);
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
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

    private void removeErrors() {
        tilFname.setError(null);
        tilFname.setErrorEnabled(false);

        tilLname.setError(null);
        tilLname.setErrorEnabled(false);

        tilEmail.setError(null);
        tilEmail.setErrorEnabled(false);

        tilPhone.setError(null);
        tilPhone.setErrorEnabled(false);

        mPromoCode.setError(null);
        mPromoCode.setErrorEnabled(false);

        tilPassword.setError(null);
        tilPassword.setErrorEnabled(false);

        tilRetypePassword.setError(null);
        tilRetypePassword.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @OnClick(R.id.tv_signup_terms)
    void onTermsClick() {
        String url = Constants.TERMS_URL;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.tv_signup_policy)
    void onPolicyClick() {
        String url = Constants.POLICY_URL;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.btn_promo_apply)
    void onApplyBtnClick() {

        InputMethodManager imm = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPromoCode.getWindowToken(), 0);


        String promoCode = mPromoCode.getEditText().getText().toString().trim();

        if (promoCode.isEmpty() || promoCode.length() < 4) {
            Util.showErrorSnackBar(getView(), "Promo code invalid.");
            return;
        }
        HashMap<String, String> map = new HashMap();
        map.put("promo_name", promoCode);
        Call<ResponseBody> call = RetrofitBuilder.getService().checkPromocode(map);
        final Dialog dialog = getBaseActivity().showProgress();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response.code() == 200) {
                    try {
                        String data = response.body().string();
                        JSONObject jsonData = new JSONObject(data);
                        if (jsonData.get("status").toString().equals(Constants.STATUS_SUCCESS)) {
                            mApplyBtn.setEnabled(false);
                            mPromoCode.setEnabled(false);
                        } else {
                            mPromoCode.setErrorEnabled(true);
                            mPromoCode.setError(jsonData.get("message").toString());
                            Util.showErrorSnackBar(getView(), jsonData.get("message").toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }
}
