package com.project.alphamania

import android.animation.ObjectAnimator
import android.animation.Animator
import android.animation.AnimatorSet
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

class LetterX : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var xrayfishSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_x)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        xrayfishSound = MediaPlayer.create(this, R.raw.sound_x)

        val xrayfishImage = findViewById<ImageView>(R.id.xrayfishImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        xrayfishImage.setOnClickListener {
            speak("X for X-ray Fish!") {
                xrayfishSound?.start()

                // Horizontal sway to simulate swimming
                val swayAnimator = ObjectAnimator.ofFloat(
                    xrayfishImage,
                    "translationX",
                    0f, -30f, 30f, -30f, 30f, 0f
                )
                swayAnimator.duration = 2000
                swayAnimator.repeatCount = ObjectAnimator.INFINITE
                swayAnimator.interpolator = LinearInterpolator()

                // Vertical bobbing for floating effect
                val bobbingAnimator = ObjectAnimator.ofFloat(
                    xrayfishImage,
                    "translationY",
                    0f, -20f, 0f, 20f, 0f
                )
                bobbingAnimator.duration = 4000
                bobbingAnimator.repeatCount = ObjectAnimator.INFINITE
                bobbingAnimator.interpolator = LinearInterpolator()

                // Opacity change to represent transparency (X-ray effect)
                val opacityAnimator = ObjectAnimator.ofFloat(
                    xrayfishImage,
                    "alpha",
                    1f, 0.5f, 1f
                )
                opacityAnimator.duration = 3000
                opacityAnimator.repeatCount = ObjectAnimator.INFINITE
                opacityAnimator.interpolator = LinearInterpolator()

                // Combine all animations
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(swayAnimator, bobbingAnimator, opacityAnimator)

                // Start animation
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
            val intent = Intent(this, LetterY::class.java)
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
        xrayfishSound?.release()
        super.onDestroy()
    }
}
