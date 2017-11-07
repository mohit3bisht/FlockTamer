package com.flocktamer.fragments;


import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.adapters.EditorAdapter;
import com.flocktamer.customview.BorderLayout;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.interfaces.SortComparetor;
import com.flocktamer.itemdecor.GridSpacingItemDecoration;
import com.flocktamer.model.PickupModel;
import com.flocktamer.model.StockModel;
import com.flocktamer.model.childmodel.PickupColumn;
import com.flocktamer.ui.DashboardActivity;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends BaseFragment implements View.OnClickListener, EditorAdapter.OnEditClickListener {

    private static boolean mIsFromNavigation;
    @BindView(R.id.tab1)
    BorderLayout tab1;
    @BindView(R.id.tab2)
    BorderLayout tab2;
    @BindView(R.id.rv_pick_categories)
    RecyclerView mPickList;
    @BindView(R.id.tv_categories_welcome)
    CustomTextView mWelcomeText;
    @BindView(R.id.tv_categories_editor)
    CustomTextView editorChoiceText;
    @BindView(R.id.tv_categories_i_pick)
    CustomTextView iWllPickText;
    @BindView(R.id.tv_company_continue)
    CustomTextView mContinueText;

    private boolean isIWillPickSelected = false;
    private JsonArray mRssJsonArr = new JsonArray();
    private List<PickupColumn> mColumns = new ArrayList<>();
    private EditorAdapter mAdapter;
    private boolean isCompanyAlreadySelected;
    private JsonArray mKeyTopicsJson = new JsonArray();

    public static CategoriesFragment newInstance(boolean isFromNavigation) {
        mIsFromNavigation = isFromNavigation;
        return new CategoriesFragment();
    }

    @Override
    protected void initViews(View view) {

        CustomTextView sortText = getBaseActivity().getSortTextView();
        if (mIsFromNavigation) {
            sortText.setVisibility(View.VISIBLE);
        } else {
            sortText.setVisibility(View.GONE);
        }

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                String fragmentName = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
                if (fragmentName.equalsIgnoreCase(CategoriesFragment.class.getName())) {

                    if (((DashboardActivity) getBaseActivity()).getRssData() != null) {
                        mRssJsonArr = ((DashboardActivity) getBaseActivity()).getRssData();
                    }
                    if (((DashboardActivity) getBaseActivity()).getKeyTopics() != null) {
                        mKeyTopicsJson = ((DashboardActivity) getBaseActivity()).getKeyTopics();
                    }


                    getBaseActivity().initToolBar("Choose Your topics", mIsFromNavigation);
                    if (isIWillPickSelected) {
                        getBaseActivity().getSortTextView().setVisibility(View.VISIBLE);
                    }

                }

            }
        });

        sortText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PickupColumn> items;
                if (isIWillPickSelected) {
                    items = mAdapter.getSelectedItems();

                    if (items.size() > 1) {
                        Collections.sort(items, new SortComparetor());
                        SortFragment sortFragment = SortFragment.newInstance(items, isCompanySelected(), mKeyTopicsJson, ((DashboardActivity) getBaseActivity()).getRssData());
                        addFragment(sortFragment);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
//        initViews(view);
        ButterKnife.bind(this, view);
//        Hi Hooman Hamzeh \nWhat are you interests?
        initViews(view);
        mWelcomeText.setText("Hi " + getAppPref().getFirstName() + " " + getAppPref().getLastName() + "\nWhat are your interests?");
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        isIWillPickSelected = mIsFromNavigation;

        mPickList.addItemDecoration(new GridSpacingItemDecoration(2, 5, false));
        callAPI();

        return view;
    }

    @Override
    public void onClick(View view) {

        /**
         if Tab 1 is clicked then set Bottom Border color
         of Tab1 to Transparent and Tab 2 color to gray.

         vice versa for Tab2 click event.
         */
        switch (view.getId()) {
            case R.id.tab1:
               onTab1Click();
                break;

            case R.id.tab2:
               onTab2Click();
                break;

            default:
                tab1.removeBoottomBorder();
                tab2.setBottomBorder();
                setIWillPickList();
                isIWillPickSelected = true;
                break;
        }
    }

    private void onTab2Click() {
        tab2.removeBoottomBorder();
        tab1.setBottomBorder();
        iWllPickText.setTypeface(null, Typeface.BOLD);
        editorChoiceText.setTypeface(null, Typeface.NORMAL);
        setIWillPickList();
        isIWillPickSelected = true;
        getBaseActivity().getSortTextView().setVisibility(View.VISIBLE);
    }

    private void onTab1Click() {
        tab1.removeBoottomBorder();
        tab2.setBottomBorder();
        iWllPickText.setTypeface(null, Typeface.NORMAL);
        editorChoiceText.setTypeface(null, Typeface.BOLD);
        setEditorChoice();
        isIWillPickSelected = false;
        getBaseActivity().getSortTextView().setVisibility(View.GONE);
    }


    private void callAPI() {
        Call<PickupModel> service = RetrofitBuilder.getService().companyColumnList(getAppPref().getAccessToken());

        final Dialog dialog = showProgress();
        service.enqueue(new Callback<PickupModel>() {
            @Override
            public void onResponse(Call<PickupModel> call, Response<PickupModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    mContinueText.setEnabled(true);
                    mColumns = response.body().getData().getColumns();
                    if (mIsFromNavigation) {
                        setIWillPickList();
                        onTab2Click();
                    } else {
                        setEditorChoice();
                        onTab1Click();
                    }

                    if (isKeyTopicAlreadySelected()) {
                        getKeyTopicData();
                    }

                } else if (response.code() == 401) {
                    getBaseActivity().logoutApp();
                }
            }

            @Override
            public void onFailure(Call<PickupModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void setIWillPickList() {
        if (!mColumns.isEmpty()) {
            mPickList.setAdapter(null);
            RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
            mPickList.setLayoutManager(manager);
            mAdapter = new EditorAdapter(mColumns, false, this);
            mPickList.setAdapter(mAdapter);

            isCompanyAlreadySelected = checkAlreadyCompanySlection(mColumns);
        }
    }

    private boolean checkAlreadyCompanySlection(List<PickupColumn> mColumns) {

        for (int i = 0; i < mColumns.size(); i++) {
            if (mColumns.get(i).getColumnName().equalsIgnoreCase("companies")) {
                return mColumns.get(i).getIsSelected().equalsIgnoreCase("yes");
            }
        }
        return false;
    }

    private boolean isCompanyInEditorChoice(List<PickupColumn> mColumns) {

        for (int i = 0; i < mColumns.size(); i++) {
            if (mColumns.get(i).getColumnName().equalsIgnoreCase("companies")) {
                return mColumns.get(i).getEditorChoice().equalsIgnoreCase("y");
            }
        }
        return false;
    }

    /**
     * set Editor Choice Adapter
     */
    private void setEditorChoice() {
        mPickList.setAdapter(null);
        if (!mColumns.isEmpty()) {
            List<PickupColumn> editorList = new ArrayList<>();
            for (int i = 0; i < mColumns.size(); i++) {
                if (mColumns.get(i).getEditorChoice().equalsIgnoreCase("y")) {
                    editorList.add(mColumns.get(i));
                }
            }

            RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
            mPickList.setLayoutManager(manager);
            mAdapter = new EditorAdapter(editorList, true, this);
            mPickList.setAdapter(mAdapter);
//            mPickList.addItemDecoration(new GridSpacingItemDecoration(2, 5, false));
        }
    }

    @OnClick(R.id.tv_company_continue)
    void onContiuneClick() {

//        mContinueText.setEnabled(false);
        HashMap<String, Object> data = new HashMap<>();
        JsonArray arr = new JsonArray();

        List<PickupColumn> items = new ArrayList<>();
        if (isIWillPickSelected) {
            items = mAdapter.getSelectedItems();
            Collections.sort(items, new SortComparetor());
//            addFragment(SortFragment.newInstance(items));
//            return;
            data.put("user_choice", "my_choice");
        } else {
            data.put("user_choice", "editor_choice");

            for (int k = 0; k < mColumns.size(); k++) {
                if (mColumns.get(k).getEditorChoice().equalsIgnoreCase("y")) {
                    items.add(mColumns.get(k));
                }
            }
        }

        for (int i = 0; i < items.size(); i++) {
            arr.add(Integer.valueOf(items.get(i).getId()));
        }
        data.put("key_topic_list", mKeyTopicsJson);
        data.put("column_list", arr);

        if ((isCompanySelected() && !isCompanyAlreadySelected) || (!isIWillPickSelected && isCompanyInEditorChoice(mColumns))) {
            replaceFragment(CompaniesFragment.newInstance(isIWillPickSelected, false, data));
        } else {
            if (isCompanyAlreadySelected && mRssJsonArr.size() == 0) {
                getSelectedCompanies(data);
            } else {
                data.put("rss_url_list", mRssJsonArr);
                saveUserDetail(data);
            }
        }
    }

    private void getSelectedCompanies(final HashMap<String, Object> data) {
        Call<StockModel> service = RetrofitBuilder.getService().companyStockList(getAppPref().getAccessToken());
        final Dialog dialog = showProgress();
        service.enqueue(new Callback<StockModel>() {
            @Override
            public void onResponse(Call<StockModel> call, Response<StockModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    List<StockModel.StockDetail> list;

                    list = response.body().getData().getList().getAll();
                    for (int i = 0; i < list.size(); i++) {
                        StockModel.StockDetail item = list.get(i);
                        if (item.getSelectedColumn().equalsIgnoreCase("yes")) {
                            mRssJsonArr.add(list.get(i).getId());
                        }
                    }
                    data.put("rss_url_list", mRssJsonArr);
                    data.put("key_topic_list", mKeyTopicsJson);
                    saveUserDetail(data);

//                    StockAutoCompleteAdapter adapter = new StockAutoCompleteAdapter(list, mActivity, R.layout.tv_spinner);
//                    ArrayAdapter<StockModel.StockDetail> adapter = new ArrayAdapter<StockModel.StockDetail>(mActivity, R.layout.tv_spinner, list);

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

    private void saveUserDetail(HashMap<String, Object> data) {
        if (mRssJsonArr == null)
            mRssJsonArr = new JsonArray();

        if (mKeyTopicsJson == null) {
            mKeyTopicsJson = new JsonArray();
        }

        data.put("key_topic_list", mKeyTopicsJson);
        FlockTamerLogger.createLog("Param: "+data);
        Call<ResponseBody> service = RetrofitBuilder.getService().saveUserData(getAppPref().getAccessToken(), data);
        final Dialog dialog = showProgress();
        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    try {
                        String data = response.body().string();
                        JSONObject obj = new JSONObject(data);
                        FlockTamerLogger.createLog("Res: "+data);
                        if (obj.getString("status").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                            getBaseActivity().start(TabsInitActivity.class);
                        } else {
                            Util.showErrorSnackBar(getView(), obj.getString("message"));
                            mContinueText.setEnabled(true);
                        }
                    } catch (IOException e) {
                        FlockTamerLogger.createLog("Error: "+e.getMessage());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        FlockTamerLogger.createLog("Error1: "+e.getMessage());
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {

                    getAppPref().clearSession();
                    Intent dashboardIntent = new Intent(getBaseActivity(), WelcomeActivity.class);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboardIntent);
                    Toast.makeText(getBaseActivity(), R.string.access_token_expire, Toast.LENGTH_SHORT).show();

                } else {
                    Util.showErrorSnackBar(getView(), "Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                FlockTamerLogger.createLog("Error: "+t.getMessage());
            }
        });
    }

    /**
     * check if company column is selected or not
     *
     * @return true if Company is selected from Topics.
     */
    private boolean isCompanySelected() {

        List<PickupColumn> selectedItems = mAdapter.getSelectedItems();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.get(i).getColumnName().equalsIgnoreCase("companies")) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if company column is selected or not
     *
     * @return true if Company is selected from Topics.
     */
    private boolean isKeyTopicAlreadySelected() {

        List<PickupColumn> selectedItems = mAdapter.getSelectedItems();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (Integer.valueOf(selectedItems.get(i).getId()) == 25) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEditClick(String position) {

        if (position.equalsIgnoreCase("25")) {
            addFragment(KeyTopicsFragment.newInstance());
        } else {
            HashMap<String, Object> data = new HashMap<>();
            if (mRssJsonArr != null) {
                data.put("rss_url_list", mRssJsonArr);
            } else {
                data.put("rss_url_list", new JSONArray());
            }
            addFragment(CompaniesFragment.newInstance(true, true, data));
        }
    }


    public void getKeyTopicData() {
        Call<ResponseBody> call = RetrofitBuilder.getService().getKeyTopics(getAppPref().getAccessToken());

        final Dialog dialog = getBaseActivity().showProgress();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    String data = response.body().string();
                    JsonObject obj = new Gson().fromJson(data, JsonObject.class);

                    mKeyTopicsJson = (obj.getAsJsonObject("data")).getAsJsonArray("List");

                } catch (IOException e) {
                    e.printStackTrace();
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
