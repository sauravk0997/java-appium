
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
    "team",
    "seed",
    "seriesRecord"
})
public class MatchupTeam {

    @JsonProperty("team")
    private Team team;
    @JsonProperty("seed")
    private Seed seed;
    @JsonProperty("seriesRecord")
    private SeriesRecord seriesRecord;

    /**
     * 
     * @return
     *     The team
     */
    @JsonProperty("team")
    public Team getTeam() {
        return team;
    }

    /**
     * 
     * @param team
     *     The team
     */
    @JsonProperty("team")
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * 
     * @return
     *     The seed
     */
    @JsonProperty("seed")
    public Seed getSeed() {
        return seed;
    }

    /**
     * 
     * @param seed
     *     The seed
     */
    @JsonProperty("seed")
    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    /**
     * 
     * @return
     *     The seriesRecord
     */
    @JsonProperty("seriesRecord")
    public SeriesRecord getSeriesRecord() {
        return seriesRecord;
    }

    /**
     * 
     * @param seriesRecord
     *     The seriesRecord
     */
    @JsonProperty("seriesRecord")
    public void setSeriesRecord(SeriesRecord seriesRecord) {
        this.seriesRecord = seriesRecord;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(team).append(seed).append(seriesRecord).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MatchupTeam) == false) {
            return false;
        }
        MatchupTeam rhs = ((MatchupTeam) other);
        return new EqualsBuilder().append(team, rhs.team).append(seed, rhs.seed).append(seriesRecord, rhs.seriesRecord).isEquals();
    }

}
