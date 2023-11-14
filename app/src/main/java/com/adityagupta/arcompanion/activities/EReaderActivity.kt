package com.adityagupta.arcompanion.activities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.databinding.ActivityEreaderBinding
import com.adityagupta.data.local.database.AppDatabase
import com.adityagupta.data.local.entities.Document
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class EReaderActivity : AppCompatActivity() {
    private lateinit var eReaderBinding: ActivityEreaderBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding
        eReaderBinding = ActivityEreaderBinding.inflate(layoutInflater)
        setContentView(eReaderBinding.root)

        handlePdfUriIntent()

    }

    // Load PDF from the given URI
    private fun loadPdfFromUri(pdfUri: String) {
        // Parse the URI
        val uri: Uri = Uri.parse(pdfUri)

        // Load PDF into the PDFView
        eReaderBinding.pdfView.fromUri(uri)
            .swipeHorizontal(true)
            .enableSwipe(true)
            .enableDoubletap(true)
            .autoSpacing(true)
            .load()
        // Log success message
        Log.i("pdf", "Loaded PDF from URI: $pdfUri")
    }



    // Handle the PDF URI from the intent
    private fun handlePdfUriIntent() {
        // Get the PDF URI from the intent
        val pdfUri = intent.getStringExtra("pdfUri")
        // Check if the PDF URI is not null
        pdfUri?.let {
            // Log the PDF URI
            Log.i("pdf", it)
            // Load the PDF from the URI
            loadPdfFromUri(it)
        }
    }




}
