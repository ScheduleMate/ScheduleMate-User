package com.schedulemate.schedulemate_user.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;

public class CommunityFragment extends Fragment {

    private CommunityViewModel communityViewModel;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityViewModel =
                new ViewModelProvider(this).get(CommunityViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_community, container, false);

        //communityViewModel.setBoardMap(sharedViewModel.getUniversity());

        return root;
    }
}