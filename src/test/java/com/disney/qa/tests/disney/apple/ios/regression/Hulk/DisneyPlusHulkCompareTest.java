package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class DisneyPlusHulkCompareTest extends DisneyBaseTest {
    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    double imageSimilarityPercentageThreshold = 90.0;
    private List<String> brandTiles = new ArrayList<>(Arrays.asList("Disney", "Pixar", "Marvel", "National Geographic", "Star Wars"));

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67499"})
    @Test(description = "No Hulu subscription - Only 5 brand tiles are visible", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyNoHuluSubscriptionFiveBrandTiles() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.isOpened();
//        sa.assertTrue(homePage.getBrandTile("Disney").isPresent(), "'Disney' brand tile was not found.");
//        sa.assertTrue(homePage.getBrandTile("Pixar").isPresent(), "'Pixar' brand tile was not found.");
//        sa.assertTrue(homePage.getBrandTile("Marvel").isPresent(), "'Marvel' brand tile was not found.");
//        sa.assertTrue(homePage.getBrandTile("Star Wars").isPresent(), "'Star Wars' brand tile was not found.");
//        sa.assertTrue(homePage.getBrandTile("National Geographic").isPresent(), "'National Geographic' brand tile was not found.");
//        sa.assertFalse(homePage.getBrandTile("Hulu").isPresent(), "'Hulu' brand tile was not found.");

        System.out.println(homePage.getBrandTile("Disney").isPresent());

        IntStream.range(0, 5).forEach(i -> {
            sa.assertTrue(homePage.getBrandTile(brandTiles.get(i)).isPresent(), i + "brand tile was not found.");
            homePage.getBrandTile(brandTiles.get(i)).click();

            System.out.println(getDriver().getPageSource());
            homePage.clickOnCollectionBackButton();
        });
        sa.assertAll();
    }

    private final List<String> networkLogos = new ArrayList<String>(
            Arrays.asList("A&E", "ABC", "ABC News", "Adult Swim", "Andscape", "Aniplex", "BBC Studios",
                    "Cartoon Network", "CBS", "Discovery", "Disney XD", "FOX", "Freeform", "FX", "FYI", "HGTV",
                    "Hulu Original Series", "Lifetime", "Lionsgate", "LMN", "Magnolia", "Moonbug Entertainment ",
                    "MTV", "National Geographic", "Nickelodeon", "Saban Films", "Samuel Goldwyn Films",
                    "Searchlight Pictures", "Paramount+", "Sony Pictures Television", "The HISTORY Channel",
                    "TLC", "TV Land", "Twentieth Century Studios", "Vertical Entertainment", "Warner Bros"));

    private String buildS3BucketPath(String title) {
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            return String.format(
                    S3_BASE_PATH + "apple-tablet/" + getDeviceNameFromCapabilities() + "/hulu-network-logos/%s", title);
        } else {
            return String.format(
                    S3_BASE_PATH + "apple-handset/" + getDeviceNameFromCapabilities() + "/hulu-network-logos/%s", title);
        }
    }

    private String getDeviceNameFromCapabilities() {
        return R.CONFIG.get("capabilities.deviceName").toLowerCase().replace(' ', '_');
    }

    private AliceApiManager getAliceApiManager() {
        return new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);
    }


    private void aliceS3BaseCompareLatestCapture(DisneyPlusHulkDataProvider.HulkContentS3 hulkContentS3, File originalFile) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        double imageSimilarityPercentageThreshold = 90.0;

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
