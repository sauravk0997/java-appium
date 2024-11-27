package com.disney.qa.tests.disney.apple.tvos.regression.search;

import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

public class DisneyPlusAppleTVSearchHuluHubTests extends DisneyPlusAppleTVBaseTest {

    private static final String HULU_CONTENT = "Only Murders in the Building";
    private static final String UNLOCK = "Unlock";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121510"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithStandaloneAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        String standaloneAccount = "alekhya.rallapalli+6740c467@disneyplustesting.com";
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        loginATVHuluHub(standaloneAccount);
        Assert.assertTrue(home.isOpened(), "Home page did not open");
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.typeInSearchField(HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(HULU_CONTENT).isPresent(), "Hulu content is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121512"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithBundleUserAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        String bundlePremiumAccount = "alekhya.rallapalli+6740d2fc@disneyplustesting.com";
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        loginATVHuluHub(bundlePremiumAccount);
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.typeInSearchField(HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(HULU_CONTENT).isPresent(), "Hulu movie is not present");
        searchPage.clickSearchResult(HULU_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open");
        Assert.assertFalse(detailsPage.getUpgradeNowButton().isPresent(), "Upsell message is present");
        Assert.assertTrue(detailsPage.isPlayButtonDisplayed(), "Play button is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121513"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithNonBundleUserAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        String premiumAccount = "robert.walters+6740c4f3@disneyplustesting.com";
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        loginATVHuluHub(premiumAccount);
        Assert.assertTrue(home.isOpened(), "Home page did not open");
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        // Look for non entitled Hulu content
        searchPage.typeInSearchField(HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(HULU_CONTENT).isPresent(), "Hulu movie is not present");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(UNLOCK).isPresent(),
                "Unlock 'upsell message' not found in search result");
        searchPage.clickSearchResult(HULU_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open");
        Assert.assertTrue(detailsPage.getUpgradeNowButton().isPresent(), "Upgrade Now button is not present");
        detailsPage.getUpgradeNowButton().click();
        Assert.assertTrue(detailsPage.isOnlyAvailableWithHuluHeaderPresent(), "Hulu ineligible screen header is not present");
        Assert.assertTrue(detailsPage.isIneligibleScreenBodyPresent(), "Hulu ineligible screen description  is not present");
        Assert.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(), "Hulu button is not present");
    }
}
