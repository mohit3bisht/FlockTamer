package com.flocktamer.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link SelectCompanyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectCompanyFragment extends BaseFragment {

    public SelectCompanyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static SelectCompanyFragment newInstance() {
        return new SelectCompanyFragment();
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_company, container, false);
        initViews(view);
        return view;
    }

}
