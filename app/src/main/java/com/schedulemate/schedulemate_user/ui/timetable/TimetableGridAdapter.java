package com.schedulemate.schedulemate_user.ui.timetable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

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
    private View root;
    private ArrayList<Integer> colors = new ArrayList<>();

    public TimetableGridAdapter(Context context, ArrayList<Integer> layouts, ArrayList<TimetableCell> timetableCells, int startTime, View root) {
        this.context = context;
        this.layouts = layouts;
        this.timetableCells = timetableCells;
        lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.startTime = startTime;
        days = context.getResources().getStringArray(R.array.days);
        this.root = root;

        colors.add(Color.parseColor("#FF8E7A"));
        colors.add(Color.parseColor("#C7FF47"));
        colors.add(Color.parseColor("#325FB3"));
        colors.add(Color.parseColor("#8EB33B"));
        colors.add(Color.parseColor("#FFCD61"));
    }

    public void setTimetableCells(ArrayList<TimetableCell> timetableCells) {
        this.timetableCells = timetableCells;
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
            for(int i = 0; i < timetableCells.size(); i++){
                TimetableCell timetableCell = timetableCells.get(i);
                if(days[(position % 6) - 1].equals(timetableCell.getDay())){
                    Matcher matcherStart = pattern.matcher(timetableCell.getStart());
                    Matcher matcherEnd = pattern.matcher(timetableCell.getEnd());

                    if(matcherStart.find() && matcherEnd.find()
                            && ((Integer.parseInt(matcherStart.group(1)) <= time && Integer.parseInt(matcherEnd.group(1)) > time)
                            || (Integer.parseInt(matcherEnd.group(1)) == time && Integer.parseInt(matcherEnd.group(2)) > 0))){
                        LinearLayout linearLayout = convertView.findViewById(R.id.linearLayout);
                        TextView textView = new TextView(context);
                        TextView textViewSub = new TextView(context);
                        textView.setBackgroundColor(colors.get(i % colors.size()));
                        int textViewHeight = 0;

                        if(Integer.parseInt(matcherStart.group(1)) < time && Integer.parseInt(matcherEnd.group(1)) > time){
                            textViewHeight = 60;

                        }
                        else if(Integer.parseInt(matcherStart.group(1)) == time){
                            textViewHeight = 60 - Integer.parseInt(matcherStart.group(2));
                            textView.setText(timetableCell.getTitle());
                        }
                        else if(Integer.parseInt(matcherEnd.group(1)) == time && Integer.parseInt(matcherEnd.group(2)) > 0){
                            textViewHeight = Integer.parseInt(matcherEnd.group(2));
                        }

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                        LinearLayout.LayoutParams layoutParamsSub = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                        layoutParams.weight = textViewHeight;
                        layoutParamsSub.weight = 60-textViewHeight;
                        textView.setLayoutParams(layoutParams);
                        textViewSub.setLayoutParams(layoutParamsSub);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(timetableCell.getRegisterSubject() == null) {
                                    TimetableFragmentDirections.ActionNavTimetableToCommonScheduleFragment action =
                                            TimetableFragmentDirections.actionNavTimetableToCommonScheduleFragment(timetableCell.getSubjectItem(), timetableCell.getClassGroup());
                                    Navigation.findNavController(v).navigate(action);
                                }
                                else{
                                    TimetableFragmentDirections.ActionNavTimetableToRegisterSubjectFragment action =
                                            TimetableFragmentDirections.actionNavTimetableToRegisterSubjectFragment();
                                    action.setRegisterSubject(timetableCell.getRegisterSubject());
                                    Navigation.findNavController(v).navigate(action);
                                }
                            }
                        });

                        if(Integer.parseInt(matcherStart.group(1)) == time){
                            linearLayout.addView(textViewSub);
                            linearLayout.addView(textView);
                        }
                        else{
                            linearLayout.addView(textView);
                            linearLayout.addView(textViewSub);
                        }
                    }
                }
            }
        }

        return convertView;
    }

}
