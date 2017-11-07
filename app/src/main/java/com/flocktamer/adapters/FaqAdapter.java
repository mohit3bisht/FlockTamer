package com.flocktamer.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.flocktamer.R;
import com.flocktamer.customview.CustomTextView;

/**
 * Created by amandeepsingh on 19/9/16.
 */
public class FaqAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[] mGroup = {"What is FlockTamer?", "Why should I use FlockTamer?", "How is FlockTamer better than Facebook News, Apple News, Google News or BuzzFeed ?",
            "How do I use it?", "Do I need Twitter account?", "Can I integrate my existing Twitter Account into FlockTamer?",
            "My Firm won’t let me access social media how can I use FlockTamer?"};

    private String[] mChild = {"FlockTamer is Real Time News & Commentary Powered by Twitter and Curated by Humans. Its 100% Free! ",
            "Twitter is a fire hose of information that has valuable nuggets but in my experience as an active user, it requires an editor, someone to curate the news. Twitter itself offers to help you figure out whom to follow based on your described interests, but the results are driven by an algorithm (which always seem to include Justin Bieber).  In contrast FlockTamer is curated and edited by a passionate consumer of news.",
            "FlockTamer is a better, more refined and more controllable experience <b>organized by topic not publication</b>. FlockTamer is real time information on areas of interest to you.&nbsp;&nbsp; As a user you have the option of selecting 5 topics or to use the template created by the editor.&nbsp;&nbsp;&nbsp; You have the ability to change your areas of interest at any time or to switch between template and custom. &nbsp;&nbsp;You will gain the ability to be up to fully informed in less than 30 seconds or dive deeper if you wish. &nbsp;If you leave the site open it will update on a schedule that is useful but not overwhelming. &nbsp;All of this controllable from either a desktop browser or a browser on your smartphone.",
            "Go to <a href=\"http://www.FlockTamer.com\" target=\"_blank\">www.FlockTamer.com</a> click on sign up.  It will take &lt; 1 minute to register.",
            "No Twitter account is needed.", " Absolutely, just select My Conversations when you register and you will be asked to link your Twitter account and have full functionality.",
            "I have the ability to work with your compliance department to white label a version of Flocktamer that allows you all the benefits while addressing the concerns the firm has about social media. It is also a useful intra-firm communication system. &nbsp;Please contact me.&nbsp; <a href=\"http://JM@FlockTamer.com\" target=\"_blank\">JM@FlockTamer.com</a> (Of course you can use FlockTamer on your personal phone!)"};

    public FaqAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mGroup.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return mGroup[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return mChild[i];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_elv_group, null);
        }
        CustomTextView listTitleTextView = (CustomTextView) convertView
                .findViewById(R.id.tv_elv_group_title);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(mGroup[i]);
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_elv_child, null);
        }
        CustomTextView expandedListTextView = (CustomTextView) view
                .findViewById(R.id.tv_elv_child);
        expandedListTextView.setText(Html.fromHtml(mChild[i]));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
