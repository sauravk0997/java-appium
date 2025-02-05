package com.disney.qa.tests.disney.apple;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.disney.jarvisutils.pages.apple.JarvisAppleTV;
import com.disney.jarvisutils.pages.apple.JarvisHandset;
import com.disney.jarvisutils.pages.apple.JarvisTablet;
import com.disney.qa.api.account.*;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.client.requests.offer.*;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.explore.ExploreApi;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.offer.pojos.*;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.proxy.GeoedgeProxyServer;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.api.watchlist.*;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.common.utils.helpers.IAPIHelper;
import com.disney.util.JiraUtils;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.registrar.Xray;
import com.zebrunner.carina.core.AbstractTest;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.utils.exception.InvalidConfigurationException;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import com.zebrunner.carina.webdriver.proxy.ZebrunnerProxyBuilder;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.remote.options.SupportsAppOption;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.jarvisutils.parameters.apple.JarvisAppleParameters;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.DateUtils;
import com.zebrunner.carina.utils.R;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.*;

/**
 * Base class for both DisneyBaseTest (mobile) and DisneyPlusAppleTVBaseTest (TVOS)
 */
@SuppressWarnings("squid:S2187")
public class DisneyAppleBaseTest extends AbstractTest implements IOSUtils, IAPIHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final int SHORT_TIMEOUT = 5;
    protected static final String CHECKED = "Checked";
    protected static final String UNCHECKED = "Unchecked";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";
    public static final String APPLE = "apple";
    public static final String DISNEY = "disney";
    public static final String LANGUAGE = "language";
    public static final String APP = "app";
    //Keeping this not to a specific plan name to support localization tests
    //Plan names in non-us countries might differ from that in us.
    public static final String BUNDLE_PREMIUM = "Yearly";
    public static final String SUBSCRIPTION_V2 = "V2";
    public static final String ZEBRUNNER_XRAY_TEST_KEY = "com.zebrunner.app/tcm.xray.test-key";
    public static final String LATAM = "LATAM";
    public static final String EMEA = "EMEA";
    public static final String MPAA = "MPAA";

    private static final LazyInitializer<DisneyContentApiChecker> API_PROVIDER = new LazyInitializer<>() {
        @Override
        protected DisneyContentApiChecker initialize() {
            if (StringUtils.equalsIgnoreCase(DisneyConfiguration.getDeviceType(), "tvOS")) {
                return new DisneyContentApiChecker(MobilePlatform.TVOS, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                        DisneyConfiguration.getPartner());
            } else {
                return new DisneyContentApiChecker(MobilePlatform.IOS, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY);
            }
        }
    };
    private static final LazyInitializer<DisneyMobileConfigApi> CONFIG_API = new LazyInitializer<>() {
        @Override
        protected DisneyMobileConfigApi initialize() {
            String version = AppCenterManager.getInstance()
                    .getAppInfo(WebDriverConfiguration.getAppiumCapability(SupportsAppOption.APP_OPTION)
                            .orElseThrow(
                                    () -> new InvalidConfigurationException("The configuration must contains the 'capabilities.app' parameter.")))
                    .getVersion();
            LOGGER.info("version:{}", version);
            if (StringUtils.equalsIgnoreCase(DisneyConfiguration.getDeviceType(), "tvOS")) {
                return new DisneyMobileConfigApi(MobilePlatform.TVOS, "prod", DisneyConfiguration.getPartner(), version);
            } else {
                return new DisneyMobileConfigApi(MobilePlatform.IOS, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY, version);
            }
        }
    };

    private static final LazyInitializer<DisneySubscriptionApi> SUBSCRIPTION_API = new LazyInitializer<>() {
        @Override
        protected DisneySubscriptionApi initialize() {
            ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                    .platform(APPLE)
                    .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                    .partner(DisneyConfiguration.getPartner())
                    .useMultiverse(Configuration.getRequired(DisneyConfiguration.Parameter.USE_MULTIVERSE, Boolean.class))
                    .multiverseAccountsUrl(Configuration.getRequired(DisneyConfiguration.Parameter.MULTIVERSE_ACCOUNTS_URL))
                    .build();
            return new DisneySubscriptionApi(apiConfiguration);
        }
    };

    private final ThreadLocal<DisneyAccount> DISNEY_ACCOUNT = ThreadLocal.withInitial(() -> {
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        return getAccountApi().createAccount(offer, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2);
    });

    private static final ThreadLocal<DisneyAccountApi> ACCOUNT_API = ThreadLocal.withInitial(() -> {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform(APPLE)
                .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                .partner(DisneyConfiguration.getPartner())
                .useMultiverse(Configuration.getRequired(DisneyConfiguration.Parameter.USE_MULTIVERSE, Boolean.class))
                .multiverseAccountsUrl(Configuration.getRequired(DisneyConfiguration.Parameter.MULTIVERSE_ACCOUNTS_URL))
                .build();
        return new DisneyAccountApi(apiConfiguration);
    });

    private static final ThreadLocal<UnifiedAccountApi> UNIFIED_ACCOUNT_API = ThreadLocal.withInitial(() -> {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform(APPLE)
                .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                .partner(DisneyConfiguration.getPartner())
                .useMultiverse(Configuration.getRequired(DisneyConfiguration.Parameter.USE_MULTIVERSE, Boolean.class))
                .multiverseAccountsUrl(Configuration.getRequired(DisneyConfiguration.Parameter.MULTIVERSE_ACCOUNTS_URL))
                .build();
        return new UnifiedAccountApi(apiConfiguration);
    });

    private static final ThreadLocal<UnifiedSubscriptionApi> UNIFIED_SUBSCRIPTION_API = ThreadLocal.withInitial(() -> {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform(APPLE)
                .environment(Configuration.getRequired(Configuration.Parameter.ENV))
                .partner(DisneyConfiguration.getPartner())
                .useMultiverse(Configuration.getRequired(
                        DisneyConfiguration.Parameter.USE_MULTIVERSE, Boolean.class))
                .multiverseAccountsUrl(Configuration.getRequired(
                        DisneyConfiguration.Parameter.MULTIVERSE_ACCOUNTS_URL))
                .build();
        return new UnifiedSubscriptionApi(apiConfiguration);

    });

    private static final LazyInitializer<DisneySearchApi> SEARCH_API = new LazyInitializer<>() {
        @Override
        protected DisneySearchApi initialize() {
            return new DisneySearchApi(APPLE, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DisneyConfiguration.getPartner());
        }
    };

    private static final LazyInitializer<ExploreApi> EXPLORE_API = new LazyInitializer<>() {
        @Override
        protected ExploreApi initialize() {
            ApiConfiguration apiConfiguration = ApiConfiguration.builder().platform(APPLE).partner(DisneyConfiguration.getPartner())
                    .environment(DisneyParameters.getEnv()).build();
            return new ExploreApi(apiConfiguration);
        }
    };

    private static final LazyInitializer<WatchlistApi> WATCHLIST_API = new LazyInitializer<>() {
        @Override
        protected WatchlistApi initialize() {
            ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                    .platform(APPLE)
                    .partner(DisneyConfiguration.getPartner())
                    .environment(DisneyParameters.getEnv())
                    .build();
            return new WatchlistApi(apiConfiguration);
        }
    };

    private static final ThreadLocal<EmailApi> EMAIL_API = ThreadLocal.withInitial(EmailApi::new);
    ThreadLocal<UnifiedAccount> UNIFIED_ACCOUNT = new ThreadLocal<>();
    private static final ThreadLocal<ZebrunnerProxyBuilder> PROXY = new ThreadLocal<>();
    private static final ThreadLocal<ExploreSearchRequest> EXPLORE_SEARCH_REQUEST = ThreadLocal.withInitial(() -> ExploreSearchRequest.builder().build());

    @BeforeSuite(alwaysRun = true)
    public void ignoreDriverSessionStartupExceptions() {
        WebDriverConfiguration.addIgnoredNewSessionErrorMessages(Stream.concat(
                        Map.of(
                                "timed out waiting for a node to become available",
                                Duration.ofMinutes(25),
                                "lock file for downloading application has not disappeared after",
                                Duration.ofMinutes(15),
                                "Could not start a new session. Possible causes are invalid address of the remote server or browser start-up failure",
                                Duration.ofMinutes(15),
                                "Cannot download the app from",
                                Duration.ofMinutes(15),
                                "App is no installed among system apps",
                                Duration.ofMinutes(15),
                                "Failed to receive any data within the timeout",
                                Duration.ofMinutes(15),
                                "Unable to start WebDriverAgent session because of xcodebuild failure",
                                Duration.ofMinutes(15),
                                "java.util.concurrent.TimeoutException",
                                Duration.ofMinutes(10),
                                "Possible causes are invalid address of the remote server or browser start-up failure",
                                Duration.ofMinutes(10),
                                "Connection was refused to port",
                                Duration.ofMinutes(10)).entrySet().stream(),
                        Map.of("Error forwarding the new session cannot find", Duration.ofMinutes(10)).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
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
        Configuration.get(DisneyConfiguration.Parameter.REPORTING_TCM_XRAY_TEST_EXECUTION_KEY).ifPresent(key -> {
            LOGGER.info("{} {} will be assigned to run", "reporting.tcm.xray.test-execution-key", key);
            Xray.setExecutionKey(key);
        });
    }

    @BeforeSuite(alwaysRun = true)
    public final void cleanAppInstall() {
        R.CONFIG.put("capabilities.fullReset", "true");
    }

    @BeforeMethod(alwaysRun = true)
    public final void overrideLocaleConfig(ITestResult result) {
        List<String> groups = Arrays.asList(result.getMethod().getGroups());
        String country;
        if (groups.contains(US)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), US, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(AT)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), AT, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(AU)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), AU, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(BR)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), BR, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), PT_LANG, true);
        } else if (groups.contains(CA)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), CA, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(CH)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), CH, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(DE)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), DE, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), DE_LANG, true);
        } else if (groups.contains(JP)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), JP, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), JA_LANG, true);
        } else if (groups.contains(KR)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), KR, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), KO_LANG, true);
        } else if (groups.contains(NL)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), NL, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(NZ)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), NZ, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(SG)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), SG, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(TR)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), TR, true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), TR_LANG, true);
        } else if (groups.contains(LATAM)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), getLATAMCountryCode(), true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), ES_LANG, true);
        } else if (groups.contains(EMEA)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), getEMEACountryCode(), true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else if (groups.contains(MPAA)) {
            R.CONFIG.put(WebDriverConfiguration.Parameter.LOCALE.getKey(), getMPAACountryCode(), true);
            R.CONFIG.put(WebDriverConfiguration.Parameter.LANGUAGE.getKey(), EN_LANG, true);
        } else {
            throw new RuntimeException("No associated Locale and Language was found.");
        }
    }

    private String getLATAMCountryCode() {
        List<String> countryCodeList = Arrays.asList(ARGENTINA, BOLIVIA, CHILE, COLOMBIA, COSTA_RICA, DOMINICAN_REPUBLIC,
                ECUADOR, EL_SALVADOR, GUATEMALA, HONDURAS, MEXICO, NICARAGUA, PANAMA, PARAGUAY, PERU, URUGUAY);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size()));
    }

    private String getEMEACountryCode() {
        List<String> countryCodeList = Arrays.asList(FRANCE);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size()));
    }

    private String getMPAACountryCode() {
        List<String> countryCodeList = Arrays.asList(CANADA, UNITED_STATES, GUAM,
                PUERTO_RICO, MARSHALL_ISLANDS);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size()));
    }

    @BeforeMethod(onlyForGroups = TestGroup.PROXY, alwaysRun = true)
    public final void initProxy() {
        //todo enable when grid will be updated and devices will use proxy
        // R.CONFIG.put("proxy_type", "Zebrunner", true);
        ZebrunnerProxyBuilder builder;
        String countryCode = getCountry();
        boolean isUpstreamProxyNeeded = !countryCode.equalsIgnoreCase("US");
        boolean isGeoedgeUnsupportedRegion = GeoedgeProxyServer.getGeoEdgeIp(countryCode)
                .isEmpty();

        if (isGeoedgeUnsupportedRegion) {
            LOGGER.info("Starting proxy -- unsupported country, no upstream proxy");
            builder = ZebrunnerProxyBuilder.getInstance();
        } else if (isUpstreamProxyNeeded && !isGeoedgeUnsupportedRegion) {
            LOGGER.info("Starting proxy using upstream proxy through country {}", countryCode);
            builder = GeoedgeProxyServer.getGeoedgeProxy(countryCode);
        } else {
            LOGGER.info("Getting proxy not using upstream proxy for country {}", countryCode);
            builder = ZebrunnerProxyBuilder.getInstance();
        }
        builder.enableSSlInsecure();
        builder.useExtendedProxy();
        PROXY.set(builder);
        builder.build(true);
    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = TestGroup.ATV_JARVIS_CONFIGURATION)
    public void configureTVOSDeviceNameForJarvis(ITestContext context) {
        String xmlTVOSDeviceName = "tvOSDeviceName";
        String tvOSDeviceName = context.getCurrentXmlTest().getParameter(xmlTVOSDeviceName);

        if (tvOSDeviceName != null && !tvOSDeviceName.isEmpty()) {
            LOGGER.info("Disabling Jarvis Companion Config");
            R.CONFIG.put(CAPABILITIES_DEVICE_NAME, tvOSDeviceName);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void clearDisneyAppleBaseTest() {
        EMAIL_API.remove();
        ACCOUNT_API.remove();
        DISNEY_ACCOUNT.remove();
        LOCALIZATION_UTILS.clear();
        UNIFIED_ACCOUNT.remove();
        UNIFIED_ACCOUNT_API.remove();
        UNIFIED_SUBSCRIPTION_API.remove();
        CREATE_UNIFIED_ACCOUNT_REQUEST.remove();
    }

    @AfterSuite(alwaysRun = true)
    public final void postTestResultsToJira(ITestContext context) {
        JiraUtils.addTestRunURLtoJiraTicketComment(context);
    }

    public static String getCountry() {
        return WebDriverConfiguration.getLocale().getCountry();
    }

    public static String getLanguage() {
        return WebDriverConfiguration.getLocale().getLanguage();
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

    public static DisneyAccountApi getAccountApi() {
        return ACCOUNT_API.get();
    }

    public static UnifiedAccountApi getUnifiedAccountApi() {
        return UNIFIED_ACCOUNT_API.get();
    }

    public static DisneySubscriptionApi getSubscriptionApi() {
        try {
            return SUBSCRIPTION_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static UnifiedSubscriptionApi getUnifiedSubscriptionApi() {
        return UNIFIED_SUBSCRIPTION_API.get();
    }

    public static EmailApi getEmailApi() {
        return Objects.requireNonNull(EMAIL_API.get());
    }

    /**
     * Get account<br>
     * <b>Unique for each test method</b>
     *
     * @return {@link DisneyAccount}
     */
    public DisneyAccount getAccount() {
        return DISNEY_ACCOUNT.get();
    }

    public void setAccount(DisneyAccount account) {
        DISNEY_ACCOUNT.set(account);
    }

    public UnifiedAccount getUnifiedAccount() {
        return Objects.requireNonNull(UNIFIED_ACCOUNT.get());
    }

    public void setAccount(UnifiedAccount account) {
        UNIFIED_ACCOUNT.set(Objects.requireNonNull(account));
    }

    public UnifiedOfferRequest getUnifiedOfferRequest(String searchTerm) {
        UnifiedOfferRequest unifiedOfferRequest = UnifiedOfferRequest.builder()
                .country(getLocalizationUtils().getLocale())
                .partner(Partner.DISNEY)
                .searchText(searchTerm)
                .skuPlatform(SkuPlatform.WEB)
                .build();
        return unifiedOfferRequest;
    }

    public UnifiedOffer getUnifiedOffer(String planName) {
        return getUnifiedSubscriptionApi().lookupUnifiedOffer(getUnifiedOfferRequest(planName));
    }

    ThreadLocal<CreateUnifiedAccountRequest> CREATE_UNIFIED_ACCOUNT_REQUEST =
            ThreadLocal.withInitial(CreateUnifiedAccountRequest::new);

    public CreateUnifiedAccountRequest getDefaultCreateUnifiedAccountRequest() {
        return CREATE_UNIFIED_ACCOUNT_REQUEST.get();
    }

    public CreateUnifiedAccountRequest getCreateUnifiedAccountRequest(String planName) {
        return getDefaultCreateUnifiedAccountRequest()
                .setPartner(Partner.DISNEY)
                .addEntitlement(UnifiedEntitlement.builder().unifiedOffer(getUnifiedOffer(planName)).subVersion(UNIFIED_ORDER).build())
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());
    }

    public static DisneySearchApi getSearchApi() {
        try {
            return SEARCH_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static ExploreApi getExploreApi() {
        try {
            return EXPLORE_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static WatchlistApi getWatchlistApi() {
        try {
            return WATCHLIST_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public String getWatchlistInfoBlock(String entityId) {
        ExploreSearchRequest pageRequest = ExploreSearchRequest.builder()
                .disneyAccount(getAccount())
                .entityId(entityId.toString())
                .build();
        return getExploreApi().getWatchlistActionInfoBlock(pageRequest);
    }

    public String getWatchlistInfoBlockForUnifiedAccount(String entityId) {
        ExploreSearchRequest pageRequest = ExploreSearchRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .entityId(entityId)
                .build();
        return getExploreApi().getWatchlistActionInfoBlock(pageRequest);
    }

    public static ExploreSearchRequest getDisneyExploreSearchRequest() {
        return EXPLORE_SEARCH_REQUEST.get().setContentEntitlements(CONTENT_ENTITLEMENT_DISNEY);
    }

    public static ExploreSearchRequest getHuluExploreSearchRequest() {
        return EXPLORE_SEARCH_REQUEST.get().setContentEntitlements(CONTENT_ENTITLEMENT_HULU);
    }

    ////////////////////////////
    protected BuildType buildType;
    protected Map<String, String> sessionBundles = new HashMap<>();

    //API Threads

    @Getter
    public enum BuildType {
        ENTERPRISE("com.disney.disneyplus.enterprise", JarvisAppleParameters.getEnterpriseBundle()),
        AD_HOC("com.bamtech.dominguez", JarvisAppleParameters.getAdhocBundle()),
        IAP("com.disney.disneyplus", JarvisAppleParameters.getIapBundle());

        private final String disneyBundle;
        private final String jarvisBundle;

        BuildType(String disneyBundle, String jarvisBundle) {
            this.disneyBundle = disneyBundle;
            this.jarvisBundle = jarvisBundle;
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
                .contains("Disney_iOS_AdHoc") || sessionBundles.get(APP).contains("Disney-IAP-Prod-AdHoc-for-Automation")) {
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
