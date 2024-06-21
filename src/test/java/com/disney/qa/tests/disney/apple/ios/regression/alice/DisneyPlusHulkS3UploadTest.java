package com.disney.qa.tests.disney.apple.ios.regression.alice;

import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
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

public class DisneyPlusHulkS3UploadTest extends DisneyBaseTest {

    public static final String HULK_100_MOVIES_JSON_PATH = "src/test/resources/json/hulk-movies-details-first-100.json";
    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    private static final Map<File, String> imageS3UploadRequests = new LinkedHashMap<>();
    private static final List<String> s3ImageNames = new ArrayList<>();
    private static String jsonS3FilePath = "";

    @DataProvider
    public Iterator<Object[]> dataContentProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object[]> data = new ArrayList<>();
        File jsonFile = new File(HULK_100_MOVIES_JSON_PATH);

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);

            jsonObjects.forEach((jsonObject) -> {
                DisneyPlusAliceDataProvider.HulkContent content = new DisneyPlusAliceDataProvider.HulkContent(
                        jsonObject.get("title").asText(),
                        jsonObject.get("entityId").asText());

                data.add(new Object[]{content});
            });

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error parsing test JSON file.");
        }

        return data.iterator();
    }

    private AliceApiUtil getAliceClient() {
        return new AliceApiUtil(MULTIVERSE_STAGING_ENDPOINT);
    }

    private String getDeviceNameFromCapabilities() {
        return R.CONFIG.get("capabilities.deviceName").toLowerCase().replace(' ', '_');
    }

    private String buildS3BucketPath(String contentTitle, DisneyPlusAliceDataProvider.PlatformType platformType, String deviceName) {
        String pngFileType = ".png";
        String path = "";

        switch (platformType) {
            case HANDSET:
                path = String.format(
                        S3_BASE_PATH + "apple-handset/" + deviceName + "/detail-page-%s-%s" +
                                pngFileType, contentTitle, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")));
                jsonS3FilePath = DisneyPlusAliceDataProvider.PlatformType.HANDSET.getS3Path();
                break;
            case TABLET:
                path = String.format(
                        S3_BASE_PATH + "apple-tablet/" + deviceName + "/detail-page-%s-%s" +
                                pngFileType, contentTitle, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")));
                jsonS3FilePath = DisneyPlusAliceDataProvider.PlatformType.TABLET.getS3Path();
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
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    @AfterTest(alwaysRun = true)
    private void uploadImagesS3() throws IOException {
        imageS3UploadRequests.forEach((screenshot, s3Path) -> getAliceClient().uploadImageToS3(screenshot, s3Path));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonObjects = objectMapper.readTree(new File(HULK_100_MOVIES_JSON_PATH));
        Iterator<JsonNode> jsonObject = jsonObjects.iterator();
        Iterator<Map.Entry<File, String>> entry = imageS3UploadRequests.entrySet().iterator();

        while (jsonObject.hasNext() && entry.hasNext()) {
            ObjectNode objectNode = (ObjectNode) jsonObject.next();
            String currEntry = entry.next().getValue();
            String formattedS3Path = currEntry.substring(0, currEntry.length() - 4);
            objectNode.put(jsonS3FilePath, formattedS3Path);
        }

        objectMapper.writeValue(new File(HULK_100_MOVIES_JSON_PATH), jsonObjects);
        LOGGER.info("S3 Storage image names: " + s3ImageNames);
    }

    private void takeScreenshotAndCompileS3Paths(String title, DisneyPlusAliceDataProvider.PlatformType platform, String s3Device) {
        LOGGER.info("Taking screenshot and compiling S3 upload image requests.");
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String s3BucketPath = buildS3BucketPath(title, platform, s3Device);
        imageS3UploadRequests.put(srcFile, s3BucketPath);
        s3ImageNames.add(s3BucketPath);
    }

    private void navigateToDeeplink(DisneyPlusAliceDataProvider.HulkContent hulkContent) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String deeplinkFormat = "disneyplus://www.disneyplus.com/browse/entity-";
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        launchDeeplink(true, deeplinkFormat + hulkContent.getEntityId(), 10);
        detailsPage.clickOpenButton();
    }

    private void recoverApp(DisneyPlusAliceDataProvider.HulkContent hulkContent, DisneyPlusAliceDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        try {
            fluentWaitNoMessage(getDriver(), 20, 3).until(it -> detailsPage.isAppRunning(sessionBundles.get(DISNEY)));
        } catch (TimeoutException e) {
            takeScreenshotAndCompileS3Paths(hulkContent.getTitle().replace(' ', '_'), platformType, s3DeviceName);
            LOGGER.info("Timeout exception: {}", e.getMessage());
            Assert.fail("Disney app is not present.");
        }
    }

    private void isContentUnavailableErrorPresent(DisneyPlusAliceDataProvider.HulkContent hulkContent, DisneyPlusAliceDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        if (detailsPage.getTextViewByLabelContains("Sorry, this content is unavailable.").isPresent(SHORT_TIMEOUT)) {
            takeScreenshotAndCompileS3Paths(hulkContent.getTitle().replace(' ', '_'), platformType, s3DeviceName);
            Assert.fail("'This content is unavailable' error displayed on " + hulkContent.getTitle());
        }
    }

    private void aliceS3Baseline(DisneyPlusAliceDataProvider.HulkContent hulkContent, DisneyPlusAliceDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
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
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present after app recovery.");
        takeScreenshotAndCompileS3Paths(hulkContent.getTitle().replace(' ', '_'), platformType, s3DeviceName);
        sa.assertAll();
    }

    @Test(dataProvider = "dataContentProvider", description = "Alice Base Images to S3 - Handset")
    public void aliceUploadBaseImagesHandset(DisneyPlusAliceDataProvider.HulkContent hulkContent) {
        aliceS3Baseline(hulkContent, DisneyPlusAliceDataProvider.PlatformType.HANDSET, getDeviceNameFromCapabilities());
    }

    @Test(dataProvider = "dataContentProvider", description = "Alice Base Images to S3 - Tablet")
    public void aliceUploadBaseImagesTablet(DisneyPlusAliceDataProvider.HulkContent hulkContent) {
        aliceS3Baseline(hulkContent, DisneyPlusAliceDataProvider.PlatformType.TABLET, getDeviceNameFromCapabilities());
    }
}