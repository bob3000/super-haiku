package net.bob3000.haiku

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class HaikuDisplayActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val haiku = intent.extras[Haiku.EXTRA_HAIKU] as Haiku
        haiku.context = applicationContext
        displayHaiku(haiku)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
           R.id.action_settings -> return showSettings()
           else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showSettings(): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun displayHaiku(haiku: Haiku) {
        findViewById<TextView>(R.id.haiku_title).setText(haiku.title)
        findViewById<TextView>(R.id.haiku_author).setText(haiku.author)
        findViewById<TextView>(R.id.haiku_body).setText(haiku.body)
    }
}