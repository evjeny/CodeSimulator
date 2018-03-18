package com.evjeny.hackersimulator.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evjeny on 19.01.2018 6:29.
 */

public class CodeContent extends Content{

    public long id;

    public CodeContent(List<CodePart> fragments, long id) {
        super(new ArrayList<Object>(fragments));
    }

    public List<CodePart> getFragments() {
        List<CodePart> fragments = new ArrayList<>();
        for (Object obj : this.contents) fragments.add((CodePart) obj);
        return fragments;
    }

    public CodePart getFragment(int index) {
        return (CodePart) this.contents.get(index);
    }

}
