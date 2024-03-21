package com.disney.qa.tests.disney.apple;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.disney.jarvisutils.pages.apple.JarvisAppleTV;
import com.disney.jarvisutils.pages.apple.JarvisHandset;
import com.disney.jarvisutils.pages.apple.JarvisTablet;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.proxy.GeoedgeProxyServer;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
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
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.DateUtils;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for both DisneyBaseTest (mobile) and DisneyPlusAppleTVBaseTest (TVOS)
 */
@SuppressWarnings("squid:S2187")
public class DisneyAppleBaseTest extends AbstractTest implements IOSUtils {

    private static final ThreadLocal<ITestContext> localContext = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final int SHORT_TIMEOUT = 5;
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
    public static final String SUBSCRIPTION_V2 = "V2";
    public static final String SUBSCRIPTION_V3 = "V3";
    public static final String SUBSCRIPTION_V2_ORDER = "V2-ORDER";
    public static final String ZEBRUNNER_XRAY_TEST_KEY = "com.zebrunner.app/tcm.xray.test-key";
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
    private static final LazyInitializer<DisneyLocalizationUtils> LOCALIZATION_UTILS = new LazyInitializer<>() {
        @Override
        protected DisneyLocalizationUtils initialize() {
            DisneyLocalizationUtils disneyLocalizationUtils;
            if (StringUtils.equalsIgnoreCase(DisneyConfiguration.getDeviceType(), "tvOS")) {
                disneyLocalizationUtils = new DisneyLocalizationUtils(getCountry(), getLanguage(), "apple-tv", "prod",
                        DisneyConfiguration.getPartner());
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
                    .partner(DisneyConfiguration.getPartner())
                    .useMultiverse(Configuration.getRequired(DisneyConfiguration.Parameter.USE_MULTIVERSE, Boolean.class))
                    .multiverseAccountsUrl(Configuration.getRequired(DisneyConfiguration.Parameter.MULTIVERSE_ACCOUNTS_URL))
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
            return new DisneySearchApi(APPLE, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DisneyConfiguration.getPartner());
        }
    };

    private static final LazyInitializer<EmailApi> EMAIL_API = new LazyInitializer<>() {
        @Override
        protected EmailApi initialize() throws ConcurrentException {
            return new EmailApi();
        }
    };

    private static final ThreadLocal<ZebrunnerProxyBuilder> PROXY = new ThreadLocal<>();

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
            // Xray.enableRealTimeSync();
        });
    }

    @BeforeSuite(alwaysRun = true)
    public void initPageDictionary() {
        //todo remove this configuration method
        DisneyPlusApplePageBase.setDictionary(getLocalizationUtils());
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

    @AfterMethod(alwaysRun = true)
    public void clearDisneyAppleBaseTest() {
        DISNEY_ACCOUNT.remove();
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

    private void limitDevicePoolForIOS17() {
        LOGGER.warn("Limiting device pool for IOS 17 only...");
        List<String> devices = List.of(R.CONFIG.get("capabilities.deviceName").split(","));
        String subset = localContext.get().getCurrentXmlTest().getParameter("iapDevices");
        LOGGER.info("Config Devices: {}", devices);
        if (devices.get(0).equals("any")) {
            LOGGER.info("deviceName set to 'any.' Using full subset.");
            R.CONFIG.put("capabilities.deviceName", subset, true);
        } else {
            LOGGER.info("Specific device found. Checking for matching entry");
            List<String> iOS17Subset = List.of(subset.split(","));
            List<String> customList = new LinkedList<>();
            devices.forEach(device -> {
                if (iOS17Subset.contains(device)) {
                    customList.add(device);
                }
            });

            if (customList.isEmpty()) {
                Assert.fail("No valid devices were provided for IAP test. Leave deviceName=any or set to valid devices: " + subset);
            } else {
                R.CONFIG.put("capabilities.deviceName", String.join(",", customList), true);
            }
        }
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
