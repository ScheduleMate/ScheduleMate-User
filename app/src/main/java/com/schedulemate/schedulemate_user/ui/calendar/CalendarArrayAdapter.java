package com.schedulemate.schedulemate_user.ui.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.schedulemate.schedulemate_user.R;

import java.util.ArrayList;

public class CalendarArrayAdapter extends ArrayAdapter<CalendarDecorator.Schedule> {
    private ArrayList<Schedule> schedules;
    Context context;

    public CalendarArrayAdapter(@NonNull Context context, ArrayList<Schedule> schedules) {
        super(context, 0);
        this.schedules = schedules;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.calendar_fragment__listview_item, null);

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        textViewTitle.setText(schedules.get(position).getTitle());
        Log.d("checkAdapter", schedules.get(position).getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return schedules != null ? schedules.size() : 0;
    }
}
