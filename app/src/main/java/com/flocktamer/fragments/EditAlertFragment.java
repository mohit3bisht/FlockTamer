package com.flocktamer.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.adapters.EditAlertAdapter;
import com.flocktamer.adapters.KeywordSkimAdapter;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.GetKeywordsModel;
import com.flocktamer.model.KeywordsModel;
import com.flocktamer.model.childmodel.GetKeywordsData;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Keys;
import com.flocktamer.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAlertFragment extends BaseFragment implements View.OnClickListener {


    /**
     * Instantiates a new Manage skim fragment.
     */
    public EditAlertFragment() {
        // Required empty public constructor
    }

    /**
     * New instance manage skim fragment.
     *
     * @return the manage skim fragment
     */
    public static EditAlertFragment newInstance() {
        EditAlertFragment fragment = new EditAlertFragment();

        return fragment;
    }

    private EditText mSkimKeywords, mTitle;
    private EditAlertAdapter mKeywordSkimAdapter;
    private TextView mAddKeywords;
    /**
     * The M keywords list.
     */
    RecyclerView mKeywordsList;
    private TextView mSaveKeywords;
    private List<String> mSelectedList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert_editing, container, false);

        initViews(view);
        if (Util.isNetworkAvailable(getActivity())) {
            getKeywords();
        } else {
            Toast.makeText(getActivity(), getString(R.string.you_are_not_connected_with_internet), Toast.LENGTH_SHORT).show();
        }
        mSkimKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    if (v.length()>0){

                        mSelectedList.add(v.getText().toString());
                        mKeywordSkimAdapter.notifyDataSetChanged();
                        mSkimKeywords.setText("");
                    }
                    //  Toast.makeText(getActivity(), "Action Done!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        return view;
    }

    @Override
    protected void initViews(View view) {
        mSkimKeywords = (EditText) view.findViewById(R.id.et_skim_keywords);
        mTitle = (EditText) view.findViewById(R.id.et_title);
        mAddKeywords = (TextView) view.findViewById(R.id.btn_add_keywords);
        mKeywordsList = (RecyclerView) view.findViewById(R.id.rv_keywords_grid);
        mSaveKeywords = (TextView) view.findViewById(R.id.tv_save_keywords);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mKeywordsList.setLayoutManager(gridLayoutManager);
        mKeywordSkimAdapter = new EditAlertAdapter(mSelectedList);
        mKeywordsList.setAdapter(mKeywordSkimAdapter);
        mSaveKeywords.setOnClickListener(this);
        mAddKeywords.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {

            case R.id.tv_save_keywords:
            //    FlockTamerLogger.createLog("Size::::::::: " + mSelectedList.size());
                if (validityCheck()) {
                    saveKeywords();

                }
                break;
            case R.id.btn_add_keywords:
                if (mSkimKeywords.getText().toString().length()>0){

                    mSelectedList.add(mSkimKeywords.getText().toString());
                    mKeywordSkimAdapter.notifyDataSetChanged();
                    mSkimKeywords.setText("");
                }
                break;
        }
    }

    /**
     * The Keywords.
     */
    String keywords = "";

    private void saveKeywords() {

        HashMap<String, String> data = new HashMap<>();
        keywords="";
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (i == 0) {
                keywords = keywords + mSelectedList.get(i);
            } else {

                keywords = keywords + "," + mSelectedList.get(i);
            }
        }
        FlockTamerLogger.createLog("Keywords::::: " + keywords);
        data.put(Keys.KEYWORDS, keywords);
        data.put(Keys.TITLE, mTitle.getText().toString());
        Call<KeywordsModel> service = RetrofitBuilder.getService().saveKeywords(getAppPref().getAccessToken(), data);
        final Dialog dialog = showProgress();
        service.enqueue(new Callback<KeywordsModel>() {
            @Override
            public void onResponse(Call<KeywordsModel> call, Response<KeywordsModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                if (response.code() == 200) {
                    KeywordsModel keywordsModel = response.body();
                    FlockTamerLogger.createLog("Message: " + response.body().getMessage());
                    FlockTamerLogger.createLog("Status: " + response.body().getStatus());
                    FlockTamerLogger.createLog("Response1: " + response.raw().toString());

                    Toast.makeText(getActivity(), keywordsModel.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (response.code() == 401) {
                    getAppPref().clearSession();
                    Intent dashboardIntent = new Intent(getBaseActivity(), WelcomeActivity.class);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboardIntent);
                    Toast.makeText(getBaseActivity(), R.string.access_token_expire, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<KeywordsModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void getKeywords() {

        Call<GetKeywordsModel> service = RetrofitBuilder.getService().getKeywords(getAppPref().getAccessToken());
        final Dialog dialog = showProgress();
        service.enqueue(new Callback<GetKeywordsModel>() {
            @Override
            public void onResponse(Call<GetKeywordsModel> call, Response<GetKeywordsModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                if (response.code() == 200) {
                    GetKeywordsModel getKeywordsModel = response.body();

//                    FlockTamerLogger.createLog("Keyords Size:::: " + getKeywordsModel.getData().getKeywords().size());
                    mSelectedList.clear();
                    GetKeywordsData getKeywordsData = getKeywordsModel.getData();
                    if (getKeywordsModel.getStatus().equals("success")){

                        mSelectedList = getKeywordsData.getKeywords();
                        mTitle.setText(getKeywordsData.getTitle());
                        mKeywordSkimAdapter = new EditAlertAdapter(mSelectedList);
                        mKeywordsList.setAdapter(mKeywordSkimAdapter);
                    }
                    else {
                        Toast.makeText(getActivity(), getKeywordsModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 401) {
                    getAppPref().clearSession();
                    Intent dashboardIntent = new Intent(getBaseActivity(), WelcomeActivity.class);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboardIntent);
                    Toast.makeText(getBaseActivity(), R.string.access_token_expire, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<GetKeywordsModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private boolean check = false;

    /**
     * checking the validation of the fields(Edittext like email, password etc)
     *
     * @return
     */
    private boolean validityCheck() {

        if (!Util.checkValidUserName(mTitle, getActivity())) {
            check = false;
        } else {
            if (Util.isNetworkAvailable(getActivity())) {


                try {


                    check = true;

                } catch (Exception e1) {
                    check = false;
                    e1.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.you_are_not_connected_with_internet), Toast.LENGTH_SHORT).show();
                check = false;
            }

        }
        return check;
    }

}
