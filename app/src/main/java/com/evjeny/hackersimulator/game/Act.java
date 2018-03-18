package com.evjeny.hackersimulator.game;

/**
 * Created by Evjeny on 20.01.2018 16:31.
 */
public class Act {

    public final ActType type;
    public final Content content;
    public final Bar bar;

    public Act(ActType type, Content content, Bar bar) {
        this.type = type;
        this.content = content;
        this.bar = bar;
    }
}
