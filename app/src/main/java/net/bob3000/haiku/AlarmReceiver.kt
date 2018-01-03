package net.bob3000.haiku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val haiku = Haiku(context)
        haiku.load(haiku::createNotification)
    }
}