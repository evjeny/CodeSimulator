package com.evjeny.hackersimulator.game;

import android.content.Context;

import com.evjeny.hackersimulator.model.ActLocalizer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Evjeny on 19.01.2018 5:57.
 */

public class Storyline {

    private ActLocalizer localizer;

    public Storyline(Context context, GameType type) throws IOException {
        this.localizer = new ActLocalizer(context, type);
    }

    public Act get(String actName) throws NoActException, IOException, XmlPullParserException {
        try {
            return localizer.get(actName);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            throw new NoActException();
        }
    }

}
