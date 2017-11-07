package com.flocktamer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.flocktamer.model.childmodel.Feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amandeepsingh on 25/8/16.
 */
public class TwitterFeedModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TwitterData data;

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
    public TwitterData getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(TwitterData data) {
        this.data = data;
    }

    /**
     * Data class for Twitter
     */
    public class TwitterData {
        @SerializedName("pages")
        @Expose
        private String pages;
        @SerializedName("current_page")
        @Expose
        private String currentPage;

        @SerializedName("max_id")
        @Expose
        private String maxId;

        @SerializedName("feeds")
        @Expose
        private List<Feed> feeds = new ArrayList<>();

        /**
         * @return The pages
         */
        public String getPages() {
            return pages;
        }

        /**
         * @param pages The pages
         */
        public void setPages(String pages) {
            this.pages = pages;
        }

        /**
         * @return The currentPage
         */
        public String getCurrentPage() {
            return currentPage;
        }

        /**
         * @param currentPage The current_page
         */
        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        /**
         * @return The feeds
         */
        public List<Feed> getFeeds() {
            return feeds;
        }

        /**
         * @param feeds The feeds
         */
        public void setFeeds(List<Feed> feeds) {
            this.feeds = feeds;
        }

        public String getMaxId() {
            return maxId;
        }

        public void setMaxId(String maxId) {
            this.maxId = maxId;
        }
    }


}
