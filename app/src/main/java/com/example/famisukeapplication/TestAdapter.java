package com.example.famisukeapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutId;
    private String[] worksList;
    private String[] fieldList;
    private String[] fromDateList;
    private String[] toDateList;

    static class ViewHolder{
        TextView textWork;
        TextView textField;
        TextView textFromDate;
        TextView textToDate;
    }

    TestAdapter(Context context, int itemLayoutId, String[] works, String[] fields,
                String[] fromDates, String[] toDates){
        Log.i("Confirm", "TestAdapter constructor");
        inflater = LayoutInflater.from(context);
        layoutId = itemLayoutId;
        worksList = works;
        fieldList = fields;
        fromDateList = fromDates;
        toDateList = toDates;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("Confirm", "TestAdapter getView position: " + position);

        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(layoutId, null);
            holder = new ViewHolder();
            holder.textWork = convertView.findViewById(R.id.tvListContent);
            holder.textField = convertView.findViewById(R.id.tvListField);
            holder.textFromDate = convertView.findViewById(R.id.tvListFrom);
            holder.textToDate = convertView.findViewById(R.id.tvListTo);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textWork.setText(worksList[position]);
        holder.textField.setText(fieldList[position]);
        holder.textFromDate.setText(fromDateList[position]);
        holder.textToDate.setText(toDateList[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        Log.i("Confirm", "TestAdapter getCount()");
        return worksList.length;
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
