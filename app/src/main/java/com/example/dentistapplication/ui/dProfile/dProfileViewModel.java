package com.example.dentistapplication.ui.dProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class dProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public dProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj lekarz będzie ustawiał swoje dane osobowe, opis siebie i zdjęcie");
    }

    public LiveData<String> getText() {
        return mText;
    }
}