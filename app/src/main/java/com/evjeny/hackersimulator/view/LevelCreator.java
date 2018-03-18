package com.evjeny.hackersimulator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.model.GameSaver;
import com.evjeny.hackersimulator.game.GameType;

import java.io.IOException;

/**
 * Created by Evjeny on 25.01.2018 18:16.
 */

public class LevelCreator extends AppCompatActivity {

    private GameSaver saver;

    private EditText level_name, player_name;

    private String[] data = {"hacker",
            "proger"};
    private GameType gameType = GameType.proger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_creator);
        level_name = findViewById(R.id.l_creator_world_name);
        player_name = findViewById(R.id.l_creator_player_name);
        Spinner type = findViewById(R.id.l_creator_world_type);

        saver = new GameSaver(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, data);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos == 0) gameType = GameType.hacker;
                else if (pos == 1) gameType = GameType.proger;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        type.setSelection(1);
    }

    public void create(View v) {
        GameSave save = saveLevel();
        if (save != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("save", save);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    public GameSave saveLevel() {
        String worldName = level_name.getText().toString(),
                playerName = player_name.getText().toString();
        int currentScene = 1;
        if (!worldName.isEmpty() && !playerName.isEmpty()) {
            try {
                return saver.writeLevel(worldName, playerName, gameType, currentScene);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fields_not_empty),
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
