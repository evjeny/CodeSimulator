package com.evjeny.hackersimulator.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evjeny on 19.01.2018 6:13.
 */

public class StoryContent extends Content {

    public StoryContent(List<String> messages) {
        super(new ArrayList<Object>(messages));
    }

    public List<String> getMessages() {
        List<String> messages = new ArrayList<String>();
        for (Object obj : this.contents) messages.add(obj != null ? obj.toString() : "");
        return messages;
    }

    public String getMessage(int index) {
        return (String) this.contents.get(index);
    }
}
