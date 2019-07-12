package com.team.rentacar.models;

public class VendorsDetailModel {
    private  String rentDays;
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
    String driverName;
    String driverNumber;
    int discount;
    String bookingDate;

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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getRentDays() {
        return rentDays;
    }

    public void setRentDays(String rentDays) {
        this.rentDays = rentDays;
    }

    public VendorsDetailModel(String id, String image, String carName, String vendorName, String vendorAddress, String hourlyPrice, String bookedBy, boolean isBooked, String uid) {
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
    public VendorsDetailModel(String id, String image, String carName, String vendorName, String vendorAddress, String hourlyPrice,String bookedBy, boolean isBooked, String uid, String driverName, String driverNumber, int discount,String bookingDate,String rentDays) {
        this.id = id;
        this.image = image;
        this.carName = carName;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.hourlyPrice = hourlyPrice;
        this.isBooked = isBooked;
        this.uid=uid;
        this.driverName=driverName;
        this.driverNumber=driverNumber;
        this.discount=discount;
        this.bookedBy=bookedBy;
        this.bookingDate=bookingDate;
        this.rentDays=rentDays;
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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

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

