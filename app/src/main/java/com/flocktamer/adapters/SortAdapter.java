package com.flocktamer.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.draggable.ItemTouchHelperAdapter;
import com.flocktamer.draggable.ItemTouchHelperViewHolder;
import com.flocktamer.model.childmodel.PickupColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amandeepsingh on 25/10/16.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortView> implements ItemTouchHelperAdapter {


    private List<PickupColumn> mItems = new ArrayList<>();

    public SortAdapter(List<PickupColumn> items) {
        mItems.addAll(items);
    }

    @Override
    public SortView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_sort_adapter, parent, false);
        return new SortView(view);
    }

    @Override
    public void onBindViewHolder(SortView holder, int position) {

        holder.mCardView.setCardBackgroundColor(Color.parseColor(mItems.get(position).getMainDivColour()));
        holder.mTitle.setTextColor(Color.parseColor(mItems.get(position).getH2DivColour()));
        holder.mTitle.setText(mItems.get(position).getColumnName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if(fromPosition==0 || toPosition==0)
        {
            return false;
        }
        if (fromPosition==1||toPosition==1){
            return false;
        }
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public List<PickupColumn> getmItems() {
        return mItems;
    }

    static class SortView extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.cv_sort_card)
        CardView mCardView;
        @BindView(R.id.tv_sort_title)
        CustomTextView mTitle;

        public SortView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
