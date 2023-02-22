package com.disney.qa.common.jarvis.android;

import com.disney.qa.api.jarvis.JarvisParameters;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.UniversalUtils;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author bsuscavage
 *
 * Jarvis is a D+ companion app designed for overriding the app config for various internal
 * testing purposes, and should only be used when required (Multi Region testing, Premier Access displays via time travel, etc.).
 *
 * Wiki: https://wiki.disneystreaming.com/pages/viewpage.action?pageId=76450612
 */

public abstract class JarvisAndroidBase extends DisneyAbstractPage {

    @FindBy(id  = "com.disney.disneyplus.jarvis:id/main_content")
    private ExtendedWebElement mainContainer;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/select_dialog_listview")
    private ExtendedWebElement transformContainer;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/design_navigation_view")
    private ExtendedWebElement mainMenuContainer;

    @FindBy(xpath = "//*[@text='%s']/parent::*[@class='androidx.appcompat.widget.LinearLayoutCompat']")
    public ExtendedWebElement mainMenuItem;

    @FindBy(xpath = "//*[@text=\"%s\"]")
    private ExtendedWebElement textElement;

    @FindBy(xpath = "//*[@text=\"%s\"]/parent::*[@class='android.view.ViewGroup']")
    private ExtendedWebElement configOverrideGroup;

    @FindBy(xpath = "//*[@text=\"%s\"]/../following-sibling::*[@class='android.widget.LinearLayout']")
    private ExtendedWebElement configOverrideItem;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::*[@resource-id='com.disney.disneyplus.jarvis:id/transform_switch']")
    private ExtendedWebElement overrideTransformSwitch;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::*[@resource-id='com.disney.disneyplus.jarvis:id/transform_value']")
    private ExtendedWebElement overrideTransformValue;

    @FindBy(xpath = "//*[@text=\"%s\"]/following-sibling::*[@resource-id='com.disney.disneyplus.jarvis:id/transform_custom_value']")
    private ExtendedWebElement overrideTransformCustomValue;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus.jarvis:id/%s']")
    private ExtendedWebElement resourceIdElement;

    @FindBy(className = "android.widget.ImageButton")
    private ExtendedWebElement menuBtn;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/launch_disney")
    private ExtendedWebElement launchDisneyBtn;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/delete_button")
    private ExtendedWebElement deleteOverrideBtn;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/confirm_button")
    private ExtendedWebElement switchEnvBtn;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/reset_button")
    private ExtendedWebElement deloreanResetBtn;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/travel_button")
    private ExtendedWebElement deloreanTravelBtn;

    @FindBy(xpath = "//*[@text='%s']/parent::*[@class='android.view.ViewGroup']")
    private ExtendedWebElement environmentSwitchRadio;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/view_config")
    private ExtendedWebElement viewConfigBtn;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/launch_disney")
    private ExtendedWebElement disneyIcon;

    protected static boolean useGUI = false;

    public static final String SWITCH = "switch";
    public static final String TRANSFORM = "transform";
    public static final String CUSTOM = "custom";
    protected static final String DELOREAN = "delorean";

    private static final String PLAYBACK_ENGINE = "playbackEngine";
    private static final String UPNEXT = "upNext";
    private static final String CONTENT_API = "contentApi";
    private static final String UNIQUE_KEY = "uniqueKey";
    private static final String TRANSFORM_MAP = "transformMap";

    private static TreeMap<String, String[]> overridesToChange = new TreeMap<>();
    private static TreeMap<String, String[]> adbOverrides = new TreeMap<>();
    private static Map<String, String> timeCircuitsToChange = new HashMap<>();
    private static Map<String, String> fluxCapacitor = new HashMap<>();
    protected String currentGroup = "";

    protected JarvisAndroidBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getMenuBtn() {
        return menuBtn;
    }

    public ExtendedWebElement getMainMenuContainer() {
        return mainMenuContainer;
    }

    public ExtendedWebElement getMainMenuItem() {
        return mainMenuItem;
    }

    public ExtendedWebElement getLaunchDisneyBtn() {
        return launchDisneyBtn;
    }

    public ExtendedWebElement getMainContainer() {
        return mainContainer;
    }

    public ExtendedWebElement getConfigOverrideGroup() {
        return configOverrideGroup;
    }

    public ExtendedWebElement getConfigOverrideItem() {
        return configOverrideItem;
    }

    public ExtendedWebElement getOverrideTransformSwitch() {
        return overrideTransformSwitch;
    }

    public ExtendedWebElement getOverrideTransformValue() {
        return overrideTransformValue;
    }

    public ExtendedWebElement getOverrideTransformCustomValue() {
        return overrideTransformCustomValue;
    }

    public ExtendedWebElement getTransformContainer() {
        return transformContainer;
    }

    public ExtendedWebElement getTextElement() {
        return textElement;
    }

