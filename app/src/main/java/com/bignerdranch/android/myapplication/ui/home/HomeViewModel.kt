package com.bignerdranch.android.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment. There will be a Welcome animation with stats and a small " +
                "new books section. Welcome : Total books, Number of entries, and Authors"
    }
    val text: LiveData<String> = _text
}