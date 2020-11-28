package com.schedulemate.schedulemate_user.ui.timetable;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;

import java.util.ArrayList;

class TimetableGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater lf;
    private ArrayList<Integer> layouts;
    private int startTime;

    public TimetableGridAdapter(Context context, ArrayList<Integer> layouts, ArrayList<TimetableCell> timetableCells, int startTime) {
        this.context = context;
        this.layouts = layouts;
        lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.startTime = startTime;
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public Object getItem(int position) {
        return layouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = lf.inflate(layouts.get(position), null);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100, context.getResources().getDisplayMetrics());
        convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        if(position % 6 == 0){
            ((TextView)convertView.findViewById(R.id.textViewTime)).setText(String.format("%02d:00", startTime + (position / 6)));
        }
        return convertView;
    }
}
