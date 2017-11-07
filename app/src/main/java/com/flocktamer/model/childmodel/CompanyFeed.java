package com.flocktamer.model.childmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amandeepsingh on 29/8/16.
 */
public class CompanyFeed {
    @SerializedName("rss_url")
    @Expose
    private RssUrl rssUrl;
    @SerializedName("stock_rss_url")
    @Expose
    private StockRssUrl stockRssUrl;
    @SerializedName("stock_rss_feed")
    @Expose
    private StockRssFeed stockRssFeed;
    @SerializedName("rss_feed")
    @Expose
    private List<RssFeed> rssFeed = new ArrayList<>();

    /**
     * @return The rssUrl
     */
    public RssUrl getRssUrl() {
        return rssUrl;
    }

    /**
     * @param rssUrl The rss_url
     */
    public void setRssUrl(RssUrl rssUrl) {
        this.rssUrl = rssUrl;
    }

    /**
     * @return The stockRssUrl
     */
    public StockRssUrl getStockRssUrl() {
        return stockRssUrl;
    }

    /**
     * @param stockRssUrl The stock_rss_url
     */
    public void setStockRssUrl(StockRssUrl stockRssUrl) {
        this.stockRssUrl = stockRssUrl;
    }

    /**
     * @return The stockRssFeed
     */
    public StockRssFeed getStockRssFeed() {
        return stockRssFeed;
    }

    /**
     * @param stockRssFeed The stock_rss_feed
     */
    public void setStockRssFeed(StockRssFeed stockRssFeed) {
        this.stockRssFeed = stockRssFeed;
    }

    /**
     * @return The rssFeed
     */
    public List<RssFeed> getRssFeed() {
        return rssFeed;
    }

    /**
     * @param rssFeed The rss_feed
     */
    public void setRssFeed(List<RssFeed> rssFeed) {
        this.rssFeed = rssFeed;
    }

    public class RssFeed {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("rss_url_id")
        @Expose
        private String rssUrlId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("pub_date")
        @Expose
        private String pubDate;
        @SerializedName("rss_timestamp")
        @Expose
        private String rssTimestamp;
        @SerializedName("guid")
        @Expose
        private String guid;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("modified")
        @Expose
        private String modified;
        @SerializedName("date_format")
        @Expose
        private String dateFormat;

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
         * @return The rssUrlId
         */
        public String getRssUrlId() {
            return rssUrlId;
        }

        /**
         * @param rssUrlId The rss_url_id
         */
        public void setRssUrlId(String rssUrlId) {
            this.rssUrlId = rssUrlId;
        }

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return The description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description The description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return The link
         */
        public String getLink() {
            return link;
        }

        /**
         * @param link The link
         */
        public void setLink(String link) {
            this.link = link;
        }

        /**
         * @return The pubDate
         */
        public String getPubDate() {
            return pubDate;
        }

        /**
         * @param pubDate The pub_date
         */
        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        /**
         * @return The rssTimestamp
         */
        public String getRssTimestamp() {
            return rssTimestamp;
        }

        /**
         * @param rssTimestamp The rss_timestamp
         */
        public void setRssTimestamp(String rssTimestamp) {
            this.rssTimestamp = rssTimestamp;
        }

        /**
         * @return The guid
         */
        public String getGuid() {
            return guid;
        }

        /**
         * @param guid The guid
         */
        public void setGuid(String guid) {
            this.guid = guid;
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

        /**
         * @return The created
         */
        public String getCreated() {
            return created;
        }

        /**
         * @param created The created
         */
        public void setCreated(String created) {
            this.created = created;
        }

        /**
         * @return The modified
         */
        public String getModified() {
            return modified;
        }

        /**
         * @param modified The modified
         */
        public void setModified(String modified) {
            this.modified = modified;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }
    }

    public class RssUrl {

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
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("modified")
        @Expose
        private String modified;

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

        /**
         * @return The created
         */
        public String getCreated() {
            return created;
        }

        /**
         * @param created The created
         */
        public void setCreated(String created) {
            this.created = created;
        }

        /**
         * @return The modified
         */
        public String getModified() {
            return modified;
        }

        /**
         * @param modified The modified
         */
        public void setModified(String modified) {
            this.modified = modified;
        }


    }

