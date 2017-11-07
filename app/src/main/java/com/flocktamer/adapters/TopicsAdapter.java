package com.flocktamer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amandeepsingh on 16/9/16.
 */
public class TopicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String[] titles = {"", "Insights", "General Business", "Investor", "Active Trader", "Breaking News", "Sports", "Real Estate", "Legal", "Politics", "Short Sellers", "Deals", "Options", "Health & Medicine", "General News", "Technology", "Trusted Sources", "Companies", "Hedge Funds", "My Twitter"};
    private static final String desc[] = {"", "This is a default selection and reflects news that the Editor thinks is especially newsworthy or thought provoking.",
            "Headlines from major news services to inform but not overwhelm someone with casual interest.",
            "More focused than General Business.  Appropriate for anyone who has an interest in investing or is monitoring the market for information relevant to a portfolio or retirement.",
            "More intense and detailed focus on stocks, bonds and commodities with varied sources offering actionable information.",
            "Breaking headlines from the largest providers of news in the country and world.",
            "A selection of interesting sports sources in addition to the names you would expect like ESPN. In time you will the ability to refine your interests to local markets/specific teams.",
            "Information about nationwide real estate markets, commercial transactions and industry participants.",
            "A broad spectrum of sources focused on business, civil and criminal law.",
            "The full spectrum of political news coverage from respected sources.",
            "Commentary from well-known short sellers.",
            "Information and commentary about mergers, acquisitions, spin offs and re-structuring.",
            "Commentary from option industry participants and identification of big option trades and what they might mean.",
            "Broad information from a variety of sources about new discoveries, drug trials and the impact on different companies and specific drugs.",
            "A blend of breaking news, politics, business, and sports.",
            "Commentary from technology industry experts and discussion of new products and recent industry developments.",
            "A mix of sources on a wide variety of topics who provide insightful and interesting commentary.  I highly recommend this channel.",
            "Links to company news from underlying company websites. You can select which companies you want to see news about by using the stock ticker.  This channel has yet to achieve its full potential and is being refined.",
            "Curated by a hedge fund industry professional with many years of experience and a sharp tongue.",
            "If you are a twitter user this will allow you to integrate the people you follow into FlockTamer."
    };

    private final static int VIEW_TYPE_HEADER = 0;
    private final static int VIEW_TYPE_ITEM = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.view_topics_header, parent, false);
            return new TopicsHeaderHolder(view);
        } else {
            View view = inflater.inflate(R.layout.view_topics_item, parent, false);
            return new TopicsHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position != 0) {
            TopicsHolder topicsHolder = (TopicsHolder) holder;
            topicsHolder.title.setText(titles[position]);
            topicsHolder.description.setText(desc[position]);
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    class TopicsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_topics_title)
        CustomTextView title;
        @BindView(R.id.tv_topics_desc)
        CustomTextView description;

        public TopicsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TopicsHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_topics_header)
        CustomTextView header;

        public TopicsHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
