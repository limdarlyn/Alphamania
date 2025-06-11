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
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class LetterO : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var octopusSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_o)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        octopusSound = MediaPlayer.create(this, R.raw.sound_o)

        val octopusImage = findViewById<ImageView>(R.id.octopusImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        octopusImage.setOnClickListener {
            speak("O for Octopus!") {
                octopusSound?.start()

                val swayAnimator = ObjectAnimator.ofFloat(
                    octopusImage,
                    "translationX",
                    0f, -30f, 30f, -20f, 20f, -10f, 10f, 0f
                )
                swayAnimator.duration = 2000
                swayAnimator.interpolator = LinearInterpolator()

                swayAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        val bobbingAnimator = ObjectAnimator.ofFloat(
                            octopusImage,
                            "translationY",
                            0f, -30f, 0f, 30f, 0f
                        )
                        bobbingAnimator.duration = 4000
                        bobbingAnimator.repeatCount = ObjectAnimator.INFINITE
                        bobbingAnimator.interpolator = LinearInterpolator()
                        bobbingAnimator.start()
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

                swayAnimator.start()
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
            val intent = Intent(this, LetterP::class.java)
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
        octopusSound?.release()
        super.onDestroy()
    }
}