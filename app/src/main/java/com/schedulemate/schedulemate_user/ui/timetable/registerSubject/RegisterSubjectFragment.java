package com.schedulemate.schedulemate_user.ui.timetable.registerSubject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RegisterSubjectFragment extends Fragment {
    private TableLayout tableLayoutTime;
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_subject, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);

        RegisterSubject registerSubject = RegisterSubjectFragmentArgs.fromBundle(getArguments()).getRegisterSubject();


        Spinner spinnerDay = view.findViewById(R.id.spinnerDay);
        TextView textViewStart = view.findViewById(R.id.textViewStart);
        TextView textViewEnd = view.findViewById(R.id.textViewEnd);

        textViewStart.setOnClickListener(textViewOnClickListener);
        textViewEnd.setOnClickListener(textViewOnClickListener);

        tableLayoutTime = view.findViewById(R.id.tableLayoutTime);
        ImageButton imageButtonDelete = view.findViewById(R.id.imageButtonDelete);
        ImageButton imageButtonAdd = view.findViewById(R.id.imageButtonAdd);

        imageButtonDelete.setOnClickListener(deleteButtonOnClickListener);
        imageButtonAdd.setOnClickListener(addButtonOnClickListener);

        if(registerSubject != null){
            EditText editTextTitle = view.findViewById(R.id.editTextTitle);
            EditText editTextProfessor = view.findViewById(R.id.editTextProfessor);
            editTextTitle.setText(registerSubject.getTitle());
            editTextProfessor.setText(registerSubject.getProfessor());

            ArrayList<String> arrays = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.days)));
            int index = (arrays.indexOf(registerSubject.getTimes().get(0).getDay()));
            Log.d("checkIndex", String.valueOf(index));
            spinnerDay.setSelection(index);
            textViewStart.setText(registerSubject.getTimes().get(0).getStart());
            textViewEnd.setText(registerSubject.getTimes().get(0).getEnd());
            for(int i = 1; i < registerSubject.getTimes().size(); i++){
                TableRow tableRow = (TableRow)LayoutInflater.from(getContext()).inflate(R.layout.register_subject_fragment__time_table_row, tableLayoutTime, false);

                Spinner newSpinnerDay = tableRow.findViewById(R.id.spinnerDay);
                TextView newTextViewStart = tableRow.findViewById(R.id.textViewStart);
                TextView newTextViewEnd = tableRow.findViewById(R.id.textViewEnd);

                newSpinnerDay.setSelection(arrays.indexOf(registerSubject.getTimes().get(i).getDay()));
                newTextViewStart.setText(registerSubject.getTimes().get(i).getStart());
                newTextViewEnd.setText(registerSubject.getTimes().get(i).getEnd());

                newTextViewStart.setOnClickListener(textViewOnClickListener);
                newTextViewEnd.setOnClickListener(textViewOnClickListener);
                tableRow.findViewById(R.id.imageButtonDelete).setOnClickListener(deleteButtonOnClickListener);

                tableLayoutTime.addView(tableRow);
            }
        }

        Button buttonRegister = view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextTitle = view.findViewById(R.id.editTextTitle);
                EditText editTextProfessor = view.findViewById(R.id.editTextProfessor);

                if(editTextTitle.getText().toString().trim() != ""){
                    DatabaseReference dr;
                    if(registerSubject != null){
                        dr = sharedViewModel.getTimetableDR().child(timetableViewModel.getSemester()).child(registerSubject.getId());
                    }
                    else {
                        dr = sharedViewModel.getTimetableDR().child(timetableViewModel.getSemester()).push();
                    }
                    dr.child("title").setValue(editTextTitle.getText().toString());
                    dr.child("professor").setValue(editTextProfessor.getText().toString());
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

                    for(int i = 1; i < tableLayoutTime.getChildCount(); i++){
                        TableRow tableRow = (TableRow)tableLayoutTime.getChildAt(i);
                        Spinner spinnerDay = tableRow.findViewById(R.id.spinnerDay);
                        TextView textViewStart = tableRow.findViewById(R.id.textViewStart);
                        TextView textViewEnd = tableRow.findViewById(R.id.textViewEnd);
                        EditText editTextPlace = tableRow.findViewById(R.id.editTextPlace);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("day", spinnerDay.getSelectedItem().toString());
                        hashMap.put("start", textViewStart.getText().toString());
                        hashMap.put("end", textViewEnd.getText().toString());
                        hashMap.put("place", editTextPlace.getText().toString());

                        arrayList.add(hashMap);
                    }
                        dr.child("time").setValue(arrayList);

                    Navigation.findNavController(v).navigate(R.id.action_registerSubjectFragment_to_nav_timetable);
                }
            }
        });

        return view;
    }

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerFragment timePickerFragment = new TimePickerFragment((TextView)v);
            timePickerFragment.show(getActivity().getSupportFragmentManager(), "time");
        }
    };

    View.OnClickListener deleteButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tableLayoutTime.removeView((TableRow)v.getParent().getParent());
        }
    };

    View.OnClickListener addButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow tableRow = (TableRow)LayoutInflater.from(getContext()).inflate(R.layout.register_subject_fragment__time_table_row, tableLayoutTime, false);

            tableRow.findViewById(R.id.textViewStart).setOnClickListener(textViewOnClickListener);
            tableRow.findViewById(R.id.textViewEnd).setOnClickListener(textViewOnClickListener);
            tableRow.findViewById(R.id.imageButtonDelete).setOnClickListener(deleteButtonOnClickListener);

            tableLayoutTime.addView(tableRow);
        }
    };
}