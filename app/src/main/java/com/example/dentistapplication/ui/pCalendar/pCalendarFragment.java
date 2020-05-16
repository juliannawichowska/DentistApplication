package com.example.dentistapplication.ui.pCalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;
/*
    Fragment ten odpowiada za wyświetlenie kalendarza pacjenta
 */
public class pCalendarFragment extends Fragment {

    private pCalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                ViewModelProviders.of(this).get(pCalendarViewModel.class);
        View root = inflater.inflate(R.layout.p_fragment_calendar, container, false);
        return root;
    }
}
