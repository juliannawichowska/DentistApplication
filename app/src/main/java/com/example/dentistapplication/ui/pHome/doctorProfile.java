package com.example.dentistapplication.ui.pHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button callDoctor;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    //dres url zdjęcia profilowego
    Uri image_uri;

    private static final int REQUEST_CALL = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {
                Toast.makeText(this, "Nie uzyskano zgody", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_activity_doctor_profile);

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
        callDoctor = findViewById(R.id.callDoctor);

        final String doctorEmail;

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

        callDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //wyszukanie użytkownika po mailu
                Query query = databaseReference.orderByChild("email").equalTo(doctorEmail);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //zapisanie do zmiennych wartości z bazy danych

                            String number_call = "" + ds.child("number").getValue();
                            if(ContextCompat.checkSelfPermission(doctorProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(doctorProfile.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                            } else {
                                String dial = "tel: " + number_call;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }


                });

            }
        });

    }
}

