package com.company;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Ariel on 8/28/2015.
 */
public class Restaurant extends User {
    private String rstName;
    private int rstId;
    private ArrayList<OrderObj> restaurantOrderList;

    public Restaurant(String userName, String password, String rstName) {
        super(userName,password, 3);
        this.rstName = rstName;
        setRstId();
        restaurantOrderList = new ArrayList<OrderObj>();
    }
    public Restaurant(String userName, String password) {
        this(userName,password,userName);
    }

    public ArrayList<OrderObj> getRestaurantOrderList() {
        return restaurantOrderList;
    }

    public void setRestaurantOrderList(ArrayList<OrderObj> restaurantOrderList) {
        this.restaurantOrderList = restaurantOrderList;
    }

    public String getRstName() {
        return rstName;
    }

    public void setRstName(String rstName) {
        this.rstName = rstName;
    }

    public int getRstId() {
        return rstId;
    }

    public void setRstId() {
        rstId++;
    }

    @Override
    public String toString() {
        return rstName;
    }

    public Restaurant(byte[] bytes){
        super("", "", 0);
        int position = 0;
        this.rstId = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;

        int lengthOfRstName = bytes[position];
        position++;
        this.rstName = new String(bytes, position, lengthOfRstName);
        position += lengthOfRstName;

        position++;
        setUserPermissions(ByteBuffer.wrap(bytes, position, 4).getInt());
        position += 4;
        setCreationDate(ByteBuffer.wrap(bytes, position, 8).getLong());
        position += 8;

        int lengthOfUserName = bytes[position];
        position++;
        setUserName(new String(bytes, position, lengthOfUserName));
        position += lengthOfUserName;

        int lengthOfPassword = bytes[position];
        position++;
        setPassword(new String(bytes, position, lengthOfPassword));
        position += lengthOfPassword;

        int lengthOfOredersArrayBytes =  ByteBuffer.wrap(bytes, position, 4).getInt();
        position+=4;
        byte[] bytes2 = new byte[lengthOfOredersArrayBytes];
        for (int i = 0; i < lengthOfOredersArrayBytes; i++) {
            bytes2[i] = bytes[position + i];
        }
        setRestaurantOrderList(OrderObj.getArrayOrderList(bytes2));
    }
    public byte[] getRestaurantBytes(){
        byte[] userBytes = getUserBytes();
        byte[] bytesOfRstName = rstName.getBytes();
        byte[] restaurantOredersArrayBytes = OrderObj.getBytesOrderList(restaurantOrderList);
        byte[] bytes = new byte[4 + 1 + bytesOfRstName.length + 1 + userBytes.length + 4 + restaurantOredersArrayBytes.length];

        int position = 0;
        ByteBuffer.wrap(bytes, position, 4).putInt(rstId);
        position += 4;

        bytes[position] = (byte)bytesOfRstName.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfRstName.length).put(bytesOfRstName);
        position+=bytesOfRstName.length;

        bytes[position] = (byte) userBytes.length;
        position++;
        ByteBuffer.wrap(bytes, position, userBytes.length).put(userBytes);
        position += userBytes.length;

        int resaurantBytesLength = restaurantOredersArrayBytes.length;
        ByteBuffer.wrap(bytes,position,4).putInt(resaurantBytesLength);
        position +=4;
        ByteBuffer.wrap(bytes, position, resaurantBytesLength).put(restaurantOredersArrayBytes);

        return bytes;
    }


}