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
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LetterW : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var whaleSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_w)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        whaleSound = MediaPlayer.create(this, R.raw.sound_w)

        val whaleImage = findViewById<ImageView>(R.id.whaleImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        whaleImage.setOnClickListener {
            speak("W for Whale!") {
                whaleSound?.start()

                // Swaying (horizontal movement to simulate swimming)
                val swayAnimator = ObjectAnimator.ofFloat(
                    whaleImage,
                    "translationX",
                    0f, -50f, 50f, -50f, 50f, 0f
                )
                swayAnimator.duration = 4000
                swayAnimator.repeatCount = ObjectAnimator.INFINITE
                swayAnimator.interpolator = LinearInterpolator()

                // Bobbing (vertical movement for a smooth rise and fall effect)
                val bobbingAnimator = ObjectAnimator.ofFloat(
                    whaleImage,
                    "translationY",
                    0f, -30f, 0f, 30f, 0f
                )
                bobbingAnimator.duration = 5000
                bobbingAnimator.repeatCount = ObjectAnimator.INFINITE
                bobbingAnimator.interpolator = LinearInterpolator()

                // Stretching (simulate swimming motion with a gentle stretch)
                val stretchX = ObjectAnimator.ofFloat(
                    whaleImage,
                    "scaleX",
                    1f, 1.2f, 1f
                )
                val stretchY = ObjectAnimator.ofFloat(
                    whaleImage,
                    "scaleY",
                    1f, 1.05f, 1f
                )
                stretchX.duration = 1500
                stretchY.duration = 1500
                stretchX.repeatCount = ObjectAnimator.INFINITE
                stretchY.repeatCount = ObjectAnimator.INFINITE

                // Combine all animations into an AnimatorSet
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(swayAnimator, bobbingAnimator, stretchX, stretchY)

                // Start the animation
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
            val intent = Intent(this, LetterX::class.java)
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
        whaleSound?.release()
        super.onDestroy()
    }
}
