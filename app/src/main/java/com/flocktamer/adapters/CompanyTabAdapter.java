package com.flocktamer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.fragments.DefaultTabFragment;
import com.flocktamer.model.childmodel.CompanyFeed;
import com.flocktamer.model.childmodel.CompanyScrapData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amandeepsingh on 12/8/16.
 * <p/>
 * Adapter for list of Tweets in recyclerView of {@link DefaultTabFragment} under ViewPager.
 */
public class CompanyTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<CompanyFeed> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private static final int VIEW_TYPE_SCRAP = 0;
    private static final int VIEW_TYPE_STOCK = 1;

    public CompanyTabAdapter(@NonNull List<CompanyFeed> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        inflater = LayoutInflater.from(parent.getContext());

//        if (viewType == VIEW_TYPE_SCRAP) {
//
//            View view = inflater.inflate(R.layout.view_scrap_view, parent, false);
//            return new ScrapDataHolder(view);
//        } else {
            View view = inflater.inflate(R.layout.view_company_adapter, parent, false);
            return new CompanyTabViewHolder(view);
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//
////        if (position == 0) {
////            return VIEW_TYPE_SCRAP;
////        } else {
//            return VIEW_TYPE_STOCK;
////        }
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

//        if (viewHolder.getItemViewType() == VIEW_TYPE_STOCK) {
            CompanyTabViewHolder holder = (CompanyTabViewHolder) viewHolder;

            holder.mName.setMovementMethod(LinkMovementMethod.getInstance());
            try {
                holder.mName.setText(Html.fromHtml(mList.get(position).getStockRssFeed().getSymbol()));
                String rate = String.format("%.2f", Double.valueOf(mList.get(position).getStockRssFeed().getLast()));
                holder.mRate.setText("$" + rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                double change = Double.valueOf(mList.get(position).getStockRssFeed().getChange());
                String changeString = String.format("%.2f", Double.valueOf(change));
                if (change < 0) {
//                String changeString = change+"";
                    changeString = changeString.substring(0, 1) + "$" + changeString.substring(1, changeString.length());
                    holder.mChange.setTextColor(Color.RED);
                    holder.mChange.setText(changeString);
                } else {
                    holder.mChange.setTextColor(mContext.getResources().getColor(R.color.green_company));
                    holder.mChange.setText("$" + changeString);
                }
            } catch (Exception e) {
                holder.mChange.setText(mList.get(position).getStockRssFeed().getChange());
            }

            holder.mLinksLayout.removeAllViews();

            for (int i = 0; i < mList.get(position).getRssFeed().size(); i++) {
                View view = inflater.inflate(R.layout.view_company_subview, null);
                CustomTextView title = (CustomTextView) view.findViewById(R.id.tv_companies_link);
                CustomTextView time = (CustomTextView) view.findViewById(R.id.tv_companies_time);

                String Htmltext = "<a href=\"" + mList.get(position).getRssFeed().get(i).getLink() + "\">" + mList.get(position).getRssFeed().get(i).getTitle() + "</a>";

                final String link = mList.get(position).getRssFeed().get(i).getLink();
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(link));
                        mContext.startActivity(intent);
                    }
                });
                Linkify.addLinks(title, Linkify.ALL);
                title.setText(Html.fromHtml(Htmltext));

                time.setText(mList.get(position).getRssFeed().get(i).getDateFormat());
                holder.mLinksLayout.addView(view);
            }
//        } else if (viewHolder.getItemViewType() == VIEW_TYPE_SCRAP) {
//            ScrapDataHolder holder = (ScrapDataHolder) viewHolder;
//            holder.mDowText.setText(Html.fromHtml(mScrapData.getDowData()));
//            holder.mNasdaq.setText(Html.fromHtml(mScrapData.getNsadaqData()));
//            holder.mSp.setText(Html.fromHtml(mScrapData.getSPData()));
//            holder.mYield.setText(Html.fromHtml(mScrapData.getYearYieldData()));
//            holder.mGold.setText(Html.fromHtml(mScrapData.getGoldData()));
//        }

//        Linkify.addLinks(holder.mDetailText, Linkify.WEB_URLS);
    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    class CompanyTabViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_company_name)
        CustomTextView mName;
        @BindView(R.id.tv_company_rate)
        CustomTextView mRate;
        @BindView(R.id.tv_company_change)
        CustomTextView mChange;
        @BindView(R.id.ll_company_links)
        LinearLayout mLinksLayout;

        public CompanyTabViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

//    class ScrapDataHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.tv_dow_value)
//        CustomTextView mDowText;
//        @BindView(R.id.tv_nasdaq_value)
//        CustomTextView mNasdaq;
//        @BindView(R.id.tv_sp_value)
//        CustomTextView mSp;
//        @BindView(R.id.tv_yield_value)
//        CustomTextView mYield;
//        @BindView(R.id.tv_gold_value)
//        CustomTextView mGold;
//
//        public ScrapDataHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//    }
}
