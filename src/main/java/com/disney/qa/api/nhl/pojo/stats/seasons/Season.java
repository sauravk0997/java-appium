
package com.disney.qa.api.nhl.pojo.stats.seasons;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "seasonId",
        "regularSeasonStartDate",
        "regularSeasonEndDate",
        "seasonEndDate",
        "numberOfGames",
        "tiesInUse",
        "olympicsParticipation",
        "conferencesInUse",
        "divisionsInUse",
        "wildCardInUse"
})
public class Season {

    @JsonProperty("seasonId")
    private String seasonId;
    @JsonProperty("regularSeasonStartDate")
    private String regularSeasonStartDate;
    @JsonProperty("regularSeasonEndDate")
    private String regularSeasonEndDate;
    @JsonProperty("seasonEndDate")
    private String seasonEndDate;
    @JsonProperty("numberOfGames")
    private Integer numberOfGames;
    @JsonProperty("tiesInUse")
    private Boolean tiesInUse;
    @JsonProperty("olympicsParticipation")
    private Boolean olympicsParticipation;
    @JsonProperty("conferencesInUse")
    private Boolean conferencesInUse;
    @JsonProperty("divisionsInUse")
    private Boolean divisionsInUse;
    @JsonProperty("wildCardInUse")
    private Boolean wildCardInUse;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The seasonId
     */
    @JsonProperty("seasonId")
    public String getSeasonId() {
        return seasonId;
    }

    /**
     * @param seasonId The seasonId
     */
    @JsonProperty("seasonId")
    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    /**
     * @return The regularSeasonStartDate
     */
    @JsonProperty("regularSeasonStartDate")
    public String getRegularSeasonStartDate() {
        return regularSeasonStartDate;
    }

    /**
     * @param regularSeasonStartDate The regularSeasonStartDate
     */
    @JsonProperty("regularSeasonStartDate")
    public void setRegularSeasonStartDate(String regularSeasonStartDate) {
        this.regularSeasonStartDate = regularSeasonStartDate;
    }

    /**
     * @return The regularSeasonEndDate
     */
    @JsonProperty("regularSeasonEndDate")
    public String getRegularSeasonEndDate() {
        return regularSeasonEndDate;
    }

    /**
     * @param regularSeasonEndDate The regularSeasonEndDate
     */
    @JsonProperty("regularSeasonEndDate")
    public void setRegularSeasonEndDate(String regularSeasonEndDate) {
        this.regularSeasonEndDate = regularSeasonEndDate;
    }

    /**
     * @return The seasonEndDate
     */
    @JsonProperty("seasonEndDate")
    public String getSeasonEndDate() {
        return seasonEndDate;
    }

    /**
     * @param seasonEndDate The seasonEndDate
     */
    @JsonProperty("seasonEndDate")
    public void setSeasonEndDate(String seasonEndDate) {
        this.seasonEndDate = seasonEndDate;
    }

    /**
     * @return The numberOfGames
     */
    @JsonProperty("numberOfGames")
    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    /**
     * @param numberOfGames The numberOfGames
     */
    @JsonProperty("numberOfGames")
    public void setNumberOfGames(Integer numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    /**
     * @return The tiesInUse
     */
    @JsonProperty("tiesInUse")
    public Boolean getTiesInUse() {
        return tiesInUse;
    }

    /**
     * @param tiesInUse The tiesInUse
     */
    @JsonProperty("tiesInUse")
    public void setTiesInUse(Boolean tiesInUse) {
        this.tiesInUse = tiesInUse;
    }

    /**
     * @return The olympicsParticipation
     */
    @JsonProperty("olympicsParticipation")
    public Boolean getOlympicsParticipation() {
        return olympicsParticipation;
    }

    /**
     * @param olympicsParticipation The olympicsParticipation
     */
    @JsonProperty("olympicsParticipation")
    public void setOlympicsParticipation(Boolean olympicsParticipation) {
        this.olympicsParticipation = olympicsParticipation;
    }

    /**
     * @return The conferencesInUse
     */
    @JsonProperty("conferencesInUse")
    public Boolean getConferencesInUse() {
        return conferencesInUse;
    }

    /**
     * @param conferencesInUse The conferencesInUse
     */
    @JsonProperty("conferencesInUse")
    public void setConferencesInUse(Boolean conferencesInUse) {
        this.conferencesInUse = conferencesInUse;
    }

    /**
     * @return The divisionsInUse
     */
    @JsonProperty("divisionsInUse")
    public Boolean getDivisionsInUse() {
        return divisionsInUse;
    }

    /**
     * @param divisionsInUse The divisionsInUse
     */
    @JsonProperty("divisionsInUse")
    public void setDivisionsInUse(Boolean divisionsInUse) {
        this.divisionsInUse = divisionsInUse;
    }

    /**
     * @return The wildCardInUse
     */
    @JsonProperty("wildCardInUse")
    public Boolean getWildCardInUse() {
        return wildCardInUse;
    }

    /**
     * @param wildCardInUse The wildCardInUse
     */
    @JsonProperty("wildCardInUse")
    public void setWildCardInUse(Boolean wildCardInUse) {
        this.wildCardInUse = wildCardInUse;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Season{" +
                "seasonId='" + seasonId + '\'' +
                ", tiesInUse=" + tiesInUse +
                '}';
    }
}