    public ExtendedWebElement getResourceIdElement() {
        return resourceIdElement;
    }

    public ExtendedWebElement getSwitchEnvBtn() {
        return switchEnvBtn;
    }

    public ExtendedWebElement getDeleteOverrideBtn() {
        return deleteOverrideBtn;
    }

    public ExtendedWebElement getDeloreanResetBtn() {
        return deloreanResetBtn;
    }

    public ExtendedWebElement getDeloreanTravelBtn() {
        return deloreanTravelBtn;
    }

    public ExtendedWebElement getEnvironmentSwitchRadio() {
        return environmentSwitchRadio;
    }

    public ExtendedWebElement getViewConfigBtn() {
        return viewConfigBtn;
    }

    @Override
    public boolean isOpened() {
        return textElement.format("JARVIS").isElementPresent();
    }

    public static void setUseGUI(Boolean status) {
        useGUI = status;
    }

    public boolean getUseGUI() {
        return useGUI;
    }

    /**
     * Opens the hamburger menu
     */
    public abstract void openMenu();

    /**
     * Opens the config overrides section
     */
    public abstract void openConfigOverrides();

    /**
     * Re-Opens the Config Overrides section to auto-close any expanded sections and automatically
     * place back at the top of the menu if the current opened group does not match the desird one.
     * @param header
     */
    public abstract void openConfigOverrideGroup(String header);

    /**
     * Opens the Delorean section
     */
    public abstract void openDelorean();

    /**
     * Uses the D+ launch button
     */
    public abstract void launchDisneyPlus();

    /**
     * The vast majority of elements in Jarvis have recycled element IDs, so accessing them via
     * text value is the only way to interface with them.
     * @param text
     */
    public abstract void clickText(String text);

    /**
     * Sets the delorean parameters to desired values. Test setup should include all configurable items,
     * otherwise TRAVEL will not activate.
     * @param timeCircuit - The item to be interfaced with
     * @param value - The value to set it to
     */
    public abstract void setDeloreanData(String timeCircuit, String value);

    /**
     * Resets Delorean data to default
     */
    public abstract void resetDeloreanData();

    /**
     * Activates the flux capacitor
     */
    public abstract void activateDelorean();

    /**
     * Sets the runtime environment for Disney+ the same way the deprectated Environment Switcher app did
     */
    public abstract void setDisneyEnvironment(DisneyEnvironments environment);

    /**
     * Sets the runtime environment for Star+ the same way the deprectated Environment Switcher app did
     */
    public abstract void setStarEnvironment(StarEnvironments environment);

    /**
     * Method scrolls to the desired override text and sets the passed value.
     * Type check is due to the 3 different element identifiers and how the override is actually activated.
     * @param overrideItem - Text value of the override
     * @param type - switch, transform, or custom
     * @param value - desired value
     */
    public abstract void setOverrideValue(String overrideItem, String type, String value);

    /**
     * The methods here are used in conjuction with setOverrideValue after the type has been checked.
     * All 3 of them take the same params.
     * @param overrideItem
     * @param value
     */
    public abstract void setSwitchValue(String overrideItem, String value);

    public abstract void setTransformValue(String overrideItem, String value);

    public abstract void setCustomValue(String overrideItem, String value);

    /**
     * Clears out any set config overrides (does not apply to Env. Switcher)
     */
    public abstract void clearOverrides();

    public void launchJarvis(){
        AndroidService.getInstance().openApp(JarvisParameters.getJarvisLaunchAdbCommand());
        boolean isPresent = isOpened();
        UniversalUtils.captureAndUpload(getCastedDriver());
        if(!isPresent) {
            throw new NoSuchElementException("Jarvis elements are not present. App did not launch");
        }
    }

    public void activateOverrides(){
        if(useGUI) {
            AndroidService.getInstance().openApp(JarvisParameters.getJarvisLaunchAdbCommand());
            if(!overridesToChange.isEmpty()) {
                activateConfigOverrides();
            }
            if(!timeCircuitsToChange.isEmpty()) {
                activateDeloreanOverrides();
            }
        } else {
            broadcastOverrides();
        }
    }

    /**
     * Cycles through desired override values and then clears them out of memory
     * in the event a subsequent test needs to change values.
     */
    public void activateConfigOverrides() {
        if(useGUI) {
            for (Map.Entry<String, String[]> entry : overridesToChange.entrySet()) {
                launchJarvis();
                openConfigOverrideGroup(entry.getValue()[0]);
                setOverrideValue(entry.getKey(), entry.getValue()[1], entry.getValue()[2]);
            }
            clearOverrideMap();
        } else {
            broadcastOverrides();
        }
    }

