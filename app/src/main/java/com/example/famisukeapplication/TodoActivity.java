package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class TodoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> _codes = new ArrayList<>();
    ArrayList<String> _fields = new ArrayList<>();
    ArrayList<String> _fromTimes = new ArrayList<>();
    ArrayList<String> _toTimes = new ArrayList<>();
    ArrayList<String> _details = new ArrayList<>();
    ArrayList<String> _dates = new ArrayList<>();
    ArrayList<String> _ids = new ArrayList<>();

    String _today = new String();

    Bundle _fieldBundle;
    Bundle _codeBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Log.i("Logging", "TodoActivity onCreate");

        Intent intent = getIntent();
        _fieldBundle = intent.getBundleExtra("field");
        _codeBundle = intent.getBundleExtra("code");

        //TextViewに本日の日付けを表示する処理
        showDate();

        //Bundleの値が取れていれば処理を継続する
        if(_fieldBundle != null && _codeBundle != null) {
            //DBからTodoリストを取得する処理
            GetTodoDatas getTodoDatas = new GetTodoDatas();
            getTodoDatas.execute();
        }
        else {
            finish();
        }
    }


    //「戻る」ボタンで戻ってきた際にActivityをリロードする処理
    @Override
    protected void onRestart() {
        Log.i("Logging", "TodoActivity onRestart");
        super.onRestart();
        reload();
    }

    //リロード処理
    public void reload() {
        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    //画面のListViewを呼び出す処理
    public void activateAdapter(){
        ListView listView = findViewById(R.id.lvTodoList);
        BaseAdapter adapter = new TestAdapter(this.getApplicationContext(), R.layout.list_items,
                _codes, _fields, _fromTimes, _toTimes, _details, _dates, _ids);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    //TextViewに日付を表示するための処理
    public void showDate(){
        Log.i("Logging", "TodoActivity showDate");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Log.i("Logging", "TodoActivity showDate simpleDateFormat: " + simpleDateFormat.format(date));

        //DB検索クエリ用のフォーマットを変数に格納
        _today = simpleDateFormat.format(date);
        Log.i("Logging", "TodoActivity showDate _today: " + _today);

        //TextViewに表示用のフォーマット
        simpleDateFormat = new SimpleDateFormat("MM/dd");
        Log.i("Logging", "TodoActivity showDate simpleDateFormat: " + simpleDateFormat.format(date));
        TextView tvShowDate = findViewById(R.id.tvShowDate);
        tvShowDate.setText(simpleDateFormat.format(date));
    }


    //AddButtonをClickした際の処理
    public void onAddButtonClick(View view){
        Log.i("Logging", "TodoActivity onAddButtonClick");

        //InputActivityへ遷移
        Intent intent = new Intent(TodoActivity.this, InputActivity.class);
        intent.putExtra("field", _fieldBundle);
        intent.putExtra("code", _codeBundle);
        startActivity(intent);
    }


    //CompleteButtonをClickした際の処理
    public void onCompleteButtonClick(View view){
        Log.i("Logging", "TodoActivity onCompleteButtonClick");

        //ConfirmActivityへ遷移
        Intent intent = new Intent(TodoActivity.this, ConfirmActivity.class);
        intent.putExtra("codes", _codes);
        intent.putExtra("fields", _fields);
        intent.putExtra("fromTimes", _fromTimes);
        intent.putExtra("toTimes", _toTimes);
        intent.putExtra("details", _details);
        intent.putExtra("dates", _dates);
        intent.putExtra("ids", _ids);
        intent.putExtra("fieldBundle", _fieldBundle);
        intent.putExtra("codeBundle", _codeBundle);
        startActivity(intent);
    }


    //BackButtonをClickした際の処理
    public void onBackButtonClick(View view){
        Log.i("Logging", "TodoActivity onBackButtonClick");
        finish();
    }


    //ListViewのアイテムをClickした際の処理
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Logging", "TodoActivity onItemClick position: " + position);
        String selectedCode = _codes.get(position);
        String selectedField = _fields.get(position);
        String selectedFromDate = _fromTimes.get(position);
        String selectedToDate = _toTimes.get(position);
        String selectedDetail = _details.get(position);
        String selectedDate = _dates.get(position);
        String selectedId = _ids.get(position);
        Log.i("Logging", "TodoActivity onItemClick selectedDetail: " + selectedDetail);
        Log.i("Logging", "TodoActivity onItemClick selectedDate: " + selectedDate);
        Log.i("Logging", "TodoActivity onItemClick selectedId: " + selectedId);

        //EditActivityへ遷移
        Intent intent = new Intent(TodoActivity.this, EditActivity.class);
        intent.putExtra("code", selectedCode);
        intent.putExtra("field", selectedField);
        intent.putExtra("fromTime", selectedFromDate);
        intent.putExtra("toTime", selectedToDate);
        intent.putExtra("detail", selectedDetail);
        intent.putExtra("date", selectedDate);
        intent.putExtra("id", selectedId);
        intent.putExtra("fieldBundle", _fieldBundle);
        intent.putExtra("codeBundle", _codeBundle);

        startActivity(intent);
    }


    //非同期でDBからデータを取得するクラス
    private class GetTodoDatas extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            //DBのテーブル名
            String table = "todo";

            //DBから取得した圃場データを格納する変数と配列
            String response = null;

            //DBクエリをPOSTするために成形
            HashMap<String, String> jsonInnerValueData = new HashMap<>();
            jsonInnerValueData.put("$ne", "");

            HashMap<String, Object> jsonInnerKeyData = new HashMap<>();
            jsonInnerKeyData.put("_id", jsonInnerValueData);

            HashMap<String, Object> jsonMiddleData = new HashMap<>();
            jsonMiddleData.put("selector", jsonInnerKeyData);


            jsonInnerValueData = new HashMap<>();
            jsonInnerValueData.put("$eq", _today);

            jsonInnerKeyData = new HashMap<>();
            jsonInnerKeyData.put("date", jsonInnerValueData);

            jsonMiddleData.put("selector", jsonInnerKeyData);

            ArrayList<String> jsonInnerArrayData = new ArrayList<>();
            jsonInnerArrayData.add("fromTime");
            jsonInnerArrayData.add("toTime");
            jsonInnerArrayData.add("field");
            jsonInnerArrayData.add("code");
            jsonInnerArrayData.add("detail");
            jsonInnerArrayData.add("date");
            jsonInnerArrayData.add("_id");

            jsonMiddleData.put("fields", jsonInnerArrayData);

            HashMap<String, Object> jsonOuterData = new HashMap<>();
            jsonOuterData.put("table", table);
            jsonOuterData.put("body", jsonMiddleData);

            //APサーバーのURLを変数に格納
            String urlStr = "https://sakuranbo-mekaru2.mybluemix.net/common/android_find_cloudant";
            Log.i("Logging", "TodoActivity doInBackground urlStr: " + urlStr);

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
                    Log.i("Logging", "TodoActivity doInBackground if");
                    JSONObject jsonObject = new JSONObject(jsonOuterData);
                    String jsonText = jsonObject.toString();
                    Log.i("Logging", "TodoActivity doInBackground jsonText: " + jsonText);
                    PrintStream printStream = new PrintStream(con.getOutputStream());
                    //PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonText);
                    printStream.close();
                }
                outputStream.close();

                final int status = con.getResponseCode();
                if(status == 200){
                    Log.i("Logging", "TodoActivity doInBackground response: " + status);
                }
                else {
                    Log.i("Logging", "TodoActivity doInBackground error: " + status);
                }

                inputStream = con.getInputStream();
                response = is2String(inputStream);
                Log.i("Logging", "TodoActivity doInBackground response: " + response);
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(con != null){
                    Log.i("Logging", "TodoActivity doInBackground finally");
                    con.disconnect();
                }
            }

            return response;
        }

        //InputStreamに入ってきたデータをString型に変換する処理
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
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.i("Logging", "TodoActivity doPostExecute response: " + response);

            String fromTime = "fromTime";
            String toTime = "toTime";
            String field = "field";
            String code = "code";
            String detail = "detail";
            String date = "date";
            String id = "_id";

            try{
                JSONObject rootJson = new JSONObject(response);
                JSONArray datasJson = rootJson.getJSONArray("data");

                int len = datasJson.length();
                Log.i("Logging", "TodoActivity doPostExecute len: " + len);
                for(int i=0; i<len; i++){
                    JSONObject dataJson = datasJson.getJSONObject(i);
                    String dataFromTime = "";
                    String dataToTime = "";
                    String dataField = "";
                    String dataCode = "";
                    String dataDetail = "";
                    String dataDate = "";
                    String dataId = "";
                    dataFromTime = dataJson.getString(fromTime);
                    dataToTime = dataJson.getString(toTime);
                    dataField = dataJson.getString(field);
                    dataCode = dataJson.getString(code);
                    dataDetail = dataJson.getString(detail);
                    dataDate = dataJson.getString(date);
                    dataId = dataJson.getString(id);
                    Log.i("Confirm", "TodoActivity doPostExecute data: " + dataFromTime);
                    Log.i("Logging", "TodoActivity doPostExecute dataDetail: " + dataDetail);
                    Log.i("Logging", "TodoActivity doPostExecute dataDate: " + dataDate);
                    Log.i("Logging", "TodoActivity doPostExecute dataId: " + dataId);
                    _fromTimes.add(dataFromTime);
                    _toTimes.add(dataToTime);
                    _fields.add(dataField);
                    _codes.add(dataCode);
                    _details.add(dataDetail);
                    _dates.add(dataDate);
                    _ids.add(dataId);

                    //ListView呼び出しメソッドを呼び出し
                    activateAdapter();
                }
            } catch (JSONException e) {
                Log.i("Logging", "TodoActivity doPostExecute JSONException");
                e.printStackTrace();
            }

        }
    }
}