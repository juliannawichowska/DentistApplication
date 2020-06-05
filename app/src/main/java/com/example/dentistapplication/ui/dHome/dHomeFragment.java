package com.example.dentistapplication.ui.dHome;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dentistapplication.R;
import com.example.dentistapplication.ui.dCalendar.dCalendarFragment;
import com.example.dentistapplication.ui.dHome.d_AdapterVisits;
import com.example.dentistapplication.ui.dHome.d_ModelVisits;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
    Fragment ten odpowiada pulpitowi aplikacji lekarza
 */

public class dHomeFragment extends Fragment {
    private dHomeViewModel dHomeViewModel;
    RecyclerView recyclerView;
    d_AdapterVisits adapterVisits;
    List<d_ModelVisits> visitsList;
    Context context;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dHomeViewModel =
                ViewModelProviders.of(this).get(dHomeViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_home, container, false);
    //inicjacja instancji FirebaseAuth i FirebaseDatabase
    firebaseAuth =  FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();

    //pobierz użytkownika
    user = firebaseAuth.getCurrentUser();
    String userId = user.getUid();

    final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    //zainicjowanie Recycler View
    recyclerView = root.findViewById(R.id.visits_RecyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
    //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    //zainicjowanie listy użytkowników
    visitsList = new ArrayList<>();

    //pobierz wszystkich użytkowników
    getAllVisits(userId);
        return root;
    }
    private void getAllVisits(String userId) {
        //pobierz wszystkie daty
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Dates/"+userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitsList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    d_ModelVisits modelVisits = ds.getValue(d_ModelVisits.class);
                    visitsList.add(modelVisits);

                    adapterVisits = new d_AdapterVisits(getActivity(), visitsList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterVisits);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}