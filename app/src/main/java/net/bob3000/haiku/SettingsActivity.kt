package net.bob3000.haiku

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.preference.DialogPreference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import android.widget.TimePicker
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
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
            picker!!.currentHour = Calendar.HOUR_OF_DAY
            picker!!.currentMinute = Calendar.MINUTE
        } else {
            picker!!.hour = Calendar.HOUR_OF_DAY
            picker!!.minute = Calendar.MINUTE
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
