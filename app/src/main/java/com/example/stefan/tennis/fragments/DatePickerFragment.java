package com.example.stefan.tennis.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OnSetDate onSetDate;
    private long minDate;
    private long maxDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("minDate")) {
                minDate = b.getLong("minDate");
                datePicker.getDatePicker().setMinDate(minDate);
            }
            if (b.containsKey("maxDate")) {
                maxDate = b.getLong("maxDate");
                datePicker.getDatePicker().setMaxDate(maxDate);
            }
        }
        // Create a new instance of DatePickerDialog and return it

        return datePicker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        onSetDate.setDate(day, month, year);
    }

    public void setOnSetDate(OnSetDate onSetDate) {
        this.onSetDate = onSetDate;
    }

    public interface OnSetDate {
        void setDate(int day, int month, int year);
    }
}