package net.bob3000.haiku

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Haiku(var context: Context? = null, var title: String? = null, var body: String? = null,
            var author: String? = null) : Parcelable {
    val CHANNEL_ID = "haiku_01"
    val NOTIFICATION_ID = 1

    constructor(parcel: Parcel) : this(
            null,
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    fun load(callback: () -> Unit) {
        val stringRequest = object : StringRequest(Request.Method.GET, todaysHaikuURL(),
                Response.Listener { response ->
                    try {
                        val json = JSONObject(response)
                        val haiku = json["haiku"] as JSONObject
                        this@Haiku.title = haiku["title"] as String
                        this@Haiku.author = haiku["author"] as String
                        this@Haiku.body = haiku["body"] as String
                        callback()
                    } catch (e: JSONException) {
                        Toast.makeText(context, R.string.haiku_not_fetched,
                                Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(context, R.string.haiku_not_fetched,
                            Toast.LENGTH_LONG).show()
                }) {
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun todaysHaikuURL(): String {
        val cal = Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        return EndPoints.URL_GET_HAIKU + "/" + dayOfYear.toString() + ".json"
    }

    /*
    Used as callback with the load() method
     */
    fun show() {
        val intent = Intent(context, HaikuDisplayActivity::class.java)
        intent.putExtra(EXTRA_HAIKU, this)
        context!!.startActivity(intent)
    }

    /*
    Used as callback with the load() method
     */
    @SuppressLint("NewApi")
    @Suppress("deprecation")
    fun createNotification() {
        var builder = NotificationCompat.Builder(context)
        val noteMgr = context!!.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(context as Context, CHANNEL_ID)
            val channelName = context!!.getString(R.string.note_channel_name)
            val channelDesc = context!!.getString(R.string.note_channel_desc)
            val noteChannel = NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
            noteChannel.description = channelDesc
            noteChannel.name = channelName
            noteMgr.createNotificationChannel(noteChannel)
        }
        builder.setContentTitle(title)
        builder.setContentText(body)
        builder.setSmallIcon(R.mipmap.banana_50)

        val intent = Intent(context, HaikuDisplayActivity::class.java)
        intent.putExtra(EXTRA_HAIKU, this)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        noteMgr.notify(NOTIFICATION_ID, builder.build())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeString(author)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Haiku> {
        val EXTRA_HAIKU = "haiku_extra"
        override fun createFromParcel(parcel: Parcel): Haiku {
            return Haiku(parcel)
        }

        override fun newArray(size: Int): Array<Haiku?> {
            return arrayOfNulls(size)
        }
    }
}
