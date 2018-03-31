package com.evjeny.hackersimulator.game;

/**
 * Created by Evjeny on 21.01.2018 21:06.
 */

public class CodePart {
    public final Type type;
    public final String value;

    public CodePart(Type type) {
        this.type = type;
        this.value = "";
    }

    public CodePart(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        READABLE,
        WRITABLE
    }
}
