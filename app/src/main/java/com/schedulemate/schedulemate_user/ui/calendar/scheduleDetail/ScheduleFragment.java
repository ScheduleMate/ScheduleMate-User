package com.schedulemate.schedulemate_user.ui.calendar.scheduleDetail;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.calendar.Schedule;
import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.TimePickerFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private TextView textViewDate;
    private Pattern pattern = Pattern.compile("([0-9]{4})년\\s([0-9]{2})월\\s([0-9]{2})일");
    private String date;
    private Schedule schedule;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        textViewDate = view.findViewById(R.id.textViewDate);
        TextView textViewTimeStart = view.findViewById(R.id.textViewTimeStart);
        TextView textViewTimeEnd = view.findViewById(R.id.textViewTimeEnd);
        EditText editTextMemo = view.findViewById(R.id.editTextMemo);

        date = (ScheduleFragmentArgs.fromBundle(getArguments())).getDate();
        textViewDate.setText(date);

        schedule = (ScheduleFragmentArgs.fromBundle(getArguments()).getSchedule());
        if (schedule != null){
            editTextTitle.setText(schedule.getTitle());
            textViewTimeStart.setText(schedule.getStartTime());
            textViewTimeEnd.setText(schedule.getEndTime());
            editTextMemo.setText(schedule.getMemo());
        }

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matcher matcher = pattern.matcher(((TextView)v).getText().toString());
                while (matcher.find()){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener,
                            Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)));
                    datePickerDialog.show();
                }
            }
        });

        textViewTimeStart.setOnClickListener(textViewOnClickListener);
        textViewTimeEnd.setOnClickListener(textViewOnClickListener);

        view.findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextTitle.getText().toString().trim().equals("")) {
                    if (schedule != null) {
                        Matcher matcher = pattern.matcher(date);
                        while (matcher.find()) {
                            sharedViewModel.getCalendarDR().child(matcher.group(1) + "-" + matcher.group(2)).child(matcher.group(3)).child(schedule.getId()).removeValue();
                        }

                    }
                    Matcher matcher = pattern.matcher(textViewDate.getText().toString());
                    while (matcher.find()) {
                        DatabaseReference dr = sharedViewModel.getCalendarDR().child(matcher.group(1) + "-" + matcher.group(2)).child(matcher.group(3)).push();
                        dr.child("title").setValue(editTextTitle.getText().toString());
                        dr.child("startTime").setValue(textViewTimeStart.getText().toString());
                        dr.child("endTime").setValue(textViewTimeEnd.getText().toString());
                        dr.child("memo").setValue(editTextMemo.getText().toString());
                    }
                    Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_to_nav_calendar);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            textViewDate.setText(String.format("%04d년 %02d월 %02d일", year, month + 1, dayOfMonth));
        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerFragment timePickerFragment = new TimePickerFragment((TextView)v);
            timePickerFragment.show(getActivity().getSupportFragmentManager(), "time");
        }
    };
}