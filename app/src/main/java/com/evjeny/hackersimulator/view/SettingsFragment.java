package com.evjeny.hackersimulator.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.model.TaskDownloader;

/**
 * Created by evjeny on 02.04.2018 20:11.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final Context context = getActivity();

        Preference reloadFiles = findPreference("reload_files");
        reloadFiles.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TaskDownloader downloader = new TaskDownloader(context);
                final ProgressDialog dialog = ProgressDialog.show(context,
                        "Загрузка заданий",
                        "Пожалуйста, подождите...",
                        true);
                dialog.setCancelable(false);
                downloader.downloadFile(new TaskDownloader.resInt() {
                    @Override
                    public void good() {
                        dialog.dismiss();
                    }

                    @Override
                    public void error() {
                        dialog.dismiss();
                        Toast.makeText(context,
                                "Ошибка при загрузке заданий. Проверьте соединение.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }
}