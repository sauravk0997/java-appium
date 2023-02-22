package com.disney.qa.disney;

import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Class is designed to store Disney+ localization dictionaries in a way that reduces the number of
 * get requests that are made to the API service, as well as provide expected supported language data
 * and full country name being used for a given locale.
 * <p>
 * Country-specific values are stored in the country-specific.yaml file which is set up to have values matching
 * the google document maintained by the Disney+ product team.
 * <p>
 * https://docs.google.com/spreadsheets/d/1Vh7VupcpbtAnQxb4a0rsCqPe3D_6J0Yas-3yPAW58hk/edit?ts=5efa5076#gid=0
 *
 * @Author - bsuscavage
 * @deprecated - This class is deprecated and is slated for removal. Update to use DisneyLocalizationUtils
 * from the disney-api-utils library.
 */

@Deprecated(forRemoval = true)
public class DisneyLanguageUtils {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DisneyContentApiChecker apiHandler;

    private final Yaml yaml = new Yaml();
    private final InputStream countryStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("YML_data/disney/country-specific.yaml");
    private final ArrayList<Object> countryYml = yaml.load(countryStream);

    private static final String COUNTRY = "country";
    private static final String CODE = "code";
    private static final String RATING_SYSTEM = "ratingSystem";
    private static final String RATING_VALUES = "ratingValues";
    private static final String ALL_LEGAL_DOCUMENTS = "links[*]";
    private static final String LABELS = "$..label";
    private static final String DOCUMENT_CODE = "$..documentCode";
    private static final String SUBSCRIBER_AGREEMENT = "isSubscriberAgreementCountry";

    private String locale;
    private String userLanguage;
    private String fallbackLang;
    private String countryName;
    private String ratingSystem;
    private String subscriberAgreementCountry;

    private List<String> ratingValues;
    private List<String> supportedLangs;

    private JsonNode masterDictionaryNode;
    private JsonNode fallbackDictionaryNode;

    private final Map<String, Map<String, String>> primaryDictionaries = new HashMap<>();
    private Map<String, Map<String, String>> fallbackDictionaries = new HashMap<>();
    private Set<String> legalHeaders = new LinkedHashSet<>();
    private Map<String, String> legalDocuments = new HashMap<>();

    //Default constructor. Does not set any language codes or country data.
    public DisneyLanguageUtils() {
        this.apiHandler = new DisneyContentApiChecker();
    }

    /**
     * All-in-1 Constructor with auto-setters based on passed values for easy localization setup.
     * Sets up presumed codes that are common for a given locale and language combination.
     * Also sets the Fallback language code used in specific areas of the app for this setup.
     * <p>
     * Ex. Canadian user will most likely be using US English or Canadian French, so if
     * CA and fr are passed, the language code of 'fr-CA' is set for dictionary pulls from the API.
     *
     * @param countryCode  - 2 letter country code (US, CA, GB, ES, etc.)
     * @param languageCode - 2 letter language code (en, es, fr, pt, etc.)
     */
    public DisneyLanguageUtils(String countryCode, String languageCode) {
        this.locale = countryCode;
        this.apiHandler = new DisneyContentApiChecker();
        setCountryName();
        setRatingSystem();
        setRatingValues();
        setSubscriberAgreementCountry();
        setUserDictionaryLanguage(languageCode);
        setFallbackLanguage(getCountryName());
        setSupportedLanguages(getCountryName());

        LOGGER.info("Building User with language code: " + getUserLanguage());
        LOGGER.info("User Fallback language code: " + getFallbackLanguage());
    }

    private void setRatingSystem() {
        try {
            this.ratingSystem = searchAndReturnCountryData(this.locale, CODE, RATING_SYSTEM).toString();
        } catch (NullPointerException npe) {
            this.ratingSystem = "No Rating System";
        }
    }

    private void setRatingValues() {
        try {
            this.ratingValues = (List<String>) searchAndReturnCountryData(this.locale, CODE, RATING_VALUES);
        } catch (NullPointerException npe) {
            this.ratingValues = new LinkedList<>();
        }
    }

