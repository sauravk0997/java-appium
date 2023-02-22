package com.disney.qa.api.nhl.pojo;

public class MetaData {
    
    private boolean isManuallyScored;

    private boolean isSplitSquad;

    public boolean isManuallyScored() {
        return isManuallyScored;
    }

    public void setIsManuallyScored(boolean isManuallyScored) {
        this.isManuallyScored = isManuallyScored;
    }

    public boolean isSplitSquad() {
        return isSplitSquad;
    }

    public void setIsSplitSquad(boolean isSplitSquad) {
        this.isSplitSquad = isSplitSquad;
    }
}
