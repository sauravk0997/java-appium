package com.disney.qa.api.nhl.support;

/*
 * Created by bsuscavage
 * Last updated as of January 3rd, 2020
 *
 * Updated Functionality: Added storage of 'last 10 games' stats
 */

import com.disney.qa.api.nhl.mobile.NhlContentApiChecker;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.DecimalFormat;
import java.util.List;

public class NhlTeamData {

    private String id = "";

    private String name  = "";

    private String shortName = "";

    private String abbreviation = "";

    private String gamesPlayed = "";

    private String wins = "";
    private String winsRank = "";
    private String lastTenWins = "";

    private String losses = "";
    private String lossesRank = "";
    private String lastTenLosses = "";

    private String ot = "";
    private String otRank = "";
    private String lastTenOt = "";

    private String pts = "";
    private String ptsRank = "";

    private String ptsPctg = "";
    private String ptsPctgRank = "";

    private String goalsPerGame = "";
    private String goalsPerGameRank = "";

    private String goalsAgainstPerGame = "";
    private String goalsAgainstPerGameRank = "";

    private String evGGARatio = "";
    private String evGGARatioRank = "";

    private String powerPlayPercentage = "";
    private String powerPlayPercentageRank = "";

    private String powerPlayGoals = "";
    private String powerPlayGoalsRank = "";

    private String powerPlayGoalsAgainst = "";
    private String powerPlayGoalsAgainstRank = "";

    private String powerPlayOpportunities = "";
    private String powerPlayOpportunitiesRank = "";

    private String penaltyKillPercentage = "";
    private String penaltyKillPercentageRank = "";

    private String penaltyKillOpportunitiesRank = "";

    private String shotsPerGame = "";
    private String shotsPerGameRank = "";

    private String shotsAllowed = "";
    private String shotsAllowedRank = "";

    private String winScoreFirst = "";
    private String winScoreFirstRank = "";

    private String winOppScoreFirst = "";
    private String winOppScoreFirstRank = "";

    private String winLeadFirstPer = "";
    private String winLeadFirstPerRank = "";

    private String winLeadSecondPer = "";
    private String winLeadsecondPerRank = "";

    private String winOutshootOpp = "";
    private String winOutshootOppRank = "";

    private String winOutshotByOpp = "";
    private String winoutshotByOppRank = "";

    private String faceOffsTaken = "";
    private String faceOffsTakenRank = "";

    private String faceOffsWon = "";
    private String faceOffsWonRank = "";

    private String faceOffsLost = "";
    private String faceOffsLostRank = "";

    private String faceOffWinPercentage = "";
    private String faceOffWinPercentageRank  = "";

    private String shootingPctg = "";
    private String shootingPctRank = "";

    private String savePctg = "";
    private String savePctRank = "";

    private String prettyLeagueStandings = "";
    private String prettyL10Standings = "";

    private static DecimalFormat goalFormat = new DecimalFormat("0.00");

