package com.adityagupta.arcompanion.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.adityagupta.arcompanion.ARCompanionApplication
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.Utils
import com.adityagupta.arcompanion.activities.ui.main.LibraryFragmentViewModelFactory
import com.adityagupta.arcompanion.activities.ui.main.PageViewModel
import com.adityagupta.arcompanion.databinding.ActivityEreaderBinding
import com.adityagupta.arcompanion.viewmodels.MeaningActivityViewModel
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnLongPressListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
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
    private var extractedWords = mutableListOf<String>()
    private var boundingBoxCoordinates = mutableListOf<Rect>()
    private var currentDocumentLocation: Long = 0
    private var documentId: Long? = null
    private val pageViewModel: PageViewModel by viewModels {
        LibraryFragmentViewModelFactory(
            (this.application as ARCompanionApplication).database.documentDao()
        )
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eReaderBinding = ActivityEreaderBinding.inflate(layoutInflater)
        setContentView(eReaderBinding.root)

        pdfUri = intent.getParcelableExtra("selectedDocumentUri")
        currentDocumentLocation = intent.getLongExtra("selectedDocumentLocation", 0L)
        documentId = intent.getLongExtra("selectedDocumentId", 0L)


        pdfUri?.let {
            loadPdfFromUri(it)
        } ?: run {
            Log.e(TAG, "Error: pdfUri is null")
            finish()
        }
        eReaderBinding.bottomNavigation.selectedItemId = R.id.library
        eReaderBinding.bottomNavigation.setOnItemSelectedListener { item ->
            // Create intent based on the selected item
            val intent = when (item.itemId) {
                R.id.tech -> Intent(this, TechStackActivity::class.java)
                R.id.home -> Intent(this, MainActivity::class.java)
                R.id.list -> Intent(this, ARSupportedActivity::class.java)
                R.id.about -> Intent(this, AboutActivity::class.java)
                R.id.library -> Intent(this, LibraryActivity::class.java)

                else -> null
            }
            // Add flags to the intent
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            // Start the new activity
            startActivity(intent)
            // Finish the current activity
            finish()
            // Return true to indicate the item selection is handled
            true
        }

    }

    private fun loadPdfFromUri(pdfUri: Uri) {
        val filePath = getFilePathFromSharedPreferences(pdfUri.toString())
        Log.i("document2", filePath.toString())
        Log.i("document2", pdfUri.toString())

        filePath?.let { displayPdf(File(it)) }
    }

    private fun displayPdf(file: File) {
        eReaderBinding.pdfView.fromFile(file)
            .swipeHorizontal(true)
            .enableSwipe(true)
            .enableAnnotationRendering(true)
            .enableAntialiasing(true)
            .defaultPage(currentDocumentLocation.toInt())
            .autoSpacing(true)
            .pageSnap(true)
            .pageFling(true)
            .onLoad(onLoadCompleteListener)
            .onPageChange(pageChangeListener)
            .onLongPress(longPressListener)
            .load()
    }

    private val onLoadCompleteListener = OnLoadCompleteListener {
        with(eReaderBinding) {
            pageNumberSlider.stepSize = 1F
            pageNumberSlider.valueFrom = 0F
            pageNumberSlider.valueTo = it.toFloat()
        }

        createBitmapAndParseText()
    }

    private val pageChangeListener = OnPageChangeListener { page, pageCount ->
        val currentPageNumber = page + 1
        eReaderBinding.pageNumberTextView.text = "Page $currentPageNumber / $pageCount"
        eReaderBinding.pageNumberSlider.value = currentPageNumber.toFloat()
        showViews()
        hideViewsJob?.cancel()

        hideViewsJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            hideViews()

            createBitmapAndParseText()
            pageViewModel.updateCurrentLocation(page.toLong(), documentId!! )
        }
    }

    private val longPressListener = OnLongPressListener {
        val (x, y) = it.x to it.y
        val index = boundingBoxCoordinates.indexOfFirst { i ->
            x > i.left && x < i.right && y > i.top && y < i.bottom
        }
        if (index != -1) {
            Log.i(Utils.FIREBASE_VISION_TAG, "Long press on ${extractedWords[index]}")
            val modalBottomSheet = MeaningBottomSheetFragment.newInstance(extractedWords[index])
            modalBottomSheet.show(supportFragmentManager, Utils.MEANING_BOTTOM_SHEET_TAG)

        }
    }

    private fun createBitmapAndParseText() {
        val bitmap = getBitmapFromPDFView()

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                processResultText(firebaseVisionText)
            }
            .addOnFailureListener {
                Log.i(Utils.FIREBASE_VISION_TAG, "failed")
            }
    }

    private fun getBitmapFromPDFView(): Bitmap {
        val view = window.decorView.rootView.findViewById<PDFView>(R.id.pdfView)
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }


    private fun processResultText(resultText: FirebaseVisionText) {
        val (words, coordinates) = Utils().processTextFromFirebaseVisionText(resultText)
        extractedWords = words.toMutableList()
        boundingBoxCoordinates = coordinates.toMutableList()
        Log.i(Utils.FIREBASE_VISION_TAG, extractedWords.toString())
    }




    private fun getFilePathFromSharedPreferences(pdfUri: String): String? {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(pdfUri, null)
    }


    private fun showViews() {
        with(eReaderBinding) {
            pageNumberTextView.visibility = View.VISIBLE
            pageNumberSlider.visibility = View.VISIBLE
        }
    }

    private fun hideViews() {
        with(eReaderBinding) {
            pageNumberTextView.visibility = View.INVISIBLE
            pageNumberSlider.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        hideViewsJob?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val PREFS_NAME = "MyPrefsFile"
        private const val TAG = "EReaderActivity"
    }
}