    private void setSubscriberAgreementCountry() {
        try {
            this.subscriberAgreementCountry = searchAndReturnCountryData(this.locale, CODE, SUBSCRIBER_AGREEMENT).toString();
        } catch (NullPointerException npe) {
            this.subscriberAgreementCountry = "false";
        }
    }

    public enum ResourceKeys {
        APPLICATION(DisneyParameters.getApplicationKey()),
        ACCESSIBILITY(DisneyParameters.getAccessibilityKey()),
        COMMERCE(DisneyParameters.getCommerceKey()),
        LANGUAGE(DisneyParameters.getLanguageKey()),
        PAYWALL(DisneyParameters.getPaywallKey()),
        PCON(DisneyParameters.getPconResourceKey()),
        PROMO(DisneyParameters.getPromoKey()),
        RATINGS(DisneyParameters.getRatingsKey()),
        SEO(DisneyParameters.getSeoKey()),
        SDK_ERRORS(DisneyParameters.getSdkErrorKey()),
        SUBSCRIPTIONS(DisneyParameters.getSubscriptionsKey()),
        WELCH(DisneyParameters.getWelchResourceKey()),
        WELCOME_MARKETING(DisneyParameters.getMarketingResourceKey());

        String resourceKey;

        ResourceKeys(String resourceKey) {
            this.resourceKey = resourceKey;
        }

        public String getResourceKey() {
            return resourceKey;
        }
    }

