package com.project.alphamania

import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.hide()

        val letterA = findViewById<ImageView>(R.id.foregroundImage)
        val startBtn = findViewById<ImageButton>(R.id.startButton)

        // Initialize and start background music
        mediaPlayer = MediaPlayer.create(this, R.raw.intro)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.start()

        animateLetterA(letterA)

        startBtn.setOnClickListener {
            startBtn.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    startBtn.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            // Play click sound
                            val clickPlayer = MediaPlayer.create(this, R.raw.click1)
                            clickPlayer.start()

                            // Fade out background music and then transition
                            fadeOutMusic {
                                val intent = Intent(this, Alphabets::class.java)
                                startActivity(intent)
                            }

                            // Release clickPlayer after playback
                            clickPlayer.setOnCompletionListener {
                                it.release()
                            }
                        }
                }
        }
    }

    private fun fadeOutMusic(onComplete: () -> Unit) {
        val animator = ValueAnimator.ofFloat(1f, 0f)
        animator.duration = 1000 // 1 second fade-out
        animator.addUpdateListener { valueAnimator ->
            val volume = valueAnimator.animatedValue as Float
            mediaPlayer.setVolume(volume, volume)
        }
        animator.doOnEnd {
            mediaPlayer.stop()
            mediaPlayer.release()
            onComplete()
        }
        animator.start()
    }

    private fun animateLetterA(view: ImageView) {
        view.animate()
            .translationYBy(-30f)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                view.animate()
                    .translationYBy(30f)
                    .setDuration(800)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        animateLetterA(view)
                    }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}
