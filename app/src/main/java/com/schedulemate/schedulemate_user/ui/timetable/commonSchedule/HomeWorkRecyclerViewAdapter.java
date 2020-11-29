package com.schedulemate.schedulemate_user.ui.timetable.commonSchedule;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.User;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class HomeWorkRecyclerViewAdapter extends RecyclerView.Adapter<HomeWorkRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ClassGroup.HomeWork> items;
    private User user;
    private String semester;
    private String classId;
    private Context context;
    private String subjectName;

    public HomeWorkRecyclerViewAdapter(ArrayList<ClassGroup.HomeWork> items, User user, String semester, String classId, String subjectName, Context context) {
        this.items = items;
        this.user = user;
        this.semester = semester;
        this.classId = classId;
        this.context = context;
        this.subjectName = subjectName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_homework_schedule_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.homeWork = items.get(position);
        holder.textViewNumber.setText(String.valueOf(position + 1) + ".");
        holder.textViewHomeworkTime.setText(items.get(position).getTimeLimit() + "까지");
        holder.textViewHomeworkContent.setText(items.get(position).getContent());
        ImageButton imageButton = holder.view.findViewById(R.id.imageButtonDelete);

        if(user.getId().equals(items.get(position).getRegistrant())) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference(user.getUniversity()).child(semester).child("classInfo").child(classId)
                            .child("schedule").child("homeWork").child(items.get(position).getRegisteredTime()).removeValue();
                    items.remove(position);
                }
            });
        }
        else{
            imageButton.setVisibility(View.GONE);
        }

        ((ImageButton)holder.view.findViewById(R.id.imageButtonReport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.report_dialog_layout, null);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("일정 신고").setMessage("이 일정을 신고하시겠습니까?").setView(view).setPositiveButton("신고", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> info = new HashMap<>();
                        info.put("classKey", classId);
                        info.put("classTitle", subjectName);
                        info.put("declareTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                        info.put("reason", ((EditText)view.findViewById(R.id.editTextReport)).getText().toString());
                        info.put("registrant", items.get(position).getRegistrant());
                        info.put("semester", semester);

                        FirebaseDatabase.getInstance().getReference(user.getUniversity()).child("declare").child("schedule").child("homeWork").child(items.get(position).getRegisteredTime()).setValue(info, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                                builder1.setMessage("신고 완료되었습니다.").setPositiveButton("확인", null);
                                builder1.show();
                            }
                        });
                    }
                }).setNegativeButton("취소", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView textViewNumber;
        public TextView textViewHomeworkTime;
        public TextView textViewHomeworkContent;
        public ClassGroup.HomeWork homeWork;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.textViewNumber = view.findViewById(R.id.textViewNumber);
            this.textViewHomeworkTime = view.findViewById(R.id.textViewHomeworkTime);
            this.textViewHomeworkContent = view.findViewById(R.id.textViewHomeworkContent);
        }
    }
}
