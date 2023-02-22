#Documentation

##Links:

NHL Stats API documenation: http://qa-statsapi.web.nhl.com/docs/
    
JSON Path: https://github.com/jayway/JsonPath

JSON Path Online Evaluator: http://jsonpath.herokuapp.com/

JSON Schema generator online: http://jsonschema.net/

Automated tickets by months: https://docs.google.com/spreadsheets/d/1glGZaGmPZ-ooR_j-oNUcyTmAUv_vC7nMHy0fwi13x8k/edit#gid=0

##Environments:

DEV: http://dev-statsapi.web.nhl.com/

QA: http://qa-statsapi.web.nhl.com/

PROD: http://statsapi.web.nhl.com/

##Code examples

Comapre reponse with JSON stored in file

```
    public void verifySeasonParameter() throws IOException, JSONException {
        String rawResponse = arenaContentService.getSchedule(ImmutableMap.of("season", "2015"), String.class);
        String responseSnapshot = fileUtils.getResourceFileAsString("arena/api/schedule/schedule_season_2015.json");

        JSONAssert.assertEquals(responseSnapshot, rawResponse, JSONCompareMode.STRICT);
    }
```

Jackson classes and JSON Path

```java
    protected void verifyRosterSeasonHydrate(Map parameters, String teamId, SoftAssert softAssert) {
        JsonNode rawResponseActual = nhlStatsApiContentService.getStandings(parameters, JsonNode.class);
        JsonNode rawResponseExpected = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20142015"), JsonNode.class, String.format("%s/roster", teamId));

        JsonNode rosterActual = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).
                read(String.format(
                        "$..records[?(@.standingsType == 'byDivision')].teamRecords[?(@.team.id == %s)]..roster.roster", teamId));
        JsonNode rosterExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseExpected).
                read("$..roster");

        List rosterActualList = Lists.newArrayList(rosterActual.get(0).iterator());
        List rosterExpectedList = Lists.newArrayList(rosterExpected.get(0).iterator());
        
        Collections.sort(rosterActualList, getRosterPersonComparator());
        Collections.sort(rosterExpectedList, getRosterPersonComparator());

        softAssert.assertEquals(rosterActualList, rosterExpectedList, "Rosters are not the same");
    }
```

JSON Path and strings

```java
    public void verifySeasonDivision() {
        String rawResponse1 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "division"), String.class, "3");
        String rawResponse2 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "division", "season", "20012002"), String.class, "3");
        String rawResponse3 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "division", "season", "19881989"), String.class, "3");
        String rawResponse4 = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2001-11-06", "hydrate", "team(division)"), String.class);

        List<String> division1 = JsonPath.read(rawResponse1, "$..division.name");
        List<String> division2 = JsonPath.read(rawResponse2, "$..division.name");
        List<String> division3 = JsonPath.read(rawResponse3, "$..division.name");
        List<String> division4 = JsonPath.read(rawResponse4, "$..team[?(@.id==3)].division.name");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(division1.get(0), "Metropolitan", String.format("Incorrect division: %s", division1));
        softAssert.assertEquals(division2.get(0), "Atlantic", String.format("Incorrect division: %s", division2));
        softAssert.assertEquals(division3.get(0), "Patrick", String.format("Incorrect division: %s", division3));
        softAssert.assertEquals(division4.get(0), "Atlantic", String.format("Incorrect division: %s", division4));

        softAssert.assertAll();
    }
```

JSON Path and document class for cases when it's necessary to read data several times

```java
    @TestRailCases(testCasesId = "C596301")
    @Test
    public void verifyPlayerDraftInformation() throws IOException, JSONException {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "draft"), String.class, "8470594");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<String> draftTeamId = JsonPath.read(document, "$..draft[*].team.id");
        List<String> draftTeamName = JsonPath.read(document, "$..draft[*].team.name");
        List<String> draftTeamLink = JsonPath.read(document, "$..draft[*].team.link");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(draftTeamId.size() != 0, "'draft' -> 'team' -> 'id' is absent");
        softAssert.assertTrue(draftTeamName.size() != 0, "'draft' -> 'team' -> 'name' is absent");
        softAssert.assertTrue(draftTeamLink.size() != 0, "'draft' -> 'team' -> 'link' is absent");
        softAssert.assertAll();
    }
```

JSON Schema validation

