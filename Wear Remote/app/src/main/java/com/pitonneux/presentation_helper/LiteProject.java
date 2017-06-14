package com.pitonneux.presentation_helper;

/**
 * Created by cacau on 1/15/2017.
 */

// a class to get the project notes from bluetooth
public class LiteProject {

    private int slideCount;
    private String[] notes;
    private int[] animationsCount;
    private String name;

    public int getSlideCount() {
        return slideCount;
    }

    public String[] getNotes() {
        return notes;
    }

    public int[] getAnimationsCount() {
        return animationsCount;
    }

    public String getName() {
        return name;
    }
}