    public NhlTeamData(JsonNode data, JsonNode teamStandingsData, String teamName){
        NhlContentApiChecker apiChecker = new NhlContentApiChecker();

        setName(apiChecker.getTeamVariableData(data, teamName, "name"));
        setId(apiChecker.getTeamVariableData(data, teamName, "id"));
        setShortName(teamName);
        setAbbreviation(apiChecker.getTeamVariableData(data, teamName, "abbreviation"));

        setGamesPlayed(apiChecker.getTeamVariableData(data, teamName, "teamStats..gamesPlayed"));
        setWins(apiChecker.getTeamVariableData(data, teamName, "teamStats..wins"));
        setLosses(apiChecker.getTeamVariableData(data, teamName, "teamStats..losses"));
        setOt(apiChecker.getTeamVariableData(data, teamName, "teamStats..ot"));
        setPts(apiChecker.getTeamVariableData(data, teamName, "teamStats..pts"));
        setPtsPctg(apiChecker.getTeamVariableData(data, teamName, "teamStats..ptPctg"));
        setGoalsPerGame(apiChecker.getTeamVariableData(data, teamName, "teamStats..goalsPerGame"));
        setGoalsAgainstPerGame(apiChecker.getTeamVariableData(data, teamName, "teamStats..goalsAgainstPerGame"));
        setEvGGARatio(apiChecker.getTeamVariableData(data, teamName, "teamStats..evGGARatio"));
        setPowerPlayPercentage(apiChecker.getTeamVariableData(data, teamName, "teamStats..powerPlayPercentage"));
        setPowerPlayGoals(apiChecker.getTeamVariableData(data, teamName, "teamStats..powerPlayGoals"));
        setPowerPlayGoalsAgainst(apiChecker.getTeamVariableData(data, teamName, "teamStats..powerPlayGoalsAgainst"));
        setPowerPlayOpportunities(apiChecker.getTeamVariableData(data, teamName, "teamStats..powerPlayOpportunities"));
        setPenaltyKillPercentage(apiChecker.getTeamVariableData(data, teamName, "teamStats..penaltyKillPercentage"));
        setPenaltyKillOpportunitiesRank(apiChecker.getTeamVariableData(data, teamName, "teamStats..penaltyKillOpportunities"));
        setShotsPerGame(apiChecker.getTeamVariableData(data, teamName, "teamStats..shotsPerGame"));
        setShotsAllowed(apiChecker.getTeamVariableData(data, teamName, "teamStats..shotsAllowed"));
        setWinScoreFirst(apiChecker.getTeamVariableData(data, teamName, "teamStats..winScoreFirst"));
        setWinOppScoreFirst(apiChecker.getTeamVariableData(data, teamName, "teamStats..winOppScoreFirst"));
        setWinLeadFirstPer(apiChecker.getTeamVariableData(data, teamName, "teamStats..winLeadFirstPer"));
        setWinLeadSecondPer(apiChecker.getTeamVariableData(data, teamName, "teamStats..winLeadSecondPer"));
        setWinOutshootOpp(apiChecker.getTeamVariableData(data, teamName, "teamStats..winOutshootOpp"));
        setWinOutshotByOpp(apiChecker.getTeamVariableData(data, teamName, "teamStats..winOutshotByOpp"));
        setFaceOffsTaken(apiChecker.getTeamVariableData(data, teamName, "teamStats..faceOffsTaken"));
        setFaceOffsWon(apiChecker.getTeamVariableData(data, teamName, "teamStats..faceOffsWon"));
        setFaceOffsLost(apiChecker.getTeamVariableData(data, teamName, "teamStats..faceOffsLost"));
        setFaceOffWinPercentage(apiChecker.getTeamVariableData(data, teamName, "teamStats..faceOffWinPercentage"));
        setShootingPctg(apiChecker.getTeamVariableData(data, teamName, "teamStats..shootingPctg"));
        setShootingPctRank(apiChecker.getTeamVariableData(data, teamName, "teamStats..shootingPctRank"));
        setSavePctg(apiChecker.getTeamVariableData(data, teamName, "teamStats..savePctg"));
        setSavePctRank(apiChecker.getTeamVariableData(data, teamName, "teamStats..savePctRank"));

        List<String> lastTenData = apiChecker.getLastTenStats(teamStandingsData, this.name);

        setLastTenWins(lastTenData);
        setLastTenLosses(lastTenData);
        setLastTenOt(lastTenData);

        setPrettyLeagueStandings();
        setPrettyL10Standings();
    }

    private void setId(List<String> id){
        this.id = id.get(0);
    }

    private void setName(List<String> name){
        this.name = name.get(0);
    }

    private void setShortName(String shortName){
        this.shortName = shortName;
    }

    private void setAbbreviation(List<String> abbreviation){
        this.abbreviation = abbreviation.get(0);
    }

