package com.disney.qa.api.nhl.pojo.feed;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "ctUrl",
        "attUrl",
        "cmtUrl",
        "bstUrl",
        "cbstmUrl",
        "cbstUrl",
        "amtUrl",
        "mtUrl",
        "tUrl",
        "mbstUrl"
})
public class Tickets {

    @JsonProperty("ctUrl")
    private String ctUrl;
    @JsonProperty("attUrl")
    private String attUrl;
    @JsonProperty("cmtUrl")
    private String cmtUrl;
    @JsonProperty("bstUrl")
    private String bstUrl;
    @JsonProperty("cbstmUrl")
    private String cbstmUrl;
    @JsonProperty("cbstUrl")
    private String cbstUrl;
    @JsonProperty("amtUrl")
    private String amtUrl;
    @JsonProperty("mtUrl")
    private String mtUrl;
    @JsonProperty("tUrl")
    private String tUrl;
    @JsonProperty("mbstUrl")
    private String mbstUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Tickets() {
    }

    /**
     * @param attUrl
     * @param ctUrl
     * @param cmtUrl
     * @param bstUrl
     * @param cbstmUrl
     * @param cbstUrl
     * @param tUrl
     * @param mtUrl
     * @param amtUrl
     * @param mbstUrl
     */
    public Tickets(String ctUrl, String attUrl, String cmtUrl, String bstUrl, String cbstmUrl, String cbstUrl, String amtUrl, String mtUrl, String tUrl, String mbstUrl) {
        this.ctUrl = ctUrl;
        this.attUrl = attUrl;
        this.cmtUrl = cmtUrl;
        this.bstUrl = bstUrl;
        this.cbstmUrl = cbstmUrl;
        this.cbstUrl = cbstUrl;
        this.amtUrl = amtUrl;
        this.mtUrl = mtUrl;
        this.tUrl = tUrl;
        this.mbstUrl = mbstUrl;
    }

    /**
     * @return The ctUrl
     */
    @JsonProperty("ctUrl")
    public String getCtUrl() {
        return ctUrl;
    }

    /**
     * @param ctUrl The ctUrl
     */
    @JsonProperty("ctUrl")
    public void setCtUrl(String ctUrl) {
        this.ctUrl = ctUrl;
    }

    public Tickets withCtUrl(String ctUrl) {
        this.ctUrl = ctUrl;
        return this;
    }

    /**
     * @return The attUrl
     */
    @JsonProperty("attUrl")
    public String getAttUrl() {
        return attUrl;
    }

    /**
     * @param attUrl The attUrl
     */
    @JsonProperty("attUrl")
    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    public Tickets withAttUrl(String attUrl) {
        this.attUrl = attUrl;
        return this;
    }

    /**
     * @return The cmtUrl
     */
    @JsonProperty("cmtUrl")
    public String getCmtUrl() {
        return cmtUrl;
    }

    /**
     * @param cmtUrl The cmtUrl
     */
    @JsonProperty("cmtUrl")
    public void setCmtUrl(String cmtUrl) {
        this.cmtUrl = cmtUrl;
    }

    public Tickets withCmtUrl(String cmtUrl) {
        this.cmtUrl = cmtUrl;
        return this;
    }

    /**
     * @return The bstUrl
     */
    @JsonProperty("bstUrl")
    public String getBstUrl() {
        return bstUrl;
    }

    /**
     * @param bstUrl The bstUrl
     */
    @JsonProperty("bstUrl")
    public void setBstUrl(String bstUrl) {
        this.bstUrl = bstUrl;
    }

    public Tickets withBstUrl(String bstUrl) {
        this.bstUrl = bstUrl;
        return this;
    }

    /**
     * @return The cbstmUrl
     */
    @JsonProperty("cbstmUrl")
    public String getCbstmUrl() {
        return cbstmUrl;
    }

    /**
     * @param cbstmUrl The cbstmUrl
     */
    @JsonProperty("cbstmUrl")
    public void setCbstmUrl(String cbstmUrl) {
        this.cbstmUrl = cbstmUrl;
    }

    public Tickets withCbstmUrl(String cbstmUrl) {
        this.cbstmUrl = cbstmUrl;
        return this;
    }

    /**
     * @return The cbstUrl
     */
    @JsonProperty("cbstUrl")
    public String getCbstUrl() {
        return cbstUrl;
    }

    /**
     * @param cbstUrl The cbstUrl
     */
    @JsonProperty("cbstUrl")
    public void setCbstUrl(String cbstUrl) {
        this.cbstUrl = cbstUrl;
    }

    public Tickets withCbstUrl(String cbstUrl) {
        this.cbstUrl = cbstUrl;
        return this;
    }

    /**
     * @return The amtUrl
     */
    @JsonProperty("amtUrl")
    public String getAmtUrl() {
        return amtUrl;
    }

    /**
     * @param amtUrl The amtUrl
     */
    @JsonProperty("amtUrl")
    public void setAmtUrl(String amtUrl) {
        this.amtUrl = amtUrl;
    }

    public Tickets withAmtUrl(String amtUrl) {
        this.amtUrl = amtUrl;
        return this;
    }

    /**
     * @return The mtUrl
     */
    @JsonProperty("mtUrl")
    public String getMtUrl() {
        return mtUrl;
    }

    /**
     * @param mtUrl The mtUrl
     */
    @JsonProperty("mtUrl")
    public void setMtUrl(String mtUrl) {
        this.mtUrl = mtUrl;
    }

    public Tickets withMtUrl(String mtUrl) {
        this.mtUrl = mtUrl;
        return this;
    }

    /**
     * @return The tUrl
     */
    @JsonProperty("tUrl")
    public String getTUrl() {
        return tUrl;
    }

    /**
     * @param tUrl The tUrl
     */
    @JsonProperty("tUrl")
    public void setTUrl(String tUrl) {
        this.tUrl = tUrl;
    }

    public Tickets withTUrl(String tUrl) {
        this.tUrl = tUrl;
        return this;
    }

    /**
     * @return The mbstUrl
     */
    @JsonProperty("mbstUrl")
    public String getMbstUrl() {
        return mbstUrl;
    }

    /**
     * @param mbstUrl The mbstUrl
     */
    @JsonProperty("mbstUrl")
    public void setMbstUrl(String mbstUrl) {
        this.mbstUrl = mbstUrl;
    }

    public Tickets withMbstUrl(String mbstUrl) {
        this.mbstUrl = mbstUrl;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Tickets withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ctUrl).append(attUrl).append(cmtUrl).append(bstUrl).append(cbstmUrl).append(cbstUrl).append(amtUrl).append(mtUrl).append(tUrl).append(mbstUrl).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tickets) == false) {
            return false;
        }
        Tickets rhs = ((Tickets) other);
        return new EqualsBuilder().append(ctUrl, rhs.ctUrl).append(attUrl, rhs.attUrl).append(cmtUrl, rhs.cmtUrl).append(bstUrl, rhs.bstUrl).append(cbstmUrl, rhs.cbstmUrl).append(cbstUrl, rhs.cbstUrl).append(amtUrl, rhs.amtUrl).append(mtUrl, rhs.mtUrl).append(tUrl, rhs.tUrl).append(mbstUrl, rhs.mbstUrl).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}