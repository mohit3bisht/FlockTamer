package com.flocktamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.flocktamer.customview.CustomTextView;
import com.flocktamer.model.StockModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by amandeepsingh on 1/9/16.
 */
public class StockAutoCompleteAdapter extends ArrayAdapter<StockModel.StockDetail> {

    private final List<StockModel.StockDetail> mList;
    //    private ArrayList<StockModel.StockDetail> itemsAll;
    private ArrayList<StockModel.StockDetail> suggestions, tempItems;
    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    public Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((StockModel.StockDetail) resultValue).getCompany();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (StockModel.StockDetail names : tempItems) {
                    if (names.getCompany().toLowerCase().startsWith("$" + constraint.toString().toLowerCase())) {
                        suggestions.add(names);
                    }
                }
                if (suggestions.size() == 0) {
                    suggestions.clear();
                    StockModel.StockDetail detail = new StockModel().getInstanceStockDetail();
                    detail.setCompany("no matches found");
                    detail.setId("-1");
                    suggestions.add(detail);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<StockModel.StockDetail> filterList;
            try {
                filterList = (ArrayList<StockModel.StockDetail>) results.values;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            }

            if (results != null && results.count > 0) {
                clear();
//                for (Iterator<StockModel.StockDetail> it = filterList.iterator(); it.hasNext(); ) {
//                    StockModel.StockDetail s = it.next();
////                    if(s.getClientPoints().getPointsSpent() == 0) {
////                        it.remove();
////                    }
//                    add(s);
//                }
                addAll(filterList);

//                for (StockModel.StockDetail names : filterList) {
//                    add(names);
//                }

                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    };
    private int viewResourceId;

    public StockAutoCompleteAdapter(List<StockModel.StockDetail> list, Context context, int resource) {
        super(context, resource);
        mList = list;
        tempItems = new ArrayList<>(list);
        this.suggestions = new ArrayList<>();
        this.viewResourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        StockModel.StockDetail stock = null;
        try {
            stock = suggestions.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stock != null) {
            CustomTextView customerNameLabel = (CustomTextView) v;
            if (customerNameLabel != null) {
                customerNameLabel.setText(stock.getCompany());

            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}
