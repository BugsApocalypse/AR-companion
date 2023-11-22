package com.adityagupta.arcompanion.activities

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.adityagupta.arcompanion.databinding.ActivityEreaderBinding
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class EReaderActivity : AppCompatActivity() {

    private lateinit var eReaderBinding: ActivityEreaderBinding
    private var pdfUri: Uri? = null
    private var delayMillis = 3000L // 3 seconds
    private var hideViewsJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the view binding
        eReaderBinding = ActivityEreaderBinding.inflate(layoutInflater)
        setContentView(eReaderBinding.root)

        // Retrieve the PDF URI from the intent
        pdfUri = intent.getParcelableExtra("selectedDocumentUri")

        // Check if pdfUri is not null before proceeding
        pdfUri?.let {
            // Load and display the PDF
            loadPdfFromUri(it)
        } ?: run {
            // Handle the case where pdfUri is null (e.g., log an error, show a message)
            Log.e(TAG, "Error: pdfUri is null")
            // You might want to finish the activity or take other appropriate action
            finish()
        }


    }

    private fun loadPdfFromUri(pdfUri: Uri) {
        // Retrieve the file path from SharedPreferences
        val filePath = getFilePathFromSharedPreferences(pdfUri.toString())

        // Check if the file path is not null and then load the PDF
        filePath?.let { displayPdf(File(it)) }
    }

    private fun displayPdf(file: File) {
        // Load PDF into the PDFView
        eReaderBinding.pdfView.fromFile(file)
            .swipeHorizontal(true)
            .enableSwipe(true)
            .autoSpacing(true)
            .pageSnap(true)
            .pageFling(true)
            .onLoad(loadCompleteListener)
            .onPageChange(pageChangeListener)
            .onPageScroll(pageScrollListener)
            .load()

        // Log success message or handle other actions as needed
    }

    private fun getFilePathFromSharedPreferences(pdfUri: String): String? {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(pdfUri, null)
    }

    private val pageChangeListener =
        OnPageChangeListener { page, pageCount -> // Update the page number in the TextView
            val currentPageNumber = page + 1 // Page numbers are zero-based
            val pageText = "Page $currentPageNumber / $pageCount"
            eReaderBinding.pageNumberTextView.text = pageText
            eReaderBinding.pageNumberSlider.value = (page + 1).toFloat()
            showViews()
            hideViewsJob?.cancel()
            hideViewsJob = CoroutineScope(Dispatchers.Main).launch {
                delay(delayMillis)
                hideViews()
            }

        }

    private val pageScrollListener =
        OnPageScrollListener { _, _ -> // Update the page number in the TextView


        }

    private val loadCompleteListener = OnLoadCompleteListener {
        // Set the maximum value of the scrollbar when the PDF is loaded
        eReaderBinding.pageNumberSlider.stepSize = 1F
        eReaderBinding.pageNumberSlider.valueFrom = 0F
        eReaderBinding.pageNumberSlider.valueTo = it.toFloat()
    }

    private fun showViews() {
        eReaderBinding.pageNumberTextView.visibility = View.VISIBLE
        eReaderBinding.pageNumberSlider.visibility = View.VISIBLE
    }

    private fun hideViews() {
        eReaderBinding.pageNumberTextView.visibility = View.INVISIBLE
        eReaderBinding.pageNumberSlider.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        // Cancel any ongoing coroutine job
        hideViewsJob?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val TAG = "EReaderActivity"
    }
}
