package com.missmetal.dbsync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import broadcast.SampleBC;
import cz.msebera.android.httpclient.Header;
import db.DBController;
import utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class MainActivity extends AppCompatActivity {


    // DB Class to perform DB related operations
    DBController controller = new DBController(this, "lean",null,1);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get User records from SQLite DB
        ArrayList<HashMap<String, String>> userList = controller.getAllRows();
        // If users exists in SQLite DB
        if (userList.size() != 0) {
            // Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, userList, R.layout.list_view, new String[] {
                    "ID", "Estado" }, new int[] { R.id.ID, R.id.estado });
            ListView myList = (ListView) findViewById(android.R.id.list);
            myList.setAdapter(adapter);
        }
        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Sincronizando...");
        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // When Options Menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        // When Sync action button is clicked
        if (id == R.id.refresh) {
            // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
            syncSQLiteMySQLDB();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Llama a getdata.php
        client.post(Constants.MAIN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getdata.php
                try {
                    updateSQLite(String.valueOf(responseBody));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    public void updateSQLite(String response) throws JSONException {
        ArrayList<HashMap<String, String>> usersynclist = new ArrayList<>(); //lista de la respuesta

        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extrae el array de json en el response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("ID"));
                    System.out.println(obj.get("Estado"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("ID", obj.get("ID").toString());
                    // Add userName extracted from Object
                    queryValues.put("Estado", obj.get("Estado").toString());
                    // Insert User into SQLite DB
                    controller.insertRow(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("ID", obj.get("ID").toString());
                    map.put("Estado", "");
                    usersynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ID", json);
        // Llama al server para updatear los datos de las antenas
        /*client.post("ftp://ftp.xysistemas.com.ar/public_html/testsrv/sqlsync/updatesyncsts.php", params, new AsyncHttpResponseHandler() { //todo : falta armar los metodos de update en php
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }
}
