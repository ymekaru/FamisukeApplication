package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static String [] works = {
            "苗植え",
            "雑草取り",
            "農薬散布",
            "肥料",
            "収穫"
    };

    public static String [] field = {
            "テスト圃場A",
            "テスト圃場B",
            "テスト圃場C",
            "テスト圃場D",
            "テスト圃場E"
    };

    public static String [] fromDate = {
            "08:00",
            "10:00",
            "13:00",
            "15:00",
            "17:00"
    };

    public static String [] toDate = {
            "09:00",
            "12:00",
            "14:00",
            "16:00",
            "18:00"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Log.i("Confirm", "Todo onCreate");

        //TextViewに本日の日付けを表示する処理
        showDate();


        ListView listView = findViewById(R.id.lvTodoList);
        BaseAdapter adapter = new TestAdapter(this.getApplicationContext(), R.layout.list_items,
                works, field, fromDate, toDate);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    //TextViewに日付を表示するための処理
    public void showDate(){
        Log.i("Confirm", "Todo showDate");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        Log.i("Confirm", "Todo showDate simpleDateFormat: " + simpleDateFormat.format(date).toString());

        TextView tvShowDate = findViewById(R.id.tvShowDate);
        tvShowDate.setText(simpleDateFormat.format(date).toString());
    }

    //addButtonClick
    public void onAddButtonClick(View view){
        Log.i("Confirm", "Todo onAddButtonClick");
        Intent intent = new Intent(TodoActivity.this, InputActivity.class);
        startActivity(intent);
    }

    //completeButtonClick
    public void onCompleteButtonClick(View view){
        Log.i("Confirm", "Todo onCompleteButtonClick");

        Intent intent = new Intent(TodoActivity.this, ConfirmActivity.class);
        intent.putExtra("works", works);
        intent.putExtra("field", field);
        intent.putExtra("fromDate", fromDate);
        intent.putExtra("toDate", toDate);
        startActivity(intent);
    }

    //backButtonClick
    public void onBackButtonClick(View view){
        Log.i("Confirm", "Todo onBackButtonClick");
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Confirm", "Todo onItemClick");
        String selectedWork = works[position];
        String selectedField = field[position];
        String selectedFromDate = fromDate[position];
        String selectedToDate = toDate[position];

        Intent intent = new Intent(TodoActivity.this, EditActivity.class);
        intent.putExtra("works", selectedWork);
        intent.putExtra("field", selectedField);
        intent.putExtra("fromDate", selectedFromDate);
        intent.putExtra("toDate", selectedToDate);

        startActivity(intent);
    }
}