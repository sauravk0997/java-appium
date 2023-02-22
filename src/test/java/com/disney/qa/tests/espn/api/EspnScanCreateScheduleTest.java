package com.disney.qa.tests.espn.api;

import com.disney.qa.api.espn.EspnApiCommon;
import com.disney.qa.api.espn.EspnContentProvider;
import com.disney.qa.api.espn.EspnContentServiceBuilder;
import com.disney.qa.api.espn.google.EspnGoogleSheets;
import com.disney.qa.api.espn.google.EspnGoogleSheetsService;
import com.disney.qa.tests.BaseAPITest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.api.services.sheets.v4.Sheets;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class EspnScanCreateScheduleTest extends BaseAPITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected EspnContentProvider espnContentProvider;

    private static Sheets serviceSheets;

    private static EspnGoogleSheets espnGoogleSheets;
    private static EspnApiCommon espnApiCommon;
    private List<String> eventsForTheDay = new LinkedList<>();
    private Map<String, String> eventsMapping = new HashMap<>();

    @BeforeClass
    public static void beforeClass() throws GeneralSecurityException, IOException {
        serviceSheets = EspnGoogleSheetsService.getSheetsService();
        espnGoogleSheets = new EspnGoogleSheets(serviceSheets);
        espnApiCommon = new EspnApiCommon();
    }

    @BeforeTest
    public void beforeTest() {
        espnContentProvider = EspnContentServiceBuilder.newInstance().withTemplateAndHost();
    }

    @Test(description = "Creating EOD Report Schedule for ESPN")
    public void createNewSchedule() {

        for (String item : fetchEvents().keySet()) {
            LOGGER.info("Event: " + item);
        }

        try {
            String sheetName = espnApiCommon.getDateForGames(1, "MM/dd/yy");
            espnGoogleSheets
                    .copyTemplateToNewSheet(sheetName, eventsMapping);

        } catch (Exception ex) {
            LOGGER.error("Checking Error: " + ex.getMessage(), ex);
            Assert.fail("EOD Report Failed To Generate Successfully.  Error Returned: " + ex.getMessage());
        }

    }

    private Map<String, String> fetchEvents() {

        Map<String, String> mapProperties = new HashMap<>();
        mapProperties.put("espn", "235");
        mapProperties.put("mlb", "14");
        mapProperties.put("mls", "231");
        mapProperties.put("nhl", "221");
        mapProperties.put("riot", "233");

        String dateToUse = espnApiCommon.getDateForGames(1, "yyyy-MM-dd");
        String dateToUseEnd = espnApiCommon.getDateForGames(2, "yyyy-MM-dd");
        String endDateToConvert = dateToUseEnd + "T03:59:59Z";
        String beginDateToConvert =dateToUse + "T04:00:00Z";
        String bodyData = "{\"endDate\": \"" + endDateToConvert + "\", \"offset\": 0, \"pageSize\": 1000, \"startDate\": \"" + beginDateToConvert + "\"}";

        for (String prop : mapProperties.values()) {

            JsonNode schedule = espnContentProvider
                    .postRetrieveSchedule(bodyData, JsonNode.class, String.format("%s/events/search", prop));

            LOGGER.info("Grabbing Schedule: " + schedule.toString());

            List<JsonNode> scheduleList = schedule.findValues("events");

            for (JsonNode node : scheduleList.get(0)) {
                Calendar calendar = Calendar.getInstance();
                TimeZone easternTimeZone = TimeZone.getTimeZone("America/New_York");

                calendar.setTimeZone(easternTimeZone);
                calendar.setTime(DateTime.parse(node.findPath("startDate").asText()).toDate());

                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                formatTime.setTimeZone(easternTimeZone);

                String convertedDateTime = formatTime.format(calendar.getTime());

                scanForValidContentId(node, convertedDateTime, dateToUse);
            }
        }

        Collections.sort(eventsForTheDay);
        eventsMapping = new TreeMap<>(eventsMapping);

        return eventsMapping;
    }

    private void scanForValidContentId(JsonNode node, String formatDateTime, String dateToUse) {

        List<JsonNode> contentList = node.findValues("contentId");

        for (JsonNode content : contentList) {
            try {
                LOGGER.info(String.format("Scanning Event (%s) for Inclusion...", node.findPath("programTitle").asText()));
                URI uri = new URI(String.format("https://s3.amazonaws.com/search-espn-ingest-us-east-1-prod/incoming/epg/Airing/contentId/%s.json", content.asText()));
                ArrayNode titles = espnApiCommon.jsonContext().parse(uri.toURL().openStream()).read("$.tags.[?(@.type== 'name')].value");
                String combinedTitle = String.format("%s %s", formatDateTime, buildTitle(titles, node.findPath("programTitle").asText()).replace(dateToUse + " - ", ""));

                if (!eventsMapping.containsKey(combinedTitle)) {
                    LOGGER.info("ADDING EVENT: " + combinedTitle);
                    eventsMapping.put(combinedTitle, content.asText());
                }

            } catch (Exception ex) {
                LOGGER.error("Skipping Event...no Valid Content, checked: " + content.asText(), ex);
            }
        }
    }

    private String buildTitle(ArrayNode titles, String eventName) {
        String combinedTitle = "";
        for (JsonNode name : titles) {
            if (name != null && !combinedTitle.contains(name.asText())) {
                combinedTitle = combinedTitle + name.asText() + " ";
            }
        }

        if (combinedTitle.isEmpty()) {
            return eventName;
        }

        return combinedTitle;
    }
}
