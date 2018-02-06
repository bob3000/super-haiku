package net.bob3000.haiku

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.preference.DialogPreference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import java.util.*


class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    val KEY_ALARM_TIME = "pref_key_AlarmTime"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
        }
    }

    private fun setupActionBar() {
        val rootView = findViewById<View>(R.id.action_bar_root) as ViewGroup //id from appcompat
        val view = layoutInflater.inflate(R.layout.app_bar_layout, rootView, false)
        rootView.addView(view, 0)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.toolbar_titleSettings)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == KEY_ALARM_TIME) {
            val timeInMillis = sharedPreferences!!.getInt(KEY_ALARM_TIME, 0)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MILLISECOND, timeInMillis)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val interval = AlarmManager.INTERVAL_DAY
            MainActivity().setTimer(hour, minute, interval)

            val haiku = Haiku(null, hour.toString(), minute.toString(), interval.toString())
            HaikuDisplayActivity().displayHaiku(haiku)
        }
    }
}


class TimePickerPreference(context: Context, attributeSet: AttributeSet) :
        DialogPreference(context, attributeSet) {

    private var calendar: Calendar? = null
    private var picker: TimePicker? = null

    init {
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        calendar = GregorianCalendar()
    }

    override fun onCreateDialogView(): View {
        picker = TimePicker(context)
        return picker!!
    }

    @Suppress("deprecation")
    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            picker!!.currentHour = calendar!!.get(Calendar.HOUR_OF_DAY)
            picker!!.currentMinute = calendar!!.get(Calendar.MINUTE)
        } else {
            picker!!.hour = calendar!!.get(Calendar.HOUR_OF_DAY)
            picker!!.minute = calendar!!.get(Calendar.MINUTE)
        }
    }

    @Suppress("deprecation")
    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)
        if (positiveResult) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                calendar!!.set(Calendar.HOUR_OF_DAY, picker!!.currentHour)
                calendar!!.set(Calendar.MINUTE, picker!!.currentMinute)
            } else {
                calendar!!.set(Calendar.HOUR_OF_DAY, picker!!.hour)
                calendar!!.set(Calendar.MINUTE, picker!!.minute)
            }
            summary = getSummay()

            if (callChangeListener(calendar!!.timeInMillis)) {
                persistLong(calendar!!.timeInMillis)
                notifyChanged()
            }
        }
    }

    @Suppress("deprecation")
    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            if (defaultValue == null) {
                calendar!!.timeInMillis = getPersistedLong(System.currentTimeMillis())
            } else {
                calendar!!.timeInMillis = getPersistedString(defaultValue.toString()).toLong()
            }
        } else {
            if (defaultValue == null) {
                calendar!!.timeInMillis = System.currentTimeMillis()
            } else {
                calendar!!.timeInMillis = defaultValue.toString().toLong()
            }
        }

        summary = getSummay()
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getString(index)
    }

    private fun getSummay(): CharSequence? {
        if (calendar == null) {
            return null
        }

        return DateFormat.getTimeFormat(context).format(calendar!!.timeInMillis)
    }

    init {
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        calendar = GregorianCalendar()
    }
}
