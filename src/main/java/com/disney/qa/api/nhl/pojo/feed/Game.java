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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "extraTime",
        "gameRecap",
        "status",
        "cPeriod",
        "sortOrder",
        "gameId",
        "a",
        "venue",
        "h",
        "easternStartTime",
        "startTime",
        "usTv",
        "caTv",
        "gs",
        "boxHeader",
        "tickets",
        "gamePreview"
})
public class Game {

    @JsonProperty("extraTime")
    private Boolean extraTime;
    @JsonProperty("gameRecap")
    private String gameRecap;
    @JsonProperty("status")
    private String status;
    @JsonProperty("cPeriod")
    private String cPeriod;
    @JsonProperty("sortOrder")
    private Integer sortOrder;
    @JsonProperty("gameId")
    private Integer gameId;
    @JsonProperty("a")
    private Team awayTeam;
    @JsonProperty("venue")
    private String venue;
    @JsonProperty("h")
    private Team homeTeam;
    @JsonProperty("easternStartTime")
    private String easternStartTime;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("usTv")
    private String usTv;
    @JsonProperty("caTv")
    private String caTv;
    @JsonProperty("gs")
    private Integer gs;
    @JsonProperty("boxHeader")
    private List<String> boxHeader = new ArrayList<String>();
    @JsonProperty("tickets")
    private Tickets tickets;
    @JsonProperty("gamePreview")
    private String gamePreview;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Game() {
    }

    /**
     * @param extraTime
     * @param gameRecap
     * @param status
     * @param cPeriod
     * @param sortOrder
     * @param gameId
     * @param awayTeam
     * @param venue
     * @param homeTeam
     * @param easternStartTime
     * @param startTime
     * @param usTv
     * @param caTv
     * @param gs
     * @param gamePreview
     * @param boxHeader
     * @param tickets
     */
    public Game(Boolean extraTime, String gameRecap, String status, String cPeriod, Integer sortOrder, Integer gameId, Team awayTeam, String venue, Team homeTeam, String easternStartTime, String startTime, String usTv, String caTv, Integer gs, List<String> boxHeader, Tickets tickets, String gamePreview) {
        this.extraTime = extraTime;
        this.gameRecap = gameRecap;
        this.status = status;
        this.cPeriod = cPeriod;
        this.sortOrder = sortOrder;
        this.gameId = gameId;
        this.awayTeam = awayTeam;
        this.venue = venue;
        this.homeTeam = homeTeam;
        this.easternStartTime = easternStartTime;
        this.startTime = startTime;
        this.usTv = usTv;
        this.caTv = caTv;
        this.gs = gs;
        this.boxHeader = boxHeader;
        this.tickets = tickets;
        this.gamePreview = gamePreview;
    }

    /**
     * @return The extraTime
     */
    @JsonProperty("extraTime")
    public Boolean getExtraTime() {
        return extraTime;
    }

    /**
     * @param extraTime The extraTime
     */
    @JsonProperty("extraTime")
    public void setExtraTime(Boolean extraTime) {
        this.extraTime = extraTime;
    }

    public Game withExtraTime(Boolean extraTime) {
        this.extraTime = extraTime;
        return this;
    }

    /**
     * @return The gameRecap
     */
    @JsonProperty("gameRecap")
    public String getGameRecap() {
        return gameRecap;
    }

    /**
     * @param gameRecap The gameRecap
     */
    @JsonProperty("gameRecap")
    public void setGameRecap(String gameRecap) {
        this.gameRecap = gameRecap;
    }

    public Game withGameRecap(String gameRecap) {
        this.gameRecap = gameRecap;
        return this;
    }

    /**
     * @return The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public Game withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * @return The cPeriod
     */
    @JsonProperty("cPeriod")
    public String getCPeriod() {
        return cPeriod;
    }

    /**
     * @param cPeriod The cPeriod
     */
    @JsonProperty("cPeriod")
    public void setCPeriod(String cPeriod) {
        this.cPeriod = cPeriod;
    }

    public Game withCPeriod(String cPeriod) {
        this.cPeriod = cPeriod;
        return this;
    }

    /**
     * @return The sortOrder
     */
    @JsonProperty("sortOrder")
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder The sortOrder
     */
    @JsonProperty("sortOrder")
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Game withSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    /**
     * @return The gameId
     */
    @JsonProperty("gameId")
    public Integer getGameId() {
        return gameId;
    }

    /**
     * @param gameId The gameId
     */
    @JsonProperty("gameId")
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Game withGameId(Integer gameId) {
        this.gameId = gameId;
        return this;
    }

    /**
     * @return The a
     */
    @JsonProperty("a")
    public Team getAwayTeam() {
        return awayTeam;
    }

