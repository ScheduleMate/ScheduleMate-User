package com.schedulemate.schedulemate_user.ui.timetable.classDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

public class ClassDialog extends Dialog {
    private Context context;
    private Subject.SubjectItem subjectItem;

    public ClassDialog(@NonNull Context context, Subject.SubjectItem subjectItem) {
        super(context);
        this.context = context;
        this.subjectItem = subjectItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_dialog_layout);

        
    }
}
