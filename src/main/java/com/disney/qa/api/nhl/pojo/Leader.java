package com.disney.qa.api.nhl.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Leader {

    private Integer rank;

    private String value;

    private Team team;

    private Person person;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leader)) return false;

        Leader leader = (Leader) o;

        // TODO see com.mlb.qa.api.nhl.NhlObjectConverter
//        if (rank != null ? !rank.equals(leader.rank) : leader.rank != null) return false;
        if (value != null ? !value.equals(leader.value) : leader.value != null) return false;
        if (team != null ? !team.equals(leader.team) : leader.team != null) return false;
        return !(person != null ? !person.equals(leader.person) : leader.person != null);

    }

    @Override
    public int hashCode() {
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
