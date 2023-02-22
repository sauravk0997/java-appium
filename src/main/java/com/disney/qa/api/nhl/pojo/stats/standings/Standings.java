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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "copyright",
        "records"
})
public class Standings {

    @JsonProperty("copyright")
    private String copyright;
    @JsonProperty("records")
    private List<Record> records = new ArrayList<Record>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Standings() {
    }

    /**
     * @param copyright
     * @param records
     */
    public Standings(String copyright, List<Record> records) {
        this.copyright = copyright;
        this.records = records;
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

    public Standings withCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    /**
     * @return The records
     */
    @JsonProperty("records")
    public List<Record> getRecords() {
        return records;
    }

    /**
     * @param records The records
     */
    @JsonProperty("records")
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Standings withRecords(List<Record> records) {
        this.records = records;
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

    public Standings withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(copyright).append(records).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Standings) == false) {
            return false;
        }
        Standings rhs = ((Standings) other);
        return new EqualsBuilder().append(copyright, rhs.copyright).append(records, rhs.records).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
