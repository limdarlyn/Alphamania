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

class LetterV : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var vultureSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_v)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        vultureSound = MediaPlayer.create(this, R.raw.sound_v)

        val vultureImage = findViewById<ImageView>(R.id.vultureImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        vultureImage.setOnClickListener {
            speak("V for Vulture!") {
                vultureSound?.start()

                // Soaring motion (horizontal movement to simulate gliding)
                val soarAnimator = ObjectAnimator.ofFloat(
                    vultureImage,
                    "translationX",
                    0f, -50f, 50f, -50f, 50f, 0f
                )
                soarAnimator.duration = 4000
                soarAnimator.repeatCount = ObjectAnimator.INFINITE
                soarAnimator.interpolator = LinearInterpolator()

                // Flapping motion (vertical movement)
                val flapUpDown = ObjectAnimator.ofFloat(
                    vultureImage,
                    "translationY",
                    0f, -30f, 0f, 30f, 0f
                )
                flapUpDown.duration = 1500
                flapUpDown.repeatCount = ObjectAnimator.INFINITE
                flapUpDown.interpolator = LinearInterpolator()

                // Stretching (scale effect to simulate wings spreading)
                val stretchX = ObjectAnimator.ofFloat(
                    vultureImage,
                    "scaleX",
                    1f, 1.2f, 1f
                )
                val stretchY = ObjectAnimator.ofFloat(
                    vultureImage,
                    "scaleY",
                    1f, 1.2f, 1f
                )
                stretchX.duration = 1500
                stretchY.duration = 1500
                stretchX.repeatCount = ObjectAnimator.INFINITE
                stretchY.repeatCount = ObjectAnimator.INFINITE

                // Create an AnimatorSet to combine all movements
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(soarAnimator, flapUpDown, stretchX, stretchY)

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
            val intent = Intent(this, LetterW::class.java)
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
        vultureSound?.release()
        super.onDestroy()
    }
}
