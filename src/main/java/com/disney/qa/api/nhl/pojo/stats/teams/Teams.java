package com.disney.qa.api.nhl.pojo.stats.teams;

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
        "copyright",
        "teams"
})
public class Teams {

    @JsonProperty("copyright")
    private String copyright;
    @JsonProperty("teams")
    private List<Team> teams = new ArrayList<Team>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Teams() {
    }

    /**
     * @param teams
     * @param copyright
     */
    public Teams(String copyright, List<Team> teams) {
        this.copyright = copyright;
        this.teams = teams;
    }

    /**
     * @return The copyright
     */
    @JsonProperty("copyright")
    public String getCopyright() {
        return copyright;
    }

    /**
     * @param copyright The copyright
     */
    @JsonProperty("copyright")
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Teams withCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    /**
     * @return The teams
     */
    @JsonProperty("teams")
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * @param teams The teams
     */
    @JsonProperty("teams")
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Teams withTeams(List<Team> teams) {
        this.teams = teams;
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

    public Teams withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(copyright).append(teams).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Teams) == false) {
            return false;
        }
        Teams rhs = ((Teams) other);
        return new EqualsBuilder().append(copyright, rhs.copyright).append(teams, rhs.teams).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
