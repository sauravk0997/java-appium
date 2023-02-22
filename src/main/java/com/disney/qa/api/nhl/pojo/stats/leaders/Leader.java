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
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "rank",
        "value",
        "team",
        "person"
})
public class Leader {

    @JsonProperty("rank")
    private Integer rank;
    @JsonProperty("value")
    private String value;
    @JsonProperty("team")
    private Team team;
    @JsonProperty("person")
    private Person person;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Leader() {
    }

    /**
     * @param person
     * @param rank
     * @param value
     * @param team
     */
    public Leader(Integer rank, String value, Team team, Person person) {
        this.rank = rank;
        this.value = value;
        this.team = team;
        this.person = person;
    }

    /**
     * @return The rank
     */
    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank The rank
     */
    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Leader withRank(Integer rank) {
        this.rank = rank;
        return this;
    }

    /**
     * @return The value
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    public Leader withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * @return The team
     */
    @JsonProperty("team")
    public Team getTeam() {
        return team;
    }

    /**
     * @param team The team
     */
    @JsonProperty("team")
    public void setTeam(Team team) {
        this.team = team;
    }

    public Leader withTeam(Team team) {
        this.team = team;
        return this;
    }

    /**
     * @return The person
     */
    @JsonProperty("person")
    public Person getPerson() {
        return person;
    }

    /**
     * @param person The person
     */
    @JsonProperty("person")
    public void setPerson(Person person) {
        this.person = person;
    }

    public Leader withPerson(Person person) {
        this.person = person;
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

    public Leader withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rank).append(value).append(team).append(person).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leader) == false) {
            return false;
        }
        Leader rhs = ((Leader) other);
        return new EqualsBuilder().append(rank, rhs.rank).append(value, rhs.value).append(team, rhs.team).append(person, rhs.person).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
