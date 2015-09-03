package com.example.gil.exsavingdogonjsonformattofile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {

    EditText txtName,txtYear;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (EditText) findViewById(R.id.txtName);
        txtYear = (EditText) findViewById(R.id.txtYear);
        btnSave = (Button) findViewById(R.id.btnSave);

        try {
            FileInputStream fileInputStream =openFileInput("dogJson.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] jsonChars = new char[1024];
            int actuallyRead = inputStreamReader.read(jsonChars);
            String dogStringJson = new String(jsonChars, 0, actuallyRead);
            JSONObject jsonObject = new JSONObject(dogStringJson);
            txtName.setText(jsonObject.get("name")+"");
            txtYear.setText(jsonObject.getInt("year")+"");
        } catch (FileNotFoundException e) {
            Log.d("Gil","file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void btnSave(View view) {
        String name = txtName.getText().toString();
        int year = Integer.valueOf(txtYear.getText().toString());

        Dog dog = new Dog(name,year);
        JSONObject jsonDog = new JSONObject();
        try {
            jsonDog.put("name",dog.getName());
            jsonDog.put("year",dog.getYear());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String stringDogAsJson = jsonDog.toString();
        try {
            FileOutputStream fileOutputStream = openFileOutput("dogJson.txt", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(stringDogAsJson);
            outputStreamWriter.close();
            Toast.makeText(MainActivity.this, "File saved successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("Gil", "error saving file: " + e.getMessage());
        } catch (IOException ex) {
            Log.d("Gil", "error writing to file: " + ex.getMessage());
        }
        txtName.setText("");
        txtYear.setText("");
    }

    class Dog{
        private String name;
        private int year;

        public Dog(String name, int year) {
            this.name = name;
            this.year = year;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
