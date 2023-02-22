package com.disney.qa.api.nhl.support;

import com.disney.qa.api.nhl.mobile.NhlContentApiChecker;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NhlSkaterData{

    private String fullName = "";

    private String firstName = "";

    private String lastName = "";

    private String primaryNumber = "";

    private String primaryPosition = "";

    private String games = "";

    private String goals = "";

    private String assists = "";

    private String points = "";

    private String plusMinus = "";

    private String pim = "";

    private String powerPlayGoals = "";

    private String gameWinningGoals = "";

    private String shots = "";

    private String shotPct = "";

    private String faceOffPct = "";

    private String goalsAgainst = "";

    private String goalAgainstAverage = "";

    private String savePercentage = "";

    private String shutouts = "";

    private String timeOnIce = "";

    private String wins = "";

    private String losses = "";

    private String overtimes = "";

    /*
     * Player node which pulls information based on playerID found within the schedule call.
     * Cannot be used for Stats page display validations as information such as Team tricode, ranked value, etc
     * as that information is not stored in the player object within those responses.
     */
    public NhlSkaterData playerFromRosterNode(JsonNode playerNode, String id){
        NhlContentApiChecker apiChecker = new NhlContentApiChecker();

        setFullName(apiChecker.getPlayerAttribute(playerNode, id, "fullName"));
        setFirstName(apiChecker.getPlayerAttribute(playerNode, id, "firstName"));
        setLastName(apiChecker.getPlayerAttribute(playerNode, id, "lastName"));
        setPrimaryNumber(apiChecker.getPlayerAttribute(playerNode, id, "primaryNumber"));
        setPrimaryPosition(apiChecker.getPlayerAttribute(playerNode, id, "primaryPosition.code"));

        setGames(apiChecker.getPlayerAttribute(playerNode, id, ".games"));
        setGoals(apiChecker.getPlayerAttribute(playerNode, id, ".goals"));
        setAssists(apiChecker.getPlayerAttribute(playerNode, id, ".assists"));
        setPoints(apiChecker.getPlayerAttribute(playerNode, id, ".points"));
        setPlusMinus(apiChecker.getPlayerAttribute(playerNode, id, ".plusMinus"));
        setPim(apiChecker.getPlayerAttribute(playerNode, id, ".pim"));
        setPowerPlayGoals(apiChecker.getPlayerAttribute(playerNode, id, ".powerPlayGoals"));
        setGameWinningGoals(apiChecker.getPlayerAttribute(playerNode, id, ".gameWiningGoals"));
        setShots(apiChecker.getPlayerAttribute(playerNode, id, ".shots"));
        setShotPct(apiChecker.getPlayerAttribute(playerNode, id, ".shotPct"));
        setFaceOffPct(apiChecker.getPlayerAttribute(playerNode, id, ".faceOffPct"));

        setGoalsAgainst(apiChecker.getPlayerAttribute(playerNode, id, ".goalsAgainst"));
        setGoalAgainstAverage(apiChecker.getPlayerAttribute(playerNode, id, ".goalAgainstAverage"));
        setSavePercentage(apiChecker.getPlayerAttribute(playerNode, id, ".savePercentage"));
        setShutouts(apiChecker.getPlayerAttribute(playerNode, id, ".shutouts"));
        setTimeOnIce(apiChecker.getPlayerAttribute(playerNode, id, ".timeOnIce"));

        setWins(apiChecker.getPlayerAttribute(playerNode, id, ".wins"));
        setLosses(apiChecker.getPlayerAttribute(playerNode, id, ".losses"));
        setOvertimes(apiChecker.getPlayerAttribute(playerNode, id, ".ot"));

        return this;
    }

    private void setFullName(String fullName){
        this.fullName = fullName;
    }

    private void setFirstName(String firstName){
        this.firstName = firstName;
    }

    private void setLastName(String lastName){
        this.lastName = lastName;
    }

    private void setPrimaryNumber(String primaryNumber){
        this.primaryNumber = primaryNumber;
    }

    private void setPrimaryPosition(String primaryPosition){
        this.primaryPosition = primaryPosition;
    }

    private void setGames(String games){
        if(games.isEmpty()){
            games = "0";
        }
        this.games = games;
    }

    private void setGoals(String goals){
        this.goals = goals;
    }

    private void setAssists(String assists){
        this.assists = assists;
    }

    private void setPoints(String points){
        this.points = points;
    }

    private void setPlusMinus(String plusMinus){
        this.plusMinus = plusMinus;
    }

    private void setPim(String pim){
        this.pim = pim;
    }

    private void setPowerPlayGoals(String powerPlayGoals){
        this.powerPlayGoals = powerPlayGoals;
    }

    private void setGameWinningGoals(String gameWinningGoals){
        this.gameWinningGoals = gameWinningGoals;
    }

    private void setShots(String shots){
        this.shots = shots;
    }

    private void setShotPct(String shotPct){
        this.shotPct = shotPct;
    }

    private void setFaceOffPct(String faceOffPct){
        this.faceOffPct = faceOffPct;
    }

    private void setGoalsAgainst(String goalsAgainst){
        this.goalsAgainst =  goalsAgainst;
    }

    private void setGoalAgainstAverage(String goalAgainstAverage){
        if(!goalAgainstAverage.isEmpty()){
            BigDecimal bd = new BigDecimal(goalAgainstAverage);
            goalAgainstAverage = bd.setScale(2, RoundingMode.HALF_UP).toString();
        }
        this.goalAgainstAverage =  goalAgainstAverage;
    }

    private void setSavePercentage(String savePercentage){
        if(!savePercentage.isEmpty()) {
            BigDecimal bd = new BigDecimal(savePercentage);
            savePercentage = bd.setScale(3, RoundingMode.HALF_UP).toString();
        }
        this.savePercentage = savePercentage;
    }

    private void setShutouts(String shutouts){
        this.shutouts = shutouts;
    }

    private void setTimeOnIce(String timeOnIce){
        this.timeOnIce = timeOnIce;
    }

    private void setWins(String wins){
        this.wins = wins;
    }

    private void setLosses(String losses){
        this.losses = losses;
    }

    private void setOvertimes(String overtimes){
        this.overtimes = overtimes;
    }

    public String getFullName(){
        return fullName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPrimaryNumber(){
        return primaryNumber;
    }

    public String getPrimaryPosition(){
        return primaryPosition;
    }

    public String getGames(){
        return games;
    }

    public String getGoals(){
        return goals;
    }

    public String getAssists(){
        return assists;
    }

    public String getPoints(){
        return points;
    }

    public String getPlusMinus(){
        return plusMinus;
    }

    public String getPim(){
        return pim;
    }

    public String getPowerPlayGoals(){
        return powerPlayGoals;
    }

    public String getGameWinningGoals(){
        return gameWinningGoals;
    }

    public String getShots(){
        return shots;
    }

    public String getShotPct(){
        return shotPct;
    }

    public String getFaceOffPct(){
        return faceOffPct;
    }

    public String getGoalsAgainst(){
        return goalsAgainst;
    }

    public String getGoalAgainstAverage(){
        return goalAgainstAverage;
    }

    public String getSavePercentage(){
        return savePercentage;
    }

    public String getShutouts(){
        return shutouts;
    }

    public String getTimeOnIce(){
        return timeOnIce;
    }

    public String getWins(){
        return wins;
    }

    public String getLosses(){
        return losses;
    }

    public String getOvertimes(){
        return overtimes;
    }
}
