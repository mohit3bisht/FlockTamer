package com.flocktamer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amandeepsingh on 17/1/17.
 */

public class KeyTopicsAdapter extends RecyclerView.Adapter<KeyTopicsAdapter.KeyViewHolder> {

    private final OnDeleteTopics mOnDeleteTopics;
    private List<String> mList = new ArrayList<>();

    public KeyTopicsAdapter(OnDeleteTopics listener) {
//        mList = list;
        mOnDeleteTopics = listener;
    }

    @Override
    public KeyTopicsAdapter.KeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_key_topics, parent, false);
        return new KeyTopicsAdapter.KeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KeyTopicsAdapter.KeyViewHolder holder, int position) {
        holder.name.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void addAll(List<String> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public void add(String data) {
        mList.add(data);
        notifyDataSetChanged();
    }

    class KeyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_key_close)
        ImageView cross;

        @BindView(R.id.tv_key_text)
        CustomTextView name;

        public KeyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.iv_key_close)
        void onCancelClick() {
            mList.remove(getAdapterPosition());

            notifyDataSetChanged();

            mOnDeleteTopics.onTopicDelete(getAdapterPosition());
        }
    }

    public List<String> getList() {
        return mList;
    }

    public interface OnDeleteTopics {
        void onTopicDelete(int position);
    }
}