package com.disney.qa.api.nhl.statsapi;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections4.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.CheckForNull;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * NHL Handset Stats Filters API
 *
 * @author bzayats
 *
 */

public class NhlFilterStatsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    final String REQUEST_SEASONS = "/api/v1/seasons";
    final String REQUEST_ACTIVE_HISTORIC_PRESENT = "/api/v1/teams?season=%s&hydrate=teams(active=false)";
    final String REQUEST_LOGOS = "/api/v1/teams?hydrate=logos(platform=android-phone,type=png,size=96)&season=%s";
    final String REQUEST_HISTORIC_SEASON_BASED_PLAYOFFS_WITH_SEASONRANGE = "/api/v1/teams?season=%s&hydrate=playoffs(season=%s),seasonRange";
    final String REQUEST_ACTIVE_HISTORIC_SEASON_BASED_WITH_SEASONRANGE = "/api/v1/teams?season=%s&hydrate=seasonRange";
    final String REQUEST_ACTIVE_TEAMS_SEASON_BASED_MADE_PLAYOFFS = "/api/v1/teams?season=%s&hydrate=playoffs(season=%s)";
    final String REQUEST_SKATERS_LEADERS_PERSONS_SEASON_BASED = "/api/v1/stats/leaders?leaderCategories=points&hydrate=skaters,person&limit=50&season=%s";
    final String REQUEST_NHL_PLAYER_SEASON_LEADERS = "/api/v1/stats/leaders?leaderCategories=%s&hydrate=%s&limit=3&season=%s&leaderGameTypes=%s";
    final String REQUEST_SPECIFIC_TEAM_SEASON_LEADERS = "/api/v1/teams/%s/leaders?leaderCategories=%s&hydrate=stats&limit=3&season=%s&leaderGameTypes=%s";
    final String REQUEST_NHL_TEAM_LEADERS = "/api/v1/teams?gameType=%s&expand=team.stats&season=%s&hydrate=stats";
    final String REQUEST_GOALIES_LEADERS_PERSONS_SEASON_BASED = "/api/v1/stats/leaders?leaderCategories=gaa&hydrate=person&limit=50&season=%s";
    final String REQUEST_GOALIES_LEADERS_WINS_SEASON_BASED = "/api/v1/stats/leaders?leaderCategories=wins&hydrate=person&limit=50&season=%s";
    final String REQUEST_TEAM_STATS_SEASON_BASED = "/api/v1/teams?season=%s&hydrate=stats";
    final String REQUEST_SKATERS_LEADERS_PERSONS_SEASON_BASED_PLAYOFFS = "/api/v1/stats/leaders?leaderCategories=points&hydrate=skaters,person&limit=50&season=%s&leaderGameTypes=P";
    final String REQUEST_GOALIES_LEADERS_PERSONS_SEASON_BASED_PLAYOFFS = "/api/v1/stats/leaders?leaderCategories=savePct&hydrate=person&limit=50&season=%s&leaderGameTypes=P";

    RestTemplate restTemplate = RestTemplateBuilder.newInstance().withDisabledSslChecking().
            withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();


    public List<String> getFilterStatsHistoricTeams(String season){
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_ACTIVE_HISTORIC_PRESENT, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> nonActiveTeams = getFilteredContent(rawResponse,"active",
                false, "$['teams'][?]");

        List<String> teamNames = new LinkedList<>();
        nonActiveTeams.forEach(nonActiveTeam->teamNames.add((String) nonActiveTeam.get("name")));

        LOGGER.info("historic teams: " + teamNames);

        return teamNames;
    }

    public List<String> getFilterStatsActiveTeams(String season){
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_ACTIVE_HISTORIC_PRESENT, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> activeTeams = getFilteredContent(rawResponse,"active",
                true, "$['teams'][?]");

        List<String> teamNames = new LinkedList<>();
        activeTeams.forEach(team->teamNames.add((String) team.get("name")));

        LOGGER.info("historic teams: " + teamNames);

        return teamNames;
    }

    public MultiValueMap getFilterStatsForTeamsWithSeasonRange(String season, boolean active){
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_ACTIVE_HISTORIC_SEASON_BASED_WITH_SEASONRANGE, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> historicTeams = getFilteredContent(rawResponse,"active",
                active, "$['teams'][?]");

        MultiValueMap<String, String> teamSeasonRange = new MultiValueMap<>();
        historicTeams.forEach(team->teamSeasonRange.put((String) team.get("name"),
                JsonPath.parse(team).read("$['seasonRange']['firstSeasonOfPlay']")));
        if (!active) {
            historicTeams.forEach(nonActiveTeam -> teamSeasonRange.put((String) nonActiveTeam.get("name"),
                    JsonPath.parse(nonActiveTeam).read("$['seasonRange']['lastSeasonOfPlay']")));
        }

        LOGGER.info("Teams and their time-frame ranges: " + teamSeasonRange);

        return teamSeasonRange;
    }

    public List<String> getFilterStatsActiveTeamsSeasonBasedMadePlayoffs(String season) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_ACTIVE_TEAMS_SEASON_BASED_MADE_PLAYOFFS, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> activeTeams = getFilteredContent(rawResponse,"playoffInfo.madePlayoffs",true, "$['teams'][?]");

        List<String> teamNames = new LinkedList<>();
        activeTeams.forEach(team -> teamNames.add((String) team.get("name")));

        return teamNames;
    }

    @CheckForNull
    public List<String> getFilterStatsLeadersSkatersGoaliesSeasonBased(String season, String value, String teamName, String leaderSkater, String filterType) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_SKATERS_LEADERS_PERSONS_SEASON_BASED, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> leaders = new LinkedList<>();

        switch(filterType) {
            case "specificTeamLeadersPerson":
                leaders = executeFilter(rawResponse, teamName, leaderSkater, value, "specificTeamLeadersPerson");
                break;
            case "nhlLeaders":
                leaders = executeFilter(rawResponse, "none", "none", value, "nhlLeaders");
                break;
            default:
                LOGGER.info("Please provide proper values for filterExecute()..");
                break;
        }

        List<String> persons = new LinkedList<>();
        leaders.forEach(skater -> persons.add(JsonPath.parse(skater).read("$['person']['fullName']")));

        LOGGER.info("Leader persons for season: '" + season + "': " + persons);

        return persons;
    }

    public List<String> getFilterStatsLeadersGoaliesGoalsAgainstAverageSeasonBased(String season, String value, String teamName, String leaderSkater, String filterType) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_GOALIES_LEADERS_PERSONS_SEASON_BASED, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> leaders = new LinkedList<>();;

        switch(filterType) {
            case "specificTeamLeadersPerson":
                leaders = executeFilter(rawResponse, teamName, leaderSkater, value, "specificTeamLeadersPerson");
                break;
            case "nhlLeaders":
                leaders = executeFilter(rawResponse, "none", "none", value, "nhlLeaders");
                break;
            default:
                LOGGER.info("Please provide proper values for filterExecute()..");
                break;
        }

        List<String> persons = new LinkedList<>();
        leaders.forEach(skater -> persons.add(JsonPath.parse(skater).read("$['person']['fullName']")));

        LOGGER.info("Leader persons for season: '" + season + "': " + persons);

        return persons;
    }

    public List<String> getFilterStatsLeadersGoaliesSeasonBased(String season, String value, String teamName, String leaderSkater, String filterType) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_GOALIES_LEADERS_WINS_SEASON_BASED, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> leaders = new LinkedList<>();;

        switch(filterType) {
            case "specificTeamLeadersPerson":
                leaders = executeFilter(rawResponse, teamName, leaderSkater, value, "specificTeamLeadersPerson");
                break;
            case "nhlLeaders":
                leaders = executeFilter(rawResponse, "none", "none", value, "nhlLeaders");
                break;
            default:
                LOGGER.info("Please provide proper values for filterExecute()..");
                break;
        }

        List<String> goalies = new LinkedList<>();
        leaders.forEach(skater -> goalies.add(JsonPath.parse(skater).read("$['person']['fullName']")));

        LOGGER.info("Leader goalies for season: '" + season + "': " + goalies);

        return goalies;
    }

    public List<String> getFilterStatsLeadersTeamsSeasonBased(String season, String attribute, String value) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_TEAM_STATS_SEASON_BASED, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> leaders = getFilteredContent(rawResponse,attribute,
                value, "$['teams'][?]");

        List<String> teams = new LinkedList<>();
        leaders.forEach(percentage -> teams.add(JsonPath.parse(percentage).read("$.name")));

        LOGGER.info("Season: '" + season + ", name: " + teams);

        return teams;
    }

    public List<String> getFilterStatsLeadersSkatersSeasonBasedPlayoffs(String season, String value, String teamName, String leaderSkater, String filterType) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_SKATERS_LEADERS_PERSONS_SEASON_BASED_PLAYOFFS, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> leaders = null;

        switch(filterType) {
            case "specificTeamLeadersPerson":
                leaders = executeFilter(rawResponse, teamName, leaderSkater, value, "specificTeamLeadersPerson");
                break;
            case "nhlLeaders":
                leaders = executeFilter(rawResponse, "none", "none", value, "nhlLeaders");
                break;
            default:
                LOGGER.info("Please provide proper values for filterExecute()..");
                break;
        }

        List<String> persons = new LinkedList<>();
        leaders.forEach(skater -> persons.add(JsonPath.parse(skater).read("$['person']['fullName']")));

        LOGGER.info("Leader persons for season: '" + season + "': " + persons);

        return persons;
    }

    public List<String> getFilterStatsLeadersGoaliesSavePtcSeasonBasedPlayoffs(String season, String value, String teamName, String leaderSkater, String filterType) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_GOALIES_LEADERS_PERSONS_SEASON_BASED_PLAYOFFS, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> leaders = new LinkedList<>();;

        switch(filterType) {
            case "specificTeamLeadersPerson":
                leaders = executeFilter(rawResponse, teamName, leaderSkater, value, "specificTeamLeadersPerson");
                break;
            case "nhlLeaders":
                leaders = executeFilter(rawResponse, "none", "none", value, "nhlLeaders");
                break;
            default:
                LOGGER.info("Please provide proper values for filterExecute()..");
                break;
        }

        List<String> persons = new LinkedList<>();
        leaders.forEach(skater -> persons.add(JsonPath.parse(skater).read("$['person']['fullName']")));

        LOGGER.info("Leader persons for season: '" + season + "': " + persons);

        return persons;
    }

    /**
     * IOS Stats api data fetch
     */

    public List<String> getListOfSeasonsIOS(){
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + REQUEST_SEASONS, JsonNode.class).getBody().toString();
        LOGGER.info("Json response: " + rawResponse);
        return JsonPath.parse(rawResponse).read("$['seasons'][*].seasonId");
    }

    public List<String> getFilterStatesActiveTeamPlayoffsIOS(String season) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_ACTIVE_TEAMS_SEASON_BASED_MADE_PLAYOFFS, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> activeTeams = JsonPath.parse(rawResponse).read("$['teams'][?(@.active == true && @.playoffInfo.madePlayoffs == true)]");

        List<String> teamNames = new LinkedList<>();
        activeTeams.forEach(team -> teamNames.add((String) team.get("name")));

        LOGGER.info("Active teams that made playoffs for season '" + season + "': " + teamNames);

        return teamNames;
    }

    public List<String> getFilterStatesHistoricTeamPlayoffsIOS(String season) {
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost()
                + String.format(REQUEST_ACTIVE_TEAMS_SEASON_BASED_MADE_PLAYOFFS, season, season), JsonNode.class)
                .getBody().toString();
        LOGGER.info("Json response: " + rawResponse);

        List<Map<String, Object>> historicTeams = JsonPath.parse(rawResponse).read("$['teams'][?(@.active == false && @.playoffInfo.madePlayoffs == true)]");

        List<String> teamNames = new LinkedList<>();
        historicTeams.forEach(team -> teamNames.add((String) team.get("name")));

        LOGGER.info("Historic teams that made playoffs for season '" + season + "': " + teamNames);

        return teamNames;
    }

    public Map<String, String> getStatsPlayerLeaderIOS(String category, String playerType, String season, String gameType){

        String formattedParam = String.format(REQUEST_NHL_PLAYER_SEASON_LEADERS, category, playerType , season, gameType);
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + formattedParam, JsonNode.class).getBody().toString();
        LOGGER.info("JSON response: " + rawResponse);

        Map<String, String> mapOfLeadersData = new HashMap<>();
        List<String> listOfLeaderNames = JsonPath.parse(rawResponse).read("$.leagueLeaders[*].leaders[*].person.fullName");

        for (String leaderName : listOfLeaderNames) {
            List<String> valueArray = JsonPath.parse(rawResponse).read("$.leagueLeaders[*].leaders[?(@.person.fullName == '" + leaderName + "')].value");

            mapOfLeadersData.put(
                    leaderName,
                    valueArray.get(0)
            );
        }

        return mapOfLeadersData;
    }

    public Map<String, String> getStatsTeamLeadersIOS(String gameType, String season, String statsData){

        String formattedParam = String.format(REQUEST_NHL_TEAM_LEADERS, gameType, season);
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + formattedParam, JsonNode.class).getBody().toString();
        LOGGER.info("JSON response: " + rawResponse);

        Map<String, String> mapToReturn = new HashMap<>();
        String[] arrayOfPlacement = {"1st","2nd","3rd"};

        for (int i = 0; i < arrayOfPlacement.length; i++) {
            List<String> arrayedData = JsonPath.parse(rawResponse).read("$.teams[?(@.teamStats[0].splits[1].stat." + statsData + " == '" + arrayOfPlacement[i] + "')].name");
            mapToReturn.put(arrayedData.get(0), String.valueOf(i+1));
        }

        return mapToReturn;
    }

    public Map<String, String> getStatsSpecificTeamLeadersIOS(String teamID, String category, String season, String gameType){

        String formattedParam = String.format(REQUEST_SPECIFIC_TEAM_SEASON_LEADERS, teamID, category, season, gameType);
        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + formattedParam, JsonNode.class).getBody().toString();
        LOGGER.info("JSON response: " + rawResponse);

        Map<String, String> mapOfLeadersData = new HashMap<>();
        List<String> listOfLeaderNames = JsonPath.parse(rawResponse).read("$.teamLeaders[*].leaders[*].person.fullName");

        for (String leaderName : listOfLeaderNames) {
            List<String> valueArray = JsonPath.parse(rawResponse).read("$.teamLeaders[*].leaders[?(@.person.fullName == '" + leaderName + "')].value");

            mapOfLeadersData.put(
                    leaderName,
                    valueArray.get(0)
            );
        }

        return mapOfLeadersData;
    }


    /** NhlStatsFilters helpers **/
    private List<Map<String, Object>> getFilteredContent(String rawResponse, String filterCriteriaAttribute,
                                                         Object filterCriteriaValue, String jsonPathReadPath) {
        List<Map<String, Object>> content = new LinkedList<>();

        Filter filter = Filter.filter(Criteria.where(filterCriteriaAttribute).is(filterCriteriaValue));
        content = JsonPath.parse(rawResponse)
                .read(jsonPathReadPath, filter);

        return content;
    }

    private List<Map<String, Object>> executeFilter(String rawResponse, String teamName, String person, String value, String filterType){

        List<Map<String, Object>> content = new LinkedList<>();

        switch(filterType){
            case "specificTeamLeadersPerson":
                content = getFilteredContent(rawResponse,"value",
                        value, "$..['leaders'][?(@.team.name == '" + teamName + "' && @.person.fullName == '" + person + "')]");
                break;
            case "nhlLeaders":
                content = getFilteredContent(rawResponse,"value",
                        value, "$..['leaders'][?]");
                break;
            case "nhlteams":
                content = getFilteredContent(rawResponse,"value",
                        value, "$['teams'][?]");
                break;
            default:
                LOGGER.info("Please provide valid team option!");
                break;
        }

        return content;
    }
}
