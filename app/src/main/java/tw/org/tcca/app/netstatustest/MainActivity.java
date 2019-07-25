package tw.org.tcca.app.netstatustest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager cmgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cmgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

    }
}
