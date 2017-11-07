package com.flocktamer.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flocktamer.R;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Keys;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkimArticleFragment extends BaseFragment implements View.OnClickListener {


    public SkimArticleFragment() {
        // Required empty public constructor
    }

    private String mTitleString = "", mUrlString = "";

    public static SkimArticleFragment newInstance() {
        SkimArticleFragment fragment = new SkimArticleFragment();

        return fragment;
    }

    private WebView web;
    private ImageView mBackIcon;
    private TextView mTitle, mArticleUrl;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skim_article, container, false);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        initViews(view);
        return view;
    }

    @Override
    protected void initViews(View view) {
        mArticleUrl = (TextView) view.findViewById(R.id.tv_article_link);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mBackIcon = (ImageView) view.findViewById(R.id.iv_back_icon);
        mArticleUrl.setClickable(true);
        mArticleUrl.setMovementMethod(LinkMovementMethod.getInstance());
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mTitleString = bundle.getString(Keys.TITLE);
            mUrlString = bundle.getString(Keys.URL);
        }
        web = (WebView) view.findViewById(R.id.article_web);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(mUrlString);
        //  web.loadUrl("http://www.allfitgoals.com");

        mTitle.setText(mTitleString);
        //mUrlString = "www.allfitgoals.com";
        mArticleUrl.setText(Html.fromHtml(mUrlString));
        mBackIcon.setOnClickListener(this);
        mArticleUrl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.iv_back_icon:
                Intent intent = new Intent(getActivity(), TabsInitActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_article_link:
                openLink();

                break;
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }


    private void openLink() {

        Uri webPage = Uri.parse(mUrlString);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
