package com.evjeny.hackersimulator.game;

/**
 * Created by Evjeny on 19.01.2018 5:58.
 */

public class Scene {

    public final SceneType type;
    public final Act act;

    public Scene(SceneType type, Act act) {
        this.type = type;
        this.act = act;
    }

}
