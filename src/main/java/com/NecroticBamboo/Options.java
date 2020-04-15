package com.NecroticBamboo;

public class Options {
    private boolean bordersOption;
    private boolean doublePointsOption;

    public Options(boolean bordersOptionIn, boolean doublePointsOptionIn) {
        bordersOption = bordersOptionIn;
        doublePointsOption = doublePointsOptionIn;
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
}
