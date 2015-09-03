package com.example.gil.mymanagerapp;

import java.io.Serializable;

import static java.lang.System.currentTimeMillis;

public class OrderObj implements Serializable {
    static int orderCounter=1;
    static final String STR_EMPTY = "empty";
    private String rstName,addressStr;
    private int addressNum,timeToPickUp,orderStatus,orderId;
    private long orderTime;
    private long orderPickUpTime;
    private long orderDeliveryTime;

    public OrderObj(String rstName, String addressStr, int addressNum, int timeToPickUp) {
        this.addressStr = addressStr;
        this.addressNum = addressNum;
        this.timeToPickUp = timeToPickUp;
        orderTime = currentTimeMillis();
        orderStatus = 0;
        setRstName(rstName);
        setOrderId(orderCounter++);

    }


    public String getRstName() {
        return rstName;
    }

    public void setRstName(String rstName) {
        if (rstName == null)
            this.rstName = STR_EMPTY;
        this.rstName = rstName;
    }
    public String getAddressStr() {
        return addressStr;
    }
    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
    public int isOrderStatus() {
        return orderStatus;
    }
    public int getAddressNum() {
        return addressNum;
    }

    public void setAddressNum(int addressNum) {
        this.addressNum = addressNum;
    }
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int id) {
        this.orderId = id;
    }

    public int getTimeToPickUp() {
        return timeToPickUp;
    }

    public void setTimeToPickUp(int timeToPickUp) {
        this.timeToPickUp = timeToPickUp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rstName + " - " +  addressStr + " " + addressNum + ". Time to Pick Up - " + timeToPickUp);
        return sb.toString();
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getOrderPickUpTime() {
        return orderPickUpTime;
    }

    public void setOrderPickUpTime(long orderPickUpTime) {
        this.orderPickUpTime = orderPickUpTime;
    }

    public long getOrderDeliveryTime() {
        return orderDeliveryTime;
    }

    public void setOrderDeliveryTime(long orderDeliveryTime) {
        this.orderDeliveryTime = orderDeliveryTime;
    }
}

