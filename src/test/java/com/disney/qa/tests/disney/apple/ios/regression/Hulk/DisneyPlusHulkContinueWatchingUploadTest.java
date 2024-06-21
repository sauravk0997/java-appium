package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

public class DisneyPlusHulkContinueWatchingUploadTest extends DisneyBaseTest {

    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    private static final Map<File, String> imageS3UploadRequests = new LinkedHashMap<>();
    private static final List<String> s3ImageNames = new ArrayList<>();
    private static String jsonS3FilePath = "";

    @Test(dataProvider = "dataContentProvider", description = "Continue Watching Alice Upload to S3 - Handset", groups = {"Hulk-Upload", TestGroup.PRE_CONFIGURATION})
    public void continueWatchingAliceUploadHandsetTest(DisneyPlusHulkDataProvider.HulkContent hulkContent) {
        aliceS3Baseline(hulkContent, DisneyPlusHulkDataProvider.PlatformType.HANDSET, getDeviceNameFromCapabilities());
    }

    @Test(dataProvider = "dataContentProvider", description = "Continue Watching Alice Upload to S3 - Tablet", groups = {"Hulk-Upload", TestGroup.PRE_CONFIGURATION})
    public void continueWatchingAliceUploadTabletTest(DisneyPlusHulkDataProvider.HulkContent hulkContent) {
        aliceS3Baseline(hulkContent, DisneyPlusHulkDataProvider.PlatformType.TABLET, getDeviceNameFromCapabilities());
    }

    @DataProvider
    public Iterator<Object[]> dataContentProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object[]> data = new ArrayList<>();

