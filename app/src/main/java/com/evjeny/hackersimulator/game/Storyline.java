package com.evjeny.hackersimulator.game;

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Evjeny on 19.01.2018 5:57.
 */

public class Storyline {

    private Context context;
    private SceneLocalizer localizer;

    private int size;

    public Storyline(Context context, GameType type) throws IOException {
        this.context = context;
        this.localizer = new SceneLocalizer(context, type);
        this.size = context.getAssets().list(type.text + File.separator + "scenes").length;
    }

    public Scene get(int pos) throws StoryEndException, IOException, XmlPullParserException {
        if (pos <= size) {
            try {
                return localizer.get(pos);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        else throw new StoryEndException();
        return null;
    }

}
