package com.example.dentistapplication.ui.pHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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


public class doctorProfile extends AppCompatActivity {

    ImageView avatar;
    TextView nameSurname, email, descriptionContent, numberContent, addressContent;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    //dres url zdjęcia profilowego
    Uri image_uri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Doctors");

        avatar = findViewById(R.id.Avatar);
        nameSurname = findViewById(R.id.NameSurname);
        email = findViewById(R.id.Email);
        descriptionContent = findViewById(R.id.descriptionContent);
        numberContent = findViewById(R.id.numberContent);
        addressContent = findViewById(R.id.addressContent);

        String doctorEmail;

        Intent intent = getIntent();
        doctorEmail = intent.getStringExtra("doctorEmail");

        //wyszukanie użytkownika po mailu
        Query query = databaseReference.orderByChild("email").equalTo(doctorEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //zapisanie do zmiennych wartości z bazy danych
                    String nameSurname1 = "" + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                    String email1 = "" + ds.child("email").getValue();
                    String number1 = "" + ds.child("number").getValue();
                    String image1 = "" + ds.child("imageURL").getValue();
                    String description1 = "" + ds.child("description").getValue();
                    String address1 = "" + ds.child("address").getValue();

                    //ustawienie w menu imienia i nazwiska, adresu e-mail, numeru telefonu, opisu lekarza i adresu gabinetu
                    nameSurname.setText(nameSurname1);
                    email.setText(email1);
                    numberContent.setText(number1);
                    descriptionContent.setText(description1);
                    addressContent.setText(address1);

                    //załadowanie zdjęcia profilowego z bazy danych
                    try {
                        Picasso.get().load(image1).into(avatar);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person).into(avatar);
                    }
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
