package com.elfec.ssc.model;

/**
 * Created by drodriguez on 05/12/2016.
 * Device stuff
 */

public class Device {
    private String phoneNumber;
    private String brand; 
    private String model;
    private String imei; 
    private String gcmToken;

    public Device() {
    }

    public Device(String brand, String model, String imei, String gcmToken) {
        this.phoneNumber = "";
        this.brand = brand;
        this.model = model;
        this.imei = imei;
        this.gcmToken = gcmToken;
    }

    //region getter setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
    //endregion
}
