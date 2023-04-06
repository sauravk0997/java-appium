package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDetailsIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    @ExtendedFindBy(accessibilityId = "shareButton")
    private ExtendedWebElement shareBtn;

    @ExtendedFindBy(accessibilityId = "play")
    protected ExtendedWebElement playButton;

    @ExtendedFindBy(accessibilityId = "bookmarked")
    private ExtendedWebElement continueButton;

    @ExtendedFindBy(accessibilityId = "watchlistButton")
    private ExtendedWebElement watchlistButton;

    @ExtendedFindBy(accessibilityId = "groupwatchButton")
    private ExtendedWebElement groupWatchBtn;

    @ExtendedFindBy(accessibilityId = "logoImage")
    protected ExtendedWebElement logoImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'EXTRAS'`][1]")
    private ExtendedWebElement extrasButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Want to stay in the loop?\"`]")
    private ExtendedWebElement notificationPopUp;

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement titleLabel;

    @ExtendedFindBy(accessibilityId = "downloadSeasonButton")
    protected ExtendedWebElement downloadSeasonButton;

    protected ExtendedWebElement detailsTab = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_DETAILS.getText()));

    private ExtendedWebElement episodesTab = dynamicBtnFindByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_EPISODES.getText()));

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"Max Width View\"]/XCUIElementTypeCollectionView/XCUIElementTypeCell[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]")
    protected ExtendedWebElement tabBar;

    @FindBy(name = "titleLabel_0")
    private ExtendedWebElement firstEpisodeTitleLabel;

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

    private ExtendedWebElement downloadBtn = dynamicBtnFindByLabel.format("downloadEpisodeList");
    private ExtendedWebElement downloadCompleteButton = dynamicBtnFindByLabelContains.format("downloadComplete");

    //FUNCTIONS

    public DisneyPlusDetailsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return shareBtn.isElementPresent();
    }

    public boolean isOpened(long time) {
        if (notificationPopUp.isPresent()) {
            dismissNotificationsPopUp();
        }
        return shareBtn.isElementPresent(time);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPlayButton() {
        getTypeButtonByName("play").click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickWatchButton() {
        getTypeButtonByName("watch").click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickContinueButton() {
        getTypeButtonByName("bookmarked").click();
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusHomeIOSPageBase clickCloseButton() {
        backButton.click();
        return initPage(DisneyPlusHomeIOSPageBase.class);
    }

    //TODO Should discuss setting up Series vs. Movies page factories due to differences like this
    public void startDownload() {
        if (!movieDownloadButton.isElementPresent(DELAY)) {
            new MobileUtilsExtended().swipe(downloadBtn);
            downloadBtn.click();
        } else {
            movieDownloadButton.click();
        }
    }

    public void pauseDownload() {
        if (!movieDownloadButton.isElementPresent(DELAY)) {
            new MobileUtilsExtended().swipe(downloadBtn);
            downloadBtn.click();
        } else {
            movieDownloadButton.click();
        }
    }

    public void waitForDownloadToComplete() {
        LOGGER.info("Waiting for download to complete");
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Download complete text is not present")
                .until(it -> downloadCompleteButton.isPresent());
        LOGGER.info("Download completed");
    }

    public boolean isDownloadPaused(DisneyLocalizationUtils dictionary) {
        return getStaticTextByLabel(dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_PAUSED.getText())).isElementPresent();
    }

    public void removeDownload(DisneyLocalizationUtils dictionary) {
        getDynamicXpathContainsName(dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.REMOVE_DOWNLOAD_BTN.getText())).click();
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
     * when the iOS dev team adds static accesiibility IDs to the elements on the page.
     * @return - Media title
     */
    public String getMediaTitle() {
        return titleImage.getText();
    }

    public String getEpisodeContentTitle() {
        //We want to remove the list numbering and duration from the episode's title label
        LOGGER.info("getting first episode title from Details page");
        return firstEpisodeTitleLabel.getAttribute("value")
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
            new IOSUtils().scrollDown();
        }
        String seasonsButton = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), season);
        getDynamicAccessibilityId(seasonsButton).click();
    }
    public void clickExtrasButton() {
        if (!extrasButton.isElementPresent()) {
            new IOSUtils().swipePageTillElementTappable(extrasButton, 1, contentDetailsPage, IMobileUtils.Direction.UP, 900);
        }
        extrasButton.click();
    }

    public void tapOnFirstContentTitle() {
        firstEpisodeTitleLabel.click();
    }

    public List<ExtendedWebElement> getSeasonsFromPicker() {
        String season = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), "");
        List<ExtendedWebElement> seasons = findExtendedWebElements(getStaticTextByLabelContains(season).getBy());
        LOGGER.info("Seasons: {}", seasons);
        return seasons;
    }

    public boolean isAlertTitleDisplayed() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return getStaticTextByLabel(getMediaTitle()).format().isElementPresent();
    }

    public boolean isTwentyDownloadsTextDisplayed() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        String twentyDownloadsText = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.DOWNLOADS_SEASON_EPISODES_BATCH.getText()), Map.of("E", Integer.parseInt("20")));
        return getDynamicAccessibilityId(twentyDownloadsText).isElementPresent();
    }

    public boolean isSeasonButtonDisplayed(String season) {
        UniversalUtils.captureAndUpload(getCastedDriver());
        String seasonsButton = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), season);
        return getDynamicAccessibilityId(seasonsButton).isElementPresent();
    }

    public void clickRemoveFromWatchlistButton() {
        String watchlistRemoveLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DETAILS_WATCHLIST_REMOVE_BTN.getText());
        getTypeButtonByLabel(watchlistRemoveLabel).format().click();
        LOGGER.info("'Remove from watchlist' button clicked");
    }


    public void swipeTabBar (IMobileUtils.Direction direction, int duration) {
        new IOSUtils().swipeInContainer(tabBar, direction, duration);
    }

    public void clickDetailsTab() {
        if (!detailsTab.isElementPresent()) {
            swipeTabBar(IMobileUtils.Direction.LEFT, 1000);
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
        return getPlayButton().isElementPresent();
    }

    public boolean isGroupWatchButtonDisplayed() {
        return groupWatchBtn.isElementPresent();
    }

    public boolean isWatchlistButtonDisplayed() {
        return watchlistButton.isElementPresent();
    }

    public void clickWatchlistButton() { watchlistButton.click(); }

    public String getWatchlistButtonText() {
        return watchlistButton.getText();
    }

    public boolean isTrailerButtonDisplayed() {
        return trailerButton.isElementPresent();
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
        new IOSUtils().swipePageTillElementPresent(releaseDate, 3, contentDetailsPage, IMobileUtils.Direction.UP, 500);
        String[] detailsTabYear = releaseDate.getText().split(", ");
        return params.get("metaDataYear(s)").contains(detailsTabYear[1]);
    }

    public boolean compareEpisodeNum() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        new IOSUtils().swipePageTillElementPresent( getDynamicXpathContainsName(titleLabel.toString()), 1, contentDetailsPage, IMobileUtils.Direction.UP, 2000);
        LOGGER.info("Retrieving current episode number..");
        String currentEpisodeNum = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        new IOSUtils().swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()), 1,  contentDetailsPage, IMobileUtils.Direction.DOWN, 2000);
        clickWatchButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.waitForContentToEnd(15);
        if (videoPlayer.isOpened()) {
            videoPlayer.clickBackButton();
        }
        isOpened();
        new IOSUtils().swipePageTillElementPresent(getDynamicXpathContainsName(titleLabel.toString()),1, contentDetailsPage, IMobileUtils.Direction.UP, 2000);
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
        new IOSUtils().pressByElement(infoView, 1);
    }

    public void swipeTillActorsElementPresent() {
        new IOSUtils().swipePageTillElementPresent(getActors(), 3, contentDetailsPage, IMobileUtils.Direction.UP, 500);
    }

    public ExtendedWebElement getDetailsTab() {
        return detailsTab;
    }

    public ExtendedWebElement getActors() {
        return actors;
    }

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
}