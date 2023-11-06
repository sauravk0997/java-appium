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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class DisneyPlusAliceTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    protected ThreadLocal<String> pathToZip = new ThreadLocal<>();

    private static final String DETAILS_PAGE = "_Details_Page";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74625"})
    @Test(description = "App Launches - Alice validates Disney logo present on Welcome screen", groups = {"Alice", TestGroup.PRE_CONFIGURATION})
    public void testAppLaunch() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreenPage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        welcomeScreenPage.isOpened();
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @Test(description = "Details Page - take screenshot, create zip of image", groups = {"Alice", TestGroup.PRE_CONFIGURATION})
    public void testSeriesDetailsScreenshot() {
        baseDirectory.set("Screenshots/");
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(disneyAccount.get());

        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.clickSeriesTab();
        searchPage.selectRandomTitle();
        detailsPage.isOpened();
        String detailsTitle = detailsPage.getMediaTitle();
        getAliceScreenshots(detailsTitle + DETAILS_PAGE, baseDirectory);

    }

    private void getAliceScreenshots(String fileName, ThreadLocal<String> directory) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            UniversalUtils.storeScreenshot(getDriver(), fileName + "_iPad_Portrait_" , directory.get());
            rotateScreen(ScreenOrientation.LANDSCAPE);
            dismissKeyboardByClicking(5, 3);
            pause(2);
            UniversalUtils.storeScreenshot(getDriver(), fileName + "_iPad_Landscape_" + getDate(), directory.get());
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            UniversalUtils.storeScreenshot(getDriver(), fileName + "_iPhone_" + getDate(), directory.get());
        }
        pathToZip.set(String.format("Detail_Page_Images_%s_%s_%s.zip", R.CONFIG.get("locale"), R.CONFIG.get("language"), getDate()));
        UniversalUtils.archiveAndUploadsScreenshots(baseDirectory.get(), pathToZip.get());
    }
}
