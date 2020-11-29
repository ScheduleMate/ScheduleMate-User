package com.schedulemate.schedulemate_user.ui.timetable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;
import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.RegisterSubject;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String semester = sharedViewModel.getSemester();
        if(semester != null){
            timetableViewModel.setSemester(semester, sharedViewModel.getTimetableDR());
        }
        timetableViewModel.setSubjectList(sharedViewModel.getUniversity());
        timetableViewModel.setClassGroupList(sharedViewModel.getUniversity());
        timetableViewModel.setTimetable(sharedViewModel.getTimetableDR());

        root = inflater.inflate(R.layout.fragment_timetable, container, false);

        GridView gridViewTimetable = root.findViewById(R.id.gridViewTimetable);

        ArrayList<TimetableCell> timetableCells = new ArrayList<>();

        ArrayList<Integer> layouts = new ArrayList<>();

        for(int i = timetableViewModel.getStartTime(); i <= timetableViewModel.getEndTime(); i++){
            layouts.add(R.layout.timetable_grid_view_item_header_layout);
            for(int j = 0; j < 5; j++){
                layouts.add(R.layout.timetable_grid_view_item_layout);
            }
        }
        TimetableGridAdapter adapter = new TimetableGridAdapter(getContext(), layouts, timetableCells, timetableViewModel.getStartTime(), root);
        gridViewTimetable.setAdapter(adapter);

        Pattern pattern = Pattern.compile("([0-9]{2}):([0-9]{2})");

        timetableViewModel.getUserTimetable().observe(getViewLifecycleOwner(), new Observer<HashMap>() {
            @Override
            public void onChanged(HashMap hashMap) {
                timetableCells.clear();
                ArrayList<Subject.SubjectItem> subjectItems = timetableViewModel.getSubjectList().getValue();
                ArrayList<ClassGroup> classGroups = timetableViewModel.getClassGroupList().getValue();

                for(String key : ((HashMap<String, String>)hashMap).keySet()){
                    if (!hashMap.get(key).equals("registered")) {
                        Subject.SubjectItem subjectItem = null;

                        for (Subject.SubjectItem s : subjectItems) {

                            if (s.id.equals(key)) {
                                Log.d("checkUserTimetableInSubject", s.id);
                                subjectItem = s;

                                ClassGroup classGroup;
                                for (ClassGroup c : classGroups) {
                                    Log.d("checkUserTimetableInClass", c.getClassId());
                                    if (c.getClassId().equals(hashMap.get(key))) {

                                        classGroup = c;

                                        String title = subjectItem.title;
                                        for (RegisterSubject.Time time : classGroup.getTimes()) {
                                            String day = time.getDay();
                                            String start = time.getStart();
                                            String end = time.getEnd();

                                            Matcher matcherStart = pattern.matcher(start);
                                            Matcher matcherEnd = pattern.matcher(end);

                                            if (matcherStart.find() && timetableViewModel.getStartTime() > Integer.parseInt(matcherStart.group(1)))
                                                timetableViewModel.setTimetableStart(Integer.parseInt(matcherStart.group(1)));

                                            if (matcherEnd.find() && timetableViewModel.getEndTime() < Integer.parseInt(matcherEnd.group(1)))
                                                timetableViewModel.setTimetableEnd(Integer.parseInt(matcherStart.group(1)));

                                            timetableCells.add(new TimetableCell(title, day, start, end, subjectItem, classGroup, null));
                                        }

                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    else{
                        HashMap<String, RegisterSubject> registerSubjects = timetableViewModel.getRegisteredTimetable().getValue();

                        RegisterSubject registerSubject = registerSubjects.get(key);

                        String title = registerSubject.getTitle();
                        for (RegisterSubject.Time time : registerSubject.getTimes()) {
                            String day = time.getDay();
                            String start = time.getStart();
                            String end = time.getEnd();

                            Matcher matcherStart = pattern.matcher(start);
                            Matcher matcherEnd = pattern.matcher(end);

                            if (matcherStart.find() && timetableViewModel.getStartTime() > Integer.parseInt(matcherStart.group(1)))
                                timetableViewModel.setTimetableStart(Integer.parseInt(matcherStart.group(1)));

                            if (matcherEnd.find() && timetableViewModel.getEndTime() < Integer.parseInt(matcherEnd.group(1)))
                                timetableViewModel.setTimetableEnd(Integer.parseInt(matcherStart.group(1)));

                            timetableCells.add(new TimetableCell(title, day, start, end, null, null, registerSubject));
                        }
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });

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