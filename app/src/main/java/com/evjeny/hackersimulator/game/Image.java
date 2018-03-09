package com.evjeny.hackersimulator.game;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by evjeny on 08.03.2018 20:06.
 */

public class Image {
    public Bitmap bitmap;
    public ArrayList<IText> texts;

    Image(Bitmap bitmap, ArrayList<IText> texts) {
        this.bitmap = bitmap;
        this.texts = texts;
    }
}
