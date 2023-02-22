
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
    "seriesSummary"
})
public class CurrentGame {

    @JsonProperty("seriesSummary")
    private SeriesSummary seriesSummary;

    /**
     * 
     * @return
     *     The seriesSummary
     */
    @JsonProperty("seriesSummary")
    public SeriesSummary getSeriesSummary() {
        return seriesSummary;
    }

    /**
     * 
     * @param seriesSummary
     *     The seriesSummary
     */
    @JsonProperty("seriesSummary")
    public void setSeriesSummary(SeriesSummary seriesSummary) {
        this.seriesSummary = seriesSummary;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(seriesSummary).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CurrentGame) == false) {
            return false;
        }
        CurrentGame rhs = ((CurrentGame) other);
        return new EqualsBuilder().append(seriesSummary, rhs.seriesSummary).isEquals();
    }

}
