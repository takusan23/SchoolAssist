package io.github.takusan23.schoolassist.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.takusan23.schoolassist.Activity.LicenceActivity
import io.github.takusan23.schoolassist.Activity.ThisAppActivity
import io.github.takusan23.schoolassist.R
import io.github.takusan23.schoolassist.SubjectAlarm

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        findPreference<Preference>("alarmmanager_clear")?.setOnPreferenceClickListener {
            val subjectAlarm = SubjectAlarm(context)
            subjectAlarm.allCancel()
                    true
        }

        findPreference<Preference>("licence")?.setOnPreferenceClickListener {
            val intent = Intent(context, LicenceActivity::class.java)
            startActivity(intent)
            true
        }

        findPreference<Preference>("this_app")?.setOnPreferenceClickListener {
            val intent = Intent(context, ThisAppActivity::class.java)
            startActivity(intent)
            true
        }

    }
}