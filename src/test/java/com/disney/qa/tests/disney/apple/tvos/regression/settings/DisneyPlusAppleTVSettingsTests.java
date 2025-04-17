package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVLegalPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSettingsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.IConstantHelper.WELCOME_SCREEN_NOT_DISPLAYED;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVSettingsTests extends DisneyPlusAppleTVBaseTest {
    private static final String SETTINGS_PAGE_NOT_DISPLAYED = "Settings page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68157"})
    @Test(groups = {TestGroup.SMOKE, US})
    public void verifyLogout() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        Assert.assertTrue(settingsPage.isOpened(), SETTINGS_PAGE_NOT_DISPLAYED);
        homePage.moveDownUntilElementIsFocused(settingsPage.getLogOutCell(), 8);
        // Log out and validate welcome screen is open
        settingsPage.getLogOutCell().click();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68357"})
    @Test(groups = {TestGroup.SMOKE, US})
    public void verifyLegalPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVLegalPage legalPage = new DisneyPlusAppleTVLegalPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String firstParagraphTerms = "ENGLISH – DISNEY TERMS OF USE – UNITED STATES";

        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        Assert.assertTrue(settingsPage.isOpened(), SETTINGS_PAGE_NOT_DISPLAYED);
        // Navigate and select Legal option and validate options
        homePage.moveDownUntilElementIsFocused(legalPage.getTypeCellLabelContains(legalPage.getLegalOption().getText()), 8);
        legalPage.getTypeCellLabelContains(legalPage.getLegalOption().getText()).click();

        Assert.assertTrue(legalPage.isOpened(), "Legal page did not open");
        legalPage.verifyLegalHeaders();

        // Validate first option is focused and opened
        sa.assertTrue(legalPage.isFocused(legalPage.getDisneyTermsUse()), "First option is not focused");
        sa.assertTrue(legalPage.getStaticTextLabelName(firstParagraphTerms).isPresent(),
                "First option's view is not displayed");
        sa.assertAll();
    }
}
