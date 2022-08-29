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
                else -> {
                    false
                }

            }
        }
    }
}