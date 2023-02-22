package com.disney.qa.api.nhl.pojo.feed.leaders;

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
        "fid",
        "teamName",
        "sweater",
        "headshot",
        "name",
        "pid",
        "team",
        "teamId",
        "stat",
        "pos",
        "country"
})
public class Leader {

    @JsonProperty("fid")
    private Integer fid;
    @JsonProperty("teamName")
    private String teamName;
    @JsonProperty("sweater")
    private Integer sweater;
    @JsonProperty("headshot")
    private String headshot;
    @JsonProperty("name")
    private String name;
    @JsonProperty("pid")
    private Integer pid;
    @JsonProperty("team")
    private String team;
    @JsonProperty("teamId")
    private Integer teamId;
    @JsonProperty("stat")
    private String stat;
    @JsonProperty("pos")
    private String pos;
    @JsonProperty("country")
    private String country;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Leader() {
    }

    /**
     * @param teamName
     * @param fid
     * @param sweater
     * @param headshot
     * @param name
     * @param pid
     * @param team
     * @param teamId
     * @param stat
     * @param country
     * @param pos
     */
    public Leader(Integer fid, String teamName, Integer sweater, String headshot, String name, Integer pid, String team, Integer teamId, String stat, String pos, String country) {
        this.fid = fid;
        this.teamName = teamName;
        this.sweater = sweater;
        this.headshot = headshot;
        this.name = name;
        this.pid = pid;
        this.team = team;
        this.teamId = teamId;
        this.stat = stat;
        this.pos = pos;
        this.country = country;
    }

    /**
     * @return The fid
     */
    @JsonProperty("fid")
    public Integer getFid() {
        return fid;
    }

    /**
     * @param fid The fid
     */
    @JsonProperty("fid")
    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Leader withFid(Integer fid) {
        this.fid = fid;
        return this;
    }

    /**
     * @return The teamName
     */
    @JsonProperty("teamName")
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName The teamName
     */
    @JsonProperty("teamName")
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Leader withTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    /**
     * @return The sweater
     */
    @JsonProperty("sweater")
    public Integer getSweater() {
        return sweater;
    }

    /**
     * @param sweater The sweater
     */
    @JsonProperty("sweater")
    public void setSweater(Integer sweater) {
        this.sweater = sweater;
    }

    public Leader withSweater(Integer sweater) {
        this.sweater = sweater;
        return this;
    }

    /**
     * @return The headshot
     */
    @JsonProperty("headshot")
    public String getHeadshot() {
        return headshot;
    }

    /**
     * @param headshot The headshot
     */
    @JsonProperty("headshot")
    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public Leader withHeadshot(String headshot) {
        this.headshot = headshot;
        return this;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Leader withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The pid
     */
    @JsonProperty("pid")
    public Integer getPid() {
        return pid;
    }

    /**
     * @param pid The pid
     */
    @JsonProperty("pid")
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Leader withPid(Integer pid) {
        this.pid = pid;
        return this;
    }

    /**
     * @return The team
     */
    @JsonProperty("team")
    public String getTeam() {
        return team;
    }

    /**
     * @param team The team
     */
    @JsonProperty("team")
    public void setTeam(String team) {
        this.team = team;
    }

    public Leader withTeam(String team) {
        this.team = team;
        return this;
    }

    /**
     * @return The teamId
     */
    @JsonProperty("teamId")
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * @param teamId The teamId
     */
    @JsonProperty("teamId")
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Leader withTeamId(Integer teamId) {
        this.teamId = teamId;
        return this;
    }

    /**
     * @return The stat
     */
    @JsonProperty("stat")
    public String getStat() {
        return stat;
    }

    /**
     * @param stat The stat
     */
    @JsonProperty("stat")
    public void setStat(String stat) {
        this.stat = stat;
    }

    public Leader withStat(String stat) {
        this.stat = stat;
        return this;
    }

    /**
     * @return The pos
     */
    @JsonProperty("pos")
    public String getPos() {
        return pos;
    }

    /**
     * @param pos The pos
     */
    @JsonProperty("pos")
    public void setPos(String pos) {
        this.pos = pos;
    }

    public Leader withPos(String pos) {
        this.pos = pos;
        return this;
    }

    /**
     * @return The country
     */
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    /**
     * @param country The country
     */
    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    public Leader withCountry(String country) {
        this.country = country;
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

    public Leader withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(fid).append(teamName).append(sweater).append(headshot).append(name).append(pid).append(team).append(teamId).append(stat).append(pos).append(country).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leader) == false) {
            return false;
        }
        Leader rhs = ((Leader) other);
        return new EqualsBuilder().append(fid, rhs.fid).append(teamName, rhs.teamName).append(sweater, rhs.sweater).append(headshot, rhs.headshot).append(name, rhs.name).append(pid, rhs.pid).append(team, rhs.team).append(teamId, rhs.teamId).append(stat, rhs.stat).append(pos, rhs.pos).append(country, rhs.country).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
