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
        "leaderCategory",
        "leaders"
})
public class LeagueLeader {

    @JsonProperty("leaderCategory")
    private String leaderCategory;
    @JsonProperty("leaders")
    private List<Leader> leaders = new ArrayList<Leader>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public LeagueLeader() {
    }

    /**
     * @param leaders
     * @param leaderCategory
     */
    public LeagueLeader(String leaderCategory, List<Leader> leaders) {
        this.leaderCategory = leaderCategory;
        this.leaders = leaders;
    }

    /**
     * @return The leaderCategory
     */
    @JsonProperty("leaderCategory")
    public String getLeaderCategory() {
        return leaderCategory;
    }

    /**
     * @param leaderCategory The leaderCategory
     */
    @JsonProperty("leaderCategory")
    public void setLeaderCategory(String leaderCategory) {
        this.leaderCategory = leaderCategory;
    }

    public LeagueLeader withLeaderCategory(String leaderCategory) {
        this.leaderCategory = leaderCategory;
        return this;
    }

    /**
     * @return The leaders
     */
    @JsonProperty("leaders")
    public List<Leader> getLeaders() {
        return leaders;
    }

    /**
     * @param leaders The leaders
     */
    @JsonProperty("leaders")
    public void setLeaders(List<Leader> leaders) {
        this.leaders = leaders;
    }

    public LeagueLeader withLeaders(List<Leader> leaders) {
        this.leaders = leaders;
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

    public LeagueLeader withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(leaderCategory).append(leaders).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LeagueLeader) == false) {
            return false;
        }
        LeagueLeader rhs = ((LeagueLeader) other);
        return new EqualsBuilder().append(leaderCategory, rhs.leaderCategory).append(leaders, rhs.leaders).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
