package com.example.dentistapplication.ui.dHome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class dHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public dHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj będzie główny ekran aplikacji, informacja o tym o jakiej godzinie są najbliższe wizyty i kim są pacjenci");
    }

    public LiveData<String> getText() {
        return mText;
    }
}