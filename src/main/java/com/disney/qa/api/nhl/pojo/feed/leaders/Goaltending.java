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
        "savePct",
        "shutouts",
        "wins",
        "gaa"
})
public class Goaltending {

    @JsonProperty("savePct")
    private List<Leader> savePct = new ArrayList<Leader>();
    @JsonProperty("shutouts")
    private List<Leader> shutouts = new ArrayList<Leader>();
    @JsonProperty("wins")
    private List<Leader> wins = new ArrayList<Leader>();
    @JsonProperty("gaa")
    private List<Leader> gaa = new ArrayList<Leader>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Goaltending() {
    }

    /**
     * @param savePct
     * @param shutouts
     * @param gaa
     * @param wins
     */
    public Goaltending(List<Leader> savePct, List<Leader> shutouts, List<Leader> wins, List<Leader> gaa) {
        this.savePct = savePct;
        this.shutouts = shutouts;
        this.wins = wins;
        this.gaa = gaa;
    }

    /**
     * @return The savePct
     */
    @JsonProperty("savePct")
    public List<Leader> getSavePct() {
        return savePct;
    }

    /**
     * @param savePct The savePct
     */
    @JsonProperty("savePct")
    public void setSavePct(List<Leader> savePct) {
        this.savePct = savePct;
    }

    public Goaltending withSavePct(List<Leader> savePct) {
        this.savePct = savePct;
        return this;
    }

    /**
     * @return The shutouts
     */
    @JsonProperty("shutouts")
    public List<Leader> getShutouts() {
        return shutouts;
    }

    /**
     * @param shutouts The shutouts
     */
    @JsonProperty("shutouts")
    public void setShutouts(List<Leader> shutouts) {
        this.shutouts = shutouts;
    }

    public Goaltending withShutouts(List<Leader> shutouts) {
        this.shutouts = shutouts;
        return this;
    }

    /**
     * @return The wins
     */
    @JsonProperty("wins")
    public List<Leader> getWins() {
        return wins;
    }

    /**
     * @param wins The wins
     */
    @JsonProperty("wins")
    public void setWins(List<Leader> wins) {
        this.wins = wins;
    }

    public Goaltending withWins(List<Leader> wins) {
        this.wins = wins;
        return this;
    }

    /**
     * @return The gaa
     */
    @JsonProperty("gaa")
    public List<Leader> getGaa() {
        return gaa;
    }

    /**
     * @param gaa The gaa
     */
    @JsonProperty("gaa")
    public void setGaa(List<Leader> gaa) {
        this.gaa = gaa;
    }

    public Goaltending withGaa(List<Leader> gaa) {
        this.gaa = gaa;
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

    public Goaltending withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(savePct).append(shutouts).append(wins).append(gaa).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Goaltending) == false) {
            return false;
        }
        Goaltending rhs = ((Goaltending) other);
        return new EqualsBuilder().append(savePct, rhs.savePct).append(shutouts, rhs.shutouts).append(wins, rhs.wins).append(gaa, rhs.gaa).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
