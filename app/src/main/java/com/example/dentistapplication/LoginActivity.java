package com.example.dentistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText emailTxt, passwordTxt;
    Button loginBtn, registerBtn;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this,"Jesteś zalogowany", Toast.LENGTH_SHORT);
                    String uid = mFirebaseUser.getUid();

                    DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference ref = refe.child("Users").child(uid).child("userType");

                    //Attach a listener to read the data at our posts reference
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userType1 = dataSnapshot.getValue(String.class);
                            System.out.println(userType1);
                            assert userType1 != null;
                            if(userType1.equals("patient")){
                                Intent pHome = new Intent(LoginActivity.this,PHomeActivity.class);
                                startActivity(pHome);
                            }else{
                                Intent dHome = new Intent(LoginActivity.this,DHomeActivity.class);
                                startActivity(dHome);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Zaloguj się", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                if(email.isEmpty()){
                    emailTxt.setError("Proszę wprowadź email..");
                    emailTxt.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordTxt.setError("Proszę wprowadź hasło..");
                    passwordTxt.requestFocus();
                }else {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Logowanie nie powiodło się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                            } else {
                                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                                assert mFirebaseUser != null;
                                String uid = mFirebaseUser.getUid();

                                DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference ref = refe.child("Users").child(uid).child("userType");

                                //Attach a listener to read the data at our posts reference
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String userType2 = dataSnapshot.getValue(String.class);
                                        System.out.println(userType2+"aa");
                                        assert userType2 != null;
                                        if(!userType2.equals("doctor")){
                                            startActivity(new Intent(LoginActivity.this,PHomeActivity.class));
                                            finish();
                                            System.out.println("ajajaja");
                                        }else{
                                            startActivity(new Intent(LoginActivity.this,DHomeActivity.class));
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
