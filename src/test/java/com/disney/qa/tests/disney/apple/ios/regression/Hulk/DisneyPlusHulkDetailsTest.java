package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.bouncycastle.util.encoders.BufferedDecoder;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.DEFAULT_IMAGE_PATH;

public class DisneyPlusHulkDetailsTest extends DisneyBaseTest {
    protected ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    protected ThreadLocal<String> pathToZip = new ThreadLocal<>();
    private static final String BABY_YODA = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";


    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74543"})
    @Test(description = "On Junior Profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        disneyAccountApi.get().addProfile(disneyAccount.get(), JUNIOR_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), BABY_YODA, true, true);

        setAppToHomeScreen(disneyAccount.get(), JUNIOR_PROFILE);
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");
        homePage.clickRandomCollectionTile(CollectionConstant.Collection.ANIMATED_MOVIES_QA, 3, homePage.getHomeContentView(), Direction.UP);
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
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");

        homePage.clickRandomCollectionTile(CollectionConstant.Collection.NEW_TO_DISNEY_QA, 3, homePage.getHomeContentView(), Direction.UP);
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
        huluPage.swipeTillCollectionPresent(CollectionConstant.Collection.HULK_MOVIES_QA, 3, null, Direction.UP);
        huluPage.clickCollectionTile(CollectionConstant.Collection.HULK_MOVIES_QA, 1);
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab was not found");

        if (R.CONFIG.get("env").equalsIgnoreCase("PROD")) {
            detailsPage.clickDetailsTab();
        }
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

        huluPage.clickCollectionTile(CollectionConstant.Collection.HULK_PLAYABLE_QA, 0);
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74548"})
    @Test(description = "Hulk Details verify included with hulu subscription network attribution", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkNetworkAttribution() throws IOException {
        baseDirectory.set("Screenshots/");
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToHomeScreen(disneyAccount.get());
        homePage.isOpened();
        System.out.println(homePage.isHuluTileVisible());
        homePage.tapHuluBrandTile();
        pause(10);
//        homePage.clickSearchIcon();
//        searchPage.searchForMedia("Only Murders in the Building");
//        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        BufferedImage networkAttributionImage = getElementImage(detailsPage.getNetworkAttributionImage());

        BufferedImage networkAttributionImage2 = getElementImage(detailsPage.getNetworkAttributionImage());

        System.out.println(areImagesTheSame(networkAttributionImage, networkAttributionImage2, 10));
        Screenshot.capture(detailsPage.getNetworkAttributionImage().getElement(), ScreenshotType.EXPLICIT_VISIBLE);
//        detailsPage.swipePageTillElementPresent(detailsPage.getEpisodeContentImage(),3, null, Direction.UP, 750);
        detailsPage.swipeUp(1, 750);
        System.out.println(detailsPage.getEpisodeContentImage().isPresent());
        pause(3);
//         detailsPage.splitImage(detailsPage.getEpisodeContentImage(), baseDirectory, "Details_Page");

        System.out.println(areImagesTheSame(networkAttributionImage, detailsPage.splitImage(detailsPage.getEpisodeContentImage(), baseDirectory, "Details_Page"), 10));
    }
}
