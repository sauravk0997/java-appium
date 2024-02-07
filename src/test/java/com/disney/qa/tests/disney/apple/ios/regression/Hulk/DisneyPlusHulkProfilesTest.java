package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

public class DisneyPlusHulkProfilesTest extends DisneyBaseTest {

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74938"})
    @Test(description = "Downloads are filtered out on Junior profile ", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileHuluFilteredOutDownloads() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), JUNIOR_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Download TV-MA content
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        detailsPage.getHuluEpisodeToDownload("1", "1").click();
        detailsPage.waitForOneHuluEpisodeDownloadToComplete(150, 15);
        searchPage.clickSearchIcon();
        searchPage.clearText();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        detailsPage.getMovieDownloadButton().click();
        detailsPage.waitForMovieDownloadComplete(350, 30);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloadsPage.isOpened();
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(ONLY_MURDERS_IN_THE_BUILDING).isPresent(),
                ONLY_MURDERS_IN_THE_BUILDING + "was not found present on Downloads screen.");
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(PREY).isPresent(),
                PREY + "was not found present on Downloads screen.");

        //Validate on Junior profile downloaded assets not visible
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(ONLY_MURDERS_IN_THE_BUILDING).isElementNotPresent(SHORT_TIMEOUT),
                ONLY_MURDERS_IN_THE_BUILDING +  " was found present on " + getAccount().getProfiles().get(1) + " profile's Downloads screen.");
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(PREY).isElementNotPresent(SHORT_TIMEOUT),
                PREY + " was found present on " + getAccount().getProfiles().get(1) + " profile's Downloads screen.");
        sa.assertAll();
    }
}
