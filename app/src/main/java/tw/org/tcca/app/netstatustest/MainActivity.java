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

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager cmgr;
    private MyReceiver myReceiver;

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

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("brad", "onReceive");
        }
    }



}
