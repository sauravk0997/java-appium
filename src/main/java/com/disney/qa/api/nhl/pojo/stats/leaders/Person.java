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
        "id",
        "fullName",
        "link"
})
public class Person {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("link")
    private String link;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Person() {
    }

    /**
     * @param id
     * @param link
     * @param fullName
     */
    public Person(Integer id, String fullName, String link) {
        this.id = id;
        this.fullName = fullName;
        this.link = link;
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

    public Person withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * @return The fullName
     */
    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName The fullName
     */
    @JsonProperty("fullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Person withFullName(String fullName) {
        this.fullName = fullName;
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

    public Person withLink(String link) {
        this.link = link;
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

    public Person withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(fullName).append(link).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Person) == false) {
            return false;
        }
        Person rhs = ((Person) other);
        return new EqualsBuilder().append(id, rhs.id).append(fullName, rhs.fullName).append(link, rhs.link).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
