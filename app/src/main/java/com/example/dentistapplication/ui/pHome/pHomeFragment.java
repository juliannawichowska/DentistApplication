package com.example.dentistapplication.ui.pHome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;

/*
    Fragment ten odpowiada pulpitowi aplikacji pacjenta
 */
public class pHomeFragment extends Fragment {

    private pHomeViewModel phomeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        phomeViewModel =
                ViewModelProviders.of(this).get(pHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }
}
