package com.disney.qa.tests.disney.web.redirect;

import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.preview.DisneyPlusPreviewPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import net.lightbody.bmp.BrowserMobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

public class DisneyPlusRedirectTest extends DisneyPlusBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @QTestCases(id = "23")
    @Test(dataProviderClass = DisneyCountryData.class, dataProvider = "generateCountriesToScan",
            description = "Check if Preview Splash Redirects Correctly")
    public void checkSplashRedirect(String TUID, String country, String countryCode, String live) {
        GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
        DisneyPlusPreviewPage page = new DisneyPlusPreviewPage(getDriver());

        ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy(country));

        int registeredPort = geoedgeProxyFreshInstance.getProxyPortForThread();
        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.start(registeredPort);

        page.open(getDriver());

        String expectedPreviewUrl = String.format("%s/%s", DisneyWebParameters.DISNEY_PROD_WEB_PREVIEW.getValue(), countryCode);
        String currentUrl = getDriver().getCurrentUrl();
        LOGGER.info("Current URL is: " + currentUrl);

        Assert.assertFalse(!currentUrl.contains("preview.disneyplus.com") && !currentUrl.contains("www.disneyplus.com"),
                String.format("Page Failed to Load to Redirect for Country (%s), please check that proxy (%s:%s) being used is available.",
                        country, proxy.getChainedProxy().getHostName(), proxy.getChainedProxy().getPort()));
        Assert.assertTrue(isExpectedUrl(live, expectedPreviewUrl, currentUrl),
                String.format("Expected URL (%s), but received (%s) for Country (%s)", expectedPreviewUrl, currentUrl, country));
    }

    private boolean isExpectedUrl(String isEnabled, String expectedPreviewUrl, String currentUrl) {

        if ("enable".equalsIgnoreCase(isEnabled)) {
            return currentUrl.contains(DisneyWebParameters.DISNEY_PROD_WEB.getValue());
        }
        return expectedPreviewUrl.equalsIgnoreCase(currentUrl);
    }
}


