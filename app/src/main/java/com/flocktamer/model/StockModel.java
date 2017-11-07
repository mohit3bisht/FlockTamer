package com.flocktamer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by amandeepsingh on 1/9/16.
 */
public class StockModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private StockData data;

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
    public StockData getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(StockData data) {
        this.data = data;
    }

    public StockDetail getInstanceStockDetail()
    {
        return new StockDetail();
    }


    public class StockData {

        @SerializedName("List")
        @Expose
        private StockList list;

        /**
         * @return The list
         */
        public StockList getList() {
            return list;
        }

        /**
         * @param list The List
         */
        public void setList(StockList list) {
            this.list = list;
        }

    }


    public class StockList {

        @SerializedName("all")
        @Expose
        private java.util.List<StockDetail> all = new ArrayList<>();
        @SerializedName("editor_choice")
        @Expose
        private java.util.List<StockDetail> editorChoice = new ArrayList<>();

        /**
         * @return The all
         */
        public java.util.List<StockDetail> getAll() {
            return all;
        }

        /**
         * @param all The all
         */
        public void setAll(java.util.List<StockDetail> all) {
            this.all = all;
        }

        /**
         * @return The editorChoice
         */
        public java.util.List<StockDetail> getEditorChoice() {
            return editorChoice;
        }

        /**
         * @param editorChoice The editor_choice
         */
        public void setEditorChoice(java.util.List<StockDetail> editorChoice) {
            this.editorChoice = editorChoice;
        }

    }

    public class StockDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("company")
        @Expose
        private String company;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("selected_col")
        @Expose
        private String selectedColumn;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The company
         */
        public String getCompany() {
            return company;
        }

        /**
         * @param company The company
         */
        public void setCompany(String company) {
            this.company = company;
        }

        /**
         * @return The url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

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

        @Override
        public String toString() {
            return company;
        }

        public String getSelectedColumn() {
            return selectedColumn;
        }

        public void setSelectedColumn(String selectedColumn) {
            this.selectedColumn = selectedColumn;
        }
    }


}
