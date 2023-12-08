package com.bignerdranch.android.myapplication.ui.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.myapplication.ui.notes.Note

@Database(entities =  [ Note::class ], version = 1)
@TypeConverters(NoteTypeConverters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

val migration_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Note ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}