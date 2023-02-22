package com.disney.qa.tests.espn.web;

import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.espn.web.EspnBasePage;
import com.disney.qa.espn.web.EspnLoginPage;
import com.disney.qa.espn.web.EspnWebParameters;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.ReportContext;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;


public class EspnCECaptureTest extends BaseTest {
    private static final String INITIAL_PAGE = EspnWebParameters.ESPN_WEB_PROD_URL.getValue();
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    BrowserMobProxy proxy;
    GeoedgeProxyServer geoedgeProxyServer = new GeoedgeProxyServer();

    Dimension d = new Dimension(1400, 900);
    String env = Configuration.get(Configuration.Parameter.ENV);
    EspnLoginPage page = null;
    String artifactName = "CE_ESPN+_Capture_Test.har";


    @BeforeClass
    public void beforeClass() {
      geoedgeProxyServer.setProxyHostForSelenoid();
        R.CONFIG.put("browsermob_proxy", "true");

    }

    @BeforeMethod
    public void beforeTest() {
        page = new EspnLoginPage(getDriver());

        proxy = ProxyPool.getProxy();
        proxy.newHar("ceESPNCaptureTest");

        getDriver().manage().window().setSize(d);

        page.redirectUrl(env, "TV");
        page.waitForPageToFinishLoading();
    }

    @Test
    public void espnPlusWebCaptureTest () {

        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        LOGGER.info("URL is: " + espnBasePage.getCurrentUrl());
        page.login(EspnWebParameters.ESPN_WEB_USER.getValue(),EspnWebParameters.ESPN_WEB_PASS.getDecryptedValue());
        page.waitForPageToFinishLoading();
        page.open(getDriver(), INITIAL_PAGE);
        page.waitForPageToFinishLoading();
        espnBasePage.espnPlusWebProxyCapture();

         try {
            LOGGER.info("creating har file");
            proxy.stop();
            File harFile = new File(artifactName);
            proxy.getHar().writeTo(harFile);
            ReportContext.saveArtifact(harFile);
         }

         catch (IOException ioe) {

            LOGGER.info("Type of exception found"  + ioe);
        }

    }
}
