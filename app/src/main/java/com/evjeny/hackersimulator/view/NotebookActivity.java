package com.evjeny.hackersimulator.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.FileLocalizer;
import com.evjeny.hackersimulator.game.NoteItem;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Evjeny on 18.02.2018 17:03.
 */

public class NotebookActivity extends AppCompatActivity {

    private FileLocalizer localizer;
    private ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        lv = findViewById(R.id.activity_notebook_lv);

        localizer = new FileLocalizer(this);
        try {
            final NotebookAdapter adapter = new NotebookAdapter(this, getItems());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Intent viewerIntent = new Intent(NotebookActivity.this,
                            NotebookViewerActivity.class);
                    viewerIntent.putExtra("noteItem", adapter.getItem(pos));
                    startActivity(viewerIntent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<NoteItem> getItems() throws IOException {
        ArrayList<NoteItem> items = new ArrayList<>();
        AssetManager manager = getAssets();
        String prefix = "proger/notebook";
        String[] files = manager.list(prefix);
        for (String str : files) {
            items.add(new NoteItem(localizer.getString(str.split("\\.")[0]),
                    prefix + "/" + str));
        }
        return items;
    }
}
