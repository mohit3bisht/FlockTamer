package com.flocktamer.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.interfaces.OnTwitterAccountDelete;
import com.flocktamer.model.childmodel.TwitterUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amandeepsingh on 2/9/16.
 */
public class TwitterUserAdapter extends RecyclerView.Adapter<TwitterUserAdapter.UserHolder> {

    private final List<TwitterUser> mTwitterAccocunts;
    private final boolean mIsAddAccountScreen;
    private final OnTwitterAccountDelete mDeleteCallback;
private boolean isFromCompose;
    public TwitterUserAdapter(List<TwitterUser> twitterAccount, boolean isAddAccountScreen, OnTwitterAccountDelete callback,boolean isFromCompose) {

        mIsAddAccountScreen = isAddAccountScreen;
        mTwitterAccocunts = twitterAccount;
        mDeleteCallback = callback;
        isFromCompose = isFromCompose;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_twitter_user, parent, false);
        if (mIsAddAccountScreen) {
            view.findViewById(R.id.cb_select_user).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.iv_delete_user).setVisibility(View.GONE);
        }
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserHolder holder, final int position) {

        final TwitterUser user = mTwitterAccocunts.get(position);
        holder.name.setText(user.getTwitterName());
        holder.screenName.setText("@" + user.getTwitterScreenName());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
                .cacheInMemory(true)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(user.getTwitterProfileImageUrl(), holder.profileImage, options);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                user.setChecked(holder.checkbox.isChecked());
                if (isFromCompose){
                    user.setChecked(true);
for (int i = 0;i<mTwitterAccocunts.size();i++){

    if (i!=position){
        mTwitterAccocunts.get(i).setChecked(false);
    }
}

                }

//                for(int h =0; h<mTwitterAccocunts.size();h++)
//                {
//                    if(mTwitterAccocunts.get(h).isChecked()!=user.isChecked())
//                    {
//                        return;
//                    }
//                }

            }
        });

        holder.checkbox.setChecked(user.isChecked());

    }

    @Override
    public int getItemCount() {
        return mTwitterAccocunts.size();
    }

    public JsonArray getCheckedList() {

        JsonArray arr = new JsonArray();

        for (int i = 0; i < mTwitterAccocunts.size(); i++) {
            if (mTwitterAccocunts.get(i).isChecked()) {
                arr.add(mTwitterAccocunts.get(i).getId());
            }
        }

        return arr;
    }

    class UserHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_twitter_user_image)
        ImageView profileImage;

        @BindView(R.id.tv_twitter_user_name)
        CustomTextView name;
        @BindView(R.id.tv_twitter_screen_name)
        CustomTextView screenName;

        @BindView(R.id.cb_select_user)
        AppCompatCheckBox checkbox;

        @BindView(R.id.iv_delete_user)
        ImageView delete;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.iv_delete_user)
        void onDeleteClick() {
            if (mDeleteCallback != null) {
                mDeleteCallback.onDeleteClick(mTwitterAccocunts.get(getAdapterPosition()));
            }
        }
    }
}
