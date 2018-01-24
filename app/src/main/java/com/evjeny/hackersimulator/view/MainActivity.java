package com.evjeny.hackersimulator.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.Scene;
import com.evjeny.hackersimulator.game.StoryEndException;
import com.evjeny.hackersimulator.game.Storyline;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private SharedPreferences sp;
    private Storyline storyline;
    private SceneGenerator generator;
    private pcInterface pointerChangedInterface;

    private LinearLayout main_fragment, main_bar;

    private int pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_fragment = findViewById(R.id.main_fragment);
        main_bar = findViewById(R.id.main_bar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        storyline = new Storyline(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        generator = new SceneGenerator(this,
                fragmentManager, main_fragment, main_bar);

        //initialing pointer
        pointer = 1;

        pointerChangedInterface = new pcInterface() {
            @Override
            public void onPointerChanged() {
                try {
                    Scene current = storyline.get(pointer);
                    generator.generateSceneAuto(current, new SceneGenerator.storyInterface() {
                        @Override
                        public void nextAct() {

                        }

                        @Override
                        public void stop() {

                        }

                        @Override
                        public void check(ArrayList<String> code) {
                            //checking code here
                        }

                        @Override
                        public void end() {
                            pointer++;
                            pointerChangedInterface.onPointerChanged();
                        }
                    });

                } catch (StoryEndException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Thx 4 playing!", Toast.LENGTH_SHORT).show();
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

}
