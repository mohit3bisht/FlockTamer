package com.flocktamer.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.adapters.SelectStockAdapter;
import com.flocktamer.adapters.StockAutoCompleteAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.StockModel;
import com.flocktamer.ui.DashboardActivity;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CompaniesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompaniesFragment extends BaseFragment {

    private static boolean mIsFromEdit;
    @BindView(R.id.rv_company_grid)
    RecyclerView mCompanyGrid;

    @BindView(R.id.ac_companylist)
    AutoCompleteTextView mAutoComplete;
    @BindView(R.id.tv_company_continue)
    CustomTextView mContinueBtn;

    List<StockModel.StockDetail> mSelectedList = new ArrayList<>();
    private SelectStockAdapter mGridAdapter;
    private StockModel.StockList mAllList;

    private boolean mIsFirstTabSelected;
    private HashMap<String, Object> mSelectedCategories;


    public CompaniesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isFirstTabSelected true if first Tab i.e I'll Pick is selected
     * @param data               fields to be sent to api
     * @return A new instance of fragment CompaniesFragment.
     */

    public static CompaniesFragment newInstance(boolean isFirstTabSelected, boolean isFromEdit, @NonNull HashMap<String, Object> data) {
        CompaniesFragment fragment = new CompaniesFragment();
        fragment.mIsFirstTabSelected = isFirstTabSelected;
        fragment.mSelectedCategories = data;
        mIsFromEdit = isFromEdit;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_companies, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {
        getBaseActivity().getSortTextView().setVisibility(View.GONE);
        callWebService();

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mGridAdapter = new SelectStockAdapter(mSelectedList);
        mCompanyGrid.setAdapter(mGridAdapter);

        mCompanyGrid.setLayoutManager(gridLayoutManager);


    }

    private void onAutoTextViewTextSelected(final ArrayAdapter<StockModel.StockDetail> adapter) {
        mAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapter.getItem(i).getId().equalsIgnoreCase("-1")) {
                    mAutoComplete.setText("");
                    getBaseActivity().hideKeyboard(getView());
                    return;
                }
                // check if user select 5 companies from I'll pick section
                if (mGridAdapter.getItemCount() == 5 && mIsFirstTabSelected) {
                    getBaseActivity().hideKeyboard(getView());
                    Util.showErrorSnackBar(getView(), "You cannot select more then 5 companies");
                } else if (!mSelectedList.contains(adapter.getItem(i))) {

                    mSelectedList.add(adapter.getItem(i));
                    mGridAdapter.notifyDataSetChanged();
                }

                mAutoComplete.setText("");
            }
        });

    }

    private void callWebService() {
        Call<StockModel> service = RetrofitBuilder.getService().companyStockList(getAppPref().getAccessToken());
        final Dialog dialog = showProgress();
        service.enqueue(new Callback<StockModel>() {
            @Override
            public void onResponse(Call<StockModel> call, Response<StockModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    mAllList = response.body().getData().getList();
                    List<StockModel.StockDetail> list;
                    if (mIsFirstTabSelected) {
                        list = response.body().getData().getList().getAll();

//                        if(mSelectedCategories==null || ((JsonArray) mSelectedCategories.get("rss_url_list")).size()==0)
//                        {
//
//                        }


                        for (int i = 0; i < list.size(); i++) {
                            StockModel.StockDetail item = list.get(i);
                            if (item.getSelectedColumn().equalsIgnoreCase("yes")) {
                                if (mIsFromEdit) {
                                    if (isAlreadySelectWithEdit(item.getId())) {
                                        mSelectedList.add(list.get(i));
                                    }
                                } else {
                                    mSelectedList.add(list.get(i));
                                }
                            } else {
                                if (mIsFromEdit) {

                                    JsonArray arr = (JsonArray) mSelectedCategories.get("rss_url_list");
                                    if (arr == null || arr.size() != 0) {
                                        if (isAlreadySelectWithEdit(item.getId())) {
                                            mSelectedList.add(list.get(i));
                                        }
                                    } else {

                                    }
                                }
                            }
                        }
                        mGridAdapter.notifyDataSetChanged();
                    } else {
                        list = response.body().getData().getList().getEditorChoice();
                        for (int i = 0; i < list.size(); i++) {
                            mSelectedList.add(list.get(i));
                        }
                        mGridAdapter.notifyDataSetChanged();
                    }

//                    StockAutoCompleteAdapter adapter = new StockAutoCompleteAdapter(list, mActivity, R.layout.tv_spinner);
//                    ArrayAdapter<StockModel.StockDetail> adapter = new ArrayAdapter<StockModel.StockDetail>(mActivity, R.layout.tv_spinner, list);

                    StockAutoCompleteAdapter adapter = new StockAutoCompleteAdapter(list, getBaseActivity(), R.layout.tv_spinner);
                    mAutoComplete.setAdapter(adapter);
                    onAutoTextViewTextSelected(adapter);
                } else if (response.code() == 401) {
                    getAppPref().clearSession();
                    Intent dashboardIntent = new Intent(getBaseActivity(), WelcomeActivity.class);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboardIntent);
                    Toast.makeText(getBaseActivity(), R.string.access_token_expire, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<StockModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private boolean isAlreadySelectWithEdit(String id) {

        JsonArray arr = (JsonArray) mSelectedCategories.get("rss_url_list");
        if (arr == null || arr.size() == 0) {
            return true;
        }
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).toString().equalsIgnoreCase(id)) {
                return true;
            }
        }

        return false;
    }

    @OnClick(R.id.tv_company_continue)
    void onContinueClick() {
        getBaseActivity().hideKeyboard(getView());
        if (mSelectedList.isEmpty()) {
            Util.showErrorSnackBar(getView(), "Add company stock names");
        } else {
//            mContinueBtn.setEnabled(false);
            JsonArray arr = new JsonArray();
            for (int i = 0; i < mSelectedList.size(); i++) {
                arr.add(Integer.valueOf(mSelectedList.get(i).getId()));
            }

            if (mIsFromEdit) {
                ((DashboardActivity) getActivity()).setRssData(arr);
                getBaseActivity().onBackPressed();
                return;
            }
            mSelectedCategories.put("rss_url_list", arr);

            Call<ResponseBody> service = RetrofitBuilder.getService().saveUserData(getAppPref().getAccessToken(), mSelectedCategories);

            final Dialog dialog = showProgress();
            service.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e("Error Code", response.code() + "");

                    if (response.code() == 200) {
                        try {
                            String data = response.body().string();
                            JSONObject obj = new JSONObject(data);

                            if (obj.getString("status").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                                getBaseActivity().start(TabsInitActivity.class);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 401) {
                        getAppPref().clearSession();
                        Intent dashboardIntent = new Intent(getBaseActivity(), WelcomeActivity.class);
                        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dashboardIntent);
                        Toast.makeText(getBaseActivity(), R.string.access_token_expire, Toast.LENGTH_SHORT).show();
                    } else {
                        mContinueBtn.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    mContinueBtn.setEnabled(true);
                    Log.e("Error ", t.getMessage());
                }
            });
        }

    }


}
