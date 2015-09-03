package com.example.gil.mymanagerapp.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gil.mymanagerapp.Adapters.AdvancedArrayAdapter;
import com.example.gil.mymanagerapp.DialogFragments.CreateOrderDialogFragment;
import com.example.gil.mymanagerapp.DialogFragments.ActiveWorkersOptionFragment;
import com.example.gil.mymanagerapp.Adapters.HintAdapter;
import com.example.gil.mymanagerapp.ObjectClasses.OrderObj;
import com.example.gil.mymanagerapp.ObjectClasses.Restaurant;
import com.example.gil.mymanagerapp.ObjectClasses.Worker;
import com.example.gil.mymanagerapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ManagerActivity extends ActionBarActivity implements CreateOrderDialogFragment.CreateOrderListener {

    public static final int WORKER_LIST_REQUEST_CODE = 5;
    HintAdapter hintAdapter;
    AdvancedArrayAdapter advancedArrayAdapter;
    TableLayout tableLayout;
    ArrayList<OrderObj> myOrders;
    ArrayList<Worker> activeWorkers;
    ArrayList<Restaurant> myRsts;
    ListView lstWorker,lstOrders;
    TextView txtDate,txtTemp;
    String userName,password;
    int permission;
    Intent intent;
    static boolean firstTime = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        intent = getIntent();
        userName = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        permission = intent.getIntExtra("permission", 2);
        setResult(RESULT_OK,intent);
        myRsts = new ArrayList<Restaurant>();
        activeWorkers = new ArrayList<Worker>();
        activeWorkers.add(new Worker("Select","Messenger",""));


        new ReadWeatherJSONFeedTask().execute("http://api.openweathermap.org/data/2.5/weather?q=Modiin,il");
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtTemp = (TextView)findViewById(R.id.txtTemperture);
        txtDate.setText(whatDateIsIt()+",");
        myOrders = new ArrayList<OrderObj>();
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        advancedArrayAdapter = new AdvancedArrayAdapter(this,
                myOrders, activeWorkers, R.layout.order_item, R.id.txtOrderId, R.id.txtPickUpTime,
                R.id.txtEnterTime, R.id.txtBusiness, R.id.txtAdress, R.id.spnMessenger);
        lstOrders = (ListView) findViewById(R.id.lstOrders);
        lstOrders.setAdapter(advancedArrayAdapter);
        advancedArrayAdapter.notifyDataSetChanged();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_manager, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionSettings() {
        Toast.makeText(this, "button not working yet", Toast.LENGTH_SHORT).show();
    }

    private void logOut() {
        setResult(RESULT_CANCELED,intent);
        intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    public void btnCreateOrder(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        CreateOrderDialogFragment createOrderDialog = new CreateOrderDialogFragment();
        createOrderDialog.setFragment((CreateOrderDialogFragment.CreateOrderListener) this, myOrders, myRsts);
        createOrderDialog.show(fragmentManager, "create order");
    }

    public void btnManageRestaurants(View view) {
        Intent intent = new Intent(this,RstManageActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("password",password);
        intent.putExtra("permission", permission);
        startActivity(intent);
    }

    public void btnWorkersList(View view) {
        Intent intent = new Intent(this,WorkersManageActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("password",password);
        intent.putExtra("permission", permission);
        startActivityForResult(intent, WORKER_LIST_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WORKER_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            int lastPositionInArray = activeWorkers.size()-1;
            if(activeWorkers.get(lastPositionInArray).getFirstName().equals("Select"))
                activeWorkers.remove(lastPositionInArray);
            ArrayList<Worker> tempActiveWorkers = (ArrayList<Worker>) data.getSerializableExtra("workers");
            for (int i = 0; i < tempActiveWorkers.size(); i++)
                activeWorkers.add(tempActiveWorkers.get(i));
            activeWorkers.add(new Worker("Select", "Messenger",""));
            hintAdapter = new HintAdapter(this,
                    android.R.layout.simple_list_item_1, activeWorkers);
            lstWorker = (ListView) findViewById(R.id.lstActiveWorkers);
            lstWorker.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lstWorker.setAdapter(hintAdapter);
            advancedArrayAdapter.notifyDataSetChanged();
            lstWorker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Gil", "position " + position);
                    FragmentManager fragmentManager = getFragmentManager();
                    ActiveWorkersOptionFragment activeWorkersOptionFragment = new ActiveWorkersOptionFragment();
                    activeWorkersOptionFragment.setFragment(hintAdapter, activeWorkers, position);
                    activeWorkersOptionFragment.show(fragmentManager, "worker option");
                }
            });

            advancedArrayAdapter = new AdvancedArrayAdapter(this,
                    myOrders, activeWorkers, R.layout.order_item, R.id.txtOrderId, R.id.txtPickUpTime,
                    R.id.txtEnterTime, R.id.txtBusiness, R.id.txtAdress, R.id.spnMessenger);
            lstOrders.setAdapter(advancedArrayAdapter);
        }
    }

    @Override
    public void onFinishCreateOrder(int id) {
        Toast.makeText(getBaseContext(), "Order Created", Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), myOrders.get(id).toString(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void invalidCreate() {
        Toast.makeText(getBaseContext(),"Please fill all Details",Toast.LENGTH_SHORT).show();
    }
    public static String whatTimeIsIt (Long time){
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //calendar.setTimeZone(TimeZone.getTimeZone("Israel"));
        return dateFormat.format(date);
    }
    public static String whatDateIsIt(){
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("dd MMMM");
        String dateAsString = formatter.format(date);
        return dateAsString;
    }
    private class ReadWeatherJSONFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return readJSONFeed(params[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String city = jsonObject.getString("name");
                JSONObject main = new JSONObject(jsonObject.getString("main"));
                String temp = main.getString("temp");
                float temperature = Float.valueOf(temp);
                txtTemp.setText((short)(temperature-273.15) + " C,    " + city);
            } catch (Exception ex) {
                Log.d("Gil","error" + ex.getMessage());
            }
        }
    }

    public String readJSONFeed(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = response.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("Gil", "Status code is not OK");
            }
        } catch (Exception ex) {
            Log.d("Gil", "error" + ex.getMessage());
        }

        return stringBuilder.toString();
    }

}
