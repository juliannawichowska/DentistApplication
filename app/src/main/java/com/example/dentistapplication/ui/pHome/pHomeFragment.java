package com.example.dentistapplication.ui.pHome;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
    Fragment ten odpowiada pulpitowi aplikacji pacjenta
    Wyświetlona zostaje tutaj lista lekarzy
 */
public class pHomeFragment extends Fragment {

    RecyclerView recyclerView;
    private pHomeViewModel phomeViewModel;
    AdapterDoctors adapterDoctors;
    List<ModelDoctors> doctorsList;
    Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        phomeViewModel =
                ViewModelProviders.of(this).get(pHomeViewModel.class);
        View root = inflater.inflate(R.layout.p_fragment_home, container, false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = (RecyclerView) root.findViewById(R.id.doctors_RecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //zainicjowanie listy użytkowników
        doctorsList = new ArrayList<>();

        //pobierz wszystkich użytkowników
        getAllDoctors();

        return root;
    }

    private void getAllDoctors() {
        //get
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorsList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelDoctors modelDoctors = ds.getValue(ModelDoctors.class);
                    doctorsList.add(modelDoctors);

                    adapterDoctors = new AdapterDoctors(getActivity(), doctorsList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterDoctors);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
