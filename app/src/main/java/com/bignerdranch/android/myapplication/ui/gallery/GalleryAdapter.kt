package com.bignerdranch.android.myapplication.ui.gallery

import com.bignerdranch.android.myapplication.ui.notes.Note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.myapplication.databinding.ListItemNoteBinding
import com.bignerdranch.android.myapplication.databinding.ListItemSharedNoteBinding
import java.text.SimpleDateFormat
import java.util.UUID

class GalleryHolder(private val binding: ListItemSharedNoteBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) {
        binding.sharedNoteTitle.text = note.title
        binding.sharedNoteDescription.text = note.description
        val formatter = SimpleDateFormat("MM/dd/yyyy")
        val strDate = formatter.format(note.date)
        binding.sharedNoteDate.text = strDate
    }
}

class GalleryAdapter : RecyclerView.Adapter<GalleryHolder>() {
    private var notes = listOf<Note>()

    fun submitList(list: List<Note>) {
        notes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemSharedNoteBinding.inflate(inflater, parent, false)
        return GalleryHolder(binding)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }
}
