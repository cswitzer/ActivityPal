package com.example.activitypal.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedItem = new MutableLiveData<>();

    public void selectItem(Integer id) {
        selectedItem.setValue(id);
    }
    public LiveData<Integer> getSelectedItem() {
        return selectedItem;
    }
}
