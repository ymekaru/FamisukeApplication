package com.example.famisukeapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Activityからの情報を受け取る処理
        Bundle bundle = getArguments();
        String activity = bundle.getString("activity");
        Log.i("Logging", "DatePickerFragment Argument: " + activity);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if(activity.equals("Input")){
            return new DatePickerDialog(getActivity(), (InputActivity)getActivity(), year, month, day);
        }
        else if(activity.equals("Edit")){
            return new DatePickerDialog(getActivity(), (EditActivity)getActivity(), year, month, day);
        }
        else {
            return null;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
