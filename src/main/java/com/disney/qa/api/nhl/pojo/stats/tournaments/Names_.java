
package com.disney.qa.api.nhl.pojo.stats.tournaments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "matchupName",
    "matchupShortName",
    "teamAbbreviationA",
    "teamAbbreviationB",
    "seriesSlug"
})
public class Names_ {

    @JsonProperty("matchupName")
    private String matchupName;
    @JsonProperty("matchupShortName")
    private String matchupShortName;
    @JsonProperty("teamAbbreviationA")
    private String teamAbbreviationA;
    @JsonProperty("teamAbbreviationB")
    private String teamAbbreviationB;
    @JsonProperty("seriesSlug")
    private String seriesSlug;

    /**
     * 
     * @return
     *     The matchupName
     */
    @JsonProperty("matchupName")
    public String getMatchupName() {
        return matchupName;
    }

    /**
     * 
     * @param matchupName
     *     The matchupName
     */
    @JsonProperty("matchupName")
    public void setMatchupName(String matchupName) {
        this.matchupName = matchupName;
    }

    /**
     * 
     * @return
     *     The matchupShortName
     */
    @JsonProperty("matchupShortName")
    public String getMatchupShortName() {
        return matchupShortName;
    }

    /**
     * 
     * @param matchupShortName
     *     The matchupShortName
     */
    @JsonProperty("matchupShortName")
    public void setMatchupShortName(String matchupShortName) {
        this.matchupShortName = matchupShortName;
    }

    /**
     * 
     * @return
     *     The teamAbbreviationA
     */
    @JsonProperty("teamAbbreviationA")
    public String getTeamAbbreviationA() {
        return teamAbbreviationA;
    }

    /**
     * 
     * @param teamAbbreviationA
     *     The teamAbbreviationA
     */
    @JsonProperty("teamAbbreviationA")
    public void setTeamAbbreviationA(String teamAbbreviationA) {
        this.teamAbbreviationA = teamAbbreviationA;
    }

    /**
     * 
     * @return
     *     The teamAbbreviationB
     */
    @JsonProperty("teamAbbreviationB")
    public String getTeamAbbreviationB() {
        return teamAbbreviationB;
    }

    /**
     * 
     * @param teamAbbreviationB
     *     The teamAbbreviationB
     */
    @JsonProperty("teamAbbreviationB")
    public void setTeamAbbreviationB(String teamAbbreviationB) {
        this.teamAbbreviationB = teamAbbreviationB;
    }

    /**
     * 
     * @return
     *     The seriesSlug
     */
    @JsonProperty("seriesSlug")
    public String getSeriesSlug() {
        return seriesSlug;
    }

    /**
     * 
     * @param seriesSlug
     *     The seriesSlug
     */
    @JsonProperty("seriesSlug")
    public void setSeriesSlug(String seriesSlug) {
        this.seriesSlug = seriesSlug;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(matchupName).append(matchupShortName).append(teamAbbreviationA).append(teamAbbreviationB).append(seriesSlug).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Names_) == false) {
            return false;
        }
        Names_ rhs = ((Names_) other);
        return new EqualsBuilder().append(matchupName, rhs.matchupName).append(matchupShortName, rhs.matchupShortName).append(teamAbbreviationA, rhs.teamAbbreviationA).append(teamAbbreviationB, rhs.teamAbbreviationB).append(seriesSlug, rhs.seriesSlug).isEquals();
    }

}
