package com.schedulemate.schedulemate_user.ui.timetable.timetableList;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.TimetableViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.SubjectListFragmentDirections;

import java.util.List;

public class TimetableRecyclerViewAdapter extends RecyclerView.Adapter<TimetableRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private SharedViewModel sharedViewModel;
    private TimetableViewModel timetableViewModel;

    public TimetableRecyclerViewAdapter(List<String> mValues, SharedViewModel sharedViewModel, TimetableViewModel timetableViewModel) {
        this.mValues = mValues;
        this.sharedViewModel = sharedViewModel;
        this.timetableViewModel = timetableViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_timetable_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = mValues.get(position);
        holder.contentView.setText(mValues.get(position));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timetableViewModel.setSemester(mValues.get(position), sharedViewModel.getTimetableDR());
                Navigation.findNavController(holder.view).navigate(R.id.action_timetableListFragment_to_nav_timetable);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView contentView;
        public String item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            contentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}