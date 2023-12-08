package com.bignerdranch.android.myapplication.ui.notes


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteListViewModel : ViewModel() {

    private val noteRepository = NoteRepository.get()

    private val _notes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())

    val notes: StateFlow<List<Note>>
        get() = _notes.asStateFlow()


    init {
        viewModelScope.launch {
            noteRepository.getNote().collect() {
                _notes.value = it
            }
        }
    }

    suspend fun  addNote(note: Note){
        noteRepository.addNote(note)
    }
}