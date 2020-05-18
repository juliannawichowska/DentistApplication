package com.example.dentistapplication;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class dHomeActivity extends AppCompatActivity {
    TextView userName, userEmail;
    ImageView userImage;
    private AppBarConfiguration mAppBarConfiguration;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_home);

        //ustawienie toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Doctors");

        //Zainicjowanie menu głównego
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_profile, R.id.nav_calendar, R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);

        //poniższe pola zostaną wypełnione informacjami o użytkowniku
        userName = headerView.findViewById(R.id.navName);
        userEmail = headerView.findViewById(R.id.navEmail);
        userImage = headerView.findViewById(R.id.navImage);

        //wyszukanie użytkownika po mailu
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //zapisanie do zmiennych wartości z bazy danych
                    String nameSurname = "" + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                    String email = "" + ds.child("email").getValue();
                    String image = "" + ds.child("imageURL").getValue();

                    //ustawienie w menu imienia i nazwiska, oraz adresu e-mail
                    userName.setText(nameSurname);
                    userEmail.setText(email);

                    //załadowanie zdjęcia profilowego z bazy danych
                    try {
                        Picasso.get().load(image).into(userImage);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person).into(userImage);
                    }
                }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //dodanie menu po prawej stronie w górnym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.p_right_menu, menu);
        return true;
    }

    //funkcja umożliwiająca wybranie z prawego górnego rogu opcję 'Wyloguj się'
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_logout){
            //wylogowanie użytkownika
            FirebaseAuth.getInstance().signOut();
            //uruchomienie ekranu logowania użytkownika
            startActivity(new Intent(dHomeActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //dodanie menu głównego
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
