package com.disney.qa.tests.disney.apple.ios.regression.alice;

import ch.qos.logback.classic.Logger;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

public class DisneyPlusAliceTest extends DisneyBaseTest {

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
    @Test(description = "Details Page - validate images, create zip of images, validate part on alice", groups = {"Alice", TestGroup.PRE_CONFIGURATION})
    public void testDetailsPageImageCompare() {

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        //        AliceDriver aliceDriver = new AliceDriver(getDriver());
        SoftAssert sa = new SoftAssert();
        //use areImagesTheSame
        setAppToHomeScreen(disneyAccount.get());

        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia("Loki");
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        BufferedImage lokiDetailsImage = getElementImage(detailsPage.contentDetailsPage);
        BufferedImage lokiDetailsImageCopy = getScaledImage(lokiDetailsImage, lokiDetailsImage.getWidth(), lokiDetailsImage.getHeight());

        detailsPage.getBackArrow().click();
        searchPage.searchForMedia("Ahsoka");
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        BufferedImage moonKnightDetailsImage = getElementImage(detailsPage.contentDetailsPage);
        BufferedImage moonKnightImageCopy = getScaledImage(moonKnightDetailsImage, moonKnightDetailsImage.getWidth(), moonKnightDetailsImage.getHeight());
//        LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
        System.out.println(areImagesTheSame(lokiDetailsImage, moonKnightDetailsImage, 31));
        System.out.println(areImagesTheSame(lokiDetailsImageCopy, moonKnightImageCopy, 31));

        sa.assertTrue(areImagesTheSame(lokiDetailsImage, moonKnightDetailsImage, 31));
        sa.assertTrue(areImagesTheSame(lokiDetailsImageCopy, moonKnightImageCopy, 31));
        sa.assertAll();
    }
}
