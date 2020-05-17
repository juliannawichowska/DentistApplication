package com.example.dentistapplication.ui.dCalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;

/*
    Fragment ten odpowiada za wyświetlenie kalendarza lekarza
 */

public class dCalendarFragment extends Fragment {
    CalendarView calendarView;
    TextView my_calendar;
    private dCalendarViewModel dCalendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dCalendarViewModel =
                ViewModelProviders.of(this).get(dCalendarViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_calendar, container, false);

        return root;
    }
}