package net.bob3000.haiku

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {

    val ALARM_HOUR = 12
    val ALARM_MINUTE = 0
    val ALARM_INTERVAL: Long = AlarmManager.INTERVAL_DAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTimer(ALARM_HOUR, ALARM_MINUTE, ALARM_INTERVAL)
        val haiku = Haiku(applicationContext)
        haiku.load(haiku::show)
    }

    private fun setTimer(hour: Int, minute: Int, interval: Long) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmCal = Calendar.getInstance(Locale.getDefault())
        val randomOffset = Random().nextInt() % 60
        alarmCal.set(Calendar.HOUR, hour)
        alarmCal.set(Calendar.MINUTE, minute)
        alarmCal.add(Calendar.MINUTE, randomOffset)
        val dayInSecs = 86400L
        val triggerTime: Long = alarmCal.timeInMillis - dayInSecs * 1000

        val alarmService: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.setRepeating(AlarmManager.RTC, triggerTime, interval, pendingIntent)
    }
}