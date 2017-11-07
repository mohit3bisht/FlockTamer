package com.flocktamer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.model.StockModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amandeepsingh on 1/9/16.
 */
public class SelectStockAdapter extends RecyclerView.Adapter<SelectStockAdapter.StockViewHolder> {

    private List<StockModel.StockDetail> mList;

    public SelectStockAdapter(List<StockModel.StockDetail> list) {
        mList = list;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_company_select_item, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getCompany());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class StockViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_company_item_cross)
        ImageView cross;

        @BindView(R.id.tv_company_item_name)
        CustomTextView name;

        public StockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.iv_company_item_cross)
        void onCancelClick() {
            mList.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}
