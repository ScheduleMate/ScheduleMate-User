package com.schedulemate.schedulemate_user.ui.timetable.registerSubject;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TextView textView;

    public TimePickerFragment(TextView textView) {
        this.textView = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] time = textView.getText().toString().split(":");
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this, Integer.parseInt(time[0]), Integer.parseInt(time[1]), DateFormat.is24HourFormat(getContext()));
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textView.setText(String.format("%02d:%02d", hourOfDay, minute));
    }
}
