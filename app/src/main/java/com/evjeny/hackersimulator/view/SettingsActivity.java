package com.evjeny.hackersimulator.view;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.model.TaskDownloader;

/**
 * Created by evjeny on 01.04.2018 18:03.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference reloadFiles = findPreference("reload_files");
        reloadFiles.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TaskDownloader downloader = new TaskDownloader(SettingsActivity.this, false);
                downloader.downloadFile();
                return true;
            }
        });
    }
}
