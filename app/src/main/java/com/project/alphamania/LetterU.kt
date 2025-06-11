package com.project.alphamania

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LetterU : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var unicornSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_u)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        unicornSound = MediaPlayer.create(this, R.raw.sound_u)

        val unicornImage = findViewById<ImageView>(R.id.unicornImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        unicornImage.setOnClickListener {
            speak("U for Unicorn!") {
                unicornSound?.start()

                // Floating up and down animation
                val floatUpDown = ObjectAnimator.ofFloat(
                    unicornImage,
                    "translationY",
                    0f, -50f, 0f, 50f, 0f
                )
                floatUpDown.duration = 3000
                floatUpDown.repeatCount = ObjectAnimator.INFINITE

                // Subtle scaling animation for bounce effect
                val scaleUp = ObjectAnimator.ofFloat(
                    unicornImage,
                    "scaleX",
                    1f, 1.1f, 1f
                )
                val scaleUpY = ObjectAnimator.ofFloat(
                    unicornImage,
                    "scaleY",
                    1f, 1.1f, 1f
                )
                scaleUp.duration = 1000
                scaleUpY.duration = 1000
                scaleUp.repeatCount = ObjectAnimator.INFINITE
                scaleUpY.repeatCount = ObjectAnimator.INFINITE

                // Create AnimatorSet to combine all animations
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(floatUpDown, scaleUp, scaleUpY)

                // Start the animations
                animatorSet.start()
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
            val intent = Intent(this, LetterV::class.java)
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
        unicornSound?.release()
        super.onDestroy()
    }
}
