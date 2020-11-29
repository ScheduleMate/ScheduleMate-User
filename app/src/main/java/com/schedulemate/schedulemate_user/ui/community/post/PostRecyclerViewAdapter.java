package com.schedulemate.schedulemate_user.ui.community.post;

import android.app.AlertDialog;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.User;
import com.schedulemate.schedulemate_user.ui.community.Comment;

import java.util.HashMap;
import java.util.List;

class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {
    private List<Comment> items;
    private String subjectId;
    private String classId;
    private String postId;
    private Context context;
    private User user;

    public PostRecyclerViewAdapter(List<Comment> items, String classId, String postId, Context context, User user, String subjectId) {
        this.items = items;
        this.subjectId = subjectId;
        this.classId = classId;
        this.postId = postId;
        this.context = context;
        this.user = user;
    }

    public void setItems(List<Comment> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_fragment__recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.comment = items.get(position);
        holder.textViewCommentWriter.setText(items.get(position).getWriterNickName());
        holder.textViewCommentTime.setText(items.get(position).getTime());
        holder.textViewCommentContent.setText(items.get(position).getContent());

        ImageButton imageButtonDelete = holder.view.findViewById(R.id.imageButtonDelete);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(holder.comment.getWriter())) {
            imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("댓글 삭제").setMessage("정말 이 댓글을 삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference("community").child(classId).child("post")
                                    .child(postId).child("comment").child(holder.comment.getId()).removeValue();
                        }
                    }).setNegativeButton("취소", null);

                    builder.show();
                }
            });
        }
        else{
            imageButtonDelete.setVisibility(View.GONE);
        }

        ((ImageButton)holder.view.findViewById(R.id.imageButtonCommentReport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.report_dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("댓글 신고").setMessage("이 댓글을 신고하시겠습니까?").setView(view).setPositiveButton("신고", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> info = new HashMap<>();
                        //info.put("class", className);
                        info.put("classKey", classId);
                        info.put("classTitle", subjectId);
                        info.put("communityKey", postId);
                        info.put("reason", ((EditText)view.findViewById(R.id.editTextReport)).getText().toString());
                        info.put("time", items.get(position).getTime());
                        info.put("writer", items.get(position).getWriter());
                        info.put("writerNickName", items.get(position).getWriterNickName());

                        FirebaseDatabase.getInstance().getReference(user.getUniversity()).child("declare").child("comment").child(items.get(position).getId()).setValue(info, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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
        public final View view;
        public final TextView textViewCommentWriter;
        public final TextView textViewCommentTime;
        public final TextView textViewCommentContent;
        public Comment comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.textViewCommentWriter = view.findViewById(R.id.textViewCommentWriter);
            this.textViewCommentTime = view.findViewById(R.id.textViewCommentTime);
            this.textViewCommentContent = view.findViewById(R.id.textViewCommentContent);
        }
    }
}
