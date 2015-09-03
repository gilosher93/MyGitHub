package com.example.gil.mymanagerapp.ObjectClasses;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Worker extends User {
    private String firstName,lastName, phoneNumber;
    private int workerId,onShift;
    private ArrayList<OrderObj> workerOrderList;

    public Worker(String userName, String password,String firstName, String lastName, String phoneNumber) {
        super(userName, password, 2);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.onShift = 0;
        setWorkerId();
        workerOrderList = new ArrayList<OrderObj>();
    }

    public Worker(String userName, String password,String firstName, String lastName) {
        this(userName, password,firstName,lastName," ");
    }
    public Worker(String firstName, String lastName,String phoneNumber) {
        this(firstName, phoneNumber,firstName,lastName,phoneNumber);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public ArrayList<OrderObj> getWorkerOrderList() {
        return workerOrderList;
    }

    public void setWorkerOrderList(ArrayList<OrderObj> workerOrderList) {
        this.workerOrderList = workerOrderList;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int isOnShift() {
        return onShift;
    }

    public void setOnShift(int onShift) {
        this.onShift = onShift;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId() {
        workerId++;
    }
    public static void print(byte[] bytes){

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + ",");
        }
        System.out.println();
    }
    public Worker(byte[] bytes) {
        super("", "", 0);
        int position = 0;
        this.workerId = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;
        this.onShift = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;

        int lengthOfFirstName = bytes[position];
        position++;
        this.firstName = new String(bytes, position, lengthOfFirstName);
        position += lengthOfFirstName;

        int lengthOfLastName = bytes[position];
        position++;
        this.lastName = new String(bytes, position, lengthOfLastName);
        position += lengthOfLastName;

        int lengthOfPhone = bytes[position];
        position++;
        this.phoneNumber = new String(bytes, position, lengthOfPhone);
        position += lengthOfPhone;

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
        setWorkerOrderList(OrderObj.getArrayOrderList(bytes2));
    }
    public byte[] getWorkerBytes() {
        byte[] userBytes = getUserBytes();
        byte[] oredersArrayBytes = OrderObj.getBytesOrderList(workerOrderList);
        byte[] bytesOfFisrtName = firstName.getBytes();
        byte[] bytesOfLastName = lastName.getBytes();
        byte[] bytesOfPhone = phoneNumber.getBytes();
        byte[] bytes = new byte[4 * 2 + 3 + bytesOfFisrtName.length + bytesOfLastName.length + bytesOfPhone.length + 1 + userBytes.length + 4 + oredersArrayBytes.length];
        int position = 0;
        ByteBuffer.wrap(bytes, position, 4).putInt(workerId);
        position += 4;
        ByteBuffer.wrap(bytes, position, 4).putInt(onShift);
        position += 4;

        bytes[position] = (byte) bytesOfFisrtName.length;
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfFisrtName.length).put(bytesOfFisrtName);
        position += bytesOfFisrtName.length;

        bytes[position] = (byte) bytesOfLastName.length;
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfLastName.length).put(bytesOfLastName);
        position += bytesOfLastName.length;

        bytes[position] = (byte) bytesOfPhone.length;
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfPhone.length).put(bytesOfPhone);
        position += bytesOfPhone.length;

        bytes[position] = (byte) userBytes.length;
        position++;
        ByteBuffer.wrap(bytes, position, userBytes.length).put(userBytes);
        position += userBytes.length;

        int orderArrayLength = oredersArrayBytes.length;
        ByteBuffer.wrap(bytes, position, 4).putInt(orderArrayLength);
        position +=4;
        ByteBuffer.wrap(bytes, position, orderArrayLength).put(oredersArrayBytes);

        return bytes;
    }
    public static ArrayList<Worker> getArrayOfWorkerList(byte[] bytes){
        ArrayList<Worker> workers = new ArrayList<Worker>();
        int bLength,position = 0;

        while (position<bytes.length) {
            bLength = ByteBuffer.wrap(bytes, position, 4).getInt();
            position+=4;
            byte[] byteO = new byte[bLength];
            for (int j = 0; j < bLength; j++) {
                byteO[j] = bytes[position+j];
            }
            workers.add(new Worker(byteO));
            position +=bLength;
        }

        return workers;
    }
}
