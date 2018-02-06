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
        val toolbar = findViewById<Toolbar>(R.id.toolbar_display)
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

    fun displayHaiku(haiku: Haiku) {
        val title = findViewById<TextView>(R.id.haiku_title)
        title.text = haiku.title
        val author = findViewById<TextView>(R.id.haiku_author)
        author.text = haiku.author
        val body = findViewById<TextView>(R.id.haiku_body)
        body.text = haiku.body
    }
}