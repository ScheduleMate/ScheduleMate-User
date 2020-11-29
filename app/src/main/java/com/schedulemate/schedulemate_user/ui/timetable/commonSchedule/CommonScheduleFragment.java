package com.schedulemate.schedulemate_user.ui.timetable.commonSchedule;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;
import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.RegisterSubject;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

        for (String key : subjectItem.classes.keySet()) {
            if (subjectItem.classes.get(key).equals(subjectItem.classes.get(classInfo.getClassId()))) {
                String classId = key;
                textViewProfessor.setText(classInfo.getProfessor());
                textViewType.setText(classInfo.getType());

                for (RegisterSubject.Time time : classInfo.getTimes()) {
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

                    textViewTime.setText(time.getDay() + " " + time.getStart() + " ~ " + time.getEnd());
                    textViewPlace.setText(time.getPlace());

                    row.addView(textViewTime);
                    row.addView(textViewPlace);
                    tableLayoutTime.addView(row);
                }
            }
        }

        LinearLayout linearLayoutClassSchedule = view.findViewById(R.id.linearLayoutClassSchedule);
        if (classInfo.getMidExam() != null) {
            View examView = getLayoutInflater().inflate(R.layout.fragment_exam_schedule, null);
            ((TextView) examView.findViewById(R.id.textViewScheduleType)).setText("<중간고사>");
            ((TextView) examView.findViewById(R.id.textViewExamTime)).setText(classInfo.getMidExam().getStartTime() + " ~ " + classInfo.getMidExam().getEndTime());
            ((TextView) examView.findViewById(R.id.textViewExamPlace)).setText(classInfo.getMidExam().getPlace());
            ((ImageButton)examView.findViewById(R.id.imageButtonReport)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = getLayoutInflater().inflate(R.layout.report_dialog_layout, null);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("일정 신고").setMessage("이 일정을 신고하시겠습니까?").setView(view).setPositiveButton("신고", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> info = new HashMap<>();
                            info.put("classKey", classInfo.getClassId());
                            info.put("classTitle", classInfo.getSubjectTitle());
                            info.put("declareTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                            info.put("reason", ((EditText)view.findViewById(R.id.editTextReport)).getText().toString());
                            info.put("registrant", classInfo.getMidExam().getRegistrant());
                            info.put("semester", timetableViewModel.getSemester());

                            FirebaseDatabase.getInstance().getReference(sharedViewModel.getUniversity()).child("declare").child("schedule").child("midExam").child(classInfo.getMidExam().getRegisteredTime()).setValue(info, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getContext());
                                    builder1.setMessage("신고 완료되었습니다.").setPositiveButton("확인", null);
                                    builder1.show();
                                }
                            });
                        }
                    }).setNegativeButton("취소", null);
                    builder.show();
                }
            });
            linearLayoutClassSchedule.addView(examView);
        }

        if (classInfo.getFinalExam() != null) {
            View examView = getLayoutInflater().inflate(R.layout.fragment_exam_schedule, null);
            ((TextView) examView.findViewById(R.id.textViewScheduleType)).setText("<기말고사>");
            ((TextView) examView.findViewById(R.id.textViewExamTime)).setText(classInfo.getFinalExam().getStartTime() + " ~ " + classInfo.getFinalExam().getEndTime());
            ((TextView) examView.findViewById(R.id.textViewExamPlace)).setText(classInfo.getFinalExam().getPlace());
            ((ImageButton)examView.findViewById(R.id.imageButtonReport)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("일정 신고").setMessage("이 일정을 신고하시겠습니까?").setPositiveButton("신고", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> info = new HashMap<>();
                            info.put("classKey", classInfo.getClassId());
                            info.put("classTitle", classInfo.getSubjectTitle());
                            info.put("declareTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                            info.put("registrant", classInfo.getFinalExam().getRegistrant());
                            info.put("semester", timetableViewModel.getSemester());

                            FirebaseDatabase.getInstance().getReference(sharedViewModel.getUniversity()).child("declare").child("schedule").child("finalExam").child(classInfo.getFinalExam().getRegisteredTime()).setValue(info, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getContext());
                                    builder1.setMessage("신고 완료되었습니다.").setPositiveButton("확인", null);
                                    builder1.show();
                                }
                            });
                        }
                    }).setNegativeButton("취소", null);
                    builder.show();
                }
            });
            linearLayoutClassSchedule.addView(examView);
        }

        if (classInfo.getHomeWorks().size() > 0) {
            View homeWorkView = getLayoutInflater().inflate(R.layout.fragment_homework_schedule, null);
            RecyclerView recyclerViewHomeWork = homeWorkView.findViewById(R.id.recyclerViewHomework);
            recyclerViewHomeWork.setLayoutManager(new LinearLayoutManager(getContext()));
            HomeWorkRecyclerViewAdapter adapter = new HomeWorkRecyclerViewAdapter(classInfo.getHomeWorks(), sharedViewModel.getUser(), timetableViewModel.getSemester(), classInfo.getClassId(), classInfo.getSubjectTitle(), getContext());
            recyclerViewHomeWork.setAdapter(adapter);

            linearLayoutClassSchedule.addView(homeWorkView);

            timetableViewModel.getClassGroupList().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
                @Override
                public void onChanged(ArrayList arrayList) {
                    adapter.notifyDataSetChanged();
                }
            });
        }

        ((Button) view.findViewById(R.id.buttonEdit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                builder.setItems(R.array.classScheduleType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                            case 1:
                                CommonScheduleFragmentDirections.ActionCommonScheduleFragmentToEditCommonScheduleFragment action =
                                        CommonScheduleFragmentDirections.actionCommonScheduleFragmentToEditCommonScheduleFragment(classInfo, which, subjectItem);
                                Navigation.findNavController(view).navigate(action);
                                break;
                            case 2:
                                CommonScheduleFragmentDirections.ActionCommonScheduleFragmentToAddHomeWorkFragment2 action2 =
                                        CommonScheduleFragmentDirections.actionCommonScheduleFragmentToAddHomeWorkFragment2(subjectItem, classInfo);
                                Navigation.findNavController(view).navigate(action2);
                        }
                    }
                }).show();

            }
        });

        return view;
    }
}