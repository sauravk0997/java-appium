package com.disney.qa.disney.apple.pages.common;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.explore.ExploreApi;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.explore.response.ExplorePageResponse;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.stream.IntStream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDetailsIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PHONE = "Phone";
    private static final String DOWNLOAD_COMPLETED = "Download completed";
    private static final String WATCH = "WATCH";
    private static final String LOWER_CASE_WATCH = "watch";
    private static final String SUGGESTED_CELL_TITLE = "suggestedCellTitle";
    private static final String IMAX_ENHANCED = "IMAX Enhanced";
    private static final String DOLBY_VISION = "Dolby Vision";
    private static final String SHOP_PROMO_LABEL_HEADER = "Discover Exclusive Disney+ Subscriber Perks";
    private static final String SHOP_PROMO_LABEL_SUBHEADER = "Visit the PERKS tab to learn more.";
    private static final String DETAILS_DURATION_SUFFIX = "remaining";
    private static final String UPGRADE_NOW = "UPGRADE NOW";
    private static final String UNLOCK = "UNLOCK";
    private static final String UNLOCK_HULU_ON_DISNEY = "Unlock Hulu on Disney+";
    private static final String UPGRADE_YOUR_PLAN = "Upgrade your plan to stream Hulu";

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "contentDetailsPage")
    public ExtendedWebElement contentDetailsPage;
    @ExtendedFindBy(accessibilityId = "play")
    protected ExtendedWebElement playButton;
    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement titleLabel;
    @ExtendedFindBy(accessibilityId = "downloadSeasonButton")
    protected ExtendedWebElement downloadSeasonButton;
    @ExtendedFindBy(accessibilityId = "DETAILS")
    protected ExtendedWebElement detailsTab;
    @ExtendedFindBy(accessibilityId = "restartButton")
    protected ExtendedWebElement restartButton;
    @ExtendedFindBy(accessibilityId = "EXTRAS")
    protected ExtendedWebElement extrasTab;
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
    @ExtendedFindBy(accessibilityId = "actors")
    protected ExtendedWebElement actors;
    @ExtendedFindBy(accessibilityId = "duration")
    protected ExtendedWebElement duration;
    @ExtendedFindBy(accessibilityId = "metaDataLabel")
    protected ExtendedWebElement metaDataLabel;
    @ExtendedFindBy(accessibilityId = "downloadButton")
    protected ExtendedWebElement movieDownloadButton;
    @ExtendedFindBy(accessibilityId = "downloadButtonDownloaded")
    protected ExtendedWebElement movieDownloadCompletedButton;
    @ExtendedFindBy(accessibilityId = "watch")
    protected ExtendedWebElement watchButton;
    @ExtendedFindBy(accessibilityId = "VERSIONS")
    protected ExtendedWebElement versionsTab;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name == \"playIcon\"`][1]")
    protected ExtendedWebElement iMaxEnhancedThumbnail;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"contentImageView\"`][1]")
    protected ExtendedWebElement contentImageView;
    @ExtendedFindBy(accessibilityId = "contentImageView")
    protected ExtendedWebElement extrasContentImageView;
    @ExtendedFindBy(accessibilityId = "shareButton")
    private ExtendedWebElement shareBtn;
    @ExtendedFindBy(accessibilityId = "watchlistButton")
    private ExtendedWebElement watchlistButton;
    private final ExtendedWebElement episodesTab = dynamicBtnFindByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_EPISODES.getText()));
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"SUGGESTED\"`][1]")
    private ExtendedWebElement suggestedTab;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]")
    protected ExtendedWebElement tabBar;
    @FindBy(name = "titleLabel_0")
    private ExtendedWebElement firstTitleLabel;
    @ExtendedFindBy(accessibilityId = "titleLabel_%s")
    private ExtendedWebElement episodeTitleLabel;
    @ExtendedFindBy(accessibilityId = "infoInactive24")
    private ExtendedWebElement infoView;
    @FindBy(id = "itemPickerClose")
    private ExtendedWebElement itemPickerClose;
    @FindBy(id = "seasonSelectorButton")
    private ExtendedWebElement seasonSelectorButton;
    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement progressBar;
    @ExtendedFindBy(accessibilityId = "playIcon")
    private ExtendedWebElement playIcon;
    @ExtendedFindBy(accessibilityId = "title")
    private ExtendedWebElement detailsTabTitle;
    @ExtendedFindBy(accessibilityId = "promoLabel")
    private ExtendedWebElement promoLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[3]/XCUIElementTypeImage")
    private ExtendedWebElement handsetNetworkAttributionImage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"Max Width View\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[4]/XCUIElementTypeImage")
    private ExtendedWebElement tabletNetworkAttributionImage;
    @ExtendedFindBy(accessibilityId = "descriptionLabel_0")
    private ExtendedWebElement firstDescriptionLabel;
    @ExtendedFindBy(accessibilityId = "descriptionLabel")
    private ExtendedWebElement descriptionLabel;
    @ExtendedFindBy(accessibilityId = "runtimeLabel_0")
    private ExtendedWebElement firstRunTimeLabel;
    @ExtendedFindBy(accessibilityId = "Stop the offline download for this title.")
    private ExtendedWebElement stopOfflineDownload;
    @ExtendedFindBy(accessibilityId = "titleLabel_9")
    private ExtendedWebElement tenthTitleLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`label == \"copy\"`]")
    private ExtendedWebElement copyShareLink;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[$type='XCUIElementTypeStaticText' AND label CONTAINS 'IMAX Enhanced'$]")
    private ExtendedWebElement imaxEnhancedMediaFeaturesRow;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"selectableTitle\"`]")
    private ExtendedWebElement seasonItemPicker;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"runtime\"`]")
    private ExtendedWebElement durationTimeLabel;
    @ExtendedFindBy(accessibilityId = "contentAdvisory")
    private ExtendedWebElement contentAdvisory;
    @ExtendedFindBy(accessibilityId = "downloadButtonDownloading")
    private ExtendedWebElement downloadStartedButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"SHOP\" OR name == \"PERKS\"`]")
    protected ExtendedWebElement shopOrPerksBtn;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$name='titleLabel_0'$]/**/XCUIElementTypeButton[$name CONTAINS 'Download'$]")
    protected ExtendedWebElement firstEpisodeDownloadButton;
    private final ExtendedWebElement stopOrPauseDownloadButton = getDynamicRowButtonLabel(
            getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                    DictionaryKeys.DOWNLOAD_STOP_DETAILS_PAGE.getText()), 1);
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS 'Season %s Episode %s'`]/" +
            "**/XCUIElementTypeStaticText[`name CONTAINS 'titleLabel'`]")
    private ExtendedWebElement dynamicEpisodeCellTitle;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name='progressBar'$]" +
            "/XCUIElementTypeStaticText[$label CONTAINS 'remaining'$]")
    protected ExtendedWebElement continueWatchingTimeRemaining;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$name='titleLabel_0'$]/**/XCUIElementTypeButton[`name " +
            "CONTAINS 'Offline'`]")
    protected ExtendedWebElement firstEpisodeDownloadComplete;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == 'progressBar'`]/XCUIElementTypeOther")
    private ExtendedWebElement progressBarBookmark;

    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeStaticText[`label =[c] 'Included with your ESPN+ subscription'`]")
    private ExtendedWebElement espnPlusEntitlementAttributionText;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[`name == 'itemPickerView'`]" +
            "/XCUIElementTypeCell[1]")
    private ExtendedWebElement firstItemPickerCell;

    private final ExtendedWebElement pauseDownloadButton = getTypeButtonByLabel(getLocalizationUtils().
            getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.BTN_PAUSE_DOWNLOAD.getText()));
    private final ExtendedWebElement removeDownloadButton = getTypeButtonByLabel(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.REMOVE_DOWNLOAD_BTN.getText()));
    private final ExtendedWebElement downloadPausedLabel = getStaticTextByLabel(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.DOWNLOAD_PAUSED.getText()));
    private final ExtendedWebElement resumeDownloadButton = getTypeButtonByLabel(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.BTN_RESUME_DOWNLOAD.getText()));

    //FUNCTIONS
    public DisneyPlusDetailsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDetailsTab().isPresent();
    }

    public boolean waitForDetailsPageToOpen() {
        LOGGER.info("Waiting for Details page to load");
        return fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Details page was not opened")
                .until(it -> getDetailsTab().isPresent());
    }

    public boolean isDetailPageOpened(long time) {
        dismissNotificationsPopUp();
        return shareBtn.isPresent(time);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPlayButton() {
        getPlayButton().click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public void clickPlayButton(int timeout) {
        fluentWait(getDriver(), timeout, THREE_SEC_TIMEOUT, "Couldn't tap on play button on details page")
                .until(it -> {
                    getPlayButton().click();
                    return getPlayButton().isElementNotPresent(THREE_SEC_TIMEOUT);
                });
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

    public ExtendedWebElement getDownloadStartedButton() {
        return downloadStartedButton;
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
        if (!movieDownloadButton.isElementPresent(TEN_SEC_TIMEOUT)) {
            swipePageTillElementPresent(getDownloadButton(), 5, null, Direction.UP, 1);
            getDownloadButton().click();
        } else {
            movieDownloadButton.click();
        }
    }

    public ExtendedWebElement getMovieDownloadButton() {
        return movieDownloadButton;
    }

    public ExtendedWebElement getStopOrPauseDownloadIcon() {
        return stopOrPauseDownloadButton;
    }

    public ExtendedWebElement getPauseDownloadButton() {
        return pauseDownloadButton;
    }

    public ExtendedWebElement getRemoveDownloadButton() {
        return removeDownloadButton;
    }

    public ExtendedWebElement getResumeDownloadButton() {
        return resumeDownloadButton;
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
                .until(it -> getMovieDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public boolean isSeriesDownloadButtonPresent() {
        return getDownloadButton().isElementPresent();
    }

    public boolean doesContinueButtonExist() {
        return getContinueButton().isPresent();
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

    public ExtendedWebElement getESPNPlusEntitlementAttributionText() {
        return espnPlusEntitlementAttributionText;
    }

    public void waitForWatchlistButtonToAppear() {
        waitForDetailsPageToOpen();
        LOGGER.info("Waiting for  WatchlistButton to appear");
        fluentWait(getDriver(), FORTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Watchlist button is not present")
                .until(it -> watchlistButton.isPresent());
    }

    public void waitForRestartButtonToAppear() {
        LOGGER.info("Waiting for restart button to appear");
        fluentWait(getDriver(), FORTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Restart button is not present")
                .until(it -> getRestartButton().isPresent());
    }

    public void addToWatchlist() {
        watchlistButton.click();
    }

    public void downloadAllOfSeason() {
        if(swipe(downloadSeasonButton)){
            downloadSeasonButton.click();
        } else {
            throw new NoSuchElementException("Download all season button was not found");
        }
    }

    public ExtendedWebElement getDownloadAllSeasonButton() {
        return downloadSeasonButton;
    }

    public void clickSeasonsButton(String season) {
        if (!isSeasonButtonDisplayed(season)) {
            scrollDown();
        }
        String seasonsButton = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, season));
        getDynamicAccessibilityId(seasonsButton).click();
    }

    public void clickExtrasTab() {
        if (!extrasTab.isPresent()) {
            swipePageTillElementTappable(extrasTab, 1, contentDetailsPage, Direction.UP, 900);
        }
        extrasTab.click();
    }

    public boolean isExtrasTabPresent() {
        if (!extrasTab.isPresent(THREE_SEC_TIMEOUT)) {
            swipePageTillElementTappable(extrasTab, 1, contentDetailsPage, IMobileUtils.Direction.UP, 900);
        }
        return extrasTab.isPresent();
    }

    public void tapOnFirstContentTitle() {
        firstTitleLabel.click();
    }

    public List<ExtendedWebElement> getSeasonsFromPicker() {
        String season = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, ""));
        List<ExtendedWebElement> seasons = findExtendedWebElements(getStaticTextByLabelContains(season).getBy());
        LOGGER.info("Seasons: {}", seasons);
        return seasons;
    }

    public boolean isAlertTitleDisplayed() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return getStaticTextByLabel(getMediaTitle()).format().isElementPresent(FIVE_SEC_TIMEOUT);
    }

    public boolean isTwentyDownloadsTextDisplayed() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String twentyDownloadsText = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.DOWNLOADS_SEASON_EPISODES_BATCH.getText()), Map.of("E", Integer.parseInt("20")));
        return getDynamicAccessibilityId(twentyDownloadsText).isElementPresent();
    }

    public boolean isSeasonButtonDisplayed(String season) {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String seasonsButton = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), Map.of(SEASON_NUMBER, season));
        return getDynamicAccessibilityId(seasonsButton).isElementPresent();
    }

    public void clickRemoveFromWatchlistButton() {
        getRemoveFromWatchListButton().click();
        LOGGER.info("'Remove from watchlist' button clicked");
    }

    public ExtendedWebElement getRemoveFromWatchListButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                DictionaryKeys.DETAILS_WATCHLIST_REMOVE_BTN.getText()));
    }

    public void swipeTabBar(Direction direction, int duration) {
        swipeInContainer(tabBar, direction, duration);
    }

    public void clickDetailsTab() {
        if (!getDetailsTab().isPresent(TEN_SEC_TIMEOUT)) {
            swipeInContainer(null, Direction.UP, 1200);
            pause(2); //transition
            swipeTabBar(Direction.LEFT, 1000);
        }
        getDetailsTab().click();
    }

    public String getContentDescriptionText() {
        return contentDescription.getText();
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

    public String getPromoLabelText() {
        return promoLabel.getText();
    }

    public ExtendedWebElement getMetaDataLabel() {
        return metaDataLabel;
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
        return getTrailerButton().isPresent();
    }

    public ExtendedWebElement getTrailerButton() {
        return getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PLAY_TRAILER.getText()));
    }

    public boolean isMovieDownloadButtonDisplayed() {
        return movieDownloadButton.isPresent();
    }

    public boolean doesOneOrMoreSeasonDisplayed() {
        String[] metadataLabelParts = metaDataLabel.getText().split(",");
        String[] seasonsText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_TOTAL_SEASON.getText()).split(" ");
        if (!getParsedString(metaDataLabel, "1", ",").contains("Seasons")) {
            LOGGER.info("verifying single season displayed");
            return metadataLabelParts[3].contains(seasonsText[0]);
        } else {
            LOGGER.info("verifying multi seasons displayed");
            String metaDataLabelSeasons = getParsedString(metaDataLabel, "1", ",");
            String[] seasonParse = metaDataLabelSeasons.split(" ");
            String numberOfSeasons = seasonParse[0];
            String multiSeason = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_TOTAL_SEASONS.getText());
            multiSeason = getLocalizationUtils().formatPlaceholderString(multiSeason, Map.of("number_of_seasons", Integer.parseInt(numberOfSeasons)));
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

    public ExtendedWebElement getExtrasTabTitle() {
        return titleLabel;
    }

    public boolean isExtrasTabTitlePresent() {
        return getExtrasTabTitle().isPresent();
    }

    public String getContentTitle() {
        return logoImage.getText();
    }


    public boolean isExtrasTabTitleDescriptionPresent() {
        return descriptionLabel.isPresent();
    }


    public void swipeTillActorsElementPresent() {
        swipePageTillElementPresent(getActors(), 3, contentDetailsPage, Direction.UP, 500);
    }

    public ExtendedWebElement getDetailsTab() {
        return getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_DETAILS.getText()));
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
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_PLAY.getText()));
    }

    public ExtendedWebElement getContinueButton() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_CONTINUE.getText()));
    }

    public ExtendedWebElement getPlayOrContinueButton() {
        return dynamicBtnFindByLabelOrLabel.format(
                getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_PLAY.getText()),
                getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_CONTINUE.getText())
                );
    }

    public ExtendedWebElement getTrailerActionButton() {
        return dynamicBtnFindByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_TRAILER.getText()));
    }

    public ExtendedWebElement getSeasonSelectorButton() {
        return seasonSelectorButton;
    }

    public ExtendedWebElement getItemPickerClose() {
        return itemPickerClose;
    }

    public ExtendedWebElement getRestartButton() {
        return restartButton;
    }

    public ExtendedWebElement getUpgradeNowButton() {
        return dynamicBtnFindByLabel.format(UPGRADE_NOW);
    }

    public ExtendedWebElement getUnlockButton() {
        return dynamicBtnFindByLabel.format(UNLOCK);
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

    public ExtendedWebElement getEpisodeTitleLabel(int episode) {
        return episodeTitleLabel.format(--episode);
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
        sa.assertTrue(detailsTabTitle.getText().toLowerCase().contains(title[0].toLowerCase()),
                "Suggested title is not the same as details tab title");
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

    public String getDetailsTabTitle() {
        return detailsTabTitle.getText();
    }

    public boolean isRatingPresent() {
        return rating.isPresent();
    }

    public ExtendedWebElement getSuggestedTab() {
        return suggestedTab;
    }

    public ExtendedWebElement getExtrasTab() {
        return extrasTab;
    }

    public boolean isProgressBarPresent() {
        return progressBar.isPresent(TEN_SEC_TIMEOUT);
    }

    public String getDetailsTabSeasonRating() {
        String[] seasonNumberRating = getTypeOtherContainsLabel("Season").getText().split(":");
        String[] seasonNumber = seasonNumberRating[0].split(" ");
        String number = seasonNumber[1];
        return getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
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

    public ExtendedWebElement getShopOrPerksBtn() {
        return shopOrPerksBtn;
    }

    public void clickShopoOrPerksTab() {
        if (!getShopOrPerksBtn().isPresent()) {
            swipeInContainer(null, Direction.UP, 1200);
            pause(2); //transition
            swipeTabBar(Direction.LEFT, 1000);
        }
        getShopOrPerksBtn().click();
    }

    public ExtendedWebElement getEpisodeToDownload(String seasonNumber, String episodeNumber) {
        return getTypeButtonContainsLabel("Download Season " + seasonNumber + " Episode " + episodeNumber);
    }

    public ExtendedWebElement getEpisodeCell(String seasonNumber, String episodeNumber) {
        String desiredSeasonLabel = "Season " + seasonNumber;
        String desiredEpisodeLabel = "Episode " + episodeNumber;
        return typeCellLabelContains.format(String.format("%s %s", desiredSeasonLabel, desiredEpisodeLabel));
    }

    public String getEpisodeCellTitle(String seasonNumber, String episodeNumber) {
        return dynamicEpisodeCellTitle.format(seasonNumber, episodeNumber).getText().split("\\.")[1];
    }

    public ExtendedWebElement getEpisodeToDownload() {
        return dynamicBtnFindByLabelOrLabel.format("Download Season", "Episode");
    }

    /**
     * Use with hulu series content only - to get Hulu series download complete button
     */
    public ExtendedWebElement getHuluSeriesDownloadCompleteButton() {
        return dynamicBtnFindByLabelContains.format("Offline Download Options");
    }

    public ExtendedWebElement getFirstEpisodeDownloadButton() {
        return firstEpisodeDownloadButton;
    }

    public ExtendedWebElement getMovieDownloadCompleteButton() {
        return movieDownloadCompletedButton;
    }

    public void waitForOneEpisodeDownloadToComplete(int timeOut, int polling) {
        LOGGER.info("Waiting for one episode download to complete");
        fluentWait(getDriver(), timeOut, polling, "Download complete text is not present")
                .until(it -> getHuluSeriesDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public void waitForFirstEpisodeToCompleteDownload(int timeOut, int polling) {
        LOGGER.info("Waiting for the download of the first episode to complete.");
        fluentWait(getDriver(), timeOut, polling, "Download complete text is not present")
                .until(it -> getFirstEpisodeDownloadCompleteButton().isPresent());
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public void waitForFirstEpisodeToCompleteDownloadAndShowAsExpired(int timeOut, int polling) {
        LOGGER.info("Waiting for the download of the first episode to start");
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "First episode download did not start")
                .until(it -> stopOrPauseDownloadButton.isPresent(ONE_SEC_TIMEOUT));
        LOGGER.info("Waiting for the download of the first episode to complete and show as expired");
        fluentWait(getDriver(), timeOut, polling, "First episode download expired icon was not present")
                .until(it -> firstEpisodeDownloadButton.isPresent(ONE_SEC_TIMEOUT));
        LOGGER.info(DOWNLOAD_COMPLETED);
    }

    public ExtendedWebElement getHuluContinueButton() {
        return getTypeButtonByName(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_CONTINUE.getText()));
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

    public ExtendedWebElement getCopyShareLink() {
        return copyShareLink;
    }

    public void clickOnCopyShareLink() {
        getCopyShareLink().click();
    }


    public boolean isImaxEnhancedPresentInMediaFeaturesRow() {
        return imaxEnhancedMediaFeaturesRow.getText().contains(IMAX_ENHANCED);
    }

    public boolean isImaxEnhancedPresentsInFormats() {
        return formats.getText().contains(IMAX_ENHANCED);
    }

    public boolean isNegativeStereotypeAdvisoryLabelPresent() {
        String contentAdvisoryText = String.format("%s, %s ",
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DETAILS_CONTENT_ADVISORY_TITLE.getText()),
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DETAILS_NEGATIVE_STEREOTYPE_ADVISORY_FULL.getText()).trim()).replaceAll("\\s+", " ");
        swipePageTillElementPresent(contentAdvisory, 1, contentDetailsPage, Direction.UP, 900);
        return contentAdvisoryText.contains(contentAdvisory.getText().replaceAll("\\s+", " "));
    }

    public ExtendedWebElement getRatingRestrictionDetailMessage() {
        String upcomingBadge = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, RATING_RESTRICTION_DETAIL_MESSAGE.getText());
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
        String[] iMaxEnhancedTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_VERSIONS_IMAX_ENHANCED_TITLE.getText()).split("-");
        swipePageTillElementTappable(firstTitleLabel, 1, contentDetailsPage, Direction.UP, 500);
        return firstTitleLabel.getText().startsWith(iMaxEnhancedTitle[0]);
    }

    public boolean isIMAXEnhancedDescriptionPresentInVersionTab() {
        String iMaxEnhancedDescription = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_VERSIONS_IMAX_ENHANCED_DESCRIPTION.getText());
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

    public boolean isExtrasContentImageViewPresent() {
        return extrasContentImageView.isPresent();
    }

    public ExtendedWebElement getSeasonItemPicker() {
        return seasonItemPicker;
    }

    public ExtendedWebElement getDurationTimeLabel() {
        return durationTimeLabel;
    }

    public boolean isTabSelected(String tabName) {
        ExtendedWebElement tabButton = getDynamicRowButtonLabel(tabName.toUpperCase(), 1);
        if (!tabButton.isPresent()) {
            swipeInContainer(null, Direction.UP, 1200);
            pause(2); //transition
            swipeTabBar(Direction.LEFT, 900);
        }
        return tabButton.getAttribute("value").equals("1");
    }

    public String getRemainingTimeText() {
        return getStaticTextByLabelContains(DETAILS_DURATION_SUFFIX).getText();
    }

    public ExtendedWebElement getContinueWatchingTimeRemaining() {
        return continueWatchingTimeRemaining;
    }

    public boolean isDurationTimeLabelPresent() {
        return getFirstDurationLabel().isPresent(THREE_SEC_TIMEOUT) || getDurationTimeLabel().isPresent(THREE_SEC_TIMEOUT);
    }

    public boolean isSeriesDownloadButtonPresent(String seasonNumber, String episodeNumber) {
        return getDownloadButton().isElementPresent(THREE_SEC_TIMEOUT) || getEpisodeToDownload(seasonNumber, episodeNumber).isPresent(THREE_SEC_TIMEOUT);
    }

    public ExtendedWebElement getEpisodeTitle(String season, String episode) {
        return findExtendedWebElement(AppiumBy.iOSClassChain(String.format("**/XCUIElementTypeStaticText[`label CONTAINS \"Season %s Episode %s\"`]", season, episode)));
    }

    public void isDolbyVisionPresentOrNot(SoftAssert sa) {
        List<String> dolbyVisionDeviceNames = Arrays.asList("iPhone_13_Pro", "iPhone_14", "iPhone_11", "iPhone_11_1", "iPhone_12", "iPhone_11_2", "iPad_Mini_5_Gen");
        List<String> noDolbyVisionDeviceNames = List.of("iPad_8_Gen_1");
        if (dolbyVisionDeviceNames.contains(R.CONFIG.get("capabilities.deviceName"))) {
            LOGGER.info("Validating Dolby Vision is present..");
            sa.assertTrue(getStaticTextByLabelContains(DOLBY_VISION).isPresent(), "`Dolby Vision` video quality is not found.");
        } else if (noDolbyVisionDeviceNames.contains(R.CONFIG.get("capabilities.deviceName"))) {
            LOGGER.info("Validating Dolby Vision is not present..");
            sa.assertFalse(getStaticTextByLabelContains(DOLBY_VISION).isPresent(), "`Dolby Vision` video quality is not found.");
        }
    }

    public boolean isShopPromoLabelHeaderPresent() {
        return getStaticTextByLabel(SHOP_PROMO_LABEL_HEADER).isPresent();
    }

    public boolean isShopPromoLabelSubHeaderPresent() {
        return getStaticTextByLabel(SHOP_PROMO_LABEL_SUBHEADER).isPresent();
    }

    public boolean isContentAvailableWithHuluSubscriptionPresent(DisneyAccount account, String environment, String platform, String seriesId) throws URISyntaxException, JsonProcessingException {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder().platform(platform)
                .environment(environment).build();
        ExploreApi exploreApi = new ExploreApi(apiConfiguration);
        ExploreSearchRequest searchRequest = ExploreSearchRequest.builder().entityId(seriesId)
                .profileId(account.getProfileId()).build();
        ExplorePageResponse pageResponse = exploreApi.getPage(searchRequest);
        String huluSubscriptionErrorMessage = pageResponse.getData().getPage().getVisuals().getRestriction().getMessage();
        return getStaticTextByLabel(huluSubscriptionErrorMessage).isPresent();
    }


    /**
     * To be used with continually navigating back and forth between details and player of same content.
     */
    public DisneyPlusVideoPlayerIOSPageBase clickPlayOrContinue() {
        if (getPlayButton().isPresent()) {
            clickPlayButton();
        } else {
            clickContinueButton();
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public void verifyRatingsInDetailsFeaturedArea(String rating, SoftAssert sa) {
        LOGGER.info("Verifying Ratings in featured area");
        Assert.assertTrue(isDetailPageOpened(FIVE_SEC_TIMEOUT), "Details screen not displayed.");
        sa.assertTrue(isRatingPresent(rating), rating + " Rating was not found on details page featured area.");
    }

    public void validateRatingsInDetailsTab(String rating, SoftAssert sa) {
        LOGGER.info("Verifying Ratings in details tab");
        clickDetailsTab();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(getTypeOtherByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DETAILS_RATING.getText())), 2);
        }
        waitForPresenceOfAnElement(getTypeOtherContainsLabel(rating));
        sa.assertTrue(getTypeOtherContainsLabel(rating).isPresent(), rating + " Rating was not found on details tab area");
    }

    public String getSeasonRating() {
        return getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DETAILS_SEASON_RATING.getText()), Map.of("season_number", Integer.parseInt(getSeasonSelector())));
    }
    public boolean isSeasonRatingPresent() {
        String[] seasonRatingSplit = getSeasonRating().split(" ");
        String expectedLastWord = convertToTitleCase(seasonRatingSplit[2], " ");
        return getStaticTextByLabelContains(String.format("%s %s %s %s", seasonRatingSplit[0],
                seasonRatingSplit[1], expectedLastWord, seasonRatingSplit[3])).isPresent();
    }

    //Download Modal Elements
    public ExtendedWebElement getDownloadModalPlayButton() {
        String play = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_PLAY_ALERT.getText());
        return dynamicBtnFindByLabel.format(play);
    }

    public boolean isDownloadModalPlayButtonDisplayed() {
        return getDownloadModalPlayButton().isPresent();
    }

    public boolean isDownloadModalRenewButtonDisplayed() {
        String renew = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_RENEW_DOWNLOAD.getText());
        return dynamicBtnFindByLabel.format(renew).isPresent();
    }

    public boolean isDownloadModalRemoveButtonDisplayed() {
        String remove = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                BTN_REMOVE_DOWNLOAD.getText());
        return dynamicBtnFindByLabel.format(remove).isPresent();
    }

    public boolean isStopOrPauseDownloadIconDisplayed() {
        return fluentWait(getDriver(), TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Download not started")
                .until(it -> stopOrPauseDownloadButton.isPresent());
    }

    public void clickStopOrPauseDownload() {
        stopOrPauseDownloadButton.click();
    }

    public boolean isDownloadSeasonButtonDisplayed(String seasonNumber) {
        return getTypeButtonByLabel(getLocalizationUtils().formatPlaceholderString(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DOWNLOAD_SEASON_NUMBER_BTN.getText()), Map.of("seasonNumber",
                        Integer.parseInt(seasonNumber)))).isPresent();
    }

    public boolean isDownloadInProgressStatusDisplayed() {
        String downLoadInProgress = getLocalizationUtils().getValuesBetweenPlaceholders(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DOWNLOAD_IN_PROGRESS_PERCENT.getText())).get(0);
        return getStaticTextByLabelContains(downLoadInProgress).isPresent();
    }

    public boolean isPauseDownloadButtonDisplayed() {
        int count = 5;
        while (!pauseDownloadButton.isPresent(THREE_SEC_TIMEOUT) && count >= 0) {
            clickAlertDismissBtn();
            clickStopOrPauseDownload();
            count--;
        }
        return pauseDownloadButton.isPresent(ONE_SEC_TIMEOUT);
    }

    public boolean isMoviePauseDownloadButtonDisplayed() {
        int count = 5;
        while (!viewAlert.isPresent(THREE_SEC_TIMEOUT) && count >= 0) {
            downloadStartedButton.click();
            count--;
        }
        count = 5;
        while (!pauseDownloadButton.isPresent(THREE_SEC_TIMEOUT) && count >= 0) {
            clickAlertDismissBtn();
            downloadStartedButton.click();
            count--;
        }
        return pauseDownloadButton.isPresent(ONE_SEC_TIMEOUT);
    }

    public boolean isRemoveDownloadButtonDisplayed() {
        return removeDownloadButton.isPresent();
    }

    public ExtendedWebElement getEpisodeTitleFromEpisodsTab(String season, String episodeTitle) {
        return getStaticTextByLabel(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                SERIES_EPISODE_TITLE.getText()),
                Map.of("episodeNumber", Integer.parseInt(season), "title", episodeTitle)));
    }

    public int getRemainingTimeInSeconds(String duration) {
        int hours;
        int minutes;
        String[] time = duration
                .split(DETAILS_DURATION_SUFFIX)[0]
                .trim()
                .split(" ");

        if (time.length == 2) {
            hours = Integer.parseInt(time[0].split("h")[0]);
            minutes = Integer.parseInt(time[1].split("m")[0]);
            return hours * 3600 + minutes * 60;
        } else {
            minutes = Integer.parseInt(time[0].split("m")[0]);
            return minutes * 60;
        }
    }

    public boolean isOnlyAvailableWithHuluHeaderPresent() {
        String dictValue = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                IPS_MESSAGING_ONLY_EXPERIENCE_SCREEN_HEADER.getText());
        return getStaticTextByLabel(dictValue).isPresent();
    }

    public boolean isIneligibleScreenBodyPresent() {
        String dictValue = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                IPS_BODY_INELIGIBLE_SCREEN_DISNEY_PLUS.getText());
        return getStaticTextByLabel(dictValue).isPresent();
    }

    public ExtendedWebElement getCtaIneligibleScreen() {
        String dictValue = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                IPS_CTAL_INELIGIBLE_SCREEN_DISNEY_PLUS.getText());
        return getTypeButtonByLabel(dictValue);
    }

    public ExtendedWebElement getFormatDetailsText() {
        return getTypeOtherContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DETAIL_FORMATS.getText()));
    }

    public ExtendedWebElement getDolbyBadge() {
        return getTypeOtherContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, MEDIA_FEATURE_DOLBY_VISION.getText()));
    }

    public ExtendedWebElement getUHDBadge() {
        return getTypeOtherContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,MEDIA_FORMAT_UHD.getText()));
    }

    public ExtendedWebElement getHDRBadge() {
        return getTypeOtherContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, MEDIA_FEATURE_HDR_10.getText()));
    }

    public boolean isDownloadPausedInDownloadModal() {
        return downloadPausedLabel.isPresent();
    }

    public ExtendedWebElement getFirstEpisodeDownloadCompleteButton() {
        return firstEpisodeDownloadComplete;
    }

    public void waitForBookmarkToRefresh(double scrubPercentage, int latency) {
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT,
                "Bookmark is not indicating correct position on details page")
                .until(it -> isBookmarkRefreshed(scrubPercentage, latency));
    }

    public boolean isBookmarkRefreshed(double scrubPercentage, int latency) {
        double bookmarkWidth = progressBarBookmark.getSize().getWidth();
        double expectedWidth = progressBar.getSize().getWidth() / (100 / scrubPercentage);
        ValueRange range = ValueRange.of(-latency, latency);
        return range.isValidIntValue((long) (expectedWidth - bookmarkWidth));
    }

    public ExtendedWebElement getTabBar() {
        return tabBar;
    }

    public boolean isSeasonPickerPresent() {
        return seasonItemPicker.isPresent(THREE_SEC_TIMEOUT);
    }

    public void tapOutsideOfSeasonPickerList() {
        int xPoint = firstItemPickerCell.getLocation().getX();
        int yPoint = firstItemPickerCell.getLocation().getY();
        tapAtCoordinateNoOfTimes(xPoint, yPoint - 10, 1);
    }

    public boolean isOnlyAvailableWithESPNHeaderPresent() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                IPS_MESSAGING_ONLY_EXPERIENCE_SCREEN_HEADER_TRIO.getText()))
                .isPresent();
    }
 }
