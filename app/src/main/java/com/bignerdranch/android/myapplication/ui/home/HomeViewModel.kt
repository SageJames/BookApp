package com.bignerdranch.android.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.myapplication.ui.notes.NoteRepository
import kotlinx.coroutines.flow.count
import androidx.lifecycle.asLiveData

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map

class HomeViewModel : ViewModel() {

    private val repository: NoteRepository = NoteRepository.get()

    val noteCount = repository.getNote()
        .map { notes -> notes.size }
        .asLiveData(viewModelScope.coroutineContext)
}