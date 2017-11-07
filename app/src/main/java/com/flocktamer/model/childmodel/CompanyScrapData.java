package com.flocktamer.model.childmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amandeepsingh on 20/2/17.
 */

public class CompanyScrapData {

    @SerializedName("dow_data")
    @Expose
    private String dowData;
    @SerializedName("euro_data")
    @Expose
    private String euroData;
    @SerializedName("gold_data")
    @Expose
    private String goldData;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nsadaq_data")
    @Expose
    private String nsadaqData;
    @SerializedName("oil_data")
    @Expose
    private String oilData;
    @SerializedName("s_p_data")
    @Expose
    private String sPData;
    @SerializedName("updated_data")
    @Expose
    private String updatedData;
    @SerializedName("year_yield_data")
    @Expose
    private String yearYieldData;
    @SerializedName("yen_data")
    @Expose
    private String yenData;

    public String getDowData() {
        return dowData;
    }

    public void setDowData(String dowData) {
        this.dowData = dowData;
    }

    public String getEuroData() {
        return euroData;
    }

    public void setEuroData(String euroData) {
        this.euroData = euroData;
    }

    public String getGoldData() {
        return goldData;
    }

    public void setGoldData(String goldData) {
        this.goldData = goldData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNsadaqData() {
        return nsadaqData;
    }

    public void setNsadaqData(String nsadaqData) {
        this.nsadaqData = nsadaqData;
    }

    public String getOilData() {
        return oilData;
    }

    public void setOilData(String oilData) {
        this.oilData = oilData;
    }

    public String getSPData() {
        return sPData;
    }

    public void setSPData(String sPData) {
        this.sPData = sPData;
    }

    public String getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(String updatedData) {
        this.updatedData = updatedData;
    }

    public String getYearYieldData() {
        return yearYieldData;
    }

    public void setYearYieldData(String yearYieldData) {
        this.yearYieldData = yearYieldData;
    }

    public String getYenData() {
        return yenData;
    }

    public void setYenData(String yenData) {
        this.yenData = yenData;
    }

}
