package com.disney.qa.tests.disney.apple.ios.eds;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.hora.validationservices.HoraValidator;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusHoraSmokeTest extends DisneyBaseTest {
    private static final boolean DEBUG_SCREENSHOTS = false;
    private static final int playbackDuration = 10;

    @Test(description = "Hora Smoke test - performs login, playback, and logout. DUST events are validated", groups = {TestGroup.HORA_SMOKE, US})
    @Maintainer("jwang4")
    public void horaSmoke(ITestContext context) {
        SoftAssert sa = new SoftAssert();

        DisneyAccount account = loginAndStartPlayback();
        exitAndLogout();

        if (Configuration.getRequired(DisneyConfiguration.Parameter.ENABLE_HORA_VALIDATION, Boolean.class)) {
            HoraValidator hv = new HoraValidator(account.getAccountId());
            hv.assertValidation(sa);
        }
        sa.assertAll();
    }

    private DisneyAccount loginAndStartPlayback() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase homePage = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusBrandIOSPageBase brandPage = new DisneyPlusBrandIOSPageBase(getDriver());
        DisneyPlusVideoPlayerIOSPageBase videoPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());

        DisneyAccount account = getAccount();
        addHoraValidationSku(account);
        welcomePage.clickDontAllowBtn();
        Assert.assertTrue(welcomePage.isOpened(), "Welcome screen did not launch");
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Login Steps
        welcomePage.clickLogInButton();
        new DisneyPlusLoginIOSPageBase(getDriver()).fillOutEmailField(account.getEmail());
        hideKeyboard();
        new DisneyPlusPasswordIOSPageBase(getDriver()).submitPasswordForLogin(account.getUserPass());
        Assert.assertTrue(homePage.isOpened(), "Home screen did not launch");
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Locate and click on the "Pixar" tile
        homePage.clickPixarTile();
        Assert.assertTrue(brandPage.isOpened(), "Pixar page not displayed");
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Navigate back to the homepage via the back button
        brandPage.tapBackButton();
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Locate and click on the "Star Wars" tile
        homePage.clickStarWarsTile();
        Assert.assertTrue(brandPage.isOpened(), "Star Wars page not displayed");
        if (DEBUG_SCREENSHOTS) if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Start a title from the star wars page
        brandPage.clickFirstCarouselPoster();
        new DisneyPlusDetailsIOSPageBase(getDriver()).clickPlayButton().waitForVideoToStart();
        pause(playbackDuration);
        Assert.assertTrue(videoPage.isOpened(), "Video Player did not launch");
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Pause 1s, then resume
        videoPage.clickPauseButton();
        pause(1);
        videoPage.displayVideoController();
        videoPage.clickPlayButton();
        pause(playbackDuration);

        return account;
    }

    private void exitAndLogout() {
        DisneyPlusVideoPlayerIOSPageBase videoPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase detailsPage = new DisneyPlusDetailsIOSPageBase(getDriver());

        // Check if we are still in the player
        Assert.assertTrue(videoPage.isOpened(), "Video Player is not currently open");

        // Exit player
        videoPage.clickBackButton();
        pause(1);
        Assert.assertTrue(detailsPage.isOpened(), "Details page is not open after exiting playback");
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Go to settings
        detailsPage.clickCloseButton();
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        // Log out
        logout();
        if (DEBUG_SCREENSHOTS) Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

}
