package tw.org.tcca.app.netstatustest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView img;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHandler = new UIHandler();
        mesg = findViewById(R.id.mesg);

        img = findViewById(R.id.img);



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

    public void get2(View view) {
        new Thread(){
            @Override
            public void run() {
                doGet2();
            }
        }.start();
    }


    private void doGet2(){
        try {
            URL url = new URL("https://s.yimg.com/ny/api/res/1.2/l5UKxUk8nG8cecb6tiIQxw--~A/YXBwaWQ9aGlnaGxhbmRlcjtzbT0xO3c9ODAw/https://media-mbst-pub-ue1.s3.amazonaws.com/creatr-uploaded-images/2019-07/91bd6220-9e54-11e9-bf6e-b3df8120db62");
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.connect();

            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            uiHandler.sendEmptyMessage(1);


        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                mesg.setText(sb);
            }else if (msg.what == 1) {
                img.setImageBitmap(bitmap);
            }

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
