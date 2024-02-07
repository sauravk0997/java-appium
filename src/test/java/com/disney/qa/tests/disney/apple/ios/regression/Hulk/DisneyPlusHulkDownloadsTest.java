package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
//import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;/
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DisneyPlusHulkDownloadsTest extends DisneyBaseTest {
    private static final String PREY = "Prey";
    private static final String ONLY_MURDERS_IN_THE_BUILDING = "Only Murders in the Building";
    Map<List<String>, List<String>> params = new HashMap<>();

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74626", "XMOBQA-75242", "XMOBQA-75325"})
    @Test(description = "Verify download actions of episode, season and movie", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHuluPremiumDownloadActions() {

        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        //Movie download button
//        homePage.isOpened();
//        homePage.clickSearchIcon();
//        searchPage.searchForMedia(PREY);
//        searchPage.getDisplayedTitles().get(0).click();
//        detailsPage.isOpened();
//        sa.assertTrue(detailsPage.getMovieDownloadButton().isPresent(), "Movie download button is not found.");

        //Episode download buttons
        detailsPage.clickSearchIcon();
//        searchPage.clearText();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        IntStream.range(0, getEpisodesNoRepeats().size()).forEach(i ->
                sa.assertTrue(detailsPage.getTypeButtonContainsLabel(getEpisodesNoRepeats().get(i)).isPresent(),
                        "Download episode was not found"));

////        System.out.println(detailsPage.findAllEpisodes());
//        sa.assertTrue(detailsPage.getEpisodeToDownload("1", "1").isPresent(), "First episode to download is not present");
//        System.out.println(getDriver().getPageSource());
//        Map<String, String> params = new HashMap<>();
//
//        IntStream.range(0, detailsPage.findAllEpisodes().size()).forEach(i ->
//                sa.assertTrue(detailsPage.getTypeButtonContainsLabel(detailsPage.findAllEpisodes().get(i)).isPresent(),
//                        "Download episode was not found"));
//
//
//        IntStream.range(5, detailsPage.findAllEpisodes().size()).forEach(i ->
//                sa.assertTrue(detailsPage.getTypeButtonContainsLabel(detailsPage.findAllEpisodes().get(i)).isPresent(),
//                        "Download episode was not found"));
    }

    private List<String> getEpisodesNoRepeats() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        Set<String> setList = new LinkedHashSet<>(detailsPage.findAllEpisodes());
        params.put(Collections.singletonList("episodes"), detailsPage.findAllEpisodes());
        swipeUp(1200);
        params.put(Collections.singletonList("episodes"), detailsPage.findAllEpisodes());
        List<String> allEpisodesNoDupes = params.get(Collections.singletonList("episodes")).stream().distinct().collect(Collectors.toList());
        return allEpisodesNoDupes;
    }
}
