package com.disney.qa.api.dgi.validationservices.hora;

import com.disney.util.HARUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.registrar.Artifact;
import net.lightbody.bmp.BrowserMobProxy;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

import org.testng.asserts.SoftAssert;
import com.jayway.jsonpath.JsonPath;

public class HoraValidator {
    protected final RestTemplate restTemplate = new RestTemplate();
    private final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    private final HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
    private Boolean isValid;
    private boolean allPassed = true;
    private String failureMessage;
    private ArrayList<String> failedUrl = new ArrayList<>();

    private BrowserMobProxy proxy;

    private JSONArray refinedInput;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String sourceString = "source";
    private static String clientString = "client";
    private static String eventString = "event";
    private static String[] eventFilters = new String[]{"urn:dss:event:glimpse:impression:containerView", "urn:dss:event:glimpse:impression:pageView", "urn:dss:event:glimpse:engagement:interaction", "urn:dss:event:glimpse:engagement:input", "urn:dss:telemetry-service:event:stream-sample", "urn:bamtech:api:stream-sample", "urn:dss:event:client:playback:event:v1", "urn:dss:event:client:playback:startup:v1", "urn:dss:event:client:playback:heartbeat:v1", "urn:dss:event:client:playback:snapshot:v1"};

    public HoraValidator(BrowserMobProxy proxy, String fileType) {
        this.proxy = proxy;
        if (proxy.getHar() != null) {
            if (!proxy.getHar().getLog().getEntries().isEmpty()) {
                isValid = validate(fileType);
            } else {
                Assert.fail("Har file is empty...");
            }
        } else {
            Assert.fail("Validator is not receiving proxy data...");
        }
    }

    public boolean validate(String fileType) {
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);
        JSONParser parser = new JSONParser();
        try {
            if (fileType.equals("har")) {
                JSONObject input = (JSONObject) parser.parse(new HARUtils(proxy).toString());
                refinedInput = filterHar(input);
            } else {
                failureMessage = "traffic log type unrecognized";
                return false;
            }

            getSessionInfo(refinedInput);

            ResponseEntity<String> response = horaInteraction(refinedInput);

            JSONObject responseBody = (JSONObject) parser.parse(response.getBody());

            allPassed = checkPassFail(responseBody);
        } catch (IOException | ParseException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
        return allPassed;
    }

