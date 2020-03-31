package com.example.famisukeapplication;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Activityからの情報を受け取る処理
        Bundle bundle = getArguments();
        String activity = bundle.getString("activity");
        Log.i("Logging", "TimePickerFragment Argument: " + activity);

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if(activity.equals("Input")){
            return new TimePickerDialog(getActivity(), (InputActivity)getActivity(), hour, minute, true);
        }
        else if(activity.equals("Edit")){
            return new TimePickerDialog(getActivity(), (EditActivity)getActivity(), hour, minute, true);
        }
        else {
            return null;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
