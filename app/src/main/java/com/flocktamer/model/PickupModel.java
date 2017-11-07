package com.flocktamer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.flocktamer.model.childmodel.PickupColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amandeepsingh on 31/8/16.
 */
public class PickupModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private PickupData data;

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The data
     */
    public PickupData getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(PickupData data) {
        this.data = data;
    }


    public class PickupData {
        @SerializedName("columns")
        @Expose
        private List<PickupColumn> columns = new ArrayList<>();

        /**
         * @return The columns
         */
        public List<PickupColumn> getColumns() {
            return columns;
        }

        /**
         * @param columns The columns
         */
        public void setColumns(List<PickupColumn> columns) {
            this.columns = columns;
        }
    }
}