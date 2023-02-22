package com.disney.qa.api.nhl;

/**
 * Created by boyle on 2/24/17.
 */
public enum NhlGameStates {

    BLOCKED_SHOT("Blocked Shot"),
    FACEOFF("Faceoff"),
    GIVEAWAY("Giveaway"),
    GOAL("Goal"),
    HIT("Hit"),
    MISSED_SHOT("Missed Shot"),
    PENALTY("Penalty"),
    SHOT("Shot"),
    TAKEAWAY("Takeaway");

    private String gameState;

    NhlGameStates(String state) {
        this.gameState = state;
    }

    public String getGameState() {
        return this.gameState;
    }
}
