package com.example.gil.mymanagerapp.DialogFragments;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.gil.mymanagerapp.Activities.HomeActivity;
import com.example.gil.mymanagerapp.ObjectClasses.Worker;
import com.example.gil.mymanagerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Gil on 31/07/2015.
 */
public class CreateWorkerDialogFragment extends DialogFragment {
    String userName,password;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtPhone;
    Button btnAddWorker;
    byte[] workerBytes;
    CreateWorkerListener listener;

    public void setFragment (CreateWorkerListener listener, String userName, String password){
        this.listener = listener;
        this.userName = userName;
        this.password = password;
    }

    public static interface CreateWorkerListener {
        void onFinishCreateWorker();
        void invalidCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_worker,container);
        btnAddWorker = (Button)view.findViewById(R.id.btnAddWorker);
        txtFirstName = (EditText)view.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) view.findViewById(R.id.txtLastName);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String phone= txtPhone.getText().toString();
                if(firstName.length() == 0 || lastName.length() == 0 ||  phone.length()==0){
                    listener.invalidCreate();
                    return;
                }
                Worker newWorker = new Worker(firstName,phone,firstName,lastName,phone);
                workerBytes = newWorker.getWorkerBytes();
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
                            outputStream.write(HomeActivity.ADD_TO_WORKERS_LIST);
                            byte[] userNameBytes = userName.getBytes();
                            outputStream.write(userNameBytes.length);
                            outputStream.write(userNameBytes);
                            byte[] userPasswordBytes = password.getBytes();
                            outputStream.write(userPasswordBytes.length);
                            outputStream.write(userPasswordBytes);
                            int response = inputStream.read();
                            userValid = response == HomeActivity.SUCCESS;
                            if (userValid && workerBytes!=null) {
                                byte[] byteLength = new byte[4];
                                ByteBuffer.wrap(byteLength, 0, 4).putInt(workerBytes.length);
                                outputStream.write(byteLength);
                                outputStream.write(workerBytes);
                                response = inputStream.read();
                                valid = response == HomeActivity.SUCCESS;
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
                        listener.onFinishCreateWorker();
                        dismiss();
                    }
                }.execute(userName, password);

            }
        });
        txtFirstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}
