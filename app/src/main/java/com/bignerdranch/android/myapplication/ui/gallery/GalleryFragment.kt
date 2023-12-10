package com.bignerdranch.android.myapplication.ui.gallery

import android.os.Bundle
import android.util.Log
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
import com.bignerdranch.android.myapplication.databinding.FragmentGalleryBinding
import com.bignerdranch.android.myapplication.databinding.FragmentNotesListBinding
import com.bignerdranch.android.myapplication.ui.notes.Note
import com.bignerdranch.android.myapplication.ui.notes.NoteListAdapter
import com.bignerdranch.android.myapplication.ui.notes.NoteListFragmentDirections
import com.bignerdranch.android.myapplication.ui.notes.NoteListViewModel
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

private var _binding: FragmentGalleryBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding
      get() = checkNotNull(_binding){
          "Cannot access binding because it is null. Is the view visible?"
      }

    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val galleryAdapter = GalleryAdapter()
        binding.noteRecyclerView.adapter = galleryAdapter
        binding.noteRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryViewModel.fetchNotesFromFirebase()

        galleryViewModel.notes.observe(viewLifecycleOwner, { updateUi(it) })
    }

    private fun updateUi(list: List<Note>) {
        (binding.noteRecyclerView.adapter as GalleryAdapter).submitList(list)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}