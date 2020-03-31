package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class ConfirmActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<String> _codes = new ArrayList<>();
    ArrayList<String> _fields = new ArrayList<>();
    ArrayList<String> _fromTimes = new ArrayList<>();
    ArrayList<String> _toTimes = new ArrayList<>();
    ArrayList<String> _details = new ArrayList<>();
    ArrayList<String> _dates = new ArrayList<>();
    ArrayList<String> _ids = new ArrayList<>();

    Bundle _fieldBundle;
    Bundle _codeBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Logging", "ConfirmActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        //前Activityからのデータを受け取る処理
        Intent intent = getIntent();
        _codes = intent.getStringArrayListExtra("codes");
        _fields = intent.getStringArrayListExtra("fields");
        _fromTimes = intent.getStringArrayListExtra("fromTimes");
        _toTimes = intent.getStringArrayListExtra("toTimes");
        _details = intent.getStringArrayListExtra("details");
        _dates = intent.getStringArrayListExtra("dates");
        _ids = intent.getStringArrayListExtra("ids");
        _fieldBundle = intent.getBundleExtra("fieldBundle");
        _codeBundle = intent.getBundleExtra("codeBundle");

        activateAdapter();
    }


    //画面のListViewを呼び出す処理
    public void activateAdapter(){
        ListView listView = findViewById(R.id.lvConfirm);
        BaseAdapter adapter = new TestAdapter(this.getApplicationContext(), R.layout.list_items,
                _codes, _fields, _fromTimes, _toTimes, _details, _dates, _ids);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    //ResisterButtonをClickした際の処理
    public void onResisterButtonClick(View view) {
        Log.i("Logging", "ConfirmActivity onResisterButtonClick");
        //ToDo  Send datas to Web Server to POST datas on DataBase

        //Serviceの処理を停止する処理
        Intent stopServiceIntent = new Intent(ConfirmActivity.this, FamisukeService.class);
        stopService(stopServiceIntent);
    }


    //BackButtonをClickした際の処理
    public void onBackButtonClick(View view) {
        Log.i("Logging", "ConfirmActivity onBackButtonClick");
        finish();
    }


    //ListViewのアイテムをClickした際の処理
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Logging", "ConfirmActivity onItemClick position: " + position);
        String selectedCode = _codes.get(position);
        String selectedField = _fields.get(position);
        String selectedFromTime = _fromTimes.get(position);
        String selectedToTime = _toTimes.get(position);
        String selectedDetail = _details.get(position);
        String selectedDate = _dates.get(position);
        String selectedId = _ids.get(position);

        //EditActivityへ遷移
        Intent intent = new Intent(ConfirmActivity.this, EditActivity.class);
        intent.putExtra("code", selectedCode);
        intent.putExtra("field", selectedField);
        intent.putExtra("fromTime", selectedFromTime);
        intent.putExtra("toTime", selectedToTime);
        intent.putExtra("detail", selectedDetail);
        intent.putExtra("date", selectedDate);
        intent.putExtra("id", selectedId);
        intent.putExtra("fieldBundle", _fieldBundle);
        intent.putExtra("codeBundle", _codeBundle);

        startActivity(intent);
        finish();
    }
}
