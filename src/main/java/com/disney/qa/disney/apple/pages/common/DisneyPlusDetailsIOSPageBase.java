package com.disney.qa.disney.apple.pages.common;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.IntStream;

import com.disney.config.DisneyConfiguration;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDetailsIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PHONE = "Phone";
    private static final String DOWNLOAD_COMPLETED = "Download completed";
    private static final String WATCH = "WATCH";
    private static final String LOWER_CASE_WATCH = "watch";
    private static final String SUGGESTED_CELL_TITLE = "suggestedCellTitle";
    private static final String SHOP_WEB_URL = "disneystore.com";
    private static final String SHOP_TAB_HEADING = "Shop this Character";
    private static final String SHOP_TAB_SUBHEADING = "Bring your favorite Disney";
    private static final String SHOP_TAB_LEGALTEXT = "Merchandise available while supplies last.";
    private static final String SHOP_TAB_NAVIGATETOWEBTEXT = "Go to Disney store.com";
    private static final String IMAX_ENHANCED = "IMAX Enhanced";
    private static final String DEAF_FEATURE_DESCRIPTION = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DETAILS_FEATURE_SDH.getText());
    private static final String AUDIO_FEATURE_DESCRIPTION = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DETAILS_FEATURE_AUDIO_DESCRIPTIONS.getText());
    private final List<String> videoOrAudioQuality = Arrays.asList("HD", "4K", "Ultra HD", "dolby vision", "5.1", DEAF_FEATURE_DESCRIPTION, AUDIO_FEATURE_DESCRIPTION);

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

    @ExtendedFindBy(accessibilityId = "restartButton")
    protected ExtendedWebElement restartButton;

    private ExtendedWebElement episodesTab = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_EPISODES.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"SUGGESTED\"`][1]")
    private ExtendedWebElement suggestedTab;
    @ExtendedFindBy(accessibilityId = "EXTRAS")
    protected ExtendedWebElement extrasTab;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]")
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
    private ExtendedWebElement handsetNetworkAttributionImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[4]/XCUIElementTypeImage")
    private ExtendedWebElement tabletNetworkAttributionImage;

    @ExtendedFindBy(accessibilityId = "descriptionLabel_0")
    private ExtendedWebElement firstDescriptionLabel;

    @ExtendedFindBy(accessibilityId = "runtimeLabel_0")
    private ExtendedWebElement firstRunTimeLabel;

    @ExtendedFindBy(accessibilityId = "SHOP")
    protected ExtendedWebElement shopTab;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[3]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage")
    private ExtendedWebElement shopTabImage;

    @ExtendedFindBy(accessibilityId = "Stop the offline download for this title.")
    private ExtendedWebElement stopOfflineDownload;

    @ExtendedFindBy(accessibilityId = "titleLabel_9")
    private ExtendedWebElement tenthTitleLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label == \"copy\"`]")
    private ExtendedWebElement copyShareLink;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[$type='XCUIElementTypeStaticText' AND label CONTAINS 'IMAX Enhanced'$][2]")
    private ExtendedWebElement imaxEnhancedmediaFeaturesRow;

    @ExtendedFindBy(accessibilityId = "VERSIONS")
    protected ExtendedWebElement versionsTab;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name == \"playIcon\"`][1]")
    protected ExtendedWebElement iMaxEnhancedThumbnail;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"contentImageView\"`][1]")
    protected ExtendedWebElement contentImageView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"selectableTitle\"`]")
    private ExtendedWebElement seasonItemPicker;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"runtime\"`]")
    private ExtendedWebElement durationTimeLabel;

    //FUNCTIONS

    public DisneyPlusDetailsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDetailsTab().isPresent();
    }

    public boolean isOpened(long time) {
        dismissNotificationsPopUp();
        return shareBtn.isElementPresent(time);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPlayButton() {
        getPlayButton().click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickWatchButton() {
        getTypeButtonByName(LOWER_CASE_WATCH).click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickContinueButton() {
        getContinueButton().click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public ExtendedWebElement getDownloadButton() {
        return dynamicBtnFindByLabelContains.format("downloadEpisodeList");
    }

    public ExtendedWebElement getDownloadCompleteButton() {
        return dynamicBtnFindByLabelContains.format("downloadComplete24");
    }

    public boolean isContinueButtonPresent() {
        return getContinueButton().isPresent();
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

    public ExtendedWebElement getMovieDownloadButton() {
        return movieDownloadButton;
    }

    public void pauseDownload() {
        if (!movieDownloadButton.isElementPresent(DELAY)) {
            swipe(getDownloadButton());
            getDownloadButton().click();
        } else {
            movieDownloadButton.click();
        }
    }

    public void waitForSeriesDownloadToComplete(int timeOut, int polling) {
        LOGGER.info("Waiting for series download to complete");
        fluentWait(getDriver(), timeOut, polling, "Download complete text is not present")
                .until(it -> getDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public void waitForMovieDownloadComplete(int timeOut, int polling) {
        LOGGER.info("Waiting for movie download to complete");
        fluentWait(getDriver(), timeOut, polling, "Downloaded button is not present")
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
     *
     * @return - Media title
     */
    public String getMediaTitle() {
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

    public void waitForWatchlistButtonToAppear() {
        LOGGER.info("Waiting for  WatchlistButton to appear");
        fluentWait(getDriver(), FORTY_FIVE_SEC_TIMEOUT, SHORT_TIMEOUT, "Watchlist button is not present")
                .until(it -> watchlistButton.isPresent());
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

    public ExtendedWebElement getDownloadAllSeasonButton() {
        return downloadSeasonButton;
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

    public ExtendedWebElement getRemoveFromWatchListButton() {
        String watchlistRemoveLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DETAILS_WATCHLIST_REMOVE_BTN.getText());
        return getTypeButtonByLabel(watchlistRemoveLabel);
    }


    public void swipeTabBar(Direction direction, int duration) {
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

    public ExtendedWebElement getContentDescription() {
        return contentDescription;
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
        return dynamicOtherFindByLabelContains.format("Director").isPresent(5) || dynamicOtherFindByLabelContains.format("Creator").isPresent(5);
    }

    public boolean areActorsDisplayed() {
        return getActors().isPresent(5) || dynamicOtherFindByNameContains.format("Starring").isPresent(5);
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

    public ExtendedWebElement getReleaseDate() {
        return releaseDate;
    }

    public ExtendedWebElement getDuration() {
        return duration;
    }

    public ExtendedWebElement getGenre() {
        return genre;
    }

    public ExtendedWebElement getRating() {
        return rating;
    }

    public boolean isPlayButtonDisplayed() {
        return getPlayButton().isPresent();
    }

    public boolean isWatchlistButtonDisplayed() {
        return watchlistButton.isPresent();
    }

    public ExtendedWebElement getWatchlistButton() {
        return watchlistButton;
    }

    public void clickWatchlistButton() {
        watchlistButton.click();
    }

    public String getWatchlistButtonText() {
        return watchlistButton.getText();
    }

    public boolean isTrailerButtonDisplayed() {
        return trailerButton.isPresent();
    }

    public ExtendedWebElement getTrailerButton() {
        return trailerButton;
    }

    public boolean isMovieDownloadButtonDisplayed() {
        return movieDownloadButton.isPresent();
    }

    public boolean doesOneOrMoreSeasonDisplayed() {
        String[] metadataLabelParts = metaDataLabel.getText().split(",");
        String[] seasonsText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_TOTAL_SEASON.getText()).split(" ");
        if (!getParsedString(metaDataLabel, "1", ",").contains("Seasons")) {
            LOGGER.info("verifying single season displayed");
            return metadataLabelParts[3].contains(seasonsText[0]);
        } else {
            LOGGER.info("verifying multi seasons displayed");
            String metaDataLabelSeasons = getParsedString(metaDataLabel, "1", ",");
            String[] seasonParse = metaDataLabelSeasons.split(" ");
            String numberOfSeasons = seasonParse[0];
            String multiSeason = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_TOTAL_SEASONS.getText());
            multiSeason = getDictionary().formatPlaceholderString(multiSeason, Map.of("number_of_seasons", Integer.parseInt(numberOfSeasons)));
            return multiSeason.contains(metaDataLabelSeasons);
        }
    }

    /**
     * Method used to compare metadataLabel split string from main detail screen
     * feature area to a detail tab element's split string
     *
     * @param metadataPart   Split part of metadataLabel for comparison
     * @param element        details tab element
     * @param detailsTabPart Split part of a details tab element for comparison
     * @return - Media title
     */
    public boolean metadataLabelCompareDetailsTab(int metadataPart, ExtendedWebElement element, int detailsTabPart) {
        Map<String, String> params = new HashMap<>();
        String[] metadataLabelParts = metaDataLabel.getText().split(",");
        params.put("metadataLabelPart", metadataLabelParts[metadataPart]);
        clickDetailsTab();
        swipePageTillElementPresent(element, 3, null, Direction.UP, 500);
        String[] detailsTabParts = element.getText().split(",");
        LOGGER.info("Verifying metadata {} is same as details tab {}", metadataLabelParts[metadataPart], detailsTabParts[detailsTabPart]);
        return detailsTabParts[detailsTabPart].contains(params.get("metadataLabelPart").trim());
    }

    public boolean compareEpisodeNum() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()), 1, contentDetailsPage, Direction.UP, 2000);
        LOGGER.info("Retrieving current episode number..");
        String currentEpisodeNum = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()), 1, contentDetailsPage, Direction.DOWN, 2000);
        clickWatchButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.waitForContentToEnd(450, 15);
        if (videoPlayer.isOpened()) {
            videoPlayer.clickBackButton();
        }
        isOpened();
        swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()), 1, contentDetailsPage, Direction.UP, 2000);
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
        return getTypeButtonByLabel("DETAILS");
    }

    public ExtendedWebElement getActors() {
        return actors;
    }

    public ExtendedWebElement getContentDetailsPage() {
        return contentDetailsPage;
    }

    public ExtendedWebElement getFormats() {
        return formats;
    }

    public ExtendedWebElement getEpisodesTab() {
        return episodesTab;
    }

    public ExtendedWebElement getPlayButton() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_PLAY.getText()));
    }

    public ExtendedWebElement getContinueButton() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_CONTINUE.getText()));
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

    public ExtendedWebElement getRestartButton() {
        return restartButton;
    }

    public boolean isHeroImagePresent() {
        return getTypeOtherByName("heroImage").isPresent();
    }

    /**
     * This returns first tab cells in view. This can be used for Suggested or Extras tab.
     *
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
        if (!suggestedTab.isPresent() && PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipePageTillElementTappable(suggestedTab, 1, null, Direction.UP, 1200);
        }
        return suggestedTab.isPresent();
    }

    public void clickSuggestedTab() {
        suggestedTab.click();
    }

    public void compareSuggestedTitleToMediaTitle(SoftAssert sa) {
        Map<String, String> params = new HashMap<>();
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
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
        return detailsTabTitle.getText();
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

    public ExtendedWebElement getHandsetNetworkAttributionImage() {
        return handsetNetworkAttributionImage;
    }

    public ExtendedWebElement getTabletNetworkAttributionImage() {
        return tabletNetworkAttributionImage;
    }

    public ExtendedWebElement getServiceAttribution() {
        return staticTextLabelContains.format("Included with your Hulu subscription");
    }

    public ExtendedWebElement getPlayIcon() {
        return playIcon;
    }

    public ExtendedWebElement getFirstDescriptionLabel() {
        return firstDescriptionLabel;
    }

    public ExtendedWebElement getFirstDurationLabel() {
        return firstRunTimeLabel;
    }

    public ExtendedWebElement getShareBtn() {
        return shareBtn;
    }

    public ExtendedWebElement getShopBtn() {
        return shopTab;
    }

    public void clickShopTab() {
        if (!shopTab.isPresent()) {
            swipeInContainer(null, Direction.UP, 1200);
            pause(2); //transition
            swipeTabBar(Direction.LEFT, 1000);
        }
        shopTab.click();
    }

    public ExtendedWebElement getShopTabImage() {
        return shopTabImage;
    }

    public boolean isShopWebviewOpen() {
        ExtendedWebElement addressbar = PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType()) ? phoneWebviewAddressBar : tabletWebviewAddressBar;
        return addressbar.getText().contains(SHOP_WEB_URL);
    }

    public boolean isShopTabHeadingTextPresent() {
        return getStaticTextByLabel(SHOP_TAB_HEADING).isPresent();
    }

    public boolean isShopTabSubHeadingTextPresent() {
        return getStaticTextByLabelContains(SHOP_TAB_SUBHEADING).isPresent();
    }

    public boolean isShopTabLegalTextPresent() {
        return getStaticTextByLabel(SHOP_TAB_LEGALTEXT).isPresent();
    }

    public boolean isShopTabNavigateToWebTextPresent() {
        return getTypeOtherByLabel(SHOP_TAB_NAVIGATETOWEBTEXT).isPresent();
    }

    public void navigateToShopWebPage() {
        getTypeOtherByLabel(SHOP_TAB_NAVIGATETOWEBTEXT).click();
    }

    public ExtendedWebElement getEpisodeToDownload(String seasonNumber, String episodeNumber) {
        return getTypeButtonContainsLabel("Download season " + seasonNumber + ", episode " + episodeNumber);
    }

    /**
     * Use with hulu series content only - to get Hulu series download complete button
     */
    public ExtendedWebElement getHuluSeriesDownloadCompleteButton() {
        return dynamicBtnFindByLabelContains.format("Offline Download Options");
    }

    public void waitForOneEpisodeDownloadToComplete(int timeOut, int polling) {
        LOGGER.info("Waiting for one episode download to complete");
        fluentWait(getDriver(), timeOut, polling, "Download complete text is not present")
                .until(it -> getHuluSeriesDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public ExtendedWebElement getHuluContinueButton() {
        return getTypeButtonByName(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_CONTINUE.getText()));
    }

    public DisneyPlusVideoPlayerIOSPageBase clickOnHuluContinueButton() {
        getHuluContinueButton().click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }
    /**
     * Use with hulu series content only - to wait for 2 or more hulu episode downloads to complete
     */
    public void waitForTwoOrMoreHuluEpisodeDownloadsToComplete(int timeOut, int polling) {
        LOGGER.info("Waiting for episode downloads to complete..");
        waitForPresenceOfAnElement(stopOfflineDownload);
        fluentWait(getDriver(), timeOut, polling, "'Stop the offline download for this title' remained present.")
                .until(it -> !stopOfflineDownload.isPresent());
        swipePageTillElementPresent(tenthTitleLabel, 3, null, Direction.UP, 500);
        waitForPresenceOfAnElement(tenthTitleLabel);
        fluentWait(getDriver(), timeOut, polling, "'Stop the offline download for this title' remained present.")
                .until(it -> !stopOfflineDownload.isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    /**
     * Use with hulu series content only - Find all episode download buttons within current view.
     */
    public List<String> findAllEpisodeDownloadButtonsInCurrentView() {
        List<ExtendedWebElement> allEpisodes = findExtendedWebElements(getTypeButtonContainsLabel("Download season").getBy());
        List<String> episodeToDownloadTitles = new ArrayList<>();
        IntStream.range(0, allEpisodes.size()).forEach(i -> episodeToDownloadTitles.add(allEpisodes.get(i).getText()));
        return episodeToDownloadTitles;
    }

    public void clickDownloadSeasonAlertButton() {
        LOGGER.info("Clicking download season alert button..");
        clickAlertConfirm();
    }

    public ExtendedWebElement getSeasonButton(String season) {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String seasonsButton = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, season));
        return getDynamicAccessibilityId(seasonsButton);
    }

    public ExtendedWebElement getCopyShareLink() {
        return copyShareLink;
    }

    public void clickOnCopyShareLink() {
        getCopyShareLink().click();
    }
    public boolean isImaxEnhancedPromoLabelPresent(){
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_IMAX_ENHANCED_PROMO_LABEL.getText())).isPresent();
    }

    public boolean isImaxEnhancedPromoSubHeaderPresent(){
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_IMAX_ENHANCED_PROMO_SUBHEADER.getText())).isPresent();
    }

    public boolean isImaxEnhancedPresentInMediaFeaturesRow(){
        return imaxEnhancedmediaFeaturesRow.getText().contains(IMAX_ENHANCED);
    }

    public boolean isImaxEnhancedPresentBeforeQualityDetailsInFeturesRow(){
        String mediaFeaturesRow = imaxEnhancedmediaFeaturesRow.getText();
        String[]  featuresRowAfterSplit = mediaFeaturesRow.split(IMAX_ENHANCED);
        for(String item : videoOrAudioQuality)
            if(!featuresRowAfterSplit[0].contains(item) && featuresRowAfterSplit[1].contains(item)){
                return true;
            }
        return false;
    }

    public boolean isImaxEnhancedPresentsInFormats(){
        return formats.getText().contains(IMAX_ENHANCED);
    }

    public boolean isImaxEnhancedPresentBeforeQualityDetailsInFormats(){
        String availableformats = formats.getText();
        String[] formatsDetails = availableformats.split(":, ");
        return formatsDetails[1].startsWith(IMAX_ENHANCED);
    }

    public boolean isNegativeStereotypeAdvisoryLabelPresent() {
        ExtendedWebElement contentAdvisoryText = getTypeOtherByLabel(String.format("%s, %s ",
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DETAILS_CONTENT_ADVISORY_TITLE.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DETAILS_NEGATIVE_STEREOTYPE_ADVISORY.getText())).replace("\n", "").replace("\r", "").replace("  ", " ").trim());
        swipePageTillElementPresent(contentAdvisoryText, 1, contentDetailsPage, Direction.UP, 900);
        return contentAdvisoryText.isPresent();
    }

    public ExtendedWebElement getRatingRestrictionDetailMessage() {
        String upcomingBadge = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, RATING_RESTRICTION_DETAIL_MESSAGE.getText());
        return getStaticTextByLabel(upcomingBadge);
    }
    public void clickVersionsTab() {
        if (!versionsTab.isPresent()) {
            swipePageTillElementTappable(versionsTab, 1, contentDetailsPage, Direction.UP, 500);
        }
        versionsTab.click();
    }

    public ExtendedWebElement getVersionTab() {
        return versionsTab;
    }

    public boolean isIMAXEnhancedTitlePresentInVersionTab() {
        String[] iMaxEnhancedTitle =  getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_VERSIONS_IMAX_ENHANCED_TITLE.getText()).split("-");
        swipePageTillElementTappable(firstTitleLabel, 1, contentDetailsPage, Direction.UP, 500);
        return firstTitleLabel.getText().startsWith(iMaxEnhancedTitle[0]);
    }

    public boolean isIMAXEnhancedDescriptionPresentInVersionTab() {
        String iMaxEnhancedDescription =  getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_VERSIONS_IMAX_ENHANCED_DESCRIPTION.getText());
        return getStaticTextByLabel(iMaxEnhancedDescription).isPresent();
    }

    public boolean isIMAXEnhancedThumbnailPresentInVersionTab() {
        return iMaxEnhancedThumbnail.isPresent();
    }

    public String getMovieNameAndDurationFromIMAXEnhancedHeader() {
        String[] iMaxEnhancedTitle = firstTitleLabel.getText().split(" - ");
        return iMaxEnhancedTitle[1];
    }

    public boolean isContentImageViewPresent() {
        return contentImageView.isPresent();
    }

    public ExtendedWebElement getSeasonItemPicker() {
        return seasonItemPicker;
    }

    public ExtendedWebElement getDurationTimeLabel() {
        return durationTimeLabel;
    }

    public boolean isTabSelected(String tabName) {
        ExtendedWebElement tabButton = getDynamicRowButtonLabel(tabName.toUpperCase(), 1);
        if(!tabButton.isPresent()){
            swipeInContainer(null, Direction.UP, 1200);
            pause(2); //transition
            swipeTabBar(Direction.LEFT, 900);
        }
        return tabButton.getAttribute("value").equals("1");
    }

    public String getContinueWatchingHours() {
        String[] time = getStaticTextByLabelContains("remaining").getText().split(" ");
        return time[0].split("h")[0];
    }

    public String getContinueWatchingMinutes() {
        String[] time = getStaticTextByLabelContains("remaining").getText().split(" ");
        return time[1].split("m")[0];
    }

    public ExtendedWebElement getContinueWatchingTimeRemaining() {
        String continueWatchingHours = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CONTINUE_WATCHING_HOURS.getText()),
                Map.of("hours_remaining", getContinueWatchingHours(), "minutes_remaining", getContinueWatchingMinutes()));
        return getDynamicAccessibilityId(continueWatchingHours);
    }

    public boolean isDurationTimeLabelPresent() {
        return getFirstDurationLabel().isPresent(SHORT_TIMEOUT) || getDurationTimeLabel().isPresent(SHORT_TIMEOUT);
    }

    public boolean isSeriesDownloadButtonPresent(String seasonNumber, String episodeNumber) {
        return getDownloadButton().isElementPresent(SHORT_TIMEOUT) || getEpisodeToDownload(seasonNumber, episodeNumber).isPresent(SHORT_TIMEOUT);
    }
}
