package com.bignerdranch.android.myapplication.ui.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Note(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String? = null,
    val date: Date = Date(),
    val photoFileName: String? = null
)
