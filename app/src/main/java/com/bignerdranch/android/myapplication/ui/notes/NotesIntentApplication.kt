package com.bignerdranch.android.myapplication.ui.notes

import android.app.Application

class NotesIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }
}