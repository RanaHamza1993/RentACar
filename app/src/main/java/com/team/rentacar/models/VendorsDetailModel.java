package com.team.rentacar.models;

public class VendorsDetailModel {
    int vendorId;
    int id;
    int image;
    String carName;
    String vendorName;
    String vendorAddress;
    String hourlyPrice;

    public VendorsDetailModel(int vendorId, int id, int image, String carName, String vendorName, String vendorAddress, String hourlyPrice) {
        this.vendorId = vendorId;
        this.id = id;
        this.image = image;
        this.carName = carName;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.hourlyPrice = hourlyPrice;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(String hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }
}

