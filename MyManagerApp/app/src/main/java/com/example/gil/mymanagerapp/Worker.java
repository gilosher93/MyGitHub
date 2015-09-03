package com.example.gil.mymanagerapp;

import java.io.Serializable;
import java.util.Date;

public class Worker implements Serializable{
    private String firstName,lastName,phone;
    //private Date date;
    private int WorkerId;
    private boolean onShift;

    public Worker(String firstName, String lastName/*, Date date*/, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        //this.date = date;
        this.phone = phone;
        ++WorkerId;
    }
    public Worker(String firstName, String lastName) {
        //date = new Date(2000,01,01);
        this(firstName,lastName,"0");
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " ";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
    }

    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
*/
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public boolean isOnShift() {
        return onShift;
    }

    public void setOnShift() {
        onShift = !onShift;
    }

    public int getWorkerId() {
        return WorkerId;
    }

    public void setWorkerId(int workerId) {
        WorkerId = workerId;
    }
}
