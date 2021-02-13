package com.southaficannewsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.southaficannewsapp.R;
import com.southaficannewsapp.util.NetworkConnection;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkConnection connection =new NetworkConnection(getApplicationContext());

        if (!connection.isNetworkAvailable()){
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
