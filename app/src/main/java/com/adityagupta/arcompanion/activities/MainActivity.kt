package com.adityagupta.arcompanion.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Install splash screen
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupClickListeners() // Set up click listeners for buttons
        setupBottomNavigation() // Set up bottom navigation
    }

    // Set up click listeners for buttons
    private fun setupClickListeners() {
        // Handle button click for CameraActivity
        viewBinding.button2.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        // Handle button click for ARSupportedActivity
        viewBinding.seeTheListButton.setOnClickListener {
            startActivity(Intent(this, ARSupportedActivity::class.java))
        }

        // Handle button click for PdfViewerActivity
        viewBinding.maBooksFloatingActionButton.setOnClickListener {
            startActivity(Intent(this, PdfViewerActivity::class.java))
        }
    }

    // Set up bottom navigation
    private fun setupBottomNavigation() {
        // Set the selected item in the bottom navigation menu
        viewBinding.bottomNavigation.selectedItemId = R.id.home
        // Handle item selection in the bottom navigation menu
        viewBinding.bottomNavigation.setOnItemSelectedListener { item ->
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
            // Start the new activity
            startActivity(intent)
            // Finish the current activity
            finish()
            // Return true to indicate the item selection is handled
            true
        }
    }
}
