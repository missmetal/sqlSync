package broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import services.DBService;

/**
 * Created by Miss Metal on 27/11/2016.
 */

public class SampleBC extends BroadcastReceiver {
    static int noOfTimes = 0;

    // Method gets called when Broad Case is issued from MainActivity for every 10 seconds
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        noOfTimes++;
        Toast.makeText(context, "Servicio iniciado " + noOfTimes + " veces", Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
        RequestHandle post = client.post("ftp://ftp.xysistemas.com.ar/public_html/testsrv/sqlsync/getdbrowcount.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println(responseBody);
                try {
                    // Create JSON object out of the response sent by getdbrowcount.php
                    JSONObject obj = new JSONObject(String.valueOf(responseBody));
                    System.out.println(obj.get("count"));
                    // If the count value is not zero, call MyService to display notification
                    if (obj.getInt("count") != 0) {
                        final Intent intnt = new Intent(context, DBService.class);
                        // Set unsynced count in intent data
                        intnt.putExtra("intntdata", "Unsynced Rows Count " + obj.getInt("count"));
                        // Call MyService
                        context.startService(intnt);
                    } else {
                        Toast.makeText(context, "Sync not needed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                if (statusCode == 404) {
                    Toast.makeText(context, "Error 404", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 500) {
                    Toast.makeText(context, "Error 500", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error Inesperado", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}
