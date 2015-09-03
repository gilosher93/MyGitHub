package com.example.gil.mymanagerapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkersManageActivity extends ActionBarActivity implements CreateWorkerDialogFragment.CreateWorkerListener {
    ArrayAdapter<Worker> adapter;
    private ArrayList<Worker> workers;
    private ArrayList<Worker> activeWorkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_manage);

        workers = new ArrayList<Worker>();
        workers.add(new Worker("Gil","Osher","0527800169"));
        workers.add(new Worker("Guy","Azran"));
        workers.add(new Worker("Ariel","Zadi"));
        workers.add(new Worker("Leon","Beni"));
        workers.add(new Worker("Elad","Lavi"));
        workers.add(new Worker("Omer","Cohen"));
        workers.add(new Worker("Lev","Levi"));
        activeWorkers = new ArrayList<Worker>();
        adapter = new ArrayAdapter<Worker>(this,android.R.layout.simple_list_item_multiple_choice,workers);
        ListView workersList = (ListView) findViewById(R.id.lstWorkeresList);
        workersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        workersList.setAdapter(adapter);
        workersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Gil", "position " + position);
                Toast.makeText(getBaseContext(), workers.get(position).toString(), Toast.LENGTH_SHORT).show();
                addWorkerToShift(workers.get(position));
            }
        });
        workersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                WorkerOptionFragment workerOptionFragment = new WorkerOptionFragment();
                workerOptionFragment.setFragment(adapter, workers, position);
                workerOptionFragment.show(fragmentManager, "worker option");
                return true;
            }
        });
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
        createWorkerDialogFragment.setFragment((CreateWorkerDialogFragment.CreateWorkerListener)this,workers);
        createWorkerDialogFragment.show(fragmentManager, "create order");
    }

    @Override
    public void onFinishCreateWorker(int id) {
        Toast.makeText(this, "Worker Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidCreate() {
        Toast.makeText(this, "Please fill all Requierd Fields", Toast.LENGTH_LONG).show();
    }

    public void addWorkerToShift(Worker workerToAdd){
        if(!activeWorkers.remove(workerToAdd))
            activeWorkers.add(workerToAdd);
        workerToAdd.setOnShift();

    }
    public void SendActiveWorkers(View view) {
        Log.d("Gil", "active workers: " + activeWorkers);
        Intent intent = new Intent();
        intent.putExtra("workers",activeWorkers);
        setResult(RESULT_OK, intent);
        finish();
    }


    public static void btnCallWorker(View view) {
        view.getTag();
    }


}
