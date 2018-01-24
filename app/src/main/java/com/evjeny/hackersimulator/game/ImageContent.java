package com.evjeny.hackersimulator.game;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evjeny on 19.01.2018 6:25.
 */

public class ImageContent extends Content {
    public ImageContent(List<Bitmap> images) {
        super(new ArrayList<Object>(images));
    }

    public ArrayList<Bitmap> getImages() {
        ArrayList<Bitmap> images = new ArrayList<>();
        for (Object obj : this.contents) images.add((Bitmap) obj);
        return images;
    }

    public Bitmap getImage(int index) {
        return (Bitmap) this.contents.get(index);
    }
}
