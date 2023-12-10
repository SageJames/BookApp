package com.bignerdranch.android.myapplication.ui.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class NoteDetailViewModel(noteId: UUID): ViewModel(){
    private val noteRepository = NoteRepository.get()


    private val _note: MutableStateFlow<Note?> = MutableStateFlow(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    init {
        viewModelScope.launch {
            _note.value = noteRepository.getNote(noteId)
        }
    }

    fun updateNote(onUpdate: (Note) -> Note){
        _note.update { oldNote ->
            oldNote?.let{
                onUpdate(it)
            }
        }
    }

    fun deleteNote(){
        note.value?.let {
            noteRepository.deleteNote(it)
        }
        _note.value = null
    }

    fun shareNote() {
        note.value?.let { note ->
            val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("notes")

            val noteId = database.push().getKey() ?: return

            database.child(noteId).setValue(note)
                .addOnSuccessListener {
                    noteRepository.deleteNote(note)
                }
                .addOnFailureListener { e ->
                    Log.e("Upload_Failed", "Failed to upload note to Firebase", e)
                }
        }
    }



    override fun onCleared() {
        super.onCleared()
        note.value?.let { noteRepository.updateNote(it) }
    }
}

class NoteDetailViewModelFactory(private val noteId: UUID): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteDetailViewModel(noteId) as T
    }
}