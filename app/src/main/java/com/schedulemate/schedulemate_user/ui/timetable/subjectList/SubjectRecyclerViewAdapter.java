package com.schedulemate.schedulemate_user.ui.timetable.subjectList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedulemate.schedulemate_user.R;

import java.util.ArrayList;
import java.util.List;

public class SubjectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Subject.SubjectItem> items;

    public SubjectRecyclerViewAdapter(List<Subject.SubjectItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder horder;
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_subject_list_header, parent, false);
                horder = new HeaderViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_subject_list, parent, false);
                horder = new ViewHolder(view);
                break;
        }
        return horder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
        }
        else {
            ViewHolder viewHolder = (ViewHolder)holder;
            position = position - 1;
            viewHolder.item = items.get(position);
            viewHolder.itemTitle.setText(items.get(position).title);
            viewHolder.itemDependancy.setText(items.get(position).dependency);
            viewHolder.itemClassification.setText(items.get(position).classification);
            viewHolder.itemGrade.setText(items.get(position).grade);
            viewHolder.itemCredit.setText(items.get(position).credit);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return super.getItemViewType(position);
        else
            return super.getItemViewType(position) + 1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView itemTitle;
        public final TextView itemDependancy;
        public final TextView itemClassification;
        public final TextView itemGrade;
        public final TextView itemCredit;
        public Subject.SubjectItem item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemTitle = (TextView) view.findViewById(R.id.item_title);
            itemDependancy = (TextView) view.findViewById(R.id.item_dependancy);
            itemClassification = (TextView) view.findViewById(R.id.item_classification);
            itemGrade = (TextView) view.findViewById(R.id.item_grade);
            itemCredit = (TextView) view.findViewById(R.id.item_credit);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() - 1;
                    if (position != RecyclerView.NO_POSITION){
                        ArrayList<String> classList = new ArrayList<>(items.get(position).classes.values());
                        CharSequence[] classes = classList.toArray(new CharSequence[classList.size()]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                        builder.setItems(classes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SubjectListFragmentDirections.ActionSubjectListFragmentToClassDetailFragment action = SubjectListFragmentDirections.actionSubjectListFragmentToClassDetailFragment(items.get(position), classList.get(which));
                                Navigation.findNavController(view).navigate(action);
                            }
                        }).show();
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemTitle.getText() + "'";
        }
    }
}