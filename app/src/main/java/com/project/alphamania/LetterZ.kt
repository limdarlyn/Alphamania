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

class LetterZ : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var zebraSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letter_z)
        supportActionBar?.hide()

        tts = TextToSpeech(this, this)
        zebraSound = MediaPlayer.create(this, R.raw.sound_z)

        val zebraImage = findViewById<ImageView>(R.id.zebraImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        zebraImage.setOnClickListener {
            speak("Z for Zebra!") {
                zebraSound?.start()

                // Horizontal swaying to simulate quick energetic movement
                val swayAnimator = ObjectAnimator.ofFloat(
                    zebraImage,
                    "translationX",
                    0f, -40f, 40f, -30f, 30f, -20f, 20f, 0f
                )
                swayAnimator.duration = 1500 // Faster sway for energetic feel
                swayAnimator.repeatCount = ObjectAnimator.INFINITE
                swayAnimator.interpolator = LinearInterpolator()

                // Vertical bobbing to simulate grazing or walking
                val bobbingAnimator = ObjectAnimator.ofFloat(
                    zebraImage,
                    "translationY",
                    0f, -20f, 0f, 20f, 0f
                )
                bobbingAnimator.duration = 2500 // Moderate bobbing for a walking effect
                bobbingAnimator.repeatCount = ObjectAnimator.INFINITE
                bobbingAnimator.interpolator = LinearInterpolator()

                swayAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

                // Start the swaying and bobbing animations
                swayAnimator.start()
                bobbingAnimator.start()
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
            val intent = Intent(this, ThankYouPage::class.java)
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
        zebraSound?.release()
        super.onDestroy()
    }
}