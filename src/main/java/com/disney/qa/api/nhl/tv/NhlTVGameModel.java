package com.disney.qa.api.nhl.tv;

public class NhlTVGameModel {
    private String awayTeam;
    private String homeTeam;
    private String gameStatus;
    private String gameScore;
    private boolean isPlayOff;
    private String gameSeriesWinLossStandings;
    private String playoffSeriesAwayTeamStandings;
    private String playoffSeriesHomeTeamStandings;
    private String playoffSeriesGameCount;

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public boolean isPlayOff() {
        return isPlayOff;
    }

    public void setPlayOff(boolean playOff) {
        isPlayOff = playOff;
    }

    public String getGameSeriesWinLossStandings() {
        return gameSeriesWinLossStandings;
    }

    public void setGameSeriesWinLossStandings(String gameSeriesWinLossStandings) {
        this.gameSeriesWinLossStandings = gameSeriesWinLossStandings;
    }

    public String getPlayoffSeriesAwayTeamStandings() {
        return playoffSeriesAwayTeamStandings;
    }

    public void setPlayoffSeriesAwayTeamStandings(String playoffSeriesAwayTeamStandings) {
        this.playoffSeriesAwayTeamStandings = playoffSeriesAwayTeamStandings;
    }

    public String getPlayoffSeriesHomeTeamStandings() {
        return playoffSeriesHomeTeamStandings;
    }

    public void setPlayoffSeriesHomeTeamStandings(String playoffSeriesHomeTeamStandings) {
        this.playoffSeriesHomeTeamStandings = playoffSeriesHomeTeamStandings;
    }

    public String getPlayoffSeriesGameCount() {
        return playoffSeriesGameCount;
    }

    public void setPlayoffSeriesGameCount(String playoffSeriesGameCount) {
        this.playoffSeriesGameCount = playoffSeriesGameCount;
    }
}
