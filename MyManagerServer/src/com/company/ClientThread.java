package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gil on 24/08/2015.
 */
public class ClientThread extends Thread {

    public static final int LOG_IN = 10;
    public static final int CHECK_FOR_ORDERS = 20;
    public static final int GET_WORKERS_LIST = 30;
    public static final int ADD_TO_WORKERS_LIST = 31;
    public static final int DELETE_FROM_WORKERS_LIST = 32;
    public static final int GET_RESTAURANT_LIST = 40;
    public static final int ADD_TO_RESTAURANT_LIST = 41;
    public static final int DELETE_FROM_RESTAURANT_LIST = 42;
    public static final int FAILURE = 99;
    public static final int SUCCESS = 100;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    HashMap<String,User> users;
    ArrayList<Worker> workers;
    ArrayList<Restaurant> myRsts;
    ArrayList<OrderObj> myOrders;
    ArrayList<Worker> activeWorkers;

    public ClientThread(Socket clientSocket, HashMap<String,User> users,
                        ArrayList<Worker> workers, ArrayList<Restaurant> myRsts,
                        ArrayList<Worker> activeWorkers ,ArrayList<OrderObj> myOrders) {
        this.clientSocket = clientSocket;
        inputStream = null;
        outputStream = null;
        this.users = users;
        this.workers = workers;
        this.myRsts = myRsts;
        this.myOrders = myOrders;
        this.activeWorkers = activeWorkers;
    }

    @Override
    public void run() {
        try {
            System.out.println("New Thread Opened");
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            int action = inputStream.read();
            switch (action){
                case LOG_IN:
                    login();
                    break;
                case CHECK_FOR_ORDERS:
                    System.out.println("proceeding to check for orders");
                    break;
                case GET_WORKERS_LIST:
                    System.out.println("proceeding to get workers list");
                    getWorkersList();
                    break;
                case ADD_TO_WORKERS_LIST:
                    System.out.println("proceeding to add a worker");
                    addToWorkersList();
                    break;
                case DELETE_FROM_WORKERS_LIST:
                    System.out.println("proceeding to delete a worker");
                    deleteFromWorkerList();
                    break;
                case GET_RESTAURANT_LIST:
                    System.out.println("proceeding to get restaurants list");
                    getRestaurantList();
                    break;
                case ADD_TO_RESTAURANT_LIST:
                    System.out.println("proceeding to add a restaurant");
                    addToRestaurantList();
                    break;
                case DELETE_FROM_RESTAURANT_LIST:
                    System.out.println("proceeding to delete a restaurant");
                    deleteFromRestaurantList();
                    break;

            }
        } catch (IOException e) {
            System.out.println("we have a problem: ");
            System.out.println(e.getMessage());
        }  finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }



