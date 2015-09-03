package com.example.gil.mymanagerapp.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gil.mymanagerapp.DialogFragments.CreateRstDialogFragment;
import com.example.gil.mymanagerapp.DialogFragments.RestaurantOptionFragment;
import com.example.gil.mymanagerapp.DialogFragments.WorkerOptionFragment;
import com.example.gil.mymanagerapp.ObjectClasses.Restaurant;
import com.example.gil.mymanagerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class RstManageActivity extends ActionBarActivity{
    ArrayList<Restaurant> myRsts;
    ArrayAdapter<Restaurant> adapter;
    ListView rstList;
    String userName,password;
    int permission;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rst_manage);

        intent = getIntent();
        userName = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        permission = intent.getIntExtra("permission", 2);
        myRsts = new ArrayList<Restaurant>();
        rstList = (ListView) findViewById(R.id.lstRstList);
        ArrayAdapter<Restaurant> adapter = new ArrayAdapter<Restaurant>(this,
                android.R.layout.simple_list_item_1, myRsts);
        rstList.setAdapter(adapter);
        refresh();
        rstList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                RestaurantOptionFragment restaurantOptionFragment = new RestaurantOptionFragment();
                restaurantOptionFragment.setFragment(new RestaurantOptionFragment.RestaurantOptionListener() {
                    @Override
                    public void onFinishDelete() {
                        refresh();
                    }
                }, userName, password, position, myRsts);
                restaurantOptionFragment.show(fragmentManager, "worker option");
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rst_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                actionSettings();
                return true;
            case R.id.add_restaurant:
                add_restaurant();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionSettings() {
        Toast.makeText(this, "button not working yet", Toast.LENGTH_SHORT).show();
    }

    private void add_restaurant() {
        FragmentManager fragmentManager = getFragmentManager();
        CreateRstDialogFragment createRstDialogFragment = new CreateRstDialogFragment();
        createRstDialogFragment.setFragment(new CreateRstDialogFragment.CreateRstListener() {
            @Override
            public void onFinishCreateRst() {
                refresh();
                Toast.makeText(getBaseContext(),"Restaurent Added Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void invalidCreate() {
                Toast.makeText(getBaseContext(),"Please fill all Details",Toast.LENGTH_SHORT).show();
            }
        },userName,password);
        createRstDialogFragment.show(fragmentManager, "add Resturant");
    }

    public void refresh(){
        new AsyncTask<String, Void, Boolean>() {
            byte[] byteRstList;

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
                    outputStream.write(HomeActivity.GET_RESTAURANT_LIST);
                    byte[] userNameBytes = userName.getBytes();
                    outputStream.write(userNameBytes.length);
                    outputStream.write(userNameBytes);
                    byte[] userPasswordBytes = password.getBytes();
                    outputStream.write(userPasswordBytes.length);
                    outputStream.write(userPasswordBytes);
                    int response = inputStream.read();
                    userValid = response == HomeActivity.SUCCESS;
                    if (userValid) {
                        byte[] bytesLength = new byte[4];
                        inputStream.read(bytesLength);
                        int length = ByteBuffer.wrap(bytesLength, 0, 4).getInt();
                        if (length == -1)
                            throw new IOException("length invalid");
                        byteRstList = new byte[length];
                        int actuallyRead = inputStream.read(byteRstList);
                        if (actuallyRead != length)
                            throw new IOException("byteWorkersList is broken");
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
                if (valid) {
                    myRsts = Restaurant.getArrayOfRstList(byteRstList);
                    adapter = new ArrayAdapter<Restaurant>(getBaseContext(),android.R.layout.simple_list_item_1, myRsts);
                    rstList = (ListView) findViewById(R.id.lstRstList);
                    rstList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "There Was a Problem", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(userName, password);
    }
}
