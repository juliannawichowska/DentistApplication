package com.example.dentistapplication.ui.pCalendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterVisits extends RecyclerView.Adapter<com.example.dentistapplication.ui.pCalendar.AdapterVisits.MyHolder> {

    private Context mcontext;
    List<ModelVisits> visitsList;

    public AdapterVisits(Context context, List<ModelVisits> visitsList) {
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
    public com.example.dentistapplication.ui.pCalendar.AdapterVisits.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_element, parent, false);
        return  new com.example.dentistapplication.ui.pCalendar.AdapterVisits.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final com.example.dentistapplication.ui.pCalendar.AdapterVisits.MyHolder myHolder, final int i) {

        //pobieranie danych do wyświetlenia na liście
        final String day = visitsList.get(i).getDay();
        final String hour = visitsList.get(i).getHour();
        final String uidDoctor = visitsList.get(i).getUidDoctor();


        Log.v("tak", "jestem tu");

        //ustawianie danych lekarza
        myHolder.vDay.setText("Data : "+day);
        myHolder.vHour.setText("Godzina : "+hour);

        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Doctors");
        //wyszukanie użytkownika po mailu
        Query query = databaseReference.orderByChild("userUid").equalTo(uidDoctor);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //zapisanie do zmiennych wartości z bazy danych
                    String vDoctor1 = "Stomatolog : " + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                    String vNumber1 = "Numer telefonu : " + ds.child("number").getValue();
                    String vAddress1 = "Adres gabinetu : " + ds.child("address").getValue();
                    String image1 = "" + ds.child("imageURL").getValue();

                    myHolder.vDoctor.setText(vDoctor1);
                    myHolder.vNumber.setText(vNumber1);
                    myHolder.vAddress.setText(vAddress1);

                    //załadowanie zdjęcia profilowego z bazy danych
                    try {
                        Picasso.get().load(image1).into(myHolder.vImage);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person).into(myHolder.vImage);
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return visitsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView vDay, vHour, vDoctor, vAddress, vNumber;
        ImageView vImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            vDay = itemView.findViewById(R.id.dayV);
            vHour = itemView.findViewById(R.id.hourV);
            vDoctor = itemView.findViewById(R.id.doctorV);
            vAddress = itemView.findViewById(R.id.addressV);
            vNumber = itemView.findViewById(R.id.numberV);
            vImage = itemView.findViewById(R.id.imageVisit);
        }

    }
}