        File jsonFile = new File(getJsonPath());

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);

            jsonObjects.forEach((jsonObject) -> {
                DisneyPlusHulkDataProvider.HulkContent content = new DisneyPlusHulkDataProvider.HulkContent(
                        jsonObject.get("title").asText(),
                        jsonObject.get("entityId").asText(),
                        jsonObject.get("continueWatchingId").asText());

                data.add(new Object[]{content});
            });

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error parsing test JSON file.");
        }

        return data.iterator();
    }

    private String getJsonPath() {
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            return "src/test/resources/json/hulk-continue-watching-handset.json";

        } else {
            return "src/test/resources/json/hulk-continue-watching-tablet.json";
        }
    }

    private AliceApiUtil getAliceClient() {
        return new AliceApiUtil(MULTIVERSE_STAGING_ENDPOINT);
    }

    private String getDeviceNameFromCapabilities() {
        return R.CONFIG.get("capabilities.deviceName").toLowerCase().replace(' ', '_');
    }

    private String buildS3BucketPath(String contentTitle, DisneyPlusHulkDataProvider.PlatformType platformType, String deviceName) {
        String pngFileType = ".png";
        String path = "";

        switch (platformType) {
            case HANDSET:
                path = String.format(
                        S3_BASE_PATH + "apple-handset/" + deviceName + "/continue-watching/%s-%s" +
                                pngFileType, contentTitle, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")));
                jsonS3FilePath = DisneyPlusHulkDataProvider.PlatformType.HANDSET.getS3Path();
                break;
            case TABLET:
                path = String.format(
                        S3_BASE_PATH + "apple-tablet/" + deviceName + "/continue-watching/%s-%s" +
                                pngFileType, contentTitle, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")));
                jsonS3FilePath = DisneyPlusHulkDataProvider.PlatformType.TABLET.getS3Path();
                break;
            default:
                Assert.fail("Unrecognized platform type.");
        }
        return path;
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
        pause(5);
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    @AfterTest(alwaysRun = true)
    private void uploadImagesS3() throws IOException {
        imageS3UploadRequests.forEach((screenshot, s3Path) -> getAliceClient().uploadImageToS3(screenshot, s3Path));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonObjects = objectMapper.readTree(new File(getJsonPath()));
        Iterator<JsonNode> jsonObject = jsonObjects.iterator();
        Iterator<Map.Entry<File, String>> entry = imageS3UploadRequests.entrySet().iterator();

        while (jsonObject.hasNext() && entry.hasNext()) {
            ObjectNode objectNode = (ObjectNode) jsonObject.next();
            String currEntry = entry.next().getValue();
            String formattedS3Path = currEntry.substring(0, currEntry.length() - 4);
            objectNode.put(jsonS3FilePath, formattedS3Path);
        }

        objectMapper.writeValue(new File(getJsonPath()), jsonObjects);
        LOGGER.info("S3 Storage image names: " + s3ImageNames);
    }

    private void takeScreenshotAndCompileS3Paths(String title, String continueWatchingId, DisneyPlusHulkDataProvider.PlatformType platform, String s3Device) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        LOGGER.info("Taking screenshot and compiling S3 upload image requests.");
        String s3BucketPath = buildS3BucketPath(title, platform, s3Device);

        homePage.swipeInContainer(null, Direction.UP, 1500);
        File srcFile = homePage.getDynamicCellByName(continueWatchingId).getElement().getScreenshotAs(OutputType.FILE);
        imageS3UploadRequests.put(srcFile, s3BucketPath);
        s3ImageNames.add(s3BucketPath);
    }

    private void navigateToDeeplink(DisneyPlusHulkDataProvider.HulkContent hulkContent) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String deeplinkFormat = "disneyplus://www.disneyplus.com/browse/entity-";
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        launchDeeplink(true, deeplinkFormat + hulkContent.getEntityId(), 10);
        detailsPage.clickOpenButton();
    }

    private void isContentUnavailableErrorPresent(DisneyPlusHulkDataProvider.HulkContent hulkContent, DisneyPlusHulkDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        if (detailsPage.getTextViewByLabelContains("Sorry, this content is unavailable.").isPresent(SHORT_TIMEOUT)) {
            takeScreenshotAndCompileS3Paths(hulkContent.getTitle().replace(' ', '_'), hulkContent.getContinueWatchingId(), platformType, s3DeviceName);
            Assert.fail("'This content is unavailable' error displayed on " + hulkContent.getTitle());
        }
    }

    private void recoverApp(DisneyPlusHulkDataProvider.HulkContent hulkContent, DisneyPlusHulkDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        try {
            fluentWaitNoMessage(getDriver(), 20, 3).until(it -> detailsPage.isAppRunning(sessionBundles.get(DISNEY)));
        } catch (TimeoutException e) {
            takeScreenshotAndCompileS3Paths(hulkContent.getTitle().replace(' ', '_'), hulkContent.getContinueWatchingId(), platformType, s3DeviceName);
            LOGGER.info("Timeout exception: {}", e.getMessage());
            Assert.fail("Disney app is not present.");
        }
    }

    private void aliceS3Baseline(DisneyPlusHulkDataProvider.HulkContent hulkContent, DisneyPlusHulkDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        navigateToDeeplink(hulkContent);
        recoverApp(hulkContent, platformType, s3DeviceName);
        isContentUnavailableErrorPresent(hulkContent, platformType, s3DeviceName);

        int count = 3;
        while (!detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT) && count > 0) {
            navigateToDeeplink(hulkContent);
            recoverApp(hulkContent, platformType, s3DeviceName);
            LOGGER.info("Count is at: " + count --);
        }

        isContentUnavailableErrorPresent(hulkContent, platformType, s3DeviceName);
        detailsPage.isOpened();
        detailsPage.getPlayButton().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(50);
        pause(5);
        videoPlayer.clickBackButton();
        detailsPage.isOpened();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        homePage.isOpened();
        takeScreenshotAndCompileS3Paths(hulkContent.getTitle().replace(' ', '_'), hulkContent.getContinueWatchingId(), platformType, s3DeviceName);
        sa.assertAll();
    }
}