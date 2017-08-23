package com.tvz.tomislav.qwaiter;

/**
 * Created by tomislav on 23/08/17.
 */

public class Category {

    private String name;
    private String imageURL;

    public Category(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
