package com.mindiii.jeparlelebassa.model;

import java.io.Serializable;

/**
 * Created by mindiii on 13/4/17.
 */

public class UserInfo implements Serializable {

    public String fullName, email, userImage, deviceToken, userImageThumb, authToken,address,totalScore;
    public Integer userId;
    public int languageType;
    public double lat, lng;


    public UserInfo() {
        this.fullName = "";
        this.email = "";
        this.userImage = "";
        this.deviceToken = "";
        this.totalScore = "";
        this.userImageThumb = "";
        this.authToken = "";
        this.userId = null;
        this.languageType = 2;
        this.lat = 0.0d;
        this.lng = 0.0d;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserImageThumb() {
        return userImageThumb;
    }

    public void setUserImageThumb(String userImageThumb) {
        this.userImageThumb = userImageThumb;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLanguageType() {
        return languageType;
    }

    public void setLanguageType(Integer languageType) {
        this.languageType = languageType;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public void setLanguageType(int languageType) {
        this.languageType = languageType;
    }
}