package com.disney.qa.tests.disney.android;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.report.ReportContext;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.CaptureType;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DisneyPlusAssetHarCapture extends BaseDisneyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private String ASSET = R.CONFIG.get("custom_string");
    private String SEARCH_QUERY = R.CONFIG.get("custom_string2");
    BrowserMobProxy proxy;

    private DisneyApiProvider apiProvider = new DisneyApiProvider();
    private DisneyAccount baseEntitlements;

    @BeforeTest
    public void setup() throws URISyntaxException, JSONException, MalformedURLException {
        DisneyAccountApi accountApi = getAccountApi();
        baseEntitlements = accountApi.createAccount("Yearly", "US", "en", "V1");
        accountApi.patchAccountAttributeForLocation(baseEntitlements, "US", PatchType.ACCOUNT);
        accountApi.overrideLocations(baseEntitlements, "US");
        R.CONFIG.put("browsermob_proxy", "true");
        getDriver();
        LOGGER.info("Getting proxy");
        proxy = ProxyPool.getProxy();
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT,
                CaptureType.RESPONSE_COOKIES, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_HEADERS,
                CaptureType.RESPONSE_HEADERS, CaptureType.REQUEST_BINARY_CONTENT, CaptureType.RESPONSE_BINARY_CONTENT);

        clearLogs();

    }

    @Test
    public void disneyAssetHarCapture() {
        DisneyPlusWelcomePageBase disneyPlusWelcomePageBase = initPage(DisneyPlusWelcomePageBase.class);

        disneyPlusWelcomePageBase.continueToLogin()
                .proceedToPasswordMode(baseEntitlements.getEmail())
                .logInWithPassword(baseEntitlements.getUserPass())
                .dismissChromecastNotification()
                .navigateToPage(DisneyPlusCommonPageBase.MenuItem.SEARCH.getText());

        DisneyPlusSearchPageBase disneyPlusSearchPage = initPage(DisneyPlusSearchPageBase.class);

        proxy.newHar();

        disneyPlusSearchPage.searchForMedia(SEARCH_QUERY);
        Assert.assertTrue(initPage(DisneyPlusCommonPageBase.class).scrollAndClickGenericTextElement(ASSET),"Asset not found...");

        initPage(DisneyPlusMediaPageBase.class).startPlayback();
        initPage(DisneyPlusVideoPageBase.class).isOpened();
        pause(60L);

        proxy.stop();


        try {
            //save har file to the file system (current dir)
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
            Date date = new Date();
            File harFile = new File(String.format("DISNEY_ANDROID_%s.har", dateFormat.format(date)));
            proxy.getHar().writeTo(harFile);
            File logsFile = new File(String.format("DISNEY_ANDROID_%s.txt", dateFormat.format(date)));
            String logs = generateLogs();
            Files.write(Paths.get("./" + logsFile.getName()), logs.getBytes());
            ReportContext.saveArtifact(harFile);
            ReportContext.saveArtifact(logsFile);

        } catch (IOException ioe) {
            LOGGER.info(ioe.getMessage());
        }
    }
}
