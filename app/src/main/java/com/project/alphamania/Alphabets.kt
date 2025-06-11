package com.project.alphamania

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Alphabets : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabets)
        supportActionBar?.hide()

        val letter_a = findViewById<ImageView>(R.id.letterA)
        val letter_b = findViewById<ImageView>(R.id.letterB)
        val letter_c = findViewById<ImageView>(R.id.letterC)
        val letter_d = findViewById<ImageView>(R.id.letterD)
        val letter_e = findViewById<ImageView>(R.id.letterE)
        val letter_f = findViewById<ImageView>(R.id.letterF)
        val letter_g = findViewById<ImageView>(R.id.letterG)
        val letter_h = findViewById<ImageView>(R.id.letterH)
        val letter_i = findViewById<ImageView>(R.id.letterI)
        val letter_j = findViewById<ImageView>(R.id.letterJ)
        val letter_k = findViewById<ImageView>(R.id.letterK)
        val letter_l = findViewById<ImageView>(R.id.letterL)
        val letter_m = findViewById<ImageView>(R.id.letterM)
        val letter_n = findViewById<ImageView>(R.id.letterN)
        val letter_o = findViewById<ImageView>(R.id.letterO)
        val letter_p = findViewById<ImageView>(R.id.letterP)
        val letter_q = findViewById<ImageView>(R.id.letterQ)
        val letter_r = findViewById<ImageView>(R.id.letterR)
        val letter_s = findViewById<ImageView>(R.id.letterS)
        val letter_t = findViewById<ImageView>(R.id.letterT)
        val letter_u = findViewById<ImageView>(R.id.letterU)
        val letter_v = findViewById<ImageView>(R.id.letterV)
        val letter_w = findViewById<ImageView>(R.id.letterW)
        val letter_x = findViewById<ImageView>(R.id.letterX)
        val letter_y = findViewById<ImageView>(R.id.letterY)
        val letter_z = findViewById<ImageView>(R.id.letterZ)
        val alphabtn = findViewById<ImageView>(R.id.alphabtn)

        mediaPlayer = MediaPlayer.create(this, R.raw.alphabets_bg)
        mediaPlayer.isLooping = true
        mediaPlayer.setVolume(0.1f, 0.1f)
        mediaPlayer.start()

        fun playClickAndNavigate(view: ImageView, destination: Class<*>) {
            val clickPlayer = MediaPlayer.create(this, R.raw.click2)
            clickPlayer.setVolume(0.1f, 0.1f)
            clickPlayer.start()
            mediaPlayer.setVolume(0.03f, 0.03f)
            mediaPlayer.start()
            animateClickAndNavigate(view, destination)
        }

        letter_a.setOnClickListener { playClickAndNavigate(letter_a, LetterA::class.java) }
        letter_b.setOnClickListener { playClickAndNavigate(letter_b, LetterB::class.java) }
        letter_c.setOnClickListener { playClickAndNavigate(letter_c, LetterC::class.java) }
        letter_d.setOnClickListener { playClickAndNavigate(letter_d, LetterD::class.java) }
        letter_e.setOnClickListener { playClickAndNavigate(letter_e, LetterE::class.java) }
        letter_f.setOnClickListener { playClickAndNavigate(letter_f, LetterF::class.java) }
        letter_g.setOnClickListener { playClickAndNavigate(letter_g, LetterG::class.java) }
        letter_h.setOnClickListener { playClickAndNavigate(letter_h, LetterH::class.java) }
        letter_i.setOnClickListener { playClickAndNavigate(letter_i, LetterI::class.java) }
        letter_j.setOnClickListener { playClickAndNavigate(letter_j, LetterJ::class.java) }
        letter_k.setOnClickListener { playClickAndNavigate(letter_k, LetterK::class.java) }
        letter_l.setOnClickListener { playClickAndNavigate(letter_l, LetterL::class.java) }
        letter_m.setOnClickListener { playClickAndNavigate(letter_m, LetterM::class.java) }
        letter_n.setOnClickListener { playClickAndNavigate(letter_n, LetterN::class.java) }
        letter_o.setOnClickListener { playClickAndNavigate(letter_o, LetterO::class.java) }
        letter_p.setOnClickListener { playClickAndNavigate(letter_p, LetterP::class.java) }
        letter_q.setOnClickListener { playClickAndNavigate(letter_q, LetterQ::class.java) }
        letter_r.setOnClickListener { playClickAndNavigate(letter_r, LetterR::class.java) }
        letter_s.setOnClickListener { playClickAndNavigate(letter_s, LetterS::class.java) }
        letter_t.setOnClickListener { playClickAndNavigate(letter_t, LetterT::class.java) }
        letter_u.setOnClickListener { playClickAndNavigate(letter_u, LetterU::class.java) }
        letter_v.setOnClickListener { playClickAndNavigate(letter_v, LetterV::class.java) }
        letter_w.setOnClickListener { playClickAndNavigate(letter_w, LetterW::class.java) }
        letter_x.setOnClickListener { playClickAndNavigate(letter_x, LetterX::class.java) }
        letter_y.setOnClickListener { playClickAndNavigate(letter_y, LetterY::class.java) }
        letter_z.setOnClickListener { playClickAndNavigate(letter_z, LetterZ::class.java) }
        alphabtn.setOnClickListener {
            val clickPlayer = MediaPlayer.create(this, R.raw.click2)
            clickPlayer.setVolume(0.1f, 0.1f)
            clickPlayer.start()
            mediaPlayer.stop()
            mediaPlayer.release()
            val intent = Intent(this, AbcVid::class.java)
            startActivity(intent)
        }
    }

    private fun animateClickAndNavigate(view: ImageView, destination: Class<*>) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction {
                        val intent = Intent(this, destination)
                        startActivity(intent)
                    }
            }
    }
}
