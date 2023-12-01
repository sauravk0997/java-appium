package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.TimeConstant.SHORT_TIMEOUT;

public class DisneyPlusHulkDetailsTest extends DisneyBaseTest {
    private static final String BABY_YODA = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";

    protected ThreadLocal<String> baseDirectory = new ThreadLocal<>();

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74543"})
    @Test(description = "On Junior Profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
//        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
//        setAppToHomeScreen(disneyAccount.get());
        setAppToHulkHomeScreen(JUNIOR_PROFILE);
        homePage.isOpened();
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");
        homePage.clickRandomCollectionTile(CollectionConstant.Collection.KIDS_PRINCESSES_AND_FAIRY_TALES_PREVIEW, 3, homePage.getHomeContentView(), Direction.UP);
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
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToHomeScreen(disneyAccount.get());

        homePage.isOpened();
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");

        homePage.clickRandomCollectionTile(CollectionConstant.Collection.NEW_TO_DISNEY_PREVIEW, 3, homePage.getHomeContentView(), Direction.UP);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74633"})
    @Test(description = "Hulk Movie Details: Verify Details Tab Metadata", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkMovieDetailsTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToHomeScreen(disneyAccount.get());

        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.swipeTillCollectionPresent(CollectionConstant.Collection.HULU_ORIGINALS_PREVIEW, 3, null, Direction.UP);
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_ORIGINALS_PREVIEW, 1);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab was not found");

        detailsPage.clickDetailsTab();
        detailsPage.swipeTillActorsElementPresent();
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
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToHomeScreen(disneyAccount.get());
        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PREVIEW, 1);
        detailsPage.isOpened();

        //validate details tab
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present");
        detailsPage.clickDetailsTab();
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present on Details tab");

        //validate episodes tab
        sa.assertTrue(detailsPage.getEpisodesTab().isPresent(), "Episodes tab not present on Details page");
        detailsPage.getEpisodesTab().click();
        sa.assertTrue(detailsPage.getSeasonSelectorButton().isPresent(), "Season selector button not found on Episodes tab");

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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74599"})
    @Test(description = "Hulk Details verify included with hulu subscription service attribution", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToHomeScreen(disneyAccount.get());

        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PREVIEW, 1);
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
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToHomeScreen(disneyAccount.get());

        homePage.isOpened();
        homePage.isHuluTileVisible();
        homePage.tapHuluBrandTile();
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULU_FEATURED_PREVIEW, 1);
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
        continuousPlay(1);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickSuggestedTab();
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Duration not displayed on extras trailer.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74863", "XMOBQA-74548"})
    @Test(description = "Hulk Network Attribution on various series/movie details pages - different networks", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkSeriesAndMovieNetworkAttribution() {
        baseDirectory.set("Screenshots/");
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
//        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
//        setAppToHomeScreen(disneyAccount.get());
        setAppToHulkHomeScreen(ADULT_PROFILE);

        homePage.isOpened();
        IntStream.range(0, getMedia().size()).forEach(i -> {
            homePage.clickSearchIcon();
            searchPage.isOpened();
            if (searchPage.getClearText().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(getMedia().get(i));
                    List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
                    results.get(0).click();
                    detailsPage.isOpened();
            if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Phone")) {
                Assert.assertTrue(detailsPage.getHandsetNetworkAttributionImage().isPresent(), "Handset Network attribution image was not found on " + i + " series details page.");
            } else {
                Assert.assertTrue(detailsPage.getTabletNetworkAttributionImage().isPresent(), "Tablet Network attribution image was not found on " + i + " series details page.");
            }
        });
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

    protected ArrayList<String> getMedia() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add("Private Property");
        contentList.add("Only Murders in the Building");
        contentList.add("Palm Springs");
        contentList.add("Abbott Elementary");
        contentList.add("Home Economics");
        contentList.add("Cruel Summer");
        contentList.add("Praise Petey");
        return contentList;
    }
}
