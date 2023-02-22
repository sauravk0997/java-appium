
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
        "num",
        "ordinalNum",
        "periodType",
        "startTime",
        "endTime",
        "home",
        "away"
})
public class Period {

    @JsonProperty("num")
    private Integer num;
    @JsonProperty("ordinalNum")
    private String ordinalNum;
    @JsonProperty("periodType")
    private String periodType;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("home")
    private HomeAway home;
    @JsonProperty("away")
    private HomeAway away;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The num
     */
    @JsonProperty("num")
    public Integer getNum() {
        return num;
    }

    /**
     * @param num The num
     */
    @JsonProperty("num")
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * @return The ordinalNum
     */
    @JsonProperty("ordinalNum")
    public String getOrdinalNum() {
        return ordinalNum;
    }

    /**
     * @param ordinalNum The ordinalNum
     */
    @JsonProperty("ordinalNum")
    public void setOrdinalNum(String ordinalNum) {
        this.ordinalNum = ordinalNum;
    }

    /**
     * @return The periodType
     */
    @JsonProperty("periodType")
    public String getPeriodType() {
        return periodType;
    }

    /**
     * @param periodType The periodType
     */
    @JsonProperty("periodType")
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    /**
     * @return The startTime
     */
    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime
     */
    @JsonProperty("startTime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The endTime
     */
    @JsonProperty("endTime")
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime
     */
    @JsonProperty("endTime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
