package com.team.rentacar.models;

public class VendorsModel implements Comparable<VendorsModel> {
    int id;
    int image;
    String name;

    public VendorsModel(int id, int image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public int compareTo(VendorsModel o) {
        if(this.id==o.id){
            return 0;
        }
        else if(this.id < o.id){
            return -1;
        }
        else{
            return 1;
        }

    }
}
