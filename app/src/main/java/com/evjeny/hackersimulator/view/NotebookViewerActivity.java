package com.evjeny.hackersimulator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.model.FileLocalizer;
import com.evjeny.hackersimulator.game.NoteItem;

import java.io.IOException;

public class NotebookViewerActivity extends AppCompatActivity {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_viewer);
        WebView wv = findViewById(R.id.notebook_viewer_wv);
        WebSettings settings = wv.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        NoteItem item = (NoteItem) getIntent().getSerializableExtra("noteItem");
        FileLocalizer localizer = new FileLocalizer(this);

        String text = "Error!";
        try {
            text = localizer.translate(item.filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        wv.loadData(text, "text/html; charset=utf-8", "utf-8");

    }

}
