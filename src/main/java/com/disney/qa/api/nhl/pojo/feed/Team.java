package com.disney.qa.api.nhl.pojo.feed;

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
        "record",
        "t",
        "name",
        "fId",
        "ab"
})
public class Team {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("record")
    private String record;
    @JsonProperty("t")
    private Integer t;
    @JsonProperty("name")
    private String name;
    @JsonProperty("fId")
    private Integer fId;
    @JsonProperty("ab")
    private String ab;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Team() {
    }

    /**
     * @param id
     * @param record
     * @param t
     * @param name
     * @param fId
     * @param ab
     */
    public Team(Integer id, String record, Integer t, String name, Integer fId, String ab) {
        this.id = id;
        this.record = record;
        this.t = t;
        this.name = name;
        this.fId = fId;
        this.ab = ab;
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

    public Team withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * @return The record
     */
    @JsonProperty("record")
    public String getRecord() {
        return record;
    }

    /**
     * @param record The record
     */
    @JsonProperty("record")
    public void setRecord(String record) {
        this.record = record;
    }

    public Team withRecord(String record) {
        this.record = record;
        return this;
    }

    /**
     * @return The t
     */
    @JsonProperty("t")
    public Integer getT() {
        return t;
    }

    /**
     * @param t The t
     */
    @JsonProperty("t")
    public void setT(Integer t) {
        this.t = t;
    }

    public Team withT(Integer t) {
        this.t = t;
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

    public Team withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The fId
     */
    @JsonProperty("fId")
    public Integer getFId() {
        return fId;
    }

    /**
     * @param fId The fId
     */
    @JsonProperty("fId")
    public void setFId(Integer fId) {
        this.fId = fId;
    }

    public Team withFId(Integer fId) {
        this.fId = fId;
        return this;
    }

    /**
     * @return The ab
     */
    @JsonProperty("ab")
    public String getAb() {
        return ab;
    }

    /**
     * @param ab The ab
     */
    @JsonProperty("ab")
    public void setAb(String ab) {
        this.ab = ab;
    }

    public Team withAb(String ab) {
        this.ab = ab;
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

    public Team withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(record).append(t).append(name).append(fId).append(ab).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Team) == false) {
            return false;
        }
        Team rhs = ((Team) other);
        return new EqualsBuilder().append(id, rhs.id).append(record, rhs.record).append(t, rhs.t).append(name, rhs.name).append(fId, rhs.fId).append(ab, rhs.ab).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}