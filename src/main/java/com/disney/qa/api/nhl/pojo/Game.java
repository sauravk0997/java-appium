package com.disney.qa.api.nhl.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Game implements Comparable {

    private Integer gameId; // 'gamePk' property in Stats API

    private String date;

    private Team homeTeam;

    private Team awayTeam;
    
    private MetaData metaData;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * 'date' field is not considered so far, as it's different in Feed and in Stats API
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        if (gameId != null ? !gameId.equals(game.gameId) : game.gameId != null) return false;
        if (homeTeam != null ? !homeTeam.equals(game.homeTeam) : game.homeTeam != null) return false;
        return !(awayTeam != null ? !awayTeam.equals(game.awayTeam) : game.awayTeam != null);

    }

    /**
     * 'date' field is not considered so far, as it's different in Feed and in Stats API
     */
    @Override
    public int hashCode() {
        int result = gameId != null ? gameId.hashCode() : 0;
        result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
        result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(gameId, ((Game) o).gameId);
    }
}
