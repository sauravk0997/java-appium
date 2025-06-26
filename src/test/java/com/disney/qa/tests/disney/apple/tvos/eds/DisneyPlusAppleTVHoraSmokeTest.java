package com.disney.qa.tests.disney.apple.tvos.eds;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.pojos.UnifiedAccount;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.hora.validationservices.HoraValidator;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
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

public class DisneyPlusAppleTVHoraSmokeTest extends DisneyPlusAppleTVBaseTest {

    private static final int playbackDuration = 10;

    @Test(description = "Hora Smoke test - performs login, playback, and logout. DUST events are validated", groups = {TestGroup.HORA_SMOKE, US})
    @Maintainer("jwang4")
    public void horaSmokeTvos(ITestContext context) {
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = loginAndStartPlayback();
        exitAndLogout();

        if (Configuration.getRequired(DisneyConfiguration.Parameter.ENABLE_HORA_VALIDATION, Boolean.class)) {
            HoraValidator hv = new HoraValidator(account.getAccountId());
            hv.assertValidation(sa);
        }
        sa.assertAll();
    }

    // Performs login, entitlement, and starts playback
    private UnifiedAccount loginAndStartPlayback() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandsPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        UnifiedAccount account = getUnifiedAccount();
        addHoraValidationSku(account);

        selectAppleUpdateLaterAndDismissAppTracking();
        logIn(account);
        pause(1);

        // Locate and click on the "Pixar" tile
        homePage.moveDownFromHeroTileToBrandTile();
        pause(1);
        homePage.moveRight(1, 1);
        Assert.assertTrue(homePage.isFocused(homePage.getDynamicCellByLabel("Pixar")), "Pixar tile not focused");
        homePage.clickSelect();
        Assert.assertTrue(brandsPage.isOpened(), "Pixar page not displayed");

        // Locate and click on the "Star Wars" tile
        brandsPage.clickBack();
        Assert.assertTrue(homePage.isOpened(), "Did not return to home page");
        Assert.assertTrue(homePage.isFocused(homePage.getDynamicCellByLabel("Pixar")), "Pixar tile not focused");
        homePage.moveRight(2, 1);
        Assert.assertTrue(homePage.isFocused(homePage.getDynamicCellByLabel("Star Wars")), "Star Wars tile not focused");
        homePage.clickSelect();
        Assert.assertTrue(brandsPage.isOpened(), "Star Wars page not displayed");

        // Start a title from the star wars page
        brandsPage.clickFirstCarouselPoster();
        detailsPage.clickPlayButton().waitForVideoToStart();
        pause(playbackDuration);
        Assert.assertTrue(videoPage.isOpened(), "Video Player did not launch");

        // Pause 1s, then resume
        videoPage.pauseAndPlayVideo();

        return account;
    }

    private void exitAndLogout() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVBrandsPage brandsPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        // Check if we are still in the player
        Assert.assertTrue(videoPage.isOpened(), "Video Player is not currently open");

        // Exit player
        videoPage.clickBack();
        pause(5);
        Assert.assertTrue(detailsPage.isOpened(), "Details page is not open after exiting playback");

        // Navigate to home
        homePage.openGlobalNavWithClickingMenu();
        Assert.assertTrue(brandsPage.isOpened(), "Brands page did not open when exiting details page");
        homePage.openGlobalNavWithClickingMenu();
        Assert.assertTrue(homePage.isOpened(), "Home page is not open");

        // Go to settings and log out
        homePage.openGlobalNavWithClickingMenu();
        homePage.clickSettingsTab();
        Assert.assertTrue(settingsPage.isOpened(), "Settings page is not open");
        settingsPage.clickAccountBtn();
        settingsPage.clickLogOutAllDevicesBtn();
        Assert.assertTrue(new DisneyPlusAppleTVWelcomeScreenPage(getDriver()).isOpened(), "Welcome screen not opened after logout");
    }

}
