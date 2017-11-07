package com.flocktamer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.flocktamer.model.childmodel.CompanyFeed;

import java.util.Map;

/**
 * Created by amandeepsingh on 15/9/16.
 */
public class RefreshCompanyModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private RefreshCompanyData data;

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
    public RefreshCompanyData getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(RefreshCompanyData data) {
        this.data = data;
    }

    public class RefreshCompanyData {
        @SerializedName("max_id")
        @Expose
        private String maxId;
        @SerializedName("new_record")
        @Expose
        private String newRecord;
        @SerializedName("feeds")
        @Expose
        private Map<String, CompanyFeed> feeds;

        /**
         * @return The maxId
         */
        public String getMaxId() {
            return maxId;
        }

        /**
         * @param maxId The max_id
         */
        public void setMaxId(String maxId) {
            this.maxId = maxId;
        }

        /**
         * @return The newRecord
         */
        public String getNewRecord() {
            return newRecord;
        }

        /**
         * @param newRecord The new_record
         */
        public void setNewRecord(String newRecord) {
            this.newRecord = newRecord;
        }

        public Map<String, CompanyFeed> getFeeds() {
            return feeds;
        }

        public void setFeeds(Map<String, CompanyFeed> feeds) {
            this.feeds = feeds;
        }
    }
}
