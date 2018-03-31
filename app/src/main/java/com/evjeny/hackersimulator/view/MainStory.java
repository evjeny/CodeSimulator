package com.evjeny.hackersimulator.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.Act;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.game.NoActException;
import com.evjeny.hackersimulator.game.Storyline;
import com.evjeny.hackersimulator.model.ActGenerator;
import com.evjeny.hackersimulator.model.GameSaver;
import com.evjeny.hackersimulator.model.TaskDownloader;
import com.evjeny.hackersimulator.model.TaskSender;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainStory extends AppCompatActivity {

    private GameSaver saver;
    private TaskSender sender;
    private TaskDownloader downloader;
    private GameSave save;
    private Storyline storyline;
    private ActGenerator generator;
    private pcInterface pointerChangedInterface;

    private String currentAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_story);

        LinearLayout main_fragment = findViewById(R.id.main_story_fragment);
        LinearLayout main_bar = findViewById(R.id.main_story_bar);

        save = (GameSave) getIntent().getSerializableExtra("save");
        saver = new GameSaver(this);
        sender = new TaskSender(this);
        downloader = new TaskDownloader(this);

        try {
            storyline = new Storyline(this, save.getGameType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        generator = new ActGenerator(this,
                fragmentManager, main_fragment, main_bar, save.getGameType());

        currentAct = save.getCurrentAct();

        pointerChangedInterface = new pcInterface() {
            @Override
            public void onPointerChanged() {
                try {
                    final Act current = storyline.get(currentAct);
                    generator.generateActAuto(current, new ActGenerator.storyInterface() {
                        @Override
                        public void nextAct(String actName) {
                            currentAct = actName;
                            pointerChangedInterface.onPointerChanged();
                        }

                        @Override
                        public void stop() {

                        }

                        @Override
                        public void check(final String actName, String code, long id) {
                            Log.d("MainStory", "check: "+code+", "+id);
                            if (!code.equals("")) {
                                sender.sendRequest(code, id, new TaskSender.ResultInterface() {
                                    @Override
                                    public void result(JSONObject res) {
                                        handleResult(actName, res);
                                    }
                                });
                            }
                        }

                        @Override
                        public void hint(String hintText) {
                            showHint(hintText);
                        }

                        @Override
                        public void endStory() {
                            Toast.makeText(MainStory.this, "The end!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                } catch (NoActException e) {
                    e.printStackTrace();
                    Toast.makeText(MainStory.this, "No such act!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        pointerChangedInterface.onPointerChanged();
    }

    private void handleResult(String actName, JSONObject res) {
        try {
            int resultCode = res.getInt("resultCode");
            String result = res.getString("result");
            String input = res.getString("input");
            String logTest = res.getString("log_test");

            if (resultCode == 1) {
                currentAct = actName;
                pointerChangedInterface.onPointerChanged();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ошибка!");
                String message = null;
                if (resultCode == -1) {
                    message = "Ошибка сервера";
                } else if (resultCode == 2) {
                    message = "Программа не прошла проверку на части тестов";
                } else if (resultCode == 3) {
                    message = "Ошибка компиляции";
                } else if (resultCode == 4) {
                    message = "Ошибка во время исполнения программы";
                } else if (resultCode == 5) {
                    message = "Превышено время исполнения";
                } else if (resultCode == 6) {
                    message = "Превышен лимит памяти";
                }
                builder.setMessage(message);
                builder.setNegativeButton("Ok", null);
                builder.show().show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showHint(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hint");
        builder.setMessage(text);
        builder.setNegativeButton("Ok", null);
        builder.show().show();
    }

    private interface pcInterface {
        void onPointerChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save.setCurrentAct(currentAct);
        try {
            saver.writeLevel(save);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
