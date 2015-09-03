 package com.example.gil.mymanagerapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

 public class HomeActivity extends AppCompatActivity {
     public static final int PORT = 3000;
     public static final String SERVER_IP = "10.0.25.27";
     public static final int LOG_IN = 10;
     public static final int FAILURE = 99;
     public static final int SUCCESS = 100;
     public static final int REQUEST_CODE = 4;
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

         //remove!!
         //Intent intent = new Intent(this, ManagerActivity.class);
         //startActivity(intent);
         txtUserName = (EditText) findViewById(R.id.txtUserName);
         txtPassword = (EditText) findViewById(R.id.txtPassword);
         txtStatus = (TextView) findViewById(R.id.txtStatus);
         btnLogin = (Button) findViewById(R.id.btnLogin);
         chkRememberPassword = (CheckBox) findViewById(R.id.chkRememberPassword);
         location = new Location(LocationManager.GPS_PROVIDER);
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
             @Override
             protected Boolean doInBackground(String... params) {
                 String userName = params[0];
                 String password = params[1];
                 boolean valid = false;
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
                     int response = inputStream.read();
                     valid = response == SUCCESS;
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

                     txtStatus.setText("Connected");
                     txtStatus.setTextColor(Color.GREEN);
                     Intent intent = new Intent(getBaseContext(), ManagerActivity.class);
                     intent.putExtra("userName", userName);
                     intent.putExtra("password", password);
                     startActivityForResult(intent,REQUEST_CODE);
                 } else {
                     txtStatus.setText("user or password are wrong");
                     txtStatus.setTextColor(Color.RED);
                 }
             }
         }.execute(userName, password);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
             finish();
         }
     }
 }