    public class StockRssFeed {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("stock_rss_url_id")
        @Expose
        private String stockRssUrlId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("pub_date")
        @Expose
        private String pubDate;
        @SerializedName("rss_timestamp")
        @Expose
        private String rssTimestamp;
        @SerializedName("guid")
        @Expose
        private String guid;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("modified")
        @Expose
        private String modified;
        @SerializedName("Last")
        @Expose
        private String last;
        @SerializedName("Change")
        @Expose
        private String change;
        @SerializedName("%Change")
        @Expose
        private String changePercentage;
        @SerializedName("Volume")
        @Expose
        private String volume;
        @SerializedName("symbol")
        @Expose
        private String symbol;

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
         * @return The stockRssUrlId
         */
        public String getStockRssUrlId() {
            return stockRssUrlId;
        }

        /**
         * @param stockRssUrlId The stock_rss_url_id
         */
        public void setStockRssUrlId(String stockRssUrlId) {
            this.stockRssUrlId = stockRssUrlId;
        }

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return The description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description The description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return The link
         */
        public String getLink() {
            return link;
        }

        /**
         * @param link The link
         */
        public void setLink(String link) {
            this.link = link;
        }

        /**
         * @return The pubDate
         */
        public String getPubDate() {
            return pubDate;
        }

        /**
         * @param pubDate The pub_date
         */
        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        /**
         * @return The rssTimestamp
         */
        public String getRssTimestamp() {
            return rssTimestamp;
        }

        /**
         * @param rssTimestamp The rss_timestamp
         */
        public void setRssTimestamp(String rssTimestamp) {
            this.rssTimestamp = rssTimestamp;
        }

        /**
         * @return The guid
         */
        public String getGuid() {
            return guid;
        }

        /**
         * @param guid The guid
         */
        public void setGuid(String guid) {
            this.guid = guid;
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

        /**
         * @return The created
         */
        public String getCreated() {
            return created;
        }

        /**
         * @param created The created
         */
        public void setCreated(String created) {
            this.created = created;
        }

        /**
         * @return The modified
         */
        public String getModified() {
            return modified;
        }

        /**
         * @param modified The modified
         */
        public void setModified(String modified) {
            this.modified = modified;
        }

        /**
         * @return The last
         */
        public String getLast() {
            return last;
        }

        /**
         * @param last The Last
         */
        public void setLast(String last) {
            this.last = last;
        }

        /**
         * @return The change
         */
        public String getChange() {
            return change;
        }

        /**
         * @param change The Change
         */
        public void setChange(String change) {
            this.change = change;
        }

        /**
         * @return The changePercentage
         */
        public String getChangePercentage() {
            return changePercentage;
        }

        /**
         * @param changePercentage The Change_percentage
         */
        public void setChangePercentage(String changePercentage) {
            this.changePercentage = changePercentage;
        }

        /**
         * @return The volume
         */
        public String getVolume() {
            return volume;
        }

        /**
         * @param volume The Volume
         */
        public void setVolume(String volume) {
            this.volume = volume;
        }

        /**
         * @return The symbol
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * @param symbol The symbol
         */
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

    }

    public class StockRssUrl {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("rss_url_id")
        @Expose
        private String rssUrlId;
        @SerializedName("stock_url")
        @Expose
        private String stockUrl;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("modified")
        @Expose
        private String modified;

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
         * @return The rssUrlId
         */
        public String getRssUrlId() {
            return rssUrlId;
        }

        /**
         * @param rssUrlId The rss_url_id
         */
        public void setRssUrlId(String rssUrlId) {
            this.rssUrlId = rssUrlId;
        }

        /**
         * @return The stockUrl
         */
        public String getStockUrl() {
            return stockUrl;
        }

        /**
         * @param stockUrl The stock_url
         */
        public void setStockUrl(String stockUrl) {
            this.stockUrl = stockUrl;
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

        /**
         * @return The created
         */
        public String getCreated() {
            return created;
        }

        /**
         * @param created The created
         */
        public void setCreated(String created) {
            this.created = created;
        }

        /**
         * @return The modified
         */
        public String getModified() {
            return modified;
        }

        /**
         * @param modified The modified
         */
        public void setModified(String modified) {
            this.modified = modified;
        }


    }
}
