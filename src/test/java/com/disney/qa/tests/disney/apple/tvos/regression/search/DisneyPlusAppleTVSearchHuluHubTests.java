package com.disney.qa.tests.disney.apple.tvos.regression.search;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustConsentBannerIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.CA;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

public class DisneyPlusAppleTVSearchHuluHubTests extends DisneyPlusAppleTVBaseTest {

    private static final String HULU_CONTENT = "Only Murders in the Building";
    private static final String HULU_CONTENT_NOT_AVAILABLE_IN_CANADA = "Normal People";

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121511"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, CA})
    public void verifyHuluHubSearchContentInNonEligibleCountry() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusOneTrustConsentBannerIOSPageBase bannerIOSPageBase =
                new DisneyPlusOneTrustConsentBannerIOSPageBase(getDriver());
        String standaloneAccount = "alekhya.rallapalli+6745f17f@disneyplustesting.com";
        selectAppleUpdateLaterAndDismissAppTracking();
       // Assert.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

       // loginATVHuluHub(standaloneAccount);
        if (bannerIOSPageBase.isAllowAllButtonPresent()) {
            bannerIOSPageBase.tapAcceptAllButton();
        }
        Assert.assertTrue(home.isOpened(), "Home page did not open");
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.typeInSearchField(HULU_CONTENT_NOT_AVAILABLE_IN_CANADA);
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(HULU_CONTENT_NOT_AVAILABLE_IN_CANADA),
                "No results found message was not as expected for non eligible country Canada");
    }
}
