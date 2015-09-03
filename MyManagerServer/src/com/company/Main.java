package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Main {


    public static final int NUMBER_OF_CLIENTS = 1000;
    public static int counter=0;
    public static final int PORT = 3000;
    private static boolean go = true;

    public static void main(String[] args) {
        HashMap<String,User> users = new HashMap<String,User>();
        ArrayList<Worker> workers = new ArrayList<Worker>();
        ArrayList<Restaurant> myRsts = new ArrayList<Restaurant>();
        ArrayList<OrderObj> myOrders = new ArrayList<OrderObj>();
        ArrayList<Worker> activeWorkers = new ArrayList<Worker>();
        Restaurant domino = new Restaurant("vico","123","domino");
        myRsts.add(domino);
        User dan = new User("dan","12345", 1);
        users.put(dan.getUserName(), dan);
        Worker gil = new Worker("gil", "123", "Gil", "Osher", "0527800169");
        workers.add(gil);
        users.put(gil.getUserName(), gil);
        workers.add(new Worker("Guy", "Azran", "123"));
        workers.add(new Worker("Ariel", "Zadi", "123"));
        workers.add(new Worker("Leon", "Beni", "123"));
        workers.add(new Worker("Elad", "Lavi", "123"));
        workers.add(new Worker("Omer", "Cohen", "123"));
        workers.add(new Worker("Lev", "Levi", "123"));
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (go){
                System.out.println("waiting for incoming client communication...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected to Server");
                if(counter< NUMBER_OF_CLIENTS) {
                    ClientThread clientThread = new ClientThread(clientSocket, users, workers, myRsts, activeWorkers, myOrders);
                    clientThread.start();
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Main.counter--;
    }

    public static byte[] getBytesOfWorkersArrayList(ArrayList<Worker> workers){
        int bLength,counter = 0;
        for (int i = 0; i < workers.size(); i++) {
            counter += workers.get(i).getWorkerBytes().length;
            counter += 4;
        }
        byte[] bytes = new byte[counter];
        counter = 0;
        for (int i = 0; i < workers.size(); i++) {
            bLength = workers.get(i).getWorkerBytes().length;
            ByteBuffer.wrap(bytes, counter, 4).putInt(bLength);
            counter +=4;
            ByteBuffer.wrap(bytes,counter,bLength).put(workers.get(i).getWorkerBytes());
            counter += bLength;
        }
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
    public static byte[] getBytesOfRstArrayList(ArrayList<Restaurant> restaurants){
        int bLength,counter = 0;
        for (int i = 0; i < restaurants.size(); i++) {
            counter += restaurants.get(i).getRestaurantBytes().length;
            counter += 4;
        }
        byte[] bytes = new byte[counter];
        counter = 0;
        for (int i = 0; i < restaurants.size(); i++) {
            bLength = restaurants.get(i).getRestaurantBytes().length;
            ByteBuffer.wrap(bytes, counter, 4).putInt(bLength);
            counter +=4;
            ByteBuffer.wrap(bytes,counter,bLength).put(restaurants.get(i).getRestaurantBytes());
            counter += bLength;
        }
        return bytes;
    }
    public static ArrayList<Restaurant> getArrayOfRstList(byte[] bytes){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        int bLength,position = 0;

        while (position<bytes.length) {
            bLength = ByteBuffer.wrap(bytes, position, 4).getInt();
            position+=4;
            byte[] byteO = new byte[bLength];
            for (int j = 0; j < bLength; j++) {
                byteO[j] = bytes[position+j];
            }
            restaurants.add(new Restaurant(byteO));
            position +=bLength;
        }

        return restaurants;
    }


    public static void stop(){
        go = false;
    }
}
