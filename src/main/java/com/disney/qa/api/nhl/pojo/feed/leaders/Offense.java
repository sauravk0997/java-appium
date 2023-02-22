package com.disney.qa.api.nhl.pojo.feed.leaders;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "goals",
        "assists",
        "plusMinus",
        "points"
})
public class Offense {

    @JsonProperty("goals")
    private List<Leader> goals = new ArrayList<Leader>();
    @JsonProperty("assists")
    private List<Leader> assists = new ArrayList<Leader>();
    @JsonProperty("plusMinus")
    private List<Leader> plusMinus = new ArrayList<Leader>();
    @JsonProperty("points")
    private List<Leader> points = new ArrayList<Leader>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Offense() {
    }

    /**
     * @param goals
     * @param assists
     * @param plusMinus
     * @param points
     */
    public Offense(List<Leader> goals, List<Leader> assists, List<Leader> plusMinus, List<Leader> points) {
        this.goals = goals;
        this.assists = assists;
        this.plusMinus = plusMinus;
        this.points = points;
    }

    /**
     * @return The goals
     */
    @JsonProperty("goals")
    public List<Leader> getGoals() {
        return goals;
    }

    /**
     * @param goals The goals
     */
    @JsonProperty("goals")
    public void setGoals(List<Leader> goals) {
        this.goals = goals;
    }

    public Offense withGoals(List<Leader> goals) {
        this.goals = goals;
        return this;
    }

    /**
     * @return The assists
     */
    @JsonProperty("assists")
    public List<Leader> getAssists() {
        return assists;
    }

    /**
     * @param assists The assists
     */
    @JsonProperty("assists")
    public void setAssists(List<Leader> assists) {
        this.assists = assists;
    }

    public Offense withAssists(List<Leader> assists) {
        this.assists = assists;
        return this;
    }

    /**
     * @return The plusMinus
     */
    @JsonProperty("plusMinus")
    public List<Leader> getPlusMinus() {
        return plusMinus;
    }

    /**
     * @param plusMinus The plusMinus
     */
    @JsonProperty("plusMinus")
    public void setPlusMinus(List<Leader> plusMinus) {
        this.plusMinus = plusMinus;
    }

    public Offense withPlusMinus(List<Leader> plusMinus) {
        this.plusMinus = plusMinus;
        return this;
    }

    /**
     * @return The points
     */
    @JsonProperty("points")
    public List<Leader> getPoints() {
        return points;
    }

    /**
     * @param points The points
     */
    @JsonProperty("points")
    public void setPoints(List<Leader> points) {
        this.points = points;
    }

    public Offense withPoints(List<Leader> leaders) {
        this.points = leaders;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Offense withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(goals).append(assists).append(plusMinus).append(points).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Offense) == false) {
            return false;
        }
        Offense rhs = ((Offense) other);
        return new EqualsBuilder().append(goals, rhs.goals).append(assists, rhs.assists).append(plusMinus, rhs.plusMinus).append(points, rhs.points).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
