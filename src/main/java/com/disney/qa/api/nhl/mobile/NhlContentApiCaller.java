package com.disney.qa.api.nhl.mobile;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.nhl.NhlParameter;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.IDriverPool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.SkipException;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("squid:S1075")
public class NhlContentApiCaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String NHL_BAM_CONTENT_HOST = NhlParameters.getNhlContentApiHost();
    private static final String NHL_STATSAPI_HOST = NhlParameters.getNhlStatsApiHost();
    private static final String NHL_SEARCHAPI_HOST = NhlParameters.getNhlSearchApiHost();
    private static final String NHL_POINT_INSIDE_HOST = NhlParameters.getPointInsideApiHost();
    private static final String NHL_CONFIG_HOST = NhlParameters.getConfigHost();

    private static final String NHL_LEAGUE_STATIC_HOST = NhlParameters.getNhlStaticHost();

    private static final String NHL_TEAMS_PATH = "/api/v1/teams/";
    private static final String NHL_SCHEDULE_PATH = "/api/v1/schedule?date=";
    private static final String NHL_GAMEPK_PATH = "/api/v1/schedule?gamePk=";
    private static final String NHL_STANDINGS_PATH = "/api/v1/standings?leagueId=133";
    private static final String NHL_LIVE_TIMECODE_PATH = "/api/v1/game/%s/feed/live?timecode=";

    private static final String NHL_LEAGUE_MATCHUP_COLORS = "clubColors/current/onDark/%s@%s.json";
    private static final String NHL_LEAGUE_LAST_5 = "lastFive/v1/all/%s.json";

    private static final String NHL_TEAMS_EXPAND = "&hydrate=tickets,leaders(categories=[goals,assists,points,gaa,timeOnIcePerGame]" +
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

    private static final String NHL_SCORES_HYDRATE = "&gameType=&platform=" +
            StringUtils.replace(IDriverPool.currentDevice.get().getDeviceType().getType(), "_", "-") +
            "&leaderGameTypes=&leaderCategories=points" +
            "&hydrate=scoringplays,linescore,game(content(media(all),highlights(scoreboard),editorial(all))," +
            "seriesSummary(series(round))),team(leaders(limit=1,person(stats(splits=statsSingleSeason,season=" +
            NhlParameter.NHL_CURRENT_SEASON.getValue() + ")),categories=[points,goals,assists],gameTypes=r)),broadcasts,tickets,metadata";

    private static final String NHL_SCORES_GAMEPK_ROSTER = "&hydrate=linescore,game(content(media(all),highlights(scoreboard)" +
            ",editorial(all)),seriesSummary(series(round))),team(roster(person(stats(splits=statsSingleSeason)))" +
            ",stats(splits=statsSingleSeason)),tickets,broadcasts";

    private static String nhlMonthlyGamePkRequest = "/api/v1/schedule?startDate=%s&endDate=%s&fields=dates,date,games,gamePk";

    private static final String NHL_TEAM_RECORDS_QUERY = "&standingsType=regularSeason&expand=standings.team,standings.record.overall";

    private static String nhlTeamsNewsExpand = "?page=%s&expand=partner";

    private static final String NHL_TEAMS_PROPERTIES_PATH = "/feed/sportsdata/nhl/en/list/v1/properties/mobile/" +
            "allTeams/android-phone-v1.json";

    private static final String NHL_POINT_INSIDE_KEYS = "f583583eae2740558f84e2a4c392673&devId=e4b6bda594d8877080e64c587cf3ffdb";

    private static final String API_LOGGER_TEXT = "Pulling API response from: ";

    private RestTemplate restTemplate;
    protected Configuration jsonPathJacksonConfiguration;

    public NhlContentApiCaller() {
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();
        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    private static String getRunningOS(){
        return IDriverPool.currentDevice.get().getDeviceType().getType().replace("_", "-");
    }

    private static String getShortFormOS(){

        if(R.CONFIG.get("env").contains("amazon")){
            return "fire";
        } else {
            String os = IDriverPool.currentDevice.get().getDeviceType().getType();
            return os.substring(0, os.indexOf('_'));
        }
    }

    public List<String> checkForContentHeaders(String content, String lang) {
        String apiPath = NHL_BAM_CONTENT_HOST +
                "/nhl/" + lang + "/section/v1/" + content + "/21/" + getRunningOS() + "-v1.json";

        LOGGER.info("Checking API Path: " + apiPath);

        String jsonPath = "";
        if("news".equals(content)){
            jsonPath = "$.articlesLongList.*.headline";
        } else {
            jsonPath = "$.videosLongList.*.headline";
        }

        LinkedList<String> headlines = new LinkedList<>();
        JsonNode rawResponse = restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
        LOGGER.info("Response: " + rawResponse);

        JsonNode articleList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPath);

        if (articleList.size() >= 1) {
            for (JsonNode node : articleList) {
                headlines.add(node.asText());
            }
        }

        LOGGER.info("Found sectionHeaders: " + headlines.toString());

        return headlines;
    }

    public JsonNode getTeamPageData(int teamId, String lang){
        try {
            String apiPath = NHL_STATSAPI_HOST + NHL_TEAMS_PATH + teamId + "?season=" +
                    NhlParameter.NHL_CURRENT_SEASON.getValue() + "&site=" + lang + "_nhl&platform=" + getRunningOS() + NHL_TEAMS_EXPAND;
            LOGGER.info("Checking API Path: " + apiPath);

            return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
        } catch (HttpClientErrorException e){
            LOGGER.info(String.format("Exception on request: %s. Attempting without season parameter in case it's a new team...", e));
            String apiPath = NHL_STATSAPI_HOST + NHL_TEAMS_PATH + teamId + "?site=" + lang + "_nhl&platform=" + getRunningOS() + NHL_TEAMS_EXPAND;

            return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
        }
    }

    public JsonNode getTeamPagePropertiesData(){
        String apiPath = NHL_BAM_CONTENT_HOST + NHL_TEAMS_PROPERTIES_PATH;
        LOGGER.info("Pulling properties from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getTeamPageNewsResults(String contentId, int pageNum, String lang){
        String apiPath = NHL_SEARCHAPI_HOST + "svc/search/v2/nhl_global_" + lang + "/topic/" + contentId + String.format(nhlTeamsNewsExpand, pageNum) + ",platform=" + getRunningOS();
        LOGGER.info("Pulling searchApi data from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getTeamPageVideoResults(String contentId, String lang){
        String apiPath = NHL_SEARCHAPI_HOST + "svc/search/v2/nhl_global_" + lang + "/topic/" + contentId + "?platform=" + getRunningOS();
        LOGGER.info("Pulling searchApi data from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getTeamArenaSdkResult(String venueId){
        String apiPath = NHL_BAM_CONTENT_HOST + "/feed/sportsdata/nhl/en/venue/v1/" + venueId + "/" + getRunningOS() + "-v1.json";
        LOGGER.info("Pulling Arena SDK data from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getTeamPointInsideResult(String venueID){
        String apiPath = NHL_POINT_INSIDE_HOST + "feeds/maps/v1.5/venues/" + venueID + "?apiKey=" + NHL_POINT_INSIDE_KEYS;
        LOGGER.info("Pulling PointInside SDK Map data from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getArenaSdkSearchResults(String venueID, String keyword){
        String apiPath = NHL_POINT_INSIDE_HOST + "search/v1.5/search?q=" + keyword + "&venue=" + venueID +
                "&classes=place&limit=100&source=NHL&apiKey=" + NHL_POINT_INSIDE_KEYS;
        LOGGER.info("Pulling PointInside SDK Search data from: " + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    /* configType value determines the file being returned. Valid values are:
     * dataRequestConfig, config, configClubPage, strings,
     * pushNotificationsConfig, scoreboardConfig, and adMarketingConfig
     */
    public JsonNode getAppConfig(String version, String configType){
        JsonNode response = null;

        for(int i = Integer.parseInt(version.substring(version.length()-1)); i >= 0; i--){
            String apiPath = String.format("%s/%s.json", NHL_CONFIG_HOST + getShortFormOS() + version.substring(0, version.length()-1) + i, configType);
            LOGGER.info("Pulling config from: " + apiPath);
            try{
                response = restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
            } catch (HttpClientErrorException e){
                LOGGER.info("ERROR: Config not available for version code " + version + "\nException: "  + e);
                if(i == 0){
                    throw new SkipException("Skipping Test. Config host is is unavaialble. App will be non-functional and cannot be used at this time.");
                }
            }
        }
        return response;
    }

    public JsonNode getScheduleData(String date){
        String apiPath = NHL_STATSAPI_HOST + NHL_SCHEDULE_PATH + date + NHL_SCORES_HYDRATE;
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getLastFiveLeadersData(String teamID){
        String apiPath  = NHL_LEAGUE_STATIC_HOST + String.format(NHL_LEAGUE_LAST_5, teamID);
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getGameRoster(String gamePk){
        String apiPath = NHL_STATSAPI_HOST + NHL_GAMEPK_PATH + gamePk + NHL_SCORES_GAMEPK_ROSTER;
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getGameCenterDarkModeColors(String awayTeam, String homeTeam){
        String apiPath = NHL_LEAGUE_STATIC_HOST + String.format(NHL_LEAGUE_MATCHUP_COLORS, awayTeam, homeTeam);
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getTeamStandingsData(){
        String apiPath = NHL_STATSAPI_HOST + NHL_STANDINGS_PATH + NHL_TEAM_RECORDS_QUERY;
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getGameLiveTimecodeData(String gamePk){
        String apiPath = NHL_STATSAPI_HOST + String.format(NHL_LIVE_TIMECODE_PATH, gamePk);
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    public JsonNode getAllMonthGamePks(String startDate, String endDate){
        String apiPath = NHL_STATSAPI_HOST + String.format(nhlMonthlyGamePkRequest, startDate, endDate);
        LOGGER.debug(API_LOGGER_TEXT + apiPath);
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }
}
