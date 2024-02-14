package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
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
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisneyPlusHulkBrandCompareTest extends DisneyBaseTest {
    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    double imageSimilarityPercentageThreshold = 90.0;

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67499"})
    @Test(dataProvider = "handsetDataContentProvider", description = "Compare Brand Featured Images and No Hulu - Handset", groups = {"Hulk-Compare", TestGroup.PRE_CONFIGURATION})
    public void brandAliceCompareHandsetTest(DisneyPlusHulkBrandDataProvider.HulkContentS3 hulkContent) {
        aliceS3BaseCompareLatestCapture(hulkContent);
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67499"})
    @Test(dataProvider = "tabletDataContentProvider", description = "Compare Brand Featured Images and No Hulu- Tablet", groups = {"Hulk-Compare", TestGroup.PRE_CONFIGURATION})
    public void brandAliceCompareTabletTest(DisneyPlusHulkBrandDataProvider.HulkContentS3 hulkContent) {
        aliceS3BaseCompareLatestCapture(hulkContent);
    }

    public List<Object[]> parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType platformType) {
        List<Object[]> data = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(getJsonPath());

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);
            jsonObjects.forEach((jsonObject) -> {
                DisneyPlusHulkBrandDataProvider.HulkContentS3 content = new DisneyPlusHulkBrandDataProvider.HulkContentS3(
                        jsonObject.get("brand").asText(),
                        jsonObject.get(platformType.getS3Path()).asText());
                data.add(new Object[]{content});
            });

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error parsing test JSON file.");
        }
        return data;
    }

    private String getJsonPath() {
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            return "src/test/resources/json/hulk-brand-handset.json";

        } else {
            return "src/test/resources/json/hulk-brand-tablet.json";
        }
    }

//    @DataProvider
//    public Iterator<Object[]> handsetDataContentProvider() {
//        boolean featured = false;
//        if (featured) {
//            return parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_FEATURED).iterator();
//        } else {
//            return parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_TILE).iterator();
//        }
//        return parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_FEATURED).iterator() && parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_TILE).iterator();
//    }
    @DataProvider
    public Object[][] handsetDataContentProvider() {
        List<Object[]> content = new ArrayList<>();
        content.add(handsetFeaturedContent());
        content.add(handsetTileContent());
        return content.toArray(new Object[0][]);
    }

    public Object[] handsetFeaturedContent() {
        parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_FEATURED);
        return new Object[0];
    }

    public Object[] handsetTileContent() {
        parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_TILE);
        return new Object[0];
    }

    @DataProvider
    public Iterator<Object[]> handsetTileDataContentProvider() {
        return parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.HANDSET_BRAND_TILE).iterator();
    }

    public Object[] tabletFeaturedContent() {
        parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_FEATURED);
        return new Object[0];
    }

    public Object[] tabletTileContent() {
        parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_TILE);
        return new Object[0];
    }

//    @DataProvider
//    public Iterator<Object[]> tabletDataContentProvider() {
//        return parseHulkS3Json(DisneyPlusHulkBrandDataProvider.PlatformType.TABLET_BRAND_FEATURED).iterator();
//    }

    @DataProvider
    public Object[][] tabletDataContentProvider() {
        List<Object[]> content = new ArrayList<>();
        content.add(tabletFeaturedContent());
        content.add(tabletTileContent());
        return content.toArray(new Object[0][]);
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

    private AliceApiManager getAliceApiManager() {
        return new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);
    }


    private void aliceS3BaseCompareLatestCapture(DisneyPlusHulkBrandDataProvider.HulkContentS3 hulkContentS3) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        double imageSimilarityPercentageThreshold = 90.0;

        sa.assertTrue(homePage.getBrandTile(hulkContentS3.getBrand()).isPresent(),
                hulkContentS3.getBrand() + "brand tile was not found.");

        homePage.getBrandTile(hulkContentS3.getBrand()).click();
        brandPage.isOpened();
        File srcFile = brandPage.getBrandFeaturedImage().getElement().getScreenshotAs(OutputType.FILE);
        homePage.tapBackButton();

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
                "Similarity Percentage score was lower than 90%.");
        sa.assertAll();
    }
}
