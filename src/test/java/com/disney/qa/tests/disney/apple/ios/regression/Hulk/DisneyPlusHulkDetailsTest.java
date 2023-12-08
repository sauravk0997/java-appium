package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusHulkDetailsTest extends DisneyBaseTest {
    private static final String BABY_YODA = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";
    private static final String ONLY_MURDERS_IN_THE_BUILDING = "Only Murders in the Building";


    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74543"})
    @Test(description = "On Junior Profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), JUNIOR_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);
        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        pause(3);
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent() ||  homePage.getUnavailableContentErrorPreview().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");
        pause(2);
        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");
        homePage.clickRandomCollectionTile(CollectionConstant.Collection.KIDS_PRINCESSES_AND_FAIRY_TALES_PROD, 3, homePage.getHomeContentView(), Direction.UP);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74568"})
    @Test(description = "On Adult profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyAdultProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");

        homePage.clickRandomCollectionTile(CollectionConstant.Collection.NEW_TO_DISNEY_PROD, 3, homePage.getHomeContentView(), Direction.UP);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74633"})
    @Test(description = "Hulk Movie Details: Verify Details Tab Metadata", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkMovieDetailsTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia("Prey");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab was not found");

        if (R.CONFIG.get("env").equalsIgnoreCase("PROD")) {
            detailsPage.clickDetailsTab();
            scrollDown();
        }
        if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Tablet")) {
            detailsPage.swipeTillActorsElementPresent();;
        }
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.isDurationDisplayed(), "Detail Tab duration is not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74830"})
    @Test(description = "Hulk Movie Details: Verify Tabs are visible", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkDetailsTabs() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PROD, 1);
        detailsPage.isOpened();

        //validate episodes tab
        sa.assertTrue(detailsPage.getEpisodesTab().isPresent(), "Episodes tab not present on Details page");
        detailsPage.getEpisodesTab().click();
        sa.assertTrue(detailsPage.getSeasonSelectorButton().isPresent(), "Season selector button not found on Episodes tab");

        //validate details tab
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present");
        detailsPage.clickDetailsTab();
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present on Details tab");

        //validate suggested tab
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(), "Suggest tab not present");
        detailsPage.compareSuggestedTitleToMediaTitle(sa);

        //validate extras tab
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(), "Extras tab not present on Details page");
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.getFirstTitleLabel().getText().toLowerCase().contains("trailer"), "'Trailer' text not found in extras title.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74548"})
    @Test(description = "Hulk Details verify included with hulu subscription network attribution", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkNetworkAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PROD, 1);
        detailsPage.isOpened();
        if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Phone")) {
            Assert.assertTrue(detailsPage.getHandsetNetworkAttributionImage().isPresent(), "Handset Network attribution image was not found on Hulu series details page.");
        } else {
            Assert.assertTrue(detailsPage.getTabletNetworkAttributionImage().isPresent(), "Tablet Network attribution image was not found on Hulu series details page.");
        }
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74599"})
    @Test(description = "Hulk Details verify included with hulu subscription service attribution", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        pause(3);
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PROD, 1);
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getServiceAttribution().isPresent(), "Service attribution was not found on Hulu series detail page.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73898"})
    @Test(description = "Hulk Details verify extras tab", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkExtrasTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PROD, 1);
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab was not found.");

        detailsPage.clickExtrasTab();
        if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Extras tab play icon was not found");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "First extras title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "First extras description was not found");
        sa.assertTrue(detailsPage.getFirstRunTimeLabel().isPresent(), "First extras runtime was not found");

        detailsPage.getPlayIcon().click();
        videoPlayer.isOpened();
        pause(20);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickSuggestedTab();
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Duration not displayed on extras trailer.");
        sa.assertAll();
    }

    private void continuousPlay(int count) {
        while (count>=0) {
            DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
            pause(15);
            videoPlayer.clickPauseButton();
            pause(2);
            videoPlayer.clickPlayButton();
            count--;
        }
    }
}