    private void deleteFromWorkerList() throws IOException {
        User user = getUser();
        outputStream.write(validUser(user) ? SUCCESS : FAILURE);
        int userNameLength = inputStream.read();
        if(userNameLength == -1)
            throw new IOException("didn't receive length of userName");
        byte[] userNameBytes = new byte[userNameLength];
        int actuallyRead;
        actuallyRead = inputStream.read(userNameBytes);
        if(actuallyRead != userNameLength)
            throw new IOException("userNameLength is  not equal to actually read");
        String userName = new String(userNameBytes);
        System.out.println("user name to remove: " + userName);
        int position = inputStream.read();
        if(workers.get(position).getUserName().equals(userName)) {
            System.out.println(userName + " was removed from workers list");
            workers.remove(position);
            outputStream.write(SUCCESS);
        }else {
            outputStream.write(FAILURE);
        }
        if (removeFromUsers(userName)) {
            System.out.println(userName + " was removed from users list");
            outputStream.write(SUCCESS);
        }else {
            outputStream.write(FAILURE);
        }

    }
    private void addToWorkersList() throws IOException {
        User user = getUser();
        outputStream.write(validUser(user) ? SUCCESS : FAILURE);
        byte[] workerLength = new byte[4];
        inputStream.read(workerLength);
        int length = ByteBuffer.wrap(workerLength, 0, 4).getInt();
        System.out.println("workerByteLength: "+ length);
        if (length == -1)
            throw new IOException("length invalid");
        byte[] workerBytes = new byte[length];
        int actuallyRead = inputStream.read(workerBytes);
        if (actuallyRead != length)
            throw new IOException("byteWorkersList is broken");
        Worker newWorker = new Worker(workerBytes);
        System.out.println("worker to add: "+newWorker);
        if(!validUser(newWorker)) {
            workers.add(newWorker);
            addToUsers(newWorker);
            outputStream.write(SUCCESS);
        }
        else
            outputStream.write(FAILURE);
    }
    private void deleteFromRestaurantList() throws IOException {
        User user = getUser();
        outputStream.write(validUser(user) ? SUCCESS : FAILURE);
        int userNameLength = inputStream.read();
        if(userNameLength == -1)
            throw new IOException("didn't receive length of userName");
        byte[] userNameBytes = new byte[userNameLength];
        int actuallyRead;
        actuallyRead = inputStream.read(userNameBytes);
        if(actuallyRead != userNameLength)
            throw new IOException("userNameLength is  not equal to actually read");
        String userName = new String(userNameBytes);
        System.out.println("user name to remove: " + userName);
        int position = inputStream.read();
        if(myRsts.get(position).getUserName().equals(userName)) {
            System.out.println(userName + " was removed from restaurant list");
            myRsts.remove(position);
            outputStream.write(SUCCESS);
        }else {
            outputStream.write(FAILURE);
        }
        if (removeFromUsers(userName)) {
            System.out.println(userName + " was removed from users list");
            outputStream.write(SUCCESS);
        }else {
            outputStream.write(FAILURE);
        }

    }
    private void addToRestaurantList() throws IOException {
        User user = getUser();
        outputStream.write(validUser(user) ? SUCCESS : FAILURE);
        byte[] restaurantLength = new byte[4];
        inputStream.read(restaurantLength);
        int length = ByteBuffer.wrap(restaurantLength, 0, 4).getInt();
        if (length == -1)
            throw new IOException("length invalid");
        byte[] restaurantBytes = new byte[length];
        int actuallyRead = inputStream.read(restaurantBytes);
        if (actuallyRead != length)
            throw new IOException("byteWorkersList is broken");
        Restaurant newRestaurant = new Restaurant(restaurantBytes);
        System.out.println("Restaurant to add: "+newRestaurant);
        if(!validUser(newRestaurant)) {
            myRsts.add(newRestaurant);
            addToUsers(newRestaurant);
            outputStream.write(SUCCESS);
        }
        else
            outputStream.write(FAILURE);
    }

    public void addToUsers(User userToAdd){
        users.put(userToAdd.getUserName(),userToAdd);
    }
    public boolean removeFromUsers(String key){
        return users.remove(key)!=null;
    }


    void login() throws IOException{
        User user = getUser();
        if(validUser(user)) {
            int permission = users.get(user.getUserName()).getUserPermissions();
            outputStream.write(SUCCESS);
            outputStream.write((byte)permission);
            outputStream.write(SUCCESS);
            System.out.println(user.getUserName() + " logged in successfully, permission is: " + permission);
        }
        else {
            outputStream.write(FAILURE);
            System.out.println("User login failed!");
        }
    }

    boolean validUser(User user){
        User existingUser  = users.get(user.getUserName());
        return existingUser != null && existingUser.validPassword(user.getPassword());
    }

    User getUser() throws IOException{
        //get User Name:
        int userNameLength = inputStream.read();
        if(userNameLength == -1)
            throw new IOException("didn't receive length of userName");
        byte[] userNameBytes = new byte[userNameLength];
        int actuallyRead;
        actuallyRead = inputStream.read(userNameBytes);
        if(actuallyRead != userNameLength)
            throw new IOException("userNameLength is  not equal to actually read");
        String userName = new String(userNameBytes);

        //get password:
        int passwordLength = inputStream.read();
        if(passwordLength == -1)
            throw new IOException("didn't receive length of userName");
        byte[] passwordBytes = new byte[passwordLength];
        actuallyRead = inputStream.read(passwordBytes);
        if(actuallyRead != passwordLength)
            throw new IOException("passwordLength is  not equal to actually read");
        String password = new String(passwordBytes);

        return new User(userName,password, 0);
    }


    void getWorkersList() throws IOException {
        User user = getUser();
        outputStream.write(validUser(user) ? SUCCESS : FAILURE);
        byte[] bytes = Main.getBytesOfWorkersArrayList(workers);
        byte[] byteLength = new byte[4];
        ByteBuffer.wrap(byteLength, 0, 4).putInt(bytes.length);
        outputStream.write(byteLength);
        outputStream.write(bytes);
        outputStream.write(SUCCESS);
    }
    void getRestaurantList() throws IOException {
        User user = getUser();
        outputStream.write(validUser(user) ? SUCCESS : FAILURE);
        byte[] bytes = Main.getBytesOfRstArrayList(myRsts);
        byte[] byteLength = new byte[4];
        ByteBuffer.wrap(byteLength, 0, 4).putInt(bytes.length);
        outputStream.write(byteLength);
        outputStream.write(bytes);
        outputStream.write(SUCCESS);
    }
}
