package com.example.dentistapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Tutaj będzie główny ekran aplikacji, informacja o tym o jakiej godzinie są najbliższe wizyty i kim są pacjenci");
    }

    public LiveData<String> getText() {
        return mText;
    }
}