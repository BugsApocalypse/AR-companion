package com.adityagupta.arcompanion.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.databinding.ActivityMainBinding
import com.adityagupta.arcompanion.databinding.ActivityTechStackBinding

class TechStackActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityTechStackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTechStackBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.bottomNavigation.selectedItemId = R.id.tech

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
            intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            // Start the new activity
            startActivity(intent)
            // Finish the current activity
            finish()
            // Return true to indicate the item selection is handled
            true
        }
    }
}