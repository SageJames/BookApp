package com.bignerdranch.android.myapplication.ui.notes

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.myapplication.ui.notes.database.NoteDatabase
import com.bignerdranch.android.myapplication.ui.notes.database.migration_1_2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID


private const val DATABASE_NAME = "note-database"
class NoteRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
){
    private val database: NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    )
        .addMigrations(migration_1_2)
        .build()

    fun getNote(): Flow<List<Note>> = database.noteDao().getNotes()

    suspend fun getNote(id: UUID): Note = database.noteDao().getNote(id)

    fun updateNote(note: Note){
        coroutineScope.launch {
            database.noteDao().updateNote(note)
        }
    }

    suspend fun addNote(note: Note){
        database.noteDao().addNote(note)
    }

    companion object{
        private var INSTANCE: NoteRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null){
                INSTANCE = NoteRepository(context)
            }
        }

        fun get(): NoteRepository {
            return INSTANCE
                ?: throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}