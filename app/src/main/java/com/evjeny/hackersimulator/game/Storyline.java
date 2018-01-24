package com.evjeny.hackersimulator.game;

import android.content.Context;

import com.evjeny.hackersimulator.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Evjeny on 19.01.2018 5:57.
 */

public class Storyline {

    private Context context;
    private FileLocalizer localizer;

    public Storyline(Context context) {
        this.context = context;
        this.localizer = new FileLocalizer(context);
    }

    public Scene get(int pos) throws StoryEndException, IOException, XmlPullParserException {
        if (pos <= context.getResources().getInteger(R.integer.mx_n_scene)) {
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
