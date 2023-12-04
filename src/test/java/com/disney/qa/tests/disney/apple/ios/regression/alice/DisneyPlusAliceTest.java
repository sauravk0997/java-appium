package com.disney.qa.tests.disney.apple.ios.regression.alice;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.ScreenOrientation;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

public class DisneyPlusAliceTest extends DisneyBaseTest {

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-74625" })
    @Test(description = "App Launches - Alice validates Disney logo present on Welcome screen", groups = { "Alice", TestGroup.PRE_CONFIGURATION })
    public void testAppLaunch() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreenPage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        welcomeScreenPage.isOpened();
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @Test(description = "Details Page - take screenshot, create zip of image", groups = { "Alice", TestGroup.PRE_CONFIGURATION })
    public void testSeriesDetailsScreenshot() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.isOpened();
        searchPage.clickSeriesTab();
        searchPage.selectRandomTitle();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> detailsPage.isOpened());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Detail page was not open." + e);
        }
        getAliceScreenshots(getDetailsTitle() + "_Details_Page");
    }

    private void getAliceScreenshots(String fileName) {
        String directory = "Screenshots/";
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            UniversalUtils.storeScreenshot(getDriver(), fileName + "_iPad_Portrait_" + getDate(), directory);
            rotateScreen(ScreenOrientation.LANDSCAPE);
            pause(2);
            UniversalUtils.storeScreenshot(getDriver(), fileName + "_iPad_Landscape_" + getDate(), directory);
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
            UniversalUtils.archiveAndUploadsScreenshots(directory,
                    String.format("iPad_Detail_Page_Images_%s_%s_%s.zip", R.CONFIG.get("locale"), R.CONFIG.get("language"), getDate()));
        } else {
            UniversalUtils.storeScreenshot(getDriver(), fileName + "_iPhone_" + getDate(), directory);
            UniversalUtils.archiveAndUploadsScreenshots(directory,
                    String.format("iPhone_Detail_Page_Images_%s_%s_%s.zip", R.CONFIG.get("locale"), R.CONFIG.get("language"), getDate()));
        }
    }

    private String getDetailsTitle() {
        String detailsTitle = initPage(DisneyPlusDetailsIOSPageBase.class).getMediaTitle();
        if (detailsTitle.contains("/")) {
            //to mitigate detail page titles that have a "/" which can cause an extra folder to be created
            return detailsTitle.replace("/", "_");
        } else {
            return detailsTitle;
        }
    }
}
