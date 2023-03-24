package com.disney.qa.tests;

import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.disney.DisneyPlusOverrideKeys;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.DisneyProductData;
import com.disney.util.disney.DisneyGlobalUtils;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;


@SuppressWarnings("squid:S2187")
public class BaseMobileTest extends BaseTest implements IMobileUtils {
    protected ThreadLocal<BrowserMobProxy> proxy = new ThreadLocal<>();

    protected static final String PARTNER = R.CONFIG.get("partner");
    protected static final String CHECKED = "Checked";
    protected static final String UNCHECKED = "Unchecked";
    private static final String BAMTECH_CDN_BYPASS_VALUE = "21ea40fe-bdb5-4426-b134-66f98acb2b68";
    private static final String BAMTECH_CANONBALL_PREVIEW_VALUE = "3Br5QesdzePvQEH";
    private static final String IS_GEOEDGE_UNSUPPORTED_REGION = "isGeoEdgeUnsupportedRegion";
    private static final String IS_GEOEDGE_SUPPORTED_REGION_WITH_ISSUES = "isGeoEdgeSupportedRegionWithIssues";

    protected static final String BROWSERMOB_PROXY = "browsermob_proxy";
    protected static final String TRUE = "true";
    protected static final String FALSE = "false";

    public WebDriver getCastedDriver() {
        WebDriver drv = getDriver();

        if (drv instanceof EventFiringWebDriver) {
            return ((EventFiringWebDriver) drv).getWrappedDriver();
        } else {
            return drv;
        }
    }

    /**
     * Searches the config at runtime for the platform being run before applying fullReset=true
     * due to issues with its use on iOS.
     */
    @BeforeSuite
    public void androidMobileCleanInstall() {
        if (R.CONFIG.get("capabilities.platformName").equalsIgnoreCase("ANDROID")) {
            R.CONFIG.put("capabilities.fullReset", "false");
            R.CONFIG.put("capabilities.enforceAppInstall", "true");
        }
    }

    //Starts a BrowserMob proxy session for the United States with Basic Request and Response captures
    public void initiateProxy() {
        initiateProxy("United States");
    }

    /**
     * Starts a BrowserMob proxy session for the designated country with Basic Request and Response captures
     *
     * @param country - Country NAME to proxy to.
     */
    public void initiateProxy(String country) {
        initiateProxy(country, CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
    }

    //TODO:Move this to a centralized location(identical to web)

    /**
     * Starts a BrowserMob proxy session for the designated country with specified capture types
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
        R.CONFIG.put("browsermob_proxy", "true");
        getDriver();
        DisneyGlobalUtils disneyGlobalUtils = new DisneyGlobalUtils();
        DisneyProductData productData = new DisneyProductData();
        boolean productHasLaunched = productData.searchAndReturnProductData("hasLaunched").equalsIgnoreCase("true");
        boolean countryHasNotLaunched = disneyGlobalUtils.getBooleanFromCountries(countryCode, "hasNotLaunched");

        if (DisneyParameters.getEnv().equalsIgnoreCase("prod")) {
            headers.put(DisneyHttpHeaders.DISNEY_STAGING, TRUE);
            if ((countryHasNotLaunched || !productHasLaunched)) {
                headers.put(DisneyHttpHeaders.BAMTECH_CDN_BYPASS, BAMTECH_CDN_BYPASS_VALUE);
            }
        }

        headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");

        boolean isStar = PARTNER.equalsIgnoreCase("star");

        if (!isStar) {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
        } else  {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY_STAR);
        }

        if ((countryHasNotLaunched || !productHasLaunched)) {
            if (!isStar) {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            } else {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
            }
            headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_CANONBALL_PREVIEW, BAMTECH_CANONBALL_PREVIEW_VALUE);
        }

        boolean isGeoEdgeUnsupportedRegion = disneyGlobalUtils.getBooleanFromCountries(countryCode, IS_GEOEDGE_UNSUPPORTED_REGION)
                || disneyGlobalUtils.getBooleanFromCountries(countryCode, IS_GEOEDGE_SUPPORTED_REGION_WITH_ISSUES);
        if (isGeoEdgeUnsupportedRegion) {
            headers.put(DisneyHttpHeaders.BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE, countryCode);
            if (isStar) {
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
                headers.put(DisneyHttpHeaders.BAMTECH_AKA_USER_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_PARTNER, PARTNER);
            }
        } else if (!countryCode.equals("US")) {
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
        }

        try {
            ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy(country));
            proxy.set(ProxyPool.getProxy());
            Set<CaptureType> captureTypeSet = new HashSet<>();
            if(captureTypes != null) {
                IntStream.range(0, captureTypes.length - 1).forEach(type -> captureTypeSet.add(captureTypes[type]));
            } else {
                proxy.get().setMitmDisabled(true);
            }
            proxy.get().addHeaders(headers);
            proxy.get().enableHarCaptureTypes(captureTypes);
            proxy.get().start(Integer.parseInt(getDevice().getProxyPort()));
        } catch (NullPointerException e) {
            e.printStackTrace();
            Assert.fail(String.format("Proxy Cannot be started for country '%s'. Manual validation is required.", country));
        }
    }
}