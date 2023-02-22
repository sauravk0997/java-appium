package com.disney.qa.api.nhl.statsapi;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.util.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.qaprosoft.carina.core.foundation.utils.common.CommonUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class NhlPlayoffsDataCapture {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String NHL_STATSAPI_HOST = NhlParameters.getNhlStatsApiHost();

    private static final String TODAY_DATE = getTodayDateInNhlformat();

    private static final String PREFIX = "$..schedule..[?(@.date=='";
    private static final String SUFFIX_SCHEDULE = "')]..gameDate";
    private static final String SUFFIX_GAMESTATE = "')]..abstractGameState";

    private static final String FILE_DIRECTORY = "PlayoffData/";

    private static final String PREVIEW = "Preview";
    private static final String LIVE = "Live";
    private static final String FINAL = "Final";

    private RestTemplate restTemplate;
    protected Configuration jsonPathJacksonConfiguration;

    public NhlPlayoffsDataCapture() {
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();
        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    public void capturePlayoffData(){
        String apiPath = NHL_STATSAPI_HOST + "/api/v1/tournaments/playoffs?" +
                "season=20182019&expand=round.series,series.schedule,schedule." +
                "game.seriesSummary,schedule.broadcasts.all,schedule.linescore," +
                "schedule.teams,team.playoffs&rounds=&seriesCodes=&teamId=";

        String jsonPath = PREFIX + TODAY_DATE + SUFFIX_SCHEDULE;


        LOGGER.info("Checking API Path: " + apiPath);

        JsonNode rawResponse = restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
        LOGGER.info("Response: " + rawResponse);

        JsonNode gameStartTime = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPath);

        LinkedList<String> startTimes = new LinkedList<>();

        if (gameStartTime.size() >= 1) {
            for (JsonNode node : gameStartTime) {
                startTimes.add(node.asText());
            }
            LOGGER.info("Found startTimes: " + startTimes.toString());
            verifyGameStatus(rawResponse, startTimes, apiPath);
        }
        else
            LOGGER.info("No Playoff games today.");
    }

    private void verifyGameStatus(JsonNode mainBody, List<String> startTimes, String apiPath){
        LinkedList<String> gameStates = getGameStateList(apiPath);

        boolean file = new File(dirPath()).mkdirs();
        if(file){
            LOGGER.info("New Directory created: " + dirPath());
        }
        else {
            LOGGER.info("Required directory already exists");
        }

        LOGGER.info("Found abstractGameStates: " + gameStates);

        do {
            while(gameStates.contains(LIVE)) {
                LOGGER.info("Live state found! Capturing data responses.");
                captureLiveData(mainBody);
                mainBody = refreshMainBodyResponse(apiPath);
                gameStates.clear();
                gameStates = getGameStateList(apiPath);
                LOGGER.info("Current Game States: " + gameStates);
            }

            if(gameStates.contains(FINAL)){
                LOGGER.info("Final game state found. Capturing post-game response.");
                captureFinalData(mainBody);
            }

            if(gameStates.contains(PREVIEW)) {
                LOGGER.info("Preview state found! Setting wait time before next attempt.");
                pauseForPreviewData(startTimes.get(gameStates.indexOf(PREVIEW)));
                gameStates.clear();
                mainBody = refreshMainBodyResponse(apiPath);
                gameStates = getGameStateList(apiPath);
                LOGGER.info("Current Game States: " + gameStates);
            }

        } while (gameStates.contains(PREVIEW) || gameStates.contains(LIVE));
    }

    private JsonNode refreshMainBodyResponse(String apiPath){
        return restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
    }

    private LinkedList<String> getGameStateList(String apiPath){
        String gameStatePath = PREFIX + TODAY_DATE + SUFFIX_GAMESTATE;

        JsonNode rawResponse = restTemplate.getForEntity(apiPath, JsonNode.class).getBody();
        JsonNode abstractGameStates = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(gameStatePath);

        LinkedList<String> gameStates = new LinkedList<>();
        for (JsonNode node: abstractGameStates){
            gameStates.add(node.asText());
        }

        return gameStates;
    }

    private void captureLiveData(JsonNode mainBody){
        //WriteFiles
        String captureBody = mainBody.toString();

        captureBody = formatToJson(captureBody);

        FileUtils.appendContentToFile(fileName(), captureBody,false);
        CommonUtils.pause(300);
    }

    private void captureFinalData(JsonNode mainBody){
        //WriteFiles
        String captureBody = mainBody.toString();

        captureBody = formatToJson(captureBody);

        FileUtils.appendContentToFile(fileName(), captureBody,false);
    }

    private void pauseForPreviewData(String startTime){
        LOGGER.info("Start Time: " + startTime.substring(11, startTime.length()-1));
        LocalDateTime eventDate = LocalDateTime.parse(startTime.substring(0, startTime.length()-1));
        Seconds seconds = Seconds.secondsBetween(LocalDateTime.parse(getCurrentDateInGMTformat() + "T" + getCurrentTimeInNhlFormat()), eventDate);
        if(seconds.getSeconds() > 0) {
            LOGGER.info("Time between Current Time and Start time: " + seconds.getSeconds());
            LOGGER.info("Sleeping system until established time and trying again.");
            CommonUtils.pause(seconds.getSeconds());
        } else {
            LOGGER.info("Scheduled start time has already been met. Waiting for data to be turned on. Sleeping for 60 seconds and trying again.");
            CommonUtils.pause(60);
        }
    }

    /** Helpers > get today's date **/
    //GMT timezone guarantees the "currentTime" matches the device's time (needs to be set manually)
    private static String getTodayDateInNhlformat(){
        DateTime dateTime = new DateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("EST")));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        String todayDate = fmt.print(dateTime);
        LOGGER.info("Current date (EST): " + todayDate);

        return todayDate;
    }

    private static String getCurrentDateInGMTformat(){
        DateTime dateTime = new DateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT")));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        String gmtDate = fmt.print(dateTime);
        LOGGER.info("Current date (GMT): " + gmtDate);

        return gmtDate;
    }

    private static String getCurrentTimeInNhlFormat(){
        DateTime dateTime = new DateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT")));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
        String curTime = fmt.print(dateTime);
        LOGGER.info("Current Time (GMT): " + curTime);

        return curTime;
    }

    private String formatToJson(String captureBody){
        JsonParser parser = new JsonParser();
        JsonObject rawData = parser.parse(captureBody).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(rawData);
    }

    private String dirPath(){
        return FILE_DIRECTORY + "/" + TODAY_DATE +"/";
    }

    private String fileName(){
        return FILE_DIRECTORY + "/" + TODAY_DATE +"/Playoffs_" + TODAY_DATE +"_"+ getCurrentTimeInNhlFormat() + ".json";
    }
}