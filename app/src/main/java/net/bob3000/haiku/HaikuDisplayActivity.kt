package net.bob3000.haiku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class HaikuDisplayActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val haiku = intent.extras[Haiku.EXTRA_HAIKU] as Haiku
        haiku.context = applicationContext
        displayHaiku(haiku)
    }

    private fun displayHaiku(haiku: Haiku) {
        findViewById<TextView>(R.id.haiku_title).setText(haiku.title)
        findViewById<TextView>(R.id.haiku_author).setText(haiku.author)
        findViewById<TextView>(R.id.haiku_body).setText(haiku.body)
    }
}