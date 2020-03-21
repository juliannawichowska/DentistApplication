package com.example.dentistapplication.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CalendarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj umieszczony będzie grafik danego dentysty z zajętymi i wolnymi terminami");
    }

    public LiveData<String> getText() {
        return mText;
    }
}