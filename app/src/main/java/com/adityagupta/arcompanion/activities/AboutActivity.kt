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
            when(item.itemId) {
                R.id.tech -> {
                    var intent = Intent(applicationContext, TechStackActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true
                }
                R.id.home -> {
                    var intent = Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true

                }
                R.id.list -> {
                    var intent = Intent(applicationContext, ARSupportedActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true
                }
                R.id.about -> {
                    var intent = Intent(applicationContext, AboutActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true
                }
                else -> {
                    false
                }

            }
        }


    }
}