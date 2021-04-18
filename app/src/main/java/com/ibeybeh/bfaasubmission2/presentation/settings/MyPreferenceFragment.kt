package com.ibeybeh.bfaasubmission2.presentation.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.utils.Const.TIME
import com.ibeybeh.bfaasubmission2.utils.alarm.AlarmReceiver

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var DAILY_REMINDER: String

    private lateinit var swDailyReminder: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preferences)
        init()
        setSummaries()

        alarmReceiver = AlarmReceiver()

        swDailyReminder.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
            override fun onPreferenceClick(preference: Preference?): Boolean {
                if (swDailyReminder.isChecked) {
                    alarmReceiver.setDailyReminder(requireContext(), TIME, requireContext().resources.getString(R.string.label_return_to_app))
                }else{
                    alarmReceiver.cancelAlarm(requireContext())
                }
                return true
            }
        }
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        if (key == DAILY_REMINDER) {
            if (sp != null) {
                swDailyReminder.isChecked = sp.getBoolean(DAILY_REMINDER, false)
            }
        }
    }

    private fun init() {
        DAILY_REMINDER = resources.getString(R.string.sw_daily_reminder)

        swDailyReminder = findPreference<SwitchPreference>(DAILY_REMINDER) as SwitchPreference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        swDailyReminder.isChecked = sh.getBoolean(DAILY_REMINDER, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}