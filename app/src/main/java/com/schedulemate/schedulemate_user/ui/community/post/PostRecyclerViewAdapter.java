package com.schedulemate.schedulemate_user.ui.community.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.community.Comment;

import java.util.List;

class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {
    private List<Comment> items;
    private String classId;
    private String postId;

    public PostRecyclerViewAdapter(List<Comment> items, String classId, String postId) {
        this.items = items;
        this.classId = classId;
        this.postId = postId;
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
