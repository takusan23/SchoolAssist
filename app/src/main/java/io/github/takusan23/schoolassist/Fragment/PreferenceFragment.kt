package io.github.takusan23.schoolassist.Fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.takusan23.schoolassist.R

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}