    private void setGamesPlayed(List<String> gamesPlayedData){
        this.gamesPlayed = gamesPlayedData.get(0);
    }

    private void setWins(List<String> winsData){
        this.wins = winsData.get(0);
        this.winsRank = winsData.get(1);
    }

    private void setLastTenWins(List<String> lastTenWins){
        this.lastTenWins =  lastTenWins.get(0);
    }

    private void setLosses(List<String> lossesData){
        this.losses = lossesData.get(0);
        this.lossesRank = lossesData.get(1);
    }

    private void setLastTenLosses(List<String> lastTenLosses){
        this.lastTenLosses = lastTenLosses.get(1);
    }

    private void setOt(List<String> otData){
        this.ot = otData.get(0);
        this.otRank = otData.get(1);
    }

    private void setLastTenOt(List<String> lastTenOt){
        this.lastTenOt = lastTenOt.get(2);
    }

    private void setPts(List<String> ptsData){
        this.pts = ptsData.get(0);
        this.ptsRank = ptsData.get(1);
    }

    private void setPtsPctg(List<String> ptsPctgData){
        this.ptsPctg = ptsPctgData.get(0);
        this.ptsPctgRank = ptsPctgData.get(1);
    }

    private void setGoalsPerGame(List<String> goalsPerGameData){
        this.goalsPerGame = goalFormat.format(Double.parseDouble(goalsPerGameData.get(0)));
        this.goalsPerGameRank = goalsPerGameData.get(1);
    }

    private void setGoalsAgainstPerGame(List<String> goalsAgainstPerGameData){
        this.goalsAgainstPerGame = goalFormat.format(Double.parseDouble(goalsAgainstPerGameData.get(0)));
        this.goalsAgainstPerGameRank = goalsAgainstPerGameData.get(1);
    }

    private void setEvGGARatio(List<String> evGGARatioData){
        this.evGGARatio =  evGGARatioData.get(0);
        this.evGGARatioRank =  evGGARatioData.get(1);
    }

    private void setPowerPlayPercentage(List<String> powerPlayPercentageData){
        this.powerPlayPercentage = powerPlayPercentageData.get(0);
        this.powerPlayPercentageRank = powerPlayPercentageData.get(1);
    }

    private void setPowerPlayGoals(List<String> powerPlayGoalsData){
        this.powerPlayGoals = powerPlayGoalsData.get(0);
        this.powerPlayGoalsRank = powerPlayGoalsData.get(1);
    }

    private void setPowerPlayGoalsAgainst(List<String> powerPlayGoalsAgainstData){
        this.powerPlayGoalsAgainst = powerPlayGoalsAgainstData.get(0);
        this.powerPlayGoalsAgainstRank = powerPlayGoalsAgainstData.get(1);
    }

    private void setPowerPlayOpportunities(List<String> powerPlayOpportunitiesData){
        this.powerPlayOpportunities = powerPlayOpportunitiesData.get(0);
        this.powerPlayOpportunitiesRank = powerPlayOpportunitiesData.get(1);
    }

    private void setPenaltyKillPercentage(List<String> penaltyKillPercentageData){
        this.penaltyKillPercentage = penaltyKillPercentageData.get(0);
        this.penaltyKillPercentageRank = penaltyKillPercentageData.get(1);
    }

    private void setPenaltyKillOpportunitiesRank(List<String> penaltyKillOpportunitiesRankData){
        this.penaltyKillOpportunitiesRank =  penaltyKillOpportunitiesRankData.get(0);
    }

    private void setShotsPerGame(List<String> shotsPerGameData){
        this.shotsPerGame = shotsPerGameData.get(0);
        this.shotsPerGameRank = shotsPerGameData.get(1);
    }

    private void setShotsAllowed(List<String> shotsAllowedData){
        this.shotsAllowed = shotsAllowedData.get(0);
        this.shotsAllowedRank = shotsAllowedData.get(1);
    }

