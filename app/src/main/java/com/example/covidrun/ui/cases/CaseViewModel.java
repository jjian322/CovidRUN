package com.example.covidrun.ui.cases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CaseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CaseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is case fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}