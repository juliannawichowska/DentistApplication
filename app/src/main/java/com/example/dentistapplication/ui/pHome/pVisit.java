package com.example.dentistapplication.ui.pHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dentistapplication.R;
import com.example.dentistapplication.ui.pCalendar.pCalendarFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class pVisit extends AppCompatActivity {

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_visit);

        Log.v("TAGG", "Dotarlam");
        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        final String day, hour, uidDoctor, free, key,uidPatient;

        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        hour = intent.getStringExtra("hour");
        uidDoctor = intent.getStringExtra("uidDoctor");
        key = intent.getStringExtra("keyVisit");
        free = "false";
        uidPatient = user.getUid();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Dates/"+uidDoctor+"/"+key);

        //utworzenie HashMap z opisem lekarza
        HashMap<String, Object> result = new HashMap<>();
        result.put("free", free);
        result.put("uidPatient", uidPatient);

        //zaktualizowanie bazy danych o opis
        databaseReference.updateChildren(result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //zaktualizowano pomyślnie
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //aktualizacja nie powiodła się
                    }
                });

        FragmentManager fm = getSupportFragmentManager();
        pCalendarFragment fragment = new pCalendarFragment();
        fm.beginTransaction().replace(R.id.container2, fragment).commit();

    }
}