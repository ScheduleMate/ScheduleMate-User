package com.schedulemate.schedulemate_user.ui.timetable.subjectList;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class SubjectListFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private Subject subject;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SubjectListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SubjectListFragment newInstance(int columnCount) {
        SubjectListFragment fragment = new SubjectListFragment();
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
        timetableViewModel.setSubjectList(sharedViewModel.getUniversity(), "2020-2");

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_list_list, container, false);

        View header = inflater.inflate(R.layout.fragment_subject_list_header, null, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new SubjectRecyclerViewAdapter(timetableViewModel.getSubjectList().getValue()));
            timetableViewModel.getSubjectList().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
                @Override
                public void onChanged(ArrayList subjectList) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
        return view;
    }
}