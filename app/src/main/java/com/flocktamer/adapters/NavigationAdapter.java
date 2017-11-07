package com.flocktamer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;
import com.flocktamer.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amandeepsingh on 25/8/16.
 */
public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String mUsername;
    private final ItemClickListener mListener;
    private String[] titles = {"header", "name", "Manage Topics","Sort Topics", "Twitter Accounts", "Edit Profile", "Topics", "FAQ", "Meet the Editor", "Suggestions", "Contact Us", "Log out"};
  //  private String[] titles = {"header", "name", "Manage Topics","Custom News Alerts","Sort Topics", "Twitter Accounts", "Edit Profile", "Topics", "FAQ", "Meet the Editor", "Suggestions", "Contact Us", "Log out"};
    //private int[] drawables = {0, R.drawable.profile_icon, R.drawable.manage_icon,R.drawable.topics_icon, R.drawable.sort ,R.drawable.add_account_icon, R.drawable.edit_profile_icon, R.drawable.topics_icon, R.drawable.faq_icon, R.drawable.humans_bl, R.drawable.suggestion_icon, R.drawable.contact_icon, R.drawable.logout_icon};
    private int[] drawables = {0, R.drawable.profile_icon, R.drawable.manage_icon, R.drawable.sort ,R.drawable.add_account_icon, R.drawable.edit_profile_icon, R.drawable.topics_icon, R.drawable.faq_icon, R.drawable.humans_bl, R.drawable.suggestion_icon, R.drawable.contact_icon, R.drawable.logout_icon};

    public NavigationAdapter(String username, ItemClickListener listener) {
        mUsername = username;
        mListener = listener;
    }

    //    private int[] drawables= {0,}
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.view_navigation_header, parent, false);
            return new HeaderHolder(view);
        } else {
            View view = inflater.inflate(R.layout.view_navigation, parent, false);
            return new NavigationHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            NavigationHolder myHolder = (NavigationHolder) holder;
            myHolder.icon.setImageResource(drawables[position]);
            if (position == 1) {
                myHolder.textview.setText("Hello, " + mUsername);
            } else {
                myHolder.textview.setText(titles[position]);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public interface ItemClickListener {

        void onItemClick(int position);
    }

    class NavigationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title_navigation)
        CustomTextView textview;
        @BindView(R.id.iv_title_navigation)
        ImageView icon;

        public NavigationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.iv_close_nav)
        void onClickClose() {
            mListener.onItemClick(Constants.CLICK_CLOSE_NAVIGATION);
        }
    }
}
