package com.disney.qa.api.disney;

import com.disney.qa.api.pojos.DisneyAccount;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.SkipException;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class DisneyContentApiChecker extends DisneyApiProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static List<String> brands = new ArrayList<>();
    private static Map<String, List<String>> refIdValues = new HashMap<>();
    private static Map<String, JsonNode> brandPages = new HashMap<>();
    private static Map<String, JsonNode> curatedSets = new HashMap<>();

    private final Configuration jsonPathJacksonConfiguration;

    String[] dictionaryMetadata = new String[]{"$..createdDate", "$..version", "$..resourceKey"};

    //Filters collections down to their types
    String contentClassQuery = "$..[?(@.contentClass=='editorial')].items..contentClass";

    //Filters collections down to media URL extensions based on contentClass
    String mediaItemQuery = "$..[?(@.contentClass=='%s')].slugs..value";

    public DisneyContentApiChecker() {
        this.jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    //Returns metadata for dictionary file in use. Function requested by MobileQA.
    public List<String> getDictionaryMetadata(JsonNode rawResponse) {
        LinkedList<String> metadata = new LinkedList<>();
        for (String field : dictionaryMetadata) {
            JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(field);
            metadata.add(data.get(0).asText());
        }
        if (!metadata.isEmpty()) {
            return metadata;
        } else {
            throw new SkipException("ERROR: COULD NOT PARSE DICTIONARY. CHECK API PATH");
        }
    }

    /* This returns a singular item based on an established key value.
     * Test is skipped if the value is null due to the value being invalid
     * and needs to be updated asap.
     */
    public String getDictionaryItemValue(JsonNode dictionary, String node) {
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read("$.." + node);
        try {
            return data.get(0).asText();
        } catch (NullPointerException e) {
            throw new SkipException("ERROR: DICTIONARY RESULT IS NULL. CHECK VALIDITY OF JSONPATH QUERY '" + node + "'");
        }
    }

    /* This allows the return of empty strings for invalid queries,
     * and should ONLY be used for queries where a hash code is used
     * as these values are added to the dictionary as needed and may or may not
     * be present in a given environment's list.
     */
    private String getDictionaryItemValueEmptyAllowed(JsonNode dictionary, String node) {
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read("$.." + node);
        try {
            return data.get(0).asText();
        } catch (NullPointerException e) {
            LOGGER.info("Node '" + node + "' not found in dictionary. Returning empty String...");
            return "";
        }
    }

    //Returns the displayed title based on the language sent. Works on Movie and series episode content ID nodes
    //Also works on Collection Json bodies for tile content-desc values (editorial, character, franchise).
    public String getMediaItemLocalizedTitle(JsonNode rawResponse, String language) {
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format("$..[?(@.field=='title' && @.type=='full' && @.sourceEntity=='program' && @.language=='%s')].content", language));
        return data.get(0).asText();
    }

    //Returns the displayed series title based on the language sent. Works on series episode content ID nodes
    //Also works on Collection Json bodies for tile content-desc values (editorial, character, franchise).
    public String getMediaItemLocalizedSeriesTitle(JsonNode rawResponse, String language) {
        language = language.replace("CA", "FR");
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format("$..[?(@.field=='title' && @.type=='full' && @.sourceEntity=='series' && @.language=='%s')].content", language));
        return data.get(0).asText();
    }

    //Returns the potential 'no purchase' welcome screen subtext values
    public List<String> getNoPurchaseSubtextValues(JsonNode dictionary) {
        LinkedList<String> values = new LinkedList<>();
        Iterator<String> iterator = dictionary.findParent("welcome_subcta_loginonly_copy").fieldNames();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.startsWith("welcome_subcta_loginonly_copy")) {
                String value = getDictionaryItemValue(dictionary, key);
                if (!value.isEmpty()) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    public Set<String> getActiveCollectionTypes(JsonNode rawResponse) {
        Set<String> collections = new HashSet<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(contentClassQuery);

        for (JsonNode node : data) {
            collections.add(node.asText());
        }

        return collections;
    }

    public String getFirstCollectionByType(JsonNode rawResponse, String type) {
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(mediaItemQuery, type));

        return data.get(0).asText();
    }


    public String getMarketingItems(String jsonString, String path) {
        try {
            JSONArray array = JsonPath.parse(jsonString).read("$..marketing.ADULT.entities." + path);
            return array.get(0).toString();

        } catch (Exception e) {
            throw new SkipException(String.format("Unable to retrieve marketing entity with error: %s", e));
        }
    }

    public Set<String> getAllLegalItems(String jsonString) {
        Set<String> set = new LinkedHashSet<>();
        try {
            JSONArray array = JsonPath.parse(jsonString).read("$..legal.ADULT.disclosures..label");
            for (Object node : array) {
                set.add(node.toString());
            }
            return set;
        } catch (Exception e) {
            throw new SkipException(String.format("Unable to retrieve legal document code with error: %s", e));
        }
    }

    //This method will work for platforms that are not able to launch web links, for other platforms a conditional statement can be used
    public String getDocument(String documentResponse) {
        JSONArray getIndices = JsonPath.parse(documentResponse).read("$..links..start");
        JSONArray getLinks = JsonPath.parse(documentResponse).read("$..links..href");
        JSONArray getLabels = JsonPath.parse(documentResponse).read("$..links..label");
        JSONArray retrievedDocument = JsonPath.parse(documentResponse).read("$..text");
        StringBuilder builder = new StringBuilder(retrievedDocument.get(0).toString());

        IntStream.range(0, getIndices.size()).forEach(index -> {
            int reverseIndex = getIndices.size() - 1 - index;
            builder.insert(Integer.valueOf(getIndices.get(reverseIndex).toString()) + getLabels.get(reverseIndex).toString().length(),
                    " (" + getLinks.get(reverseIndex) + ")");
        });

        //Carriage return needs to be removed
        return builder.toString().replaceAll("\\r", "");
    }

    public String replaceValue(String item, String... replace) {
        String replaced = "";
        for (String replaceWith : replace) {
            replaced = item.replaceFirst("\\$.*?}", replaceWith);
        }
        return replaced;
    }

    /*
     * Returns version number for dictionary files in use with running version.
     * iOS data structuring requires a specific query to it alone (probably for Apple TV as well)
     * Android, Android-TV, and Web configs use the same data structure.
     */
    public String getDictionaryVersion(JsonNode config, String resourceKey, String platform) {
        LOGGER.debug("Checking for {}", resourceKey);
        JsonNode data;
        try {
            if (platform.equals("ios") || platform.equalsIgnoreCase("apple-tv")) {
                data = JsonPath.using(jsonPathJacksonConfiguration).parse(config)
                        .read("$..[?(@.resourceKey == '" + resourceKey + "')]..version");
            } else {
                data = JsonPath.using(jsonPathJacksonConfiguration).parse(config)
                        .read("$.." + resourceKey);
            }
            return data.get(0).asText();
        } catch (Exception e) {
            LOGGER.debug("No version number found.");
            return "0.0";
        }
    }

    public List<String> queryResponse(JsonNode response, String query) {
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(response).read(query);
        List<String> list = new ArrayList<>();
        data.forEach(item -> list.add(item.asText()));
        return list;
    }

    public Map<String, String> getSetIdAndType(JsonNode setCollection, String type) {
        JsonNode node = JsonPath.using(jsonPathJacksonConfiguration).parse(setCollection)
                .read(String.format("$..[?(@.type == '%s')]", type));
        Map<String, String> map = new LinkedHashMap<>();
        node.forEach(c -> {
            String setType;
            String refId;
            String shelfContainer = c.toString();
            List<String> list;
            try {
                list = JsonPath.read(shelfContainer, "$..refId");
                refId = list.get(0);
                list = JsonPath.read(shelfContainer, "$..refType");
                setType = list.get(0);
            } catch (Exception e) {
                list = JsonPath.read(shelfContainer, "$..set.setId");
                refId = list.get(0);
                list = JsonPath.read(shelfContainer, "$..set.type");
                setType = list.get(0);
            }
            map.put(refId, setType);
        });
        return map;
    }

    public byte[] getRipcutImageAsByteArray(String masterId, int width) {
        try {
            URI uri = new URI(getDictionaryItemValue(getDictionaryBody("en", "application"),
                    "config_ripcut_base") + masterId + "/scale");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).queryParam("width", width)
                    .queryParam("format", "jpeg");
            return restTemplate.getForObject(builder.toUriString(), byte[].class);
        } catch (Exception e) {
            throw new SkipException("Failed with Exception" + e);
        }
    }

    /**
     * Returns list of headers contained in the passed collections data endpoint.
     *
     * @param collection - JsonNode raw data to be parsed
     * @param filters    - Filters out items from the list. Pass nothing to return full unfiltered list
     */
    public List<String> getPageCarouselHeaders(JsonNode collection, String... filters) {
        List<String> headers = new ArrayList<>();
        List<String> filterList = Arrays.asList(filters);
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(collection)
                .read("$..[?(@.type=='ShelfContainer')]..texts..[?(@.targetEntity=='set')]..content");

        data.forEach(node -> headers.add(node.asText()));
        Assert.assertFalse(headers.isEmpty(), "ERROR: DATA ENDPOINT RETURNED NO CONTENT HEADERS. CHECK URI AND JSON PATH");
        filterList.forEach(filter -> headers.removeIf(header -> header.contains(filter)));

        return headers;
    }

    public List<String> getActiveBrandTiles(DisneyAccount account, String language, String locale) {
        if (brands.isEmpty()) {
            JsonNode data = JsonPath.using(jsonPathJacksonConfiguration)
                    .parse(getCollectionBody(account, language, locale, "home", "home", true))
                    .read("$..set..key");
            data.forEach(node -> {
                String brand = node.asText();
                brand = brand.replace("brand-", "");
                brands.add(brand);
            });
        }
        return brands;
    }

    public List<String> getBrandActiveSetIds(JsonNode brandPage) {
        List<String> refIds = new ArrayList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(brandPage)
                .read("$..refId");
        data.forEach(node -> refIds.add(node.asText()));
        return refIds;
    }

    /*
     * See overload for details
     */
    public Map<String, String> findMediaByRating(String rating, DisneyAccount account, String language, String locale) {
        return findMediaByRating(rating, account, language, locale, "", new ArrayList<>());
    }

    public Map<String, String> findMediaByRating(String rating, DisneyAccount account, String language, String locale, List<String> ignoreList) {
        return findMediaByRating(rating, account, language, locale, "", ignoreList);
    }

    /**
     * Returns the first piece of media for a given rating without ignoring any items by storing and scanning through
     * available collections across all brands.
     *
     * @param rating     - Rating to filter by
     * @param account    - D+ Account for access token
     * @param language   - Language the title will be returned in
     * @param locale     - Locale being parsed due to regional content availability
     * @param filterType - String to set the check for Series or Programs (Movies)
     * @param ignoreList - Titles to be ignored for any given reason
     * @return
     */
    public Map<String, String> findMediaByRating(String rating, DisneyAccount account, String language, String locale, String filterType, List<String> ignoreList) {
        getActiveBrandTiles(account, language, locale);
        HashMap<String, String> media = new HashMap<>();
        if (brandPages.isEmpty()) {
            brands.forEach(brand -> brandPages.put(brand, getCollectionBody(account, language, locale, "brand", brand, false)));
        }

        for (String brand : brands) {
            if (refIdValues.get(brand) == null) {
                refIdValues.put(brand, getBrandActiveSetIds(brandPages.get(brand)));
            }
            for (String refId : refIdValues.get(brand)) {
                if (curatedSets.get(refId) == null) {
                    curatedSets.put(refId, getCuratedSetBySetId(account, language, locale, refId));
                }
                String foundItem = getMediaWithDesiredRatingByCollection(curatedSets.get(refId), rating, filterType, ignoreList);
                if (foundItem != null && !ignoreList.contains(foundItem)) {
                    LOGGER.info("FOUND: '{}'", foundItem);
                    media.put(rating, foundItem);
                    return media;
                }
            }
        }
        return media;
    }

    /**
     * Returns the first piece of media available in a given collection by desired rating.
     * Works in conjunction with findMediaByRating() but can also be called independently.
     *
     * @param collection    - The JsonNode collection to be parsed
     * @param desiredRating - Rating to get media by
     * @param filterType    - String value to filter the list by if desired
     * @param ignoreList    - List of titles to ignore
     * @return
     */
    public String getMediaWithDesiredRatingByCollection(JsonNode collection, String desiredRating, String filterType, List<String> ignoreList) {
        JsonNode items = JsonPath.using(jsonPathJacksonConfiguration).parse(collection)
                .read("$..items.*");

        List<JsonNode> itemsList = new ArrayList<>();
        items.forEach(itemsList::add);
        itemsList.removeIf(item -> item.findValue("sourceEntity").asText().equals(filterType));

        for (JsonNode item : itemsList) {
            try {
                String title = item.findValue("full").findValue("content").asText();
                if (ignoreList.contains(title)) {
                    continue;
                }

                String ratingValue = item.findValue("ratings").findValue("value").asText();
                String ratingSystem = item.findValue("ratings").findValue("system").asText();
                if (desiredRating.contains("_")
                        && ratingValue.equals(StringUtils.substringAfter(desiredRating, "_"))
                        && ratingSystem.equals(StringUtils.substringBefore(desiredRating, "_"))) {
                    return title;
                } else {
                    if (ratingValue.equals(desiredRating)) {
                        return title;
                    }
                }
            } catch (NullPointerException e) {
                LOGGER.debug("Media had no rating");
            }
        }
        return null;
    }

    /**
     * Returns the desired attribute from an account's json object as text.
     *
     * @param accountBody - The raw Json of the Account object
     * @param profileName - The profile name being parsed
     * @param attribute   - The attribute being parsed
     * @return - The text value of the atribute
     */
    //TODO: Deprecate once the external account system is implemented
    public String getProfileAttribute(JsonNode accountBody, String profileName, String attribute) {
        JsonNode rawValue = JsonPath.using(jsonPathJacksonConfiguration).parse(accountBody)
                .read(String.format("$..[?(@.profileName == '%s')]..%s", profileName, attribute));

        String textValue = rawValue.get(0).asText();
        if (textValue.isEmpty()) {
            Assert.fail(String.format("Invalid attribute request '%s'. No result found", attribute));
        }
        return textValue;
    }

    /**
     * Returns a list of profiles presently active on an account
     *
     * @param accountBody - The account's json body
     * @return
     */
    //TODO: Deprecate once the external account system is implemented
    public List<String> getAccountProfileNames(JsonNode accountBody) {
        JsonNode rawValue = JsonPath.using(jsonPathJacksonConfiguration).parse(accountBody)
                .read("$..profileName");

        List<String> names = new ArrayList<>();
        for (JsonNode node : rawValue) {
            names.add(node.asText());
        }

        return names;
    }

    public String getDetailsViewVersion() {
        try {
            JsonNode override = JsonPath.using(jsonPathJacksonConfiguration).parse(getAppConfig(getPlatform()))
                    .read("$..detailPageVersion");

            if (override.isEmpty()) {
                return "V2";
            } else {
                return override.asText();
            }
        } catch (IllegalArgumentException iae) {
            LOGGER.error("Config result was null. Returning V2 as default.");
            return "V2";
        }
    }
}
