package com.disney.qa.api.nhl.pojo.stats.schedule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mk on 11/29/15.
 */
public class LeagueRecord {
    @JsonProperty("wins")
    private Integer wins;
    @JsonProperty("losses")
    private Integer losses;
    @JsonProperty("ot")
    private Integer ot; // overtime loses
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeagueRecord)) return false;

        LeagueRecord that = (LeagueRecord) o;

        if (wins != null ? !wins.equals(that.wins) : that.wins != null) return false;
        if (losses != null ? !losses.equals(that.losses) : that.losses != null) return false;
        if (ot != null ? !ot.equals(that.ot) : that.ot != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(additionalProperties != null ? !additionalProperties.equals(that.additionalProperties) : that.additionalProperties != null);

    }

    @Override
    public int hashCode() {
        int result = wins != null ? wins.hashCode() : 0;
        result = 31 * result + (losses != null ? losses.hashCode() : 0);
        result = 31 * result + (ot != null ? ot.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
