package com.disney.util;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.harreader.model.HarEntry;
import com.disney.exceptions.HARException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebrunner.agent.core.registrar.Artifact;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class HARUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public enum RequestDataType {
        COMMENT,
        COOKIES,
        HEADER_SIZE,
        HTTP_VERSION,
        METHOD,
        POST_DATA,
        QUERY_STRING,
        RESPONSE_CODE,
        RESPONSE_DATA,
        URL
    }

    private BrowserUpProxy proxy;

    public HARUtils(BrowserUpProxy proxy) {
        this.proxy = proxy;
    }

    /* Save har in local dir and/or artifact */
    public void publishHAR(String fileName) {
        if (proxy == null) {
            LOGGER.info("Unable to save har file as proxy object is null!");
            return;
        }
        
        // Saving har to a file...
        String name = "Report.har";
        if (fileName != null && !fileName.isEmpty()) {
            name = fileName;
            if (!name.endsWith(".har")) {
                name = name + ".har";
            }
        }

        File file = new File(name);
        if (proxy.getHar() == null) {
            LOGGER.error("Unable to save har file as proxy returns null!");
            return;
        }

        try {
            proxy.getHar().writeTo(file);
            Artifact.attachToTest(name, file);
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to save har file: %s", name), e);
        }

    }

    public static String beautify(String json) throws IOException {
        Objects.requireNonNull(json);
        if (!json.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(mapper.readValue(json, Object.class));
        }
        return "";
    }

    //TODO add verbose level
    public void printHarDetails() {
        if (this.proxy != null && this.proxy.isStarted()) {
            List<HarEntry> entries = this.proxy.getHar().getLog().getEntries();

            LOGGER.info("Generated HAR size: {}", entries.size());

            entries.forEach(harEntry -> {
                try {
                    LOGGER.info("Method: " + harEntry.getRequest().getMethod()
                            + "\nRequest URL: " + harEntry.getRequest().getUrl()
                            + "\nHttpVersion: " + harEntry.getRequest().getHttpVersion()
                            + "\nCookies: " + harEntry.getRequest().getCookies()
                            + "\nHeaderSize: " + harEntry.getRequest().getHeadersSize()
                            + "\nHeaders: " + harEntry.getRequest().getHeaders()
                            + "\nQueryString: " + harEntry.getRequest().getQueryString()
                            + "\nPostData: " + harEntry.getRequest().getPostData().getText()
                            + "\nComment: " + harEntry.getRequest().getComment()
                            + "\nResponse Code: " + harEntry.getResponse().getStatus());

                } catch (NullPointerException e) {
                    LOGGER.debug(e.getMessage(), e);
                }
            });
        } else {
            Assert.fail("Proxy isn't running..");
        }
    }

    /**
     * Prints out the captured proxy data similar to printHarDetails, only filtered to
     * specified entry data types and host values. Supply hosts list with an empty String
     * to print specified data type entries for all captured hosts.
     *
     * @param dataTypes - HAR entry data types provided by the Enum
     * @param hosts - String value of the host. Can be generic for multiple hosts or specific to each one desired
     */
    public void printSpecificHarDetails(List<RequestDataType> dataTypes, List<String> hosts){
        List<String> outputs = new ArrayList<>();

        if(this.proxy != null && this.proxy.isStarted()) {
            List<HarEntry> entries = this.proxy.getHar().getLog().getEntries();
            entries.forEach(entry -> hosts.forEach(host ->{
                if(entry.getRequest().getUrl().contains(host)){
                    outputs.addAll(getDesiredEntryData(dataTypes, entry));
                    outputs.add("");
                }
            }));

            StringBuilder builder = new StringBuilder();
            for(String output : outputs){
                builder.append(String.format("%s%n", output));
            }

            LOGGER.info("Proxy Output:\n{}", builder);
        } else {
            Assert.fail("Proxy isn't running...");
        }
    }

    /**
     * Prints out captured POST data - Must initialize a newHar at least once prior to calling in a test.
     * @param hosts
     */

    public void printSpecificHarDetailsPost(List<String> hosts) {
        printSpecificHarDetails(Arrays.asList(HARUtils.RequestDataType.URL, RequestDataType.POST_DATA, RequestDataType.RESPONSE_DATA), hosts);
    }

    public void publishHarConfiguredName(String prefixName) {
        String fileName = String.format("%s_", prefixName) + new Date().toString().replaceAll("[^a-zA-Z0-9+]", "-");
        publishHAR(fileName);
    }

    /**
     * Prints filtered har entry data. Iterates over all HAR entries and printing the data only if it meets the filter
     * criteria.
     * @param filters - HashMap of the DataType and a list of possible values for the har entry to contain.
     *                 (Ex. 'RequestDataType.POST_DATA' and 'urn:dss:event:fed:media:playback:started')
     *                 (Ex. 'RequestDataType.URL' and 'bamgrid.com/dust')
     */
    public void printFilteredHarDetails(Map<RequestDataType, List<String>> filters) {
        List<String> outputs = new ArrayList<>();
        List<HarEntry> entries = this.proxy.getHar().getLog().getEntries();

        entries.forEach(entry -> {
            List<Boolean> entryIsMatch = new LinkedList<>();
            List<RequestDataType> dataTypes = new LinkedList<>(filters.keySet());
            for (RequestDataType dataType : dataTypes) {
                try {
                    String dataEntry = getHarEntryData(entry, dataType);
                    entryIsMatch.add(filters.get(dataType).stream().anyMatch(dataEntry::contains));
                } catch (NullPointerException npe) {
                    entryIsMatch.add(false);
                }
            }

            if (!entryIsMatch.contains(false)) {
                LOGGER.info("HAR Entry:\n{}", getDesiredEntryData(dataTypes, entry));
                outputs.addAll(getDesiredEntryData(dataTypes, entry));
                outputs.add("");
            }
        });

        StringBuilder builder = new StringBuilder();
        for(String output : outputs){
            builder.append(String.format("%s%n", output));
        }

        LOGGER.info("Combined Output:\n{}", builder);
    }

    /**
     * Returns a grouping of specific HAR entry data types (POST data, URL, and Response for example)
     * @param dataTypes String values of the different recorded data types you want printed
     * @param entry The recording from the proxy
     * @return The concatenated entry's data
     */
    private List<String> getDesiredEntryData(List<RequestDataType> dataTypes, HarEntry entry){
        List<String> outputs = new ArrayList<>();
        String output = "%s: %s";
        try {
            dataTypes.forEach(dataType -> {
                if(dataType.equals(RequestDataType.POST_DATA) || dataType.equals(RequestDataType.RESPONSE_DATA)){
                    try {
                        outputs.add(String.format(output, dataType, beautify(getHarEntryData(entry, dataType))));
                    } catch (IOException e) {
                        LOGGER.debug("Error: {}", e.getMessage());
                        outputs.add(String.format(output, dataType, getHarEntryData(entry, dataType)));
                    }
                } else {
                    outputs.add(String.format(output, dataType, getHarEntryData(entry, dataType)));
                }
            });
        } catch (NullPointerException e) {
            LOGGER.debug(e.getMessage(), e);
        }

        return outputs;
    }

    /**
     * This method modifies the generated har so it can be opened by charles when a variety of capture types are used
     *
     * @param proxy    - proxy instance
     * @param fileName - name of the har file
     */
    public static void generateValidHarForCharles(BrowserUpProxy proxy, String fileName) {
        try (PrintWriter printWriter = new PrintWriter(fileName.concat(".har"))) {
            StringWriter stringWriter = new StringWriter();
            proxy.getHar().writeTo(stringWriter);
            String jsonString = stringWriter.toString();
            jsonString = StringUtils.replaceEach(jsonString, new String[]{"\"comment\":\"\",", ",\"comment\":\"\""}, new String[]{"", ""});
            printWriter.println(jsonString);
        } catch (Exception e) {
            throw new HARException("Unable to create a valid har for charles due to : " + e);
        }
    }

    public static void attachHarAsArtifact(BrowserUpProxy proxy, String fileName) {
        File harFileForCharles = new File(fileName.concat(".har"));
        generateValidHarForCharles(proxy, fileName);
        Artifact.attachToTest(fileName.concat(".har"), harFileForCharles);
    }

    /**
     * Basic version of harContainsValues which does not filter by host URL. See main method for
     * more details.
     */
    public static boolean harContainsValue(BrowserUpProxy proxy, RequestDataType dataType, String value) {
        return harContainsValue(proxy, "", dataType, value);
    }

    /**
     * Checks each har entry of a proxy session for a desired value
     * @param proxy - Proxy instance
     * @param url - String for URLs to be checked. Pass an empty string to run the check against all entries
     *                     regardless of host.
     * @param dataType - har file datatype to be parsed
     * @param value - Value to be checked for. Best Practice is to use the "key":"value" format as this is
     *              not app-specific
     * @return - Returns true upon the first confirmed find of the desired value as further validation is not
     *              necessary at this point. Only returns false if it iterates through the entire session
     *              without finding the desired value.
     */
    public static boolean harContainsValue(BrowserUpProxy proxy, String url, RequestDataType dataType, String value){
        String entry = "";
        for (HarEntry harEntry : proxy.getHar().getLog().getEntries()) {
            try {
                if(harEntry.getRequest().getUrl().contains(url)) {
                    entry = getHarEntryData(harEntry, dataType);
                }
                if(entry.contains(value)){
                    return true;
                }
            } catch (Exception e){
                LOGGER.debug("No data to check. Returning false");
            }
        }
        return false;
    }
    /**As above, but for an array of strings rather than a singular one.
     */
    public static boolean harContainsValue(BrowserUpProxy proxy, String url, RequestDataType dataType, String[] values) {
        String entry = "";
        boolean hasAll = false;
        for (HarEntry harEntry : proxy.getHar().getLog().getEntries()) {
            try {
                if (harEntry.getRequest().getUrl().contains(url)) {
                    entry = getHarEntryData(harEntry, dataType);
                    hasAll = true;
                    for (String value : values) {
                        if (!entry.contains(value)) {
                            hasAll = false;
                        }
                    }
                    if (hasAll) {
                        return true;
                    }
                }

            } catch (Exception e) {
                LOGGER.debug("No data to check. Returning false");
            }
        }
        return false;
    }
    public static boolean harContainsValue(BrowserUpProxy proxy, RequestDataType dataType, String[] values){
        return harContainsValue(proxy, "", dataType, values);
    }


    private static String getHarEntryData(HarEntry harEntry, RequestDataType dataType){
        String entry = "";
        switch (dataType) {
            case URL:
                entry = harEntry.getRequest().getUrl();
                break;
            case METHOD:
                entry = harEntry.getRequest().getMethod().toString();
                break;
            case COMMENT:
                entry = harEntry.getRequest().getComment();
                break;
            case COOKIES:
                entry = harEntry.getRequest().getCookies().toString();
                break;
            case POST_DATA:
                entry = harEntry.getRequest().getPostData().getText();
                break;
            case HEADER_SIZE:
                entry = String.valueOf(harEntry.getRequest().getHeadersSize());
                break;
            case HTTP_VERSION:
                entry = harEntry.getRequest().getHttpVersion();
                break;
            case QUERY_STRING:
                entry = harEntry.getRequest().getQueryString().toString();
                break;
            case RESPONSE_DATA:
                entry = harEntry.getResponse().getContent().getText();
                break;
            case RESPONSE_CODE:
                entry = String.valueOf(harEntry.getResponse().getStatus());
                break;
        }
        return entry;
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        try {
            this.proxy.getHar().writeTo(writer);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage(), e);
        }
        return writer.toString();
    }

}
