package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    TextView _tvDate, _tvFromTime, _tvToTime;
    EditText _etDetail;
    Spinner _spSelectedCode, _spSelectedField;

    Bundle _fieldBundle;
    Bundle _codeBundle;

    int _timeId = 0;

    String _editDate, _editFrom, _editTo, _editField, _editCode, _editDetail;

    String _selectedCode, _selectedField, _selectedFromTime, _selectedToTime, _selectedDetail,
            _selectedDate, _selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //各画面部品のオブジェクトを変数に格納
        _tvDate = findViewById(R.id.tvEditDate);
        _tvFromTime = findViewById(R.id.tvEditFrom);
        _tvToTime = findViewById(R.id.tvEditTo);
        _spSelectedCode = findViewById(R.id.spSelectedCode);
        _spSelectedField = findViewById(R.id.spSelectedField);
        _etDetail = findViewById(R.id.etEditDetail);

        //Intentクラスで遷移されたデータを取得
        Intent intent = getIntent();
        _selectedCode = intent.getStringExtra("code");
        _selectedField = intent.getStringExtra("field");
        _selectedFromTime = intent.getStringExtra("fromTime");
        _selectedToTime = intent.getStringExtra("toTime");
        _selectedDetail = intent.getStringExtra("detail");
        _selectedDate = intent.getStringExtra("date");
        _selectedId = intent.getStringExtra("id");
        _fieldBundle = intent.getBundleExtra("fieldBundle");
        _codeBundle = intent.getBundleExtra("codeBundle");

        //Bundleクラスオブジェクトで渡されたデータをArrayListに格納
        ArrayList<String> codes = new ArrayList<>();
        codes = _codeBundle.getStringArrayList("code");
        String code = codes.get(0);
        Log.i("Logging", "EditActivity onCreate code: " + code);
        int selectedCodeIndex = codes.indexOf(_selectedCode);

        ArrayList<String> fields = new ArrayList<>();
        fields = _fieldBundle.getStringArrayList("field");
        String field = fields.get(0);
        Log.i("Logging", "EditActivity onCreate field: " + field);
        int selectedFieldIndex = fields.indexOf(_selectedField);

        //スピナーにアダプターとリスナーをセット
        //「作業内容」選択スピナー
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditActivity.this, android.R.layout.simple_spinner_item, codes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spSelectedCode.setAdapter(adapter);
        //_spSelectedCode.setOnItemSelectedListener(new EditActivity.SpinnerItemSelectListener());
        _spSelectedCode.setSelection(selectedCodeIndex);

        //「圃場」選択スピナー
        adapter = new ArrayAdapter<>(
                EditActivity.this, android.R.layout.simple_spinner_item, fields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spSelectedField.setAdapter(adapter);
        //_spSelectedField.setOnItemSelectedListener(new EditActivity.SpinnerItemSelectListener());
        _spSelectedField.setSelection(selectedFieldIndex);

        //TextViewクラスとEditTextクラスのオブジェクトにデータをセット
        _tvDate.setHint(_selectedDate);
        _tvFromTime.setHint(_selectedFromTime);
        _tvToTime.setHint(_selectedToTime);
        _etDetail.setHint(_selectedDetail);
    }


    //スピナーにセットするリスナークラス
