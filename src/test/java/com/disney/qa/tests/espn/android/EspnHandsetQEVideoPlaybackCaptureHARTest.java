package com.disney.qa.tests.espn.android;

import com.disney.qa.espn.android.pages.authentication.EspnFirstTimeLaunchPageBase;
import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.ReportContext;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
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
 * ESPN Live Video Playback - HAR Capture test
 *
 * @author bzayats
 */
public class EspnHandsetQEVideoPlaybackCaptureHARTest extends BaseEspnTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private String harAsset = R.CONFIG.get("custom_string");

    BrowserMobProxy proxy;

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        initProxy();
        initApp();
    }

    @Test(description = "Verify LIVE content is launched")
    public void testLaunchLiveContent() {
        SoftAssert sa = new SoftAssert();

        EspnCommonPageBase commonPageBase = initPage(EspnCommonPageBase.class);
        if (!harAsset.equals("NULL")) {
            LOGGER.info("Attempting to retrieve HAR capture for " + harAsset);

            proxy.newHar();

            sa.assertTrue(commonPageBase.launchContentForQE(harAsset),
                    "Expected - '" + harAsset + "' live content to launch");


            proxy.stop();

            try {
                //save har file to the file system (current dir)
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
                Date date = new Date();
                File harFile = new File(String.format("ESPN_ANDROID_%s.har", dateFormat.format(date)));
                proxy.getHar().writeTo(harFile);
                // save har file as ReportContext artifact
                ReportContext.saveArtifact(harFile);
            } catch (IOException ioe) {
                LOGGER.info(ioe.getMessage());
            }
        } else {
            skipExecution("Skipping test as no Asset was provided");
        }

        sa.assertAll();
    }

    @AfterTest
    public void closeApp() {
        getDriver().quit();
    }

    /**
     * Helper methods
     **/
    private void initApp() {
        EspnHomePageBase homePageBase = initPage(EspnHomePageBase.class);

        if (homePageBase.isOpened()) {
            LOGGER.info("homePageBase is open");
        } else {
            initPage(EspnCommonPageBase.class).handleAccessToMediaAlert(true);
            initPage(EspnCommonPageBase.class).handleLocationAlert(true);

            if (initPage(EspnFirstTimeLaunchPageBase.class).isOpened()) {
                espnInitialLogin(AcctTypes.QE.getText());
            }
        }
    }

    private void initProxy() {
        getDriver();

        LOGGER.info("Getting proxy..");
        proxy = ProxyPool.getProxy();
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT,
                CaptureType.RESPONSE_COOKIES, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_HEADERS, CaptureType.REQUEST_BINARY_CONTENT, CaptureType.RESPONSE_BINARY_CONTENT);
    }
}
