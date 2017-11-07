package com.flocktamer.fragments;


import android.app.Dialog;
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
import com.flocktamer.adapters.KeyTopicsAdapter;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.ui.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KeyTopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeyTopicsFragment extends BaseFragment implements View.OnClickListener, KeyTopicsAdapter.OnDeleteTopics {

    private EditText mKeywordBox;
    private CustomTextView mContinue;
    private RecyclerView mGrid;
    private KeyTopicsAdapter mAdapter;

    public KeyTopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KeyTopicsFragment.
     */

    public static KeyTopicsFragment newInstance() {
        KeyTopicsFragment fragment = new KeyTopicsFragment();
        return fragment;
    }

    @Override
    protected void initViews(View view) {
        getBaseActivity().getSortTextView().setVisibility(View.GONE);
        mKeywordBox = (EditText) view.findViewById(R.id.et_keywords);
        mGrid = (RecyclerView) view.findViewById(R.id.rv_keywords_grid);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mGrid.setLayoutManager(gridLayoutManager);
        mAdapter = new KeyTopicsAdapter(this);
        mGrid.setAdapter(mAdapter);

        mKeywordBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mAdapter.getItemCount() == 2) {
                        Toast.makeText(getBaseActivity(), "Limit exceeded", Toast.LENGTH_SHORT).show();
                    } else {
                        String data = mKeywordBox.getText().toString().trim();
                        if (data.isEmpty()) {
                            Toast.makeText(getBaseActivity(), "No text found", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        mKeywordBox.setText(null);
                        mAdapter.add(data);
                    }
                }
                return false;
            }
        });


        callApi();
        view.findViewById(R.id.tv_keywords_continue).setOnClickListener(this);
        view.findViewById(R.id.btn_add_topics).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_key_topics, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_keywords_continue:
                getBaseActivity().hideKeyboard(getView());
                JsonArray arr = new JsonArray();
                for (int i = 0; i < mAdapter.getList().size(); i++) {
                    arr.add(mAdapter.getList().get(i));
                }
                ((DashboardActivity) getActivity()).setKeyTopics(arr);
                getBaseActivity().onBackPressed();
                break;

            case R.id.btn_add_topics:
                getBaseActivity().hideKeyboard(getView());
                String data = mKeywordBox.getText().toString().trim();
                if (data.isEmpty()) {
                    Toast.makeText(getBaseActivity(), "No text found", Toast.LENGTH_SHORT).show();
                    return;
                }
                mKeywordBox.setText(null);
                mAdapter.add(data);

                break;
        }

        if (mAdapter.getItemCount() == 2) {
            disableBox();
        }
    }

    private void disableBox() {
        mKeywordBox.setHint("Max keywords reached");
        mKeywordBox.setEnabled(false);
    }

    private void enasbleBox() {
        mKeywordBox.setHint("Enter a key Topic");
        mKeywordBox.setEnabled(true);
    }


    public void callApi() {
        final Dialog dialog = showProgress();
        Call<ResponseBody> call = RetrofitBuilder.getService().getKeyTopics(getAppPref().getAccessToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                try {
                    String data = response.body().string();
                    JSONObject obj = new JSONObject(data);
                    JSONObject dataObj = obj.getJSONObject("data");
                    Type listType = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> listOfKeys = new Gson().fromJson(dataObj.getJSONArray("List").toString(), listType);
                    if (listOfKeys != null) {
                        mAdapter.addAll(listOfKeys);
                    }

                    if (mAdapter.getItemCount() == 2) {
                        disableBox();
                    } else {
                        enasbleBox();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onTopicDelete(int position) {
        enasbleBox();
    }
}
