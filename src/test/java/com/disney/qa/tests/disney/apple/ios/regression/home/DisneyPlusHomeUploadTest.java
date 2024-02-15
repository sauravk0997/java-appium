package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.OutputType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DisneyPlusHomeUploadTest extends DisneyBaseTest {
    static final String DISNEY_ORIGINALS_CONTENT = "Choir";
    private static final Map<File, String> imageS3UploadRequests = new LinkedHashMap<>();
    private static final List<String> s3ImageNames = new ArrayList<>();
    @Maintainer("mparra5")
    @Test(description = "Upload Images for Originals - Disney+ Originals Badge Placement throughout Disney+", groups = {"Originals", TestGroup.PRE_CONFIGURATION})
    public void uploadOriginalsDisneyOriginalsBadgePlacementImages() {
        SoftAssert sa = new SoftAssert();
        String s3BucketPath = buildS3BucketPath(String.format("%s.png", "disney_originals_badge"), "originals");
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickDisneyTile();
        homePage.swipeTillCollectionPresent(CollectionConstant.Collection.ORIGINALS_DISNEY_CAROUSEL, 10);

        sa.assertTrue(homePage.isContentPresent(DISNEY_ORIGINALS_CONTENT, CollectionConstant.Collection.ORIGINALS_DISNEY_CAROUSEL));

        File srcFile = homePage.getContentImage(DISNEY_ORIGINALS_CONTENT).getElement().getScreenshotAs(OutputType.FILE);;
        takeScreenshotAndCompileS3Paths(s3BucketPath, srcFile);
        sa.assertAll();
    }

    private void takeScreenshotAndCompileS3Paths(String s3BucketPath, File srcFile) {
        LOGGER.info("Taking screenshot and compiling S3 upload image requests.");
        imageS3UploadRequests.put(srcFile, s3BucketPath);
        s3ImageNames.add(s3BucketPath);
    }

    @AfterTest(alwaysRun = true)
    private void uploadImagesS3() {
        imageS3UploadRequests.forEach((screenshot, s3Path) -> getAliceClient().uploadImageToS3(screenshot, s3Path));
        LOGGER.info("S3 Storage image names: " + s3ImageNames);
    }

    private AliceApiUtil getAliceClient() {
        return new AliceApiUtil(MULTIVERSE_STAGING_ENDPOINT);
    }
}
