package com.adityagupta.arcompanion

import android.content.Context
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation
import java.io.File
import java.io.IOException

/**
 * Utility class containing functions for extracting information from PDF files.
 */
class Utils {

    /**
     * Extracts information about the PDF, such as the page count.
     *
     * @param context The application context.
     * @param pdfFile The PDF file from which to extract information.
     * @return A string containing information about the PDF.
     */
    fun extractPdfInfo(context: Context, pdfFile: File): String {
        val stringBuilder = StringBuilder()

        try {
            // Open a ParcelFileDescriptor for the PDF file
            val parcelFileDescriptor: ParcelFileDescriptor =
                context.contentResolver.openFileDescriptor(pdfFile.toUri(), "r") ?: return ""

            // Create a PdfRenderer for the PDF file
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            val pageCount = pdfRenderer.pageCount

            // Append page count information to the string
            stringBuilder.append("Page Count: $pageCount\n")

            // Extract additional information as needed
            // For example, you can loop through each page and extract text or images

            // Log success
            Log.i(TAG, "PDF information extracted successfully.")

            // Close the PdfRenderer
            pdfRenderer.close()

        } catch (e: IOException) {
            // Log error
            Log.e(TAG, "Error extracting PDF information: ${e.message}")

            // Handle IOException
            e.printStackTrace()
        }

        // Return the accumulated information as a string
        return stringBuilder.toString()
    }

    /**
     * Extracts the title of the PDF document.
     *
     * @param context The application context.
     * @param pdfUri The URI of the PDF file.
     * @return The title of the PDF document.
     */
    fun extractPdfInfo(context: Context, pdfUri: Uri): PDDocumentInformation{
        // Initialize PDFBox resource loader
        PDFBoxResourceLoader.init(context)

        // Convert the PDF URI to a File
        val pdfFile = uriToFile(context, pdfUri)


            // Load the PDF document
            val document = PDDocument.load(pdfFile)

            // Extract document information
            val documentInfo: PDDocumentInformation = document.documentInformation

            // Log the creator information
            Log.i(TAG, "PDF Title extracted successfully. Creator: ${documentInfo.keywords}")

            // Return the documentInfo
            return documentInfo

    }

    /**
     * Extracts the file name from a URI.
     *
     * @param context The application context.
     * @param uri The URI of the file.
     * @return The file name.
     */
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        // Query the content resolver to get the display name
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                // Check if the "_display_name" column exists in the result set
                val displayNameIndex = cursor.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    // Retrieve the display name if the column index is valid
                    return cursor.getString(displayNameIndex) ?: ""
                } else {
                    // Log a warning if the column index is -1
                    Log.w(TAG, "Column '_display_name' not found in the result set.")
                }
            }
        }

        // Return an empty string if the file name cannot be retrieved
        return ""
    }

    fun processTextFromFirebaseVisionText(resultText: FirebaseVisionText): Pair<List<String>, List<Rect>> {
        val extractedWords = mutableListOf<String>()
        val boundingBoxCoordinates = mutableListOf<Rect>()

        if (resultText.textBlocks.isEmpty()) {
            Log.e("FirebaseVision", "The FirebaseVisionText object is Empty")
            return Pair(emptyList(), emptyList())
        }

        for (block in resultText.textBlocks) {
            for (line in block.lines) {
                line.elements.forEach { element ->
                    val elementText = element.text
                    val elementFrame = element.boundingBox

                    removeSpecialCharacter(elementText)?.let { extractedWords.add(it) }
                    elementFrame?.let { boundingBoxCoordinates.add(it) }
                }
            }
        }

        return Pair(extractedWords, boundingBoxCoordinates)

    }

    /**
     * Converts a URI to a File.
     *
     * @param context The application context.
     * @param uri The URI to convert.
     * @return The corresponding File object.
     */
    public fun uriToFile(context: Context, uri: Uri): File {
        // Open an input stream from the URI
        val inputStream = context.contentResolver.openInputStream(uri)

        // Create a temporary output file
        val outputFile = File(context.cacheDir, "temp.epub")

        try {
            // Use extension functions for InputStream and OutputStream to simplify resource handling
            inputStream?.use { input ->
                outputFile.createNewFile()
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Log success
            Log.i(TAG, "URI converted to File successfully.")

        } catch (e: IOException) {
            // Log error
            Log.e(TAG, "Error converting URI to File: ${e.message}")

            // Handle IOException
            e.printStackTrace()
        }

        // Return the created File
        return outputFile
    }

    public fun removeSpecialCharacter(input: String): String {
        return input.filter { it in 'A'..'Z' || it in 'a'..'z' }
    }


    companion object {
        // Tag for logging purposes
        private const val TAG = "PDF_UTILS"
        const val FIREBASE_VISION_TAG = "FirebaseVision"
        const val MEANING_BOTTOM_SHEET_TAG = "MeaningBottomSheet"
    }
}
