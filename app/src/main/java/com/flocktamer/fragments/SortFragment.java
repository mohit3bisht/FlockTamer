package com.flocktamer.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flocktamer.R;
import com.flocktamer.WelcomeActivity;
import com.flocktamer.adapters.SortAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.draggable.OnStartDragListener;
import com.flocktamer.draggable.SimpleItemTouchHelperCallback;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.StockModel;
import com.flocktamer.model.childmodel.PickupColumn;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
 * Use the {@link SortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortFragment extends BaseFragment implements OnStartDragListener {

    private static boolean mIsCompanySelected;
    @BindView(R.id.rv_sort)
    RecyclerView mList;
    @BindView(R.id.tv_sort_continue)
    CustomTextView mContinue;
    private List<PickupColumn> mItems;
    private ItemTouchHelper mItemTouchHelper;
    private SortAdapter mAdapter;
    private boolean isFromMenu = false;
    private JsonArray mKeyTopicsJson;
    private JsonArray mStockList;

    public SortFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param items
     * @return A new instance of fragment SortFragment.
     */
    public static SortFragment newInstance(List<PickupColumn> items, boolean isCompnaySelected, JsonArray keyTopicsJson, JsonArray stockList) {
        SortFragment fragment = new SortFragment();
        fragment.mItems = items;
        mIsCompanySelected = isCompnaySelected;
        fragment.mKeyTopicsJson = keyTopicsJson;
        fragment.mStockList = stockList;

        return fragment;
    }

    public boolean isFromMenu() {
        return isFromMenu;
    }

    public void setFromMenu(boolean fromMenu) {
        isFromMenu = fromMenu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        getBaseActivity().initToolBar("Sort Columns", true);
        return view;
    }

    @Override
    protected void initViews(View view) {
        getBaseActivity().getSortTextView().setVisibility(View.GONE);
        if (mItems != null) {
            mAdapter = new SortAdapter(mItems);
            mList.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
            mList.setHasFixedSize(true);
            mList.setAdapter(mAdapter);

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mList);
        }

        if (mStockList == null) {
            callCompanyWebService();
        }
        if (mKeyTopicsJson == null) {
            getKeyTopicData();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == 0) {

        } else {
            mItemTouchHelper.startDrag(viewHolder);
        }
    }

    @OnClick(R.id.tv_sort_continue)
    void onContinueClick() {
        if (mAdapter == null) {
            Util.showErrorSnackBar(getView(), "No items added");
            return;
        }
        List<PickupColumn> sortedItems = mAdapter.getmItems();
        continueData(sortedItems);

    }

    private void continueData(List<PickupColumn> sortedItems) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("user_choice", "my_choice");
        JsonArray arr = new JsonArray();

        for (int i = 0; i < sortedItems.size(); i++) {
            arr.add(Integer.valueOf(sortedItems.get(i).getId()));
        }
        data.put("column_list", arr);
//        data.put("")

//        if (mIsCompanySelected) {
        data.put("key_topic_list", mKeyTopicsJson);
        data.put("rss_url_list", mStockList);
        callApi(data);
//            callCompanyWebService(data);
//            addFragment(CompaniesFragment.newInstance(true, false, data));
//        } else if (isFromMenu && isCompanySelectedinList()) {
//            callCompanyWebService();
//        } else {
//            JsonArray arr1 = new JsonArray();
//            data.put("rss_url_list", arr1);
//            data.put("key_topic_list", mKeyTopicsJson);
//            callApi(data);
    }
//    }

    private boolean isCompanySelectedinList() {
        List<PickupColumn> sortedItems = mAdapter.getmItems();

        for (int i = 0; i < sortedItems.size(); i++) {
            if (Integer.valueOf(sortedItems.get(i).getId()) == 2) {
                return true;
            }
        }

        return false;
    }

    private boolean isKeyTopicSelected() {
        List<PickupColumn> sortedItems = mAdapter.getmItems();

        for (int i = 0; i < sortedItems.size(); i++) {
            if (Integer.valueOf(sortedItems.get(i).getId()) == 25) {
                return true;
            }
        }

        return false;
    }


    private void callApi(HashMap<String, Object> data) {


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

                        if (obj.getString("status").equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                            getBaseActivity().start(TabsInitActivity.class);
                        } else {
                            mContinue.setEnabled(true);
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
                    Util.showErrorSnackBar(getView(), "Something went wrong");
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

    private void callCompanyWebService() {

        final JsonArray arr1 = new JsonArray();

            Call<StockModel> service = RetrofitBuilder.getService().companyStockList(getAppPref().getAccessToken());
            final Dialog dialog = showProgress();
            service.enqueue(new Callback<StockModel>() {
                @Override
                public void onResponse(Call<StockModel> call, Response<StockModel> response) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (response.code() == 200) {
                        List<StockModel.StockDetail> dataList = response.body().getData().getList().getAll();
                        dataList.get(0).getSelectedColumn();
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getSelectedColumn().equalsIgnoreCase("yes")) {
                                arr1.add(dataList.get(i).getId());
                            }
                        }
//                    data.put("rss_url_list", arr1);
                        mStockList = arr1;

//                    callApi(data);

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


    public void getKeyTopicData() {
        if (isKeyTopicSelected()) {
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

        } else {
            mKeyTopicsJson = new JsonArray();
        }
    }
}