    /**
     * Cycles through desired override values and then clears them out of memory
     * in the event a subsequent test needs to change values.
     */
    private void activateDeloreanOverrides() {
        openDelorean();
        timeCircuitsToChange.keySet().forEach(timeCircuit -> setDeloreanData(timeCircuit, timeCircuitsToChange.get(timeCircuit)));
        activateDelorean();
        timeCircuitsToChange.clear();
    }

    private void broadcastOverrides() {
        JSONArray overridesJson = new JSONArray();
        for(Map.Entry<String, String[]> entry : adbOverrides.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            JSONObject overrideGroup = new JSONObject();
            JSONObject transformObject = new JSONObject();

            if(values.length == 3) {
                String header = values[0];
                String custom = values[1];
                String overrideString = values[2];
                if (overrideString.equals("true") || overrideString.equals("false")) {
                    transformObject.put(header, new JSONObject().put(custom, Boolean.parseBoolean(overrideString)));
                } else if (header.equals(PLAYBACK_ENGINE) || header.equals(UPNEXT)) {
                    transformObject.put(header, new JSONObject().put(custom, Integer.parseInt(overrideString)));
                } else {
                    transformObject.put(header, new JSONObject().put(custom, overrideString));
                }
            } else {
                JSONObject object = new JSONObject(values[2]);
                transformObject.put(values[0], new JSONObject().put(values[1], object));
            }
            overrideGroup.put("name", key);
            overrideGroup.put(UNIQUE_KEY, values[0]);
            overrideGroup.put(TRANSFORM_MAP, transformObject);

            overridesJson.put(overrideGroup);
        }

        for(Map.Entry<String, String> timeCircuit : fluxCapacitor.entrySet()) {
            String key = timeCircuit.getKey();
            String value = timeCircuit.getValue();
            JSONObject overrideGroup = new JSONObject();
            JSONObject transformObject = new JSONObject();

            if(key.equals(DeloreanItems.COUNTRY.getItem())) {
                overrideGroup.put("name", "Search Country Override");
                transformObject.put(CONTENT_API, new JSONObject().put("X-GEO-OVERRIDE", value));
            } else {
                overrideGroup.put("name", "Search Date Override");
                transformObject.put(CONTENT_API, new JSONObject().put("X-DELOREAN", value));
            }
            overrideGroup.put(UNIQUE_KEY, CONTENT_API);
            overrideGroup.put(TRANSFORM_MAP, transformObject);
            overridesJson.put(overrideGroup);
        }

        executeBroadcast(overridesJson);
    }

    private void executeBroadcast(JSONArray overridesJson) {
        String response = AndroidService.getInstance().executeShell(
                JarvisParameters.getConfigOverrideCommand(
                        Base64.getEncoder().encodeToString(
                                overridesJson.toString().getBytes())));

        if(!response.contains("Transforms activated")) {
            LOGGER.error("ERROR. BROADCAST RESPONSE DID NOT HAVE EXPECTED VALUES. LAUNCHING JARVIS AND RETRYING...");
            launchJarvis();
            isOpened();

            response = AndroidService.getInstance().executeShell(
                    JarvisParameters.getConfigOverrideCommand(
                            Base64.getEncoder().encodeToString(
                                    overridesJson.toString().getBytes())));

            if (!response.contains("Transforms activated")) {
                throw new SecurityException("ADB Broadcast is not returning the expected output");
            }
        }
    }

    public void clearOverrideMap() {
        overridesToChange.clear();
    }

    public static void clearAdbOverrides() {
        adbOverrides.clear();
        fluxCapacitor.clear();
    }

    public static void clearTimeCircuits() {
        timeCircuitsToChange.clear();
    }

    /**
     * Basic enum to make determining the desired value for Switch types to be on or off.
     */
    public enum SwitchStatus{
        ON("true"),
        OFF("false");

        private final String status;
        SwitchStatus(String status){
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public String getSwitch(SwitchStatus s) {
            return s.getStatus();
        }
    }

    public enum DisneyEnvironments {
        PRODUCTION("Production", "DISNEY_PROD"),
        QA("QA", "DISNEY_QA"),
        EDITORIAL("Editorial", "DISNEY_EDITORIAL"),
        STAGING("Staging", "DISNEY_STAGING"),
        STAR_PRODUCTION("Star+ Production", "DISNEY_ON_STAR_PROD"),
        STAR_QA("Star+ QA", "DISNEY_ON_STAR_QA");

        private final String envSwitchItem;
        private final String envSwitchAdbValue;

        DisneyEnvironments(String envSwitchItem, String envSwitchAdbValue) {
            this.envSwitchItem = envSwitchItem;
            this.envSwitchAdbValue = envSwitchAdbValue;
        }

        public String getEnvSwitchItem() {
            return this.envSwitchItem;
        }

        public String getEnvSwitchAdbValue() {
            return this.envSwitchAdbValue;
        }
    }

