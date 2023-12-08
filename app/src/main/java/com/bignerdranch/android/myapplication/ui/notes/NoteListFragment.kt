package com.bignerdranch.android.myapplication.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.bignerdranch.android.myapplication.databinding.FragmentNotesListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class NoteListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null

    private val binding
      get() = checkNotNull(_binding){
          "Cannot access binding because it is null. Is the view visible?"
      }

    private val noteListViewModel: NoteListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)

        binding.noteRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteListViewModel.notes.collect{note ->
                    binding.noteRecyclerView.adapter = NoteListAdapter(note) {noteId ->
                        findNavController().navigate(
                            NoteListFragmentDirections.showNoteDetail(noteId)
                        )
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }



    //Might have to move this to main for plus sign
    fun showNewNote(){
        viewLifecycleOwner.lifecycleScope.launch {
            val newNote = Note(
                id = UUID.randomUUID(),
                title = "",
                description = "",
                date = Date()
            )
            noteListViewModel.addNote(newNote)
            findNavController().navigate(
                NoteListFragmentDirections.showNoteDetail(newNote.id)
            )
        }
    }

}

