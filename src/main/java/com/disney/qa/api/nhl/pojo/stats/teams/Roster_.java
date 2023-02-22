
package com.disney.qa.api.nhl.pojo.stats.teams;

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
        "person",
        "position"
})
public class Roster_ {

    @JsonProperty("person")
    private Person person;
    @JsonProperty("position")
    private Position position;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The person
     */
    @JsonProperty("person")
    public Person getPerson() {
        return person;
    }

    /**
     * 
     * @param person
     *     The person
     */
    @JsonProperty("person")
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * 
     * @return
     *     The position
     */
    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    /**
     * 
     * @param position
     *     The position
     */
    @JsonProperty("position")
    public void setPosition(Position position) {
        this.position = position;
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
