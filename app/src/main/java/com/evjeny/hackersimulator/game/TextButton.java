package com.evjeny.hackersimulator.game;

/**
 * Created by Evjeny on 21.01.2018 10:32.
 */

public class TextButton extends Button {

    public String name;

    TextButton(String name, String action) {
        super(name, action);
        this.name = name;
    }
}
