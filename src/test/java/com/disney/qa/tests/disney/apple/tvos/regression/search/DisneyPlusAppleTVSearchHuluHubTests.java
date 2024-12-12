package com.disney.qa.tests.disney.apple.tvos.regression.search;

import com.disney.qa.api.utils.DisneySkuParameters;
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

    private static final String UNENTITLED_HULU_CONTENT = "Only Murders in the Building";
    private static final String ENTITLED_HULU_CONTENT = "The Bold Type";
    private static final String UNLOCK = "Unlock";
    private static final String HOME_PAGE_ERROR_MESSAGE = "Home page did not open";
    private static final String SEARCH_PAGE_ERROR_MESSAGE = "Search page did not open";
    private static final String HULU_CONTENT_ERROR_MESSAGE = "Hulu content is not present";
    private static final String DETAILS_PAGE_ERROR_MESSAGE = "Details page did not open";
    private static final String WELCOME_SCREEN_ERROR_MESSAGE = "Welcome screen did not launch";
    private static final String HULU_CONTENT_NOT_AVAILABLE_IN_CANADA = "Normal People";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121510"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithStandaloneAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        String standaloneAccount = "alekhya.rallapalli+6740c467@disneyplustesting.com";

        loginATVHuluHub(standaloneAccount);
        Assert.assertTrue(home.isOpened(), HOME_PAGE_ERROR_MESSAGE);
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121512"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithBundleUserAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        String bundlePremiumAccount = "alekhya.rallapalli+6740d2fc@disneyplustesting.com";

        loginATVHuluHub(bundlePremiumAccount);
        Assert.assertTrue(home.isOpened(), HOME_PAGE_ERROR_MESSAGE);
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
        searchPage.clickSearchResult(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);
        Assert.assertFalse(detailsPage.getUpgradeNowButton().isPresent(), "Upsell message is present");
        Assert.assertTrue(detailsPage.isPlayButtonDisplayed(), "Play button is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121511"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, CA})
    public void verifyHuluHubSearchContentInNonEligibleCountry() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusOneTrustConsentBannerIOSPageBase bannerIOSPageBase =
                new DisneyPlusOneTrustConsentBannerIOSPageBase(getDriver());
        String standaloneAccount = "alekhya.rallapalli+6745f17f@disneyplustesting.com";

        loginATVHuluHub(standaloneAccount);
        if (bannerIOSPageBase.isAllowAllButtonPresent()) {
            bannerIOSPageBase.tapAcceptAllButton();
        }
        Assert.assertTrue(home.isOpened(), HOME_PAGE_ERROR_MESSAGE);
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(HULU_CONTENT_NOT_AVAILABLE_IN_CANADA);
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(HULU_CONTENT_NOT_AVAILABLE_IN_CANADA),
                "No results found message was not as expected for non eligible country Canada");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121513"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentWithNonBundleUserAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        logIn(getAccount());
        Assert.assertTrue(home.isOpened(), HOME_PAGE_ERROR_MESSAGE);
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        // Look for entitled Hulu content
        searchPage.typeInSearchField(ENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(ENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
        Assert.assertFalse(searchPage.getTypeCellLabelContains(UNLOCK).isPresent(),
                "Unlock 'upsell message' found in search result");

        searchPage.clearSearchBar();

        // Look for non entitled Hulu content
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
        Assert.assertTrue(searchPage.getTypeCellLabelContains(UNLOCK).isPresent(),
                "Unlock 'upsell message' not found in search result");
        searchPage.clickSearchResult(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);
        Assert.assertTrue(detailsPage.getUpgradeNowButton().isPresent(), "Upgrade Now button is not present");
        detailsPage.getUpgradeNowButton().click();
        Assert.assertTrue(detailsPage.isOnlyAvailableWithHuluHeaderPresent(), "Hulu ineligible screen header is not present");
        Assert.assertTrue(detailsPage.isIneligibleScreenBodyPresent(), "Hulu ineligible screen description is not present");
        Assert.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(), "Ineligible CTA button is not present");
    }
}
