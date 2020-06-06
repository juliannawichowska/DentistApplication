package com.example.dentistapplication.ui.pHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.LoginActivity;
import com.example.dentistapplication.R;
import com.example.dentistapplication.dHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
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
        recyclerView = root.findViewById(R.id.doctors_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //zainicjowanie listy użytkowników
        doctorsList = new ArrayList<>();

        //pobierz wszystkich użytkowników
        getAllDoctors();

        //wyświetlanie menu po prawej stronie
       setHasOptionsMenu(true);

        return root;
    }



    //dodanie menu po prawej stronie w górnym rogu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        //searhc view
        MenuItem item= menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //wywoływane gdy kliknięty jest przycisk wyszukiwania na klawiaturze
                //sprawdzenie czy coś zostało wpisane
                if(!TextUtils.isEmpty(query.trim())){
                    searchUsers(query);
                } else {
                    getAllDoctors();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //wywoływane gdy zostanie kliknięty jakikolwiek inny przycisk na klawiaturze
                //sprawdzenie czy coś zostało wpisane
                if(!TextUtils.isEmpty(newText.trim())){
                    searchUsers(newText);
                } else {
                    getAllDoctors();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchUsers(final String query) {
        //pobierz wszystkich wyszukanych lekarzy
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorsList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelDoctors modelDoctors = ds.getValue(ModelDoctors.class);

                    if (modelDoctors.getName().toLowerCase().contains(query.toLowerCase())
                        || modelDoctors.getEmail().toLowerCase().contains(query.toLowerCase())
                            || modelDoctors.getSurname().toLowerCase().contains(query.toLowerCase())) {

                        doctorsList.add(modelDoctors);
                    }

                    adapterDoctors = new AdapterDoctors(getActivity(), doctorsList);
                    //odswież adapter
                    adapterDoctors.notifyDataSetChanged();
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterDoctors);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getAllDoctors() {
        //pobierz wszystkich lekarzy
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
