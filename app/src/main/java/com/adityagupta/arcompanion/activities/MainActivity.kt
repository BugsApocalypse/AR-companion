package com.adityagupta.arcompanion.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.util.*




class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.button2.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).apply {
            }
            startActivity(intent)
        }
        //TODO Request camera permissions

        viewBinding.bottomNavigation.selectedItemId = R.id.home

        viewBinding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.tech -> {
                    Log.i("something", "hello")
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