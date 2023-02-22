
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
    "type",
    "rank",
    "isTop"
})
public class Seed {

    @JsonProperty("type")
    private String type;
    @JsonProperty("rank")
    private Integer rank;
    @JsonProperty("isTop")
    private Boolean isTop;

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

    /**
     * 
     * @return
     *     The rank
     */
    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    /**
     * 
     * @param rank
     *     The rank
     */
    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * 
     * @return
     *     The isTop
     */
    @JsonProperty("isTop")
    public Boolean getIsTop() {
        return isTop;
    }

    /**
     * 
     * @param isTop
     *     The isTop
     */
    @JsonProperty("isTop")
    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(type).append(rank).append(isTop).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Seed) == false) {
            return false;
        }
        Seed rhs = ((Seed) other);
        return new EqualsBuilder().append(type, rhs.type).append(rank, rhs.rank).append(isTop, rhs.isTop).isEquals();
    }

}
