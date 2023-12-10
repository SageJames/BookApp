package com.bignerdranch.android.myapplication.ui.notes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.myapplication.ui.notes.Note
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id=(:id)")
    suspend fun getNote(id: UUID): Note

    @Update
    suspend fun updateNote(note: Note)

    @Insert
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}