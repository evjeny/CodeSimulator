package com.evjeny.hackersimulator.game;

/**
 * Created by evjeny on 23.03.2018 6:36.
 */

public class NoActException extends Throwable {
    public NoActException() {
        super("No such act!");
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