    private void setWinScoreFirst(List<String> winScoreFirstData){
        this.winScoreFirst = winScoreFirstData.get(0);
        this.winScoreFirstRank = winScoreFirstData.get(1);
    }

    private void setWinOppScoreFirst(List<String> winOppScoreFirstData){
        this.winOppScoreFirst = winOppScoreFirstData.get(0);
        this.winOppScoreFirstRank = winOppScoreFirstData.get(1);
    }

    private void setWinLeadFirstPer(List<String> winLeadFirstPerData){
        this.winLeadFirstPer = winLeadFirstPerData.get(0);
        this.winLeadFirstPerRank = winLeadFirstPerData.get(1);
    }

    private void setWinLeadSecondPer(List<String> winLeadSecondPerData){
        this.winLeadSecondPer = winLeadSecondPerData.get(0);
        this.winLeadsecondPerRank = winLeadSecondPerData.get(1);
    }

    private void setWinOutshootOpp(List<String> winOutshootOppData){
        this.winOutshootOpp = winOutshootOppData.get(0);
        this.winOutshootOppRank= winOutshootOppData.get(1);
    }

    private void setWinOutshotByOpp(List<String> winOutshotByOppData){
        this.winOutshotByOpp = winOutshotByOppData.get(0);
        this.winoutshotByOppRank = winOutshotByOppData.get(1);
    }

    private void setFaceOffsTaken(List<String> faceOffsTakenData){
        this.faceOffsTaken = faceOffsTakenData.get(0);
        this.faceOffsTakenRank = faceOffsTakenData.get(1);
    }

    private void setFaceOffsWon(List<String> faceOffsWonData){
        this.faceOffsWon = faceOffsWonData.get(0);
        this.faceOffsWonRank = faceOffsWonData.get(1);
    }

    private void setFaceOffsLost(List<String> faceOffsLostData){
        this.faceOffsLost = faceOffsLostData.get(0);
        this.faceOffsLostRank = faceOffsLostData.get(1);
    }

    private void setFaceOffWinPercentage(List<String> faceOffWinPercentageData){
        this.faceOffWinPercentage = faceOffWinPercentageData.get(0);
        this.faceOffWinPercentageRank = faceOffWinPercentageData.get(1);
    }

    private void setShootingPctg(List<String> shootingPctgData){
        this.shootingPctg = shootingPctgData.get(0);
    }

    private void setShootingPctRank(List<String> shootingPctRankData){
        this.shootingPctRank = shootingPctRankData.get(0);
    }

    private void setSavePctg(List<String> savePctgData){
        this.savePctg = savePctgData.get(0);
    }

    private void setSavePctRank(List<String> savePctRankData){
        this.savePctRank = savePctRankData.get(0);
    }

    private void setPrettyLeagueStandings(){
        this.prettyLeagueStandings = this.wins + "-" + this.losses + "-" + this.ot;
    }

    private void setPrettyL10Standings(){
        this.prettyL10Standings = "L10: " + this.lastTenWins + "-" + this.lastTenLosses + "-" + this.lastTenOt;
    }

