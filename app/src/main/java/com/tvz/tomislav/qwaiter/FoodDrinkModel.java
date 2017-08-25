package com.tvz.tomislav.qwaiter;

/**
 * Created by tomislav on 23/08/17.
 */

public class FoodDrinkModel {

    private String imageURL;
    private String name;
    private Integer price;
    private int quantity;
    private String category;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

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