    /**
     * @param awayTeam The a
     */
    @JsonProperty("a")
    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Game withAwayTeam(Team team) {
        this.awayTeam = team;
        return this;
    }

    /**
     * @return The venue
     */
    @JsonProperty("venue")
    public String getVenue() {
        return venue;
    }

    /**
     * @param venue The venue
     */
    @JsonProperty("venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Game withVenue(String venue) {
        this.venue = venue;
        return this;
    }

    /**
     * @return The h
     */
    @JsonProperty("h")
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * @param homeTeam The h
     */
    @JsonProperty("h")
    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Game withHomeTeam(Team team) {
        this.homeTeam = team;
        return this;
    }

    /**
     * @return The easternStartTime
     */
    @JsonProperty("easternStartTime")
    public String getEasternStartTime() {
        return easternStartTime;
    }

    /**
     * @param easternStartTime The easternStartTime
     */
    @JsonProperty("easternStartTime")
    public void setEasternStartTime(String easternStartTime) {
        this.easternStartTime = easternStartTime;
    }

    public Game withEasternStartTime(String easternStartTime) {
        this.easternStartTime = easternStartTime;
        return this;
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

    public Game withStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * @return The usTv
     */
    @JsonProperty("usTv")
    public String getUsTv() {
        return usTv;
    }

    /**
     * @param usTv The usTv
     */
    @JsonProperty("usTv")
    public void setUsTv(String usTv) {
        this.usTv = usTv;
    }

    public Game withUsTv(String usTv) {
        this.usTv = usTv;
        return this;
    }

    /**
     * @return The caTv
     */
    @JsonProperty("caTv")
    public String getCaTv() {
        return caTv;
    }

    /**
     * @param caTv The caTv
     */
    @JsonProperty("caTv")
    public void setCaTv(String caTv) {
        this.caTv = caTv;
    }

    public Game withCaTv(String caTv) {
        this.caTv = caTv;
        return this;
    }

    /**
     * @return The gs
     */
    @JsonProperty("gs")
    public Integer getGs() {
        return gs;
    }

    /**
     * @param gs The gs
     */
    @JsonProperty("gs")
    public void setGs(Integer gs) {
        this.gs = gs;
    }

    public Game withGs(Integer gs) {
        this.gs = gs;
        return this;
    }

    /**
     * @return The boxHeader
     */
    @JsonProperty("boxHeader")
    public List<String> getBoxHeader() {
        return boxHeader;
    }

    /**
     * @param boxHeader The boxHeader
     */
    @JsonProperty("boxHeader")
    public void setBoxHeader(List<String> boxHeader) {
        this.boxHeader = boxHeader;
    }

    public Game withBoxHeader(List<String> boxHeader) {
        this.boxHeader = boxHeader;
        return this;
    }

    /**
     * @return The tickets
     */
    @JsonProperty("tickets")
    public Tickets getTickets() {
        return tickets;
    }

    /**
     * @param tickets The tickets
     */
    @JsonProperty("tickets")
    public void setTickets(Tickets tickets) {
        this.tickets = tickets;
    }

    public Game withTickets(Tickets tickets) {
        this.tickets = tickets;
        return this;
    }

    /**
     * @return The gamePreview
     */
    @JsonProperty("gamePreview")
    public String getGamePreview() {
        return gamePreview;
    }

    /**
     * @param gamePreview The gamePreview
     */
    @JsonProperty("gamePreview")
    public void setGamePreview(String gamePreview) {
        this.gamePreview = gamePreview;
    }

    public Game withGamePreview(String gamePreview) {
        this.gamePreview = gamePreview;
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

    public Game withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(extraTime).append(gameRecap).append(status).append(cPeriod).append(sortOrder).append(gameId).append(awayTeam).append(venue).append(homeTeam).append(easternStartTime).append(startTime).append(usTv).append(caTv).append(gs).append(boxHeader).append(tickets).append(gamePreview).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Game) == false) {
            return false;
        }
        Game rhs = ((Game) other);
        return new EqualsBuilder().append(extraTime, rhs.extraTime).append(gameRecap, rhs.gameRecap).append(status, rhs.status).append(cPeriod, rhs.cPeriod).append(sortOrder, rhs.sortOrder).append(gameId, rhs.gameId).append(awayTeam, rhs.awayTeam).append(venue, rhs.venue).append(homeTeam, rhs.homeTeam).append(easternStartTime, rhs.easternStartTime).append(startTime, rhs.startTime).append(usTv, rhs.usTv).append(caTv, rhs.caTv).append(gs, rhs.gs).append(boxHeader, rhs.boxHeader).append(tickets, rhs.tickets).append(gamePreview, rhs.gamePreview).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}