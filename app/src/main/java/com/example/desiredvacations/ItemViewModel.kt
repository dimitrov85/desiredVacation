package com.example.desiredvacations

import android.content.ClipData
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    var chosenVacation: Int = 0
}