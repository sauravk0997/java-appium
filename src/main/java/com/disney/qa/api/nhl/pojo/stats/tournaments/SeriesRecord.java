
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
    "wins",
    "losses"
})
public class SeriesRecord {

    @JsonProperty("wins")
    private Integer wins;
    @JsonProperty("losses")
    private Integer losses;

    /**
     * 
     * @return
     *     The wins
     */
    @JsonProperty("wins")
    public Integer getWins() {
        return wins;
    }

    /**
     * 
     * @param wins
     *     The wins
     */
    @JsonProperty("wins")
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    /**
     * 
     * @return
     *     The losses
     */
    @JsonProperty("losses")
    public Integer getLosses() {
        return losses;
    }

    /**
     * 
     * @param losses
     *     The losses
     */
    @JsonProperty("losses")
    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(wins).append(losses).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SeriesRecord) == false) {
            return false;
        }
        SeriesRecord rhs = ((SeriesRecord) other);
        return new EqualsBuilder().append(wins, rhs.wins).append(losses, rhs.losses).isEquals();
    }

}
