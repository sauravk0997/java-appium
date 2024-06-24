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

public class DisneyPlusHulkDisneyFiveBrandCompareTest extends DisneyBaseTest {


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67499"})
    @Test(dataProvider = "handsetDataContentProvider", description = "Disney - Five Brands: Compare Brand Featured Images and No Hulu - Handset", groups = {"Hulk-Compare", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void brandAliceCompareHandsetTest(DisneyPlusHulkDisneyFiveBrandDataProvider.HulkContentS3 hulkContent) {
        validateBrandTileComparisonAndNoHulu(hulkContent);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67499"})
    @Test(dataProvider = "tabletDataContentProvider", description = "Disney - Five Brands: Compare Brand Featured Images and No Hulu- Tablet", groups = {"Hulk-Compare", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void brandAliceCompareTabletTest(DisneyPlusHulkDisneyFiveBrandDataProvider.HulkContentS3 hulkContent) {
        validateBrandTileComparisonAndNoHulu(hulkContent);
    }

    public List<Object[]> parseHulkS3Json(DisneyPlusHulkDisneyFiveBrandDataProvider.PlatformType platformType) {
        List<Object[]> data = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(getJsonPath());

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);
            jsonObjects.forEach((jsonObject) -> {
                DisneyPlusHulkDisneyFiveBrandDataProvider.HulkContentS3 content = new DisneyPlusHulkDisneyFiveBrandDataProvider.HulkContentS3(
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

    @DataProvider
    public Iterator<Object[]> handsetDataContentProvider() {
        return parseHulkS3Json(DisneyPlusHulkDisneyFiveBrandDataProvider.PlatformType.HANDSET_BRAND_TILE).iterator();
    }

    @DataProvider
    public Iterator<Object[]> tabletDataContentProvider() {
        return parseHulkS3Json(DisneyPlusHulkDisneyFiveBrandDataProvider.PlatformType.TABLET_BRAND_TILE).iterator();
    }

    @BeforeTest(alwaysRun = true, groups = TestGroup.NO_RESET)
    private void setUp() {
        initialSetup();
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


    private void validateBrandTileComparisonAndNoHulu(DisneyPlusHulkDisneyFiveBrandDataProvider.HulkContentS3 hulkContentS3) {
        double imageSimilarityPercentageThreshold = 90.0;
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        sa.assertTrue(homePage.getBrandTile(hulkContentS3.getBrand()).isPresent(),
                hulkContentS3.getBrand() + "brand tile was not found.");
        sa.assertTrue(homePage.getStaticTextByLabel("Hulu").isElementNotPresent(SHORT_TIMEOUT), "Hulu brand was found on standard Disney account.");

        File srcTileFile = homePage.getBrandTile(hulkContentS3.getBrand()).getElement().getScreenshotAs(OutputType.FILE);
        LOGGER.info("S3 File: " + hulkContentS3.getS3file());
        ImagesRequestS3 imagesTileComparisonRequest = new ImagesRequestS3(srcTileFile.getName(), FileUtil.encodeBase64File(srcTileFile), hulkContentS3.getS3file());
        ImagesResponse360 imagesTileResponse360 = getAliceApiManager().compareImages360S3(imagesTileComparisonRequest);
        JSONObject tileJsonResponse = new JSONObject(imagesTileResponse360.getData().toString());
        LOGGER.info("Raw JSON response: " + tileJsonResponse);
        double tileImageSimilarityPercentage = imagesTileResponse360.getSummary().getImageSimilarityPercentage();

        LOGGER.info("Similarity Percentage is: " + tileImageSimilarityPercentage);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(
                tileImageSimilarityPercentage >= imageSimilarityPercentageThreshold,
                "Similarity Percentage score was lower than 90%.");

        homePage.getBrandTile(hulkContentS3.getBrand()).click();
        sa.assertTrue(brandPage.isOpened(), "Brand page did not open.");
        homePage.tapBackButton();
        sa.assertAll();
    }
}
