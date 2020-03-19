package com.example.famisukeapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TestAdapter extends BaseAdapter {

    private LayoutInflater _inflater;
    private int _layoutId;
    private ArrayList<String> _codesList = new ArrayList<>();
    private ArrayList<String> _fieldsList = new ArrayList<>();
    private ArrayList<String> _fromTimesList = new ArrayList<>();
    private ArrayList<String> _toTimesList = new ArrayList<>();
    private ArrayList<String> _detailsList = new ArrayList<>();
    private ArrayList<String> _datesList = new ArrayList<>();
    private ArrayList<String> _idsList = new ArrayList<>();

    static class ViewHolder{
        TextView textCode;
        TextView textField;
        TextView textFromTime;
        TextView textToTime;
        TextView textDetail;
        TextView textDate;
        TextView textId;
    }

    TestAdapter(Context context, int itemLayoutId, ArrayList<String> codes, ArrayList<String> fields,
                ArrayList<String> fromTimes, ArrayList<String> toTimes, ArrayList<String> details,
                ArrayList<String> dates, ArrayList<String> ids){
        Log.i("Confirm", "TestAdapter constructor");
        _inflater = LayoutInflater.from(context);
        _layoutId = itemLayoutId;
        _codesList = codes;
        _fieldsList = fields;
        _fromTimesList = fromTimes;
        _toTimesList = toTimes;
        _detailsList = details;
        _datesList = dates;
        _idsList = ids;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("Confirm", "TestAdapter getView position: " + position);

        ViewHolder holder;

        if(convertView == null){
            convertView = _inflater.inflate(_layoutId, null);
            holder = new ViewHolder();
            holder.textCode = convertView.findViewById(R.id.tvListCode);
            holder.textField = convertView.findViewById(R.id.tvListField);
            holder.textFromTime = convertView.findViewById(R.id.tvListFrom);
            holder.textToTime = convertView.findViewById(R.id.tvListTo);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textCode.setText(_codesList.get(position));
        holder.textField.setText(_fieldsList.get(position));
        holder.textFromTime.setText(_fromTimesList.get(position));
        holder.textToTime.setText(_toTimesList.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        Log.i("Confirm", "TestAdapter getCount()");
        return _codesList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i("Confirm", "TestAdapter getItem position: " + position);
        return position;
    }

    @Override
    public long getItemId(int position) {
        Log.i("Confirm", "TestAdapter getItemId position: " + position);
        return position;
    }
}
