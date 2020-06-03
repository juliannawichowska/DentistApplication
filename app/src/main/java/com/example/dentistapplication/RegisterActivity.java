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

/*
    aktywność, w której następuje proces rejestracji uzytkownika w aplikacji
 */

public class RegisterActivity extends AppCompatActivity {
    EditText emailTxt, passwordTxt,nameTxt, surnameTxt, numberTxt;
    Button registerBtn;
    String userType;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

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

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //reakcja na kliknięciu przycisku "Zarejestruj się"
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* zapisanie do zmiennych wartości z pól tekstowych
                    - adres e-mail
                    - hasło
                    - imię
                    - nazwisko
                    - numer telefonu
                 */
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                final String name = nameTxt.getText().toString();
                final String surname = surnameTxt.getText().toString();
                final String number = numberTxt.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if(email.isEmpty()){
                    emailTxt.setError("Proszę wprowadź email..");
                    emailTxt.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordTxt.setError("Proszę wprowadź hasło..");
                    passwordTxt.requestFocus();
                }
                else if(name.isEmpty()){
                    nameTxt.setError("Proszę wprowadź imię..");
                    nameTxt.requestFocus();
                }
                else if(surname.isEmpty()){
                    surnameTxt.setError("Proszę wprowadź nazwisko..");
                    surnameTxt.requestFocus();
                }
                else if(number.isEmpty()){
                    numberTxt.setError("Proszę wprowadź numer telefonu..");
                    numberTxt.requestFocus();
                }else {
                    //odwołanie się do instancji FirebaseAuth i utworzenie nowego użytkownika
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                //rejestracja nie powiodła się
                                Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                            } else {
                                //rejestracja powiodła się, stworzony został nowy użytkownika w sekcji 'Authentication', należy jeszcze dodać użytkownika do tabeli w bazie danych

                                //pobranie aktualnie zalogowanego użytkownika
                                FirebaseUser user = mAuth.getCurrentUser();

                                //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                                assert user != null;
                                String email = user.getEmail();
                                String uid = user.getUid();

                                //utworzenie HashMap i uzupełnienie jej danymi na temat nowego użytkownika
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("name", name);
                                hashMap.put("surname", surname);
                                hashMap.put("number", number);
                                hashMap.put("userType", userType);
                                hashMap.put("imageURL", "");
                                hashMap.put("address", "");
                                hashMap.put("description", "");

                                //inicjacja instancji FirebaseDatabase
                                firebaseDatabase = FirebaseDatabase.getInstance();

                                //referencja do ścieżki do tabeli 'Users'
                                DatabaseReference reference1 = firebaseDatabase.getReference("Users");
                                //przesłanie hashMap z danymi użytkownika do bazy
                                reference1.child(uid).setValue(hashMap);

                                //sprawdzanie czy użytkownik się zalogował jako pacjent, czy jako lekarz
                                if("patient".equals(userType)){
                                    //referencja do ścieżki do tabeli 'Patients'
                                    DatabaseReference reference2 = firebaseDatabase.getReference("Patients");
                                    //przesłanie hashMap z danymi użytkownika do bazy
                                    reference2.child(uid).setValue(hashMap);
                                    //otwórz aplikację po stronie pacjenta
                                    Toast.makeText(RegisterActivity.this, "Zalogowałeś się do aplikacji DentAPP jako pacjent danym mailem : " + email, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, pHomeActivity.class));
                                    finish();
                                } else {
                                    //referencja do ścieżki do tabeli 'Doctors'
                                    DatabaseReference reference3 = firebaseDatabase.getReference("Doctors");
                                    //przesłanie hashMap z danymichild(uid). użytkownika do bazy
                                    reference3.setValue(hashMap);
                                    //otwórz aplikację po stronie pacjenta
                                    Toast.makeText(RegisterActivity.this, "Zalogowałeś się do aplikacji DentAPP jako doktor danym mailem : " + email, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, dHomeActivity.class));
                                    finish();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    //funkcja sprawdzającą którą opcję rejestracji wybrał użytkownik - lekarz czy pacjent
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        //Nadanie wartości parametrowi 'userType'
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
