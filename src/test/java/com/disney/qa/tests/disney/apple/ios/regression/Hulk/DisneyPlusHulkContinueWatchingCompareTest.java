package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.regression.Hulk.DisneyPlusHulkDataProvider.HulkContentS3;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

public class DisneyPlusHulkContinueWatchingCompareTest extends DisneyBaseTest {

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74565"})
    @Test(dataProvider = "handsetDataContentProvider", description = "Continue Watching Compare Hulu Badging - Handset", groups = {"Hulk-Compare", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void continueWatchingAliceCompareHandsetTest(HulkContentS3 hulkContent) {
        aliceS3BaseCompareLatestCapture(hulkContent);
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74565"})
    @Test(dataProvider = "tabletDataContentProvider", description = "Continue Watching Compare Hulu Badging - Tablet", groups = {"Hulk-Compare", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void continueWatchingAliceCompareTabletTest(HulkContentS3 hulkContent) {
        aliceS3BaseCompareLatestCapture(hulkContent);
    }

    public List<Object[]> parseHulkS3Json(DisneyPlusHulkDataProvider.PlatformType platformType) {
        List<Object[]> data = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(getJsonPath());

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);

            jsonObjects.forEach((jsonObject) -> {
                HulkContentS3 content = new HulkContentS3(
                        jsonObject.get("title").asText(),
                        jsonObject.get("entityId").asText(),
                        jsonObject.get("continueWatchingId").asText(),
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
        return parseHulkS3Json(DisneyPlusHulkDataProvider.PlatformType.HANDSET).iterator();
    }

    @DataProvider
    public Iterator<Object[]> tabletDataContentProvider() {
        return parseHulkS3Json(DisneyPlusHulkDataProvider.PlatformType.TABLET).iterator();
    }

    private String getJsonPath() {
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            return "src/test/resources/json/hulk-continue-watching-handset.json";

        } else {
            return "src/test/resources/json/hulk-continue-watching-tablet.json";
        }
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
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    private void navigateToDeeplink(HulkContentS3 hulkContentS3) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String deeplinkFormat = "disneyplus://www.disneyplus.com/browse/entity-";
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        try {
            launchDeeplink(true, deeplinkFormat + hulkContentS3.getEntityId(), 10);
        } catch (WebDriverException exception) {
            LOGGER.info("Error launching deeplink. Restarting driver and trying again... " + exception.getMessage());
            getDriver().navigate().refresh();
            launchDeeplink(true, deeplinkFormat + hulkContentS3.getEntityId(), 10);
        }
        detailsPage.clickOpenButton();
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

    private void isContentUnavailableErrorPresent(DisneyPlusHulkDataProvider.HulkContentS3 hulkContentS3) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        if (detailsPage.getTextViewByLabelContains("Sorry, this content is unavailable.").isPresent(SHORT_TIMEOUT)) {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            Assert.fail("'This content is unavailable' error displayed on " + hulkContentS3.getTitle());
        }
    }

    private void playContentAndNavigateToContinueWatching() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        detailsPage.isOpened();
        detailsPage.clickPlayButton().isOpened();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(50);
        videoPlayer.clickBackButton();
        detailsPage.isOpened();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        homePage.isOpened();
        homePage.swipeInContainer(null, Direction.UP, 1500);
    }

    private void aliceS3BaseCompareLatestCapture(HulkContentS3 hulkContentS3) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        double imageSimilarityPercentageThreshold = 90.0;

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
        playContentAndNavigateToContinueWatching();

        File srcFile = homePage.getDynamicCellByName(hulkContentS3.getContinueWatchingId()).getElement().getScreenshotAs(OutputType.FILE);
        LOGGER.info("S3 File: " + hulkContentS3.getS3file());

        ImagesRequestS3 imagesComparisonRequest = new ImagesRequestS3(srcFile.getName(), FileUtil.encodeBase64File(srcFile), hulkContentS3.getS3file());
        ImagesResponse360 imagesResponse360 = getAliceApiManager().compareImages360S3(imagesComparisonRequest);
        JSONObject jsonResponse = new JSONObject(imagesResponse360.getData().toString());
        LOGGER.info("Raw JSON response: " + jsonResponse);
        double imageSimilarityPercentage = imagesResponse360.getSummary().getImageSimilarityPercentage();

        LOGGER.info("Similarity Percentage is: " + imageSimilarityPercentage);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        sa.assertTrue(
                imageSimilarityPercentage >= imageSimilarityPercentageThreshold,
                "Similarity Percentage score was 95 or lower.");
        sa.assertAll();
    }
}