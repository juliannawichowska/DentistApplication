package com.example.dentistapplication.ui.dTools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;

/*
    Fragment ten odpowiada za ewentualne ustawienia aplikajci
 */

public class dToolsFragment extends Fragment {

    private dToolsViewModel dToolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dToolsViewModel =
                ViewModelProviders.of(this).get(dToolsViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_tools, container, false);

        return root;
    }
}