package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.pojo.stats.schedule.Schedule;
import com.disney.qa.api.nhl.pojo.stats.teams.Teams;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * One more name dependent on language: Sénateurs d'Ottawa / Ottawa Senators
 * <p/>
 * Finish this task:
 * https://www.pivotaltracker.com/projects/1427108/stories/111614776
 */
public class NhlLanguageTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String TEAM_NAME_ENGLISH = "Montréal Canadiens";

    public static final String TEAM_NAME_FRENCH = "Canadiens de Montréal";

    public static final String TEAM_NAME_SPANISH = "Montreal Canadiens";

    public static final String DIVISION_ALL_EN = "nhl/api/divisions/divisions_all_en.json";

    public static final String DIVISION_ALL_FR = "nhl/api/divisions/divisions_all_fr.json";

    public static final String DIVISION_ALL_ES = "nhl/api/divisions/divisions_all_es.json";

    public static final String CONFERENCES_INACTIVE_EN = "nhl/api/conferences/inactive_conferences_en.json";

    public static final String CONFERENCES_INACTIVE_FR = "nhl/api/conferences/inactive_conferences_fr.json";

    public static final String TEAMS_LEAGUE_ID_394_LANGUAGE_RU_DATA_FILE = "nhl/api/teams/teams_league_id_394_language_ru.json";

    protected Gson gson = new Gson();

    // TODO looks like 'FUS' constant should be added to french language
    protected List ordinalNumValuesFr = Lists.newArrayList("1re", "2e", "3e", "P", "FUS", "3e P", "2e P");
    protected List ordinalNumValuesEn = Lists.newArrayList("1st", "2nd", "3rd", "OT", "2OT", "3OT", "SO");
    protected List ordinalNumValuesEs = Lists.newArrayList("1ero", "2do", "3ero", "TE", "2TE", "3TE", "TD", "1", "2", "3", "P");

    /* Verifies that the team name returned in the endpoint used matches the value stored in the test class corresponding
     * to the language passed in the header. URL hit: http://qa-statsapi.web.nhl.com/api/v1/schedule?date=01/06/2016
     */
    @Test(description = "JIRA# QAA-1057", groups = "Team Name: Schedule Specific Date simple")
    public void verifyLanguageInScheduleSpecificDateEnglish() {
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("X-Site", "en_nhl"), ImmutableMap.of("date", "01/06/2016"), Schedule.class);

        Assert.assertEquals(schedule.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName(), "Montréal Canadiens");
    }

    @Test(groups = "Team Name: Schedule Specific Date simple")
    public void verifyLanguageInScheduleSpecificDateFrench() {
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("X-Site", "fr_nhl"), ImmutableMap.of("date", "01/06/2016"), Schedule.class);

        Assert.assertEquals(schedule.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName(), TEAM_NAME_FRENCH);
    }

    @Test(groups = "Team Name: Schedule Specific Date simple")
    public void verifyLanguageInScheduleSpecificDateSpanish() {
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("X-Site", "es_nhl"), ImmutableMap.of("date", "01/06/2016"), Schedule.class);

        Assert.assertEquals(schedule.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName(), TEAM_NAME_SPANISH);
    }

    /* Verifies that the team name returned in the endpoint used matches the value stored in the test class corresponding
     * to the language passed in the header. URL hit: http://qa-statsapi.web.nhl.com/api/v1/schedule?date=01/06/2016&expand=schedule.teams
     */
    @Test(description = "JIRA# QAA-1057", groups = "Team Name: Schedule Specific Date Expand")
    public void verifyLanguageInScheduleExpandTeamsSpecificDateEnglish() {
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("X-Site", "en_nhl"), ImmutableMap.of("date", "01/06/2016", "expand", "schedule.teams"), Schedule.class);

        Assert.assertEquals(schedule.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName(), TEAM_NAME_ENGLISH);
    }

    @Test(groups = "Team Name: Schedule Specific Date Expand")
    public void verifyLanguageInScheduleExpandTeamsSpecificDateFrench() {
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("X-Site", "fr_nhl"), ImmutableMap.of("date", "01/06/2016", "expand", "schedule.teams"), Schedule.class);

        Assert.assertEquals(schedule.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName(), TEAM_NAME_FRENCH);
    }

    @Test(groups = "Team Name: Schedule Specific Date Expand")
    public void verifyLanguageInScheduleExpandTeamsSpecificDateSpanish() {
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("X-Site", "es_nhl"), ImmutableMap.of("date", "01/06/2016", "expand", "schedule.teams"), Schedule.class);

        Assert.assertEquals(schedule.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName(), TEAM_NAME_SPANISH);
    }

    /* Verifies that the team name returned in the endpoint used matches the value stored in the test class corresponding
     * to the language passed in the header. URL hit: http://qa-statsapi.web.nhl.com/api/v1/teams
     */
    @Test(description = "JIRA# QAA-1057", groups = "Team Name: Teams endpoint")
    public void verifyLanguageInTeamsEnglish() {
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("X-Site", "en_nhl"), Collections.EMPTY_MAP, Teams.class);

        Assert.assertEquals(teams.getTeams().get(7).getName(), TEAM_NAME_ENGLISH);
    }

    @Test(groups = "Team Name: Teams endpoint")
    public void verifyLanguageInTeamsFrench() {
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("X-Site", "fr_nhl"), Collections.EMPTY_MAP, Teams.class);

        Assert.assertEquals(teams.getTeams().get(7).getName(), TEAM_NAME_FRENCH);
    }

    @Test(groups = "Team Name: Teams endpoint")
    public void verifyLanguageInTeamsSpanish() {
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("X-Site", "es_nhl"), Collections.EMPTY_MAP, Teams.class);

        Assert.assertEquals(teams.getTeams().get(7).getName(), TEAM_NAME_SPANISH);
    }

    /* Test verifies division names are returned with the correct values as stored in our local
     * division names json files. URL hit: http://qa-statsapi.web.nhl.com/api/v1/divisions?site=xx_nhl
     */
    @Test(description = "JIRA# QAA-1057", groups = "division")
    public void verifyAllDivisionsEnglish() throws IOException, JSONException {
        verifyDivisionsResponseWithJsonAssert(DIVISION_ALL_EN, ImmutableMap.of("site", "en_nhl"));
    }

    @Test(groups = "division")
    public void verifyAllDivisionsFrench() throws IOException, JSONException {
        verifyDivisionsResponseWithJsonAssert(DIVISION_ALL_FR, ImmutableMap.of("site", "fr_nhl"));
    }

    @Test(groups = "division")
    public void verifyAllDivisionsSpanish() throws IOException, JSONException {
        verifyDivisionsResponseWithJsonAssert(DIVISION_ALL_ES, ImmutableMap.of("site", "es_nhl"));
    }

    /**
     * Comment from Jeremy, Jun 2, 2016: WE will get back to you on the conferences, but should be ok.
     * Related to issue with unexpected conference:
     * <p/>
     * {
     * "id": 7,
     * "name": "Unknown",
     * "link": "/api/v1/conferences/7",
     * "abbreviation": "WCH",
     * "shortName": "WCup",
     * "active": false
     * }
     */
    @Test(enabled = false)
    public void verifyInactiveConferencesEnglish() throws IOException, JSONException {
        verifyConferencesResponseWithJsonAssert(CONFERENCES_INACTIVE_EN, ImmutableMap.of("includeInactive", "true", "site", "en_nhl"));
    }

    @Test(enabled = false, description = "JIRA# QAA-1057")
    public void verifyInactiveConferencesFrench() throws IOException, JSONException {
        verifyConferencesResponseWithJsonAssert(CONFERENCES_INACTIVE_FR, ImmutableMap.of("includeInactive", "true", "site", "fr_nhl"));
    }

    /* Verifies the ordinals used for Periods and Plays in a 3OT game match those stored in the test class for the corresponding language.
     * Values are provided by CMS and any failures must be corresponded with that team in the event a value change is required
     * on our end. This is done both by passing a header and using a URL parameter.
     * URL hit with header: http://qa-statsapi.web.nhl.com/api/v1/game/2014030164/feed/live
     * URL hit with parameter: http://qa-statsapi.web.nhl.com/api/v1/game/2014030164/feed/live?language=xx
     */
    @Test(description = "JIRA# QAA-1057", groups = "3 Overtime")
    void verifyOrdinal3otGameLanguageEnHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "en_nhl"), null, String.format("%s/feed/live", "2014030164"), ordinalNumValuesEn);
    }

    @Test(groups = "3 Overtime")
    void verifyOrdinal3otGameLanguageEnUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "en"), String.format("%s/feed/live", "2014030164"), ordinalNumValuesEn);
    }

    @Test(groups = "3 Overtime")
    void verifyOrdinal3otGameLanguageEsHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "es_nhl"), null, String.format("%s/feed/live", "2014030164"), ordinalNumValuesEs);
    }

    @Test(groups = "3 Overtime")
    void verifyOrdinal3otGameLanguageEsUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "es"), String.format("%s/feed/live", "2014030164"), ordinalNumValuesEs);
    }

    @Test(groups = "3 Overtime")
    void verifyOrdinal3otGameLanguageFrHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "fr_nhl"), null, String.format("%s/feed/live", "2014030164"), ordinalNumValuesFr);
    }

    @Test(groups = "3 Overtime")
    void verifyOrdinal3otGameLanguageFrUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "fr"), String.format("%s/feed/live", "2014030164"), ordinalNumValuesFr);
    }

    /* Verifies the ordinals used for Periods and Plays in a SO game match those stored in the test class for the corresponding language.
     * Values are provided by CMS and any failures must be corresponded with that team in the event a value change is required
     * on our end. This is done both by passing a header and using a URL parameter.
     * URL hit with header: http://qa-statsapi.web.nhl.com/api/v1/game/2014030164/feed/live
     * URL hit with parameter: http://qa-statsapi.web.nhl.com/api/v1/game/2014030164/feed/live?language=xx
     */
    @Test(description = "JIRA# QAA-1057", groups = "shootout")
    void verifyOrdinalSoGameLanguageEnHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "en_nhl"), null, String.format("%s/feed/live", "2015020566"), ordinalNumValuesEn);
    }

    @Test(groups = "shootout")
    void verifyOrdinalSoGameLanguageEnUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "en"), String.format("%s/feed/live", "2015020566"), ordinalNumValuesEn);
    }

    @Test(groups = "shootout")
    void verifyOrdinalSoGameLanguageEsHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "es_nhl"), null, String.format("%s/feed/live", "2015020566"), ordinalNumValuesEs);
    }

    @Test(groups = "shootout")
    void verifyOrdinalSoGameLanguageEsUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "es"), String.format("%s/feed/live", "2015020566"), ordinalNumValuesEs);
    }

    @Test(groups = "shootout")
    void verifyOrdinalSoGameLanguageFrHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "fr_nhl"), null, String.format("%s/feed/live", "2015020566"), ordinalNumValuesFr);
    }

    @Test(groups = "shootout")
    void verifyOrdinalSoGameLanguageFrUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "fr"), String.format("%s/feed/live", "2015020566"), ordinalNumValuesFr);
    }

    /* Verifies the ordinals used for Periods and Plays in an OT game match those stored in the test class for the corresponding language.
     * Values are provided by CMS and any failures must be corresponded with that team in the event a value change is required
     * on our end. This is done both by passing a header and using a URL parameter.
     * URL hit with header: http://qa-statsapi.web.nhl.com/api/v1/game/2014030164/feed/live
     * URL hit with parameter: http://qa-statsapi.web.nhl.com/api/v1/game/2014030164/feed/live?language=xx
     */
    @Test(description = "JIRA# QAA-1057", groups = "1 overtime")
    void verifyOrdinalOtGameLanguageEnHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "en_nhl"), null, String.format("%s/feed/live", "2015020573"), ordinalNumValuesEn);
    }

    @Test(groups = "1 overtime")
    void verifyOrdinalOtGameLanguageEnUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "en"), String.format("%s/feed/live", "2015020573"), ordinalNumValuesEn);
    }

    @Test(groups = "1 overtime")
    void verifyOrdinalOtGameLanguageEsHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "es_nhl"), null, String.format("%s/feed/live", "2015020573"), ordinalNumValuesEs);
    }

    @Test(groups = "1 overtime")
    void verifyOrdinalOtGameLanguageEsUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "es"), String.format("%s/feed/live", "2015020573"), ordinalNumValuesEs);
    }

    @Test(groups = "1 overtime")
    void verifyOrdinalOtGameLanguageFrHeader() {
        verifyOrdinalNumParameter(
                ImmutableMap.of("X-Site", "fr_nhl"), null, String.format("%s/feed/live", "2015020573"), ordinalNumValuesFr);
    }

    @Test(groups = "1 overtime")
    void verifyOrdinalOtGameLanguageFrUrlParameter() {
        verifyOrdinalNumParameter(
                null, ImmutableMap.of("language", "fr"), String.format("%s/feed/live", "2015020573"), ordinalNumValuesFr);
    }

    @QTestCases(id = "42898")
    @Test
    public void verifyRussianTeamNamesForWorldCup() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_LEAGUE_ID_394_LANGUAGE_RU_DATA_FILE, ImmutableMap.of("leagueIds", "394", "language", "ru"), null);
    }

    @QTestCases(id = "42899")
    @Test
    public void verifyCurrentPeriodDataLocalisedForRussianAndFinnish() {
        JsonNode rawResponseFin = nhlStatsApiContentService.getGames(
                ImmutableMap.of("site", "fi_nhl", "skipCache", "true"), JsonNode.class, "2016020328/feed/live");
        JsonNode rawResponseRu = nhlStatsApiContentService.getGames(
                ImmutableMap.of("site", "ru_nhl", "skipCache", "true"), JsonNode.class, "2016020328/feed/live");

        JsonNode linescoreFin = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseFin).
                read("$..linescore");
        JsonNode linescoreRu = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseRu).
                read("$..linescore");


        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(linescoreFin.get(0).get("currentPeriodOrdinal").asText(), "JA ", String.format(
                "'currentPeriodOrdinal' is not 'JA': %s", linescoreRu.get(0).get("currentPeriodOrdinal").asText()));
        softAssert.assertTrue(
                linescoreFin.get(0).get("currentPeriodTimeRemaining").asText().equals("Lopputulos")
                        || linescoreFin.get(0).get("currentPeriodTimeRemaining").asText().equals("Tulos"),
                String.format("'currentPeriodTimeRemaining' is not 'Lopputulos': %s",
                        linescoreRu.get(0).get("currentPeriodTimeRemaining").asText()));

        softAssert.assertEquals(linescoreRu.get(0).get("currentPeriodOrdinal").asText(), "ОТ",
                String.format("'currentPeriodOrdinal' is not 'ОТ': %s",
                        linescoreRu.get(0).get("currentPeriodOrdinal").asText()));
        softAssert.assertEquals(linescoreRu.get(0).get("currentPeriodTimeRemaining").asText(), "Окончен",
                String.format("'currentPeriodTimeRemaining' is not 'Окончен': %s",
                        linescoreRu.get(0).get("currentPeriodTimeRemaining").asText()));

        softAssert.assertAll();
    }

    protected void verifyDivisionsResponseWithJsonAssert(String responseDataFile, Map parameters) throws IOException, JSONException {
        String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualResponse = nhlStatsApiContentService.getDivisions(parameters, String.class);

        JSONAssert.assertEquals(responseSnapshot, actualResponse, false);
    }

    protected void verifyConferencesResponseWithJsonAssert(String responseDataFile, Map parameters) throws IOException, JSONException {
        String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualResponse = nhlStatsApiContentService.getConferences(parameters, String.class);

        JSONAssert.assertEquals(responseSnapshot, actualResponse, false);
    }

    protected void verifyOrdinalNumParameter(Map headers, Map parameters, String pathSegment, List ordinalNumValues) {
        String gameResponse = nhlStatsApiContentService.getGames(headers, parameters, String.class, pathSegment);
        JsonElement gameObject = gson.fromJson(gameResponse, JsonElement.class);

        // temporary, for catching potential issue
        LOGGER.info("Response: " + gameResponse);

        SoftAssert softAssert = new SoftAssert();

        verifyPeriods(gameObject, ordinalNumValues, softAssert);
        verifyAllPlays(gameObject, ordinalNumValues, softAssert);

        softAssert.assertAll();
    }

    protected void verifyPeriods(JsonElement gameObject, List ordinalNumValues, SoftAssert softAssert) {
        JsonArray periods = gameObject.getAsJsonObject().get("liveData").getAsJsonObject().
                get("linescore").getAsJsonObject().get("periods").getAsJsonArray();

        for (JsonElement period : periods) {
            String ordinalNumValue = period.getAsJsonObject().get("ordinalNum").getAsString();
            softAssert.assertTrue(ordinalNumValues.contains(ordinalNumValue),
                    String.format("'ordinalNum' value '%s' is not valid for Periods", ordinalNumValue));
        }
    }

    protected void verifyAllPlays(JsonElement gameObject, List ordinalNumValues, SoftAssert softAssert) {
        JsonArray allPlays = gameObject.getAsJsonObject().get("liveData").getAsJsonObject().
                get("plays").getAsJsonObject().get("allPlays").getAsJsonArray();

        for (JsonElement play : allPlays) {
            String ordinalNumValue = play.getAsJsonObject().get("about").getAsJsonObject().get("ordinalNum").getAsString();
            softAssert.assertTrue(ordinalNumValues.contains(ordinalNumValue),
                    String.format("'ordinalNum' value '%s' is not valid for Plays", ordinalNumValue));
        }
    }

}
