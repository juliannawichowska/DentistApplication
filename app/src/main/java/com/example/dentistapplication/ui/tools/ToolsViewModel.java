package com.example.dentistapplication.ui.tools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToolsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ToolsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj będą ewentualne ustawienia, na ten moment nie są zadeklarowane");
    }

    public LiveData<String> getText() {
        return mText;
    }
}