package com.disney.qa.api.nhl.pojo.stats.standings;

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
        "id",
        "name",
        "link",
        "venue",
        "abbreviation",
        "teamName",
        "locationName",
        "firstYearOfPlay",
        "division",
        "conference",
        "officialSiteUrl",
        "franchiseId"
})
public class Team {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("link")
    private String link;
    @JsonProperty("venue")
    private Venue venue;
    @JsonProperty("abbreviation")
    private String abbreviation;
    @JsonProperty("teamName")
    private String teamName;
    @JsonProperty("locationName")
    private String locationName;
    @JsonProperty("firstYearOfPlay")
    private String firstYearOfPlay;
    @JsonProperty("division")
    private Division division;
    @JsonProperty("conference")
    private Conference conference;
    @JsonProperty("officialSiteUrl")
    private String officialSiteUrl;
    @JsonProperty("franchiseId")
    private Integer franchiseId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Team() {
    }

    /**
     * @param teamName
     * @param id
     * @param firstYearOfPlay
     * @param division
     * @param officialSiteUrl
     * @param link
     * @param name
     * @param locationName
     * @param conference
     * @param abbreviation
     * @param venue
     * @param franchiseId
     */
    public Team(Integer id, String name, String link, Venue venue, String abbreviation, String teamName, String locationName, String firstYearOfPlay, Division division, Conference conference, String officialSiteUrl, Integer franchiseId) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.venue = venue;
        this.abbreviation = abbreviation;
        this.teamName = teamName;
        this.locationName = locationName;
        this.firstYearOfPlay = firstYearOfPlay;
        this.division = division;
        this.conference = conference;
        this.officialSiteUrl = officialSiteUrl;
        this.franchiseId = franchiseId;
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Team withId(Integer id) {
        this.id = id;
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

    public Team withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The link
     */
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    public Team withLink(String link) {
        this.link = link;
        return this;
    }

    /**
     * @return The venue
     */
    @JsonProperty("venue")
    public Venue getVenue() {
        return venue;
    }

    /**
     * @param venue The venue
     */
    @JsonProperty("venue")
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Team withVenue(Venue venue) {
        this.venue = venue;
        return this;
    }

    /**
     * @return The abbreviation
     */
    @JsonProperty("abbreviation")
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation The abbreviation
     */
    @JsonProperty("abbreviation")
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Team withAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    public Team withTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    /**
     * @return The locationName
     */
    @JsonProperty("locationName")
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName The locationName
     */
    @JsonProperty("locationName")
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Team withLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    /**
     * @return The firstYearOfPlay
     */
    @JsonProperty("firstYearOfPlay")
    public String getFirstYearOfPlay() {
        return firstYearOfPlay;
    }

    /**
     * @param firstYearOfPlay The firstYearOfPlay
     */
    @JsonProperty("firstYearOfPlay")
    public void setFirstYearOfPlay(String firstYearOfPlay) {
        this.firstYearOfPlay = firstYearOfPlay;
    }

    public Team withFirstYearOfPlay(String firstYearOfPlay) {
        this.firstYearOfPlay = firstYearOfPlay;
        return this;
    }

    /**
     * @return The division
     */
    @JsonProperty("division")
    public Division getDivision() {
        return division;
    }

    /**
     * @param division The division
     */
    @JsonProperty("division")
    public void setDivision(Division division) {
        this.division = division;
    }

    public Team withDivision(Division division) {
        this.division = division;
        return this;
    }

    /**
     * @return The conference
     */
    @JsonProperty("conference")
    public Conference getConference() {
        return conference;
    }

    /**
     * @param conference The conference
     */
    @JsonProperty("conference")
    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Team withConference(Conference conference) {
        this.conference = conference;
        return this;
    }

    /**
     * @return The officialSiteUrl
     */
    @JsonProperty("officialSiteUrl")
    public String getOfficialSiteUrl() {
        return officialSiteUrl;
    }

    /**
     * @param officialSiteUrl The officialSiteUrl
     */
    @JsonProperty("officialSiteUrl")
    public void setOfficialSiteUrl(String officialSiteUrl) {
        this.officialSiteUrl = officialSiteUrl;
    }

    public Team withOfficialSiteUrl(String officialSiteUrl) {
        this.officialSiteUrl = officialSiteUrl;
        return this;
    }

    /**
     * @return The franchiseId
     */
    @JsonProperty("franchiseId")
    public Integer getFranchiseId() {
        return franchiseId;
    }

    /**
     * @param franchiseId The franchiseId
     */
    @JsonProperty("franchiseId")
    public void setFranchiseId(Integer franchiseId) {
        this.franchiseId = franchiseId;
    }

    public Team withFranchiseId(Integer franchiseId) {
        this.franchiseId = franchiseId;
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

    public Team withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(link).append(venue).append(abbreviation).append(teamName).append(locationName).append(firstYearOfPlay).append(division).append(conference).append(officialSiteUrl).append(franchiseId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Team) == false) {
            return false;
        }
        Team rhs = ((Team) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(link, rhs.link).append(venue, rhs.venue).append(abbreviation, rhs.abbreviation).append(teamName, rhs.teamName).append(locationName, rhs.locationName).append(firstYearOfPlay, rhs.firstYearOfPlay).append(division, rhs.division).append(conference, rhs.conference).append(officialSiteUrl, rhs.officialSiteUrl).append(franchiseId, rhs.franchiseId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
