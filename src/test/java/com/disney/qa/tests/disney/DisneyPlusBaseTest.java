package com.disney.qa.tests.disney;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyApiCommon;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.disney.pojo.DisneyAccount;
import com.disney.qa.api.disney.pojo.DisneySubscriptions;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.carina.AllowListManager;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.DisneyProductData;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.BaseTest;
import com.disney.util.HARUtils;
import com.disney.util.disney.DisneyGlobalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.Configuration.Parameter;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.common.CommonUtils;
import com.qaprosoft.carina.core.foundation.webdriver.core.capability.impl.desktop.ChromeCapabilities;
import com.zebrunner.agent.core.registrar.CurrentTest;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.joda.time.DateTime;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import static com.disney.qa.disney.web.common.DisneyPlusBasePage.IS_STAR;

@SuppressWarnings("squid:S2187")
public class DisneyPlusBaseTest extends BaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final boolean ENABLE_ALLOW_LIST = R.CONFIG.getBoolean("enable_allow_list");
    private static final boolean ENABLE_FUZZBUCKET_SCALING = R.CONFIG.getBoolean("enable_fuzzbucket_scaling");
    protected static final String WEB_PLATFORM = "browser";
    protected static final String PROJECT = getProjectApiName();
    protected static final String US = "US";
    protected static final String NL = "NL";
    protected static final String DE = "DE";
    protected static final String MX = "MX";

    public static final String DIS = "DIS";
    public static final String STA = "STA";

    protected String language;
    protected ThreadLocal<BrowserMobProxy> proxy = new ThreadLocal<>();
    protected static final String CODE = "code";
    protected static final String COUNTRY = "country";
    protected static final String PROD = "PROD";
    protected static final String BETA = "BETA";
    protected static final String QA = "QA";
    protected static final String LIVE = "LIVE";
    protected static final String TRUNK = "TRUNK";
    protected static final String MAIN = "MAIN";

    public static final String SUB_VERSION_V1 = "V1";
    public static final String SUB_VERSION_V2 = "V2";
    public static final String SUB_VERSION_V2_ORDER = "V2-ORDER";
    public static final String MONTHLY = "Monthly";
    public static final String YEARLY = "Yearly";
    public static final String COMBO = "Combo";
    public static final String STANDALONE_MONTHLY_ADS = "Disney Plus Monthly - US - Web - With Ads";
    public static final String STANDALONE_MONTHLY_NO_ADS = "Disney Plus Monthly - US - Web";
    public static final String BUNDLE_PREMIUM = "Disney+, Hulu No Ads, and ESPN+";
    public static final String BUNDLE_BASIC = "Disney+ With Ads, Hulu with Ads, and ESPN+";

    public static final String EXP = "exp";
    public static final String CVV = "cvv";
    public static final String ZIP = "zip";
    public static final String ZIP_CA = "zip-ca";

    protected static final int ONE_SEC_TIMEOUT = 1;
    protected static final int SHORT_TIMEOUT = 3;
    protected static final int HALF_TIMEOUT = 5;
    protected static final int TIMEOUT = 10;
    protected static final int LONG_TIMEOUT = 15;
    protected static final int EXPLICIT_TIMEOUT = Configuration.getInt(Parameter.EXPLICIT_TIMEOUT); // 30

    protected DisneyAccount account = new DisneyAccount();
    protected DisneyCountryData countryData = new DisneyCountryData();
    protected DisneyProductData productData = new DisneyProductData();
    protected DisneyApiCommon disneyApiCommon = new DisneyApiCommon();
    protected DisneyGlobalUtils disneyGlobalUtils = new DisneyGlobalUtils();
    private static final boolean HAR_CAPTURE_ALL = R.CONFIG.getBoolean("proxy_har_capture_all");
    private static final boolean HAR_CAPTURE_ON_FAILURE = R.CONFIG.getBoolean("proxy_har_capture_on_failure");
    protected static final String ENVIRONMENT = R.CONFIG.get("env");
    private static final String APP_VERSION = R.CONFIG.get("app_version");
    protected static final List<String> proxiedEnvironments = Collections.unmodifiableList(
            Arrays.asList(PROD, BETA, QA));
    protected static final List<String> STAR_PLUS_SKIP_COUNTRIES = new ArrayList<>(Arrays.asList("US"));
    protected static final List<String> DISNEY_PLUS_SKIP_COUNTRIES = new ArrayList<>(Arrays.asList("MX"));
    protected static final List<String> EMPTY_SKIP_COUNTRIES_LIST = new ArrayList<>();

    //TODO: refactor as in DisneyPlusKidsModeTest...
    protected static final ThreadLocal<DisneyApiProvider> apiProvider = new ThreadLocal<>();

    protected final ThreadLocal<Boolean> isMobile = ThreadLocal.withInitial(() -> false);
    protected final ThreadLocal<Boolean> isOverride = ThreadLocal.withInitial(() -> false);

    @BeforeSuite(alwaysRun = true)
    public void unblockServersFromApiDetective() {
        if (IS_STAR) {
            DisneyAccountApi disneyAccountApi = new DisneyAccountApi(WEB_PLATFORM, ENVIRONMENT, PROJECT);
            List<String> elasticIps = DisneyParameters.returnQaAwsNatIps();
            DisneyContentApiChecker disneyContentApiChecker = new DisneyContentApiChecker(WEB_PLATFORM, ENVIRONMENT, PROJECT);
            for (int i = 3; i < elasticIps.size(); i++) {
                String ip = elasticIps.get(i);
                JsonNode jsonNode = disneyAccountApi.getLocationOverrideDetails(ip);
                String currentOverrideCountryCode = disneyContentApiChecker.queryResponse(jsonNode, "$..postalInfo.countryCode").get(0);
                if (!currentOverrideCountryCode.equalsIgnoreCase("mx")) {
                    try {
                        LOGGER.info("S+ MX override not found... overriding IP ({}) to Mexico", ip);
                        disneyAccountApi.overrideLocationByIp(ip, "MX");
                    } catch (Exception e) {
                        LOGGER.error("Override failed with the following Exception: {}", e.getMessage());
                    }
                }
            }
        }

        if (APP_VERSION.equalsIgnoreCase(TRUNK) && ENVIRONMENT.equalsIgnoreCase(QA)) {
            throw new SkipException(String.format("TRUNK - QA - not available for testing, skipping test suite"));
        } else {
            fuzzbucketScaler("10", "30");
            if (proxiedEnvironments.contains(ENVIRONMENT) && ENABLE_ALLOW_LIST) {
                AllowListManager allowListManager = new AllowListManager();
                for (String ip : DisneyParameters.returnQaAwsNatIps()) {
                    allowListManager.addIpToAllowList(ip);
                }
                GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
                DisneyApiProvider disneyApiProvider = new DisneyApiProvider();
                String countryIp = countryData.searchAndReturnCountryData(R.CONFIG.get("locale"), CODE, COUNTRY);
                disneyApiProvider.unblockJenkinsServers(geoedgeProxyFreshInstance.retrieveProxyCountryHost(countryIp));
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        if (ENABLE_FUZZBUCKET_SCALING && (locale.equalsIgnoreCase("US") || locale.equalsIgnoreCase("MX"))) {
            LOGGER.info("Scaling down fuzzbucket environments...");
            fuzzbucketScaler("1", "3");
        }
    }

    @Parameters({"isMobileUserAgent"})
    @BeforeMethod(alwaysRun = true)
    public void startProxy(@Optional("false") String isMobileUserAgent) {
        if (proxiedEnvironments.contains(ENVIRONMENT)) {
            GeoedgeProxyServer geoedgeProxyServer = new GeoedgeProxyServer();
            geoedgeProxyServer.setProxyHostForSelenoid();
            R.CONFIG.put("browsermob_proxy", "true");
        }

        isMobile.set("true".equalsIgnoreCase(isMobileUserAgent));
        LOGGER.info("isMobileUserAgent: " + isMobile());
        if (isMobile()) {
            LOGGER.info("Overriding chrome caps");
            DesiredCapabilities caps = null;
            caps = new ChromeCapabilities().getCapability("mobile-caps");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--user-agent='Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1'");
            // customize chrome args
            caps.setCapability(ChromeOptions.CAPABILITY, options);
            LOGGER.info("Override chrome caps (user-agent): " + caps);
            setCapabilities(caps);
        }
        setLanguageLocale();
        LOGGER.info("Starting to create api provider");
        try {
            apiProvider.set(new DisneyApiProvider());
        } catch (Exception e) {
            LOGGER.error("Error initializing DisneyApiProvider", e);
        }
        LOGGER.info("Finished Before Method");
    }

    @Override
    public WebDriver getDriver() {
        WebDriver drv = super.getDriver();

        /*
         * To Prevent error like below:
         *
         * 20:58:37  2021-08-26 17:58:37 ProxyToServerConnection [LittleProxy-5-ProxyToServerWorker-6-263] [ERROR] (AWAITING_INITIAL) [id: 0xaa1e3486, L:/10.199.1.131:38610 - R:stage-web.disneyplus.com/96.17.237.236:443]: Caught an exception on ProxyToServerConnection
         * 20:58:37  io.netty.handler.codec.TooLongFrameException: HTTP content length exceeded 2097152 bytes.
         */
        if (ProxyPool.isProxyRegistered()) {
            proxy.set(ProxyPool.getProxy());
            proxy.get().addFirstHttpFilterFactory(new HttpFiltersSourceAdapter() {
                @Override
                public int getMaximumRequestBufferSizeInBytes() {
                    return Integer.MAX_VALUE;
                }

                @Override
                public int getMaximumResponseBufferSizeInBytes() {
                    return Integer.MAX_VALUE;
                }
            });
        }

        if ((HAR_CAPTURE_ALL || HAR_CAPTURE_ON_FAILURE) || horaEnabled()) {
            // Capturing network data if har_proxy_enable = true
            // Captures on test failure if proxy_har_capture_on_failure = true
            proxy.set(ProxyPool.getProxy());
            proxy.get().enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
            proxy.get().newHar();
        }

        return drv;
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result) {
        LOGGER.info("Starting teardown...");
        if (result.getStatus() != ITestResult.SKIP) {
            endHarCaptureAndSave(result.isSuccess(), result.getName());
        }
        isMobile.remove();
        isOverride.remove();
        LOGGER.info("Finished teardown.");
    }

    protected void testCleanup(boolean isTestResultSuccess, com.disney.qa.api.pojos.DisneyAccount disneyAccount) {
        LOGGER.info("Starting testCleanup...");
        cancelOrder(disneyAccount);
        if (isTestResultSuccess) {
            removeLocationOverride(isOverride(), disneyAccount);
        }
        LOGGER.info("Finished testCleanup.");
    }

    protected boolean isMobile() {
        return this.isMobile.get();
    }

    protected boolean isOverride() {
        return this.isOverride.get();
    }

    protected void setOverride(boolean override) {
        this.isOverride.set(override);
    }

    protected void printAllAccountInformation(DisneyAccount account) {
        LOGGER.info("Account ID: {}", account.getAccountId());
        LOGGER.info("Device ID: {}", account.getDeviceId());
        LOGGER.info("Email: {}", account.getEmail());
        LOGGER.info("First Name: {}", account.getFirstName());
        LOGGER.info("Last Name: {}", account.getLastName());
        LOGGER.info("Profile ID: {}", account.getProfileId());
        LOGGER.info("Session ID: {}", account.getSessionId());
        LOGGER.info("User Name: {}", account.getUserName());
        LOGGER.info("IdentityPoint ID: {}", account.getIdentityPointId());
        LOGGER.info("Subscriptions: {}", account.getSubscriptions().size());
    }

    protected void cancelOrder(com.disney.qa.api.pojos.DisneyAccount account) {
        DisneyAccountApi disneyAccountApi = getAccountApi();
        if (account.getSubscriptions().stream().anyMatch(s -> s.getSubscriptionVersion().equalsIgnoreCase("V2-ORDER"))) {
            LOGGER.info("V2-Order found, cancelling account");

            if (!account.getAccountId().isEmpty()) {
                disneyAccountApi.getSubscriptions(account);
                for (com.disney.qa.api.pojos.DisneySubscriptions subscription : account.getSubscriptions()) {
                    String subscriptionGuid = subscription.getSubscriptionGuid();
                    if (!subscriptionGuid.isEmpty()) {
                        disneyAccountApi.cancelOrder(account);
                    } else {
                        LOGGER.info("Subscription GUID does not exist, skipping cancelOrder.");
                    }
                }
            } else {
                LOGGER.info("accountId does not exist, skipping getSubscriptions.");
            }
        }
    }

    //Remove Location Override for Users
    protected void removeLocationOverride(boolean override, com.disney.qa.api.pojos.DisneyAccount disneyAccount) {
        DisneyAccountApi disneyAccountApi = getAccountApi();
        if (override && !disneyAccount.getAccountId().isEmpty()) {
            LOGGER.info("Removing account location override");
            disneyAccountApi.removeLocationOverrides(disneyAccount);
        } else {
            LOGGER.info("No account location override required");
        }
    }

    protected static String getEnvironmentOnly() {
        String environment = R.CONFIG.get("env");
        try {
            environment = R.CONFIG.get("env").split("_")[1];
        } catch (IndexOutOfBoundsException e) {
            LOGGER.info(String.format("Environment passed has no `_` split, returning: %s", environment));
        }
        return environment;
    }

    protected JsonNode getFullDictionary() {
        return apiProvider.get().getFullDictionaryBody(language);
    }

    protected void setLanguageLocale() {
        language = R.CONFIG.get("language");
        locale = R.CONFIG.get("locale");
        LOGGER.info(String.format("Checking Language (%s) Locale (%s)", language, locale));
    }

    /**
     * Return an opaque handle to this window that uniquely identifies it within this driver instance.
     *
     * @return String name
     */
    protected String getWindowHandle(WebDriver driver) {
        String name;

        FluentWait<WebDriver> wait = new FluentWait<>(driver);
        wait.pollingEvery(Duration.ofMillis(Configuration.getInt(Parameter.RETRY_INTERVAL)));
        wait.withTimeout(Duration.ofSeconds(Configuration.getInt(Parameter.EXPLICIT_TIMEOUT)));
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(InvalidElementStateException.class);
        wait.ignoring(WebDriverException.class);

        Function<WebDriver, String> waitElement = arg0 -> {
            return driver.getWindowHandle();
        };
        name = wait.until(waitElement);
        return name;
    }

    /**
     * Return a set of window handles which can be used to iterate over all open windows of this WebDriver instance
     *
     * @return Set names
     */
    protected Set<String> getWindowHandles(WebDriver driver) {
        Set<String> names;

        FluentWait<WebDriver> wait = new FluentWait<>(driver);
        wait.pollingEvery(Duration.ofMillis(Configuration.getInt(Parameter.RETRY_INTERVAL)));
        wait.withTimeout(Duration.ofSeconds(Configuration.getInt(Parameter.EXPLICIT_TIMEOUT)));
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(InvalidElementStateException.class);
        wait.ignoring(WebDriverException.class);

        Function<WebDriver, Set<String>> waitElement = arg0 -> {
            return driver.getWindowHandles();
        };
        names = wait.until(waitElement);
        return names;
    }

    /**
     * @param result   Test result boolean. Pass = true, Fail = false
     * @param testName Name of Test currently running in thread
     */
    protected void endHarCaptureAndSave(boolean result, String testName) {
        if (!result || HAR_CAPTURE_ALL) {
            saveHar(testName);
        }
    }

    protected void endHarCaptureAndSave(String testName) {
        saveHar(testName);
    }

    /**
     * Saves har artifact either in root folder for local runs. Artifacts folder for CI
     * @param testName Name of test for proxy report saving
     */

    private void saveHar(String testName) {
        String fileName = testName + UUID.randomUUID();
        fileName = fileName + ".har";
        new HARUtils(proxy.get()).publishHAR(fileName);
    }

    public void skipTestByEnv(String env) {
        boolean shouldAllowAllTestsOnQa = productData.searchAndReturnProductData("shouldAllowAllTestsOnQa").equalsIgnoreCase("true");
        boolean countryHasNotLaunched = disneyGlobalUtils.getBooleanFromCountries(locale, "hasNotLaunched");
        boolean isQA = ENVIRONMENT.equalsIgnoreCase(QA);

        if ((shouldAllowAllTestsOnQa || countryHasNotLaunched) && isQA) {
            LOGGER.info(String.format("Test is not being skipped on QA for %s", productData.searchAndReturnProductData("abbrevName")));
        } else if (ENVIRONMENT.equalsIgnoreCase(env)) {
            CurrentTest.revertRegistration();
            throw new SkipException(String.format("Skipping test due to the test not working in the %s environment", env));
        }
    }

    public void skipTestByAppVersion(String appVersion) {
        if (APP_VERSION.equalsIgnoreCase(appVersion)) {
            CurrentTest.revertRegistration();
            throw new SkipException(String.format("Skipping test due to the test not working on the %s app version", appVersion));
        }
    }

    public void skipTestByProjectLocale(String locale, List<String> StarPlusLocalesToSkip, List<String> DisneyPlusLocalesToSkip) {
        boolean isStar = "STA".equalsIgnoreCase(DisneyGlobalUtils.getProject());
        List<String> localesToSkip = isStar ? StarPlusLocalesToSkip : DisneyPlusLocalesToSkip;
        if (localesToSkip.contains(locale)) {
            CurrentTest.revertRegistration();
            throw new SkipException(String.format("Skipping test in %s+ region: %s", productData.searchAndReturnProductData("abbrevName"), locale));
        }
    }

    public void skipPayPalTest(boolean shouldSkip) {
        if (shouldSkip) {
            CurrentTest.revertRegistration();
            throw new SkipException("Skipping test due to rate limiting for paypal");
        }
    }

    public void skipPayPalTestByProduct() {
        boolean isPaypalQACountry = disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_PAYPAL_QA_COUNTRY);
        if (!isPaypalQACountry && productData.searchAndReturnProductData("usePaypalMock").equalsIgnoreCase("true")) {
            skipTestByEnv(QA);
        } else {
            skipTestByEnv(PROD);
            skipTestByEnv(BETA);
        }
    }

    public void handleEASkips(boolean overrideSkip) {
        DateTime cruellaStartDate = DateTime.parse(disneyApiCommon.formatDateForQuery("2021-05-14T00:00:00.595-04:00"));
        DateTime cruellaEndDate = DateTime.parse(disneyApiCommon.formatDateForQuery("2021-07-30T00:00:00.595-04:00"));
        DateTime blackWidowStartDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-07-09T00:00:00.595-04:00"));
        DateTime blackWidowEndDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-09-10T00:00:00.595-04:00"));
        DateTime jungleCruiseStartDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-07-16T00:00:00.595-04:00"));
        DateTime jungleCruiseEndDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-10-01T00:00:00.595-04:00"));
        boolean isCruellaOnProd = disneyGlobalUtils.isDateWithinRange(cruellaStartDate, cruellaEndDate);
        boolean isBlackWidowOnProd = disneyGlobalUtils.isDateWithinRange(blackWidowStartDate, blackWidowEndDate);
        boolean isJungleCruiseOnProd = disneyGlobalUtils.isDateWithinRange(jungleCruiseStartDate, jungleCruiseEndDate);

        if (overrideSkip) {
            skipTestByEnv(BETA);
            skipTestByEnv(PROD);
        } else if (isCruellaOnProd || isBlackWidowOnProd || isJungleCruiseOnProd) {
            skipTestByEnv(QA);
        } else {
            skipTestByEnv(BETA);
            skipTestByEnv(PROD);
        }
    }

    public void skipEATest(boolean shouldNotSkip) {
        if (!shouldNotSkip) {
            throw new SkipException("Skipping test due to issues with the state of Early Access on QA");
        }
    }

    // This allows a test to skip if it is not on a specified app version or environment
    public void skipTestNotOnAppVersionOrEnv(String appVersion, String env) {
        LOGGER.info("App Version {}, ENV: {}", appVersion, env);
        if (!APP_VERSION.equalsIgnoreCase(appVersion)) {
            CurrentTest.revertRegistration();
            throw new SkipException(
                    String.format("Skipping test due the to this feature being present only on the '%s' app version", appVersion));
        } else if (ENVIRONMENT.equalsIgnoreCase(env)) {
            LOGGER.info("Env {}", env);
            CurrentTest.revertRegistration();
            throw new SkipException(
                    String.format("Skipping test due the to this feature not present on the %s environment", env));
        }
    }

    /**
     * Simple string converter to return full project name without spaces for fuzzbucket scaling
     * @return disneyplus or starplus as String
     */

    protected static final String getFullProject() {
        String project = DisneyGlobalUtils.getProject();
        return project.equalsIgnoreCase("DIS") ? "disneyplus" : "starplus";
    }

    protected static final String getProjectApiName() {
        String project = DisneyGlobalUtils.getProject();
        return project.equalsIgnoreCase("DIS") || project.equalsIgnoreCase(ANALYTICS_PROJECT_KEY) ? "disney" : "star";
    }

    protected static final String getPartnerProjectApiName() {
        String project = DisneyGlobalUtils.getProject();
        return project.equalsIgnoreCase("DIS") ? "star" : "disney";
    }

    /**
     * A simple Jenkins job to scale up Fuzzbucket environments primarily for the purpose of handling a high number of concurrent e2e testing threads.
     * https://github.bamtech.co/site-dominguez/fuzzbucket-scaling
     * We scale 3 environments: us-east-1, us-west-2, eu-west-1 concurrently
     * @throws URISyntaxException
     */

    private void fuzzbucketScaler(String minCap, String maxCap) {
        locale = R.CONFIG.get("locale");

        if (ENABLE_FUZZBUCKET_SCALING && (locale.equalsIgnoreCase("US") || locale.equalsIgnoreCase("MX"))) {
            if (APP_VERSION.equalsIgnoreCase(TRUNK) && ENVIRONMENT.equalsIgnoreCase(PROD)) {
                LOGGER.info("Trunk-Prod already scaled, skipping scaling job");
            } else {
                String EncodedCredentials = Base64.getEncoder().encodeToString((DisneyParameters.DISNEY_FUZZBUCKET_SCALING_CREDENTIALS.getDecryptedValue()).getBytes(StandardCharsets.UTF_8));
                String[] regions = {"us-east-1", "us-west-2", "eu-west-1"};

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.AUTHORIZATION, "Basic " + EncodedCredentials);

                RestTemplate restTemplate = RestTemplateBuilder
                        .newInstance()
                        .withSpecificJsonMessageConverter()
                        .withUtf8EncodingMessageConverter()
                        .build();

                for (String region : regions) {
                    URI urlQuery = UriComponentsBuilder.fromHttpUrl(DisneyParameters.DISNEY_FUZZBUCKET_SCALING_SERVICE.getValue())
                            .queryParam("APPLICATION", getFullProject())
                            .queryParam("POOL", APP_VERSION.toLowerCase() + "-" + ENVIRONMENT.toLowerCase())
                            .queryParam("ENVIRONMENT", PROD.toLowerCase())
                            .queryParam("REGION", region)
                            .queryParam("MIN_CAPACITY", minCap)
                            .queryParam("MAX_CAPACITY", maxCap)
                            .build().toUri();

                    RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.POST, urlQuery);
                    LOGGER.info("Fuzzbucket Scaling {}", request);

                    try {
                        restTemplate.exchange(request, String.class);
                        LOGGER.info("Successfully requested Fuzzbucket minimum capacity {}, maximum capacity {}, for {}", minCap, maxCap, region);
                    } catch (HttpClientErrorException exception) {
                        LOGGER.error(exception.getMessage());
                    }
                }
            }
        }
    }
    protected DisneyAccountApi getAccountApi() {
        String env = getEnvironmentOnly();
        String project = getProjectApiName();
        LOGGER.info("Getting API with ENV: " + env + " and project: " + project);
        return new DisneyAccountApi(WEB_PLATFORM, env, project);
    }

    protected DisneyAccountApi getPartnerAccountApi() {
        String env = getEnvironmentOnly();
        String project = getPartnerProjectApiName();
        LOGGER.info("Getting API with ENV: " + env + " and project: " + project);
        return new DisneyAccountApi(WEB_PLATFORM, env, project);
    }

    public void waitForSeconds(long secs) {
        LOGGER.info("Waiting for : " + secs + " seconds");
        CommonUtils.pause(secs);
    }

    /**
     * Retrieves the Proxy port for the current thread. Must be called after the BMP is started.
     * TODO: Move this to mitm-proxy-utils - QTE-1263
     */
    public int getProxyPort() {
        LOGGER.info("Retrieving Browser Mob Proxy Port");
        int port = ProxyPool.getProxy().getPort();
        LOGGER.info("Port:  {}", port);
        ProxyPool.getProxy().stop();
        return port;
    }

}

