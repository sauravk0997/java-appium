package com.disney.qa.tests.dgi.mobile;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.api.disney.DisneyPlusOverrideKeys;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.DisneyLanguageUtils;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseMobileDgiTest extends BaseMobileTest {
    protected DisneyAccount entitledUser;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String PARTNER = "disney";

    private static final String HOME_SLUG = R.TESTDATA.get("disney_home_content_class");

    List<String> homePageCarouselFilters = Arrays.asList("Home", "${title}", "Continue Watching", "Recommended For You");

    protected static final String FUNKO_EPISODE = "1. Spellbound";
    protected static final String MARVEL_FUNKO_SHORTS = "Marvel Funko Shorts";
    protected static final String MARVEL_FUNKO_PAGE_KEY = "1zpXcAgarSdO";
    protected static final String MARVEL_FUNKO_URL = String.format("%s/series/marvel-funko-shorts/%s", R.TESTDATA.get("disney_prod_discover_deeplink"), MARVEL_FUNKO_PAGE_KEY);
    protected static final String ZENIMATION = "Zenimation";
    protected static final String ZENIMATION_PAGE_KEY = "6hSv4CBT2Q3N";
    protected static final String ZENIMATION_URL = String.format("%s/series/zenimation/%s", R.TESTDATA.get("disney_prod_discover_deeplink"), ZENIMATION_PAGE_KEY);
    protected static final String ZENIMATION_EPISODE = "1. Water";
    protected static final String MULAN_2020_URL = R.TESTDATA.get("disney_prod_mulan_deeplink");

    protected String[] events = R.CONFIG.get("custom_string").split(",");

    protected String pageLoadError = "%s page did not load. Cannot validate dust entries.";

    private int retryCount = Integer.valueOf(R.CONFIG.get("retry_count"));
    private int testAttempt = 0;

    public ITestResult testResult;

    protected String language;
    protected String country;

    protected DisneyLanguageUtils languageUtils;

    @BeforeMethod
    public void initiateProxy(){
        GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
        geoedgeProxyFreshInstance.setProxyHostForSelenoid();
        R.CONFIG.put("browsermob_proxy", "true");
        getDriver();
        if(new AndroidUtilsExtended().isWifiEnabled()) {
            Map<String, String> headers = new HashMap<>();
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);

            Set<CaptureType> captureTypes = new HashSet<>();
            captureTypes.add(CaptureType.REQUEST_CONTENT);

            ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy("United States"));
            BrowserMobProxy proxy =  ProxyPool.getProxy();
            proxy.addHeaders(headers);
            proxy.enableHarCaptureTypes(captureTypes);
            proxy.start(Integer.parseInt(getDevice().getProxyPort()));
        } else {
            LOGGER.info("WiFi is not running on this device. Proxy will not be run as data cannot be captured.");
        }
    }

    /*
     * Language codes used in the D+ dictionaries are unique and cannot be used to actually launch the app.
     * This sets the language code for the dictionary API to the correct value based on the locale being
     * tested.
     *
     * Defaults to en-GB if language is not currently supported per product designs. See Wiki for details
     * https://wiki.bamtechmedia.com/pages/resumedraft.action?draftId=76458870&draftShareId=c9ef23c7-2c93-4c6d-9693-42e3524f2394&
     */
    public void setLocationData(){
        country = R.CONFIG.get("locale");
        language = R.CONFIG.get("language");

        language = languageUtils.getUserLanguage();

        R.CONFIG.put("locale", language.replace("-", "_"));
        L10N.load();
    }

    public List<String> getHomePageContentHeaders(String platform, String env, String partner){
        DisneySearchApi searchApi = new DisneySearchApi(platform, env, partner);
        CollectionRequest request = CollectionRequest.builder()
                .maturity("1450")
                .slug(DisneyStandardCollection.HOME.getSlug())
                .contentClass(DisneyStandardCollection.HOME.getContentClass())
                .build();
        ContentCollection collection = searchApi.getCollection(request);
        List<String> content = new LinkedList<>();
        collection.getCollectionSetsInfo().forEach(entry -> content.add(entry.getContent()));
        homePageCarouselFilters.forEach(filter -> content.removeIf(header -> header.contains(filter)));
        return content;
    }

    /*
     * Prints out retry attempt starts to the log with a format 'Current Attempt of Max Attempts' for readability.
     * Resets testAttempt to 0 if max tries has already been reached for a given test.
     */
    @AfterMethod
    public void setReadableTestResult(ITestResult result){
        testResult = result;
    }
    
    public void printUserFriendlyRetry() {
        if (testResult != null) {
            if (testAttempt == retryCount) {
                testAttempt = 0;
            } else if (retryCount != 0 && testAttempt < retryCount && testResult.getStatus() == ITestResult.SKIP) {
                testAttempt++;
                LOGGER.info(String.format("******************** Previous test attempt failed. Attempting test retry %s of %s ********************", testAttempt, retryCount));
            }
        }
    }
}
