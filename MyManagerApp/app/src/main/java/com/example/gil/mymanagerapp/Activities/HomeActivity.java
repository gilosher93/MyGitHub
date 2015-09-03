package com.example.gil.mymanagerapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gil.mymanagerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HomeActivity extends AppCompatActivity {
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";
    public static final String CHK_REMEMBER = "chk_remember";
    public static final int PORT = 3000;
    public static final String SERVER_IP = "192.168.2.109";
    public static final int LOG_IN = 10;
    public static final int GET_WORKERS_LIST = 30;
    public static final int ADD_TO_WORKERS_LIST = 31;
    public static final int DELETE_FROM_WORKERS_LIST = 32;
    public static final int GET_RESTAURANT_LIST = 40;
    public static final int ADD_TO_RESTAURANT_LIST = 41;
    public static final int DELETE_FROM_RESTAURANT_LIST = 42;
    public static final int FAILURE = 99;
    public static final int SUCCESS = 100;
    public static final int REQUEST_CODE = 4;
    SharedPreferences sharedPreferences;
    public static final String prefName = "MyPref";
    Button btnLogin;
    EditText txtUserName, txtPassword;
    TextView txtStatus;
    String userName,password;
    CheckBox chkRememberPassword;
    private Handler handler = new Handler();
    public static String currentUser;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        chkRememberPassword = (CheckBox) findViewById(R.id.chkRememberPassword);
        location = new Location(LocationManager.GPS_PROVIDER);
        sharedPreferences = getSharedPreferences(prefName, MODE_PRIVATE);
        readSharedPrefrences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void blockUI() {
        txtStatus.setText("please wait...");
        btnLogin.setEnabled(false);
        txtUserName.setEnabled(false);
        txtPassword.setEnabled(false);
        chkRememberPassword.setEnabled(false);
    }

    public void releaseUI() {
        btnLogin.setEnabled(true);
        txtUserName.setEnabled(true);
        txtPassword.setEnabled(true);
        chkRememberPassword.setEnabled(true);

    }

    public void btnLogin(View view) {
        userName = txtUserName.getText().toString();
        password = txtPassword.getText().toString();
        blockUI();

        new AsyncTask<String, Void, Boolean>() {
            int permission;
            @Override
            protected Boolean doInBackground(String... params) {
                String userName = params[0];
                String password = params[1];
                boolean userValid = false,valid = false;

                int response;
                Socket clientSocket = null;
                try {
                    clientSocket = new Socket(SERVER_IP, PORT);
                    clientSocket.setSoTimeout(500);
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(LOG_IN);
                    byte[] userNameBytes = userName.getBytes();
                    outputStream.write(userNameBytes.length);
                    outputStream.write(userNameBytes);
                    byte[] userPasswordBytes = password.getBytes();
                    outputStream.write(userPasswordBytes.length);
                    outputStream.write(userPasswordBytes);
                    response = inputStream.read();
                    userValid = response == SUCCESS;
                    if(userValid) {
                        permission = (byte) inputStream.read();
                        response = inputStream.read();
                        valid = response == SUCCESS;
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
                releaseUI();
                if (valid) {
                    if(chkRememberPassword.isChecked()){
                        writeSharedPrefrences(userName,password,true);
                    }else{
                        writeSharedPrefrences("","",false);
                    }
                    txtStatus.setText("Connected");
                    txtStatus.setTextColor(Color.GREEN);
                    Intent intent = new Intent(getBaseContext(), ManagerActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("password", password);
                    intent.putExtra("permission", permission);
                    startActivityForResult(intent,REQUEST_CODE);
                } else {
                    txtStatus.setText("user or password are wrong");
                    txtStatus.setTextColor(Color.RED);
                }
            }
        }.execute(userName, password);

    }

    private void writeSharedPrefrences(String userName, String password, boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.putString(PASSWORD, password);
        editor.putBoolean(CHK_REMEMBER, checked);
        editor.commit();
    }

    private void readSharedPrefrences() {
        String userName = sharedPreferences.getString(USER_NAME, "");
        txtUserName.setText(userName);
        String password = sharedPreferences.getString(PASSWORD, "");
        txtPassword.setText(password);
        boolean isChk = sharedPreferences.getBoolean(CHK_REMEMBER,false);
        chkRememberPassword.setChecked(isChk);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            finish();
        }
    }
}
