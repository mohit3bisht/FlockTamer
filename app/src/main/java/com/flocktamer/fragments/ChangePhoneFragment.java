package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.flocktamer.utils.Validation;

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
 * A simple {@link } subclass.
 * Use the {@link ChangePhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePhoneFragment extends BaseFragment {

    private String mUserId;
    private ConfirmPhoneFragment mConfirmFragment;

    @BindView(R.id.et_reenter_phone)
    EditText mPhoneEditText;


    public ChangePhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangePhoneFragment.
     */
    public static ChangePhoneFragment newInstance(String userId, ConfirmPhoneFragment confirmFragment) {
        ChangePhoneFragment fragment = new ChangePhoneFragment();
        fragment.mUserId = userId;
        fragment.mConfirmFragment = confirmFragment;
        return fragment;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_phone, container, false);
        getBaseActivity().initToolBar("Re-Enter Number", true);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    @OnClick(R.id.rl_confirm_reenter_phone)
    void onContinue() {
        getBaseActivity().hideKeyboard(mPhoneEditText);
        final String phone = mPhoneEditText.getText().toString().trim();

        if (!Validation.isValidMobile(phone)) {
            Util.showErrorSnackBar(mPhoneEditText, "Phone number is not valid");
            return;
        }
        final HashMap<String, String> data = new HashMap<>();
        data.put("user_id", mUserId);
        data.put("phone", "+1" + phone);

        Call<ResponseBody> service = RetrofitBuilder.getService().reEnterPhone(data);
        final Dialog dialog = showProgress();
        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    String data = response.body().string();
                    Log.e("data", data);

                    JSONObject obj = new JSONObject(data);
                    String status = obj.getString("status").trim();
                    String message = obj.getString("message").trim();

                    Util.showErrorSnackBar(getView(), message);
                    if (Constants.STATUS_SUCCESS.equalsIgnoreCase(status)) {
//                        FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1);

                        getFragmentManager().popBackStack(mConfirmFragment.getTag(), 0);

                        replaceFragment(ConfirmPhoneFragment.newInstance(mUserId, "+1" + phone));
//                        mConfirmFragment.updatePhoneNumber("+1" + phone);
                        getBaseActivity().onBackPressed();
                    } else {

                    }
                    //This is bloody response
                    //{"status":"success","message":"Phone number updated and confirmation pin successfully resend.","data":{"user_id":"236"}}
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

}
