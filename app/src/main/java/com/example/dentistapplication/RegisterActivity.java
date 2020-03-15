package com.example.dentistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText emailTxt, passwordTxt,nameTxt, surnameTxt, numberTxt;
    Button registerBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        nameTxt = findViewById(R.id.nameTxt);
        surnameTxt = findViewById(R.id.surnameTxt);
        numberTxt = findViewById(R.id.numberTxt);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();

                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                String name = nameTxt.getText().toString();
                String surname = surnameTxt.getText().toString();
                String number = numberTxt.getText().toString();

                if(email.isEmpty()){
                    emailTxt.setError("Proszę wprowadź email..");
                    emailTxt.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordTxt.setError("Proszę wprowadź hasło..");
                    passwordTxt.requestFocus();
                }
                else if(name.isEmpty()){
                    nameTxt.setError("Proszę wprowadź hasło..");
                    nameTxt.requestFocus();
                }
                else if(surname.isEmpty()){
                    surnameTxt.setError("Proszę wprowadź hasło..");
                    surnameTxt.requestFocus();
                }
                else if(number.isEmpty()){
                    numberTxt.setError("Proszę wprowadź hasło..");
                    numberTxt.requestFocus();
                }else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Rejestracja powiodła się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(RegisterActivity.this,HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }

            }
        });


    }
}
