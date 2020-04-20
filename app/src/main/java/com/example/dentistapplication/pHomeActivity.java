package com.example.dentistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.dentistapplication.R.menu.right_menu;

public class pHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_activity_home);
    }

    //dodanie menu po prawej stronie w górnym rogu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(right_menu, menu);
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

}
