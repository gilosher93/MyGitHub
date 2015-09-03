package com.example.gil.mymanagerapp.ObjectClasses;

import java.io.Serializable;
import java.nio.ByteBuffer;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Gil on 24/08/2015.
 */
public class User implements Serializable {
    private final static String STR_EMPTY = " ";
    private String userName,password;
    private long creationDate;
    private int userPermissions;

    public User(String userName, String password, int userPermissions) {
        this.userName = userName;
        this.password = password;
        this.userPermissions = userPermissions;
        creationDate = currentTimeMillis();
    }

    public int getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(int userPermissions) {
        this.userPermissions = userPermissions;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return userName + " " + password + " user permission: " + userPermissions;
    }

    public boolean validPassword(String password){
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if (o == this)
            return true;

        //this check is unnecessary if we trust the code that uses this class
        //remove this to improve performance
        if(this.userName.equals(STR_EMPTY) || this.password.equals(STR_EMPTY))
            return false;
        if(o instanceof User){
            User other = (User) o;
            return this.userName.equals(other.userName) && this.password.equals(other.password);
        }
        return false;
    }
    public User(byte[] bytes){
        int position = 0;
        this.userPermissions = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;
        this.creationDate = ByteBuffer.wrap(bytes, position, 8).getLong();
        position += 8;

        int lengthOfUserName = bytes[position];
        position++;
        this.userName = new String(bytes, position, lengthOfUserName);
        position += lengthOfUserName;

        int lengthOfPassword = bytes[position];
        position++;
        this.password = new String(bytes, position, lengthOfPassword);
    }
    public byte[] getUserBytes(){
        byte[] bytesOfUserName = userName.getBytes();
        byte[] bytesOfPassword = password.getBytes();
        byte[] bytes = new byte[4 + 8 + 2 +  bytesOfUserName.length + bytesOfPassword.length];
        int position = 0;
        ByteBuffer.wrap(bytes, position, 4).putInt(userPermissions);
        position += 4;
        ByteBuffer.wrap(bytes, position, 8).putLong(creationDate);
        position += 8;

        bytes[position] = (byte)bytesOfUserName.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfUserName.length).put(bytesOfUserName);
        position+=bytesOfUserName.length;

        bytes[position] = (byte)bytesOfPassword.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfPassword.length).put(bytesOfPassword);

        return bytes;
    }
}