```
    public void verifyAdditionalBioHydrate() throws IOException, ProcessingException {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "additionalBio"), String.class, "8473541");
        String jsonSchema = fileUtils.getResourceFileAsString("nhl/api/people/schema/people_additional_bio_schema.json");

        SoftAssert softAssert = new SoftAssert();
        
        jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse, "team", softAssert);
        
        softAssert.assertAll();
    }
    
    public void validateJsonAgainstSchema(String jsonSchema, String jsonToValidate, String nodeNameToValidate, SoftAssert softAssert) throws IOException, ProcessingException {
        JsonNode schema = JsonLoader.fromString(jsonSchema);
        JsonNode jsonNode = JsonLoader.fromString(jsonToValidate);

        if (null != nodeNameToValidate) {
            jsonNode = jsonNode.findValue(nodeNameToValidate);
        }

        com.github.fge.jsonschema.main.JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
        ProcessingReport processingReport = jsonValidator.validate(schema, jsonNode, true);

        softAssert.assertTrue(processingReport.isSuccess(), processingReport.toString());
    }
```

Direct call of REST Servise using Rest Template

```
    @DataProvider(name = "invalidNhlRequests")
    public Object[][] invalidNhlRequestsDataProvider() {
        return new Object[][]{
                {"/api/v1/performers/5171"},
                {"/api/v1/venues"},
                {"/api/v1/schedule/events?startDate=10/1/2016&endDate=11/1/2016"},
        };
    }

    @TestRailCases(testCasesId = "C825733")
    @Test(dataProvider = "invalidNhlRequests", expectedExceptions = HttpServerErrorException.class, expectedExceptionsMessageRegExp = "500 Internal Server Error")
    public void verifyErrorsWhenArenaDataRequestedFromNhl(String request) {
        restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, String.class);
    }
```

##Test Cases

###playoff tests - tournament endpoint

https://www.pivotaltracker.com/projects/1427108/stories/117189055

there's a new endpoint 'tournament' used to return data for special tournaments (in this case, the NHL hockey playoffs.  it would be good to have additional tests for this endpoint and the special expands.  since 20152016 playoffs are just getting started and 20142015 are being used for development, we may want to use 20132014.  

expands:
round.series - the tournament rounds are returned by default, but there is an expand to ge the series details.
series.schedule - returns all the games for a series.

parameters:
seriesCodes - only returns information for a subset of series (seriesCodes: A,B,C,D,E,F,G,H,I,J,K,L,M,O)
rounds - only returns information for certain rounds (rounds: 1,2,3,4)

example call:
https://qa-statsapi.web.nhl.com/api/v1/tournaments/playoffs?expand=round.series,series.schedule&season=20132014

https://qa-statsapi.web.nhl.com/api/v1/tournaments/playoffs?expand=round.series,series.schedule&season=20132014&rounds=1

###playoff tests - schedule expands

https://www.pivotaltracker.com/n/projects/1427108

for playoff games, there are addition expands available in the schedule to return series information, series status, etc.  since this data is currently in flux for 20152016 and 20142015 is being used for testing, it may make sense to use 20132014 data for this.  we'd like to include the postseason expands as part of the automated tests.

schedule.game.seriesSummary - returns information about the series for a particular game (such as what number the game is in the series)
seriesSummary.series - returns addition series information, such as the teams and their seeding
series.round - returns information on the round the series is in.

the NHL playoffs run from the mid of april into june.

https://qa-statsapi.web.nhl.com/api/v1/schedule?expand=schedule.game.seriesSummary,seriesSummary.series,series.round&date=04/19/2014

###team tests - Create automated tests for retrieving teams by league ID

Please create automated tests for the new feature to retrieve teams by leagueId.
Examples:
api/v1/teams?leagueIds=394 - will pull back all teams for world cup of hockey league
if no "leagueIds" parameter is supplied, the default league of 133 (NHL) will be returned. This is just a listing of all of the active teams in the NHL.
The leagueIds parameter can be used with the season parameter. For example, this returns
the current World Cup of Hockey Teams for the 2016-2017 season.
/api/v1/teams?leagueIds=394&season=20162017
If there are no teams for that league in that season, none will be returned, as below.
/api/v1/teams?leagueIds=394&season=20152016
If the leagueIds parameter is used with teamId it will be IGNORED and the teams will be returned, regardless of their league
api/v1/teams?leagueIds=394&teamId=1,2,3,4

https://jira.mlbam.com/browse/SDAPINHL-334

###Test quoting of hockey stats
https://jira.mlbam.com/browse/SDAPINHL-350

###Check for schedule metadata node
https://jira.mlbam.com/browse/SDAPINHL-496
