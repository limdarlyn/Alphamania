package com.project.alphamania

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ThankYouPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.thank_you_page)
        supportActionBar?.hide()

        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, AbcVid::class.java)
            startActivity(intent)
            finish()
        }
    }
}
