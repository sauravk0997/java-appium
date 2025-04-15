package com.disney.qa.tests.disney.apple.tvos.regression.anthology;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static com.disney.qa.api.disney.DisneyEntityIds.DANCING_WITH_THE_STARS;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVAnthologyTest extends DisneyPlusAppleTVBaseTest {

    //Test constants
    private static final String PLAY = "PLAY";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106662"})
    @Test(description = "Verify Anthology Series - Watchlist", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyWatchlist() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchList = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage search = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();
        details.addToWatchlist();
        details.clickMenuTimes(1,1);
        pause(1); //from transition to search bar
        search.clickMenuTimes(1,1);
        home.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(watchList.areWatchlistTitlesDisplayed(DANCING_WITH_THE_STARS.getTitle()), "Dancing With The Stars was not added to watchlist.");

        watchList.getTypeCellLabelContains(DANCING_WITH_THE_STARS.getTitle()).click();
        sa.assertTrue(details.isOpened(), DANCING_WITH_THE_STARS.getTitle() + " details page did not load.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106657"})
    @Test(description = "Verify Anthology Series - Search", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologySearch() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage search = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        logIn(getUnifiedAccount());
        home.isOpened();
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        search.isOpened();
        search.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        sa.assertTrue(search.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).isElementPresent(), "Dancing with the Stars title not found in search results.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110034"})
    @Test(description = "Verify Anthology Series - Title, Description, Date", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyTitleDescriptionDate() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.getLogoImage().isPresent(), DANCING_WITH_THE_STARS.getTitle() + "logo image was not found.");
        sa.assertTrue(details.doesMetadataYearContainDetailsTabYear(), "Metadata label date year not found and does not match details tab year.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Content Description not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110036"})
    @Test(groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyVODProgress() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ PLAY + " label not found, currently live content playing. " + e);
        }

        details.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open after clicking play button.");
        videoPlayer.clickMenuTimes(1,1);
        sa.assertTrue(details.isOpened(), "Details page did not open.");
        details.waitForPresenceOfAnElement(details.getContinueButton());
        details.clickContinueButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open after clicking continue button.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105988", "XCDQA-110055"})
    @Test(description = "Verify Anthology Series - Details Tab", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyDetailsTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        String mediaTitle = details.getMediaTitle();
        sa.assertTrue(details.isOpened(), "Details page did not open.");
        sa.assertTrue(details.getDynamicRowButtonLabel("DETAILS", 1).isElementPresent(), "Details tab is not found.");

        details.moveDown(1,1);
        details.moveRight(3,1);
        details.isFocused(details.getDetailsTab());
        sa.assertTrue(details.getDetailsTabTitle().contains(mediaTitle), "Details tab title does not match media title.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Details tab content description is not present.");
        sa.assertTrue(details.isReleaseDateDisplayed(), "Release date is not present.");
        sa.assertTrue(details.isGenreDisplayed(), "Genre is not present.");
        sa.assertTrue(details.isRatingPresent(), "Season rating is not present.");
        sa.assertTrue(details.areFormatsDisplayed(), "Formats are not present.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105998", "XCDQA-110054"})
    @Test(description = "Verify Anthology Series - Suggested Tab", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologySuggestedTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found.");
        details.compareSuggestedTitleToDetailsTabTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105989", "XCDQA-110046"})
    @Test(description = "Verify Anthology Series - Extras Tab", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyExtrasTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isExtrasTabPresent(), "Extras tab was not found.");
        sa.assertTrue(details.isExtrasContentImageViewPresent(), "Artwork is not found in extras Tab");
        sa.assertTrue(details.isExtrasTabTitlePresent(), "Content Title is not found in extras tab");
        sa.assertTrue(details.isDurationTimeLabelPresent(), "Content Duration time is not found in extras tab");
        sa.assertTrue(details.isExtrasTabTitleDescriptionPresent(), "Content Description is not found in extras tab");
        details.compareExtrasTabToPlayerTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105993"})
    @Test(groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyFeaturedVOD() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found, currently live content playing. " + e);
        }

        sa.assertTrue(details.isLogoImageDisplayed(), "Logo image is not present.");
        sa.assertTrue(details.isHeroImagePresent(), "Hero image is not present.");
        sa.assertTrue(details.getStaticTextByLabelContains("TV-PG").isPresent(), "TV-PG rating was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("HD").isPresent(), "HD was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("5.1").isPresent(), "5.1 was not found.");
        sa.assertTrue(details.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(details.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(details.isPlayButtonDisplayed(), "Play button is not found.");

        details.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickMenuTimes(1,1);
        details.isOpened();
        details.waitForPresenceOfAnElement(details.getContinueButton());
        sa.assertTrue(details.doesContinueButtonExist(), "Continue button not displayed after exiting playback.");
        sa.assertTrue(details.getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_DETAILS_RESTART.getText())).isPresent(),
                "Restart button is not displayed");
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not present after exiting playback.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-109938"})
    @Test(description = "Verify Anthology Series - Trailer", groups = {TestGroup.ANTHOLOGY, US})
    public void verifyAnthologyTrailer() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isTrailerButtonDisplayed(), "Trailer button was not found.");

        details.getTrailerButton().click();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");

        videoPlayer.waitForTvosTrailerToEnd(75, 5);
        sa.assertTrue(details.isOpened(), "After trailer completed, did not return to details page.");
        sa.assertTrue(details.isFocused(details.getTrailerButton()), "Trailer button is not focused on.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-109773"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.VIDEO_PLAYER, US})
    public void verifyAnthologyTabsAndOrderingUI() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        logIn(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        ArrayList<Container> dwtsDetailsPageContainers =
                getDisneyAPIPage(DANCING_WITH_THE_STARS.getEntityId(),
                        getLocalizationUtils().getLocale(),
                        getLocalizationUtils().getUserLanguage());

        IntStream.range(0, dwtsDetailsPageContainers.size()).forEach(i -> {
            String expectedTabBarTitleName = dwtsDetailsPageContainers.get(i).getVisuals().getName();
            Assert.assertTrue(details.getTypeButtonByLabel(expectedTabBarTitleName).isPresent(),
                    expectedTabBarTitleName + " tabBar title not displayed");
            Assert.assertEquals(
                    expectedTabBarTitleName,
                    details.getTabBarTitleInfo().get(i).getAttribute(Attributes.NAME.getAttribute()),
                    "Tabs are not displayed in expected order");
        });
    }

    private void searchAndOpenDWTSDetails() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        searchPage.clickSearchResult(DANCING_WITH_THE_STARS.getTitle());
        detailsPage.isOpened();
    }
}
