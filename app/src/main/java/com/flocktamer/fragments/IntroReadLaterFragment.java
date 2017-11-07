package com.flocktamer.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.flocktamer.R;
import com.flocktamer.interfaces.WelcomeScreenInterface;

/**
 * A simple {@link Fragment} subclass.
 * * Fragment showing on the introduction slide at 4th position.
 */
@SuppressLint("ValidFragment")
public class IntroReadLaterFragment extends Fragment {

    private RelativeLayout mgetStarted;

    private WelcomeScreenInterface mCallback;
    public IntroReadLaterFragment(WelcomeScreenInterface callback) {
        // Required empty public constructor
        mCallback = callback;
    }

public IntroReadLaterFragment(){


}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_read_later, container, false);

        mgetStarted = (RelativeLayout) view.findViewById(R.id.rl_splash_getstart);
        mgetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onGetStarted();
            }
        });
        return view;
    }

}
