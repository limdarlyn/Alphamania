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

class LetterT : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var tigerSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_t)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        tigerSound = MediaPlayer.create(this, R.raw.sound_t)

        val tigerImage = findViewById<ImageView>(R.id.tigerImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        tigerImage.setOnClickListener {
            speak("T for Tiger!") {
                tigerSound?.start()

                val walkX = ObjectAnimator.ofFloat(
                    tigerImage,
                    "translationX",
                    0f, -20f, 20f, -15f, 15f, -10f, 10f, 0f
                )
                walkX.duration = 3000
                walkX.repeatCount = ObjectAnimator.INFINITE
                walkX.interpolator = LinearInterpolator()

                val walkY = ObjectAnimator.ofFloat(
                    tigerImage,
                    "translationY",
                    0f, 10f, -10f, 8f, -8f, 5f, -5f, 0f
                )
                walkY.duration = 3000
                walkY.repeatCount = ObjectAnimator.INFINITE
                walkY.interpolator = LinearInterpolator()

                val tigerWalk = AnimatorSet()
                tigerWalk.playTogether(walkX, walkY)
                tigerWalk.start()
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
            val intent = Intent(this, LetterU::class.java)
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
        tigerSound?.release()
        super.onDestroy()
    }
}
