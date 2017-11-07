package com.flocktamer.interfaces;

import com.flocktamer.model.childmodel.PickupColumn;

import java.util.Comparator;

/**
 * Created by amandeepsingh on 7/12/16.
 */

public class SortComparetor implements Comparator<PickupColumn> {

    @Override
    public int compare(PickupColumn col1, PickupColumn col2) {

        if (col1.getColumnSequence() > col2.getColumnSequence()) {
            return 1;
        } else if (col1.getColumnSequence() == col2.getColumnSequence()) {
            return 0;
        } else {
            return -1;
        }
    }

}
