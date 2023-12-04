package com.disney.qa.tests.disney.apple;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import com.browserup.bup.proxy.CaptureType;
import com.disney.jarvisutils.pages.apple.JarvisAppleTV;
import com.disney.jarvisutils.pages.apple.JarvisHandset;
import com.disney.jarvisutils.pages.apple.JarvisTablet;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.disney.DisneyPlusOverrideKeys;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.config.DisneyConfiguration;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.DisneyProductData;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.util.disney.DisneyGlobalUtils;
import com.zebrunner.agent.core.registrar.Xray;
import com.zebrunner.carina.core.AbstractTest;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.jarvisutils.parameters.apple.JarvisAppleParameters;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.DateUtils;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for both DisneyBaseTest (mobile) and DisneyPlusAppleTVBaseTest (TVOS)
 */
@SuppressWarnings("squid:S2187")
public class DisneyAppleBaseTest extends AbstractTest implements IOSUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final String CHECKED = "Checked";
    protected static final String UNCHECKED = "Unchecked";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";
    public static final String APPLE = "apple";
    public static final String DISNEY = "disney";
    public static final String APP = "app";
    //Keeping this not to a specific plan name to support localization tests
    //Plan names in non-us countries might differ from that in us.
    public static final String BUNDLE_PREMIUM = "Yearly";
    public static final String BUNDLE_BASIC = "Disney+ With Ads, Hulu with Ads, and ESPN+";
    public static final String SUBSCRIPTION_V1 = "V1";
    public static final String SUBSCRIPTION_V3 = "V3";
    public static final String SUBSCRIPTION_V2_ORDER = "V2-ORDER";
    public static final String ZEBRUNNER_XRAY_TEST_KEY = "com.zebrunner.app/tcm.xray.test-key";
    private static final String COUNTRY = R.CONFIG.get("locale");
    private static final String LANGUAGE = R.CONFIG.get("language");
    private static final LazyInitializer<DisneyContentApiChecker> API_PROVIDER = new LazyInitializer<>() {
        @Override
        protected DisneyContentApiChecker initialize() {
            return new DisneyContentApiChecker();
        }
    };
    private static final LazyInitializer<DisneyMobileConfigApi> CONFIG_API = new LazyInitializer<>() {
        @Override
        protected DisneyMobileConfigApi initialize() {
            String version = AppCenterManager.getInstance()
                    .getAppInfo(R.CONFIG.get("capabilities.app"))
                    .getVersion();
            LOGGER.info("version:{}", version);
            if (StringUtils.equalsIgnoreCase(R.CONFIG.get("capabilities.deviceType"), "tvOS")) {
                return new DisneyMobileConfigApi("tvos", "prod", DisneyConfiguration.partner(), version);
            } else {
                return new DisneyMobileConfigApi(MobilePlatform.IOS, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY, version);
            }
        }
    };
    private static final LazyInitializer<DisneyLocalizationUtils> LOCALIZATION_UTILS = new LazyInitializer<>() {
        @Override
        protected DisneyLocalizationUtils initialize() {
            DisneyLocalizationUtils disneyLocalizationUtils;
            if (StringUtils.equalsIgnoreCase(R.CONFIG.get("capabilities.deviceType"), "tvOS")) {
                disneyLocalizationUtils = new DisneyLocalizationUtils(getCountry(), getLanguage(), "apple-tv", "prod", DisneyConfiguration.partner());
            } else {
                disneyLocalizationUtils = new DisneyLocalizationUtils(getCountry(), getLanguage(), MobilePlatform.IOS,
                        DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                        DISNEY);
            }

            disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
            disneyLocalizationUtils.setLegalDocuments();
            return disneyLocalizationUtils;
        }
    };

    private static final LazyInitializer<DisneyAccountApi> ACCOUNT_API = new LazyInitializer<>() {
        @Override
        protected DisneyAccountApi initialize() {
            ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                    .platform(APPLE)
                    .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                    .partner(R.CONFIG.get("partner"))
                    .useMultiverse(DisneyConfiguration.useMultiverse())
                    .multiverseAccountsUrl(R.CONFIG.get("multiverseAccountsUrl"))
                    .build();
            return new DisneyAccountApi(apiConfiguration);
        }
    };

    private static final ThreadLocal<DisneyAccount> DISNEY_ACCOUNT = ThreadLocal.withInitial(() -> {
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        return getAccountApi().createAccount(offer, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V1);
    });

    private static final LazyInitializer<DisneySearchApi> SEARCH_API = new LazyInitializer<>() {
        @Override
        protected DisneySearchApi initialize() {
            return new DisneySearchApi(APPLE, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), getContentApiChecker().getPartner());
        }
    };

    private static final LazyInitializer<EmailApi> EMAIL_API = new LazyInitializer<>() {
        @Override
        protected EmailApi initialize() throws ConcurrentException {
            return new EmailApi();
        }
    };

    @BeforeSuite(alwaysRun = true)
    public void ignoreStartupExceptions() {
        WebDriverConfiguration.addIgnoredNewSessionErrorMessages("timed out waiting for a node to become available");
    }

    @BeforeSuite(alwaysRun = true)
    public void setXRayExecution() {
        // QCE-545 Jenkins- XML: Set x-ray execution key dynamically

        /*
         * Register custom parameter in TestNG suite xml
         * <parameter name="stringParam::reporting.tcm.xray.test-execution-key::XRay test execution value" value="XWEBQAS-31173"/>
         *
         * Run a job and provide updated execution key if necessary
         */

        String xrayExectionKey = R.CONFIG.get("reporting.tcm.xray.test-execution-key");
        if (!xrayExectionKey.isEmpty() && !xrayExectionKey.equalsIgnoreCase("null")) {
            LOGGER.info("{} {} will be assigned to run", "reporting.tcm.xray.test-execution-key", xrayExectionKey);
            Xray.setExecutionKey(xrayExectionKey);
            // Xray.enableRealTimeSync();
        }
    }

    @BeforeSuite(alwaysRun = true)
    public void initPageDictionary() {
        DisneyPlusApplePageBase.setDictionary(getLocalizationUtils());
    }

    @AfterMethod(alwaysRun = true)
    public void clearDisneyAppleBaseTest() {
        DISNEY_ACCOUNT.remove();
    }

    public static String getCountry() {
        return COUNTRY;
    }

    public static String getLanguage() {
        return LANGUAGE;
    }

    public static DisneyContentApiChecker getContentApiChecker() {
        try {
            return API_PROVIDER.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static DisneyMobileConfigApi getConfigApi() {
        try {
            return CONFIG_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static DisneyLocalizationUtils getLocalizationUtils() {
        try {
            return LOCALIZATION_UTILS.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static DisneyAccountApi getAccountApi() {
        try {
            return ACCOUNT_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static EmailApi getEmailApi() {
        try {
            return EMAIL_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    /**
     * Get account<br>
     * <b>Unique for each test method</b>
     *
     * @return {@link DisneyAccount}
     */
    public static DisneyAccount getAccount() {
        return DISNEY_ACCOUNT.get();
    }

    public static void setAccount(DisneyAccount account) {
        DISNEY_ACCOUNT.set(account);
    }

    public static DisneySearchApi getSearchApi() {
        try {
            return SEARCH_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    ////////////////////////////

    protected BuildType buildType;
    protected Map<String, String> sessionBundles = new HashMap<>();

    //API Threads

    public enum BuildType {
        ENTERPRISE("com.disney.disneyplus.enterprise", JarvisAppleParameters.getEnterpriseBundle()),
        AD_HOC("com.bamtech.dominguez", JarvisAppleParameters.getAdhocBundle()),
        IAP("com.disney.disneyplus", JarvisAppleParameters.getIapBundle());

        private String disneyBundle;
        private String jarvisBundle;

        BuildType(String disneyBundle, String jarvisBundle) {
            this.disneyBundle = disneyBundle;
            this.jarvisBundle = jarvisBundle;
        }

        public String getDisneyBundle() {
            return this.disneyBundle;
        }

        public String getJarvisBundle() {
            return this.jarvisBundle;
        }
    }

    public void setBuildType() {
        sessionBundles.put(APP, R.CONFIG.get("capabilities.app"));
        LOGGER.info("App Download: {}", sessionBundles.get(APP));
        if (sessionBundles.get(APP).contains("Enterprise")) {
            buildType = BuildType.ENTERPRISE;
            sessionBundles.put(DISNEY, buildType.getDisneyBundle());
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleParameters.getEnterpriseBundle());
            removeAdHocApps();
            removePurchaseApps();
        } else if (sessionBundles.get(APP).contains("Disney-Ad-Hoc") || sessionBundles.get(APP).contains("Disney_IAP") || sessionBundles.get(APP)
                .contains("Disney_iOS_AdHoc")) {
            buildType = BuildType.IAP;
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleParameters.getIapBundle());
            removeEnterpriseApps();
            removeAdHocApps();
        } else {
            buildType = BuildType.AD_HOC;
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleParameters.getAdhocBundle());
            removeEnterpriseApps();
            removePurchaseApps();
        }
        sessionBundles.put(DISNEY, buildType.getDisneyBundle());
    }

    /**
     * Executes a launchApp command depending on the bundle being used in test.
     */
    public void relaunch() {
        LOGGER.info("Executing relaunch command...");
        launchApp(sessionBundles.get(DISNEY));
        pause(3);
    }

    private void removeEnterpriseApps() {
        LOGGER.info("Removing Enterprise apps");
        removeApp(BuildType.ENTERPRISE.getDisneyBundle());
        removeApp(BuildType.ENTERPRISE.getJarvisBundle());
    }

    private void removeAdHocApps() {
        LOGGER.info("Removing AdHoc apps");
        removeApp(BuildType.AD_HOC.getDisneyBundle());
        removeApp(BuildType.AD_HOC.getJarvisBundle());
    }

    private void removePurchaseApps() {
        LOGGER.info("Removing Purchase apps");
        removeApp(BuildType.IAP.getDisneyBundle());
        removeApp(BuildType.IAP.getJarvisBundle());
    }

    protected void installJarvis() {
        String platformName;

        if (StringUtils.equalsIgnoreCase(getDevice().getCapabilities().getCapability("deviceType").toString(), MobilePlatform.TVOS)) {
            platformName = MobilePlatform.TVOS;
        } else {
            platformName = getDevice().getCapabilities().getCapability("platformName").toString();
        }

        switch (buildType) {
        case ENTERPRISE:
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Dominguez-Jarvis-Enterprise/%s/enterprise/latest", platformName))
                    .getDirectLink());
            break;
        case AD_HOC:
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Dominguez-Jarvis/%s/adhoc/latest", platformName))
                    .getDirectLink());
            break;
        case IAP:
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Disney-Jarvis/%s/adhoc/latest", platformName))
                    .getDirectLink());
        }
    }

    public String getDate() {
        String date = DateUtils.now();
        return date.replace(":", "_");
    }

    /**
     * Starts a BrowserUp proxy session for the designated country with Basic Request and Response captures
     *
     * @param country - Country NAME to proxy to.
     */
    public void initiateProxy(String country) {
        initiateProxy(country, CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
    }

    /**
     * Starts a BrowserUp proxy session for the designated country with specified capture types
     *
     * @param country      - Country NAME to proxy to
     * @param captureTypes - Desired capture types to record
     */
    public void initiateProxy(String country, CaptureType... captureTypes) {
        GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
        geoedgeProxyFreshInstance.setProxyHostForSelenoid();
        Map<String, String> headers = new HashMap<>();

        String countryCode = new DisneyCountryData()
                .searchAndReturnCountryData(country,
                        "country",
                        "code");
        getDriver();
        DisneyGlobalUtils disneyGlobalUtils = new DisneyGlobalUtils();
        DisneyProductData productData = new DisneyProductData();
        boolean productHasLaunched = productData.searchAndReturnProductData("hasLaunched").equalsIgnoreCase("true");
        boolean countryHasNotLaunched = disneyGlobalUtils.getBooleanFromCountries(countryCode, "hasNotLaunched");

        if (DisneyParameters.getEnv().equalsIgnoreCase("prod")) {
            headers.put(DisneyHttpHeaders.DISNEY_STAGING, TRUE);
            if ((countryHasNotLaunched || !productHasLaunched)) {
                headers.put(DisneyHttpHeaders.BAMTECH_CDN_BYPASS, "21ea40fe-bdb5-4426-b134-66f98acb2b68");
            }
        }

        headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");

        boolean isStar = DisneyConfiguration.partner().equalsIgnoreCase("star");

        if (!isStar) {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
        } else {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY_STAR);
        }

        if ((countryHasNotLaunched || !productHasLaunched)) {
            if (!isStar) {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            } else {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
            }
            headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_CANONBALL_PREVIEW, "3Br5QesdzePvQEH");
        }

        boolean isGeoEdgeUnsupportedRegion = disneyGlobalUtils.getBooleanFromCountries(countryCode, "isGeoEdgeUnsupportedRegion")
                || disneyGlobalUtils.getBooleanFromCountries(countryCode, "isGeoEdgeSupportedRegionWithIssues");
        if (isGeoEdgeUnsupportedRegion) {
            headers.put(DisneyHttpHeaders.BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE, countryCode);
            if (isStar) {
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
                headers.put(DisneyHttpHeaders.BAMTECH_AKA_USER_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_PARTNER, DisneyConfiguration.partner());
            }
        } else if (!countryCode.equals("US")) {
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
        }
    }

    public JarvisAppleBase getJarvisPageFactory() {
        switch (currentDevice.get().getDeviceType()) {
        case APPLE_TV:
            return new JarvisAppleTV(getDriver());
        case IOS_PHONE:
            return new JarvisHandset(getDriver());
        case IOS_TABLET:
            return new JarvisTablet(getDriver());
        default:
            throw new IllegalArgumentException(String.format("Invalid device type %s. No factory is available", currentDevice.get().getDeviceType()));
        }
    }

}
