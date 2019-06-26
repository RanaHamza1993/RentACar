package com.team.rentacar.models;

public class VendorsDetailModel {
   // int vendorId;
    String id;
    String image;
    String carName;
    String vendorName;
    String vendorAddress;
    String hourlyPrice;
    String bookedBy;
    boolean isBooked;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public VendorsDetailModel(String id, String image, String carName, String vendorName, String vendorAddress, String hourlyPrice, String bookedBy, boolean isBooked,String uid) {
        this.id = id;
        this.image = image;
        this.carName = carName;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.hourlyPrice = hourlyPrice;
        this.bookedBy = bookedBy;
        this.isBooked = isBooked;
        this.uid=uid;
    }
    public VendorsDetailModel(String id, String image, String carName, String vendorName, String vendorAddress, String hourlyPrice,  boolean isBooked,String uid) {
        this.id = id;
        this.image = image;
        this.carName = carName;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.hourlyPrice = hourlyPrice;
        this.isBooked = isBooked;
        this.uid=uid;
    }
//    public int getVendorId() {
//        return vendorId;
//    }
//
//    public void setVendorId(int vendorId) {
//        this.vendorId = vendorId;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

