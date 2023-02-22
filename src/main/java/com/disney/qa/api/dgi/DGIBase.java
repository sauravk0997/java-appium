package com.disney.qa.api.dgi;

import com.disney.qa.api.dgi.dust.DustPageKeys;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config.Events;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.edge.EdgeParameters;
import com.disney.util.HARUtils;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.HarEntry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.qaprosoft.carina.core.foundation.utils.common.CommonUtils.pause;

public class DGIBase implements DgiApiProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private BrowserMobProxy proxy;

    public BrowserMobProxy getProxy() {
        return proxy;
    }

    public void setProxy(BrowserMobProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, T[] body) {
        return null;
    }

    @Override
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, boolean[] flags) {
        return null;
    }

    @Override
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, T[] body, boolean[] flags) {
        return null;
    }

    public DGIBase() {
    }

    public DGIBase(BrowserMobProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * @deprecated Will be deprecated, please switch to isEntryValidEvent(DgiEndpoints endpoints, HarEntry harEntry)
     */
    @Deprecated
    public boolean isEntryValidDustEvent(HarEntry harEntry) {
        boolean isValid = true;
        boolean notEmpty;
        boolean isDust;
        try {
            String postDataText = harEntry.getRequest().getPostData().getText();
            notEmpty = !postDataText.isEmpty();
            isDust = harEntry.getRequest().getUrl().equals(EdgeParameters.getApiHost() + "/dust");
            if (new DisneyApiProvider().getPlatform().equals("web")) {
                isValid = StringUtils.countMatches(postDataText, "server") == 1;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return isValid && notEmpty && isDust;
    }

    /**
     * @deprecated - Method is deprecated and will eventually be removed. Please switch to isEntryValidEndpoint(DgiEndpoints[] endpoints, HarEntry harEntry)
     */
    @Deprecated
    public boolean isEntryValidEvent(DgiEndpoints endpoints, HarEntry harEntry){
        boolean isValid;
        boolean notEmpty;
        boolean matchesEndpoint;
        try {
            String postDataText = harEntry.getRequest().getPostData().getText();
            notEmpty = !postDataText.isEmpty();
            matchesEndpoint = harEntry.getRequest().getUrl().equals(EdgeParameters.getApiHost() + endpoints.getEndpoint());
            if (new DisneyApiProvider().getPlatform().equals("web")) {
                isValid = StringUtils.countMatches(postDataText, "server") == 1;
            } else {
                isValid = true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return isValid && notEmpty && matchesEndpoint;
    }


    /**
     * Method verifies an endpoint within the HAR is a valid entry for DGI validation
     * @param endpoints - Array of endpoints (/dust, /telemetry, etc.) that are to be checked
     * @param harEntry - The raw har entry from the proxy
     * @return - True if the criteria are met, false if otherwise or if there is a problem with the data
     */
    public boolean isEntryValidEndpoint(DgiEndpoints[] endpoints, HarEntry harEntry){
        boolean isValid;
        boolean notEmpty;
        boolean matchesEndpoint;

        LOGGER.debug("HAR entry URL " + harEntry.getRequest().getUrl());

        //Checks if the entry URL contains the host address we're checking for. Avoids unnecessary parsing.
        if(!harEntry.getRequest().getUrl().contains(EdgeParameters.getApiHost())){
            LOGGER.debug("Entry host does not match expected value. Skipping entry.");
            return false;
        }

        /*
         * Loop iterates through the desired endpoints. Loop placement here allows for the addition of
         * new loggers if needed that will be more grouped together, making readability much easier for each entry check.
         */
        for (int currentEndpoint = 0; currentEndpoint < endpoints.length; currentEndpoint++) {
            try{
                matchesEndpoint = harEntry.getRequest().getUrl().equals(EdgeParameters.getApiHost() + endpoints[currentEndpoint].getEndpoint());
                /*
                 * Checks for the entry URL to match the endpoint. Allows the validation to check each entry
                 * for each endpoint as needed without having to iterate through the entire proxy entry list
                 * for each endpoint every time.
                 *
                 * Scenario 1: Endpoints List only contains /dust
                 * Ex. 1: https://global.edge.bamgrid.com/dust - Proceed to validation checks for HAR Entry
                 * Ex. 2: https://global.edge.bamgrid.com/telemetry - Continue (ends loop). Returns false for invalid entry (desired result)
                 *
                 * Scenario 2: Endpoints List contains /dust and /telemetry
                 * Ex. 1: https://global.edge.bamgrid.com/dust - Proceed to validation checks for HAR Entry
                 * Ex. 2: https://global.edge.bamgrid.com/telemetry - Continues loop (/dust does not match). /telemetry is a match. Proceeds to validation checks for HAR Entry(desired result)
                 * Ex. 3: https://global.edge.bamgrid.com/account - Continue x2 (/dust and /telemetry do not match). Loop ends. Returns false;
                 */
                if(!matchesEndpoint){
                    continue;
                }

                /**
                 * Gets the postdata text (the core of what is being validated.
                 * If the text is empty, something went wrong with the proxy and the entry
                 * cannot be verified.
                 */
                String postDataText = harEntry.getRequest().getPostData().getText();
                notEmpty = !postDataText.isEmpty();
                if (new DisneyApiProvider().getPlatform().equals("web")){
                    isValid = StringUtils.countMatches(postDataText, "server") == 1;
                } else {
                    isValid = true;
                }
            } catch (NullPointerException e){
                LOGGER.debug(String.format("Null pointer found for entry: %s%n%s", harEntry.getRequest().getUrl(), e));
                return false;
            }
            /*
             * Returns true if all criteria for validation are met. Otherwise iterates to next endpoint or returns false
             * if there are no more endpoints to check for the entry, indicating is not not a valid entry to test.
             */
            if(isValid && notEmpty){
                return true;
            }
        }
        return false;
    }

    public List<String> getEventObjects(HarEntry harEntry){
        List<String> events = new ArrayList<>();
        String entry = harEntry.getRequest().getPostData().getText();

        int clients = StringUtils.countMatches(entry, "client");
        LOGGER.debug("Total number of client objects in call: " + clients);

        for (int i = 0; i < clients; i++) {
            LinkedHashMap object = JsonPath.parse(entry).read(String.format("$.[%s]", i));
            LOGGER.debug("EVENT OBJECT: " + JsonPath.parse(entry).read(String.format("$.[%s]..event", i)));
            events.add(new GsonBuilder()
                    .create()
                    .toJson(object));
        }
        return events;
    }

    /**
     *
     * Use to ensure expected event has triggered before taking further actions
     *
     * @platform web
     *
     * @param delay thread sleep time in seconds
     * @param expectedPagekey expected page key to appear in dust call upon page load
     * @param expectedEvent expected event to trigger upon page load
     *
     * TODO add timeout
     * TODO research additional platform support
     * TODO add capability to support duplicate events (example scenario - navigating to page A, hit back btn, navigate back to page A. Page A will contain same events)
     */
    public void addEventTriggerDelay(int delay, boolean refresh, DustPageKeys[] expectedPagekey, Events[] expectedEvent) {
        int maxAttempts = 10;
        int refreshAttempt = 5;
        boolean found = false;
        if (expectedPagekey.length != expectedEvent.length) {
            LOGGER.error("Number of expected pagekeys don't match expected events!");
            return;
        }
        for (int currentExpectedPagekey = 0; currentExpectedPagekey < expectedPagekey.length; currentExpectedPagekey++) {
            while (!found && --maxAttempts > 0) {
                    found = containsPagekeysAndEvents(expectedPagekey, expectedEvent, currentExpectedPagekey);
                    if (!found && refresh) {
                        refreshAttempt--;
                        if (refreshAttempt == 0) {
                            LOGGER.info("Event did not trigger, refreshing page...");
                            getDriver().navigate().refresh();
                            refreshAttempt = 5;
                        } else {
                            LOGGER.info("Event not found for pagekey, pausing for " + delay + " seconds..." +
                                    "\nMax attempts remaining: " + maxAttempts);
                            pause(delay);
                        }
                    }

            }
            found = false;
        }
    }

    private boolean containsPagekeysAndEvents(DustPageKeys[] expectedPagekey, Events[] expectedEvent, int index) {
        try {
            for (HarEntry harEntry : getProxy().getHar().getLog().getEntries()) {
                try {
                    if (harEntry.getRequest().getPostData().getText().contains("\"pageKey\":\"" + expectedPagekey[index].getPageKey() + "\"")
                            && harEntry.getRequest().getPostData().getText().contains("\"event\":\"" + expectedEvent[index].getEvent() + "\"")) {
                        LOGGER.info("Found: \"" + expectedPagekey[index].getPageKey() + "\":\"" + expectedEvent[index].getEvent() + "\"");
                        return true;
                    }
                } catch (NullPointerException e) {
                    LOGGER.debug(e.getMessage());
                }
            }
        } catch (NullPointerException e) {
            LOGGER.debug(e.getMessage());
        }
        return false;
    }

    public void verifyEntriesContainPageKeys(BrowserMobProxy proxy, SoftAssert softAssert, List<String> expectedKeys){
        expectedKeys.forEach(key -> softAssert.assertTrue(
                HARUtils.harContainsValue(proxy, EdgeParameters.getApiHost(), HARUtils.RequestDataType.POST_DATA, String.format("\"pageKey\":\"%s\"", key)),
                String.format("Expected - Session to contain recordings for pageKey: %s", key)));
    }
}