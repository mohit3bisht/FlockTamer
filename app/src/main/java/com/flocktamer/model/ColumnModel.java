package com.flocktamer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.flocktamer.model.childmodel.Column;

import java.util.ArrayList;

/**
 * Created by amandeepsingh on 24/8/16.
 */
public class ColumnModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("columns")
        @Expose
        private ArrayList<Column> columns = new ArrayList<>();

        /**
         * @return The columns
         */
        public ArrayList<Column> getColumns() {
            return columns;
        }

        /**
         * @param columns The columns
         */
        public void setColumns(ArrayList<Column> columns) {
            this.columns = columns;
        }

    }
}
