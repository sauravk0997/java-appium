package com.disney.qa.tests.drm;

import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.HARUtils;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class DisneyPlusDrmCapture extends BaseDisneyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void captureObtainLicenseCall() {
        initiateProxy("United States",
                CaptureType.REQUEST_BINARY_CONTENT,
                CaptureType.REQUEST_CONTENT,
                CaptureType.REQUEST_COOKIES,
                CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_CONTENT,
                CaptureType.RESPONSE_BINARY_CONTENT,
                CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_COOKIES);

        BrowserMobProxy proxy = ProxyPool.getProxy();
        proxy.newHar();

        login(disneyAccount.get(), false);
        dismissChromecastOverlay();

        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        searchPageBase.navigateToPage("Search");
        searchPageBase.clickOnGenericTextElement("Movies");
        searchPageBase.openFirstCategoryItem();
        initPage(DisneyPlusMediaPageBase.class).startPlayback();
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        LOGGER.info("Video Playback started on media: {}. Pausing for 30s to allow for data capture...", videoPageBase.getActiveMediaTitle());
        pause(30);

        HARUtils harUtils = new HARUtils(proxy);
        LOGGER.info("Printing basic Post/Response data. See attached har file for more details.");
        harUtils.printSpecificHarDetails(Arrays.asList(
                HARUtils.RequestDataType.URL,
                        HARUtils.RequestDataType.POST_DATA,
                        HARUtils.RequestDataType.RESPONSE_DATA),
                Collections.singletonList("widevine/v1/obtain-license"));
        String fileName = "ProxyCapture_" + new Date().toString().replaceAll("[^a-zA-Z0-9+]", "-");
        HARUtils.generateValidHarForCharles(proxy, fileName);
        harUtils.publishHAR(fileName);
    }
}
