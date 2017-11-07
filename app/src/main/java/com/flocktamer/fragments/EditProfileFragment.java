package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.UserModel;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends BaseFragment {

    @BindView(R.id.til_editprofile_fname)
    TextInputLayout fName;
    @BindView(R.id.til_editprofile_lname)
    TextInputLayout lName;
    @BindView(R.id.til_editprofile_email)
    TextInputLayout email;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    protected void initViews(View view) {
        fName.getEditText().setText(getAppPref().getFirstName());
        lName.getEditText().setText(getAppPref().getLastName());
        email.getEditText().setText(getAppPref().getUserEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    @OnClick(R.id.tv_editprofile_submit)
    void onSubmit() {
        HashMap<String, String> data = new HashMap<>();
        data.put("first_name", fName.getEditText().getText().toString().trim());
        data.put("last_name", lName.getEditText().getText().toString().trim());

        Call<UserModel> service = RetrofitBuilder.getService().editProfile(getAppPref().getAccessToken(), data);
        final Dialog dialog =  getBaseActivity().showProgress();
        service.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }


//                mAppPref.setName(fName.getEditText().getText().toString().trim(), lName.getEditText().getText().toString().trim());
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                        getAppPref().setUserLoginDetails(response.body().getUser());
                        getBaseActivity().start(TabsInitActivity.class);
                    } else {
//                        Util.showErrorSnackBar(getView(), response.body().getMessage());
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
}
