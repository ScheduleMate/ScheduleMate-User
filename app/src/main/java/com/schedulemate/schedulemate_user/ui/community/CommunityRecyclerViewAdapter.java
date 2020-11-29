package com.schedulemate.schedulemate_user.ui.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.schedulemate.schedulemate_user.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommunityRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Post> items;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Pattern pattern = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2})\\s([0-9]{2}):([0-9]{2})");
    private String classTitle;

    public CommunityRecyclerViewAdapter(List<Post> items, String classTitle) {
        this.items = items;
        this.classTitle = classTitle;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CommunityRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment__recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Date today = new Date(System.currentTimeMillis());
        String todayString = simpleDateFormat.format(today);
        Matcher matcherToday = pattern.matcher(todayString);
        Matcher matcherPost = pattern.matcher(items.get(position).getTime());

        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.post = items.get(position);
        viewHolder.textViewTitle.setText(items.get(position).getTitle());
        viewHolder.textViewContent.setText(items.get(position).getContent());
        viewHolder.textViewWriter.setText(items.get(position).getWriterNickName());

        if(matcherPost.find()){
            if(matcherToday.find() && matcherPost.group(1).equals(matcherToday.group(1))) {
                    if (matcherPost.group(2).equals(matcherToday.group(2)) && matcherPost.group(3).equals(matcherToday.group(3)))
                    viewHolder.textViewTime.setText(String.format("%02d:%02d", Integer.parseInt(matcherPost.group(4)), Integer.parseInt(matcherPost.group(5))));
                else
                    viewHolder.textViewTime.setText(String.format("%02d/%02d %02d:%02d", Integer.parseInt(matcherPost.group(2)),
                            Integer.parseInt(matcherPost.group(3)), Integer.parseInt(matcherPost.group(4)), Integer.parseInt(matcherPost.group(5))));
            }
            else{
                viewHolder.textViewTime.setText(String.format("%04d/%02d/%02d %02d:%02d", Integer.parseInt(matcherPost.group(1)), Integer.parseInt(matcherPost.group(2)),
                        Integer.parseInt(matcherPost.group(3)), Integer.parseInt(matcherPost.group(4)), Integer.parseInt(matcherPost.group(5))));
            }
        }
        else{
            viewHolder.textViewTime.setText(items.get(position).getTime());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public final TextView textViewTitle;
        public final TextView textViewContent;
        public final TextView textViewWriter;
        public final TextView textViewTime;
        public Post post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewContent = itemView.findViewById(R.id.textViewContent);
            this.textViewWriter = itemView.findViewById(R.id.textViewWriter);
            this.textViewTime = itemView.findViewById(R.id.textViewTime);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommunityFragmentDirections.ActionNavCommunityToPostFragment action = CommunityFragmentDirections.actionNavCommunityToPostFragment(post, textViewTitle.getText().toString());
                    Navigation.findNavController(v).navigate(action);
                }
            });
        }
    }
}
