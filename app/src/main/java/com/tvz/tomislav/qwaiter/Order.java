package com.tvz.tomislav.qwaiter;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Order {

    private List<FoodDrinkModel> orderList;
    private FirebaseUser userData;
    private String orderDate;
    private int subtotal;
    private String waiter;
    private String paymentModel;
    private String placeName;
    private String placeCategory;
    private int table;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(String placeCategory) {
        this.placeCategory = placeCategory;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }



    public Order() {
    }

    public Order(List<FoodDrinkModel> OrderList, FirebaseUser userData, String orderDate, int subtotal) {
        this.orderList = OrderList;
        this.userData = userData;
        this.orderDate = orderDate;
        this.subtotal = subtotal;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getPaymentModel() {
        return paymentModel;
    }

    public void setPaymentModel(String paymentModel) {
        this.paymentModel = paymentModel;
    }

    public List<FoodDrinkModel> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<FoodDrinkModel> orderList) {
        this.orderList = orderList;
    }

    public FirebaseUser getUserData() {
        return userData;
    }

    public void setUserData(FirebaseUser userData) {
        this.userData = userData;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
