package com.adityagupta.arcompanion.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adityagupta.arcompanion.ARCompanionApplication
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.activities.ui.main.SectionsPagerAdapter
import com.adityagupta.arcompanion.databinding.ActivityLibraryBinding

import com.adityagupta.data.local.dao.DocumentDao
import com.adityagupta.data.local.entities.Document

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

    }

    private fun setupBottomNavigation() {
        // Set the selected item in the bottom navigation menu
        binding.bottomNavigation.selectedItemId = R.id.library
        // Handle item selection in the bottom navigation menu
        binding.bottomNavigation.setOnItemSelectedListener { item ->
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