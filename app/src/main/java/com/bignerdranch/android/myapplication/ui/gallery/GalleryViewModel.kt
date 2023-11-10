package com.bignerdranch.android.myapplication.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is new books Fragment. This will display a " +
                "list of books from our API that the reader can look at and buy"
    }
    val text: LiveData<String> = _text
}