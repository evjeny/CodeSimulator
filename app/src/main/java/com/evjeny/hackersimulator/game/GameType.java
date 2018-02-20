package com.evjeny.hackersimulator.game;

/**
 * Created by Evjeny on 25.01.2018 18:29.
 */

public enum GameType {
    hacker("hacker"),
    proger("proger");

    public String text;

    GameType(String text) {
        this.text = text;
    }
}
