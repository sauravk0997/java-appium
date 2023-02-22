
package com.disney.qa.api.nhl.pojo.stats.tournaments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "copyright",
    "name",
    "season",
    "defaultRound",
    "rounds"
})
public class Tournaments {

    @JsonProperty("copyright")
    private String copyright;
    @JsonProperty("name")
    private String name;
    @JsonProperty("season")
    private String season;
    @JsonProperty("defaultRound")
    private Integer defaultRound;
    @JsonProperty("rounds")
    private List<Round> rounds = new ArrayList<Round>();

    /**
     * 
     * @return
     *     The copyright
     */
    @JsonProperty("copyright")
    public String getCopyright() {
        return copyright;
    }

    /**
     * 
     * @param copyright
     *     The copyright
     */
    @JsonProperty("copyright")
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The season
     */
    @JsonProperty("season")
    public String getSeason() {
        return season;
    }

    /**
     * 
     * @param season
     *     The season
     */
    @JsonProperty("season")
    public void setSeason(String season) {
        this.season = season;
    }

    /**
     * 
     * @return
     *     The defaultRound
     */
    @JsonProperty("defaultRound")
    public Integer getDefaultRound() {
        return defaultRound;
    }

    /**
     * 
     * @param defaultRound
     *     The defaultRound
     */
    @JsonProperty("defaultRound")
    public void setDefaultRound(Integer defaultRound) {
        this.defaultRound = defaultRound;
    }

    /**
     * 
     * @return
     *     The rounds
     */
    @JsonProperty("rounds")
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * 
     * @param rounds
     *     The rounds
     */
    @JsonProperty("rounds")
    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(copyright).append(name).append(season).append(defaultRound).append(rounds).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tournaments) == false) {
            return false;
        }
        Tournaments rhs = ((Tournaments) other);
        return new EqualsBuilder().append(copyright, rhs.copyright).append(name, rhs.name).append(season, rhs.season).append(defaultRound, rhs.defaultRound).append(rounds, rhs.rounds).isEquals();
    }

}
