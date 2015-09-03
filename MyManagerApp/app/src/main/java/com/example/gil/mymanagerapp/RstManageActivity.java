package com.example.gil.mymanagerapp;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RstManageActivity extends ActionBarActivity implements CreateRstDialogFragment.CreateRstListener{
    ArrayList<RstObj> myRsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rst_manage);

        myRsts = (ArrayList<RstObj>)getIntent().getSerializableExtra("myRsts");
        ListView rstList = (ListView) findViewById(R.id.lstRstList);
        ArrayAdapter<RstObj> adapter = new ArrayAdapter<RstObj>(this,
                android.R.layout.simple_list_item_1, myRsts);
        rstList.setAdapter(adapter);
        rstList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"Go",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btnAddResturant(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        CreateRstDialogFragment createRstDialogFragment = new CreateRstDialogFragment();
        createRstDialogFragment.setFragment((CreateRstDialogFragment.CreateRstListener) this,myRsts);
        createRstDialogFragment.show(fragmentManager, "add Resturant");
    }


    @Override
    public void onFinishCreateRst(RstObj rstObj) {
        Toast.makeText(getBaseContext(), rstObj + " added Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void invalidCreate() {
        Toast.makeText(getBaseContext(),"Please fill all Details",Toast.LENGTH_SHORT).show();
    }
}
