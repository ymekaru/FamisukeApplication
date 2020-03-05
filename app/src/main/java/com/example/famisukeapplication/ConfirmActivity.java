package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ConfirmActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String[] works;
    private String[] field;
    private String[] fromDate;
    private String[] toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Confirm", "Confirm onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();
        works = intent.getStringArrayExtra("works");
        field = intent.getStringArrayExtra("field");
        fromDate = intent.getStringArrayExtra("fromDate");
        toDate = intent.getStringArrayExtra("toDate");

        ListView listView = findViewById(R.id.lvConfirm);
        BaseAdapter adapter = new TestAdapter(this.getApplicationContext(), R.layout.list_items,
                works, field, fromDate, toDate);

        listView.setAdapter(adapter);
    }


    //resisterButtonClick
    public void onResisterButtonClick(View view) {
        Log.i("Confirm", "Confirm onResisterButtonClick");
        //ToDo  Send datas to Web Server to POST datas on DataBase

        //Serviceの処理を停止する処理
        Intent stopServiceIntent = new Intent(ConfirmActivity.this, FamisukeService.class);
        stopService(stopServiceIntent);
    }


    //backButtonClick
    public void onBackButtonClick(View view) {
        Log.i("Confirm", "Confirm onBackButtonClick");
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Confirm", "Confirm onItemClick");
        String selectedWork = works[position];
        String selectedField = field[position];
        String selectedFromDate = fromDate[position];
        String selectedToDate = toDate[position];

        Intent intent = new Intent(ConfirmActivity.this, EditActivity.class);
        intent.putExtra("works", selectedWork);
        intent.putExtra("field", selectedField);
        intent.putExtra("fromDate", selectedFromDate);
        intent.putExtra("toDate", selectedToDate);

        startActivity(intent);
    }
}
