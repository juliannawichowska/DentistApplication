package com.example.dentistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText emailTxt, passwordTxt,nameTxt, surnameTxt, numberTxt;
    Button registerBtn;
    private FirebaseAuth mAuth;
    String userType;


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
                final String name = nameTxt.getText().toString();
                final String surname = surnameTxt.getText().toString();
                final String number = numberTxt.getText().toString();


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
                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                            } else {

                                FirebaseUser user = mAuth.getCurrentUser();

                                //get user email and uid
                                assert user != null;
                                String email = user.getEmail();
                                String uid = user.getUid();

                                //Store info in Firebase database
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("name", name);
                                hashMap.put("surname", surname);
                                hashMap.put("number", number);
                                hashMap.put("userType", userType);

                                //firebase database instance
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                //path to store data named "Patients"
                                DatabaseReference reference1 = firebaseDatabase.getReference("Users");
                                //put data in database
                                reference1.child(uid).setValue(hashMap);
                                //start patients app
                                //check if user is patient or doctor
                                if("patient".equals(userType)){
                                    //path to store data named "Patients"
                                    DatabaseReference reference2 = firebaseDatabase.getReference("Patients");
                                    //put data in database
                                    reference2.child(uid).setValue(hashMap);
                                    //start patients app
                                    Toast.makeText(RegisterActivity.this, "Zalogowałeś się do aplikacji DentAPP jako pacjent danym mailem : " + email, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this,PHomeActivity.class));
                                    finish();
                                } else {
                                    //path to store data named "Doctors"
                                    DatabaseReference reference3 = firebaseDatabase.getReference("Doctors");
                                    //put data in database
                                    reference3.child(uid).setValue(hashMap);
                                    //start doctors app
                                    Toast.makeText(RegisterActivity.this, "Zalogowałeś się do aplikacji DentAPP jako doktor danym mailem : " + email, Toast.LENGTH_LONG).show();
                                    //go to results activity after logging in
                                    startActivity(new Intent(RegisterActivity.this,DHomeActivity.class));
                                    finish();
                                }

                            }
                        }
                    });
                }

            }
        });




    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_patient:
                if (checked)
                    userType = "patient";
                    break;
            case R.id.radio_doctor:
                if (checked)
                    userType = "doctor";
                    break;
        }
    }
}
