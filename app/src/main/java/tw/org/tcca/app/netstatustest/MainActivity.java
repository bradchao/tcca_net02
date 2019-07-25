package tw.org.tcca.app.netstatustest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager cmgr;
    private MyReceiver myReceiver;
    private boolean isConnectNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cmgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        Log.v("brad", "network:" + isConnectNetwork());
        Log.v("brad", "wifi:" + isWifi());

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver, filter);

    }


    @Override
    public void finish() {
        unregisterReceiver(myReceiver);

        super.finish();
    }

    private boolean isConnectNetwork(){
        NetworkInfo info = cmgr.getActiveNetworkInfo();
        boolean isConnected = info != null && info.isConnectedOrConnecting();
        return  isConnected;

    }

    private boolean isWifi(){
        NetworkInfo info =cmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return  info.isConnected();
    }

    public void get1(View view) {
        // HTTP or HTTPS
        new Thread(){
            @Override
            public void run() {
                doGet1();
            }
        }.start();
    }

    private void doGet1(){
        try {
            URL url = new URL("http://www.tcca.org.tw");
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.connect();

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ( (line = reader.readLine()) != null){
                Log.v("brad", line);
            }
            reader.close();
            Log.v("brad", "over");

        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("brad", "onReceive");
            isConnectNetwork = isConnectNetwork();
        }
    }



}
