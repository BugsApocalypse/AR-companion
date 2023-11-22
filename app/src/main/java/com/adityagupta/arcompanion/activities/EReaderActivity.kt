package com.adityagupta.arcompanion.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adityagupta.arcompanion.databinding.ActivityEreaderBinding
import com.adityagupta.data.local.entities.Document
import java.io.File


class EReaderActivity : AppCompatActivity() {
    private lateinit var eReaderBinding: ActivityEreaderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding
        eReaderBinding = ActivityEreaderBinding.inflate(layoutInflater)
        setContentView(eReaderBinding.root)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val filePath = sharedPreferences.getString(FILE_PATH_KEY, null)

        // Check if the file path is not null and then use it
        filePath?.let { loadPdfFromUri(File(it)) }


    }


    // Load PDF from the given URI
    private fun loadPdfFromUri(file: File) {
        // Parse the URI
        // Load PDF into the PDFView
        eReaderBinding.pdfView.fromFile(file)
            .swipeHorizontal(true)
            .enableSwipe(true)
            .enableDoubletap(true)
            .autoSpacing(true)
            .load()
        // Log success message
    }


    // Handle the PDF URI from the intent


    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val FILE_PATH_KEY = "filePath"
    }


}


