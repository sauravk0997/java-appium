package com.disney.qa.api.nhl.pojo.stats.teams;

import com.disney.qa.api.nhl.pojo.stats.leaders.LeagueLeader;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.List;
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
        "franchiseId",
        "active"
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
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("teamLeaders")
    private List<LeagueLeader> leagueLeaders;
    @JsonProperty("roster")
    private Roster roster;
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
     * @param active
     * @param locationName
     * @param conference
     * @param abbreviation
     * @param venue
     * @param franchiseId
     */
    public Team(Integer id, String name, String link, Venue venue, String abbreviation, String teamName, String locationName, String firstYearOfPlay, Division division, Conference conference, String officialSiteUrl, Integer franchiseId, Boolean active) {
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
        this.active = active;
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

    /**
     * @return The active
     */
    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active The active
     */
    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Team withActive(Boolean active) {
        this.active = active;
        return this;
    }

    @JsonProperty("teamLeaders")
    public List<LeagueLeader> getLeagueLeaders() {
        return leagueLeaders;
    }

    @JsonProperty("teamLeaders")
    public void setLeagueLeaders(List<LeagueLeader> leagueLeaders) {
        this.leagueLeaders = leagueLeaders;
    }

    @JsonProperty("roster")
    public Roster getRoster() {
        return roster;
    }

    @JsonProperty("roster")
    public void setRoster(Roster roster) {
        this.roster = roster;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;

        Team team = (Team) o;

        if (id != null ? !id.equals(team.id) : team.id != null) return false;
        if (name != null ? !name.equals(team.name) : team.name != null) return false;
        if (link != null ? !link.equals(team.link) : team.link != null) return false;
        if (venue != null ? !venue.equals(team.venue) : team.venue != null) return false;
        if (abbreviation != null ? !abbreviation.equals(team.abbreviation) : team.abbreviation != null) return false;
        if (teamName != null ? !teamName.equals(team.teamName) : team.teamName != null) return false;
        if (locationName != null ? !locationName.equals(team.locationName) : team.locationName != null) return false;
        if (firstYearOfPlay != null ? !firstYearOfPlay.equals(team.firstYearOfPlay) : team.firstYearOfPlay != null)
            return false;
        if (division != null ? !division.equals(team.division) : team.division != null) return false;
        if (conference != null ? !conference.equals(team.conference) : team.conference != null) return false;
        if (officialSiteUrl != null ? !officialSiteUrl.equals(team.officialSiteUrl) : team.officialSiteUrl != null)
            return false;
        if (franchiseId != null ? !franchiseId.equals(team.franchiseId) : team.franchiseId != null) return false;
        if (active != null ? !active.equals(team.active) : team.active != null) return false;
        if (leagueLeaders != null ? !leagueLeaders.equals(team.leagueLeaders) : team.leagueLeaders != null)
            return false;
        if (additionalProperties != null ? !additionalProperties.equals(team.additionalProperties) : team.additionalProperties != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (venue != null ? venue.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (teamName != null ? teamName.hashCode() : 0);
        result = 31 * result + (locationName != null ? locationName.hashCode() : 0);
        result = 31 * result + (firstYearOfPlay != null ? firstYearOfPlay.hashCode() : 0);
        result = 31 * result + (division != null ? division.hashCode() : 0);
        result = 31 * result + (conference != null ? conference.hashCode() : 0);
        result = 31 * result + (officialSiteUrl != null ? officialSiteUrl.hashCode() : 0);
        result = 31 * result + (franchiseId != null ? franchiseId.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (leagueLeaders != null ? leagueLeaders.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
