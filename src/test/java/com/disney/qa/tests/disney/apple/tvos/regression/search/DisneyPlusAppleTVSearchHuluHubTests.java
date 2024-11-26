package com.disney.qa.tests.disney.apple.tvos.regression.search;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustConsentBannerIOSPageBase;
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121511"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyHuluHubSearchContentInNonEligibleCountry() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusOneTrustConsentBannerIOSPageBase bannerIOSPageBase =
                new DisneyPlusOneTrustConsentBannerIOSPageBase(getDriver());
        String standaloneAccount = "alekhya.rallapalli+6745f17f@disneyplustesting.com";
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        loginATVHuluHub(standaloneAccount);
        if (bannerIOSPageBase.isAllowAllButtonPresent()) {
            bannerIOSPageBase.tapAcceptAllButton();
        }
        Assert.assertTrue(home.isOpened(), "Home page did not open");
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.typeInSearchField(HULU_CONTENT);
        Assert.assertFalse(searchPage.getStaticTextByLabelContains(HULU_CONTENT).isPresent(),
                "Hulu content is present");
    }


}
