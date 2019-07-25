package tw.org.tcca.app.netstatustest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager cmgr;
    private MyReceiver myReceiver;
    private boolean isConnectNetwork;
    private TextView mesg;
    private StringBuffer sb = new StringBuffer();
    private UIHandler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHandler = new UIHandler();
        mesg = findViewById(R.id.mesg);

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
                sb.append(line + "\n");
            }
            reader.close();
            Log.v("brad", "over");

            uiHandler.sendEmptyMessage(0);

            //mesg.setText(sb);

        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            mesg.setText(sb);
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
