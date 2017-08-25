package com.tvz.tomislav.qwaiter;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Order {

    private List<FoodDrinkModel> orderList;
    private String userID;
    private String orderDate;
    private int subtotal;
    private String waiter;
    private String paymentModel;
    private int table;


    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }



    public Order() {
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userData) {
        this.userID = userData;
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