    public ResponseEntity<String> horaInteraction(JSONArray refinedInput) throws IOException, URISyntaxException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("refinedInput.json"))) {
            writer.write(refinedInput.toString());
        }
        String inputName = "RefinedInput-" + new Date().toString().replaceAll("[^a-zA-Z0-9+]", "-") + ".json";
        Artifact.attachToTest(inputName, Paths.get("refinedInput.json"));
        RequestEntity<String> request = new RequestEntity<>(refinedInput.toString(),
                new HttpHeaders(), HttpMethod.POST, new URI(R.TESTDATA.get("hora_prod_url")));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        LOGGER.info("Response code is: {}", response.getStatusCode());
        try( BufferedWriter writer2 = new BufferedWriter(new FileWriter("horaResponse.json"))){
            writer2.write(response.getBody());
        }
        String responseName = "HoraResponse-" + new Date().toString().replaceAll("[^a-zA-Z0-9+]", "-") + ".json";
        Artifact.attachToTest(responseName, Paths.get("horaResponse.json"));
        return response;
    }

    public boolean checkPassFail(JSONObject responseBody) {
        if (responseBody.containsKey("successes")) {
            JSONArray responseSuccesses = (JSONArray) responseBody.get("successes");
            checkSuccesses(responseSuccesses);
        }
        if (responseBody.containsKey("failures")) {
            JSONArray responseFailures = (JSONArray) responseBody.get("failures");
            checkFailures(responseFailures);
        }
        failureMessage = "The following endpoints failed: ";
        if (!failedUrl.isEmpty()) {
            for (int j = 0; j < failedUrl.size(); j++) {
                failureMessage = failureMessage.concat(failedUrl.get(j) + ", ");
            }
        }

        if (!allPassed) {
            allPassed = checkEndpoints(failedUrl);
        }
        return allPassed;
    }

    public void checkSuccesses(JSONArray responseSuccesses) {
        ArrayList<String> sucessList = new ArrayList<>();
        for (int i = 0; i < responseSuccesses.size(); i++) {
            JSONObject success = (JSONObject) responseSuccesses.get(i);
            if (Objects.equals(success.get(sourceString).toString(), "Sdp")) {
                sucessList.add("SDP passed");
            } else if (Objects.equals(success.get(sourceString).toString(), "EdsMl")) {
                sucessList.add(String.format("%s passed", success.get("name").toString()));
            }
        }
        sucessList.forEach(LOGGER::info);
    }

    public void checkFailures(JSONArray responseFailures) {
        ArrayList<String> failList = new ArrayList<>();
        for (int i = 0; i < responseFailures.size(); i++) {
            JSONObject failure = (JSONObject) responseFailures.get(i);
            if (Objects.equals(failure.get(sourceString).toString(), "Sdp")) {
                allPassed = false;
                failedUrl.add("Sdp");
                failList.add("Sdp");
            } else if (Objects.equals(failure.get(sourceString).toString(), "EdsMl")) {
                allPassed = false;
                JSONObject validationManifest = (JSONObject) failure.get("validationManifest");
                failedUrl.add(validationManifest.get("url").toString());
                failList.add(String.format("%s failed", validationManifest.get("url")));
            }
        }
        failList.forEach(LOGGER::info);
    }

    /**
     * this method filters the json data from the logs as it typically contains both unnecessary network info
     * surrounding the playload and events that we are not interested in validating.
     * @param input The raw har file that the proxy gives
     * @return returns a JsonArray of the events that meet the criteria, in the relevant format
     * @throws IOException
     * @throws ParseException
     */
    public JSONArray filterHar(JSONObject input) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray results = new JSONArray();
        String postText = JsonPath.parse(input).read("$.log.entries.*.request[?(@.method=='POST')].postData.text").toString();
        JSONArray postArray = (JSONArray) parser.parse(new StringReader(postText));
        for (int i = 0; i < postArray.size(); i++) {
            if ((postArray.get(i).toString().length() > 0) && (String.valueOf(postArray.get(i).toString().charAt(0)).equals("["))) { //to prevent bad request formats
                JSONArray eventArray = (JSONArray) parser.parse(new StringReader(postArray.get(i).toString()));
                results.addAll(filterByEvent(eventArray));
            }
        }
        Assert.assertTrue((!results.isEmpty()), "No events found");
        return results;
    }

    public JSONArray filterByEvent(JSONArray eventArray) {
        JSONArray results = new JSONArray();
        for (int j = 0; j < eventArray.size(); j++) {
            JSONObject events = (JSONObject) eventArray.get(j);
            if (events.containsKey(clientString)) {
                JSONObject client = (JSONObject) events.get(clientString);
                if (client.containsKey(eventString)) {
                    String eventType = (String) client.get(eventString);
                    if (Arrays.asList(eventFilters).contains(eventType)) {
                        results.add(events);
                    }
                }
            }
        }
        return results;
    }

    public void getSessionInfo(JSONArray input) {
        String sessionId;
        String appVersion;
        for (int k = 0; k < input.size(); k++) {
            JSONObject event = (JSONObject) input.get(k);
            JSONObject client = (JSONObject) event.get(clientString);
            if (client.containsKey("data")) {
                JSONObject data = (JSONObject) client.get("data");
                appVersion = (String) data.get("appVersion");
                sessionId = (String) data.get("activitySessionId");
                LOGGER.info("Session id is {}", sessionId);
                LOGGER.info("App Version is {}",  appVersion);
                break;
            }
        }
    }

    public Boolean checkEndpoints(List<String> failedUrls) {
        Boolean importantFailures = false;
        for (int k = 0; k < failedUrls.size(); k++) {
            if ((!failedUrls.get(k).contains("telemetry")) && (!failedUrls.get(k).contains("sequence"))) {
                importantFailures = true;
            }
        }
        return !importantFailures;
    }

    public Boolean getValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return failureMessage;
    }

    public void assertValidation(SoftAssert softAssert) {
        softAssert.assertTrue(getValid(), getErrorMessage());
    }

    /* This is the main method for validating all of the requirements stored in the EventCheckList, typically for pqoe
    events. Returns true only if all requirements are contained. Requirements must be in the checklist before running.*/
    public void checkListForPQOE(SoftAssert softAssert, JSONArray checkList) {
        boolean result = true;
        ArrayList errorMessages = new ArrayList();
        //long lowestTime = 999999; To be readded when solution for timing is found
        for (int i =0; i<checkList.size(); i++){
            EventChecklist checkListEntry = (EventChecklist) checkList.get(i);
            String checkListEventType = checkListEntry.getEventType();
            boolean foundMatch = false;
            boolean isMatch;
            for (int j =0; j<refinedInput.size();j++){
                isMatch = false;
                JSONObject event = (JSONObject) refinedInput.get(j);
                JSONObject client = (JSONObject) event.get("client");
                String eventType = client.get("event").toString();

                if (checkListEventType.equals(eventType)){
                    JSONObject data = (JSONObject) client.get("data");
                    isMatch = checkListByEntry(data, checkListEntry.getRequirements());
                }
                if (isMatch){
                    foundMatch = true;
                }
            }
            if (!foundMatch){
                result = false;
                errorMessages.add(String.format("No event of type %s with requirements %s", checkListEventType,
                        checkListEntry.getRequirements().toString()));
                //Write a better error message here
            }
        }
        softAssert.assertTrue(result, errorMessages.toString());
    }
    /*Method takes in an event and compares it against all parameters of a single requirement.
    eventData is a singular event's data field while checkListRequirements is parameters for a single requirement.
    Returns true if the event*/
    public boolean checkListByEntry(JSONObject eventData, JSONArray checkListRequirements){
        for (int i=0; i<checkListRequirements.size(); i++){
            JSONObject requirement = (JSONObject) checkListRequirements.get(i);
            if(!checkListRequirements(eventData,requirement)){
                return false;
            }
        }
        return true;
    }

    /* Checks a single event against a single parameter of a requirement, returns true if the required parameter is
    contained. This is the part that determines which type of check to use,
    see below for more information on the respective types. */
    public boolean checkListRequirements(JSONObject eventData, JSONObject eventRequirement){
        String elementCheckType = eventRequirement.get("type").toString();
        String elementCheckName = eventRequirement.get("element").toString();
        if (Objects.equals(elementCheckType, "exists")){
            return checkListExists(eventData, elementCheckName);
        }
        else {
            Object requiredValue = eventRequirement.get("value");
            switch (elementCheckType){
                case "exact": return checkListExact(eventData, elementCheckName, requiredValue);
                case "contains": return checkListEnum(eventData, elementCheckName, (ArrayList) requiredValue);
                case "class": return  checkListType(eventData, elementCheckName, (Class) requiredValue);
                default: return false;
            }
        }
    }

    // Method checks to see if an event has the required parameter as a field, does not check value of parameter
    public boolean checkListExists(JSONObject eventData, String eventElement) {
        return eventData.containsKey(eventElement);
    }

    /* Method checks to see if an event has a value that is exactly equal to the expected, i.e. value is 1, "Hello", true,
    etc. Method uses the required value's class' version of equals to compare. */
    public  boolean checkListExact(JSONObject eventData, String eventElement, Object elementValue){
        if (eventData.containsKey(eventElement)){
            Object element = eventData.get(eventElement);
            return element.equals(elementValue);
        }
        return false;
    }

    /* Method checks to see if value for a given parameter is one from a series of potential accepted values, i.e. a
    lightswitch might have "on" or "off" but shouldn't have 1, "blue", or true. */
    public boolean checkListEnum(JSONObject eventData, String eventElement, List acceptedValues){
        if (eventData.containsKey(eventElement)){
            Object element = eventData.get(eventElement);
            return acceptedValues.contains(element);
        }
        return false;
    }

    // Method checks if a value is of a given type, i.e. String, int, bool, etc.
    public boolean checkListType(JSONObject eventData, String eventElement, Class eventClass){
        if (eventData.containsKey(eventElement)){
            return eventClass.isInstance(eventData.get(eventElement));

        }
        return false;
    }
}
