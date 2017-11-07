package com.flocktamer.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.flocktamer.R;
import com.flocktamer.adapters.FaqAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link FaqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaqFragment extends BaseFragment {

    @BindView(R.id.elv_faq)
    ExpandableListView mList;

    public FaqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FaqFragment.
     */
    public static FaqFragment newInstance() {
        return new FaqFragment();
    }

    @Override
    protected void initViews(View view) {
        getBaseActivity().initToolBar("FAQ", true);
        mList.setAdapter(new FaqAdapter( getBaseActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

}
