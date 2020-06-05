package com.example.dentistapplication.ui.dHome;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;
import com.example.dentistapplication.ui.dHome.d_ModelVisits;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class d_AdapterVisits extends RecyclerView.Adapter<d_AdapterVisits.MyHolder> {
    private Context mcontext;
    List<com.example.dentistapplication.ui.dHome.d_ModelVisits> visitsList;

    public d_AdapterVisits(Context context, List<d_ModelVisits> visitsList) {
        this.mcontext = context;
        this.visitsList = visitsList;
    }

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_visit_element, parent, false);
        return  new d_AdapterVisits.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        final String free = visitsList.get(i).getFree();
        //pobieranie danych do wyświetlenia na liście
        final String date = visitsList.get(i).getDate();
        final String hour = visitsList.get(i).getHour();
        final String uidPatient = visitsList.get(i).getUidPatient();


        Log.v("tak", "jestem tu");

        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Doctors");
        //wyszukanie użytkownika po mailu
        Query query = databaseReference.orderByChild("userUid").equalTo(uidPatient);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //zapisanie do zmiennych wartości z bazy danych
                    myHolder.vDay.setText("Dzień : "+date);
                    myHolder.vHour.setText("Godzina : "+hour);
                    if(free.equals("false")){
                        myHolder.mFree.setText("Wizyta zajeta");
                        String vPatient1 = "Pacjent : " + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                        myHolder.vPatient.setText(vPatient1);
                        String vNumber1_p = "Numer telefonu : " + ds.child("number").getValue();

                        myHolder.vPatient.setText(vPatient1);
                        myHolder.vNumber_p.setText(vNumber1_p);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

            @Override
    public int getItemCount() {
        return visitsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView vDay, vHour, mFree, vPatient, vNumber_p;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            vDay = itemView.findViewById(R.id.dayV_d);
            vHour = itemView.findViewById(R.id.hourV_d);
            mFree = itemView.findViewById(R.id.is_free_d);
            vPatient = itemView.findViewById(R.id.patientV_d);
            vNumber_p = itemView.findViewById(R.id.number_d);
        }

    }
}

