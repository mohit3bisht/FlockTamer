package com.flocktamer.ui;

import android.app.Dialog;
import android.os.Bundle;

import com.flocktamer.R;
import com.flocktamer.fragments.SortFragment;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.http.RetrofitService;
import com.flocktamer.interfaces.SortComparetor;
import com.flocktamer.model.PickupModel;
import com.flocktamer.model.childmodel.PickupColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SortActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_sort);
        initToolBar("Sort Topics", true);
        initViews();
    }

    @Override
    protected void initViews() {
        RetrofitService service = RetrofitBuilder.getService();
        Call<PickupModel> call = service.companyColumnList(getAppPref().getAccessToken());

        final Dialog dialog = showProgress();
        call.enqueue(new Callback<PickupModel>() {
            @Override
            public void onResponse(Call<PickupModel> call, Response<PickupModel> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    List<PickupColumn> columns = response.body().getData().getColumns();

                    List<PickupColumn> selectedColumns = getSelectedColumns(columns);
                    Collections.sort(selectedColumns, new SortComparetor());

                    SortFragment fragment = SortFragment.newInstance(selectedColumns, false, null,null);
                    fragment.setFromMenu(true);

                    replaceFragment(fragment, R.id.content_sort);
                } else if (response.code() == 401) {
                    logoutApp();
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<PickupModel> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

//
    }

    private List<PickupColumn> getSelectedColumns(List<PickupColumn> columns) {
        List<PickupColumn> selectedCols = new ArrayList<>();
        for (PickupColumn col : columns) {
            if (col.getIsSelected().equalsIgnoreCase("yes")) {
                selectedCols.add(col);
            }
        }

        return selectedCols;
    }

    @Override
    protected void initManagers() {

    }
}
