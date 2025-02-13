package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.*;

import static com.disney.qa.common.constant.IConstantHelper.*;

public class DisneyPlusHulkNetworkPageTest extends DisneyBaseTest {

    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    private static final AliceApiManager aliceManager = new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);
    double imageSimilarityPercentageThreshold = 90.0;

    private final List<String> networkLogos = new ArrayList<String>(
            Arrays.asList("A&E", "ABC", "ABC News", "Adult Swim", "Andscape", "Aniplex", "BBC Studios",
                    "Cartoon Network", "CBS", "Discovery", "Disney XD", "FOX", "Freeform", "FX", "FYI", "HGTV",
                    "Hulu Original Series", "Lifetime", "Lionsgate", "LMN", "Magnolia", "Moonbug Entertainment ",
                    "MTV", "National Geographic", "Nickelodeon", "Saban Films", "Samuel Goldwyn Films",
                    "Searchlight Pictures", "Paramount+", "Sony Pictures Television", "The HISTORY Channel",
                    "TLC", "TV Land", "Twentieth Century Studios", "Vertical Entertainment", "Warner Bros"));

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74637"})
    @Test(groups = {TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyHulkCollectionPagesNetworkPageUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");
        sa.assertTrue(huluPage.isBackButtonPresent(), "Back button is not present");
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), "Artwork images is not present");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), STUDIOS_AND_NETWORKS_NOT_DISPLAYED);

        networkLogos.forEach(item -> {
            sa.assertTrue(huluPage.isNetworkLogoPresent(item), String.format("%s Network logo is not present", item));
            huluPage.clickOnNetworkLogo(item);
            sa.assertTrue(homePage.isNetworkLogoImageVisible(item), "Network logo page are not present");
            pause(3);
            String s3BucketPath = buildS3BucketPath(String.format("%s.png", item.replace(' ', '_')), "hulu-network-logos");
            File srcFile = homePage.getNetworkLogoImage(item).getElement().getScreenshotAs(OutputType.FILE);
            ImagesRequestS3 imagesComparisonRequest = new ImagesRequestS3(srcFile.getName(), FileUtil.encodeBase64File(srcFile), s3BucketPath);
            ImagesResponse360 imagesResponse360 = aliceManager.compareImages360S3(imagesComparisonRequest);
            JSONObject jsonResponse = new JSONObject(imagesResponse360.getData().toString());
            LOGGER.info("Raw JSON response: " + jsonResponse);
            double imageSimilarityPercentage = imagesResponse360.getSummary().getImageSimilarityPercentage();

            LOGGER.info("Similarity Percentage is: " + imageSimilarityPercentage);

            sa.assertTrue(
                    imageSimilarityPercentage >= imageSimilarityPercentageThreshold,
                    String.format("Similarity Percentage score was %,.2f or lower in %s Network logo {%,.2f}.", imageSimilarityPercentageThreshold, item, imageSimilarityPercentage));
            huluPage.clickOnNetworkBackButton();
            sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), STUDIOS_AND_NETWORKS_NOT_DISPLAYED);
        });

        sa.assertAll();
    }
}
