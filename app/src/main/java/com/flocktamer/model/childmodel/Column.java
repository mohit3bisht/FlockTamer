package com.flocktamer.model.childmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amandeepsingh on 24/8/16.
 */
public class Column implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("column_name")
    @Expose
    private String columnName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("twitter_list_url")
    @Expose
    private String twitterListUrl;
    @SerializedName("column_abbr")
    @Expose
    private String columnAbbr;
    @SerializedName("twitter_widget")
    @Expose
    private String twitterWidget;
    @SerializedName("twitter_id")
    @Expose
    private String twitterId;
    @SerializedName("column_sequence")
    @Expose
    private String columnSequence;
    @SerializedName("h2_div_style")
    @Expose
    private String h2DivStyle;
    @SerializedName("content_div_id")
    @Expose
    private String contentDivId;
    @SerializedName("content_div_class")
    @Expose
    private String contentDivClass;
    @SerializedName("content_style")
    @Expose
    private String contentStyle;
    @SerializedName("main_div_id")
    @Expose
    private String mainDivId;
    @SerializedName("main_div_class")
    @Expose
    private String mainDivClass;
    @SerializedName("main_div_style")
    @Expose
    private String mainDivStyle;
    @SerializedName("editor_choice")
    @Expose
    private String editorChoice;
    @SerializedName("sponsored")
    @Expose
    private String sponsored;

    @SerializedName("sponsored_company_colour")
    @Expose
    private String sponsoredCompanyColour;

    @SerializedName("sponsored_url")
    @Expose
    private String sponsoredUrl;
    @SerializedName("is_default")
    @Expose
    private String isDefault;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("main_div_colour")
    @Expose
    private String mainDivColour;
    @SerializedName("h2_div_colour")
    @Expose
    private String h2DivColour;
    @SerializedName("col_type")
    @Expose
    private String colType;
    @SerializedName("ajax_running_time")
    @Expose
    private String ajaxRunningTime;

    public Column() {
    }

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
     * @return The columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName The column_name
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
     * @return The twitterListUrl
     */
    public String getTwitterListUrl() {
        return twitterListUrl;
    }

    /**
     * @param twitterListUrl The twitter_list_url
     */
    public void setTwitterListUrl(String twitterListUrl) {
        this.twitterListUrl = twitterListUrl;
    }

    /**
     * @return The columnAbbr
     */
    public String getColumnAbbr() {
        return columnAbbr;
    }

    /**
     * @param columnAbbr The column_abbr
     */
    public void setColumnAbbr(String columnAbbr) {
        this.columnAbbr = columnAbbr;
    }

    /**
     * @return The twitterWidget
     */
    public String getTwitterWidget() {
        return twitterWidget;
    }

    /**
     * @param twitterWidget The twitter_widget
     */
    public void setTwitterWidget(String twitterWidget) {
        this.twitterWidget = twitterWidget;
    }

    /**
     * @return The twitterId
     */
    public String getTwitterId() {
        return twitterId;
    }

    /**
     * @param twitterId The twitter_id
     */
    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    /**
     * @return The columnSequence
     */
    public String getColumnSequence() {
        return columnSequence;
    }

    /**
     * @param columnSequence The column_sequence
     */
    public void setColumnSequence(String columnSequence) {
        this.columnSequence = columnSequence;
    }

    /**
     * @return The h2DivStyle
     */
    public String getH2DivStyle() {
        return h2DivStyle;
    }

    /**
     * @param h2DivStyle The h2_div_style
     */
    public void setH2DivStyle(String h2DivStyle) {
        this.h2DivStyle = h2DivStyle;
    }

    /**
     * @return The contentDivId
     */
    public String getContentDivId() {
        return contentDivId;
    }

    /**
     * @param contentDivId The content_div_id
     */
    public void setContentDivId(String contentDivId) {
        this.contentDivId = contentDivId;
    }

    /**
     * @return The contentDivClass
     */
    public String getContentDivClass() {
        return contentDivClass;
    }

    /**
     * @param contentDivClass The content_div_class
     */
    public void setContentDivClass(String contentDivClass) {
        this.contentDivClass = contentDivClass;
    }

    /**
     * @return The contentStyle
     */
    public String getContentStyle() {
        return contentStyle;
    }

    /**
     * @param contentStyle The content_style
     */
    public void setContentStyle(String contentStyle) {
        this.contentStyle = contentStyle;
    }

    /**
     * @return The mainDivId
     */
    public String getMainDivId() {
        return mainDivId;
    }

    /**
     * @param mainDivId The main_div_id
     */
    public void setMainDivId(String mainDivId) {
        this.mainDivId = mainDivId;
    }

    /**
     * @return The mainDivClass
     */
    public String getMainDivClass() {
        return mainDivClass;
    }

    /**
     * @param mainDivClass The main_div_class
     */
    public void setMainDivClass(String mainDivClass) {
        this.mainDivClass = mainDivClass;
    }

    /**
     * @return The mainDivStyle
     */
    public String getMainDivStyle() {
        return mainDivStyle;
    }

    /**
     * @param mainDivStyle The main_div_style
     */
    public void setMainDivStyle(String mainDivStyle) {
        this.mainDivStyle = mainDivStyle;
    }

    /**
     * @return The editorChoice
     */
    public String getEditorChoice() {
        return editorChoice;
    }

    /**
     * @param editorChoice The editor_choice
     */
    public void setEditorChoice(String editorChoice) {
        this.editorChoice = editorChoice;
    }

    /**
     * @return The sponsored
     */
    public String getSponsored() {
        return sponsored;
    }

    /**
     * @param sponsored The sponsored
     */
    public void setSponsored(String sponsored) {
        this.sponsored = sponsored;
    }

    /**
     * @return The sponsoredUrl
     */
    public String getSponsoredUrl() {
        return sponsoredUrl;
    }

    /**
     * @param sponsoredUrl The sponsored_url
     */
    public void setSponsoredUrl(String sponsoredUrl) {
        this.sponsoredUrl = sponsoredUrl;
    }

    /**
     * @return The isDefault
     */
    public String getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault The is_default
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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
     * @return The mainDivColour
     */
    public String getMainDivColour() {
        return mainDivColour;
    }

    /**
     * @param mainDivColour The main_div_colour
     */
    public void setMainDivColour(String mainDivColour) {
        this.mainDivColour = mainDivColour;
    }

    /**
     * @return The h2DivColour
     */
    public String getH2DivColour() {
        return h2DivColour;
    }

    /**
     * @param h2DivColour The h2_div_colour
     */
    public void setH2DivColour(String h2DivColour) {
        this.h2DivColour = h2DivColour;
    }

    /**
     * @return The colType
     */
    public String getColType() {
        return colType;
    }

    /**
     * @param colType The col_type
     */
    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getAjaxRunningTime() {
        return ajaxRunningTime;
    }

    public void setAjaxRunningTime(String ajaxRunningTime) {
        this.ajaxRunningTime = ajaxRunningTime;
    }

    public String getSponsoredCompanyColour() {
        return sponsoredCompanyColour;
    }

    public void setSponsoredCompanyColour(String sponsoredCompanyColour) {
        this.sponsoredCompanyColour = sponsoredCompanyColour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.columnName);
        dest.writeString(this.description);
        dest.writeString(this.twitterListUrl);
        dest.writeString(this.columnAbbr);
        dest.writeString(this.twitterWidget);
        dest.writeString(this.twitterId);
        dest.writeString(this.columnSequence);
        dest.writeString(this.h2DivStyle);
        dest.writeString(this.contentDivId);
        dest.writeString(this.contentDivClass);
        dest.writeString(this.contentStyle);
        dest.writeString(this.mainDivId);
        dest.writeString(this.mainDivClass);
        dest.writeString(this.mainDivStyle);
        dest.writeString(this.editorChoice);
        dest.writeString(this.sponsored);
        dest.writeString(this.sponsoredCompanyColour);
        dest.writeString(this.sponsoredUrl);
        dest.writeString(this.isDefault);
        dest.writeString(this.status);
        dest.writeString(this.created);
        dest.writeString(this.modified);
        dest.writeString(this.mainDivColour);
        dest.writeString(this.h2DivColour);
        dest.writeString(this.colType);
        dest.writeString(this.ajaxRunningTime);
    }

    protected Column(Parcel in) {
        this.id = in.readString();
        this.columnName = in.readString();
        this.description = in.readString();
        this.twitterListUrl = in.readString();
        this.columnAbbr = in.readString();
        this.twitterWidget = in.readString();
        this.twitterId = in.readString();
        this.columnSequence = in.readString();
        this.h2DivStyle = in.readString();
        this.contentDivId = in.readString();
        this.contentDivClass = in.readString();
        this.contentStyle = in.readString();
        this.mainDivId = in.readString();
        this.mainDivClass = in.readString();
        this.mainDivStyle = in.readString();
        this.editorChoice = in.readString();
        this.sponsored = in.readString();
        this.sponsoredCompanyColour = in.readString();
        this.sponsoredUrl = in.readString();
        this.isDefault = in.readString();
        this.status = in.readString();
        this.created = in.readString();
        this.modified = in.readString();
        this.mainDivColour = in.readString();
        this.h2DivColour = in.readString();
        this.colType = in.readString();
        this.ajaxRunningTime = in.readString();
    }

    public static final Creator<Column> CREATOR = new Creator<Column>() {
        @Override
        public Column createFromParcel(Parcel source) {
            return new Column(source);
        }

        @Override
        public Column[] newArray(int size) {
            return new Column[size];
        }
    };
}
