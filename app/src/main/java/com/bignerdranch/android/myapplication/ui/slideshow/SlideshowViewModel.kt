package com.bignerdranch.android.myapplication.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is personal collection Fragment. We will have a " +
                "list of new entries for all of the book our reader is reading."
    }
    val text: LiveData<String> = _text
}