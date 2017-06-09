package com.borovyksv.sql.vehicle;


import java.util.List;

public class FrontendVehicle {

    private String vendor;
    private List<String> models;

    public FrontendVehicle(String vendor, List<String> models) {
        this.vendor = vendor;
        this.models = models;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                " vendor='" + vendor + '\'' +
                ", models='" + models + '\'' +
                '}';
    }

    public FrontendVehicle() {
    }


    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

}
