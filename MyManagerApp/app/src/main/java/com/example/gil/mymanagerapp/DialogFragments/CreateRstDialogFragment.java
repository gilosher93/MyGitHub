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
import com.example.gil.mymanagerapp.ObjectClasses.Restaurant;
import com.example.gil.mymanagerapp.ObjectClasses.Worker;
import com.example.gil.mymanagerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Gil on 2/08/2015.
 */
public class CreateRstDialogFragment extends DialogFragment {
    String userName, password;
    EditText txtRstName;
    EditText txtUserName;
    EditText txtPassword;
    Button btnCreateRst;
    CreateRstListener listener;
    byte[] restaurantBytes;

    public void setFragment (CreateRstListener listener, String userName, String password){
        this.listener = listener;
        this.userName = userName;
        this.password = password;
    }

    public static interface CreateRstListener {
        void onFinishCreateRst();
        void invalidCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_rst,container);
        btnCreateRst = (Button)view.findViewById(R.id.btnCreateRst);
        txtRstName = (EditText)view.findViewById(R.id.txtRestaurantName);
        txtUserName = (EditText)view.findViewById(R.id.txtUserName);
        txtPassword = (EditText)view.findViewById(R.id.txtPassword);
        btnCreateRst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rstName = txtRstName.getText().toString();
                String rstUserName = txtUserName.getText().toString();
                String rstPassword = txtPassword.getText().toString();
                if (rstName.length() == 0  || rstUserName.length() == 0 || rstPassword.length() == 0) {
                    listener.invalidCreate();
                    return;
                }
                Restaurant newRestaurant = new Restaurant(rstUserName,rstPassword,rstName);
                restaurantBytes = newRestaurant.getRestaurantBytes();
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
                            outputStream.write(HomeActivity.ADD_TO_RESTAURANT_LIST);
                            byte[] userNameBytes = userName.getBytes();
                            outputStream.write(userNameBytes.length);
                            outputStream.write(userNameBytes);
                            byte[] userPasswordBytes = password.getBytes();
                            outputStream.write(userPasswordBytes.length);
                            outputStream.write(userPasswordBytes);
                            int response = inputStream.read();
                            userValid = response == HomeActivity.SUCCESS;
                            if (userValid && restaurantBytes!=null) {
                                byte[] byteLength = new byte[4];
                                ByteBuffer.wrap(byteLength, 0, 4).putInt(restaurantBytes.length);
                                outputStream.write(byteLength);
                                outputStream.write(restaurantBytes);
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
                        listener.onFinishCreateRst();
                        dismiss();
                    }
                }.execute(userName, password);
            }
        });
        txtRstName.requestFocus();

        //make the keyboard appear automatically
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }
}
