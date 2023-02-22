
package com.disney.qa.api.nhl.pojo.stats.schedule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "currentPeriod",
        "currentPeriodOrdinal",
        "currentPeriodTimeRemaining",
        "periods",
        "shootoutInfo",
        "teams",
        "powerPlayStrength",
        "hasShootout"
})
public class Linescore {

    @JsonProperty("currentPeriod")
    private Integer currentPeriod;
    @JsonProperty("currentPeriodOrdinal")
    private String currentPeriodOrdinal;
    @JsonProperty("currentPeriodTimeRemaining")
    private String currentPeriodTimeRemaining;
    @JsonProperty("periods")
    private List<Period> periods = new ArrayList<Period>();
    @JsonProperty("shootoutInfo")
    private ShootoutInfo shootoutInfo;
    @JsonProperty("teams")
    private Teams teams;
    @JsonProperty("powerPlayStrength")
    private String powerPlayStrength;
    @JsonProperty("hasShootout")
    private Boolean hasShootout;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The currentPeriod
     */
    @JsonProperty("currentPeriod")
    public Integer getCurrentPeriod() {
        return currentPeriod;
    }

    /**
     * @param currentPeriod The currentPeriod
     */
    @JsonProperty("currentPeriod")
    public void setCurrentPeriod(Integer currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    /**
     * @return The currentPeriodOrdinal
     */
    @JsonProperty("currentPeriodOrdinal")
    public String getCurrentPeriodOrdinal() {
        return currentPeriodOrdinal;
    }

    /**
     * @param currentPeriodOrdinal The currentPeriodOrdinal
     */
    @JsonProperty("currentPeriodOrdinal")
    public void setCurrentPeriodOrdinal(String currentPeriodOrdinal) {
        this.currentPeriodOrdinal = currentPeriodOrdinal;
    }

    /**
     * @return The currentPeriodTimeRemaining
     */
    @JsonProperty("currentPeriodTimeRemaining")
    public String getCurrentPeriodTimeRemaining() {
        return currentPeriodTimeRemaining;
    }

    /**
     * @param currentPeriodTimeRemaining The currentPeriodTimeRemaining
     */
    @JsonProperty("currentPeriodTimeRemaining")
    public void setCurrentPeriodTimeRemaining(String currentPeriodTimeRemaining) {
        this.currentPeriodTimeRemaining = currentPeriodTimeRemaining;
    }

    /**
     * @return The periods
     */
    @JsonProperty("periods")
    public List<Period> getPeriods() {
        return periods;
    }

    /**
     * @param periods The periods
     */
    @JsonProperty("periods")
    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    /**
     * @return The shootoutInfo
     */
    @JsonProperty("shootoutInfo")
    public ShootoutInfo getShootoutInfo() {
        return shootoutInfo;
    }

    /**
     * @param shootoutInfo The shootoutInfo
     */
    @JsonProperty("shootoutInfo")
    public void setShootoutInfo(ShootoutInfo shootoutInfo) {
        this.shootoutInfo = shootoutInfo;
    }

    /**
     * @return The teams
     */
    @JsonProperty("teams")
    public Teams getTeams() {
        return teams;
    }

    /**
     * @param teams The teams
     */
    @JsonProperty("teams")
    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    /**
     * @return The powerPlayStrength
     */
    @JsonProperty("powerPlayStrength")
    public String getPowerPlayStrength() {
        return powerPlayStrength;
    }

    /**
     * @param powerPlayStrength The powerPlayStrength
     */
    @JsonProperty("powerPlayStrength")
    public void setPowerPlayStrength(String powerPlayStrength) {
        this.powerPlayStrength = powerPlayStrength;
    }

    /**
     * @return The hasShootout
     */
    @JsonProperty("hasShootout")
    public Boolean getHasShootout() {
        return hasShootout;
    }

    /**
     * @param hasShootout The hasShootout
     */
    @JsonProperty("hasShootout")
    public void setHasShootout(Boolean hasShootout) {
        this.hasShootout = hasShootout;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
