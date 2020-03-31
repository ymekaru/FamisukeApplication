package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class InputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    Spinner spSelectCode, spSelectField;
    EditText etInputDetail;
    TextView tvInputDate, tvInputFrom, tvInputTo;

    Bundle fieldBundle;
    Bundle codeBundle;

    int _timeId = 0;
    String _inputDate, _inputFrom, _inputTo, _inputField, _inputCode, _inputDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Logging", "InputActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //前画面から渡されたデータを受け取りArrayListに格納する処理
        Intent intent = getIntent();
        fieldBundle = intent.getBundleExtra("field");
        codeBundle = intent.getBundleExtra("code");

        ArrayList<String> fields = new ArrayList<>();
        fields = fieldBundle.getStringArrayList("field");

        ArrayList<String> codes = new ArrayList<>();
        codes = codeBundle.getStringArrayList("code");

        //Spinnerの画面毎のオブジェクトを作成し変数に格納
        spSelectCode = findViewById(R.id.spSelectCode);
        spSelectField = findViewById(R.id.spSelectField);

        //EditTextとTextViewの画面部品毎のオブジェクトを作成し変数に格納
        etInputDetail = findViewById(R.id.etInputDetail);
        tvInputDate = findViewById(R.id.tvInputDate);
        tvInputFrom = findViewById(R.id.tvInputFrom);
        tvInputTo = findViewById(R.id.tvInputTo);


        //スピナーにアダプターとリスナーをセット
        //「作業内容」選択スピナー
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                InputActivity.this, android.R.layout.simple_spinner_item, codes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectCode.setAdapter(adapter);
//        spSelectCode.setOnItemSelectedListener(new SpinnerItemSelectListener());
//        //「圃場」選択スピナー
        adapter = new ArrayAdapter<>(
                InputActivity.this, android.R.layout.simple_spinner_item, fields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectField.setAdapter(adapter);
        //spSelectField.setOnItemSelectedListener(new SpinnerItemSelectListener());

    }


    //DatePickerFragmentからの値を表示する処理
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i("Logging", "InputActivity onDateSet");
        String yearString;
        String monthString;
        String dayString;

        month = month + 1;

        if(month < 10){
            monthString = "0" + String.valueOf(month);
        }
        else{
            monthString = String.valueOf(month);
        }

        if(dayOfMonth < 10){
            dayString = "0" + String.valueOf(dayOfMonth);
        }
        else{
            dayString = String.valueOf(dayOfMonth);
        }
        yearString = String.valueOf(year);

        String date = yearString + "/" + monthString + "/" + dayString;
        Log.i("Logging", "InputActivity onDateSet date: " + date);
        tvInputDate.setText(date);
    }

    //DatePickerFragmentを生成する処理
    public void showDatePickerDialog(View view){
        //FragmentにActivity情報を渡す処理
        Bundle bundle = new Bundle();
        bundle.putString("activity", "Input");
        //Fragmentの生成と表示をする処理
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "DatePicker");
    }


    //TimePickerFragmentからの値を表示する処理
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("Logging", "InputActivity onTimeSet");

        String hourString;
        String minuteString;

        if(hourOfDay < 10){
            hourString = "0" + String.valueOf(hourOfDay);
        }
        else {
            hourString = String.valueOf(hourOfDay);
        }

        if(minute < 10){
            minuteString = "0" + String.valueOf(minute);
        }
        else {
            minuteString = String.valueOf(minute);
        }

        String time = hourString + ":" + minuteString;
        Log.i("Logging", "InputActivity onTimeSet time: " + time);

        switch (_timeId){
            case R.id.tvInputFrom:
                Log.i("Logging", "InputActivity onTimeSet switch from");
                tvInputFrom.setText(time);
                _timeId = 0;
                break;
            case R.id.tvInputTo:
                Log.i("Logging", "InputActivity onTimeSet switch to");
                tvInputTo.setText(time);
                _timeId = 0;
                break;
        }
    }

    //TimePickerFragmentを生成する処理
    public void showTimePickerDialog(View view){
        //FragmentにActivity情報を渡す処理
        Bundle bundle = new Bundle();
        bundle.putString("activity", "Input");
        //Fragmentの生成と表示をする処理
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "TimePicker");
        _timeId = view.getId();
    }


