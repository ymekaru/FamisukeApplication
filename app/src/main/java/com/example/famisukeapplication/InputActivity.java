package com.example.famisukeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InputActivity extends AppCompatActivity {

    EditText etDate, etWorks, etField, etFromDate, etToDate, etDetail;
    TextView tvDate, tvWorks, tvField, tvFromDate, tvToDate, tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Confirm", "Input onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //EditTextとTextViewの画面部品毎のオブジェクトを作成し変数に格納
        etDate = findViewById(R.id.etInputDate);
        etWorks = findViewById(R.id.etInputWork);
        etField = findViewById(R.id.etInputField);
        etFromDate = findViewById(R.id.etInputFromDate);
        etToDate = findViewById(R.id.etInputToDate);
        etDetail = findViewById(R.id.etInputDetail);

        tvDate = findViewById(R.id.tvDisplayDate);
        tvWorks = findViewById(R.id.tvDisplayWork);
        tvField = findViewById(R.id.tvDisplayField);
        tvFromDate = findViewById(R.id.tvDisplayFromDate);
        tvToDate = findViewById(R.id.tvDisplayToDate);
        tvDetail = findViewById(R.id.tvDisplayDetail);

        //EditTextオブジェクトにリスナを設定
        etDate.addTextChangedListener(new GenericTextwatcher(etDate));
        etWorks.addTextChangedListener(new GenericTextwatcher(etWorks));
        etField.addTextChangedListener(new GenericTextwatcher(etField));
        etFromDate.addTextChangedListener(new GenericTextwatcher(etFromDate));
        etToDate.addTextChangedListener(new GenericTextwatcher(etToDate));
        etDetail.addTextChangedListener(new GenericTextwatcher(etDetail));
    }


    //TextWatcherでEditTextの記入を監視する処理
    private class GenericTextwatcher implements TextWatcher {

        private View view;

        private GenericTextwatcher(View view){
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("Confirm", "Input GenericTextwatcher afterTextChanged");
            String inputWord = s.toString();

            //入力されている画面部品のidを取得し変数に格納
            int id = view.getId();

            switch (id){
                case R.id.etInputDate:
                    tvDate.setText(inputWord);
                    break;

                case R.id.etInputWork:
                    tvWorks.setText(inputWord);
                    break;

                case R.id.etInputField:
                    tvField.setText(inputWord);
                    break;

                case R.id.etInputFromDate:
                    tvFromDate.setText(inputWord);
                    break;

                case R.id.etInputToDate:
                    tvToDate.setText(inputWord);
                    break;

                case R.id.etInputDetail:
                    tvDetail.setText(inputWord);
                    break;

                default:
                    break;
            }
        }
    }


    //inputButtonClick
    public void onResisterButtonClick(View view){
        Log.i("Confirm", "Input onResisterButtonClick");
        //ToDo  Send datas to Web Server to POST datas on DataBase
    }


    //backButtonClick
    public void onBackButtonClick(View view){
        Log.i("Confirm", "Input onBackButtonClick");
        finish();
    }
}
