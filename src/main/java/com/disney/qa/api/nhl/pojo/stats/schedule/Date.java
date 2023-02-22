package com.disney.qa.api.nhl.pojo.stats.schedule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "date",
        "totalItems",
        "games"
})
public class Date {

    @JsonProperty("date")
    private String date;
    @JsonProperty("totalItems")
    private Integer totalItems;
    @JsonProperty("games")
    private List<Game> games = new ArrayList<Game>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Date() {
    }

    /**
     * @param games
     * @param totalItems
     * @param date
     */
    public Date(String date, Integer totalItems, List<Game> games) {
        this.date = date;
        this.totalItems = totalItems;
        this.games = games;
    }

    /**
     * @return The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    public Date withDate(String date) {
        this.date = date;
        return this;
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

    public Date withTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    /**
     * @return The games
     */
    @JsonProperty("games")
    public List<Game> getGames() {
        return games;
    }

    /**
     * @param games The games
     */
    @JsonProperty("games")
    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Date withGames(List<Game> games) {
        this.games = games;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Date withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(date).append(totalItems).append(games).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Date) == false) {
            return false;
        }
        Date rhs = ((Date) other);
        return new EqualsBuilder().append(date, rhs.date).append(totalItems, rhs.totalItems).append(games, rhs.games).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}