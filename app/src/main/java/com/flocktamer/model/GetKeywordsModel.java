package com.flocktamer.model;

import com.flocktamer.model.childmodel.GetKeywordsData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shahbaz on 28/6/17.
 */

public class GetKeywordsModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private GetKeywordsData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetKeywordsData getData() {
        return data;
    }

    public void setData(GetKeywordsData data) {
        this.data = data;
    }

}
