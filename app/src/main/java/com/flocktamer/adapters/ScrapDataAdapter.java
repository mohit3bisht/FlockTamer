package com.flocktamer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.model.childmodel.CompanyScrapData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amandeepsingh on 22/2/17.
 */

public class ScrapDataAdapter extends RecyclerView.Adapter<ScrapDataAdapter.ScrapDataHolder> {


    private String[] data = {"Dow", "Nasdaq", "S&P", "10-year Yield", "Oil", "Gold", "Yen", "Euro"};
    private final CompanyScrapData mScrapData;
    private LayoutInflater inflater;
    private Context context;

    public ScrapDataAdapter(CompanyScrapData scrapedData) {
        mScrapData = scrapedData;
    }

    @Override
    public ScrapDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_scrap_view, parent, false);
        return new ScrapDataHolder(view);
    }

    @Override
    public void onBindViewHolder(ScrapDataHolder holder, int position) {
        int width = context.getResources().getDisplayMetrics().widthPixels / 3;
//        int hei=getResources().getDisplayMetrics().heightPixels/3;
//        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(width, holder.itemView.getHeight()));
        holder.mTitle.setText(data[position]);

        if (position == 0) {
            String[] data = Html.fromHtml(mScrapData.getDowData()).toString().split("\\s");

            holder.mValue1.setText(data[2]);
            String valDow = data[1].charAt(0) + "";
            holder.mValue2.setText(data[1]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        } else if (position == 1) {
            String[] data = Html.fromHtml(mScrapData.getNsadaqData()).toString().split("\\s");
            holder.mValue1.setText(data[2]);
            String valDow = data[1].charAt(0) + "";
            holder.mValue2.setText(data[1]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

//            holder.mValue1.setText(Html.fromHtml(mScrapData.getNsadaqData()));
        } else if (position == 2) {
            String[] data = Html.fromHtml(mScrapData.getSPData()).toString().split("\\s");
            holder.mValue1.setText(data[2]);
            String valDow = data[1].charAt(0) + "";
            holder.mValue2.setText(data[1]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        } else if (position == 3) {
            String[] data = Html.fromHtml(mScrapData.getYearYieldData()).toString().split("\\s");
            holder.mValue1.setText(data[2]);
            String valDow = data[4].charAt(0) + "";
            holder.mValue2.setText(data[4]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

//            holder.mValue1.setText(Html.fromHtml(mScrapData.getYearYieldData()));
        } else if (position == 4) {
            String[] data = Html.fromHtml(mScrapData.getOilData()).toString().split("\\s");
            holder.mValue1.setText(data[2]);
            String valDow = data[3].charAt(0) + "";
            holder.mValue2.setText(data[3]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
//            holder.mValue1.setText(Html.fromHtml(mScrapData.getOilData()));
        } else if (position == 5) {
            String[] data = Html.fromHtml(mScrapData.getGoldData()).toString().split("\\s");
            holder.mValue1.setText(Html.fromHtml(mScrapData.getGoldData()));
            holder.mValue1.setText(data[2]);
            String valDow = data[3].charAt(0) + "";
            holder.mValue2.setText(data[3]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        } else if (position == 6) {
            String[] data = Html.fromHtml(mScrapData.getYenData()).toString().split("\\s");
            holder.mValue1.setText(Html.fromHtml(mScrapData.getGoldData()));
            holder.mValue1.setText(data[2]);
            String valDow = data[3].charAt(0) + "";
            holder.mValue2.setText(data[3]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        } else if (position == 7) {
            String[] data = Html.fromHtml(mScrapData.getEuroData()).toString().split("\\s");
            holder.mValue1.setText(Html.fromHtml(mScrapData.getGoldData()));
            holder.mValue1.setText(data[2]);
            String valDow = data[3].charAt(0) + "";
            holder.mValue2.setText(data[3]);
            if (valDow.equalsIgnoreCase("+")) {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_up), null, null, null);
            } else if (valDow.equalsIgnoreCase("-")) {
                holder.mValue2.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.arrow_down), null, null, null);
            } else {
                holder.mValue2.setTextColor(context.getResources().getColor(R.color.green_company));
                holder.mValue2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class ScrapDataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_value1)
        CustomTextView mValue1;
        @BindView(R.id.tv_value2)
        CustomTextView mValue2;

        @BindView(R.id.tv_title)
        CustomTextView mTitle;

        public ScrapDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
