package com.evjeny.hackersimulator.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evjeny on 19.01.2018 6:29.
 */

public class CodeContent extends Content{

    public long id;

    private String hint;

    public CodeContent(List<CodePart> fragments, long id, String hint) {
        super(new ArrayList<Object>(fragments));
        this.id = id;
        this.hint = hint;
    }

    public List<CodePart> getFragments() {
        List<CodePart> fragments = new ArrayList<>();
        for (Object obj : this.contents) fragments.add((CodePart) obj);
        return fragments;
    }

    public CodePart getFragment(int index) {
        return (CodePart) this.contents.get(index);
    }

    public String getHint() {
        return hint;
    }
}