    public boolean isSelectedLanguageSupported() {
        boolean isSupported = this.supportedLangs.contains(getUserLanguage());
        LOGGER.info("Current Language is Supported: " + isSupported);
        return isSupported;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getUserLanguage() {
        return this.userLanguage;
    }

    public String getFallbackLanguage() {
        return this.fallbackLang;
    }

    private void setFallbackLanguage(String countryName) {
        this.fallbackLang = searchAndReturnCountryData(countryName, COUNTRY, "fallbackLanguage").toString();
    }

    public List<String> getSupportedLangs() {
        return this.supportedLangs;
    }

    public String getRatingSystem() {
        return this.ratingSystem;
    }

    public List<String> getRatingValues() {
        return this.ratingValues;
    }

    public boolean isSubscriberAgreementRequired() {
        return Boolean.parseBoolean(this.subscriberAgreementCountry);
    }

    public JsonNode getMasterDictionaryNode() {
        return this.masterDictionaryNode;
    }

    public JsonNode getFallbackDictionaryNode() {
        return this.fallbackDictionaryNode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getDictionaryItem(ResourceKeys resourceKey, DictionaryKeys dictionaryKey) {
        return getDictionaryItem(resourceKey, dictionaryKey, true);
    }

    public String getDictionaryItem(ResourceKeys resourceKey, DictionaryKeys dictionaryKey, boolean usePrimaryDictionary) {
        if(usePrimaryDictionary) {
            return getNodeValue(this.primaryDictionaries.get(resourceKey.getResourceKey()), dictionaryKey.getText());
        } else {
            return getNodeValue(this.fallbackDictionaries.get(resourceKey.getResourceKey()), dictionaryKey.getText());
        }
    }

    /**
     * @param node - Node to return
     * @return - String value of the node
     */
    public String getMasterDictionaryItem(String node) {
        return apiHandler.getDictionaryItemValue(getMasterDictionaryNode(), node);
    }

    public String getMasterDictionaryItem(DictionaryKeys key) {
        return apiHandler.getDictionaryItemValue(getMasterDictionaryNode(), key.getText());
    }

    public String getFallbackDictionaryItem(String node) {
        return apiHandler.getDictionaryItemValue(getFallbackDictionaryNode(), node);
    }

    public String getFallbackDictionaryItem(DictionaryKeys key) {
        return apiHandler.getDictionaryItemValue(getFallbackDictionaryNode(), key.getText());
    }

    public String getApplicationItem(DictionaryKeys key) {
        return getApplicationItem(key.getText());
    }

    public String getApplicationItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getApplicationKey()), node);
    }

    public String getPaywallItem(DictionaryKeys key) {
        return getPaywallItem(key.getText());
    }

    public String getPaywallItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getPaywallKey()), node);
    }

    public String getErrorsItem(DictionaryKeys key) {
        return getErrorsItem(key.getText());
    }

    public String getErrorsItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getSdkErrorKey()), node);
    }

    public String getAccessibilityItem(DictionaryKeys key) {
        return getAccessibilityItem(key.getText());
    }

    public String getAccessibilityItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getAccessibilityKey()), node);
    }

    public String getRatingsItem(DictionaryKeys key) {
        return getRatingsItem(key.getText());
    }

    public String getRatingsItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getRatingsKey()), node);
    }

    public String getLanguageItem(DictionaryKeys key) {
        return getLanguageItem(key.getText());
    }

    public String getLanguageItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getLanguageKey()), node);
    }

    public String getCommerceItem(DictionaryKeys key) {
        return getCommerceItem(key.getText());
    }

    public String getCommerceItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getCommerceKey()), node);
    }

    public String getPromoItem(DictionaryKeys key) {
        return getPromoItem(key.getText());
    }

    public String getPromoItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getPromoKey()), node);
    }

    public String getSeoItem(DictionaryKeys key) {
        return getSeoItem(key.getText());
    }

    public String getSeoItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getSeoKey()), node);
    }

    public String getWelcomeMarketingItem(DictionaryKeys key) {
        return getWelcomeMarketingItem(key.getText());
    }

    public String getWelcomeMarketingItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getMarketingResourceKey()), node);
    }

    public String getWelchItem(DictionaryKeys key) {
        return getWelchItem(key.getText());
    }

    public String getWelchItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getWelchResourceKey()), node);
    }

    public String getPconItem(DictionaryKeys key) {
        return getPconItem(key.getText());
    }

    public String getPconItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getPconResourceKey()), node);
    }

    public String getSubscriptionsItem(DictionaryKeys key) {
        return getSubscriptionsItem(key.getText());
    }

    public String getSubscriptionsItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getSubscriptionsKey()), node);
    }

    public String getFallbackApplicationItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getApplicationKey()), node);
    }

    public String getFallbackApplicationItem(DictionaryKeys key) {
        return getFallbackApplicationItem(key.getText());
    }

    public String getFallbackPaywallItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getPaywallKey()), node);
    }

    public String getFallbackPaywallItem(DictionaryKeys key) {
        return getFallbackPaywallItem(key.getText());
    }

    public String getFallbackErrorsItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getSdkErrorKey()), node);
    }

    public String getFallbackErrorsItem(DictionaryKeys key) {
        return getFallbackErrorsItem(key.getText());
    }

    public String getFallbackAccessibilityItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getAccessibilityKey()), node);
    }

    public String getFallbackAccessibilityItem(DictionaryKeys key) {
        return getFallbackAccessibilityItem(key.getText());
    }

    public String getFallbackRatingsItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getRatingsKey()), node);
    }

    public String getFallbackRatingsItem(DictionaryKeys key) {
        return getFallbackRatingsItem(key.getText());
    }

    public String getFallbackLanguageItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getLanguageKey()), node);
    }

    public String getFallbackLanguageItem(DictionaryKeys key) {
        return getFallbackLanguageItem(key.getText());
    }

    public String getFallbackCommerceItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getCommerceKey()), node);
    }

    public String getFallbackCommerceItem(DictionaryKeys key) {
        return getFallbackCommerceItem(key.getText());
    }

    public String getFallbackPromoItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getPromoKey()), node);
    }

    public String getFallbackPromoItem(DictionaryKeys key) {
        return getFallbackPromoItem(key.getText());
    }

    public String getFallbackSeoItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getSeoKey()), node);
    }

    public String getFallbackSeoItem(DictionaryKeys key) {
        return getFallbackSeoItem(key.getText());
    }

    public String getFallbackWelcomeMarketingItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getMarketingResourceKey()), node);
    }

    public String getFallbackWelcomeMarketingItem(DictionaryKeys key) {
        return getFallbackWelcomeMarketingItem(key.getText());
    }

    public String getFallbackWelchItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getWelchResourceKey()), node);
    }

    public String getFallbackWelchItem(DictionaryKeys key) {
        return getFallbackWelchItem(key.getText());
    }

    public String getFallbackPconItem(String node) {
        return getNodeValue(this.fallbackDictionaries.get(DisneyParameters.getPconResourceKey()), node);
    }

    public String getFallbackPconItem(DictionaryKeys key) {
        return getFallbackPconItem(key.getText());
    }

    public String getFallbackSubscriptionsItem(String node) {
        return getNodeValue(this.primaryDictionaries.get(DisneyParameters.getSubscriptionsKey()), node);
    }

    public String getFallbackSubscriptionsItem(DictionaryKeys key) {
        return getFallbackSubscriptionsItem(key.getText());
    }

    public JsonNode getLegalItems(String jsonString, String path) {
        try {
            return JsonPath.using(Configuration.builder()
                    .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build()).parse(jsonString).read("$..legal.ADULT.disclosures.." + path);

        } catch (Exception e) {
            throw new SkipException(String.format("Unable to retrieve legal document code with error: %s", e));
        }
    }

    public String getLegalDocument(String documentCode) {
        return apiHandler.getOneIdDocument(documentCode);
    }

    public String getLegalText(String documentResponse) {
        net.minidev.json.JSONArray retrievedDocument = JsonPath.parse(documentResponse).read("$..text");
        return (String) retrievedDocument.get(0);
    }

    public void printDictionaryMetadata() {
        StringBuilder builder = new StringBuilder();
        builder.append("Printing metadata for dictionaries pulled from API...\n\n");
        for (String resourceKey : DisneyParameters.getResourceKeys()) {
            builder.append(String.format("Resource Key: %s%n", resourceKey));
            builder.append(String.format("Created Date: %s%n", getNodeValue(this.fallbackDictionaries.get(resourceKey), "createdDate")));
            builder.append(String.format("Version: %s%n%n", getNodeValue(this.fallbackDictionaries.get(resourceKey), "version")));
        }
        LOGGER.info(builder.toString());
    }

    private String getNodeValue(Map<String, String> dictionary, String node) {
        if (!dictionary.containsKey(node)) {
            Assert.fail("Dictionary does not have desired node. Recheck test criteria\nNode Checked: " + node);
        }
        return dictionary.get(node);
    }

    private Object searchAndReturnCountryData(String valueToSearchFor, String fieldToSearch, String fieldToReturn) {
        LOGGER.info(String.format("Searching for (%s) in Field (%s) and Returning Field (%s)", valueToSearchFor, fieldToSearch, fieldToReturn));
        Iterator<Object> countryAttribute = this.countryYml.iterator();

        HashMap country;
        String searchableField;
        do {
            if (!countryAttribute.hasNext()) {
                return "";
            }

            Object item = countryAttribute.next();
            country = (HashMap) item;
            searchableField = (String) country.get(fieldToSearch);
        } while (!searchableField.equalsIgnoreCase(valueToSearchFor));

        LOGGER.info(String.format("Returning Field Value: %s", country.get(fieldToReturn)));
        return country.get(fieldToReturn);
    }

    /**
     * Returns the correct language code for EN, ES, BR, and FR depending on locale setting in the driver.
     * Otherwise it returns the language code set in the driver.
     */
    private void setUserDictionaryLanguage(String configValue) {
        switch (configValue) {
            case "en":
                setEnglishRuntimeValue();
                break;
            case "es":
                setSpanishRuntimeValue();
                break;
            case "fr":
                setFrenchRuntimeValue();
                break;
            case "pt":
                setPortugeseRuntimeValue();
                break;
            case "zh":
                setChineseRuntimeValue();
                break;
            default:
                this.userLanguage = configValue;
        }
    }

    /**
     * Sets the language code for the profile directly
     *
     * @param languageCode (en, en-GB, es-ES, es-419, etc.)
     */
    public void setProfileLanguage(String languageCode) {
        setUserDictionaryLanguage(languageCode);
    }

    public void setCountryDataByCode(String countryCode) {
        this.locale = countryCode;
        this.countryName = searchAndReturnCountryData(this.locale, CODE, COUNTRY).toString();
        setRatingSystem();
        setRatingValues();
        setSubscriberAgreementCountry();
    }

    public void setCountryDataByName(String countryName) {
        this.countryName = countryName;
        this.locale = searchAndReturnCountryData(countryName, COUNTRY, CODE).toString();
        setRatingSystem();
        setRatingValues();
        setSubscriberAgreementCountry();
    }

    private void setEnglishRuntimeValue() {
        setFallbackLanguage(countryName);
        this.userLanguage = getFallbackLanguage();
    }

    private void setSpanishRuntimeValue() {
        if (locale.equals("ES") || locale.equals("AD")) {
            this.userLanguage = "es-ES";
        } else {
            this.userLanguage = "es-419";
        }
    }

    private void setFrenchRuntimeValue() {
        if (locale.equals("CA")) {
            this.userLanguage = "fr-CA";
        } else {
            this.userLanguage = "fr-FR";
        }
    }

    private void setPortugeseRuntimeValue() {
        if (locale.equals("BR")) {
            this.userLanguage = "pt-BR";
        } else {
            this.userLanguage = "pt-PT";
        }
    }

    private void setChineseRuntimeValue() {
        if (locale.equals("TW")) {
            this.userLanguage = "zh-Hant";
        } else if(locale.equals("SG")) {
            this.userLanguage = "zh-Hans";
        } else {
            this.userLanguage = "zh-HK";
        }
    }

    private void setSupportedLanguages(String countryName) {
        this.supportedLangs = (List<String>) searchAndReturnCountryData(countryName, COUNTRY, "supportedLanguages");
    }

    public void setCountryName() {
        this.countryName = searchAndReturnCountryData(locale, "code", COUNTRY).toString();
    }

    /**
     * In order to reduce the amount of requests to the service, a condition is used
     * to set Fallback to the same as the User dictionary if the user's set language
     * is the same as the fallback language for the country.
     */
    public void setDictionaries() {
        if (this.masterDictionaryNode == null) {
            this.masterDictionaryNode = apiHandler.getFullDictionaryBody(getUserLanguage());

            parseMasterDictionary(getMasterDictionaryNode(), true);
            if (getUserLanguage().equals(getFallbackLanguage())) {
                this.fallbackDictionaries = this.primaryDictionaries;
            } else {
                this.fallbackDictionaryNode = apiHandler.getFullDictionaryBody(getFallbackLanguage());
                parseMasterDictionary(getFallbackDictionaryNode(), false);
            }
        } else {
            LOGGER.debug("User dictionaries are already built. Pinging host server is not required.");
        }
    }

    private void parseMasterDictionary(JsonNode masterFile, boolean isPrimary) {
        JSONObject data = new JSONObject();
        for (String key : DisneyParameters.getResourceKeys()) {
            try {
                data = parseResourceKeyData(masterFile, key);
            } catch (Exception e) {
                LOGGER.info(String.format("Key '%s' was not returned in the API call. Contact TPM for more information. Pulling unpinned version as backup.", key));

                try {
                    data = parseResourceKeyData(apiHandler.getDictionaryBody(getUserLanguage(), key, true), key);
                } catch (Exception f) {
                    Assert.fail(String.format("Dictionary fetch failed. Verify resource key '%s' is correct.", key));
                }
            }
            if (isPrimary) {
                this.primaryDictionaries.put(key, data);
            } else {
                this.fallbackDictionaries.put(key, data);
            }
        }
    }

    private JSONObject parseResourceKeyData(JsonNode node, String key) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject dictionary = (JSONObject) jsonParser.parse(String.valueOf(node));
        JSONObject data = (JSONObject) ((JSONArray) jsonParser.parse(JsonPath.read(dictionary, String.format("$..[?(@.resourceKey=='%s')]", key)).toString())).get(0);
        JSONObject entries = (JSONObject) ((JSONArray) jsonParser.parse(JsonPath.read(dictionary, String.format("$..[?(@.resourceKey=='%s')].entries_json", key)).toString())).get(0);
        data.remove("entries_json");
        data.putAll(entries);
        return data;
    }

    //Returns the potential 'no purchase' welcome screen subtext values
    public List<String> getNoPurchaseSubtextValues(boolean isPrimary) {
        LinkedList<String> values = new LinkedList<>();
        Map<String, String> dictionary;
        if (isPrimary) {
            dictionary = this.primaryDictionaries.get(DisneyParameters.getPaywallKey());
        } else {
            dictionary = this.fallbackDictionaries.get(DisneyParameters.getPaywallKey());
        }

        for (String key : dictionary.keySet()) {
            if (key.startsWith("welcome_subcta_loginonly_copy")) {
                String value = getPaywallItem(key);
                if (!value.isEmpty()) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    public String getSiteConfig(String language) {
        return apiHandler.getOneIdSiteConfigurations(language);
    }

    //Specialized Methods
    public String getAccessProfileString(String profileName) {
        return getPconItem("accessibility_whoswatching_selectprofile")
                .replace("${user_profile}", profileName);
    }

    public String getEditProfileString(String profileName) {
        return getAccessibilityItem("editprofiles_edit")
                .replace("${user_profile}", profileName);
    }

    /**
     * Replaces any placeholder text within a given String returned from the dictionary
     *
     * @param original - The original String pulled from the Dictionary
     * @param values   - The values you want to insert.
     *                 - Passing 1 value will apply that value to every placeholder.
     *                 - Passing Multiple values MUST match the number of placeholders being replaced!
     * @return - The modified String
     */
    public String replaceValuePlaceholders(String original, String... values) {
        StringBuilder builder = new StringBuilder();
        List<String> split = Arrays.asList(original.split("\\$"));
        builder.append(split.get(0));
        for (var i = 1; i < split.size(); i++) {
            String fragment = split.get(i);
            if(values.length ==  1) {
                fragment = fragment.replace(StringUtils.substringBetween(fragment, "{", "}"), values[0]);
            } else {
                fragment = fragment.replace(StringUtils.substringBetween(fragment, "{", "}"), values[i - 1]);
            }
            fragment = fragment.replace("{", "").replace("}", "");
            builder.append(fragment);
        }
        return builder.toString();
    }

    /**
     * Used for returning a dictionary's String value up to the point of the first placeholder.
     * Useful for dynamic values such as device storage space
     *
     * @param rawValue - The original String
     * @return - The string up to that point
     */
    public String getValueBeforePlaceholder(String rawValue) {
        return StringUtils.substringBefore(rawValue, "${").trim();
    }

    /**
     * Similar to returning before placeholder, this returns Strings after the closing brace
     * of the first found placeholder.
     *
     * @param rawValue - The original String
     * @return - The String after that point
     */
    public String getValueAfterPlaceholder(String rawValue) {
        return StringUtils.substringAfter(rawValue, "}").trim();
    }

    public void setLegalDocuments() {
        setLegalDocuments(getLocale());
    }

    public void setLegalDocuments(String locale) {
        String language;
        if(isSelectedLanguageSupported()) {
            language = getUserLanguage();
        } else {
            language = getFallbackLanguage();
        }

        String siteConfig = getSiteConfig(String.format("%s_%s", language, locale));
        JsonNode allDocuments = getLegalItems(siteConfig, ALL_LEGAL_DOCUMENTS);
        IntStream.range(0, allDocuments.size()).forEach(i -> {
            String header = apiHandler.queryResponse(allDocuments, LABELS).get(i);
            this.legalHeaders.add(header);
            String document = getLegalDocument(apiHandler.queryResponse(allDocuments, DOCUMENT_CODE).get(i));
            this.legalDocuments.put(header, getLegalText(document));
        });
    }

    public Set<String> getLegalHeaders() {
        return this.legalHeaders;
    }

    public Map<String, String> getLegalDocuments() {
        return this.legalDocuments;
    }

    public String getLegalDocumentBody(String header) {
        return this.legalDocuments.get(header);
    }
}
