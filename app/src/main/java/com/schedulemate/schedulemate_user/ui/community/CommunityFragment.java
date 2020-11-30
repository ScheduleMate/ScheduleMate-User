package com.schedulemate.schedulemate_user.ui.community;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunityFragment extends Fragment {

    private CommunityViewModel communityViewModel;
    private SharedViewModel sharedViewModel;
    private CommunityRecyclerViewAdapter adapter;
    private TextView textViewTitle;
    private String classTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityViewModel =
                new ViewModelProvider(requireActivity()).get(CommunityViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_community, container, false);

        setHasOptionsMenu(true);

        textViewTitle = root.findViewById(R.id.textViewTitle);

        RecyclerView recyclerViewPost = root.findViewById(R.id.recyclerViewPost);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));
        if(classTitle != null && communityViewModel.getBoardMap().getValue().containsValue(classTitle)){
            adapter = new CommunityRecyclerViewAdapter(communityViewModel.getPostList().getValue(), classTitle);
            recyclerViewPost.setAdapter(adapter);
            Log.d("classTitleAtStart", classTitle);
        }

        communityViewModel.setBoardMap(sharedViewModel.getUniversity(), sharedViewModel.getSemester());
        communityViewModel.getClassId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(communityViewModel.getBoardMap().getValue().containsKey(s)) {
                    textViewTitle.setText(communityViewModel.getBoardMap().getValue().get(s).toString());
                    classTitle = communityViewModel.getBoardMap().getValue().get(s).toString();
                    if(adapter == null){
                        adapter = new CommunityRecyclerViewAdapter(communityViewModel.getPostList().getValue(), classTitle);
                        recyclerViewPost.setAdapter(adapter);
                        Log.d("classTitleAtClassId", classTitle);
                    }
                }
            }
        });

        communityViewModel.getBoardMap().observe(getViewLifecycleOwner(), new Observer<HashMap>() {
            @Override
            public void onChanged(HashMap hashMap) {
                if(hashMap.containsKey(communityViewModel.getClassId().getValue())) {
                    textViewTitle.setText(hashMap.get(communityViewModel.getClassId().getValue()).toString());
                    classTitle = hashMap.get(communityViewModel.getClassId().getValue()).toString();
                    if(adapter == null){
                        adapter = new CommunityRecyclerViewAdapter(communityViewModel.getPostList().getValue(), classTitle);
                        recyclerViewPost.setAdapter(adapter);
                        Log.d("classTitleAtBoardMap", classTitle);
                    }
                }
            }
        });

        communityViewModel.getPostList().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> list) {
                if(adapter != null) {
                    adapter.setItems(list, classTitle);

                    Log.d("classTitleAtPostList", classTitle);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ((ImageButton)root.findViewById(R.id.imageButtonPost)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragmentDirections.ActionNavCommunityToPostingFragment action =
                        CommunityFragmentDirections.actionNavCommunityToPostingFragment(classTitle);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.community, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.communityList:
                ArrayList<String> list = new ArrayList<String>(communityViewModel.getBoardMap().getValue().values());
                CharSequence[] menu = list.toArray(new CharSequence[list.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> map = (HashMap<String, String>)communityViewModel.getBoardMap().getValue();
                        for(String key : map.keySet()){
                            if(map.get(key).equals(menu[which])){
                                communityViewModel.setClassId(key);
                            }
                        }
                    }
                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}