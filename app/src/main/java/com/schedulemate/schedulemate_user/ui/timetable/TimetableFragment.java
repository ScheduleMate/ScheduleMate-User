package com.schedulemate.schedulemate_user.ui.timetable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;

public class TimetableFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;
    private View root;
    private String semester;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        timetableViewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        String semester = (TimetableFragmentArgs.fromBundle(getArguments()).getSemester());
        if(semester != null){
            timetableViewModel.setSemester(semester);
        }
        timetableViewModel.setTimetable(sharedViewModel.getTimetableDR());

        root = inflater.inflate(R.layout.fragment_timetable, container, false);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAdd:
                CharSequence[] menu = {"시간표 선택", "직접 추가"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (menu[which].toString()){
                            case "시간표 선택":
                                Navigation.findNavController(root).navigate(R.id.action_nav_timetable_to_subjectListFragment);
                                break;
                            case "직접 추가":
                                Navigation.findNavController(root).navigate(R.id.action_nav_timetable_to_registerSubjectFragment);
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.itemList:
                    Navigation.findNavController(root).navigate(R.id.action_nav_timetable_to_timetableListFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}