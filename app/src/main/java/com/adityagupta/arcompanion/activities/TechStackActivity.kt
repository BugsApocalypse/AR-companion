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
                    startActivity(Intent(applicationContext, TechStackActivity::class.java))
                    true
                }
                R.id.home -> {
                    Log.i("something", "hello")
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    true

                }
                else -> {
                    Log.i("something", "hello else")

                    false
                }

            }
        }
    }
}