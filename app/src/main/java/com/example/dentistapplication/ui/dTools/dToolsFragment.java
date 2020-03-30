package com.example.dentistapplication.ui.dTools;

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

public class dToolsFragment extends Fragment {

    private dToolsViewModel dToolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dToolsViewModel =
                ViewModelProviders.of(this).get(dToolsViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        dToolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}