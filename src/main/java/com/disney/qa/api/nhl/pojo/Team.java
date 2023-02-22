package com.disney.qa.api.nhl.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by mk on 11/29/15.
 */
public class Team {

    private Integer id;

    private String name;

    private Integer score; // 't' property in Feed

    private Integer wins; // 'record' property in Feed, first number: '3-1-0'

    private Integer losses; //'record' property in Feed, second number: '3-1-0'

    private Integer ot; // 'record' property in Feed, third number: '3-1-0'

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getOt() {
        return ot;
    }

    public void setOt(Integer ot) {
        this.ot = ot;
    }

    public Team() {
    }

    public Team(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;

        Team team = (Team) o;

        if (id != null ? !id.equals(team.id) : team.id != null) return false;
        if (name != null ? !name.equals(team.name) : team.name != null) return false;
        if (score != null ? !score.equals(team.score) : team.score != null) return false;
        // disabled as working not proper for following request, looks lie feed response was changed
        // http://www.nhl.com/feed/nhl/league/schedule/expanded/schedule.json?gameType=1,2,3,4,8,6,7,9
        //if (wins != null ? !wins.equals(team.wins) : team.wins != null) return false;
        //if (losses != null ? !losses.equals(team.losses) : team.losses != null) return false;
        return !(ot != null ? !ot.equals(team.ot) : team.ot != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (wins != null ? wins.hashCode() : 0);
        result = 31 * result + (losses != null ? losses.hashCode() : 0);
        result = 31 * result + (ot != null ? ot.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
