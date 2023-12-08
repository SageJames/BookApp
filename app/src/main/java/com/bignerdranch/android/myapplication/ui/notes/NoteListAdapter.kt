package com.bignerdranch.android.myapplication.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.myapplication.databinding.ListItemNoteBinding
import java.util.UUID

class NoteHolder(
    private val binding: ListItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note, onNoteClicked: (noteId: UUID) -> Unit){
        binding.noteTitle.text = note.title
        binding.noteDate.text = note.date.toString()

        binding.root.setOnClickListener {
            onNoteClicked(note.id)
        }
    }
}


class NoteListAdapter(
    private val notes: List<Note>,
    private val onNoteClicked: (noteId: UUID) -> Unit
): RecyclerView.Adapter<NoteHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNoteBinding.inflate(inflater, parent, false)
        return NoteHolder(binding)
    }

    override fun getItemCount() = notes.size


    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = notes[position]
        holder.bind(note, onNoteClicked)
    }
}