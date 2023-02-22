
package com.disney.qa.api.nhl.pojo.stats.tournaments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "numberOfGames",
    "numberOfWins"
})
public class Format {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("numberOfGames")
    private Integer numberOfGames;
    @JsonProperty("numberOfWins")
    private Integer numberOfWins;

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The numberOfGames
     */
    @JsonProperty("numberOfGames")
    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    /**
     * 
     * @param numberOfGames
     *     The numberOfGames
     */
    @JsonProperty("numberOfGames")
    public void setNumberOfGames(Integer numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    /**
     * 
     * @return
     *     The numberOfWins
     */
    @JsonProperty("numberOfWins")
    public Integer getNumberOfWins() {
        return numberOfWins;
    }

    /**
     * 
     * @param numberOfWins
     *     The numberOfWins
     */
    @JsonProperty("numberOfWins")
    public void setNumberOfWins(Integer numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(description).append(numberOfGames).append(numberOfWins).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Format) == false) {
            return false;
        }
        Format rhs = ((Format) other);
        return new EqualsBuilder().append(name, rhs.name).append(description, rhs.description).append(numberOfGames, rhs.numberOfGames).append(numberOfWins, rhs.numberOfWins).isEquals();
    }

}
