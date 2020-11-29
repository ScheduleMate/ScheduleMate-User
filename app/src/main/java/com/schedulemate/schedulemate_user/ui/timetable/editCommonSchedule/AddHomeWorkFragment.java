package com.schedulemate.schedulemate_user.ui.timetable.editCommonSchedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddHomeWorkFragment extends Fragment {
    private Pattern pattern = Pattern.compile("([0-9]{4})년\\s([0-9]{2})월\\s([0-9]{2})일\\s([0-9]{2}):([0-9]{2})");
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ClassGroup classInfo = AddHomeWorkFragmentArgs.fromBundle(getArguments()).getClassInfo();
        Subject.SubjectItem subjectItem = AddHomeWorkFragmentArgs.fromBundle(getArguments()).getSubjectInfo();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_home_work, container, false);

        TextView textViewTimeLimit = view.findViewById(R.id.textViewTimeLimit);
        EditText editTextContent = view.findViewById(R.id.editTextContent);

        textViewTimeLimit.setText((new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(new Date(System.currentTimeMillis()))));
        textViewTimeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matcher matcher = pattern.matcher(((TextView)v).getText().toString());
                if (matcher.find()){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    ((TextView)v).setText(String.format("%04d년 %02d월 %02d일 %02d:%02d", year,
                                            month + 1, dayOfMonth, hourOfDay, minute));

                                }
                            }, Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), DateFormat.is24HourFormat(getContext()));
                            timePickerDialog.show();
                        }
                    }, Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)));

                    datePickerDialog.show();
                }
            }
        });

        ((Button)view.findViewById(R.id.buttonAdd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference(sharedViewModel.getUniversity()).child(timetableViewModel.getSemester())
                        .child("classInfo").child(classInfo.getClassId()).child("schedule").child("homeWork").child(now);

                dr.child("timeLimit").setValue(textViewTimeLimit.getText().toString());
                dr.child("content").setValue(editTextContent.getText().toString());
                dr.child("registrant").setValue(sharedViewModel.getUserId());

                classInfo.addHomeWorks(new ClassGroup.HomeWork(textViewTimeLimit.getText().toString(),
                        editTextContent.getText().toString(), sharedViewModel.getUserId(), dr.getKey()));

                AddHomeWorkFragmentDirections.ActionAddHomeWorkFragment2ToCommonScheduleFragment action =
                        AddHomeWorkFragmentDirections.actionAddHomeWorkFragment2ToCommonScheduleFragment(subjectItem, classInfo);
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }
}