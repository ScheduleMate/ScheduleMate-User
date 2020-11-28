package com.schedulemate.schedulemate_user.ui.timetable.registerSubject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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

        Button buttonRegister = view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(buttonRegisterOnClickListener);

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
            /*Spinner spinner = new Spinner(getContext());
            ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.days, android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            TableRow.LayoutParams layoutParamsSpinner = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParamsSpinner.rightMargin = 10;
            layoutParamsSpinner.weight = 2;
            spinner.setLayoutParams(layoutParamsSpinner);
            spinner.setPadding(5, 5, 5, 5);

            TableRow.LayoutParams layoutParamsTextViewTime = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParamsTextViewTime.weight = 3;

            TextView textViewStart = new TextView(getContext());
            textViewStart.setLayoutParams(layoutParamsTextViewTime);
            textViewStart.setText("9 : 00");
            textViewStart.setBackgroundResource(R.drawable.edit_text_border);
            textViewStart.setPadding(5, 5, 5, 5);
            textViewStart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewStart.setTextColor(Color.BLACK);
            textViewStart.setOnClickListener(textViewOnClickListener);

            TextView textView = new TextView(getContext());
            textView.setText(" ~ ");
            TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParamsTextView.weight = 1;
            textView.setLayoutParams(layoutParamsTextView);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(Color.BLACK);

            TextView textViewEnd = new TextView(getContext());
            textViewEnd.setLayoutParams(layoutParamsTextViewTime);
            textViewEnd.setText("10 : 00");
            textViewEnd.setBackgroundResource(R.drawable.edit_text_border);
            textViewEnd.setPadding(5, 5, 5, 5);
            textViewEnd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewEnd.setTextColor(Color.BLACK);
            textViewEnd.setOnClickListener(textViewOnClickListener);

            LinearLayout linearLayoutTime = new LinearLayout(getContext());
            TableRow.LayoutParams layoutParamsTime = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParamsTime.weight = 6;
            layoutParamsTime.gravity = Gravity.CENTER;
            linearLayoutTime.setLayoutParams(layoutParamsTime);
            linearLayoutTime.setBackgroundResource(R.drawable.class_detail_cell_border);
            linearLayoutTime.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutTime.setPadding(5, 5, 5, 5);

            linearLayoutTime.addView(spinner);
            linearLayoutTime.addView(textViewStart);
            linearLayoutTime.addView(textView);
            linearLayoutTime.addView(textViewEnd);

            EditText editTextPlace = new EditText(getContext());
            TableRow.LayoutParams layoutParamsEditTextPlace = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            layoutParamsEditTextPlace.weight = 1;
            editTextPlace.setLayoutParams(layoutParamsEditTextPlace);
            editTextPlace.setBackgroundResource(R.drawable.edit_text_border);
            editTextPlace.setEms(10);
            editTextPlace.setPadding(5, 5, 5, 5);

            LinearLayout linearLayoutPlace = new LinearLayout(getContext());
            TableRow.LayoutParams layoutParamsPlace = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
            layoutParamsPlace.weight = 3;
            linearLayoutPlace.setLayoutParams(layoutParamsPlace);
            linearLayoutPlace.setBackgroundResource(R.drawable.class_detail_cell_border);
            linearLayoutPlace.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutPlace.setPadding(5, 5, 5, 5);
            linearLayoutPlace.addView(editTextPlace);

            ImageButton imageButtonDelete = new ImageButton(getContext());
            TableRow.LayoutParams layoutParamsButtonDelete = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            layoutParamsButtonDelete.weight = 1;
            imageButtonDelete.setLayoutParams(layoutParamsButtonDelete);
            imageButtonDelete.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
            imageButtonDelete.setBackgroundColor(Color.WHITE);

            LinearLayout linearLayoutDelete = new LinearLayout(getContext());
            TableRow.LayoutParams layoutParamsDelete = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
            layoutParamsDelete.weight = 1;
            linearLayoutDelete.setLayoutParams(layoutParamsDelete);
            linearLayoutDelete.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutDelete.addView(imageButtonDelete);

            TableRow tableRow = new TableRow(getContext());
            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
            tableRow.setLayoutParams(layoutParams);
            tableRow.addView(linearLayoutTime);
            tableRow.addView(linearLayoutPlace);
            tableRow.addView(linearLayoutDelete);

            tableLayoutTime.addView(tableRow);*/
        }
    };

    View.OnClickListener buttonRegisterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editTextTitle = view.findViewById(R.id.editTextTitle);
            EditText editTextProfessor = view.findViewById(R.id.editTextProfessor);

            if(editTextTitle.getText().toString().trim() != ""){
                DatabaseReference dr = sharedViewModel.getTimetableDR().child(timetableViewModel.getSemester()).push();
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
    };
}