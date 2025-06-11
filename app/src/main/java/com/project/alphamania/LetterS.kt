package com.project.alphamania

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LetterS : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var sharkSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_s)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        sharkSound = MediaPlayer.create(this, R.raw.sound_s)

        val sharkImage = findViewById<ImageView>(R.id.sharkImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        sharkImage.setOnClickListener {
            speak("S for Shark!") {
                sharkSound?.start()

                val swimX = ObjectAnimator.ofFloat(
                    sharkImage,
                    "translationX",
                    0f, -40f, 40f, -30f, 30f, -20f, 20f, 0f
                )
                swimX.duration = 3000
                swimX.repeatCount = ObjectAnimator.INFINITE
                swimX.interpolator = LinearInterpolator()

                val swimY = ObjectAnimator.ofFloat(
                    sharkImage,
                    "translationY",
                    0f, 20f, -20f, 15f, -15f, 10f, -10f, 0f
                )
                swimY.duration = 3000
                swimY.repeatCount = ObjectAnimator.INFINITE
                swimY.interpolator = LinearInterpolator()

                val swimSet = AnimatorSet()
                swimSet.playTogether(swimX, swimY)
                swimSet.start()
            }
        }

        fun clickSound() {
            val clickPlayer = MediaPlayer.create(this, R.raw.click2)
            clickPlayer.setVolume(0.1f, 0.1f)
            clickPlayer.start()
        }

        // Go to previous activity (optional)
        backButton.setOnClickListener {
            animateButton(it)
            clickSound()
            val intent = Intent(this, Alphabets::class.java)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener {
            animateButton(it)
            clickSound()
            val intent = Intent(this, LetterT::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun animateButton(view: View) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setPitch(1.0f)
            tts.setSpeechRate(0.95f)
        }
    }

    private fun speak(text: String, onDone: (() -> Unit)? = null) {
        if (::tts.isInitialized) {
            val params = Bundle()
            val utteranceId = "TTS_${System.currentTimeMillis()}"

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {}
                override fun onDone(utteranceId: String) {
                    Handler(Looper.getMainLooper()).post {
                        onDone?.invoke()
                    }
                }

                override fun onError(utteranceId: String) {}
            })

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        sharkSound?.release()
        super.onDestroy()
    }
}
