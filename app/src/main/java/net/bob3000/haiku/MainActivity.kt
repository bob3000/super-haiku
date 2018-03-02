package net.bob3000.haiku

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    val ALARM_HOUR = 12
    val ALARM_MINUTE = 0
    val ALARM_INTERVAL: Long = AlarmManager.INTERVAL_DAY

    companion object {

        fun setTimer(context: Context, hour: Int, minute: Int, interval: Long) {
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            val alarmCal = Calendar.getInstance(Locale.getDefault())
            val randomOffset = Random().nextInt() % 60
            alarmCal.set(Calendar.HOUR, hour)
            alarmCal.set(Calendar.MINUTE, minute)
            alarmCal.add(Calendar.SECOND, randomOffset)
            val dayInSecs = 86400L
            val triggerTime: Long = alarmCal.timeInMillis - dayInSecs * 1000

            val alarmService: AlarmManager = context.getSystemService(
                    Context.ALARM_SERVICE) as AlarmManager
            alarmService.setInexactRepeating(AlarmManager.RTC, triggerTime, interval, pendingIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = prefs.getLong(SettingsActivity.KEY_ALARM_TIME, 0)
        if (calendar.timeInMillis == 0L) {
            calendar.set(Calendar.HOUR_OF_DAY, ALARM_HOUR)
            calendar.set(Calendar.MINUTE, ALARM_MINUTE)
        }
        setTimer(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                ALARM_INTERVAL)
        val haiku = Haiku(applicationContext)
        haiku.load(haiku::show)
    }
}