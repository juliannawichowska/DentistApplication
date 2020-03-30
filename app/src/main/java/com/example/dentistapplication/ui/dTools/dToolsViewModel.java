package com.example.dentistapplication.ui.dTools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class dToolsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public dToolsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj będą ewentualne ustawienia, na ten moment nie są zadeklarowane");
    }

    public LiveData<String> getText() {
        return mText;
    }
}