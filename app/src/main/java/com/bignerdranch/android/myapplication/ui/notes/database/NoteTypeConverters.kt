package com.bignerdranch.android.myapplication.ui.notes.database

import androidx.room.TypeConverter
import java.util.Date

class NoteTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}