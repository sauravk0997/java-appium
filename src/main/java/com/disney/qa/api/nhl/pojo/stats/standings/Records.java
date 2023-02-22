
package com.disney.qa.api.nhl.pojo.stats.standings;

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
        "divisionRecords",
        "overallRecords"
})
public class Records {

    @JsonProperty("divisionRecords")
    private List<DivisionRecord> divisionRecords = new ArrayList<DivisionRecord>();
    @JsonProperty("overallRecords")
    private List<OverallRecord> overallRecords = new ArrayList<OverallRecord>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The divisionRecords
     */
    @JsonProperty("divisionRecords")
    public List<DivisionRecord> getDivisionRecords() {
        return divisionRecords;
    }

    /**
     * 
     * @param divisionRecords
     *     The divisionRecords
     */
    @JsonProperty("divisionRecords")
    public void setDivisionRecords(List<DivisionRecord> divisionRecords) {
        this.divisionRecords = divisionRecords;
    }

    /**
     * 
     * @return
     *     The overallRecords
     */
    @JsonProperty("overallRecords")
    public List<OverallRecord> getOverallRecords() {
        return overallRecords;
    }

    /**
     * 
     * @param overallRecords
     *     The overallRecords
     */
    @JsonProperty("overallRecords")
    public void setOverallRecords(List<OverallRecord> overallRecords) {
        this.overallRecords = overallRecords;
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
