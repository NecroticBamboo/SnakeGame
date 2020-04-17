package com.NecroticBamboo;

public class Options {
    private boolean bordersOption;
    private boolean doublePointsOption;
    private boolean objectsOption;

    public Options(boolean bordersOptionIn, boolean doublePointsOptionIn, boolean objectsOptionIn) {
        bordersOption = bordersOptionIn;
        doublePointsOption = doublePointsOptionIn;
        objectsOption=objectsOptionIn;
    }

    public boolean getBorderOption() {
        return bordersOption;
    }

    public void setBorderOption(boolean bordersOptionIn) {
        bordersOption = bordersOptionIn;
    }

    public boolean getDoublePointsOption() {
        return doublePointsOption;
    }

    public void setDoublePointsOption(boolean doublePointsOptionIn) {
        doublePointsOption = doublePointsOptionIn;
    }

    public boolean getObjectsOption(){return objectsOption;}

    public void setObjectsOption(boolean objectsOptionIn){objectsOption=objectsOptionIn;}
}
