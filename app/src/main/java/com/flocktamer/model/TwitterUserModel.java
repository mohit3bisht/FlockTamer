package com.flocktamer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.flocktamer.model.childmodel.TwitterUser;

import java.util.ArrayList;

/**
 * Created by amandeepsingh on 2/9/16.
 */
public class TwitterUserModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<TwitterUser> data = new ArrayList<>();

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
    public ArrayList<TwitterUser> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(ArrayList<TwitterUser> data) {
        this.data = data;
    }

}