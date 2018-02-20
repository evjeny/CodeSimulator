package com.evjeny.hackersimulator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;

/**
 * Created by Evjeny on 25.01.2018 17:59.
 */

public class MainActivity extends AppCompatActivity{

    private final int CREATE_GAME =  0, CONTINUE_GAME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
