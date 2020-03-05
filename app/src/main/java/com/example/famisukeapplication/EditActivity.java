package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();

        String selectedWorks = intent.getStringExtra("works");
        String selectedField = intent.getStringExtra("field");
        String selectedFromDate = intent.getStringExtra("fromDate");
        String selectedToDate = intent.getStringExtra("toDate");

        EditText etWork = findViewById(R.id.etWork);
        EditText etField = findViewById(R.id.etField);
        EditText etFromDate = findViewById(R.id.etFromDate);
        EditText etToDate = findViewById(R.id.etToDate);

        etWork.setHint(selectedWorks);
        etField.setHint(selectedField);
        etFromDate.setHint(selectedFromDate);
        etToDate.setHint(selectedToDate);
    }

    //editButtonClick
    public void onEditButtonClick(View view){
        Log.i("Confirm", "EditActivity onEditButtonClick");
        //ToDo  Send datas to Web Server to UPDATE datas on DataBase
    }

    //deleteButtonClick
    public void onDeleteButtonClick(View view){
        Log.i("Confirm", "EditActivity onDeleteButtonClick");
        //ToDo  Send datas to Web Server to DELETE datas on DataBase
    }

    //backButtonClick
    public void onBackButtonClick(View view){
        Log.i("Confirm", "EditActivity onBackButtonClick");
        finish();
    }
}
