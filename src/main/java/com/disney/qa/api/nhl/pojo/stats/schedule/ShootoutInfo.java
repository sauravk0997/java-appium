
package com.disney.qa.api.nhl.pojo.stats.schedule;

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
        "away",
        "home"
})
public class ShootoutInfo {

    @JsonProperty("away")
    private HomeAway away;
    @JsonProperty("home")
    private HomeAway home;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The away
     */
    @JsonProperty("away")
    public HomeAway getAway() {
        return away;
    }

    /**
     * @param away The away
     */
    @JsonProperty("away")
    public void setAway(HomeAway away) {
        this.away = away;
    }

    /**
     * @return The home
     */
    @JsonProperty("home")
    public HomeAway getHome() {
        return home;
    }

    /**
     * @param home The home
     */
    @JsonProperty("home")
    public void setHome(HomeAway home) {
        this.home = home;
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
