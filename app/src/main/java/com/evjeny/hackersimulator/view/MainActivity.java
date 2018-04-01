package com.evjeny.hackersimulator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.model.TaskDownloader;
import com.evjeny.hackersimulator.model.TaskSender;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Evjeny on 25.01.2018 17:59.
 */

public class MainActivity extends AppCompatActivity{

    private final int CREATE_GAME =  0, CONTINUE_GAME = 1;

    private TaskSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TaskDownloader(this, true);
        sender = new TaskSender(this);
    }

    private void sendTasksAndCheck() throws JSONException {
        int id = 756597;
        String code = "a = int(input())\n" +
                "b = int(input())\n" +
                "c = int(input())\n" +
                "k = int(input())\n" +
                "n = int(input())\n" +
                "z = int(input())\n" +
                "print(((a ** 10) * (b ** 3) - k * (a ** 2 + b ** 7)) / (c + k * (n ** 7)) * z)\n";
        sender.sendRequest(code, id, new TaskSender.ResultInterface() {
            @Override
            public void result(JSONObject res) {
                if (res == null) {
                    Log.d("MainActivity", "result: null");
                } else {
                    Log.d("MainActivity", "result: " + res.toString());
                }
            }
        });
    }

    public void continue_game(View v) {
        Intent levelReader = new Intent(this, LevelReader.class);
        startActivityForResult(levelReader, CONTINUE_GAME);
    }

    public void new_game(View v) {
        Intent levelCreator = new Intent(this, LevelCreator.class);
        startActivityForResult(levelCreator, CREATE_GAME);
    }

    public void notebook(View v) {
        Intent nb = new Intent(this, NotebookActivity.class);
        startActivity(nb);
    }

    public void settings(View view) {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_GAME && resultCode == RESULT_OK) {
            GameSave save = (GameSave) data.getSerializableExtra("save");
            Intent mainStory = new Intent(this, MainStory.class);
            mainStory.putExtra("save", save);
            startActivity(mainStory);
        } else if (requestCode == CONTINUE_GAME && resultCode == RESULT_OK) {
            GameSave save = (GameSave) data.getSerializableExtra("save");
            Intent mainStory = new Intent(this, MainStory.class);
            mainStory.putExtra("save", save);
            startActivity(mainStory);
        }
    }
}
