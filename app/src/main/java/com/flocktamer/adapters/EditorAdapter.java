package com.flocktamer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.model.childmodel.PickupColumn;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amandeepsingh on 22/8/16.
 */
public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.EditorViewHolder> {

    private final List<PickupColumn> mColumns;
    private final List<PickupColumn> mSelectedColumns = new ArrayList<>();
    private final boolean mIsEditorChoice;
    private final OnEditClickListener mEditListner;
    int counter = 0;
    private boolean[] checkList;
    private Context mContext;

    public EditorAdapter(List<PickupColumn> columns, boolean isEditorChoice, OnEditClickListener editListner) {
        mIsEditorChoice = isEditorChoice;
        mColumns = columns;
        mEditListner = editListner;
        checkList = new boolean[columns.size()];
        for (int i = 0; i < checkList.length; i++) {
            checkList[i] = false;
            if (mColumns.get(i).getIsDefault().equalsIgnoreCase("yes") || mColumns.get(i).getIsSelected().equalsIgnoreCase("yes")) {
                mSelectedColumns.add(mColumns.get(i));
                counter++;
            }
        }
    }

    @Override
    public EditorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_editor_choice, parent, false);

        return new EditorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditorViewHolder holder, int position) {

        PickupColumn column = mColumns.get(position);
//        Drawable drawable = mContext.getResources().getDrawable(R.drawable.info_black_icon);
//
////        drawable.clearColorFilter();
//        drawable.setColorFilter(Color.parseColor(column.getH2DivColour()), PorterDuff.Mode.SRC_IN);
//        drawable.invalidateSelf();


//        drawable.setColorFilter(Color.parseColor(column.getH2DivColour()), PorterDuff.Mode.MULTIPLY);
//        holder.info.setImageDrawable(drawable);
        holder.title.setText(column.getColumnName());
        try {
            holder.itemView.setBackgroundColor(Color.parseColor(column.getMainDivColour()));
        } catch (Exception e) {

        }
        if (column.getIsDefault().equalsIgnoreCase("yes") || checkList[position] || mIsEditorChoice || column.getIsSelected().equalsIgnoreCase("yes")) {
            holder.check.setVisibility(View.VISIBLE);
            checkList[position] = true;
        } else {
            holder.check.setVisibility(View.INVISIBLE);
        }

        if (column.getColumnName().equalsIgnoreCase("companies") && !mIsEditorChoice) {
            if (column.getIsSelected().equalsIgnoreCase("yes")) {
                holder.mEditCompany.setVisibility(View.VISIBLE);
            } else {
                holder.mEditCompany.setVisibility(View.GONE);
            }
        } else if (column.getId().equalsIgnoreCase("25") && column.getIsSelected().equalsIgnoreCase("yes")) {
            holder.mEditCompany.setVisibility(View.VISIBLE);
        } else {
            holder.mEditCompany.setVisibility(View.GONE);
        }

        holder.title.setTextColor(Color.parseColor(column.getH2DivColour()));
    }

    @Override
    public int getItemCount() {
        return mColumns.size();
    }

    public List<PickupColumn> getSelectedItems() {
        return mSelectedColumns;
    }

    class EditorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_editor_title)
        CustomTextView title;
        @BindView(R.id.tv_edit_company)
        CustomTextView mEditCompany;
        @BindView(R.id.tv_editor_check)
        ImageView check;

        @BindView(R.id.iv_editor_info)
        ImageView info;


        public EditorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mColumns.get(getAdapterPosition()).getIsDefault().equalsIgnoreCase("yes") || mIsEditorChoice) {
                        return;
                    }
//                    if (!checkList[getAdapterPosition()] && counter == 6) {
//                        Toast.makeText(mContext, "You cannot select more then 6 topics", Toast.LENGTH_SHORT).show();
//                    } else {
                    checkList[getAdapterPosition()] = !checkList[getAdapterPosition()];
                    if (checkList[getAdapterPosition()]) {

                        if (mColumns.get(getAdapterPosition()).getId().equalsIgnoreCase("25")) {
                            mEditCompany.setVisibility(View.VISIBLE);
                        } else {
                            mEditCompany.setVisibility(View.GONE);
                        }

                        check.setVisibility(View.VISIBLE);
                        mSelectedColumns.add(mColumns.get(getAdapterPosition()));
                        counter++;
                    } else {
                        mEditCompany.setVisibility(View.GONE);
                        check.setVisibility(View.INVISIBLE);
                        mSelectedColumns.remove(mColumns.get(getAdapterPosition()));
                        mColumns.get(getAdapterPosition()).setIsSelected("no");
                        counter--;
                    }

//                    if (&& mColumns.get(getAdapterPosition()).getIsSelected().equalsIgnoreCase("yes")) {
//                        mEditCompany.setVisibility(View.VISIBLE);
//                    } else {
//                        mEditCompany.setVisibility(View.GONE);
//                    }
//                    }
                }
            });
        }

        @OnClick(R.id.iv_editor_info)
        void onInfoIconClcik() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setNeutralButton("OK", null);
            alertDialogBuilder.setTitle(mColumns.get(getAdapterPosition()).getColumnName());
            alertDialogBuilder.setMessage(mColumns.get(getAdapterPosition()).getDescription());

            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.show();
        }

        @OnClick(R.id.tv_edit_company)
        void onEditClick() {
            mEditListner.onEditClick(mColumns.get(getAdapterPosition()).getId());
        }
    }

    public interface OnEditClickListener {
        void onEditClick(String id);
    }
}


