package com.disney.qa.tests.espn.ios;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchEspnPlusIOSPageBase;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.ReportContext;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * ESPN QE video playback HAR capture
 *
 * @author achowdhury
 */
public class EspnHandsetQEVideoPlaybackCaptureTest extends BaseMobileTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String HAR_ASSET = R.CONFIG.get("custom_string");

    BrowserMobProxy proxy;

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        initProxy();
    }


    @Test(description = "Record API calls while streaming videos")
    public void testVideoStreamHarCapture() throws IOException {

        SoftAssert softAssert = new SoftAssert();

        EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

        espnIOSPageBase.navigateToSearchWithLogin("QE");


        EspnWatchEspnPlusIOSPageBase espnWatchEspnPlusIOSPageBase =
                initPage(EspnWatchEspnPlusIOSPageBase.class);

        proxy.newHar();

        softAssert.assertTrue(espnWatchEspnPlusIOSPageBase.playLiveVideo(HAR_ASSET,60L),
                HAR_ASSET + " Stream not found");

        proxy.stop();

        //adding reports
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        Date date = new Date();
        File harFile = new File(String.format("ESPN_iOS_%s.har", dateFormat.format(date)));

        proxy.getHar().writeTo(harFile);
        ReportContext.saveArtifact(harFile);

        softAssert.assertAll();
    }

    private void initProxy() {
        getDriver();

        LOGGER.info("Getting proxy..");
        proxy = ProxyPool.getProxy();
        proxy.enableHarCaptureTypes(CaptureType.RESPONSE_BINARY_CONTENT, CaptureType.REQUEST_BINARY_CONTENT, CaptureType.RESPONSE_HEADERS,
                CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_COOKIES, CaptureType.RESPONSE_CONTENT,
                CaptureType.REQUEST_CONTENT);
    }

}