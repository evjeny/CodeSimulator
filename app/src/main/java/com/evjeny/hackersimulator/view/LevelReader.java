package com.evjeny.hackersimulator.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.model.GameSaver;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Evjeny on 26.01.2018 11:30.
 */

public class LevelReader extends AppCompatActivity {

    private GameSaver saver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_reader);
        ListView levels = findViewById(R.id.level_reader_lv);

        saver = new GameSaver(this);

        ArrayList<GameSave> saves = saver.getSavesWithoutData();

        final GameSaveAdapter adapter = new GameSaveAdapter(this, saves);
        levels.setAdapter(adapter);
        levels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameSave shortSave = adapter.getGameSave(i); // small save without some parameters
                try {
                    GameSave fullSave = saver.getLevel(shortSave);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("save", fullSave);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        levels.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LevelReader.this);
                builder.setTitle(R.string.remove_q);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saver.removeLevel(adapter.getGameSave(pos));
                        adapter.remove(pos);
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.create().show();
                return true;
            }
        });
    }

}
