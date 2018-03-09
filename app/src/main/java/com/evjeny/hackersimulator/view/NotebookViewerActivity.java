package com.evjeny.hackersimulator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evjeny.hackersimulator.game.FileLocalizer;
import com.evjeny.hackersimulator.game.NoteItem;

import java.io.IOException;

public class NotebookViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView wv = new WebView(this);
        WebSettings settings = wv.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        setContentView(wv);

        NoteItem item = (NoteItem) getIntent().getSerializableExtra("noteItem");
        FileLocalizer localizer = new FileLocalizer(this);

        String text = "Error!";
        try {
            text = localizer.translate(item.filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        wv.loadData(text, "text/html", "utf-8");

    }

}
