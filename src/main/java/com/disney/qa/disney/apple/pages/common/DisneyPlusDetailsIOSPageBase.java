package com.disney.qa.disney.apple.pages.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDetailsIOSPageBase extends DisneyPlusApplePageBase {

    private static final String DOWNLOAD_COMPLETED = "Download completed";
    private static final String WATCH = "WATCH";
    private static final String LOWER_CASE_WATCH = "watch";
    private static final String BOOKMARKED = "BOOKMARKED";
    private static final String LOWER_CASE_BOOKMARKED = "bookmarked";
    private static final String LOWER_CASED_PLAY = "play";
    private static final String PLAY = "PLAY";
    private static final String SUGGESTED_CELL_TITLE = "suggestedCellTitle";

    //LOCATORS

    @ExtendedFindBy(accessibilityId = "shareButton")
    private ExtendedWebElement shareBtn;

    @ExtendedFindBy(accessibilityId = "play")
    protected ExtendedWebElement playButton;

    @ExtendedFindBy(accessibilityId = "bookmarked")
    protected ExtendedWebElement continueButton;

    @ExtendedFindBy(accessibilityId = "watchlistButton")
    private ExtendedWebElement watchlistButton;

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement titleLabel;

    @ExtendedFindBy(accessibilityId = "downloadSeasonButton")
    protected ExtendedWebElement downloadSeasonButton;

    @ExtendedFindBy(accessibilityId = "DETAILS")
    protected ExtendedWebElement detailsTab;

    private ExtendedWebElement episodesTab = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_EPISODES.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"SUGGESTED\"`][1]")
    private ExtendedWebElement suggestedTab;
    @ExtendedFindBy(accessibilityId = "EXTRAS")
    protected ExtendedWebElement extrasTab;

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"Max Width View\"]/XCUIElementTypeCollectionView/XCUIElementTypeCell[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]")
    protected ExtendedWebElement tabBar;

    @FindBy(name = "titleLabel_0")
    private ExtendedWebElement firstTitleLabel;

    @ExtendedFindBy(accessibilityId = "contentDescription")
    protected ExtendedWebElement contentDescription;

    @ExtendedFindBy(accessibilityId = "releaseDate")
    protected ExtendedWebElement releaseDate;

    @ExtendedFindBy(accessibilityId = "genre")
    protected ExtendedWebElement genre;

    @ExtendedFindBy(accessibilityId = "rating")
    protected ExtendedWebElement rating;

    @ExtendedFindBy(accessibilityId = "formats")
    protected ExtendedWebElement formats;

    @ExtendedFindBy(accessibilityId = "suits")
    protected ExtendedWebElement suits;

    @ExtendedFindBy(accessibilityId = "actors")
    protected ExtendedWebElement actors;

    @ExtendedFindBy(accessibilityId = "duration")
    protected ExtendedWebElement duration;

    @ExtendedFindBy(accessibilityId = "metaDataLabel")
    protected ExtendedWebElement metaDataLabel;

    @ExtendedFindBy(accessibilityId = "trailerButton")
    protected ExtendedWebElement trailerButton;

    @ExtendedFindBy(accessibilityId = "downloadButton")
    protected ExtendedWebElement movieDownloadButton;

    @ExtendedFindBy(accessibilityId = "contentDetailsPage")
    public ExtendedWebElement contentDetailsPage;

    @ExtendedFindBy(accessibilityId = "additionalContentView")
    public ExtendedWebElement additionalContentView;

    @ExtendedFindBy(accessibilityId = "watch")
    protected ExtendedWebElement watchButton;

    @ExtendedFindBy(accessibilityId = "infoInactive24")
    private ExtendedWebElement infoView;

    @FindBy(id = "itemPickerClose")
    private ExtendedWebElement itemPickerClose;

    @ExtendedFindBy(accessibilityId = "flashAlertView")
    private ExtendedWebElement flashAlertView;

    @ExtendedFindBy(accessibilityId = "informationTitleLabel")
    private ExtendedWebElement informationTitleLabel;

    @ExtendedFindBy(accessibilityId = "informationDescriptionLabel")
    private ExtendedWebElement informationDescriptionLabel;

    @ExtendedFindBy(accessibilityId = "Widescreen format")
    private ExtendedWebElement widescreenFormat;

    @ExtendedFindBy(accessibilityId = "Original format")
    private ExtendedWebElement originalFormat;

    @FindBy(id = "seasonSelectorButton")
    private ExtendedWebElement seasonSelectorButton;

    @ExtendedFindBy(accessibilityId = "seasonRating")
    private ExtendedWebElement seasonRating;

    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement progressBar;

    @ExtendedFindBy(accessibilityId = "playIcon")
    private ExtendedWebElement playIcon;

    @ExtendedFindBy(accessibilityId = "title")
    private ExtendedWebElement detailsTabTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[3]/XCUIElementTypeImage")
    private ExtendedWebElement networkAttributionImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"contentImageView\"`][1]")
    private ExtendedWebElement firstContentImage;

    //FUNCTIONS

    public DisneyPlusDetailsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getContentDetailsPage().isPresent();
    }

    public boolean isOpened(long time) {
        dismissNotificationsPopUp();
        return shareBtn.isElementPresent(time);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPlayButton() {
        getTypeButtonByName("play").click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickWatchButton() {
        getTypeButtonByName(LOWER_CASE_WATCH).click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickContinueButton() {
        getTypeButtonByName(LOWER_CASE_BOOKMARKED).click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public ExtendedWebElement getDownloadButton() {
        return dynamicBtnFindByLabelContains.format("downloadEpisodeList");
    }

    public ExtendedWebElement getDownloadCompleteButton() {
        return dynamicBtnFindByLabelContains.format("downloadComplete");
    }

    public boolean isContinueButtonPresent() {
        return getTypeButtonByName(LOWER_CASE_BOOKMARKED).isElementPresent();
    }

    public DisneyPlusHomeIOSPageBase clickCloseButton() {
        backButton.click();
        return initPage(DisneyPlusHomeIOSPageBase.class);
    }

    //TODO Should discuss setting up Series vs. Movies page factories due to differences like this
    public void startDownload() {
        if (!movieDownloadButton.isElementPresent(DELAY)) {
            swipePageTillElementPresent(getDownloadButton(), 5, null, Direction.UP, 1);
            getDownloadButton().click();
        } else {
            movieDownloadButton.click();
        }
    }

    public void pauseDownload() {
        if (!movieDownloadButton.isElementPresent(DELAY)) {
            swipe(getDownloadButton());
            getDownloadButton().click();
        } else {
            movieDownloadButton.click();
        }
    }

    public void waitForSeriesDownloadToComplete() {
        LOGGER.info("Waiting for series download to complete");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Download complete text is not present")
                .until(it -> getDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public void waitForLongSeriesDownloadToComplete(int timeOut, int polling) {
        LOGGER.info("Waiting for long series download to complete");
        fluentWait(getDriver(), timeOut, polling, "Download complete text is not present")
                .until(it -> getDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public void waitForMovieDownloadComplete() {
        LOGGER.info("Waiting for movie download to complete");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Downloaded button is not present")
                .until(it -> getTypeButtonByName("downloadButtonDownloaded").isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public boolean isDownloadPaused(DisneyLocalizationUtils dictionary) {
        return getStaticTextByLabel(dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_PAUSED.getText())).isElementPresent();
    }

    public void removeDownload(DisneyLocalizationUtils dictionary) {
        getDynamicXpathContainsName(dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.REMOVE_DOWNLOAD_BTN.getText())).click();
    }

    public boolean isSeriesDownloadButtonPresent() {
        return getDownloadButton().isElementPresent();
    }

    public boolean doesContinueButtonExist() {
        return continueButton.isPresent();
    }

    public boolean doesPlayButtonExist() {
        return playButton.isPresent();
    }

    /**
     * This returns the media title of the given Details page by means of referencing the images that are on display.
     * The images are added to a list, which are then purged if their .getText() return is empty, as the only image
     * with a text value on display is the details page's title graphic. This will eventually be replaced
     * when the iOS dev team adds static accessibility IDs to the elements on the page.
     * @return - Media title
     */
    public String getMediaTitle() {
        System.out.println(logoImage.getText());
        return logoImage.getText();
    }

    public ExtendedWebElement getLogoImage() {
        return logoImage;
    }

    public String getEpisodeContentTitle() {
        //We want to remove the list numbering and duration from the episode's title label
        LOGGER.info("getting first episode title from Details page");
        return firstTitleLabel.getAttribute("value")
                .split("[.]")[1]
                .split("\\d+", 2)[0]
                .trim();
    }

    public void addToWatchlist() {
        watchlistButton.click();
    }

    public boolean isMediaPremierAccessLocked() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PREMIER_ACCESS_HEADLINE_MOVIE.getText())).isElementPresent();
    }

    public void dismissAlert() {
        logoImage.click();
    }

    public void downloadAllOfSeason() {
        downloadSeasonButton.click();
    }

    public void clickSeasonsButton(String season) {
        if (!isSeasonButtonDisplayed(season)) {
            scrollDown();
        }
        String seasonsButton = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, season));
        getDynamicAccessibilityId(seasonsButton).click();
    }
    public void clickExtrasTab() {
        if (!extrasTab.isPresent()) {
            swipePageTillElementTappable(extrasTab, 1, contentDetailsPage, Direction.UP, 900);
        }
        extrasTab.click();
    }

    public boolean isExtrasTabPresent() {
        if (!extrasTab.isPresent(SHORT_TIMEOUT)) {
            swipePageTillElementTappable(extrasTab, 1, contentDetailsPage, IMobileUtils.Direction.UP, 900);
        }
        return extrasTab.isPresent();
    }

    public void tapOnFirstContentTitle() {
        firstTitleLabel.click();
    }

    public List<ExtendedWebElement> getSeasonsFromPicker() {
        String season = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, ""));
        List<ExtendedWebElement> seasons = findExtendedWebElements(getStaticTextByLabelContains(season).getBy());
        LOGGER.info("Seasons: {}", seasons);
        return seasons;
    }

    public boolean isAlertTitleDisplayed() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return getStaticTextByLabel(getMediaTitle()).format().isElementPresent();
    }

    public boolean isTwentyDownloadsTextDisplayed() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String twentyDownloadsText = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.DOWNLOADS_SEASON_EPISODES_BATCH.getText()), Map.of("E", Integer.parseInt("20")));
        return getDynamicAccessibilityId(twentyDownloadsText).isElementPresent();
    }

    public boolean isSeasonButtonDisplayed(String season) {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String seasonsButton = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, season));
        return getDynamicAccessibilityId(seasonsButton).isElementPresent();
    }

    public void clickRemoveFromWatchlistButton() {
        String watchlistRemoveLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DETAILS_WATCHLIST_REMOVE_BTN.getText());
        getTypeButtonByLabel(watchlistRemoveLabel).format().click();
        LOGGER.info("'Remove from watchlist' button clicked");
    }


    public void swipeTabBar (Direction direction, int duration) {
        swipeInContainer(tabBar, direction, duration);
    }

    public void clickDetailsTab() {
        if (!detailsTab.isPresent()) {
            swipeInContainer(null, Direction.UP, 1200);
            pause(2); //transition
            swipeTabBar(Direction.LEFT, 1000);
        }
        detailsTab.click();
    }

    public boolean isContentDescriptionDisplayed() {
        return contentDescription.isPresent();
    }

    public boolean isReleaseDateDisplayed() {
        return releaseDate.isPresent();
    }

    public boolean isGenreDisplayed() {
        return genre.isPresent();
    }

    public boolean areFormatsDisplayed() {
        return formats.isPresent();
    }

    //Series = Creator, Movies = Director
    public boolean isCreatorDirectorDisplayed() {
        return suits.isPresent();
    }

    public boolean areActorsDisplayed() {
        return getActors().isPresent();
    }

    public boolean isDurationDisplayed() {
        return duration.isPresent();
    }

    public boolean isLogoImageDisplayed() {
        return logoImage.isPresent();
    }

    public boolean isMetaDataLabelDisplayed() {
        return metaDataLabel.isPresent();
    }

    public boolean isPlayButtonDisplayed() {
        return getPlayButton().isPresent();
    }

    public boolean isWatchlistButtonDisplayed() {
        return watchlistButton.isPresent();
    }

    public void clickWatchlistButton() { watchlistButton.click(); }

    public String getWatchlistButtonText() {
        return watchlistButton.getText();
    }

    public boolean isTrailerButtonDisplayed() {
        return trailerButton.isElementPresent();
    }

    public ExtendedWebElement getTrailerButton() {
        return trailerButton;
    }

    public boolean isDownloadButtonDisplayed() {
        return movieDownloadButton.isElementPresent();
    }

    public boolean doesOneOrMoreSeasonDisplayed() {
        String[] metadataLabelParts = metaDataLabel.getText().split(",");
        String[] seasonsText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_TOTAL_SEASON.getText()).split(" ");
        if (!getParsedString(metaDataLabel, "1", ",").contains("Seasons")) {
            LOGGER.info("verifying single season displayed");
            return metadataLabelParts[3].contains(seasonsText[0]);
        } else {
            LOGGER.info("verifying multi seasons displayed");
            String metaDataLabelSeasons = getParsedString(metaDataLabel, "1",",");
            String[] seasonParse = metaDataLabelSeasons.split(" ");
            String numberOfSeasons = seasonParse[0];
            String multiSeason = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_TOTAL_SEASONS.getText());
            multiSeason = getDictionary().formatPlaceholderString(multiSeason, Map.of("number_of_seasons", Integer.parseInt(numberOfSeasons)));
            return multiSeason.contains(metaDataLabelSeasons);
        }
    }

    public boolean doesMetadataYearContainDetailsTabYear() {
        LOGGER.info("verifying season year range");
        Map<String, String> params = new HashMap<>();
        String[] metadataLabelParts = metaDataLabel.getText().split(",");
        params.put("metaDataYear(s)", metadataLabelParts[0]);
        clickDetailsTab();
        swipePageTillElementPresent(releaseDate, 3, contentDetailsPage, Direction.UP, 500);
        String[] detailsTabYear = releaseDate.getText().split(", ");
        return params.get("metaDataYear(s)").contains(detailsTabYear[1]);
    }

    public boolean compareEpisodeNum() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        swipePageTillElementPresent( getDynamicXpathContainsName(titleLabel.toString()), 1, contentDetailsPage, Direction.UP, 2000);
        LOGGER.info("Retrieving current episode number..");
        String currentEpisodeNum = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()), 1,  contentDetailsPage, Direction.DOWN, 2000);
        clickWatchButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.waitForContentToEnd(450, 15);
        if (videoPlayer.isOpened()) {
            videoPlayer.clickBackButton();
        }
        isOpened();
        swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()),1, contentDetailsPage, Direction.UP, 2000);
        LOGGER.info("Retrieving recently played episode number..");
        String recentlyPlayedEpisode = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        LOGGER.info("Comparing current episode number with recently played episode number..");
        return currentEpisodeNum.contains(recentlyPlayedEpisode);
    }

    public boolean isWatchButtonPresent() {
        return watchButton.isElementPresent();
    }

    public String getContentTitle() {
        return logoImage.getText();
    }

    public boolean isWidescreenFormatTextPresent() {
        return widescreenFormat.isPresent();
    }

    public boolean isOriginalFormatTextPresent() {
        return originalFormat.isPresent();
    }

    public boolean isInformationTitleLabelPresent() {
        return informationTitleLabel.isElementPresent();
    }

    public boolean isInformationDescriptionPresent() {
        return informationDescriptionLabel.isElementPresent();
    }

    public void clickInfoView() {
        pressByElement(infoView, 1);
    }

    public void swipeTillActorsElementPresent() {
        swipePageTillElementPresent(getActors(), 3, contentDetailsPage, Direction.UP, 500);
    }

    public ExtendedWebElement getDetailsTab() {
        return detailsTab;
    }

    public ExtendedWebElement getActors() {
        return actors;
    }

    public ExtendedWebElement getContentDetailsPage() { return contentDetailsPage; }

    public ExtendedWebElement getFormats() { return formats; }

    public ExtendedWebElement getEpisodesTab() {
        return episodesTab;
    }

    public ExtendedWebElement getPlayButton() {
        return playButton;
    }

    public ExtendedWebElement getSeasonSelectorButton() {
        return seasonSelectorButton;
    }

    public ExtendedWebElement getItemPickerClose() {
        return itemPickerClose;
    }

    public ExtendedWebElement getTabBar() {
        return tabBar;
    }

    public ExtendedWebElement getInfoView() {
        return infoView;
    }

    public boolean isHeroImagePresent() {
        return getTypeOtherByName("heroImage").isPresent();
    }

    /**
     * This returns first tab cells in view. This can be used for Suggested or Extras tab.
     * @return - Tab cells
     */
    public List<String> getTabCells() {
        return getContentItems(6);
    }

    public void clickFirstTabCell() {
        String firstTabCell = getTabCells().get(0);
        getDynamicCellByLabel(firstTabCell).click();
    }

    public boolean isSuggestedTabPresent() {
        if (!suggestedTab.isPresent() && "Phone".equalsIgnoreCase(R.CONFIG.get(IOSUtils.DEVICE_TYPE))) {
            swipePageTillElementTappable(suggestedTab, 1, null, Direction.UP, 1200);
        }
        return suggestedTab.isPresent();
    }

    public void clickSuggestedTab() {
        suggestedTab.click();
    }

    public void compareSuggestedTitleToMediaTitle(SoftAssert sa) {
        Map<String, String> params = new HashMap<>();
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(IOSUtils.DEVICE_TYPE))) {
            swipeInContainer(null, Direction.UP, 1200);
        }
        clickSuggestedTab();
        params.put(SUGGESTED_CELL_TITLE, getTabCells().get(0));
        clickFirstTabCell();
        isOpened();
        String[] title = params.get(SUGGESTED_CELL_TITLE).split(",");
        clickDetailsTab();
        sa.assertTrue(title[0].toLowerCase().contains(detailsTabTitle.getText().toLowerCase()), "Suggested title is not the same as details tab title");
        params.clear();
    }

    public void compareExtrasTabToPlayerTitle(SoftAssert sa) {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        Map<String, String> params = new HashMap<>();
        clickExtrasTab();
        String[] extrasCellTitle = getTabCells().get(0).split(",");
        params.put("extrasCellTitle", extrasCellTitle[0].trim());
        clickFirstTabCell();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(params.get("extrasCellTitle").equalsIgnoreCase(videoPlayer.getTitleLabel()),
                "Extras title is not the same as video player title");
    }

    public boolean isStaticTextLabelPresent(String label) {
        return getStaticTextByLabelContains(label).isElementPresent(HALF_TIMEOUT);
    }

    public ExtendedWebElement getUpcomingDateTime() {
        String[] upcomingDateTime = getAiringBadgeLabel().getText().split(" ");
        String upcomingDate = upcomingDateTime[2] + " " + upcomingDateTime[3];
        String upcomingTime = upcomingDateTime[0];
        String upcomingDateAndTime = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        BADGE_TEXT_DATE_TIME.getText()),
                Map.of("date", upcomingDate, "time", upcomingTime));
        return getDynamicAccessibilityId(upcomingDateAndTime);
    }

    public ExtendedWebElement getUpcomingBadge() {
        String upcomingBadge = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, BADGE_LABEL_EVENT_UPCOMING.getText());
        return getStaticTextByLabel(upcomingBadge);
    }

    public ExtendedWebElement getUpcomingTodayBadge() {
        String upcomingTodayBadge = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, BADGE_LABEL_EVENT_UPCOMING_TODAY.getText());
        return getStaticTextByLabel(upcomingTodayBadge);
    }

    public String getDetailsTabTitle() {
        String[] contentDesc = contentDescription.getText().split(" is");
        return contentDesc[0];
    }

    public boolean isSeasonRatingPresent() {
        return seasonRating.isPresent();
    }

    public ExtendedWebElement getSuggestedTab() {
        return suggestedTab;
    }

    public ExtendedWebElement getExtrasTab() {
        return extrasTab;
    }

    public boolean isProgressBarPresent() {
        return progressBar.isPresent();
    }

    public String getDetailsTabSeasonRating() {
        String[] seasonNumberRating = getTypeOtherContainsLabel("Season").getText().split(":");
        String[] seasonNumber = seasonNumberRating[0].split(" ");
        String number = seasonNumber[1];
        return getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DETAILS_SEASON_RATING.getText()), Map.of("season_number", Integer.parseInt(number)));
    }

    public String getSeasonSelector() {
        String[] seasonSelector = seasonSelectorButton.getText().split(" ");
        return seasonSelector[1];
    }

    /**
     * Below are QA env specific methods for DWTS Anthology.
     * To be deprecated when DWTS Test Streams no longer available on QA env (QAA-12244).
     */

    public DisneyPlusVideoPlayerIOSPageBase clickQAWatchButton() {
        if (getTypeButtonByName(WATCH).isPresent()) {
            getTypeButtonByName(WATCH).click();
        } else {
            getTypeButtonByName(LOWER_CASE_WATCH).click();
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public boolean isQAWatchButtonPresent() {
        return getStaticTextByLabelContains(WATCH).isPresent() || getStaticTextByLabelContains(LOWER_CASE_WATCH).isPresent();
    }

    public boolean isContentDetailsPagePresent() {
        return getTypeOtherByName("contentDetailsPage").isPresent();
    }

    public ExtendedWebElement getFirstTitleLabel() {
        return firstTitleLabel;
    }

    public ExtendedWebElement getNetworkAttributionImage() {
        return networkAttributionImage;
    }

    public ExtendedWebElement getEpisodeContentImage() {
        return firstContentImage;
    }

    public ExtendedWebElement getServiceAttribution() {
        return staticTextLabelContains.format("Included with your Hulu subscription");
    }

    public ExtendedWebElement getPlayIcon() {
        return playIcon;
    }

    public ExtendedWebElement getCompactEpisodeAdditionalContentView() {
        return getTypeOtherByName("compactEpisodeAdditionalContentView");
    }
}