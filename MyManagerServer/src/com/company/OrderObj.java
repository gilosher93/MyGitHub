package com.company;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class OrderObj implements Serializable {
    public static final int ORDER_CREATED = 0;
    public static final int ASCRIBE_TO_MESSANGER = 1;
    public static final int ORDER_PICKED_UP = 2;
    public static final int ORDER_FINISHED = 3;
    static int orderCounter=1;
    static final String STR_EMPTY = "empty";
    private String rstName,addressStr,addressNum, comment, ascribeTo;
    private int timeToPickUp,orderStatus,orderId, paymentMethod;
    private long orderTime, orderPickUpTime, orderDeliveryTime;

    public OrderObj(String rstName, String addressStr, String addressNum, int timeToPickUp, String comment, int paymentMethod) {
        setRstName(rstName);
        setAddressStr(addressStr);
        setAddressNum(addressNum);
        this.timeToPickUp = timeToPickUp;
        setComment(comment);
        this.paymentMethod = paymentMethod;
        orderTime = currentTimeMillis();
        orderStatus = ORDER_CREATED;
        setOrderId(orderCounter++);
        this.ascribeTo = STR_EMPTY;
    }

    public String getAscribeTo() {
        return ascribeTo;
    }

    public void setAscribeTo(String messanger) {
        this.ascribeTo = messanger;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAddressStr(String addressStr) {
        if (addressStr == null)
            this.addressStr = STR_EMPTY;
        this.addressStr = addressStr;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (comment == null)
            this.comment = STR_EMPTY;
        this.comment = comment;
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
    public String getAddressNum() {
        return addressNum;
    }

    public void setAddressNum(String addressNum) {
        if (addressNum == null)
            this.addressNum = STR_EMPTY;
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

    public OrderObj(byte[] bytes){
        int position = 0;
        this.timeToPickUp = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;
        this.orderStatus = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;
        this.orderId = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;
        this.paymentMethod = ByteBuffer.wrap(bytes, position, 4).getInt();
        position += 4;
        this.orderTime = ByteBuffer.wrap(bytes, position, 8).getLong();
        position += 8;
        this.orderPickUpTime = ByteBuffer.wrap(bytes, position, 8).getLong();
        position += 8;
        this.orderDeliveryTime = ByteBuffer.wrap(bytes, position, 8).getLong();
        position += 8;

        int lengthOfRstName = bytes[position];
        position++;
        this.rstName = new String(bytes, position, lengthOfRstName);
        position += lengthOfRstName;

        int lengthOfAddressStr = bytes[position];
        position++;
        this.addressStr = new String(bytes, position, lengthOfAddressStr);
        position += lengthOfAddressStr;

        int lengthOfAddressNum = bytes[position];
        position++;
        this.addressNum = new String(bytes, position, lengthOfAddressNum);
        position += lengthOfAddressNum;

        int lengthOfComment = ByteBuffer.wrap(bytes, position, 4).getInt();
        position+=4;
        this.comment = new String(bytes, position, lengthOfComment);
        position += lengthOfComment;

        int lengthOfAscribeTo = bytes[position];
        position++;
        this.ascribeTo = new String(bytes, position, lengthOfAscribeTo);
    }
    public byte[] getOrderBytes(){
        byte[] bytesOfRstName = rstName.getBytes();
        byte[] bytesOfAddressStr = addressStr.getBytes();
        byte[] bytesOfAddressNum = addressNum.getBytes();
        byte[] bytesOfComment = comment.getBytes();
        byte[] bytesOfAscribeTo = ascribeTo.getBytes();
        byte[] bytes = new byte[4*4 + 8*3 + 4 +  bytesOfRstName.length
                + bytesOfAddressStr.length + bytesOfAddressNum.length +4+ bytesOfComment.length + bytesOfAscribeTo.length];
        int position = 0;
        ByteBuffer.wrap(bytes, position, 4).putInt(timeToPickUp);
        position += 4;
        ByteBuffer.wrap(bytes, position, 4).putInt(orderStatus);
        position += 4;
        ByteBuffer.wrap(bytes, position, 4).putInt(orderId);
        position += 4;
        ByteBuffer.wrap(bytes, position, 4).putInt(paymentMethod);
        position += 4;
        ByteBuffer.wrap(bytes, position, 8).putLong(orderTime);
        position += 8;
        ByteBuffer.wrap(bytes, position, 8).putLong(orderPickUpTime);
        position += 8;
        ByteBuffer.wrap(bytes, position, 8).putLong(orderDeliveryTime);
        position += 8;

        bytes[position] = (byte)bytesOfRstName.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfRstName.length).put(bytesOfRstName);
        position+=bytesOfRstName.length;

        bytes[position] = (byte)bytesOfAddressStr.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfAddressStr.length).put(bytesOfAddressStr);
        position+=bytesOfAddressStr.length;

        bytes[position] = (byte)bytesOfAddressNum.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfAddressNum.length).put(bytesOfAddressNum);
        position+=bytesOfAddressNum.length;

        int commentLength = bytesOfComment.length;
        ByteBuffer.wrap(bytes, position, 4).putInt(commentLength);// !!!!!!!!
        position+=4;
        ByteBuffer.wrap(bytes, position, commentLength).put(bytesOfComment);
        position+=bytesOfComment.length;

        bytes[position] = (byte)bytesOfAscribeTo.length; // !!!!!!!!
        position++;
        ByteBuffer.wrap(bytes, position, bytesOfAscribeTo.length).put(bytesOfAscribeTo);
        return bytes;
    }
    public static byte[] getBytesOrderList(ArrayList<OrderObj> arrayList){
        int counter = 0 , bLength;
        for (int i = 0; i <arrayList.size() ; i++) {
            counter += arrayList.get(i).getOrderBytes().length;
            counter += 4;
        }
        byte[] bytes = new byte[counter];
        counter = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            bLength = arrayList.get(i).getOrderBytes().length;
            ByteBuffer.wrap(bytes,counter,4).putInt(bLength);
            counter+=4;
            ByteBuffer.wrap(bytes,counter,bLength).put(arrayList.get(i).getOrderBytes());
            counter += bLength;
        }
        return bytes;
    }
    public static ArrayList<OrderObj> getArrayOrderList(byte[] bytes){
        ArrayList<OrderObj> arrayList = new ArrayList<OrderObj>();
        int bLength,position = 0;

        while (position<bytes.length) {
            bLength = ByteBuffer.wrap(bytes, position, 4).getInt();
            position+=4;
            byte[] byteO = new byte[bLength];
            for (int j = 0; j < bLength; j++) {
                byteO[j] = bytes[position+j];
            }
            arrayList.add(new OrderObj(byteO));
            position +=bLength;
        }

        return arrayList;
    }

}