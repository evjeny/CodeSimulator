package com.evjeny.hackersimulator.game;

/**
 * Created by evjeny on 18.03.2018 15:12.
 */

public class Task {
    public String name, text;
    public long id;

    public Task(String name, long id, String text) {
        this.name = name;
        this.text = text;
        this.id = id;
    }

    @Override
    public String toString() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<task>\n" +
                "\t<text>" + text + "</text>\n" +
                "\t<id>" + id + "</id>\n" +
                "</task>";
    }
}
