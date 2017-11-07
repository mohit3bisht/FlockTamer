package com.flocktamer.model;

import com.flocktamer.model.childmodel.CompanyScrapData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.flocktamer.model.childmodel.CompanyFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amandeepsingh on 29/8/16.
 */
public class CompanyModel {
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

        @SerializedName("feeds")
        @Expose
        private List<CompanyFeed> feeds = new ArrayList<>();

        @SerializedName("max_id")
        @Expose
        private String MaxId;


        @SerializedName("scraped_data")
        @Expose
        private CompanyScrapData scrapedData;

        /**
         * @return The feeds
         */
        public List<CompanyFeed> getFeeds() {
            return feeds;
        }

        /**
         * @param feeds The feeds
         */
        public void setFeeds(List<CompanyFeed> feeds) {
            this.feeds = feeds;
        }

        public String getMaxId() {
            return MaxId;
        }

        public void setMaxId(String maxId) {
            MaxId = maxId;
        }

        public CompanyScrapData getScrapedData() {
            return scrapedData;
        }

        public void setScrapedData(CompanyScrapData scrapedData) {
            this.scrapedData = scrapedData;
        }
    }
}
