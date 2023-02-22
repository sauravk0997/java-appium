package com.disney.qa.api.nhl.tv;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.nhl.NhlParameter;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

public class NhlTVContentApiCaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

//    private static final String NHL_BAM_CONTENT_HOST = NhlParameters.getNhlContentApiHost();
    private static final String NHL_STATSAPI_HOST = NhlParameters.getNhlStatsApiHost();
    private static final String NHL_SEARCH_API_HOST = NhlParameters.getNhlSeachApiHost();

    //TODO: has to be formatted in dd/mm/yyyy
    private String scoresDate;
    private static final String NHL_TEAMS_PATH = "/api/v1/teams?";
    private static final String NHL_TEAM_PATH = "/api/v1/teams/%s/?";
    private static final String NHL_TEAM_CONTENT_PATH = "/svc/search/v2/nhl_global_en/topic/";
    private static final String NHL_TEAM_CONTENT_PATH_EXPAND = "%s?page=%s&expand=partner,platform=ftv";
    private static final String NHL_TEAMS_EXPAND = "fields=teams,id,name,abbreviation,teamName," +
            "division,conference,shortName,active&expand=";
    private static final String NHL_SCORES_PATH = "/api/v1/schedule?";
    private String NHL_SCORES_EXPAND = "date=" + "%s" + "&gameType=&leaderGameTypes=" +
            "&leaderCategories=points&hydrate=scoringplays,linescore,game(content(media(all),highlights(scoreboard),editorial(all))," +
            "seriesSummary(series(round))),team,broadcasts,tickets,metadata&platform=ftv";
    private static final String NHL_TEAMS_EXPAND_DATA = "&hydrate=tickets,leaders(categories=[goals,assists,points,gaa,timeOnIcePerGame]" +
            ",person,gameTypes=p),content(sections),previousSchedule(limit=4,game(seriesSummary(series(round)))," +
            "team(leaders(limit=1,categories=[goals,assists,points,gaa,timeOnIcePerGame]," +
            "person(stats(splits=statsSingleSeasonPlayoffs,season=" +
            NhlParameter.NHL_CURRENT_SEASON.getValue() + ")),gameTypes=p))," +
            "scoringplays,linescore,broadcasts,tickets,metadata,game(content(all,highlights(scoreboard))))," +
            "nextSchedule(limit=4,game(seriesSummary(series(round)))," +
            "team(leaders(limit=1,categories=[goals,assists,points,gaa,timeOnIcePerGame]," +
            "person(stats(splits=statsSingleSeasonPlayoffs,season=" +
            NhlParameter.NHL_CURRENT_SEASON.getValue() + ")),gameTypes=p))," +
            "scoringplays,linescore,broadcasts,tickets,metadata,game(content(all,highlights(scoreboard))))";
    private static final String NHL_STANDINGS_PATH = "/api/v1/standings?";
    private static final String NHL_STANDINGS_EXPAND = "leagueId=133&standingsType=regularSeason&expand=" +
            "standings.team,standings.record.overall,team.schedule.next";


    private static final String NHL_TEAMS_PROPERTIES_PATH = "/feed/sportsdata/nhl/en/list/v1/properties/mobile/" +
            "allTeams/android-phone-v1.json";

//    private static final String OS = IDriverPool.currentDevice.get().getDeviceType().getType().replace("_", "-");

    private RestTemplate restTemplate;
    protected Configuration jsonPathJacksonConfiguration;

    public NhlTVContentApiCaller(){
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();
        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    public NhlTVContentApiCaller(String date) {
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();
        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
        scoresDate = date;
    }

    public JsonNode getTeamsData(){
        String apiPath = NHL_STATSAPI_HOST + NHL_TEAMS_PATH + NHL_TEAMS_EXPAND;
        LOGGER.info("Getting teams data from API Path: " + apiPath);

        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getDesiredTeamsData(int teamId){
        String apiPath = NHL_STATSAPI_HOST + String.format(NHL_TEAM_PATH, teamId) + "&season=" +
                NhlParameter.NHL_CURRENT_SEASON.getValue() + "&platform=ftv" + NHL_TEAMS_EXPAND_DATA;
        LOGGER.info("Checking API Path: " + apiPath);

        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getDesiredTeamContentBasedOnTopicId(String topicId, int pageNum){
        String apiPath = NHL_SEARCH_API_HOST + String.format(NHL_TEAM_CONTENT_PATH_EXPAND, topicId, pageNum);
        LOGGER.info("Pulling searchApi data from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getScheduleScoresData(){
        String apiPath = NHL_STATSAPI_HOST + NHL_SCORES_PATH +  String.format(NHL_SCORES_EXPAND, scoresDate);
        LOGGER.info("Getting schedule/scores data from API Path: " + apiPath);

        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }
}
