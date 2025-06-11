package com.project.alphamania

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class AbcVid : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var pauseButton: ImageButton
    private lateinit var replayButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.abc_vid)
        supportActionBar?.hide()

        videoView = findViewById(R.id.videoView)
        pauseButton = findViewById(R.id.playButton) // still using pause icon
        replayButton = findViewById(R.id.replayButton)

        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.closing}")
        videoView.setVideoURI(videoUri)


        pauseButton.visibility = View.GONE
        replayButton.visibility = View.GONE

        videoView.setOnPreparedListener {
            it.isLooping = false
            videoView.start()
        }

        videoView.setOnCompletionListener {
            replayButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }

        // Tap behavior
        videoView.setOnClickListener {
            if (videoView.isPlaying) {
                // Pause the video and show the pause icon
                videoView.pause()
                pauseButton.visibility = View.VISIBLE
            } else {
                // Play the video and hide the pause icon
                videoView.start()
                pauseButton.visibility = View.GONE
            }
        }

        // Replay behavior
        replayButton.setOnClickListener {
            videoView.seekTo(0)
            videoView.start()
            replayButton.visibility = View.GONE
            pauseButton.visibility = View.GONE
        }

        // Optional: pause button click does same toggle
        pauseButton.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
                pauseButton.visibility = View.VISIBLE
            } else {
                videoView.start()
                pauseButton.visibility = View.GONE
            }
        }
    }
}
