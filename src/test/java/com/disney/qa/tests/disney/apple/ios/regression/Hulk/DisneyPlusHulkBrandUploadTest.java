package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zebrunner.agent.core.annotation.Maintainer;
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
import java.util.*;
import java.util.List;

public class DisneyPlusHulkBrandUploadTest extends DisneyBaseTest {

    private static final String HANDSET_S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/apple-handset/";
    private static final String TABLET_S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/apple-tablet/";
    private static final Map<File, String> imageS3UploadRequests = new LinkedHashMap<>();
    private static final List<String> s3FeaturedImageNames = new ArrayList<>();
    private static final List<String> s3TileImageNames = new ArrayList<>();
    private static String jsonS3FilePath = "";
    private static final String FIVE_BRANDS_TILE = "/five-brands/tile/%s";
    private static final String FIVE_BRANDS_FEATURED = "/five-brands/featured/%s";

    @Maintainer("csolmaz")
    @Test(dataProvider = "dataContentProvider", description = "Brand Alice Upload to S3 - Handset", groups = {"Hulk-Upload", TestGroup.PRE_CONFIGURATION})
    public void brandAliceUploadHandsetTest(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent) {
        SoftAssert sa = new SoftAssert();
        aliceS3TileBaseline(hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_TILE, getDeviceNameFromCapabilities(), sa);
        aliceS3FeaturedBaseline(hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_FEATURED, getDeviceNameFromCapabilities(), sa);
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @Test(dataProvider = "dataContentProvider", description = "Brand Alice Upload to S3 - Tablet", groups = {"Hulk-Upload", TestGroup.PRE_CONFIGURATION})
    public void brandAliceUploadTabletTest(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent) {
        SoftAssert sa = new SoftAssert();
        aliceS3TileBaseline(hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_TILE, getDeviceNameFromCapabilities(), sa);
        aliceS3FeaturedBaseline(hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_FEATURED, getDeviceNameFromCapabilities(), sa);
        sa.assertAll();
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

    private String buildS3BucketFeaturedPath(String brandFeatured, DisneyPlusHulkBrandDataProvider.PlatformType platformType, String deviceName) {
        String pngFileType = ".png";
        String path = "";

        switch (platformType) {
            case HANDSET_BRAND_FEATURED:
                path = String.format(
                        HANDSET_S3_BASE_PATH + deviceName + FIVE_BRANDS_FEATURED +
                                pngFileType, brandFeatured);
                jsonS3FilePath = DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_FEATURED.getS3Path();
                break;
            case TABLET_BRAND_FEATURED:
                path = String.format(
                        TABLET_S3_BASE_PATH + deviceName + FIVE_BRANDS_FEATURED +
                                pngFileType, brandFeatured);
                jsonS3FilePath = DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_FEATURED.getS3Path();
                break;
            default:
                Assert.fail("Unrecognized platform type.");
        }
        return path;
    }

    private String buildS3BucketTilePath(String brandTile, DisneyPlusHulkBrandDataProvider.PlatformType platformType, String deviceName) {
        String pngFileType = ".png";
        String path = "";

        switch (platformType) {
            case HANDSET_BRAND_TILE:
                path = String.format(
                        HANDSET_S3_BASE_PATH + deviceName + FIVE_BRANDS_TILE +
                                pngFileType, brandTile);
                jsonS3FilePath = DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_TILE.getS3Path();
                break;
            case TABLET_BRAND_TILE:
                path = String.format(
                        TABLET_S3_BASE_PATH + deviceName + FIVE_BRANDS_TILE +
                                pngFileType, brandTile);
                jsonS3FilePath = DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_TILE.getS3Path();
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
        LOGGER.info("S3 Storage image names: " + s3FeaturedImageNames);
    }

    private void takeScreenshotAndCompileS3FeaturedPaths(String brandFeatured, DisneyPlusHulkBrandDataProvider.PlatformType platform, String s3Device) {
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        LOGGER.info("Taking screenshot and compiling S3 upload image requests.");
        String s3FeaturedBucketPath = buildS3BucketFeaturedPath(brandFeatured, platform, s3Device);
        File srcFeaturedFile = brandPage.getBrandFeaturedImage().getElement().getScreenshotAs(OutputType.FILE);
        imageS3UploadRequests.put(srcFeaturedFile, s3FeaturedBucketPath);
        s3FeaturedImageNames.add(s3FeaturedBucketPath);
    }

    private void takeScreenshotAndCompileS3TilePaths(String brandTile, DisneyPlusHulkBrandDataProvider.PlatformType platform, @NotEmpty String s3Device,
                                                     DisneyPlusHulkBrandDataProvider.HulkContent hulkContent) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        LOGGER.info("Taking screenshot and compiling S3 upload image requests.");
        String s3TileBucketPath = buildS3BucketTilePath(brandTile, platform, s3Device);
        File srcTileFile = homePage.getBrandTile(hulkContent.getBrand()).getElement().getScreenshotAs(OutputType.FILE);
        imageS3UploadRequests.put(srcTileFile, s3TileBucketPath);
        s3TileImageNames.add(s3TileBucketPath);
    }

    private void aliceS3FeaturedBaseline(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName, SoftAssert sa) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        homePage.getBrandTile(hulkContent.getBrand()).click();
        brandPage.isOpened();
        takeScreenshotAndCompileS3FeaturedPaths(hulkContent.getBrand().replace(' ', '_'), platformType, s3DeviceName);
        homePage.tapBackButton();
    }

    private void aliceS3TileBaseline(DisneyPlusHulkBrandDataProvider.HulkContent hulkContent, DisneyPlusHulkBrandDataProvider.PlatformType platformType, @NotEmpty String s3DeviceName, SoftAssert sa) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        sa.assertTrue(homePage.getBrandTile(hulkContent.getBrand()).isPresent(),
                hulkContent.getBrand() + "brand tile was not found.");
        sa.assertTrue(homePage.getStaticTextByLabel("Hulu").isElementNotPresent(SHORT_TIMEOUT), "Hulu brand was found on standard Disney account.");
        takeScreenshotAndCompileS3TilePaths(hulkContent.getBrand().replace(' ', '_'), platformType, s3DeviceName, hulkContent);
    }
}
