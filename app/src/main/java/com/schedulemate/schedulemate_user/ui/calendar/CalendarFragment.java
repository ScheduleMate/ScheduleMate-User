package com.schedulemate.schedulemate_user.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.schedulemate.schedulemate_user.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarFragment extends Fragment {
    private CalendarDecorator.Schedule scheduleDecorator;
    private CalendarViewModel calendarViewModel;
    private ArrayList<Schedule> schedules;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        MaterialCalendarView calendarView = root.findViewById(R.id.calendarView);
        TextView textViewDate = root.findViewById(R.id.textViewDate);
        ListView listViewSchedule = root.findViewById(R.id.listViewSchedule);

        CalendarDay today = CalendarDay.today();
        calendarViewModel.setMonthSchedule(String.format("%04d-%02d", today.getYear(), today.getMonth() + 1));

        schedules = calendarViewModel.getMonthSchedule().getValue().containsKey(String.valueOf(today.getDay())) ?
                new ArrayList<>((ArrayList<Schedule>)calendarViewModel.getMonthSchedule().getValue().get(String.valueOf(today.getDay()))) : new ArrayList<>();
        CalendarArrayAdapter adapter = new CalendarArrayAdapter(getContext(), schedules);
        listViewSchedule.setAdapter(adapter);

        scheduleDecorator = new CalendarDecorator.Schedule(calendarViewModel.getMonthSchedule().getValue());

        calendarView.setSelectedDate(today);
        calendarView.addDecorators(
                new CalendarDecorator.Saturday(),
                new CalendarDecorator.Sunday(),
                new CalendarDecorator.Today(),
                scheduleDecorator
        );
        calendarViewModel.getMonthSchedule().observe(getViewLifecycleOwner(), new Observer<HashMap>() {
            @Override
            public void onChanged(HashMap hashMap) {
                calendarView.removeDecorator(scheduleDecorator);
                scheduleDecorator = new CalendarDecorator.Schedule(hashMap);
                calendarView.addDecorator(scheduleDecorator);
                schedules.clear();
                if (calendarViewModel.getMonthSchedule().getValue().containsKey(String.valueOf(calendarView.getSelectedDate().getDay())))
                    schedules.addAll((ArrayList<Schedule>)calendarViewModel.getMonthSchedule().getValue().get(String.valueOf(calendarView.getSelectedDate().getDay())));
                adapter.notifyDataSetChanged();
            }
        });

        textViewDate.setText(String.format("%04d년 %02d월 %02d일", today.getYear(), today.getMonth() + 1, today.getDay()));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                textViewDate.setText(String.format("%04d년 %02d월 %02d일", date.getYear(), date.getMonth() + 1, date.getDay()));
                schedules.clear();
                if (calendarViewModel.getMonthSchedule().getValue().containsKey(String.valueOf(date.getDay())))
                    schedules.addAll((ArrayList<Schedule>)calendarViewModel.getMonthSchedule().getValue().get(String.valueOf(date.getDay())));
                adapter.notifyDataSetChanged();
            }
        });

        root.findViewById(R.id.buttonAddSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarFragmentDirections.ActionNavCalendarToScheduleFragment action = CalendarFragmentDirections.actionNavCalendarToScheduleFragment(textViewDate.getText().toString());
                Navigation.findNavController(root).navigate(action);
            }
        });

        listViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarFragmentDirections.ActionNavCalendarToScheduleFragment action = CalendarFragmentDirections.actionNavCalendarToScheduleFragment(textViewDate.getText().toString());
                action.setSchedule(schedules.get(position));
                Navigation.findNavController(root).navigate(action);
            }
        });

        return root;
    }
}