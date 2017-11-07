package com.flocktamer.model.childmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by amandeepsingh on 31/8/16.
 */
public class PickupColumn {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("column_name")
    @Expose
    private String columnName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("column_sequence")
    @Expose
    private int columnSequence;
    @SerializedName("editor_choice")
    @Expose
    private String editorChoice;
    @SerializedName("sponsored")
    @Expose
    private String sponsored;
    @SerializedName("sponsored_url")
    @Expose
    private String sponsoredUrl;
    @SerializedName("is_default")
    @Expose
    private String isDefault;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("main_div_style")
    @Expose
    private String mainDivStyle;
    @SerializedName("h2_div_style")
    @Expose
    private String h2DivStyle;
    @SerializedName("main_div_colour")
    @Expose
    private String mainDivColour;
    @SerializedName("h2_div_colour")
    @Expose
    private String h2DivColour;
    @SerializedName("selected_col")
    @Expose
    private String isSelected;


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
     * @return The columnSequence
     */
    public int getColumnSequence() {
        return columnSequence;
    }

    /**
     * @param columnSequence The column_sequence
     */
    public void setColumnSequence(int columnSequence) {
        this.columnSequence = columnSequence;
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

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

}
