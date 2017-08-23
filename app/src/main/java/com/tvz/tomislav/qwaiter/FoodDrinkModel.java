package com.tvz.tomislav.qwaiter;

/**
 * Created by tomislav on 23/08/17.
 */

public class FoodDrinkModel {

    private String imageURL;
    private String name;

    public FoodDrinkModel() {
    }

    public FoodDrinkModel(String imageURL, String name) {
        this.imageURL = imageURL;
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
