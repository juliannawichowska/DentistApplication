package com.example.dentistapplication.ui.dHome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;

/*
    Fragment ten odpowiada pulpitowi aplikacji lekarza
 */

public class dHomeFragment extends Fragment {

    private dHomeViewModel dHomeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dHomeViewModel =
                ViewModelProviders.of(this).get(dHomeViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_home, container, false);

        return root;
    }
}