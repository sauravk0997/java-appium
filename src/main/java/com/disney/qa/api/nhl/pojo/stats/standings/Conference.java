package com.disney.qa.api.nhl.pojo.stats.standings;

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
        "id",
        "name",
        "link",
        "abbreviation",
        "shortName",
        "active"
})
public class Conference {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("link")
    private String link;
    @JsonProperty("abbreviation")
    private String abbreviation;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("active")
    private Boolean active;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Conference() {
    }

    /**
     * @param id
     * @param link
     * @param name
     * @param active
     * @param shortName
     * @param abbreviation
     */
    public Conference(Integer id, String name, String link, String abbreviation, String shortName, Boolean active) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.abbreviation = abbreviation;
        this.shortName = shortName;
        this.active = active;
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Conference withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Conference withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The link
     */
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    public Conference withLink(String link) {
        this.link = link;
        return this;
    }

    /**
     * @return The abbreviation
     */
    @JsonProperty("abbreviation")
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation The abbreviation
     */
    @JsonProperty("abbreviation")
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Conference withAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    /**
     * @return The shortName
     */
    @JsonProperty("shortName")
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName The shortName
     */
    @JsonProperty("shortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Conference withShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    /**
     * @return The active
     */
    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active The active
     */
    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Conference withActive(Boolean active) {
        this.active = active;
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

    public Conference withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(link).append(abbreviation).append(shortName).append(active).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Conference) == false) {
            return false;
        }
        Conference rhs = ((Conference) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(link, rhs.link).append(abbreviation, rhs.abbreviation).append(shortName, rhs.shortName).append(active, rhs.active).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
