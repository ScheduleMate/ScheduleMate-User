package com.schedulemate.schedulemate_user.ui.timetable.commonSchedule;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassDetailFragmentArgs;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

public class CommonScheduleFragment extends Fragment {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_common_schedule, container, false);

        Subject.SubjectItem subjectItem = CommonScheduleFragmentArgs.fromBundle(getArguments()).getSubjectInfo();
        ClassGroup classInfo = CommonScheduleFragmentArgs.fromBundle(getArguments()).getClassInfo();

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        TextView textViewDependency = view.findViewById(R.id.textViewDependency);
        TextView textViewClassification = view.findViewById(R.id.textViewClassification);
        TextView textViewClass = view.findViewById(R.id.textViewClass);
        TextView textViewCredit = view.findViewById(R.id.textViewCredit);
        TextView textViewGrade = view.findViewById(R.id.textViewGrade);
        TextView textViewProfessor = view.findViewById(R.id.textViewProfessor);
        TextView textViewType = view.findViewById(R.id.textViewType);
        TableLayout tableLayoutTime = view.findViewById(R.id.tableLayoutTime);

        textViewTitle.setText(subjectItem.title);
        textViewDependency.setText(subjectItem.dependency);
        textViewClassification.setText(subjectItem.classification);
        textViewClass.setText(subjectItem.classes.get(classInfo.getClassId()));
        textViewCredit.setText(subjectItem.credit + " 학점");
        textViewGrade.setText(subjectItem.grade + " 학년");

        for(String key : subjectItem.classes.keySet()) {
            if (subjectItem.classes.get(key).equals(subjectItem.classes.get(classInfo.getClassId()))) {
                String classId = key;

                sharedViewModel.getSemesterDR().child("classInfo").child(classId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        textViewProfessor.setText(snapshot.child("professor").getValue().toString());
                        textViewType.setText(snapshot.child("type").getValue().toString());
                        for (DataSnapshot time : snapshot.child("time").getChildren()) {
                            TableRow row = new TableRow(getContext());

                            TextView textViewTime = new TextView(getContext());
                            TableRow.LayoutParams params1 = new TableRow.LayoutParams();
                            params1.weight = 2;
                            textViewTime.setLayoutParams(params1);
                            textViewTime.setWidth(0);
                            textViewTime.setPadding(5, 5, 5, 5);
                            textViewTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            textViewTime.setTextColor(Color.BLACK);
                            textViewTime.setBackgroundResource(R.drawable.class_detail_cell_border);

                            TextView textViewPlace = new TextView(getContext());
                            TableRow.LayoutParams params2 = new TableRow.LayoutParams();
                            params2.weight = 1;
                            textViewPlace.setLayoutParams(params2);
                            textViewPlace.setWidth(0);
                            textViewPlace.setPadding(5, 5, 5, 5);
                            textViewPlace.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            textViewPlace.setTextColor(Color.BLACK);
                            textViewPlace.setBackgroundResource(R.drawable.class_detail_cell_border);

                            textViewTime.setText(time.child("day").getValue() + " " + time.child("start").getValue() + " ~ " + time.child("end").getValue());
                            textViewPlace.setText(time.child("place").getValue().toString());

                            row.addView(textViewTime);
                            row.addView(textViewPlace);
                            tableLayoutTime.addView(row);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }



        return view;
    }
}