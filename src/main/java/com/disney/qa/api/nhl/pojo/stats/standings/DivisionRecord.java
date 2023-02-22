
package com.disney.qa.api.nhl.pojo.stats.standings;

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
        "wins",
        "losses",
        "ties",
        "ot",
        "type"
})
public class DivisionRecord {

    @JsonProperty("wins")
    private Integer wins;
    @JsonProperty("losses")
    private Integer losses;
    @JsonProperty("ties")
    private Integer ties;
    @JsonProperty("ot")
    private Integer ot;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The wins
     */
    @JsonProperty("wins")
    public Integer getWins() {
        return wins;
    }

    /**
     * 
     * @param wins
     *     The wins
     */
    @JsonProperty("wins")
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    /**
     * 
     * @return
     *     The losses
     */
    @JsonProperty("losses")
    public Integer getLosses() {
        return losses;
    }

    /**
     * 
     * @param losses
     *     The losses
     */
    @JsonProperty("losses")
    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    /**
     * 
     * @return
     *     The ties
     */
    @JsonProperty("ties")
    public Integer getTies() {
        return ties;
    }

    /**
     * 
     * @param ties
     *     The ties
     */
    @JsonProperty("ties")
    public void setTies(Integer ties) {
        this.ties = ties;
    }

    /**
     * 
     * @return
     *     The ot
     */
    @JsonProperty("ot")
    public Integer getOt() {
        return ot;
    }

    /**
     * 
     * @param ot
     *     The ot
     */
    @JsonProperty("ot")
    public void setOt(Integer ot) {
        this.ot = ot;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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
