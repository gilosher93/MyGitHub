package com.example.gil.mymanagerapp;

import java.io.Serializable;

/**
 * Created by Gil on 2/08/2015.
 */
public class RstObj implements Serializable{
    //private User user;
    private String RstName;
    static int countId;
    private int rstID;

    /*public RstObj(User user) {
        this.user = user;
        RstName = user.getUserName();
        setRstID(countId++);
    }*/

    public int getRstID() {
        return rstID;
    }

    @Override
    public String toString() {
        return RstName;
    }

    public void setRstID(int rstID) {
        this.rstID = rstID;
    }
}
