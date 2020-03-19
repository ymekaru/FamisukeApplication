package com.example.famisukeapplication;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class FamisukeService extends Service {
    //GPSから取得する緯度経度を格納する変数
    //初期値はMTCの緯度経度を入れておく
    private double _latitude = 26.2692409;
    private double _longitude = 127.7738417;

    //locationManagerとGPSLocationListenerのインスタンスを格納する変数
    LocationManager locationManager;
    GPSLocationListener locationListener;

    @Override
    public void onCreate() {
        Log.i("Confirm", "Service onCreate");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new GPSLocationListener();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0,
                locationListener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Confirm", "Service onStartCommand");

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i("Confirm", "Service onDestroy");
        locationManager.removeUpdates(locationListener);
    }


    public void resisterToDb() {
//        String latitude;
//        latitude = BigDecimal.valueOf(_latitude).toPlainString();
//        Log.i("Confirm", "confiming latitude: " + latitude);

        ResisterDataBase resisterDataBase = new ResisterDataBase();
        resisterDataBase.execute();

    }


    //GPSの値を取得するListener
    private class GPSLocationListener implements LocationListener {
        //        String latitude;
        @Override
        public void onLocationChanged(Location location) {
            _latitude = location.getLatitude();
            _longitude = location.getLongitude();

//            latitude = BigDecimal.valueOf(_latitude).toPlainString();
//            Log.i("Confirm", "Service onLocationChanged latitude" + latitude);

            resisterToDb();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }


    //GPSLocationListenerが取得した値を非同期でDBに登録するクラス
    private class ResisterDataBase extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            //緯度経度を格納する変数
            String latitude;
            String longitude;
            //double型からString型へ変換
            latitude = BigDecimal.valueOf(_latitude).toPlainString();
            longitude = BigDecimal.valueOf(_longitude).toPlainString();
            Log.i("Confirm", "AsyncTask doInBackground latitude: " + latitude);
            Log.i("Confirm", "AsyncTask doInBackground longitude: " + longitude);

            Date date = new Date();
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);

            //緯度経度をDBに登録するために成形
            HashMap<String, Object> jsonData = new HashMap<>();
            jsonData.put("table", "testlatlng");

            HashMap<String, String> innerJson = new HashMap<>();
            innerJson.put("latitude", latitude);
            innerJson.put("longitude", longitude);
            innerJson.put("currentTime", timestamp.toString());

            jsonData.put("data", innerJson);

            //APサーバーのURLを変数に格納
            String urlStr = "https://sakuranbo-mekaru2.mybluemix.net/common/android_post_cloudant";

            HttpsURLConnection con = null;

            try{
                URL url = new URL(urlStr);
                con = (HttpsURLConnection)url.openConnection();
                con.setReadTimeout(10000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Android");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.connect();

                OutputStream outputStream = con.getOutputStream();
                if(jsonData.size()>0){
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String jsonText = jsonObject.toString();
                    Log.i("Confirm", "AsyncTask doInBackground jsonText: " + jsonText);
                    //PrintStream printStream = new PrintStream(con.getOutputStream());
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonText);
                    printStream.close();
                }
                outputStream.close();

                final int status = con.getResponseCode();
                if(status == 201){
                    Log.i("Confirm", "ResisterDataBase doInBackground response: " + status);
                }
                else {
                    Log.i("Confirm", "ResisterDataBase doInBackground error: " + status);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(con != null){
                    Log.i("Confirm", "AsyncTask doInBackground finally");
                    con.disconnect();
                }
            }
            return null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
