package com.example.dentistapplication.ui.dHome;

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

public class dHomeFragment extends Fragment {

    private dHomeViewModel dHomeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dHomeViewModel =
                ViewModelProviders.of(this).get(dHomeViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        dHomeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}