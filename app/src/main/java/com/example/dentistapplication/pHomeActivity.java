package com.example.dentistapplication;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;


public class pHomeActivity extends AppCompatActivity {
    TextView userName, userEmail;
    private AppBarConfiguration mAppBarConfiguration;
    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phome);
        //ustawienie toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();
        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Patients");
        //Zainicjowanie menu głównego
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.p_nav_home, R.id.p_nav_calendar, R.id.p_nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.p_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
//poniższe pola zostaną wypełnione informacjami o użytkowniku
        userName = headerView.findViewById(R.id.navName);
        userEmail = headerView.findViewById(R.id.navEmail);

        //wyszukanie użytkownika po mailu
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //zapisanie do zmiennych wartości z bazy danych
                    String nameSurname = "" + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                    String email = "" + ds.child("email").getValue();

                    //ustawienie w menu imienia i nazwiska, oraz adresu e-mail
                    userName.setText(nameSurname);
                    userEmail.setText(email);

                }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    //dodanie menu po prawej stronie w górnym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.d_right_menu, menu);
        return true;
    }

    //funkcja umożliwiająca wybranie z prawego górnego rogu opcję 'Wyloguj się'
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(pHomeActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.p_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
