package com.bignerdranch.android.myapplication.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.myapplication.ui.notes.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.Date
import java.util.UUID


class GalleryViewModel : ViewModel() {
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    fun fetchNotesFromFirebase() {
        val database = FirebaseDatabase.getInstance().getReference("notes")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notes = ArrayList<Note>()
                for (noteSnapshot in dataSnapshot.children) {

                    val msb = noteSnapshot.child("id/mostSignificantBits").getValue(Long::class.java)
                    val lsb = noteSnapshot.child("id/leastSignificantBits").getValue(Long::class.java)
                    val uuid = if (msb != null && lsb != null) UUID(msb, lsb) else null

                    val title = noteSnapshot.child("title").getValue(String::class.java) ?: ""
                    val description = noteSnapshot.child("description").getValue(String::class.java)
                    val photoFileName = noteSnapshot.child("photoFileName").getValue(String::class.java)

                    val year    =   noteSnapshot.child("date/year").getValue(Int::class.java) ?: 0
                    val month   =   noteSnapshot.child("date/month").getValue(Int::class.java) ?: 0
                    val date    =   noteSnapshot.child("date/date").getValue(Int::class.java) ?: 0
                    val hrs     =   noteSnapshot.child("date/hours").getValue(Int::class.java) ?: 0
                    val min     =   noteSnapshot.child("date/minutes").getValue(Int::class.java) ?: 0
                    val sec     =   noteSnapshot.child("date/seconds").getValue(Int::class.java) ?: 0
                    val data    =  Date(Date.UTC(year,month, date, hrs, min, sec))


                    if (uuid != null) {
                        val note = Note(uuid, title, description, data, photoFileName)
                        notes.add(note)
                    }
                }
                _notes.value = notes
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Access Fail", "Accessing the Firebase Database and failed (populating our gallery)")
            }
        })
    }



}
