package com.aurawin.scs.media;


import java.awt.*;

public enum Scale {
    sNone(new Point(1,1)),
    sIcon(new Point(48,48)),
    sThumb(new Point(64,64)),
    sPalm(new Point (128,128)),
    sDisplay(new Point(640,480));

    Scale(Point value){this.value=value;}
    private final Point value;
    public Point getValue(){return value;}
    }