    public enum StarEnvironments {
        PRODUCTION("Production", "STAR_PROD"),
        QA("QA", "STAR_QA"),
        EDITORIAL("Editorial", "STAR_EDITORIAL"),
        STAGING("Staging", "STAR_STAGING"),
        DISNEY_PRODUCTION("Disney+ Production", "STAR_ON_DISNEY_PROD"),
        DISNEY_QA("Disney+ QA", "STAR_ON_DISNEY_QA");

        private final String envSwitchItem;
        private final String envSwitchAdbValue;
        StarEnvironments(String envSwitchItem, String envSwitchAdbValue) {
            this.envSwitchItem = envSwitchItem;
            this.envSwitchAdbValue = envSwitchAdbValue;
        }

        public String getEnvSwitchItem() {
            return this.envSwitchItem;
        }

        public String getEnvSwitchAdbValue() {
            return this.envSwitchAdbValue;
        }
    }

    private interface Operation {
        void setOverrideValue(Object status);
    }

    public enum DeloreanItems {
        RESET("RESET"),
        TRAVEL("TRAVEL"),
        MONTH("month_field"),
        DAY("day_field"),
        YEAR("year_field"),
        HOUR("hour_field"),
        MINUTE("minute_field"),
        COUNTRY("location_field");

        private final String btn;
        DeloreanItems(String btn) {
            this.btn = btn;
        }

        public String getItem() {
            return this.btn;
        }
    }

    /**
     * Special config override functions that allow the Android device to set its Search API locale
     * to the desired location without the need for proxying as well as spoof a date to a desired value
     * (Useful for time-limited aspects or promotions, such as Premier Access displays)
     *
     * TIME_CIRCUITS: Special type that will apply all date related items in 1 command by parsing
     * the String provided, so long as it is a date syntax.
     *
     * All Other Fields: Update the value for the specific field. Useful if the test requires jumping between years
     * or Before/After days without changing anything else.
     */
    public enum Delorean implements Operation{
        COUNTRY {
            public void setOverrideValue(Object status) {
                timeCircuitsToChange.put(DeloreanItems.COUNTRY.getItem(), status.toString());
                fluxCapacitor.put(DeloreanItems.COUNTRY.getItem(), status.toString());
            }
        },
        TIME_CIRCUITS {
            public void setOverrideValue(Object status) {
                DateTime date = new DateTime(DateTime.parse(status.toString()));
                fluxCapacitor.put("date", date.toString());
                timeCircuitsToChange.put(DeloreanItems.MONTH.getItem(), String.valueOf(date.getMonthOfYear()));
                timeCircuitsToChange.put(DeloreanItems.DAY.getItem(), String.valueOf(date.getDayOfMonth()));
                timeCircuitsToChange.put(DeloreanItems.YEAR.getItem(), String.valueOf(date.getYear()));
                timeCircuitsToChange.put(DeloreanItems.HOUR.getItem(), String.valueOf(date.getHourOfDay()));
                timeCircuitsToChange.put(DeloreanItems.MINUTE.getItem(), String.valueOf(date.getMinuteOfHour()));
            }
        };

        Delorean() {

        }
    }

    private static String getSwitchStatus(Object object){
        if((boolean) object){
            return SwitchStatus.ON.getStatus();
        } else {
            return SwitchStatus.OFF.getStatus();
        }
    }

