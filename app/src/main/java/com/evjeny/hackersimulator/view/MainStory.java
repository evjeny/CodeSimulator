package com.evjeny.hackersimulator.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.game.Scene;
import com.evjeny.hackersimulator.game.StoryEndException;
import com.evjeny.hackersimulator.game.Storyline;
import com.evjeny.hackersimulator.model.GameSaver;
import com.evjeny.hackersimulator.model.TaskSender;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainStory extends AppCompatActivity {

    private GameSaver saver;
    private TaskSender sender;
    private GameSave save;
    private Storyline storyline;
    private SceneGenerator generator;
    private pcInterface pointerChangedInterface;

    private int pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_story);

        LinearLayout main_fragment = findViewById(R.id.main_story_fragment);
        LinearLayout main_bar = findViewById(R.id.main_story_bar);

        save = (GameSave) getIntent().getSerializableExtra("save");
        saver = new GameSaver(this);
        sender = new TaskSender(this);

        try {
            storyline = new Storyline(this, save.getGameType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        generator = new SceneGenerator(this,
                fragmentManager, main_fragment, main_bar, save.getGameType());

        pointer = save.getCurrentScene();

        pointerChangedInterface = new pcInterface() {
            @Override
            public void onPointerChanged() {
                try {
                    Scene current = storyline.get(pointer);
                    generator.generateScene(current, new SceneGenerator.storyInterface() {
                        @Override
                        public void actStart() {

                        }

                        @Override
                        public void nextContent() {

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

                        @Override
                        public void sceneEnd() {
                            pointer++;
                            pointerChangedInterface.onPointerChanged();
                        }
                    });

                } catch (StoryEndException e) {
                    e.printStackTrace();
                    Toast.makeText(MainStory.this, "Thx 4 playing!", Toast.LENGTH_SHORT).show();
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
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
        save.setCurrentScene(pointer);
        try {
            saver.writeLevel(save);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
