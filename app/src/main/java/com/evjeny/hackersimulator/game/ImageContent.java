package com.evjeny.hackersimulator.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evjeny on 19.01.2018 6:25.
 */

public class ImageContent extends Content {
    ImageContent(List<Image> images) {
        super(new ArrayList<Object>(images));
    }

    public ArrayList<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        for (Object obj : this.contents) images.add((Image) obj);
        return images;
    }

    public Image getImage(int index) {
        return (Image) this.contents.get(index);
    }
}