    public enum AccountSettings implements Operation{
        DISABLE_OTP_FOR_EMAIL {
            public void setOverrideValue(Object status){
                overridesToChange.put("Disable Force OTP for ChangeEmail", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_OTP_FOR_PASSWORD {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable Force OTP for ChangePassword", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        FORCE_ENABLE_HORIZONTAL_STACKING {
            public void setOverrideValue(Object status){
                overridesToChange.put("Horizontal Stacking - Force Enabled", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        FORCE_DISABLE_HORIZONTAL_STACKING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Horizontal Stacking - Force Disabled", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "AccountSettings";
        AccountSettings(){

        }
    }

    public enum BifDelay implements Operation {
        DELAY_FIRST_FRAME {
            public void setOverrideValue(Object status){
                overridesToChange.put("Delay until first frame", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DELAY_15_SECONDS {
            public void setOverrideValue(Object status){
                overridesToChange.put("Delay until 15s in buffer", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "BIF Delay";
        BifDelay() {

        }
    }

    public enum Collections implements Operation {
        HERO_FORCE_TYPE_FULL_BLEED {
            public void setOverrideValue(Object status){
                overridesToChange.put("Hero: Force ContainerType to 'her_full_bleed' (STB only)`", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        HERO_FORCE_TYPE_HERO {
            public void setOverrideValue(Object status){
                overridesToChange.put("Hero: Force ContainerType to 'hero'", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAP_EDITORIAL_TO_LARGE {
            public void setOverrideValue(Object status){
                overridesToChange.put("Map 'editorial' to 'editorialPanelLarge' for 'home' collection", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAP_EDITORIAL_TO_PANEL {
            public void setOverrideValue(Object status){
                overridesToChange.put("Map 'editorial' to 'editorialPanel' for 'home' collection", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAP_HERO_TO_SINGLE {
            public void setOverrideValue(Object status){
                overridesToChange.put("Map 'hero' to 'heroSingle' for 'home' collection", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAP_HERO_TO_INTERACTIVE {
            public void setOverrideValue(Object status){
                overridesToChange.put("Map 'hero' to 'heroInteractive' for 'home' collection", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAP_BECAUSE_YOU_WATCHED_TO_BLOCKBUSTER {
            public void setOverrideValue(Object status){
                overridesToChange.put("Map 'BecauseYouSet' to 'Blockbuster' for 'home' collection", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAP_RECOMMENDATION_TO_LOGOS {
            public void setOverrideValue(Object status){
                overridesToChange.put("Map 'RecommendationSet' to 'collectionLogos' for 'home' collection`", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Collections";
        Collections() {

        }
    }

    public enum ContentApiV5 implements Operation {
        ENABLE_PERSONALIZED_COLLECTION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable 'PersonalizedCollection' collectionSubType override on 'getCollection' endpoint for 'home' slug", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_V5 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable v5.0 for all endpoints (resets to v3.2)", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Content Api V5 (requires app version 1.13.x)";
        ContentApiV5() {

        }
    }

    public enum ContentApiV3 implements Operation {
        ENABLE_3_POINT_2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable 3.2 for all endpoints", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_SUPPORTED_VERSIONS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Supported Content Api Versions", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_ALL_CONTENT_API_ENDPOINTS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable All ContentApi endpoints", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        SEARCH_COUNTRY_OVERRIDE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Search Country Override", new String[]{HEADER, TRANSFORM, status.toString()});
            }
        };

        private static final String HEADER = "Content Api V3";
        ContentApiV3() {

        }
    }

    public enum ContentDetail implements Operation {
        SHOW_SHARE_BTN {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Show Share button", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        SHOW_BOOOKMARK_ON_SERIES_DETAILS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Show bookmark progress for episode tiles on series detail page.", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        FORCE_DETAIL_PAGE_PCON_BLOCK {
                public void setOverrideValue(Object status) {
                    overridesToChange.put("Force Detail Page PCON Blocked", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                }
        };

        private static final String HEADER = "Content Detail";
        ContentDetail(){

        }
    }

    public enum ContinueWatching implements Operation {
        DISABLE_CONTINUE_WATCHING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable ContinueWatchingSet Completely", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_SPARSE_CONTINUE_WATCHING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Sparse ContinueWatchingSet", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_SPARSE_CONTINUE_WATCHING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable Sparse ContinueWatchingSet", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "ContinueWatching";
        ContinueWatching(){

        }
    }

    public enum Conviva implements Operation {
        ENABLE_TOUCHSTONE {
          public void setOverrideValue(Object status) {
              overridesToChange.put("Enable Touchstone", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
          }
        };

        private static final String HEADER = "Conviva";
        Conviva(){

        }
    }

    public enum CountryCodeOverrides implements Operation {
        COUNTRY_CODE_OVERRIDE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("CountryCodeOverride\n(Devs only, not for Testing Purposes)", new String[]{HEADER, TRANSFORM, status.toString()});
            }
        },
        UPCOMING_SEARCH_LOCATION_OVERRIDES {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Upcoming Search location overrides", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "CountryCodeOverrides";
        CountryCodeOverrides(){

        }
    }

    public enum CTV_Activation implements Operation {
        ENABLE_V2_FLOW {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable V2 flow (License Plate)", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                adbOverrides.put("Enable V2 flow (License Plate)", new String[]{"ctvActivation", "v2Enabled", String.valueOf((boolean) status)});
            }
        },
        DISABLE_CTV_ACTIVATION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable CTV Activation all together", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                adbOverrides.put("Disable CTV Activation all together", new String[]{"ctvActivation", "enabled", String.valueOf(!(boolean) status)});
            }
        };

        public static final String HEADER = "CTV activation";
        CTV_Activation() {

        }
    }

    public enum CustomBuilds implements Operation {
        DISABLE_TIME_TRAVEL {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable hardcoded time travel", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_SUPPORTED_LOCATION_OVERRIDE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable hardcoded supportedLocation override", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        public static final String HEADER = "Custom Builds";
        CustomBuilds(){

        }
    }

    public enum DataSaver implements Operation {
        MODERATE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("7038k Moderate", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Data Saver";
        DataSaver(){

        }
    }

    public enum DebugMenu implements Operation {
        SHOW_DEBUG {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Show Debug Menu", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Debug Menu";
        DebugMenu(){

        }
    }

    public enum Deeplinks implements Operation {
        FORCE_DEEPLINK_TO_WEB {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Force deeplink to open on Web", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Deeplinks";
        Deeplinks(){

        }
    }

    public enum Dictionaries implements Operation {
        UNPIN_DICTIONARIES{
            public void setOverrideValue(Object status) {
                overridesToChange.put("Unpin Dictionaries", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                JSONObject dictionaries = new JSONObject();
                dictionaries.put("accessibility", "0.0");
                dictionaries.put("application", "0.0");
                dictionaries.put("decorations", "0.0");
                dictionaries.put("media", "0.0");
                dictionaries.put("paywall", "0.0");
                dictionaries.put("pcon", "0.0");
                dictionaries.put("ratings", "0.0");
                dictionaries.put("sdk-errors", "0.0");
                dictionaries.put("welch", "0.0");
                adbOverrides.put("Unpin Dictionaries", new String[]{"dictionaries", "versions", dictionaries.toString(), "true"});
            }
        },
        OVERRIDE_PAYWALL_DICTIONARY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Override Paywall Dictionary", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        OVERRIDE_DEFAULT_DICTIONARIES {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Override default dictionaries (for QA & Editorial)", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_DEBUG_DICTIONARY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Debug Dictionary", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                adbOverrides.put("Debug Dictionary", new String[]{"dictionaries", "debugDictionaryEnabled", getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Dictionaries";
        Dictionaries(){

        }
    }

    public enum DisneyAccountLogo implements Operation {
        HIDE_LOGO {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Hide the Disney Account Logo on Onboarding and Account pages", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Disney Account Logo";
        DisneyAccountLogo(){

        }
    }

    public enum EarlyContent implements Operation {
        DISABLE_EARLY_PURCHASE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable the Early content purchase", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        PURCHASE_TO_WEBSITE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Add a purchase URL to use Website instead of Paywall", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Early Content";
        EarlyContent() {

        }
    }

    public enum ForcedAppUpdates implements Operation {
        DISABLE_UPDATE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable Google In-App Update", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        FORCE_UPDATE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Force App Update screen", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Forced App Updates";
        ForcedAppUpdates() {

        }
    }

    public enum Glimpse implements Operation  {
        ENABLE_VALIDATION_ENDPOINT {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable validation endpoint", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Glimpse";
        Glimpse() {

        }
    }

    public enum GroupWatch implements Operation {
        ENABLE_ALL_REGIONS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable GroupWatch for all regions", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_MANUAL_INVITES {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable to create manually inviteLinks in the app", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_ALL_REGIONS_INVITELINKS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable GroupWatch for all regions plus inviteLinks", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        USE_BUFFERING_FOR_THRESHOLD {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Use buffering time for seek threshold", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        USE_THREE_SECOND_THRESHOLD {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Use 3 second seek threshold", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "GroupWatch";
        GroupWatch(){

        }
    }

    public enum InAppPurchaseAndPaywall implements Operation {
        DISABLE_AMAZON_IAP {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable Amazon IAP", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_GOOGLE_IAP {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable Google IAP", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MARKET_TIMEOUT_FIVE_MINUTES {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Market Timeout - Extra Long - 5 minutes", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        SKU_OVERRIDE_FREE_TRIAL_ONLY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("SKU Override - Free Trial Only", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        SKU_OVERRIDE_UNSUPPORTED_REGION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("SKU Override - Simulate Unsupported Region", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ALL_EU_LAUNCH_CURRENCIES {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Supported Currencies - All EU Launch Countries", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        NORDIC_LAUNCH_CURRENCIES {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Supported Currencies - All Nordic Launch Countries", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        EUR_CURRENCIES_ONLY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Supported Currencies - EUR Only", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        USD_CURRENIES_ONLY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Supported Currencies - USD Only", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "In App Purchases & Paywall";
        InAppPurchaseAndPaywall() {

        }
    }

    public enum LanguageSettingsFile implements Operation {
        USE_EU_VERSION_2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Use EU Launch version (2.0.0)", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        USE_BETA_FOR_NORDIC_AND_JAPAN {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Use beta/preview version for Nordic and Japanese language support", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Language Settings File";
        LanguageSettingsFile() {

        }
    }

    public enum LocationAvailability implements Operation {
        FORCE_UNSUPPORTED_LOCATION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Force Unsupported Location", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                adbOverrides.put("Force Unsupported Location", new String[]{"portability", "availabilityOverride", String.valueOf(!(boolean) status)});
            }
        },
        FORCE_SUPPORTED_LOCATION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Force Supported Location", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                adbOverrides.put("Force Supported Location", new String[]{"portability", "availabilityOverride", String.valueOf((boolean) status)});
            }
        };

        private static final String HEADER = "Location Availability";
        LocationAvailability() {

        }
    }

    public enum Localization implements Operation {
        REMOVE_REGIONAL_LANGUAGE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Remove regional language tags from GlobalizationApi requests", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ADD_REGIONAL_LANGUAGE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Add regional language tags from GlobalizationApi requests", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        GLOBALIZATION_API_VER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Globalization API Version", new String[]{HEADER, CUSTOM, status.toString()});
                adbOverrides.put("Globalization API Version", new String[]{"localization", "globalization", status.toString()});
            }
        },
        GLOBALIZATION_API_VER_1 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Globalization API 1.x.x", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
                adbOverrides.put("Globalization API 1.x.x", new String[]{"localization", "globalization", "1.x.x"});
            }
        };

        private static final String HEADER = "Localization";
        Localization() {

        }
    }

    public enum NegativeStereotype implements Operation {
        ENABLE_DISPLAY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Negative Stereotype copy (details) and interstitial (playback).", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Negative Stereotype";
        NegativeStereotype() {

        }
    }

    public enum PageVersions implements Operation {
        SEARCH_V2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Search page V2", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DETAIL_V2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Detail Page V2", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Page Versions";
        PageVersions() {

        }
    }

    public enum PartnerDeviceModels implements Operation {
        REMOVE_RESTRICTIONS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Remove all partner device paywall restrictions", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Partner Device Models";
        PartnerDeviceModels() {

        }
    }

    public enum ParentalControls implements Operation {
        KIDS_PROOF_EXIT {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Kids Proof Exit Feature", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MATURITY_RATING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Maturity Rating Feature", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        PROFILE_ACCESS_PIN {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Profile Access Pin Feature", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_TRAVELING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable User is Traveling Modal", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Parental Controls";
        ParentalControls() {

        }
    }

    public enum PaywallExperiences implements Operation {
        LOGIN {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Paywall Experience - Login", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        IAP {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Paywall Experience - IAP", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        PARTNER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Paywall Experience - Partner", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DIRECT_BILLING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Paywall Experience - Direct Billing (For future use)", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Paywall Experiences";
        PaywallExperiences() {

        }
    }

    public enum Performance implements Operation {
        LITE_MODE_OVERRIDE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Lite/High Mode Override", new String[]{HEADER, TRANSFORM, status.toString()});
            }
        };

        private static final String HEADER = "Performance";
        Performance() {

        }
    }

    public enum Playback implements Operation {
        ENABLE_PIP {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable PIP", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_HDR10 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable HDR10", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_H265 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable H265", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_HDR10_DOLBY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable HDR10 & Dolby Vision", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        DISABLE_HDR10_DOLBY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable HDR10 & Dolby Vision", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_ATMOS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Atmos", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        FORCE_L3 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Force L3", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_TUNNELED_PLAYBACK {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Tunneled Playback", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        AUTO_CHANNEL_MAX2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Audio Channels max 2", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        AUTO_CHANNEL_MAX6 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Audio Channels max 6", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        SEEK_TICKRATE_42MS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("SeekBar tickrate 42ms", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        SEEK_TICKRATE_200MS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("SeekBar tickrate 200ms", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        STEREOTYPE_COUNTDOWN_10S {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Negative Stereotype countdown duration 10 seconds", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        HIDE_TIMEOUT {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Chrome Fadeout in seconds", new String[]{HEADER, CUSTOM, status.toString()});
                adbOverrides.put("Hide Timeout in seconds", new String[]{PLAYBACK_ENGINE, "hideTimeoutSeconds", status.toString()});
            }
        },
        QUICK_CHROME_FADEOUT {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Quick Chrome Fadeout in seconds", new String[]{HEADER, CUSTOM, status.toString()});
                adbOverrides.put("Quick Chrome Fadeout in seconds", new String[]{PLAYBACK_ENGINE, "chromeQuickFadeOutSeconds", status.toString()});
            }
        };

        private static final String HEADER = "Playback";
        Playback() {

        }
    }

    public enum PlaybackPauseTimeout implements Operation{
        ENABLE_ALL_REGIONS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Pause Timeout for all regions", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        PAUSE_SECONDS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Pause Timeout for all regions", new String[]{HEADER, CUSTOM, status.toString()});
            }
        };

        private static final String HEADER = "Playback Pause Timeout";
        PlaybackPauseTimeout() {

        }
    }

    public enum PlaybackBuffering implements Operation {
        MIN_15_MAX_50 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("BufferSize: Default, MinBufferMs: 15s, MaxBufferMs: 50s", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MIN_15_MAX_15 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("BufferSize: Default, MinBufferMs: 15s, MaxBufferMs: 15s", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MIN_50_MAX_50 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("BufferSize: Default, MinBufferMs: 50s, MaxBufferMs: 50s", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Playback buffering";
        PlaybackBuffering() {

        }
    }

    public enum Profiles implements Operation {
        ENABLE_GRAPH {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable using Graph Service for fetching profiles", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Profiles";
        Profiles() {

        }
    }

    public enum PromoLabel implements Operation {
        DISABLE_COMING_SOON {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable Coming Soon state for seasons", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Promo Label";
        PromoLabel() {

        }
    }

    public enum SDK {
        ENABLE_EDGE_EVENTS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Events@Edge", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "SDK";
        SDK() {

        }
    }

    public enum SmartLockAutoLogin {
        DISABLE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Disable SmartLock AutoLogin", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "SmartLock AutoLogin";
        SmartLockAutoLogin() {

        }
    }

    public enum Subscriptions implements Operation {
        ENABLE_V2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable usage of Subscription API v2", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Subscriptions";
        Subscriptions() {

        }
    }

    public enum Surf implements Operation {
        ENABLE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable Surf", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "Surf";
        Surf() {

        }
    }

    public enum BtmpAppServices implements Operation {
        ENABLE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable BtmpAppServices", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "BTMP BtmpAppServices";
        BtmpAppServices() {

        }
    }

    public enum BtmpConfig implements Operation {
        ALLOW_HDR {
            public void setOverrideValue(Object status) {
                overridesToChange.put("AllowHDR", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ALLOW_DOLBY_VISION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("allowDolbyVision", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ALLOW_UHD {
            public void setOverrideValue(Object status) {
                overridesToChange.put("allowUHD", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ALLOW_ATMOS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("allowAtmos", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        PLAYBACK_SCENARIO {
            public void setOverrideValue(Object status) {
                overridesToChange.put("playbackScenario", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MAX_ALLOWED_CHANNELS {
            public void setOverrideValue(Object status) {
                overridesToChange.put("maxAllowedChanelCount", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        SUPPORT_H265_CODEC {
            public void setOverrideValue(Object status) {
                overridesToChange.put("supportsH265Codec", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAX_BITRATE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("maximumBitrateKbps", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        STARTING_BITRATE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("startingBitrateKbps", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MAX_RES_HEIGHT {
            public void setOverrideValue(Object status) {
                overridesToChange.put("maxResolutionHeight", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MIN_BUFFER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("minBufferMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MAX_BUFFER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("maxBufferMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        PLAYBACK_BUFFER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("bufferForPlaybackMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        PLAYBACK_REBUFFER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("bufferForPlaybackAfterRebufferMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MAX_BUFFER_BYTE_SIZE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("maxBufferByteSize", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MIN_DURATION_QUALITY_INCREASE {
            public void setOverrideValue(Object status) {
                overridesToChange.put("minDurationForQualityIncreaseMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        MIN_DURATION_RETAIN_AFTER_DISCARD {
            public void setOverrideValue(Object status) {
                overridesToChange.put("minDurationToRetainAfterDiscardMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        BANDWIDTH_FRACTION {
            public void setOverrideValue(Object status) {
                overridesToChange.put("bandwidthFraction", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        BUFFER_REEVAL_MIN_TIME {
            public void setOverrideValue(Object status) {
                overridesToChange.put("minTimeBetweenBufferReevaluationMs", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        BUFFER_TARGET_DURATION_MULTIPLIER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("bufferTargetDurationMultiplier", new String[]{HEADER, CUSTOM, status.toString()});
            }
        },
        BAMTRACK_SELECTION_LOGIC {
            public void setOverrideValue(Object status) {
                overridesToChange.put("shouldUseBAMTrackSelectionLogic", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_TUNNELED_PLAYBACK {
            public void setOverrideValue(Object status) {
                overridesToChange.put("enableTunneledVideoPlayback", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        ENABLE_BUFFER_COUNTER {
            public void setOverrideValue(Object status) {
                overridesToChange.put("enableBufferCounter", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "BTMP Config";
        BtmpConfig() {

        }
    }

    public enum UpNext implements Operation {
        ENABLE_V2 {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable V2 Up Next", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        MAX_CONSECUTIVE_MINUTES_AUTOPLAY {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Hours after auto play turns to click to play", new String[]{HEADER, CUSTOM, status.toString()});
                adbOverrides.put("Max Consecutive Minutes Auto Play", new String[]{UPNEXT, "maxConsecutiveMinutesAutoPlay", status.toString()});
            }
        };

        private static final String HEADER = "Up Next";

        UpNext() {

        }
    }

    public enum WeaponX implements Operation {
        WITH_CONTROL {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable local experiment '84a6a464&&Sub-Copy_on_Welcome_Page' with 'control' variant", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        },
        WITH_SAVINGS_MESSAGING {
            public void setOverrideValue(Object status) {
                overridesToChange.put("Enable local experiment '84a6a464&&Sub-Copy_on_Welcome_Page' with 'savings_messaging' variant", new String[]{HEADER, SWITCH, getSwitchStatus(status)});
            }
        };

        private static final String HEADER = "WeaponX - Local Experiments";
        WeaponX() {

        }
    }
}
