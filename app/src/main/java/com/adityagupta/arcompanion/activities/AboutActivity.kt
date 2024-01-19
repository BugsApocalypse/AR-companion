package com.adityagupta.arcompanion.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.databinding.ActivityAboutBinding
import com.adityagupta.arcompanion.databinding.ActivityArsupportedBinding


class AboutActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.bottomNavigation.selectedItemId = R.id.about

        viewBinding.buyUsCoffeeButton.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.buymeacoffee.com/arcompanion") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        viewBinding.aditya.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.linkedin.com/in/adityagupta985/") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        viewBinding.rahul.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.linkedin.com/in/rahul-raj-3b6b45113/") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        viewBinding.sohail.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.linkedin.com/in/ahmadsohail404/") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        viewBinding.tuba.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.linkedin.com/in/tuba-ali-b4062122a/") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

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
            intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            startActivity(intent)
            // Finish the current activity
            finish()
            // Return true to indicate the item selection is handled
            true
        }


    }
}