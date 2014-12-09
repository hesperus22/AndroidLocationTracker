package com.bocian.locationTracker.android.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.bocian.locationTracker.android.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }
}