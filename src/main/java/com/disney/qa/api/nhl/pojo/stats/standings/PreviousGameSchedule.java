
package com.disney.qa.api.nhl.pojo.stats.standings;

import com.disney.qa.api.nhl.pojo.stats.schedule.Date;
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
        "totalItems",
        "dates"
})
public class PreviousGameSchedule {

    @JsonProperty("totalItems")
    private Integer totalItems;
    @JsonProperty("dates")
    private List<Date> dates = new ArrayList<Date>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public PreviousGameSchedule() {
    }

    /**
     * @param dates
     * @param totalItems
     */
    public PreviousGameSchedule(Integer totalItems, List<Date> dates) {
        this.totalItems = totalItems;
        this.dates = dates;
    }

    /**
     * @return The totalItems
     */
    @JsonProperty("totalItems")
    public Integer getTotalItems() {
        return totalItems;
    }

    /**
     * @param totalItems The totalItems
     */
    @JsonProperty("totalItems")
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public PreviousGameSchedule withTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    /**
     * @return The dates
     */
    @JsonProperty("dates")
    public List<Date> getDates() {
        return dates;
    }

    /**
     * @param dates The dates
     */
    @JsonProperty("dates")
    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public PreviousGameSchedule withDates(List<Date> dates) {
        this.dates = dates;
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

    public PreviousGameSchedule withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalItems).append(dates).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PreviousGameSchedule) == false) {
            return false;
        }
        PreviousGameSchedule rhs = ((PreviousGameSchedule) other);
        return new EqualsBuilder().append(totalItems, rhs.totalItems).append(dates, rhs.dates).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
