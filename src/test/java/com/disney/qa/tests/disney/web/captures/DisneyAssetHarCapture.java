package com.disney.qa.tests.disney.web.captures;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.media.DisneyPlusMediaPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusVideoPlayerPage;
import com.disney.qa.disney.web.appex.search.DisneyPlusSearchPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.ReportContext;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DisneyAssetHarCapture extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private DisneyAccount user;
    BrowserMobProxy proxy;

    private static final String ASSET = R.CONFIG.get("custom_string");
    private static final String SEARCH_QUERY = R.CONFIG.get("custom_string2");
    private DisneyPlusBasePage disneyPlusBasePage;


    @BeforeClass
    public void setUp() {
        disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        DisneyPlusUserPage disneyPlusUserPage = new DisneyPlusUserPage(getDriver());
        user = new DisneyAccountApi("browser", R.CONFIG.get("environment"), "disney").createAccount("Yearly", "US", "en", "V1");
        GeoedgeProxyServer geoedgeProxyServer = new GeoedgeProxyServer();

        ProxyPool.registerProxy(geoedgeProxyServer.getGeoedgeProxy("United States"));
        int port = geoedgeProxyServer.getProxyPortForThread();
        proxy = ProxyPool.getProxy();
        proxy.start(port);

        disneyPlusUserPage.open(getDriver());
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT,
                CaptureType.RESPONSE_COOKIES, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_HEADERS, CaptureType.REQUEST_BINARY_CONTENT, CaptureType.RESPONSE_BINARY_CONTENT);
    }

    @Test
    public void harCaptureOfAsset() {
        DisneyPlusSearchPage disneyPlusSearchPage = new DisneyPlusSearchPage(getDriver());
        DisneyPlusMediaPage disneyPlusMediaPage = new DisneyPlusMediaPage(getDriver());
        DisneyPlusVideoPlayerPage disneyPlusVideoPlayerPage = new DisneyPlusVideoPlayerPage(getDriver());

        disneyPlusBasePage.dBaseUniversalLogin(user.getEmail(), user.getUserPass());
        disneyPlusSearchPage.clickSearch();
        Assert.assertTrue(disneyPlusSearchPage.isSearchBarPresent(),
                "Search page did not launch");

        proxy.newHar();

        disneyPlusSearchPage.searchForAsset(SEARCH_QUERY);
        Assert.assertTrue(disneyPlusSearchPage.clickAssetIfPresent(ASSET),
                String.format("Asset %s not found...", ASSET));
        disneyPlusMediaPage.startPlayback();
        Assert.assertTrue(disneyPlusVideoPlayerPage.isVideoPlayerPresent(),
                "Expected Playback but playback failed");

        pause(60L);
        proxy.stop();

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
            Date date = new Date();
            File harFile = new File(String.format("DISNEY_WEB_%s.har", dateFormat.format(date)));
            proxy.getHar().writeTo(harFile);
            ReportContext.saveArtifact(harFile);

        } catch (IOException ioe) {
            LOGGER.info(ioe.getMessage());
        }
    }
}
