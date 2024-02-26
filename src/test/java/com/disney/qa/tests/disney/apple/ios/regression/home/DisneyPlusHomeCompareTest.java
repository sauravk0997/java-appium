package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;

public class DisneyPlusHomeCompareTest extends DisneyBaseTest {
    static final String DISNEY_ORIGINALS_CONTENT = "Choir";
    private static final AliceApiManager aliceManager = new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);
    double imageSimilarityPercentageThreshold = 90.0;
    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61874"})
    @Test(description = "Originals - Disney+ Originals Badge Placement throughout Disney+", groups = {"Originals", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyOriginalsDisneyOriginalsBadgePlacement() {
        SoftAssert sa = new SoftAssert();
        String s3BucketPath = buildS3BucketPath(String.format("%s.png", "disney_originals_badge"), "originals");
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickDisneyTile();
        homePage.swipeTillCollectionPresent(CollectionConstant.Collection.ORIGINALS_DISNEY_CAROUSEL, 10);

        sa.assertTrue(homePage.isContentPresent(DISNEY_ORIGINALS_CONTENT, CollectionConstant.Collection.ORIGINALS_DISNEY_CAROUSEL));

        File srcFile = homePage.getContentImage(DISNEY_ORIGINALS_CONTENT).getElement().getScreenshotAs(OutputType.FILE);;
        ImagesRequestS3 imagesComparisonRequest = new ImagesRequestS3(srcFile.getName(), FileUtil.encodeBase64File(srcFile), s3BucketPath);
        ImagesResponse360 imagesResponse360 = aliceManager.compareImages360S3(imagesComparisonRequest);
        JSONObject jsonResponse = new JSONObject(imagesResponse360.getData().toString());
        LOGGER.info("Raw JSON response: " + jsonResponse);
        double imageSimilarityPercentage = imagesResponse360.getSummary().getImageSimilarityPercentage();
        sa.assertTrue(
                imageSimilarityPercentage >= imageSimilarityPercentageThreshold,
                String.format("Similarity Percentage score was %,.2f or lower in disney_originals Network logo {%,.2f}.", imageSimilarityPercentageThreshold, imageSimilarityPercentage));

        sa.assertAll();
    }
}
