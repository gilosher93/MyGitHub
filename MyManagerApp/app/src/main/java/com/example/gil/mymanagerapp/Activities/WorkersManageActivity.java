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

import com.example.gil.mymanagerapp.DialogFragments.CreateWorkerDialogFragment;
import com.example.gil.mymanagerapp.DialogFragments.WorkerOptionFragment;
import com.example.gil.mymanagerapp.ObjectClasses.Worker;
import com.example.gil.mymanagerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class WorkersManageActivity extends ActionBarActivity{
    String userName, password;
    int permission;
    Intent intent;
    ArrayAdapter<Worker> adapter;
    ArrayList<Worker> workers;
    ListView workersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_manage);


        intent = getIntent();
        userName = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        permission = intent.getIntExtra("permission", 0);
        workers = new ArrayList<Worker>();
        adapter = new ArrayAdapter<Worker>(this, android.R.layout.simple_list_item_multiple_choice, workers);
        workersList = (ListView) findViewById(R.id.lstWorkeresList);
        workersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        workersList.setAdapter(adapter);
        refresh();
        workersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        workersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                WorkerOptionFragment workerOptionFragment = new WorkerOptionFragment();
                workerOptionFragment.setFragment(new WorkerOptionFragment.WorkerOptionListener() {
                    @Override
                    public void onFinishDelete() {
                        refresh();
                    }
                }, userName, password,position,workers);
                workerOptionFragment.show(fragmentManager, "worker option");
                return true;
            }
        });
    }
    public void refresh(){
        new AsyncTask<String, Void, Boolean>() {
            byte[] byteWorkersList;

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
                    outputStream.write(HomeActivity.GET_WORKERS_LIST);
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
                        byteWorkersList = new byte[length];
                        int actuallyRead = inputStream.read(byteWorkersList);
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
                    workers = Worker.getArrayOfWorkerList(byteWorkersList);
                    adapter = new ArrayAdapter<Worker>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, workers);
                    workersList = (ListView) findViewById(R.id.lstWorkeresList);
                    workersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    workersList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "There Was a Problem", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(userName, password);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workers_list, menu);
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

    public void btnAddNewWorker(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        CreateWorkerDialogFragment createWorkerDialogFragment = new CreateWorkerDialogFragment();
        createWorkerDialogFragment.setFragment(new CreateWorkerDialogFragment.CreateWorkerListener() {
            @Override
            public void onFinishCreateWorker() {
                refresh();
            }

            @Override
            public void invalidCreate() {
                Toast.makeText(getBaseContext(), "Please fill all Requierd Fields", Toast.LENGTH_LONG).show();
            }
        }, userName, password);
        createWorkerDialogFragment.show(fragmentManager, "create order");
    }
}