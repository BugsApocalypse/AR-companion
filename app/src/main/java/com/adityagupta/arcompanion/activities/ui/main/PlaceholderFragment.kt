package com.adityagupta.arcompanion.activities.ui.main

import DocumentsAdapter
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.adityagupta.arcompanion.ARCompanionApplication
import com.adityagupta.arcompanion.Utils
import com.adityagupta.arcompanion.databinding.FragmentLibraryBinding
import com.adityagupta.data.local.entities.Document
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation
import java.io.File
import java.io.FileOutputStream

class PlaceholderFragment : Fragment() {



    private lateinit var sharedPreferences: SharedPreferences


    // ViewModel instance
    // View binding instance
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val pageViewModel: PageViewModel by activityViewModels {
        LibraryFragmentViewModelFactory(
            (activity?.application as ARCompanionApplication).database.documentDao()

        )

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the ViewModel using ViewModelProvider
        pageViewModel.setIndex(requireArguments().getInt(ARG_SECTION_NUMBER, 1))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        pageViewModel.allDocuments.observe(viewLifecycleOwner, Observer { documents ->
            if(documents.isEmpty()){
                binding.lfNothingFoundConstraintLayout.visibility = View.VISIBLE
            }else {
                binding.lfNothingFoundConstraintLayout.visibility = View.GONE
            }
            binding.libraryDocumentsRv.adapter = DocumentsAdapter(requireContext(), documents)
        })

        binding.adddDocumentFloatingActionButton.setOnClickListener {
            openFilePicker.launch("application/pdf")
        }


        // Set a click listener for the add button to pick PDF
        binding.lfAddBooksButton.setOnClickListener {
            // Hide the "nothing found" layout
            binding.lfNothingFoundConstraintLayout.visibility = View.GONE
            // Launch the file picker for PDFs
            openFilePicker.launch("application/pdf")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Check and request permissions

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Permissions granted, proceed with file copying logic
                } else {
                    // Permissions denied, handle accordingly (e.g., show a message or exit)
                }
            }
        }
    }



    private val openFilePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val pdfInfo = Utils().extractPdfInfo(requireContext(), uri)

                val pdfTitle = pdfInfo.title ?: Utils().getFileNameFromUri(requireContext(), uri)

                val document = createDocumentFromPdfInfo(uri.toString(), pdfTitle, pdfInfo)

                pageViewModel.insertDocument(document)
                if (hasPermissions()) {
                    // Permissions are already granted, proceed with file copying logic
                    copySelectedPdf(uri)
                } else {
                    // Request permissions
                    requestPermissions()
                    copySelectedPdf(uri)
                }


            }
        }

    private fun copySelectedPdf(uri: Uri) {
        val selectedPdfUri: Uri = uri // Obtain the URI of the selected PDF

        // Get an input stream from the content resolver
        val inputStream = requireContext().contentResolver.openInputStream(selectedPdfUri)

        // Create a destination file in your app's data directory
        val destinationFile = File(requireContext().filesDir, "copied_file.pdf")

        // Copy the file
        inputStream?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        }

        // Save the file path in SharedPreferences
        sharedPreferences.edit().putString(FILE_PATH_KEY, destinationFile.absolutePath).apply()
    }


    // Function to create a Document object from PdfInfo and other details
    private fun createDocumentFromPdfInfo(
        docLocalUri: String,
        pdfTitle: String,
        pdfInfo: PDDocumentInformation
    ): Document {
        return Document(
            title = pdfTitle,
            author = pdfInfo.author ?: "",
            creationDate = pdfInfo.creationDate.time.toString(),
            creator = pdfInfo.creator ?: "",
            producer = pdfInfo.producer ?: "",
            subject = pdfInfo.subject ?: "",
            docLocalUri = docLocalUri
        )

    }

    private fun hasPermissions(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return readPermission && writePermission
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }


    // Clean up the view binding instance to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Argument key for section number
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val PREFS_NAME = "MyPrefsFile"
        private const val FILE_PATH_KEY = "filePath"
        private const val PERMISSION_REQUEST_CODE = 100
        // Create a new instance of this fragment with the provided section number
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
