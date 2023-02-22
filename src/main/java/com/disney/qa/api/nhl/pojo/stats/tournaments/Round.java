
package com.disney.qa.api.nhl.pojo.stats.tournaments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "number",
    "code",
    "names",
    "format",
    "series"
})
public class Round {

    @JsonProperty("number")
    private Integer number;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("names")
    private Names names;
    @JsonProperty("format")
    private Format format;
    @JsonProperty("series")
    private List<Series> series = new ArrayList<Series>();

    /**
     * 
     * @return
     *     The number
     */
    @JsonProperty("number")
    public Integer getNumber() {
        return number;
    }

    /**
     * 
     * @param number
     *     The number
     */
    @JsonProperty("number")
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 
     * @return
     *     The code
     */
    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The names
     */
    @JsonProperty("names")
    public Names getNames() {
        return names;
    }

    /**
     * 
     * @param names
     *     The names
     */
    @JsonProperty("names")
    public void setNames(Names names) {
        this.names = names;
    }

    /**
     * 
     * @return
     *     The format
     */
    @JsonProperty("format")
    public Format getFormat() {
        return format;
    }

    /**
     * 
     * @param format
     *     The format
     */
    @JsonProperty("format")
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * 
     * @return
     *     The series
     */
    @JsonProperty("series")
    public List<Series> getSeries() {
        return series;
    }

    /**
     * 
     * @param series
     *     The series
     */
    @JsonProperty("series")
    public void setSeries(List<Series> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(number).append(code).append(names).append(format).append(series).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Round) == false) {
            return false;
        }
        Round rhs = ((Round) other);
        return new EqualsBuilder().append(number, rhs.number).append(code, rhs.code).append(names, rhs.names).append(format, rhs.format).append(series, rhs.series).isEquals();
    }

}
