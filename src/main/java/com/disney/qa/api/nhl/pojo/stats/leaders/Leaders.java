package com.disney.qa.api.nhl.pojo.stats.leaders;

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
        "leagueLeaders"
})
public class Leaders {

    @JsonProperty("copyright")
    private String copyright;
    // TODO [minor] investigate in which way JSON is parced, because actually 'teamLeaders' property is present, but not 'leagueLeaders'
    @JsonProperty("leagueLeaders")
    private List<LeagueLeader> leagueLeaders = new ArrayList<LeagueLeader>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Leaders() {
    }

    /**
     * @param copyright
     * @param leagueLeaders
     */
    public Leaders(String copyright, List<LeagueLeader> leagueLeaders) {
        this.copyright = copyright;
        this.leagueLeaders = leagueLeaders;
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

    public Leaders withCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    /**
     * @return The leagueLeaders
     */
    @JsonProperty("leagueLeaders")
    public List<LeagueLeader> getLeagueLeaders() {
        return leagueLeaders;
    }

    /**
     * @param leagueLeaders The leagueLeaders
     */
    @JsonProperty("leagueLeaders")
    public void setLeagueLeaders(List<LeagueLeader> leagueLeaders) {
        this.leagueLeaders = leagueLeaders;
    }

    public Leaders withLeagueLeaders(List<LeagueLeader> leagueLeaders) {
        this.leagueLeaders = leagueLeaders;
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

    public Leaders withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(copyright).append(leagueLeaders).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leaders) == false) {
            return false;
        }
        Leaders rhs = ((Leaders) other);
        return new EqualsBuilder().append(copyright, rhs.copyright).append(leagueLeaders, rhs.leagueLeaders).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
