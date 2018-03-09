package com.evjeny.hackersimulator.game;

import java.io.Serializable;

/**
 * Created by evjeny on 08.03.2018 19:49.
 */

public class IText implements Serializable {

    public String text;
    public int color;
    public float posX, posY, textSize;

    IText(String text, int color, float posX, float posY, float textSize) {
        this.text = text;
        this.color = color;
        this.posX = posX;
        this.posY = posY;
        this.textSize = textSize;
    }
}
