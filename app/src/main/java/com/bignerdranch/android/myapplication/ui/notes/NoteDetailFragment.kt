package com.bignerdranch.android.myapplication.ui.notes

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.myapplication.R
import com.bignerdranch.android.myapplication.databinding.FragmentNoteDetailBinding
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


private const val DATE_FORMAT = "EEE, MMM, dd"
class NoteDetailFragment: Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null

    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: NoteDetailFragmentArgs by navArgs()

    private val noteDetailViewModel: NoteDetailViewModel by viewModels{
        NoteDetailViewModelFactory(args.noteId)
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            noteDetailViewModel.updateNote { oldNote ->
                oldNote.copy(photoFileName = photoName)
            }
        }
    }
    private var photoName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            noteTitle.doOnTextChanged { text, _, _, _ ->
                noteDetailViewModel.updateNote { oldNote ->
                    oldNote.copy(title = text.toString())
                }
            }

            noteDescription.doOnTextChanged { text, _, _, _ ->
                noteDetailViewModel.updateNote { oldNote ->
                    oldNote.copy(description = text.toString())
                }
            }

            noteDelete.setOnClickListener {
                deleteCurrentNote()
            }

            noteCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir, photoName)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.bignerdranch.android.myapplication.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }

            val captureImageIntent = takePhoto.contract.createIntent(
                requireContext(),
                Uri.parse("")
            )
            noteCamera.isEnabled = canResolveIntent(captureImageIntent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                noteDetailViewModel.note.collect{ note ->
                    note?.let{updateUi(it)}
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            noteDetailViewModel.updateNote { it.copy(date = newDate) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }

    private fun deleteCurrentNote(){
        noteDetailViewModel.deleteNote()
        findNavController().navigateUp()
    }

    private fun updateUi(note: Note){
        binding.apply {
            if(noteTitle.text.toString() != note.title){
                noteTitle.setText(note.title)
            }
            if(noteDescription.text.toString() != note.description){
                noteDescription.setText(note.description)
            }
            val formatter = SimpleDateFormat("MM/dd/yyyy")
            val strDate = formatter.format(note.date)
            noteDate.text = strDate
            noteDate.setOnClickListener {
                findNavController().navigate(
                    NoteDetailFragmentDirections.selectDate(note.date)
                )
            }
            updatePhoto(note.photoFileName)
        }
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.notePhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }

            if (photoFile?.exists() == true) {
                binding.notePhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.notePhoto.setImageBitmap(scaledBitmap)
                    binding.notePhoto.tag = photoFileName
                    binding.notePhoto.contentDescription =
                        getString(R.string.note_photo_image_description)
                }
            } else {
                binding.notePhoto.setImageBitmap(null)
                binding.notePhoto.tag = null
                binding.notePhoto.contentDescription =
                    getString(R.string.note_photo_no_image_description)
            }
        }
    }



}