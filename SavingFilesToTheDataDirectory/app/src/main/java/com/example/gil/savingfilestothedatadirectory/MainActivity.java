package com.example.gil.savingfilestothedatadirectory;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //writing to file (also creates the file if it doesn't exist.
        try {
            FileOutputStream fileOutputStream = openFileOutput("textfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write("again, we are learning today about writing to files.");
            outputStreamWriter.close();
            fileOutputStream.close();//perhaps it's enough to close the writer
            Toast.makeText(MainActivity.this, "File saved successfully", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Log.d("Gil", "error saving file: " + e.getMessage());
        } catch (IOException ex) {
            Log.d("Gil", "error writing to file: " + ex.getMessage());
        }

        //reading from file
        try {
            FileInputStream fileInputStream = openFileInput("textfile.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] chars = new char[1024];
            int actuallyRead = inputStreamReader.read(chars);
            String s = new String(chars, 0, actuallyRead);
            Toast.makeText(MainActivity.this, "s=" + s, Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
}
