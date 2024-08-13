package com.disney.qa.tests.disney.apple.ios.regression.alice;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.config.ConfigProperties;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.regression.alice.DisneyPlusAliceDataProvider.HulkContentS3;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;
import static com.disney.qa.tests.disney.apple.ios.regression.alice.DisneyPlusHulkS3UploadTest.HULK_100_MOVIES_JSON_PATH;

public class DisneyPlusHulkS3BaselineCompareTest extends DisneyBaseTest {

    public List<Object[]> parseHulkS3Json(DisneyPlusAliceDataProvider.PlatformType platformType) {
        List<Object[]> data = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(HULK_100_MOVIES_JSON_PATH);

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);

            jsonObjects.forEach((jsonObject) -> {
                HulkContentS3 content = new HulkContentS3(
                        jsonObject.get("title").asText(),
                        jsonObject.get("entityId").asText(),
                        jsonObject.get(platformType.getS3Path()).asText());

                data.add(new Object[]{content});
            });

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error parsing test JSON file.");
        }
        return data;
    }

    @DataProvider
    public Iterator<Object[]> handsetDataContentProvider() {
        return parseHulkS3Json(DisneyPlusAliceDataProvider.PlatformType.HANDSET).iterator();
    }

    @DataProvider
    public Iterator<Object[]> tabletDataContentProvider() {
        return parseHulkS3Json(DisneyPlusAliceDataProvider.PlatformType.TABLET).iterator();
    }

    private AliceApiManager getAliceApiManager() {
        return new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);
    }

    @BeforeTest(alwaysRun = true, groups = TestGroup.NO_RESET)
    private void setUp() {
        initialSetup();
        handleAlert();
        disableBrazeConfig();
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        }
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    private void recoverApp() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        try {
            fluentWaitNoMessage(getDriver(), 20, 3).until(it -> detailsPage.isAppRunning(sessionBundles.get(DISNEY)));
        } catch (TimeoutException e) {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            LOGGER.info("Timeout exception: {}", e.getMessage());
            Assert.fail("Disney app is not present.");
        }
    }

    private void navigateToDeeplink(HulkContentS3 hulkContentS3) {
        String deeplinkFormat = "disneyplus://www.disneyplus.com/browse/entity-";
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        launchDeeplink(deeplinkFormat + hulkContentS3.getEntityId());
    }

    private void isContentUnavailableErrorPresent(DisneyPlusAliceDataProvider.HulkContentS3 hulkContentS3) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        if (detailsPage.getTextViewByLabelContains("Sorry, this content is unavailable.").isPresent(SHORT_TIMEOUT)) {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            Assert.fail("'This content is unavailable' error displayed on " + hulkContentS3.getTitle());
        }
    }

    private void aliceS3BaselineVsLatestScreenshot(HulkContentS3 hulkContentS3) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        navigateToDeeplink(hulkContentS3);
        recoverApp();
        isContentUnavailableErrorPresent(hulkContentS3);

        int count = 3;
        while (!detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT) && count > 0) {
            navigateToDeeplink(hulkContentS3);
            recoverApp();
            LOGGER.info("Count is at: " + count --);
        }

        isContentUnavailableErrorPresent(hulkContentS3);
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present after app recovery.");

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        LOGGER.info("S3 File: " + hulkContentS3.getS3file());

        ImagesRequestS3 imagesComparisonRequest = new ImagesRequestS3(srcFile.getName(), FileUtil.encodeBase64File(srcFile), hulkContentS3.getS3file());
        ImagesResponse360 imagesResponse360 = getAliceApiManager().compareImages360S3(imagesComparisonRequest);
        JSONObject jsonResponse = new JSONObject(imagesResponse360.getData().toString());
        double imageSimilarityPercentage = imagesResponse360.getSummary().getImageSimilarityPercentage();

        LOGGER.info("Similarity Percentage is: " + imageSimilarityPercentage);
        LOGGER.info("Raw JSON response: " + jsonResponse);

        sa.assertTrue(
                imageSimilarityPercentage > ConfigProperties.getInstance().getPercentageOfSimilarity(),
                "Similarity Percentage score was 95 or lower.");
        sa.assertAll();
    }

    @Test(dataProvider = "handsetDataContentProvider", description = "Alice Base Compare Images Test - Handset")
    public void aliceBaselineCompareS3HandsetTest(HulkContentS3 hulkContent) {
        aliceS3BaselineVsLatestScreenshot(hulkContent);
    }

    @Test(dataProvider = "tabletDataContentProvider", description = "Alice Base Compare Images Test - Tablet")
    public void aliceBaselineCompareS3TabletTest(HulkContentS3 hulkContent) {
        aliceS3BaselineVsLatestScreenshot(hulkContent);
    }
}