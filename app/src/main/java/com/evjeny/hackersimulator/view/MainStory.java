package com.evjeny.hackersimulator.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.Act;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.game.NoActException;
import com.evjeny.hackersimulator.game.Storyline;
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
                        public void actStart() {

                        }

                        @Override
                        public void nextContent() {

                        }

                        @Override
                        public void nextAct(String actName) {
                            currentAct = actName;
                            pointerChangedInterface.onPointerChanged();
                        }

                        @Override
                        public void actEnd() {

                        }

                        @Override
                        public void stop() {

                        }

                        @Override
                        public void check(String code, long id) {
                            try {
                                sender.sendRequest(code, id, new TaskSender.ResultInterface() {
                                    @Override
                                    public void result(JSONObject res) {
                                        Toast.makeText(MainStory.this,
                                                res.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
