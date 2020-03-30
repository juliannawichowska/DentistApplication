package com.example.dentistapplication.ui.dCalendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class dCalendarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public dCalendarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj umieszczony będzie grafik danego dentysty z zajętymi i wolnymi terminami");
    }

    public LiveData<String> getText() {
        return mText;
    }
}