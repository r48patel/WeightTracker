package com.rpatel.weighttracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = getArguments().getInt("year" ,c.get(Calendar.YEAR));
        int month = getArguments().getInt("month" ,c.get(Calendar.MONTH)) - 1; //Calendar months start index is 0
        int day = getArguments().getInt("day" ,c.get(Calendar.DAY_OF_MONTH));
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        System.out.println("DatePicked: " + year + " " + month + " " + day);
        Home.setTimeText(year, month, day);
    }
}
