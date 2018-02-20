package com.evjeny.hackersimulator.game;

import java.io.Serializable;

/**
 * Created by Evjeny on 18.02.2018 17:06.
 */

public class NoteItem implements Serializable {
    public String label, filepath;

    public NoteItem(String label, String filepath) {
        this.label = label;
        this.filepath = filepath;
    }
}
