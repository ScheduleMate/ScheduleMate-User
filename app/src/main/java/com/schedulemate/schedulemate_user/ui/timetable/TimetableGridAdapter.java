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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TimetableGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater lf;
    private ArrayList<Integer> layouts;
    private ArrayList<TimetableCell> timetableCells;
    private int startTime;
    private String[] days;
    private Pattern pattern = Pattern.compile("([0-9]{2}):([0-9]{2})");

    public TimetableGridAdapter(Context context, ArrayList<Integer> layouts, ArrayList<TimetableCell> timetableCells, int startTime) {
        this.context = context;
        this.layouts = layouts;
        this.timetableCells = timetableCells;
        lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.startTime = startTime;
        days = context.getResources().getStringArray(R.array.days);
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

        int time = startTime + (position / 6);

        if (position % 6 == 0){
            ((TextView)convertView.findViewById(R.id.textViewTime)).setText(String.format("%02d:00", time));
        }
        else{
            for(TimetableCell timetableCell : timetableCells){
                if(days[position % 6].equals(timetableCell.getDay())){
                    Matcher matcherStart = pattern.matcher(timetableCell.getStart());
                    Matcher matcherEnd = pattern.matcher(timetableCell.getEnd());

                    if(matcherStart.find() && matcherEnd.find() && Integer.parseInt(matcherStart.group(1)) == time){
                        convertView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewAttachedToWindow(View v) {
                                TextView textView = new TextView(context);
                                int size = (Integer.parseInt(matcherEnd.group(1)) - Integer.parseInt(matcherStart.group(1))
                                        - (60 - Integer.parseInt(matcherStart.group(2))) + Integer.parseInt(matcherEnd.group(2)))
                                        * (10 / 6);
                                int textViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size, context.getResources().getDisplayMetrics());
                                textView.setHeight(textViewHeight);
                            }

                            @Override
                            public void onViewDetachedFromWindow(View v) {

                            }
                        });
                    }
                }
            }
        }

        return convertView;
    }
}
