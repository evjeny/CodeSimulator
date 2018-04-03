package com.evjeny.hackersimulator.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.model.TaskDownloader;

import java.io.IOException;

/**
 * Created by Evjeny on 25.01.2018 17:59.
 */

public class MainActivity extends AppCompatActivity{

    private final int CREATE_GAME =  0, CONTINUE_GAME = 1;

    private TaskDownloader downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloader = new TaskDownloader(this);
        downloader.makeHashRequest(new TaskDownloader.resInt() {
            @Override
            public void good() {
                try {
                    if (downloader.need2dl(downloader.netHash)) {
                        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this,
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
                                Toast.makeText(MainActivity.this,
                                        "Ошибка при загрузке заданий. Проверьте соединение.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
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
