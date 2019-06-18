package com.team.rentacar.models;

public class MainPageModel {

    private int id;
    private int catImage;
    private String catName;

    public MainPageModel(int id, int catImage, String catName) {
        this.id = id;
        this.catImage = catImage;
        this.catName = catName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatImage() {
        return catImage;
    }

    public void setCatImage(int catImage) {
        this.catImage = catImage;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}
