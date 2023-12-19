package com.disney.qa.tests.disney.apple.ios.regression.alice;

import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.AliceApiUtil;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.config.ConfigProperties;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.regression.alice.DisneyPlusAliceDataProvider.HulkContentS3;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWait;

public class DisneyPlusHulkS3BaselineCompare extends DisneyBaseTest {

    private static final String HANDSET_S3_PATH = "src/test/resources/json/hulk-top-ten-s3-handset.json";
    private static final String TABLET_S3_PATH = "src/test/resources/json/hulk-top-ten-s3-tablet.json";

    private static final String TABLET_S3_LOCAL_PATH = "src/test/resources/json/ipad6genlocal.json";

    public List<Object[]> parseHulkS3Json(String filePath) {
        List<Object[]> data = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);

        try {
            JsonNode jsonObjects = objectMapper.readTree(jsonFile);

            jsonObjects.forEach((jsonObject) -> {
                HulkContentS3 content = new HulkContentS3(
                        jsonObject.get("title").asText(),
                        jsonObject.get("entityId").asText(),
                        jsonObject.get("s3File").asText());

                data.add(new Object[]{content});
            });

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error parsing test JSON file.");
        }
        return data;
    }

    @DataProvider
    public Iterator<Object[]> handsetDataContentProvider() {
        return parseHulkS3Json(HANDSET_S3_PATH).iterator();
    }

    @DataProvider
    public Iterator<Object[]> tabletDataContentProvider() { return parseHulkS3Json(TABLET_S3_PATH).iterator(); }

    private AliceApiUtil getAliceClient() {
        return new AliceApiUtil(MULTIVERSE_STAGING_ENDPOINT);
    }

    private AliceApiManager getAliceApiManager() {
        return new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);
    }

    @BeforeTest(alwaysRun = true, groups = TestGroup.NO_RESET)
    private void setUp() {
        initialSetup("US", "en");
        handleAlert();
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        }
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    private void aliceS3BaselineVsLatestScreenshot(HulkContentS3 hulkContentS3) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String deeplinkFormat = "disneyplus://www.disneyplus.com/browse/entity-";

        launchDeeplink(true, deeplinkFormat + hulkContentS3.getEntityId(), 10);
        detailsPage.clickOpenButton();
        sa.assertTrue(detailsPage.isAppRunning(sessionBundles.get(DISNEY)), "Disney app is not running.");
        detailsPage.dismissNotificationsPopUp();
        fluentWait(getDriver(), 20, 3, "Details tab is not present").until(it -> detailsPage.getDetailsTab().isPresent(1));

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        LOGGER.info("S3 File: " + hulkContentS3.getS3FileName());
        sa.assertTrue(getAliceClient().isImageSimilar360S3(
                srcFile, hulkContentS3.getS3FileName()),"Images are not similar");

        ImagesRequestS3 imagesComparisonRequest = new ImagesRequestS3(srcFile.getName(), FileUtil.encodeBase64File(srcFile), hulkContentS3.getS3FileName());
        ImagesResponse360 imagesResponse360 = getAliceApiManager().compareImages360S3(imagesComparisonRequest);
        JSONObject jsonResponse = new JSONObject(imagesResponse360.getData().toString());
        double imageSimilarityPercentage = imagesResponse360.getSummary().getImageSimilarityPercentage();

        LOGGER.info("Similarity Percentage is: " + imageSimilarityPercentage);
        LOGGER.info("Raw JSON response: " + jsonResponse);

        sa.assertTrue(
                imageSimilarityPercentage > ConfigProperties.getInstance().getPercentageOfSimilarity(),
                "Similarity Percentage score was 95 or lower.");

        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @Test(dataProvider = "handsetDataContentProvider", description = "Alice Base Compare Images Test - Handset")
    public void aliceBaselineCompareS3HandsetTest(HulkContentS3 hulkContent) {
        aliceS3BaselineVsLatestScreenshot(hulkContent);
    }

    @Maintainer("csolmaz")
    @Test(dataProvider = "tabletDataContentProvider", description = "Alice Base Compare Images Test - Tablet")
    public void aliceBaselineCompareS3TabletTest(HulkContentS3 hulkContent) {
        aliceS3BaselineVsLatestScreenshot(hulkContent);
    }
}