//    //スピナーにセットするリスナークラス
//    private class SpinnerItemSelectListener implements AdapterView.OnItemSelectedListener{
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Spinner spinner = (Spinner)parent;
//            String item = (String)spinner.getSelectedItem();
//            Log.i("Logging", "InputActivity onItemSelected item: " + item);
//            //スピナーのidで処理を分岐させる処理
//            int layoutId = spinner.getId();
//            switch (layoutId){
//                case R.id.spSelectCode:
//                    Log.i("Logging", "InputActivity onItemSelected switch: " + item);
////                    tvCode.setText(item);
//                    break;
//                case R.id.spSelectField:
//                    Log.i("Logging", "InputActivity onItemSelected switch: " + item);
////                    tvField.setText(item);
//                    break;
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            Log.i("Logging", "InputActivity onNothingSelected");
//        }
//    }


    //ResisterButtonをClickした際の処理
    public void onResisterButtonClick(View view){
        Log.i("Logging", "Input onResisterButtonClick");
        //ToDo  Send datas to Web Server to POST datas on DataBase
        _inputDate = (String) tvInputDate.getText();
        _inputFrom = (String) tvInputFrom.getText();
        _inputTo = (String) tvInputTo.getText();
        _inputField = (String) spSelectField.getSelectedItem();
        _inputCode = (String) spSelectCode.getSelectedItem();
        _inputDetail = etInputDetail.getText().toString();
        Log.i("Logging", "Input onResisterButtonClick inputDate: " + _inputDetail);

        //DBにデータをPOSTするクラスのインスタンスを生成し実行する処理
        PostInputData postInputData = new PostInputData();
        postInputData.execute();
    }


    //BackButtonをClickした際の処理
    public void onBackButtonClick(View view){
        Log.i("Logging", "Input onBackButtonClick");
        finish();
    }


    //入力した作業内容をDBに登録する処理
    private class PostInputData extends AsyncTask<String, String, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            int status = 0;

            //各入力項目をリストに格納
            HashMap<String, String> innerJsonData = new HashMap<>();
            innerJsonData.put("date", _inputDate);
            innerJsonData.put("fromTime", _inputFrom);
            innerJsonData.put("toTime", _inputTo);
            innerJsonData.put("field", _inputField);
            innerJsonData.put("code", _inputCode);
            innerJsonData.put("detail", _inputDetail);

            HashMap<String, Object> jsonData = new HashMap<>();
            jsonData.put("table", "todo");
            jsonData.put("data", innerJsonData);

            //APサーバーのURLを変数に格納
            String urlStr = "https://sakuranbo-mekaru2.mybluemix.net/common/android_post_cloudant";

            HttpsURLConnection con = null;
            try {
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
                    Log.i("Logging", "InputActivity doInBackground jsonText: " + jsonText);
                    //PrintStream printStream = new PrintStream(con.getOutputStream());
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonText);
                    printStream.close();
                }
                outputStream.close();

                status = con.getResponseCode();
                if(status == 201){
                    Log.i("Logging", "InputActivity doInBackground response: " + status);
                }
                else {
                    Log.i("Logging", "InputActivity doInBackground error: " + status);
                }
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(con != null){
                    Log.i("Logging", "InputActivity doInBackground finally");
                    con.disconnect();
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            if(status == 201){
                Log.i("Logging", "InputActivity onPostExecute status: " + status);
                Toast.makeText(InputActivity.this, R.string.tstResistered, Toast.LENGTH_LONG).show();
            }
            else {
                Log.i("Logging", "InputActivity onPostExecute status: " + status);
                Toast.makeText(InputActivity.this, R.string.tstError, Toast.LENGTH_LONG).show();
            }

            //グローバル変数の値を削除
            _inputDate = "";
            _inputFrom = "";
            _inputTo = "";
            _inputField = "";
            _inputCode = "";
            _inputDetail = "";
        }
    }

}
