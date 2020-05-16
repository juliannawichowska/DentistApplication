package com.example.dentistapplication.ui.pTools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;
/*
    Fragment ten odpowiada za wyświetlenie profilu pacjenta, oraz umożliwia mu edycje danych
 */

public class pToolsFragment extends Fragment {

    private pToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(pToolsViewModel.class);
        View root = inflater.inflate(R.layout.p_fragment_tools, container, false);
        return root;
    }
}
