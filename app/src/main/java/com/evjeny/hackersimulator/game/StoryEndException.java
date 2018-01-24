package com.evjeny.hackersimulator.game;

/**
 * Created by Evjeny on 20.01.2018 16:34.
 */

public class StoryEndException extends Throwable {
    public StoryEndException() {
        super("Story ended!");
    }

    @Override

    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
