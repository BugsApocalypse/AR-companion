package com.adityagupta.arcompanion.activities.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.ViewModelProvider
import com.adityagupta.arcompanion.ARCompanionApplication
import com.adityagupta.arcompanion.Utils
import com.adityagupta.arcompanion.activities.EReaderActivity
import com.adityagupta.arcompanion.activities.TechStackActivity
import com.adityagupta.arcompanion.databinding.FragmentLibraryBinding
import com.adityagupta.data.local.entities.Document
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation
import java.io.File
import java.io.FileOutputStream

class PlaceholderFragment : Fragment() {

    // ViewModel instance
    // View binding instance
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val pageViewModel: PageViewModel by activityViewModels {
        LibraryFragmentViewModelFactory(
            (activity?.application as ARCompanionApplication).database.documentDao()

        )

    }

    // PDF rendering variables
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var pdfFile: File
    private lateinit var currentPage: PdfRenderer.Page

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

        // Set a click listener for the add button to pick PDF
        binding.lfAddBooksButton.setOnClickListener {
            // Hide the "nothing found" layout
            binding.lfNothingFoundConstraintLayout.visibility = View.GONE
            // Launch the file picker for PDFs
            openFilePicker.launch("application/pdf")
        }

        return binding.root
    }

    private val openFilePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val intent = Intent(requireContext(), EReaderActivity::class.java)
                val pdfInfo = Utils().extractPdfInfo(requireContext(), uri)

                val pdfTitle = pdfInfo.title ?: Utils().getFileNameFromUri(requireContext(), uri)

                val document = createDocumentFromPdfInfo(uri.toString(), pdfTitle, pdfInfo)

                pageViewModel.insertDocument(document)

                Log.i("pdf_stuff", pdfTitle)
                intent.putExtra("pdfUri", uri.toString())

                startActivity(intent)
                displayPDF(requireContext(), uri)
            }
        }

    // Function to display the selected PDF
    private fun displayPDF(context: Context, uri: Uri) {

        // Open an input stream from the selected URI
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            // Create a temporary file to store the PDF
            pdfFile = File(context.cacheDir, "temp.pdf")
            pdfFile.createNewFile()
            // Open an output stream for the temporary file
            FileOutputStream(pdfFile).use { outputStream ->
                // Copy the PDF content from the input stream to the temporary file
                inputStream.copyTo(outputStream)
            }
            // Open the PdfRenderer with the temporary PDF file
            pdfRenderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
            // Display the first page of the PDF
            displayPage(0)
        }
    }

    // Function to display a specific page of the PDF
    private fun displayPage(index: Int) {
        // Close the current page if it exists
        if (::currentPage.isInitialized) {
            currentPage.close()
        }

        // Get the specific page from the PdfRenderer
        currentPage = pdfRenderer.openPage(index)

        // Render the page as a bitmap
        val bitmap = Bitmap.createBitmap(currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        // Display the rendered bitmap in the designated image view
        binding.pdfContentImageView.setImageBitmap(bitmap)

        // Close the current page when done
        currentPage.close()
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
            creationDate = pdfInfo.creationDate.toString() ?: "",
            creator = pdfInfo.creator ?: "",
            producer = pdfInfo.producer ?: "",
            subject = pdfInfo.subject ?: "",
            docLocalUri = docLocalUri
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
