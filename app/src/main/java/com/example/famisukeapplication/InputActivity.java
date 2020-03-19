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
        Log.i("Confirm", "InputActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent = getIntent();
        fieldBundle = intent.getBundleExtra("field");
        codeBundle = intent.getBundleExtra("code");

        Log.i("Confirm", "InputActivity onCreate");

        ArrayList<String> codes = new ArrayList<>();
        codes = codeBundle.getStringArrayList("code");
        String code = codes.get(0);
        Log.i("Confirm", "InputActivity onCreate code: " + code);

        ArrayList<String> fields = new ArrayList<>();
        fields = fieldBundle.getStringArrayList("field");
        String field = fields.get(0);
        Log.i("Confirm", "InputActivity onCreate field: " + field);

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
        spSelectCode.setOnItemSelectedListener(new SpinnerItemSelectListener());
        //「圃場」選択スピナー
        adapter = new ArrayAdapter<>(
                InputActivity.this, android.R.layout.simple_spinner_item, fields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectField.setAdapter(adapter);
        spSelectField.setOnItemSelectedListener(new SpinnerItemSelectListener());

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i("Confirm", "InputActivity onDateSet");
        String str = String.format(Locale.JAPAN, "%d/%d/%d",year, month+1, dayOfMonth);
        Log.i("Confirm", "InputActivity onDateSet str: " + str);
        tvInputDate.setText(str);
    }


    public void showDatePickerDialog(View view){
        //FragmentにActivity情報を渡す処理
        Bundle bundle = new Bundle();
        bundle.putString("activity", "Input");
        //Fragmentの生成と表示をする処理
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "DatePicker");
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("Confirm", "InputActivity onTimeSet");
        String str = String.format(Locale.JAPAN, "%d:%d", hourOfDay, minute);
        Log.i("Confirm", "InputActivity onTimeSet str: " + str);

        switch (_timeId){
            case R.id.tvInputFrom:
                Log.i("Confirm", "InputActivity onTimeSet switch from");
                tvInputFrom.setText(str);
                _timeId = 0;
                break;
            case R.id.tvInputTo:
                Log.i("Confirm", "InputActivity onTimeSet switch to");
                tvInputTo.setText(str);
                _timeId = 0;
                break;
        }
    }


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

    String spinnerItem;
    //スピナーにセットするリスナークラス
    private class SpinnerItemSelectListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner)parent;
            String item = (String)spinner.getSelectedItem();
            Log.i("Confirm", "InputActivity onItemSelected item: " + item);
            //スピナーのidで処理を分岐させる処理
            int layoutId = spinner.getId();
            switch (layoutId){
                case R.id.spSelectCode:
                    Log.i("Confirm", "InputActivity onItemSelected switch: " + item);
//                    tvCode.setText(item);
                    break;
                case R.id.spSelectField:
                    Log.i("Confirm", "InputActivity onItemSelected switch: " + item);
//                    tvField.setText(item);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.i("Confirm", "InputActivity onNothingSelected");
        }
    }


//    //TextWatcherでEditTextの記入を監視する処理
//    private class GenericTextwatcher implements TextWatcher {
//
//        private View view;
//
//        private GenericTextwatcher(View view){
//            this.view = view;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            Log.i("Confirm", "Input GenericTextwatcher afterTextChanged");
//            String inputWord = s.toString();
//
//            //入力されている画面部品のidを取得し変数に格納
//            int id = view.getId();
//
//            switch (id){
//                case R.id.etInputDate:
//                    tvDate.setText(inputWord);
//                    break;
//                case R.id.etInputFromDate:
//                    tvFromDate.setText(inputWord);
//                    break;
//                case R.id.etInputToDate:
//                    tvToDate.setText(inputWord);
//                    break;
//                case R.id.etInputDetail:
//                    tvDetail.setText(inputWord);
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    }


    //inputButtonClick
    public void onResisterButtonClick(View view){
        Log.i("Confirm", "Input onResisterButtonClick");
        //ToDo  Send datas to Web Server to POST datas on DataBase
        //String inputDate, inputFrom, inputTo, inputField, inputCode, inputDetail;
        _inputDate = (String) tvInputDate.getText();
        _inputFrom = (String) tvInputFrom.getText();
        _inputTo = (String) tvInputTo.getText();
        _inputField = (String) spSelectField.getSelectedItem();
        _inputCode = (String) spSelectCode.getSelectedItem();
        _inputDetail = etInputDetail.getText().toString();
        Log.i("Confirm", "Input onResisterButtonClick inputDate: " + _inputDetail);

        PostInputData postInputData = new PostInputData();
        postInputData.execute();
    }


    //backButtonClick
    public void onBackButtonClick(View view){
        Log.i("Confirm", "Input onBackButtonClick");
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
                    Log.i("Confirm", "InputActivity doInBackground jsonText: " + jsonText);
                    //PrintStream printStream = new PrintStream(con.getOutputStream());
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonText);
                    printStream.close();
                }
                outputStream.close();

                status = con.getResponseCode();
                if(status == 201){
                    Log.i("Confirm", "InputActivity doInBackground response: " + status);
                }
                else {
                    Log.i("Confirm", "InputActivity doInBackground error: " + status);
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
                    Log.i("Confirm", "InputActivity doInBackground finally");
                    con.disconnect();
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            if(status == 201){
                Log.i("Confirm", "InputActivity onPostExecute status: " + status);
                Toast.makeText(InputActivity.this, R.string.tstResistered, Toast.LENGTH_LONG).show();
            }
            else {
                Log.i("Confirm", "InputActivity onPostExecute status: " + status);
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