//    private class SpinnerItemSelectListener implements AdapterView.OnItemSelectedListener{
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Spinner spinner = (Spinner)parent;
//            String item = (String)spinner.getSelectedItem();
//            Log.i("Logging", "EditActivity onItemSelected item: " + item);
//            //スピナーのidで処理を分岐させる処理
//            int layoutId = spinner.getId();
//            switch (layoutId){
//                case R.id.spSelectedCode:
//                    Log.i("Logging", "EditActivity onItemSelected switch: " + item);
////                    tvCode.setText(item);
//                    break;
//                case R.id.spSelectedField:
//                    Log.i("Logging", "EditActivity onItemSelected switch: " + item);
////                    tvField.setText(item);
//                    break;
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            Log.i("Logging", "EditActivity onNothingSelected");
//        }
//    }


    //EditButtonをClickした際の処理
    public void onEditButtonClick(View view){
        Log.i("Logging", "EditActivity onEditButtonClick");

        _editDate = (String) _tvDate.getText();
        _editFrom = (String) _tvFromTime.getText();
        _editTo = (String) _tvToTime.getText();
        _editDetail = _etDetail.getText().toString();

        if(_editDate.equals("")){
            _editDate = _selectedDate;
        }
        if(_editFrom.equals("")){
            _editFrom = _selectedFromTime;
        }
        if(_editTo.equals("")){
            _editTo = _selectedToTime;
        }
        if(_editDetail.equals("")){
            _editDetail = _selectedDetail;
        }

        _editField = (_spSelectedField != null) ? (String) _spSelectedField.getSelectedItem() : _selectedField;
        _editCode = (_spSelectedCode != null) ? (String) _spSelectedCode.getSelectedItem() : _selectedCode;

        String flag = "update";
        ChangeTodoData updateTodoData = new ChangeTodoData();
        updateTodoData.execute(flag);
    }

    //DeleteButtonをClickした際の処理
    public void onDeleteButtonClick(View view){
        Log.i("Logging", "EditActivity onDeleteButtonClick");

        String flag = "delete";
        ChangeTodoData deleteTodoData = new ChangeTodoData();
        deleteTodoData.execute(flag);
    }

    //BackButtonをClickした際の処理
    public void onBackButtonClick(View view){
        Log.i("Logging", "EditActivity onBackButtonClick");
        finish();
    }


    //DatePickerの設定
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i("Logging", "EditActivity onDateSet");
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
        _tvDate.setText(date);
    }

    //DatePickerのonClickメソッド
    public void showEditDatePickerDialog(View view){
        Log.i("Logging", "EditActivity showDatePickerDialog");
        //FragmentにActivity情報を渡す処理
        Bundle bundle = new Bundle();
        bundle.putString("activity", "Edit");
        //Fragmentの生成と表示をする処理
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "DatePicker");
    }


    //TimePickerの設定
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("Logging", "EditActivity onTimeSet");

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

        Log.i("Logging", "EditActivity onTimeSet _timeId: " + _timeId);
        switch (_timeId){
            case R.id.tvEditFrom:
                Log.i("Logging", "EditActivity onTimeSet switch from");
                _tvFromTime.setText(time);
                _timeId = 0;
                break;
            case R.id.tvEditTo:
                Log.i("Logging", "EditActivity onTimeSet switch to");
                _tvToTime.setText(time);
                _timeId = 0;
                break;
        }
    }

    //TimePickerのonClickメソッド
    public void showEditTimePickerDialog(View view){
        //FragmentにActivity情報を渡す処理
        Bundle bundle = new Bundle();
        bundle.putString("activity", "Edit");
        //Fragmentの生成と表示をする処理
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "TimePicker");
        _timeId = view.getId();
        Log.i("Logging", "EditActivity showEditTimePickerDialog _timeId: " + _timeId);
    }


    //DBの内容に変更を加える処理
    private class ChangeTodoData extends AsyncTask<String, String, Integer>{
        @Override
        protected Integer doInBackground(String... strings) {

            String flag = strings[0];
            Log.i("Logging", "EditActivity doInBackground flag: " + flag);
            int status = 0;

            //DBのテーブル名
            String table = "todo";
            String urlStr = "";
            //DBから取得した圃場データを格納する変数と配列
            String response = null;

            HashMap<String, Object> jsonOuterData = new HashMap<>();

            //押下ボタンが「変更」か「削除」かで処理を分岐
            if(flag.equals("delete")) {
                //DBクエリをPOSTするために成形
                HashMap<String, String> jsonInnerValueData = new HashMap<>();
                jsonInnerValueData.put("_id", _selectedId);

                HashMap<String, Object> jsonMiddleData = new HashMap<>();
                jsonMiddleData.put("selector", jsonInnerValueData);

                jsonOuterData.put("table", table);
                jsonOuterData.put("body", jsonMiddleData);

                //APサーバーのURLを変数に格納
                urlStr = "https://sakuranbo-mekaru2.mybluemix.net/common/android_delete_cloudant";
                Log.i("Logging", "EditActivity doInBackground delete urlStr: " + urlStr);
            }
            else if(flag.equals("update")){
                //各入力項目をリストに格納
                HashMap<String, String> innerJsonData = new HashMap<>();
                innerJsonData.put("date", _editDate);
                innerJsonData.put("fromTime", _editFrom);
                innerJsonData.put("toTime", _editTo);
                innerJsonData.put("field", _editField);
                innerJsonData.put("code", _editCode);
                innerJsonData.put("detail", _editDetail);
                innerJsonData.put("_id", _selectedId);

                jsonOuterData.put("table", "todo");
                jsonOuterData.put("data", innerJsonData);

                //APサーバーのURLを変数に格納
                urlStr = "https://sakuranbo-mekaru2.mybluemix.net/common/android_post_cloudant";
                Log.i("Logging", "EditActivity doInBackground update urlStr: " + urlStr);
            }

            HttpsURLConnection con = null;
            InputStream inputStream;

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

                if(jsonOuterData.size()>0){
                    Log.i("Logging", "EditActivity doInBackground if");
                    JSONObject jsonObject = new JSONObject(jsonOuterData);
                    String jsonText = jsonObject.toString();
                    Log.i("Logging", "EditActivity doInBackground jsonText: " + jsonText);
                    PrintStream printStream = new PrintStream(con.getOutputStream());
                    //PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonText);
                    printStream.close();
                }
                outputStream.close();

                status = con.getResponseCode();
                if(status == 201){
                    Log.i("Logging", "EditActivity doInBackground response: " + status);
                }
                else {
                    Log.i("Logging", "EditActivity doInBackground error: " + status);
                }

                inputStream = con.getInputStream();
                response = is2String(inputStream);
                Log.i("Logging", "TodoActivity doInBackground response: " + response);
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
                    Log.i("Logging", "EditActivity doInBackground finally");
                    con.disconnect();
                }
            }

            return status;
        }

        private String is2String(InputStream is) throws IOException{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while (0 <= (line = reader.read(b))){
                stringBuffer.append(b, 0, line);
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            if(status == 201){
                Log.i("Logging", "EditActivity onPostExecute status: " + status);
                Toast.makeText(EditActivity.this, R.string.tstResistered, Toast.LENGTH_LONG).show();
            }
            else {
                Log.i("Logging", "EditActivity onPostExecute status: " + status);
                Toast.makeText(EditActivity.this, R.string.tstError, Toast.LENGTH_LONG).show();
            }
        }
    }
}
