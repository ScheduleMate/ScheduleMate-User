package com.schedulemate.schedulemate_user.ui.community;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommunityViewModel extends ViewModel {
    private MutableLiveData<HashMap> boardMap = new MutableLiveData<>();
    private HashMap<String, String> boards = new HashMap<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CommunityViewModel() {
        boardMap.setValue(boards);
    }

    public void setBoardMap(String university, String semester){
        FirebaseDatabase.getInstance().getReference(university).child("timetable").child(user.getUid()).child(semester).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boardMap.setValue(snapshot.getValue(HashMap.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })
        ;
    }
}