    public String getId(){
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getEvGGARatio() {
        return evGGARatio;
    }

    public String getEvGGARatioRank() {
        return evGGARatioRank;
    }

    public String getGamesPlayed() {
        return gamesPlayed;
    }

    public String getGoalsAgainstPerGame() {
        return goalsAgainstPerGame;
    }

    public String getGetGoalsAgainstPerGameRank() {
        return goalsAgainstPerGameRank;
    }

    public String getGoalsPerGame() {
        return goalsPerGame;
    }

    public String getGoalsPerGameRank() {
        return goalsPerGameRank;
    }

    public String getLosses() {
        return losses;
    }

    public String getLossesRank() {
        return lossesRank;
    }

    public String getLastTenLosses(){
        return lastTenLosses;
    }

    public String getOt() {
        return ot;
    }

    public String getOtRank() {
        return otRank;
    }

    public String getLastTenOt(){
        return lastTenOt;
    }

    public String getPowerPlayPercentage() {
        return powerPlayPercentage;
    }

    public String getPowerPlayPercentageRank() {
        return powerPlayPercentageRank;
    }

    public String getPowerPlayGoals() {
        return powerPlayGoals;
    }

    public String getPowerPlayGoalsRank() {
        return powerPlayGoalsRank;
    }

    public String getPowerPlayGoalsAgainst() {
        return powerPlayGoalsAgainst;
    }

    public String getPowerPlayGoalsAgainstRank() {
        return powerPlayGoalsAgainstRank;
    }

    public String getPenaltyKillPercentage() {
        return penaltyKillPercentage;
    }

    public String getPenaltyKillPercentageRank() {
        return penaltyKillPercentageRank;
    }

    public String getPenaltyKillOpportunitiesRank(){
        return penaltyKillOpportunitiesRank;
    }

    public String getPts() {
        return pts;
    }

    public String getPtsRank() {
        return ptsRank;
    }

    public String getPowerPlayOpportunities() {
        return powerPlayOpportunities;
    }

    public String getPowerPlayOpportunitiesRank() {
        return powerPlayOpportunitiesRank;
    }

    public String getPtsPctg() {
        return ptsPctg;
    }

    public String getPtsPctgRank() {
        return ptsPctgRank;
    }

    public String getWins() {
        return wins;
    }

    public String getWinsRank() {
        return winsRank;
    }

    public String getLastTenWins(){
        return lastTenWins;
    }

    public String getShotsAllowed() {
        return shotsAllowed;
    }

    public String getShotsAllowedRank() {
        return shotsAllowedRank;
    }

    public String getFaceOffsLost() {
        return faceOffsLost;
    }

    public String getFaceOffsLostRank() {
        return faceOffsLostRank;
    }

    public String getFaceOffsTaken() {
        return faceOffsTaken;
    }

    public String getFaceOffsTakenRank() {
        return faceOffsTakenRank;
    }

    public String getFaceOffsWon() {
        return faceOffsWon;
    }

    public String getFaceOffsWonRank() {
        return faceOffsWonRank;
    }

    public String getFaceOffWinPercentage() {
        return faceOffWinPercentage;
    }

    public String getFaceOffWinPercentageRank() {
        return faceOffWinPercentageRank;
    }

    public String getSavePctg() {
        return savePctg;
    }

    public String getSavePctRank() {
        return savePctRank;
    }

    public String getShootingPctg() {
        return shootingPctg;
    }

    public String getShootingPctRank() {
        return shootingPctRank;
    }

    public String getShotsPerGame() {
        return shotsPerGame;
    }

    public String getShotsPerGameRank() {
        return shotsPerGameRank;
    }

    public String getWinLeadFirstPer() {
        return winLeadFirstPer;
    }

    public String getWinLeadFirstPerRank() {
        return winLeadFirstPerRank;
    }

    public String getWinLeadSecondPer() {
        return winLeadSecondPer;
    }

    public String getWinLeadsecondPerRank() {
        return winLeadsecondPerRank;
    }

    public String getWinOppScoreFirst() {
        return winOppScoreFirst;
    }

    public String getWinOppScoreFirstRank() {
        return winOppScoreFirstRank;
    }

    public String getWinOutshootOpp() {
        return winOutshootOpp;
    }

    public String getWinOutshootOppRank() {
        return winOutshootOppRank;
    }

    public String getWinOutshotByOpp() {
        return winOutshotByOpp;
    }

    public String getWinoutshotByOppRank() {
        return winoutshotByOppRank;
    }

    public String getWinScoreFirst() {
        return winScoreFirst;
    }

    public String getWinScoreFirstRank() {
        return winScoreFirstRank;
    }

    public String getPrettyLeagueStandings(){
        return prettyLeagueStandings;
    }

    public String getPrettyL10Standings(){
        return prettyL10Standings;
    }
}
