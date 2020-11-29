package com.schedulemate.schedulemate_user.ui.timetable.editCommonSchedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.TimePickerFragment;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCommonScheduleFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;
    private Pattern pattern = Pattern.compile("([0-9]{4})년\\s([0-9]{2})월\\s([0-9]{2})일\\s([0-9]{2}):([0-9]{2})");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ClassGroup classInfo = EditCommonScheduleFragmentArgs.fromBundle(getArguments()).getClassInfo();
        int type = EditCommonScheduleFragmentArgs.fromBundle(getArguments()).getType();
        Subject.SubjectItem subjectItem = EditCommonScheduleFragmentArgs.fromBundle(getArguments()).getSubjectInfo();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_common_schedule, container, false);

        TextView textViewType = view.findViewById(R.id.textViewType);
        TextView textViewDateStart = view.findViewById(R.id.textViewDateStart);
        TextView textViewDateEnd = view.findViewById(R.id.textViewDateEnd);
        EditText editTextPlace = view.findViewById(R.id.editTextPlace);

        Calendar calendar = Calendar.getInstance();
        Date now = new Date(System.currentTimeMillis());
        calendar.setTime(now);
        calendar.add(Calendar.HOUR, 1);

        textViewType.setText("<" + getResources().getStringArray(R.array.classScheduleType)[type] + ">");
        textViewDateStart.setOnClickListener(onClickListener);
        textViewDateEnd.setOnClickListener(onClickListener);

        if(classInfo.getMidExam() != null){
            textViewDateStart.setText(classInfo.getMidExam().getStartTime());
            textViewDateEnd.setText(classInfo.getMidExam().getEndTime());
            editTextPlace.setText(classInfo.getMidExam().getPlace());
        }
        else {
            textViewDateStart.setText((new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(now)));
            textViewDateEnd.setText((new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(calendar.getTime())));
        }
        ((Button)view.findViewById(R.id.buttonEdit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                DatabaseReference dr;
                switch (type){
                    case 0:
                        dr = FirebaseDatabase.getInstance().getReference(sharedViewModel.getUniversity()).child(timetableViewModel.getSemester())
                                .child("classInfo").child(classInfo.getClassId()).child("schedule").child("midExam").child(now);
                        break;
                    case 1:
                        dr = FirebaseDatabase.getInstance().getReference(sharedViewModel.getUniversity()).child(timetableViewModel.getSemester())
                                .child("classInfo").child(classInfo.getClassId()).child("schedule").child("finalExam").child(now);
                        break;
                    default:
                        dr = FirebaseDatabase.getInstance().getReference("error");
                        break;
                }

                dr.child("startTime").setValue(textViewDateStart.getText().toString());
                dr.child("endTime").setValue(textViewDateEnd.getText().toString());
                dr.child("place").setValue(editTextPlace.getText().toString());
                dr.child("registrant").setValue(sharedViewModel.getUserId());

                switch (type){
                    case 0:
                        classInfo.setMidExam(new ClassGroup.Exam(textViewDateStart.getText().toString(), textViewDateEnd.getText().toString(),
                                editTextPlace.getText().toString(), sharedViewModel.getUserId(), dr.getKey()));
                        break;
                    case 1:
                        classInfo.setFinalExam(new ClassGroup.Exam(textViewDateStart.getText().toString(), textViewDateEnd.getText().toString(),
                                editTextPlace.getText().toString(), sharedViewModel.getUserId(), dr.getKey()));
                        break;
                }

                EditCommonScheduleFragmentDirections.ActionEditCommonScheduleFragmentToCommonScheduleFragment action =
                        EditCommonScheduleFragmentDirections.actionEditCommonScheduleFragmentToCommonScheduleFragment(subjectItem, classInfo);
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
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
    };
}