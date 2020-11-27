package com.schedulemate.schedulemate_user.ui.timetable.timetableList;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;

import java.util.ArrayList;

public class TimetableListFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TimetableListFragment newInstance(int columnCount) {
        TimetableListFragment fragment = new TimetableListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);

        timetableViewModel.setTimetableList(sharedViewModel.getTimetableDR());
        sharedViewModel.setSemesterList(sharedViewModel.getSemesterInfoDR());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable_list_list, container, false);

        setHasOptionsMenu(true);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new TimetableRecyclerViewAdapter(timetableViewModel.getTimetableList().getValue()));
            timetableViewModel.getTimetableList().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
                @Override
                public void onChanged(ArrayList arrayList) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.timetable_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addTimetable:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogLayout = getLayoutInflater().inflate(R.layout.timetable_list_fragment__semester_dialog_layout, null);

                builder.setTitle("새 시간표 만들기");
                builder.setMessage("새로 만들 시간표의 학기를 선택해주세요");

                Spinner spinner = dialogLayout.findViewById(R.id.spinnerSemester);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sharedViewModel.getSemesterList().getValue());
                spinner.setAdapter(arrayAdapter);
                builder.setView(dialogLayout);

                sharedViewModel.getSemesterList().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
                    @Override
                    public void onChanged(ArrayList arrayList) {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });

                builder.setPositiveButton("만들기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedViewModel.getTimetableDR().child(spinner.getSelectedItem().toString()).setValue("true");

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("취소", null);

                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}