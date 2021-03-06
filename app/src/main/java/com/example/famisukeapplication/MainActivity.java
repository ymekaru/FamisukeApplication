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

public class MainActivity extends AppCompatActivity {

    Bundle fieldBundle;
    Bundle codeBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Logging", "Main onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBからデータを取得するクラスのインスタンスを生成し、fieldテーブルのデータを取得する処理
        GetDatasFromDb getDatasFromDb = new GetDatasFromDb();
        getDatasFromDb.execute("field");

        //DBからデータを取得するクラスのインスタンスを生成し、codeテーブルのデータを取得する処理
        GetDatasFromDb getDatasFromDb1 = new GetDatasFromDb();
        getDatasFromDb1.execute("code");

        //permission Check
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("Logging", "Main onCreate checkSelfPermission");
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1000);
            return;
        }
    }

    //InputButtonをClickした際の処理
    public void onInputButtonClick(View view) {
        Log.i("Logging", "MainActivity onInputButtonClick");

        //fieldBundleインスタンスとcodeBundleインスタンスに値が入っているかチェック
        if(fieldBundle != null && codeBundle != null) {
            Log.i("Logging", "MainActivity onInputButtonClick if");
            //値が入っていればInputActivityに遷移
            Intent intent = new Intent(MainActivity.this, InputActivity.class);
            intent.putExtra("field", fieldBundle);
            intent.putExtra("code", codeBundle);
            startActivity(intent);
        }
        else{
            Log.i("Logging", "MainActivity onInputButtonClick else");
        }
    }

    //StartButtonをClickした際の処理
    public void onStartButtonClick(View view) {
        Log.i("Logging", "MainActivity onStartButtonClick");

        //fieldBundleインスタンスとcodeBundleインスタンスに値が入っているかチェック
        if(fieldBundle != null && codeBundle != null){
            Log.i("Logging", "MainActivity onStartButtonClick if");
            //値が入っていればTodoActivityに遷移
            Intent intent = new Intent(MainActivity.this, TodoActivity.class);
            intent.putExtra("field", fieldBundle);
            intent.putExtra("code", codeBundle);
            startActivity(intent);
        }
        else {
            Log.i("Logging", "MainActivity onStartButtonClick else");
        }

        //Serviceクラスを起動する処理
        Log.i("Logging", "Main onStartButtonClick startService");
        Intent intentService = new Intent(MainActivity.this, FamisukeService.class);
        startService(intentService);
    }



    //非同期でDBからデータを取得するクラス
    private class GetDatasFromDb extends AsyncTask<String, String, String>{

        private String _fieldKey;
        private int _flag = -1;
        String _param;

        @Override
        protected String doInBackground(String... strings) {
            _param = strings[0];
            Log.i("Logging", "MainActivity AsyncTask doInBackground param: " + _param);

            if(_param.equals("field")){
                _fieldKey = "Name";
                _flag = 0;
            }
            else if (_param.equals("code")){
                _fieldKey = "name";
                _flag = 1;
            }

            //DBから取得した圃場データを格納する変数と配列
            String response = null;

            //DBクエリをPOSTするために成形
            HashMap<String, String> jsonInnerValueData = new HashMap<>();
            jsonInnerValueData.put("$ne", "");

            HashMap<String, Object> jsonInnerKeyData = new HashMap<>();
            jsonInnerKeyData.put("_id", jsonInnerValueData);

            HashMap<String, Object> jsonMiddleData = new HashMap<>();
            jsonMiddleData.put("selector", jsonInnerKeyData);

            if (_flag == 1){
                jsonInnerValueData = new HashMap<>();
                jsonInnerValueData.put("$eq", "1");

                jsonInnerKeyData = new HashMap<>();
                jsonInnerKeyData.put("KBN_code", jsonInnerValueData);

                jsonMiddleData.put("selector", jsonInnerKeyData);
            }

            ArrayList<String> jsonInnerArrayData = new ArrayList<>();
            jsonInnerArrayData.add(_fieldKey);

            jsonMiddleData.put("fields", jsonInnerArrayData);

            HashMap<String, Object> jsonOuterData = new HashMap<>();
            jsonOuterData.put("table", _param);
            jsonOuterData.put("body", jsonMiddleData);

            //APサーバーのURLを変数に格納
            String urlStr = "https://sakuranbo-mekaru2.mybluemix.net/common/android_find_cloudant";
            Log.i("Logging", "MainActivity AsyncTask doInBackground urlStr: " + urlStr);

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
                    Log.i("Logging", "MainActivity AsyncTask doInBackground if");
                    JSONObject jsonObject = new JSONObject(jsonOuterData);
                    String jsonText = jsonObject.toString();
                    Log.i("Logging", "MainActivity AsyncTask doInBackground jsonText: " + jsonText);
                    PrintStream printStream = new PrintStream(con.getOutputStream());
                    //PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonText);
                    printStream.close();
                }
                outputStream.close();

                final int status = con.getResponseCode();
                if(status == 200){
                    Log.i("Logging", "MainActivity doInBackground response: " + status);
                }
                else {
                    Log.i("Logging", "MainActivity doInBackground error: " + status);
                }

                inputStream = con.getInputStream();
                response = is2String(inputStream);
                Log.i("Logging", "MainActivity doInBackground response: " + response);
            }
            catch (ProtocolException e) {
                Log.i("Logging", "MainActivity AsyncTask doInBackground ProtocolException");
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                Log.i("Logging", "MainActivity AsyncTask doInBackground MalformedURLException");
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.i("Logging", "MainActivity AsyncTask doInBackground IOException");
                e.printStackTrace();
            }
            finally {
                if(con != null){
                    Log.i("Logging", "MainActivity doInBackground finally");
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
            Log.i("Logging", "MainActivity doPostExecute response: " + response);

            try{
                JSONObject rootJson = new JSONObject(response);
                JSONArray datasJson = rootJson.getJSONArray("data");

                ArrayList<String> fields = new ArrayList<>();
                int len = datasJson.length();
                Log.i("Logging", "MainActivity doPostExecute len: " + len);

                for(int i=0; i<len; i++){
                    JSONObject dataJson = datasJson.getJSONObject(i);
                    String data = "";
                    data = dataJson.getString(_fieldKey);
                    Log.i("Logging", "MainActivity doPostExecute data: " + data);
                    fields.add(data);
                }

                if(_param == "field"){
                    Log.i("Logging", "MainActivity doPostExecute _param==field");
                    fieldBundle = new Bundle();
                    fieldBundle.putStringArrayList(_param, fields);
                }
                else if(_param == "code"){
                    Log.i("Logging", "MainActivity doPostExecute _param==code");
                    codeBundle = new Bundle();
                    codeBundle.putStringArrayList(_param, fields);
                }
            }
            catch (JSONException e) {
                Log.i("Logging", "MainActivity doPostExecute JSONException");
                e.printStackTrace();
            }
        }
    }
}
