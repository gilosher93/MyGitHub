package com.example.gil.mymanagerapp.DialogFragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.gil.mymanagerapp.Activities.HomeActivity;
import com.example.gil.mymanagerapp.ObjectClasses.Worker;
import com.example.gil.mymanagerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Gil on 16/08/2015.
 */
public class WorkerOptionFragment extends DialogFragment {
    WorkerOptionListener listener;
    String userName,password;
    ArrayList<Worker> workers;
    int position;
    Button btnDeleteWorker,btnInfo,btnShowWorkersOrderArray;

    public void setFragment (WorkerOptionListener listener, String userName, String password,int position,ArrayList<Worker> workers){
        this.userName = userName;
        this.password = password;
        this.listener = listener;
        this.position = position;
        this.workers = workers;
    }

    public static interface WorkerOptionListener{
        void onFinishDelete();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_options,container);
        btnDeleteWorker = (Button)view.findViewById(R.id.btnDeleteWorker);
        btnInfo= (Button) view.findViewById(R.id.btnInfo);
        btnShowWorkersOrderArray= (Button) view.findViewById(R.id.btnShowOrders);
        btnDeleteWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                YesOrNoDialogFragment yesOrNoDialogFragment = new YesOrNoDialogFragment();
                yesOrNoDialogFragment.setFragment(new YesOrNoDialogFragment.YesOrNoDialogListener() {
                    @Override
                    public void answer(boolean answer) {
                        if (answer) {
                            new AsyncTask<String, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(String... params) {
                                    String userName = params[0];
                                    String password = params[1];
                                    boolean userValid = false, valid = false;
                                    Socket clientSocket = null;
                                    try {
                                        clientSocket = new Socket(HomeActivity.SERVER_IP, HomeActivity.PORT);
                                        clientSocket.setSoTimeout(500);
                                        InputStream inputStream = clientSocket.getInputStream();
                                        OutputStream outputStream = clientSocket.getOutputStream();
                                        outputStream.write(HomeActivity.DELETE_FROM_WORKERS_LIST);
                                        byte[] userNameBytes = userName.getBytes();
                                        outputStream.write(userNameBytes.length);
                                        outputStream.write(userNameBytes);
                                        byte[] userPasswordBytes = password.getBytes();
                                        outputStream.write(userPasswordBytes.length);
                                        outputStream.write(userPasswordBytes);
                                        int response = inputStream.read();
                                        userValid = response == HomeActivity.SUCCESS;

                                        if (userValid) {
                                            byte[] userNameToDelete = workers.get(position).getUserName().getBytes();
                                            outputStream.write(userNameToDelete.length);
                                            outputStream.write(userNameToDelete);
                                            outputStream.write(position);
                                            int workersListResponse = inputStream.read();
                                            int usersListResponse = inputStream.read();
                                            valid = workersListResponse == HomeActivity.SUCCESS && usersListResponse == HomeActivity.SUCCESS;
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (clientSocket != null) {
                                            try {
                                                clientSocket.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    return valid;
                                }

                                @Override
                                protected void onPostExecute(Boolean valid) {
                                    if (valid) {
                                        listener.onFinishDelete();
                                        dismiss();
                                    }
                                }
                            }.execute(userName, password);
                        }
                    }
                }, userName, password, "Are you sure you want to delete this worker?");
                yesOrNoDialogFragment.show(fragmentManager, "worker option");
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                ShowDetailsDialogFragment showDetailsDialogFragment = new ShowDetailsDialogFragment();
                showDetailsDialogFragment.setFragment(workers, position);
                showDetailsDialogFragment.show(fragmentManager, "show details");
                dismiss();
            }
        });
        return view;
    }
}
