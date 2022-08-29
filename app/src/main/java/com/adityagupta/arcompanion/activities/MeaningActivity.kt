package com.adityagupta.arcompanion.activities

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.databinding.ActivityMeaningBinding
import com.adityagupta.arcompanion.viewmodels.MeaningActivityViewModel
import com.squareup.picasso.Picasso
import java.io.IOException


class MeaningActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMeaningBinding
    lateinit var mediaPlayer: MediaPlayer

    val model: MeaningActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meaning)

        viewBinding = ActivityMeaningBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val word = intent.getStringExtra("word")

        model.getOxfordWordMeaning(word!!)
        model.getWikiData(word)

        model.wordOxford.observe(this, Observer { word ->
            if(word.title == "error"){
                Toast.makeText(applicationContext, "Something went wrong!! :(", Toast.LENGTH_SHORT).show()
                finish()
            }else {
                viewBinding.progressBar.visibility = View.INVISIBLE
                viewBinding.consLayout5.visibility = View.VISIBLE
                viewBinding.wikiConstraintLayout.visibility = View.VISIBLE
                viewBinding.wikiTitleText.visibility = View.VISIBLE
                viewBinding.wordTitle.text = word.title
                viewBinding.wordDef1.text = word.definition
                viewBinding.wordExample1.text = word.example
                viewBinding.speaker.setOnClickListener {
                    playAudio(word.audioUrl)
                }
            }
        })

        model.wikiData.observe(this, Observer {
            viewBinding.wikiConstraintLayout.setOnClickListener {
                startActivity(Intent(this@MeaningActivity, WikipediaWebViewActivity::class.java).putExtra("title", model.rootedWord.value))
            }

            viewBinding.wikiTitle.text = it.title
            viewBinding.wikiDescription.text =it.description
            Picasso.with(applicationContext)
                .load("https:" + (it.url))
                .into(viewBinding.wikiImageView)

        })
    }

    private fun playAudio(audio: String?) {
        val audioUrl = audio

        // initializing media player
        mediaPlayer = MediaPlayer()

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl)
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // below line is use to display a toast message.
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show()
    }


}