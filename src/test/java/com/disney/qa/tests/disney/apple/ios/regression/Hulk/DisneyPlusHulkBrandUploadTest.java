package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.slack.api.model.Im;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.amazonaws.services.rekognition.model.BoundingBox;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.shooting.CuttingDecorator;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.cropper.ImageCropper;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.validation.constraints.NotEmpty;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class DisneyPlusHulkBrandUploadTest extends DisneyBaseTest {

    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    private static final Map<File, String> imageS3UploadRequests = new LinkedHashMap<>();
    private static final List<String> s3ImageNames = new ArrayList<>();
    private static String jsonS3FilePath = "";

    @Maintainer("csolmaz")
    @Test(dataProvider = "dataContentProvider", description = "Brand Alice Upload to S3 - Handset", groups = {"Hulk-Upload", TestGroup.PRE_CONFIGURATION})
    public void brandAliceUploadHandsetTest(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent) throws IOException {
        aliceS3Baseline(hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET, getDeviceNameFromCapabilities());
    }

    @Maintainer("csolmaz")
    @Test(dataProvider = "dataContentProvider", description = "Brand Alice Upload to S3 - Tablet", groups = {"Hulk-Upload", TestGroup.PRE_CONFIGURATION})
    public void brandAliceUploadTabletTest(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent) throws IOException {
        aliceS3Baseline(hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType.TABLET, getDeviceNameFromCapabilities());
    }

    @DataProvider
    public Iterator<Object[]> dataContentProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object[]> data = new ArrayList<>();

        File jsonFile = new File(getJsonPath());

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);

            jsonObjects.forEach((jsonObject) -> {
                DisneyPlusHulkBrandDataProvider.HulkContent content = new DisneyPlusHulkBrandDataProvider.HulkContent(
                        jsonObject.get("brand").asText());

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
            return "src/test/resources/json/hulk-brand-handset.json";

        } else {
            return "src/test/resources/json/hulk-brand-tablet.json";
        }
    }

    private AliceApiUtil getAliceClient() {
        return new AliceApiUtil(MULTIVERSE_STAGING_ENDPOINT);
    }

    private String getDeviceNameFromCapabilities() {
        return R.CONFIG.get("capabilities.deviceName").toLowerCase().replace(' ', '_');
    }

    private String buildS3BucketPath(String brand, DisneyPlusHulkBrandDataProvider.PlatformType platformType, String deviceName) {
        String pngFileType = ".png";
        String path = "";

        switch (platformType) {
            case HANDSET:
                path = String.format(
                        S3_BASE_PATH + "apple-handset/" + deviceName + "/hulu-brand/%s" +
                                pngFileType, brand);
                jsonS3FilePath = DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET.getS3Path();
                break;
            case TABLET:
                path = String.format(
                        S3_BASE_PATH + "apple-tablet/" + deviceName + "/hulu-brand/%s" +
                                pngFileType, brand);
                jsonS3FilePath = DisneyPlusHulkBrandDataProvider.PlatformType.TABLET.getS3Path();
                break;
            default:
                Assert.fail("Unrecognized platform type.");
        }
        return path;
    }

    @BeforeTest(alwaysRun = true, groups = TestGroup.NO_RESET)
    private void setUp() {
        initialSetup("US", "en");
        handleAlert();
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        }
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

    private void takeScreenshotAndCompileS3Paths(String brand, DisneyPlusHulkBrandDataProvider.PlatformType platform, String s3Device) {
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        LOGGER.info("Taking screenshot and compiling S3 upload image requests.");
        String s3BucketPath = buildS3BucketPath(brand, platform, s3Device);
        File srcFile = brandPage.getBrandFeaturedImage().getElement().getScreenshotAs(OutputType.FILE);
        imageS3UploadRequests.put(srcFile, s3BucketPath);
        s3ImageNames.add(s3BucketPath);
    }

    private void aliceS3Baseline(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        homePage.isOpened();

        sa.assertTrue(homePage.getBrandTile(hulkContent.getBrand()).isPresent(),
                hulkContent.getBrand() + "brand tile was not found.");
        homePage.getBrandTile(hulkContent.getBrand()).click();
        brandPage.isOpened();
        takeScreenshotAndCompileS3Paths(hulkContent.getBrand().replace(' ', '_'), platformType, s3DeviceName);
        homePage.tapBackButton();
        sa.assertAll();
    }
}
