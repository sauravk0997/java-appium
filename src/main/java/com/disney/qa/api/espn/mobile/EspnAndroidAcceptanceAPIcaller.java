package com.disney.qa.api.espn.mobile;

import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

public class EspnAndroidAcceptanceAPIcaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ESPN_WATCH_API_HOST = "http://watch.product.api.espn.com/";
    private RestTemplate restTemplate;
    private static final String TODAY_DATE = getTodaysDateInESPNformatt();

    protected Configuration jsonPathJacksonConfiguration;

    public EspnAndroidAcceptanceAPIcaller() {
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();
        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    public List<String> checkForAllContent() {
        LinkedList<String> sectionHeaders = new LinkedList<>();
        JsonNode rawResponse = restTemplate.getForEntity(ESPN_WATCH_API_HOST +
                "/api/product/v3/mobile/espn/espnplus?countryCode=US&lang=en&tz=America%2FNew_York&deviceType=handset&zipcode" +
                "=07044&entitlements=1000000000005%2CESPN_PLUS&authStates=mvpd_login%2Cmvpd_previous&authNetworks=espn1%2Cespn2%2Cespn3" +
                "%2Cespnu%2Cespnews%2Csec%2Clonghorn%2Cgoalline", JsonNode.class).getBody();
        LOGGER.info("Response: " + rawResponse);

        JsonNode datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..buckets[*].name");

        if (datesNodeList.size() >= 1) {
            for (JsonNode node : datesNodeList) {
                sectionHeaders.add(node.asText());
            }
        }

        LOGGER.info("Found sectionHeaders: " + sectionHeaders.toString());

        return sectionHeaders;
    }

    /**
     * API call > retrieves all tile 'sub-titles'
     **/
    public List<String> checkForSubtitleContent(String subTitle) {
        LinkedList<String> desiredContent = new LinkedList<>();
        JsonNode rawResponse = restTemplate.getForEntity(ESPN_WATCH_API_HOST +
                "/api/product/v3/mobile/espn/espnplus?countryCode=US&lang=en&tz=America%2FNew_York&deviceType=handset&zipcode" +
                "=07044&entitlements=1000000000005%2CESPN_PLUS&authStates=mvpd_login%2Cmvpd_previous&authNetworks=espn1%2Cespn2%2Cespn3" +
                "%2Cespnu%2Cespnews%2Csec%2Clonghorn%2Cgoalline", JsonNode.class).getBody();
        LOGGER.info("Response: " + rawResponse);

        JsonNode datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..contents[*].subtitle");

        if (datesNodeList.size() >= 1) {
            for (JsonNode node : datesNodeList) {
                if (node.asText().contains(subTitle)) {
                    desiredContent.add(node.asText());
                }
            }
        }

        LOGGER.info("Found packages: " + desiredContent.toString());

        return desiredContent;
    }

    /**
     * API call > retrieves all content sub-titles based on only one specified package
     **/
    public List<String> checkForContentNameBasedOnPackages(String contentType, String... packageName) {
        LinkedList<String> desiredContent = new LinkedList<>();

        JsonNode rawResponse = restTemplate.getForEntity(
                ESPN_WATCH_API_HOST + "/api/product/v3/mobile/espn/espnplus?countryCode=US&lang=en&tz=" +
                        "America/New_York&deviceType=handset&zipcode=10011&authStates=&authNetworks=&entitlements=ESPN_PLUS, ESPN_PLUS_NHL,ESPN_PLUS_MLB,ESPN_EXEC",
                JsonNode.class).getBody();

        LOGGER.info("Response: " + rawResponse);

        //this can be used with either way from above

        JsonNode datesNodeList;

        for (String str : packageName) {
            datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..contents[?(@.streams[0].packages[0] == '" +
                    str + "' && @.streams[0].packages.length() < 2 && @.date == '" + TODAY_DATE + "' && @.status == '" + contentType + "')].name");

            if (datesNodeList.size() >= 1) {
                for (JsonNode node : datesNodeList) {
                    desiredContent.add(node.asText());
                }
            }
        }

        LOGGER.info("Content found: " + desiredContent.toString());

        return desiredContent;
    }

    /**
     * API call > returns section names with either "see all" option or not depending on param supplied
     **/
    public List<String> checkForSectionTitlesWithOrWithoutSeeAllOption(boolean withSeeAllOption) {
        LinkedList<String> desiredContent = new LinkedList<>();

        JsonNode rawResponse = restTemplate.getForEntity(
                ESPN_WATCH_API_HOST + "/api/product/v3/mobile/espn/espnplus?countryCode=US&lang=en&tz=" +
                        "America/New_York&deviceType=handset&zipcode=10011&authStates=&authNetworks=&entitlements=ESPN_PLUS, ESPN_PLUS_NHL,ESPN_PLUS_MLB,ESPN_EXEC",
                JsonNode.class).getBody();

        LOGGER.info("Response: " + rawResponse);

        //this can be used with either way from above

        JsonNode datesNodeList;

        if (withSeeAllOption) {
            datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..buckets[*][?(@.links.self)].name");
        }else{
            datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..buckets[*][?(!@.links.self)].name");
        }

        if (datesNodeList.size() >= 1) {
            for (JsonNode node : datesNodeList) {
                desiredContent.add(node.asText());
            }
        }

        LOGGER.info("Content found: " + desiredContent.toString());

        return desiredContent;
    }

    /**
     * API call > retrieves all content packages
     **/
    public List<String> checkForVODcontentNameBasedOnPackages(String contentType, boolean withDate, String... packageName) {
        LinkedList<String> desiredContent = new LinkedList<>();

        JsonNode rawResponse = restTemplate.getForEntity(
                ESPN_WATCH_API_HOST + "/api/product/v3/mobile/espn/espnplus?countryCode=US&lang=en&tz=" +
                        "America/New_York&deviceType=handset&zipcode=10011&authStates=&authNetworks=&entitlements=ESPN_PLUS,ESPN_PLUS_NHL,ESPN_PLUS_MLB,ESPN_EXEC",
                JsonNode.class).getBody();

        LOGGER.info("Response: " + rawResponse);

        //this can be used with either way from above

        JsonNode datesNodeList;

        for (String str : packageName) {
            if (withDate) {
                datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..contents[?(@.streams[0].packages[0] == '" +
                        str + "' && @.date == '" + TODAY_DATE + "' && @.status == '" + contentType + "')].name");
            }else{
                datesNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..contents[?(@.streams[0].packages[0] == '" +
                        str + "' && @.status == '" + contentType + "')].name");
            }

            if (datesNodeList.size() >= 1) {
                for (JsonNode node : datesNodeList) {
                    desiredContent.add(node.asText());
                }
            }
        }

        LOGGER.info("Content found: " + desiredContent.toString());

        return desiredContent;
    }

    /**
     * API call > get all non-metada titles
     *
     **/
    public List<String> getNonPlayableAssets() {

        LinkedList desiredContent = new LinkedList<>();

        JsonNode rawResponse = restTemplate.getForEntity(
                ESPN_WATCH_API_HOST + "/api/product/v3/mobile/espn/espnplus?countryCode=US&lang=en&tz=" +
                        "America/New_York&deviceType=handset&zipcode=10011&authStates=&authNetworks=&entitlements=ESPN_PLUS,ESPN_PLUS_NHL,ESPN_PLUS_MLB,ESPN_EXEC",
                JsonNode.class).getBody();

        LOGGER.info("Response: " + rawResponse);

        //TODO: check if its possible to get these titles by date, otherwise, title retreived may not be available for Watch tab
        JsonNode nodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..buckets[?(@.contents[0].imageFormat == '5x2')].name");

        if(nodeList.size() > 1) {
            for(JsonNode node : nodeList) {
                desiredContent.add(node.asText());
            }
        }

        return desiredContent;
    }

    /** Helpers > get today's date **/
    private static String getTodaysDateInESPNformatt(){
        DateTime dateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE, MMMM dd");
        String todayDate = fmt.print(dateTime);
        LOGGER.info("Today's date: " + todayDate);

        return todayDate;
    }
}
