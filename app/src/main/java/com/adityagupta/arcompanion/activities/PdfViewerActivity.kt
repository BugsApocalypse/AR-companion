package com.adityagupta.arcompanion.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adityagupta.arcompanion.activities.ui.theme.ARCompanionTheme
import com.adityagupta.data.local.entities.Document
import java.io.File
import java.io.FileOutputStream

class PdfViewerActivity : ComponentActivity() {
    // PDF rendering variables
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var pdfFile: File
    private lateinit var currentPage: PdfRenderer.Page

    private fun displayPDF(context: Context, uri: Uri) {
        Log.i("pdf", uri.toString())

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
//        binding.pdfContentImageView.setImageBitmap(bitmap)

        // Close the current page when done
        currentPage.close()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {
            ARCompanionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ARCompanionTheme {
        Greeting("Android")
    }
}