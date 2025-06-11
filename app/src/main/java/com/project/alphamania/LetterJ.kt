package com.project.alphamania

import android.animation.ObjectAnimator
import android.animation.Animator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LetterJ : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var sound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_j)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        sound = MediaPlayer.create(this, R.raw.jellyfish)

        val image = findViewById<ImageView>(R.id.image)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        image.setOnClickListener {
            speak("J for Jellyfish!") {
                sound?.start()

                val bounceAnimator = ObjectAnimator.ofFloat(
                    image,
                    "translationY",
                    0f, -50f, 0f, -30f, 0f
                )
                bounceAnimator.duration = 1500
                bounceAnimator.interpolator = LinearInterpolator()

                bounceAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        val wiggleAnimator = ObjectAnimator.ofFloat(
                            image,
                            "rotation",
                            0f, 15f, -15f, 10f, -10f, 5f, -5f, 0f
                        )
                        wiggleAnimator.duration = 2000
                        wiggleAnimator.interpolator = LinearInterpolator()
                        wiggleAnimator.repeatCount = ObjectAnimator.INFINITE
                        wiggleAnimator.start()
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

                bounceAnimator.start()
            }
        }

        // Go to previous activity (optional)
        backButton.setOnClickListener {
            val intent = Intent(this, Alphabets::class.java)
            startActivity(intent)
            finish()
        }

        // Go to MainActivity2
        nextButton.setOnClickListener {
            val intent = Intent(this, LetterK::class.java)
            startActivity(intent)
            finish()
        }
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
        sound?.release()
        super.onDestroy()
    }
}
