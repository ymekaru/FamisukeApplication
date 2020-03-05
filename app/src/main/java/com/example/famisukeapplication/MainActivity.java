package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Confirm", "Main onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission Check
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("Confirm", "Main onCreate checkSelfPermission");
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1000);
            return;
        }
    }

    //inputButtonClick
    public void onInputButtonClick(View view) {
        Log.i("Confirm", "Main onInputButtonClick");
        Intent intent = new Intent(MainActivity.this, InputActivity.class);
        startActivity(intent);
    }

    //startButtonClick
    public void onStartButtonClick(View view) {
        Log.i("Confirm", "Main onStartButtonClick");
        Intent intent = new Intent(MainActivity.this, TodoActivity.class);
        startActivity(intent);

        //Serviceクラスを起動する処理
        Log.i("Confirm", "Main onStartButtonClick startService");
        Intent intentService = new Intent(MainActivity.this, FamisukeService.class);
        startService(intentService);
    }
}
