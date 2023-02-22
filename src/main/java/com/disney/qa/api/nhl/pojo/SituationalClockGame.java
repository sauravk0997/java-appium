package com.disney.qa.api.nhl.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boyle on 4/13/17.
 */
public class SituationalClockGame {

    @JsonProperty
    private String gameState;

    @JsonProperty
    private String gameType;

    @JsonProperty
    private boolean inSituation;

    @JsonProperty
    private int period;

    @JsonProperty
    private int skatersAway;

    @JsonProperty
    private int skatersHome;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String state) {
        this.gameState = state;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String type) {
        this.gameType = type;
    }

    public boolean getInSituation() {
        return inSituation;
    }

    public void setInSituation(boolean situation) {
        this.inSituation = situation;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getSkatersAway() {
        return skatersAway;
    }

    public void setSkatersAway(String away) {
        this.skatersAway = Integer.parseInt(away);
    }

    public int getSkatersHome() {
        return skatersHome;
    }

    public void setSkatersHome(String home) {
        this.skatersHome = Integer.parseInt(home);
    }
}
