package com.disney.qa.tests.disney.apple.ios.regression.alice;

import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWait;

public class DisneyPlusHulkS3UploadTest extends DisneyBaseTest {

    Map<File, String> imageS3UploadRequests = new HashMap<>();
    List<String> s3ImageNames = new ArrayList<>();

    @DataProvider
    public Iterator<Object[]> dataContentProvider() {
        List<Object[]> data = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/test/resources/json/hulk-details-top-ten.json");

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

    private String buildS3BucketPath(String contentTitle, PlatformType platformType, String deviceName) {
        String path = "";
        String pngFileType = ".png";

        switch (platformType) {
            case HANDSET:
                path = String.format(
                        "bamtech-qa-alice/disney/recognition/alice/apple-handset/" + deviceName + "/detail-page-%s-%s" +
                                pngFileType, contentTitle, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")));
                break;
            case TABLET:
                path = String.format(
                        "bamtech-qa-alice/disney/recognition/alice/apple-tablet/" + deviceName + "/detail-page-%s-%s" +
                                pngFileType, contentTitle, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")));
                break;
            default:
                Assert.fail("Unrecognized platform type.");
        }
        return path;
    }

    private enum PlatformType {
        HANDSET,
        TABLET;
    }

    @BeforeTest(alwaysRun = true, groups = TestGroup.NO_RESET)
    private void setUp() {
        initialSetup("US", "en");
        handleAlert();
        setBrazeConfig();
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        }
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    @AfterTest(alwaysRun = true)
    private void uploadImagesS3() throws IOException {
        String filePath = "reports/s3FileNames.txt";
        imageS3UploadRequests.forEach((screenshot, s3Path) -> getAliceClient().uploadImageToS3(screenshot, s3Path));
        try (FileWriter listOfS3Paths = new FileWriter(filePath)) {
            s3ImageNames.forEach((fileName) -> {
                try {
                    listOfS3Paths.write(fileName + System.lineSeparator());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        LOGGER.info("S3 Storage image names: " + s3ImageNames);
    }

    private void aliceS3Baseline(DisneyPlusAliceDataProvider.HulkContent hulkContent, PlatformType platformType, String deviceName) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String underscoreTitle = hulkContent.getTitle().replace(' ', '_');
        String deeplinkFormat = "disneyplus://www.disneyplus.com/browse/entity-";
        launchDeeplink(true, deeplinkFormat + hulkContent.getEntityId(), 10);
        detailsPage.clickOpenButton();
        sa.assertTrue(detailsPage.isAppRunning(sessionBundles.get(DISNEY)), "Disney app is not running.");
        fluentWait(getDriver(), 20, 3, "Details tab is not present").until(it -> detailsPage.getDetailsTab().isPresent(1));

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String s3BucketPath = buildS3BucketPath(underscoreTitle, platformType, deviceName);

        imageS3UploadRequests.put(srcFile, s3BucketPath);
        s3ImageNames.add(s3BucketPath);
    }

    @Maintainer("csolmaz")
    @Test(dataProvider = "dataContentProvider", description = "Alice Base Images to S3 - Handset")
    public void aliceUploadBaseImagesHandset(DisneyPlusAliceDataProvider.HulkContent hulkContent) {
        aliceS3Baseline(hulkContent, PlatformType.HANDSET, getDeviceNameFromCapabilities());
    }

    @Maintainer("csolmaz")
    @Test(dataProvider = "dataContentProvider", description = "Alice Base Images to S3 - Tablet")
    public void aliceUploadBaseImagesTablet(DisneyPlusAliceDataProvider.HulkContent hulkContent) {
        aliceS3Baseline(hulkContent, PlatformType.TABLET, getDeviceNameFromCapabilities());
    }
}

