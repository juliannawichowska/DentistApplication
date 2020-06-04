package com.example.dentistapplication.ui.pCalendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;
import com.example.dentistapplication.ui.pHome.AdapterDoctors;
import com.example.dentistapplication.ui.pHome.ModelDoctors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/*
    Fragment ten odpowiada za wyświetlenie kalendarza pacjenta
 */
public class pCalendarFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterVisits adapterVisits;
    List<ModelVisits> visitsList;
    Context context;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private pCalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.p_fragment_calendar, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobierz użytkownika
        user = firebaseAuth.getCurrentUser();
        String myUid = user.getUid();

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
        getAllVisits(myUid);

        return root;
    }

    private void getAllVisits(String myUid) {
        //pobierz wszystkie daty
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Visits/"+myUid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitsList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelVisits modelVisits = ds.getValue(ModelVisits.class);
                    visitsList.add(modelVisits);

                    adapterVisits = new AdapterVisits(getActivity(), visitsList);
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
