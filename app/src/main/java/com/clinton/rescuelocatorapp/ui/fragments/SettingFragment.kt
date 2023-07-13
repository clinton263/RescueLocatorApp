package com.clinton.rescuelocatorapp.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.clinton.rescuelocatorapp.R

class SettingsFragment : PreferenceFragmentCompat()  {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

    }

}