package com.example.dentistapplication.ui.dCalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;

public class dCalendarFragment extends Fragment {

    private dCalendarViewModel dCalendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dCalendarViewModel =
                ViewModelProviders.of(this).get(dCalendarViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_calendar, container, false);
        final TextView textView = root.findViewById(R.id.text_calendar);
        dCalendarViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}