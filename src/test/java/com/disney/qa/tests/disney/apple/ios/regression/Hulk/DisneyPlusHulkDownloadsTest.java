package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.PREY;

public class DisneyPlusHulkDownloadsTest extends DisneyBaseTest {

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74626", "XMOBQA-75242", "XMOBQA-75325"})
    @Test(description = "Verify download actions of episode, season and movie", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHuluPremiumDownloadActions() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        //Movie download button
        homePage.isOpened();
        homePage.clickSearchIcon();
//        searchPage.searchForMedia(PREY);
//        searchPage.getDisplayedTitles().get(0).click();
//        detailsPage.isOpened();
//        sa.assertTrue(detailsPage.getMovieDownloadButton().isPresent(),
//                "Movie download button was not found.");

        //Episode download buttons
//        detailsPage.clickSearchIcon();
//        searchPage.clearText();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
//        String season1NumberOfEpisodeDownloads = String.valueOf(getEpisodes().size());
//        detailsPage.clickSeasonsButton("1");
//        List <ExtendedWebElement> seasons = detailsPage.getSeasonsFromPicker();
//        seasons.get(1).click();
//        sa.assertTrue(String.valueOf(getEpisodes().size()).equalsIgnoreCase(season1NumberOfEpisodeDownloads),
//                "Season 1 and 2 total number of episode download buttons are not the same. Total expected number for each season: 10");

        detailsPage.getDownloadAllSeasonButton().click();
        detailsPage.clickDownloadSeasonAlertButton();
        detailsPage.waitForTwoOrMoreHuluEpisodeDownloadsToComplete("10", 600, 200);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloads.isOpened();
        System.out.println(getDriver().getPageSource());
        sa.assertTrue(downloads.getStaticTextByLabelContains("10 Episodes").isPresent(), "10 episode downloads were not found.");
        downloads.clickSeriesMoreInfoButton();
        System.out.println(getDriver().getPageSource());
//        sa.assertTrue(downloads.getStaticTextByLabelContains("Season 2").isPresent(), "Season 2 was not downloaded.");
         sa.assertAll();
    }

    private List<String> getEpisodes() {
        Map<List<String>, List<String>> params = new HashMap<>();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        params.put(Collections.singletonList("episodes"), detailsPage.findAllDownloadableEpisodesInCurrentView());
        swipeUp(1200);
        params.put(Collections.singletonList("episodes"), detailsPage.findAllDownloadableEpisodesInCurrentView());
        List<String> allEpisodesNoDuplicates = params.get(Collections.singletonList("episodes")).stream().distinct().collect(Collectors.toList());
        swipeDown(1200);
        return allEpisodesNoDuplicates;
    }
}
