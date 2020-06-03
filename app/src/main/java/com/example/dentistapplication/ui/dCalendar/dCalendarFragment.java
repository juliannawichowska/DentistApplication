package com.example.dentistapplication.ui.dCalendar;

import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.dentistapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/*
    Fragment ten odpowiada za wyświetlenie kalendarza lekarza
 */

public class dCalendarFragment extends Fragment implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker, btnConfirm;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;


    Calendar c = Calendar.getInstance();
    private dCalendarViewModel dCalendarViewModel;
    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dCalendarViewModel = ViewModelProviders.of(this).get(dCalendarViewModel.class);
        View view = inflater.inflate(R.layout.d_fragment_calendar, container, false);
        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();


        btnDatePicker = view.findViewById(R.id.btn_date);
        btnTimePicker = view.findViewById(R.id.btn_time);
        btnConfirm = view.findViewById(R.id.btn_save);
        txtDate = view.findViewById(R.id.in_date);
        txtTime = view.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
// Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,  int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            timePickerDialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) { txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }
        if (v == btnConfirm)
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            //referencja do ścieżki do tabeli 'Users'
            databaseReference = firebaseDatabase.getReference("Dates");
            //zapisanie do zmiennej wprowadzonego adresu
            String value1 = txtDate.getText().toString().trim() ;
            String value2 = txtTime.getText().toString().trim() ;//+ txtTime.getText().toString().trim();
            String free  = "true";
            //utworzenie HashMap z adresem gabinetu
            Map<String, Object> result = new HashMap<>();
            result.put("date", value1);
            result.put("hour", value2);
            result.put("free", free);
            String key = databaseReference.push().getKey();

            //zaktualizowanie bazy danych o wolny termin
            databaseReference.child(user.getUid()).child(key).setValue(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //zaktualizowano pomyślnie
                            timePickerDialog.dismiss();
                            Toast.makeText(getActivity(),"Zapisano..", Toast.LENGTH_SHORT).show();;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //aktualizacja nie powiodła się
                            timePickerDialog.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}