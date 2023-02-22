package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.client.requests.content.SearchPageRequest;
import com.disney.qa.api.client.responses.content.ContentMovie;
import com.disney.qa.api.client.responses.content.ContentSearch;
import com.disney.qa.api.client.responses.content.ContentSeries;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMediaPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "contentDetailRecyclerView")
    private ExtendedWebElement mediaBackgroundContainerV1;

    @FindBy(id = "detailRecyclerView")
    private ExtendedWebElement mediaBackgroundContainerV2;

    @FindBy(id = "contentDetailTitleImage")
    private ExtendedWebElement mediaDetailImageV1;

    @FindBy(id = "detailLogoImage")
    private ExtendedWebElement mediaDetailImageV2;

    @FindBy(id = "downloadButton")
    private ExtendedWebElement mediaDownloadButtonV1;

    @FindBy(id = "detailDownloadLayout")
    private ExtendedWebElement mediaDownloadButtonV2;

    @FindBy(id = "detailEpisodeDownloadStatus")
    private ExtendedWebElement episodeDownloadButton;

    @FindBy(id = "detailSeasonDownloadText")
    private ExtendedWebElement seasonDownloadButton;

    @FindBy(id = "downloadAction")
    private ExtendedWebElement seasonDownloadAction;

    @FindBy(id = "mainTabContent")
    private ExtendedWebElement mediaPageContainer;

    @FindBy(id = "watchlistButton")
    private ExtendedWebElement watchlistButtonV1;

    @FindBy(id = "detailWatchlistLayout")
    private ExtendedWebElement watchlistButtonV2;

    @FindBy(id = "title")
    private ExtendedWebElement contentPageTitle;

    @FindBy(id = "videoDetailDescription")
    private ExtendedWebElement mediaDetails;

    @FindBy(id = "startPlayerButton")
    private ExtendedWebElement mediaPlayButton;

    @FindBy(id = "message")
    private ExtendedWebElement popupMessage;

    @FindBy(id = "playTrailerLabel")
    private ExtendedWebElement trailerButton;

    @FindBy(xpath = "//*[contains(@text, \"%s\")]/preceding-sibling::*[@resource-id='com.disney.disneyplus:id/playIcon']")
    private ExtendedWebElement extrasAssetItem;

    @FindBy(xpath = "//*[@text='Rating:']/following-sibling::*")
    private ExtendedWebElement contentRatingV1;

    @FindBy(xpath = "//*[contains(@text, 'Rating:')]")
    private ExtendedWebElement contentRatingV2;

    @FindBy(id = "detailDetailsRatingTitle")
    private ExtendedWebElement detailDetailsRatingTitle;

    @FindBy(id = "metadataTextView")
    private ExtendedWebElement metadataTextViewV1;

    @FindBy(id = "detailMetadataRoot")
    private ExtendedWebElement metadataTextViewV2;

    @FindBy(id = "actionText")
    private ExtendedWebElement actionText;

    @FindBy(id = "eaLogo")
    private ExtendedWebElement eaLogo;

    @FindBy(id = "detailSeasonSelector")
    private ExtendedWebElement seasonSelector;

    @FindBy(id = "standardButtonContainer")
    private ExtendedWebElement standardButtonContainer;

    @FindBy(xpath = "//*[@content-desc='CONTINUE']")
    private ExtendedWebElement continuePlayButton;

    @FindBy(id = "tabLayoutRecyclerView")
    private ExtendedWebElement tabLayoutView;

    @FindBy(id = "dialogLayout")
    private ExtendedWebElement dialogLayout;

    @FindBy(xpath = "//android.widget.ImageView[contains(@content-desc, \"%s\")]")
    private ExtendedWebElement selectedEpisodeDownloadButton;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc, \"%s\")]")
    private ExtendedWebElement detailEpisodeRoot;

    @FindBy(id = "emptyStateTvTitle")
    private ExtendedWebElement emptyDownloadContainerMessageStub;

    private boolean isEpisode;

    private boolean groupwatchDismissed = false;

    private final String detailsVersion = setDisplayVersion();

    public DisneyPlusMediaPageBase(WebDriver driver){
        super(driver);
    }

    private boolean isDetailsV2() {
        return detailsVersion.equals("V2");
    }

    /**
     * Use custom_string5 to set the display version if utilizing the override in Jarvis.
     * Otherwise, the page will parse the app config if V2 is enabled or not.
     * Is called on page builder to automatically handle element setting.
     *
     * @return - Internal override value or returned parse from app config
     */
    public String setDisplayVersion() {
        String forceVersion = R.CONFIG.get("custom_string5");
        if(forceVersion.equals("NULL")) {
           return new DisneyContentApiChecker().getDetailsViewVersion();
        } else {
            return forceVersion;
        }
    }


    @Override
    public boolean isOpened(){
        return getBackgroundContainer().isElementPresent();
    }

    public void dismissPopup(){
        if(!groupwatchDismissed) {
            getMediaDetailImage().click();
            groupwatchDismissed = true;
        }
    }

    public ExtendedWebElement getBackgroundContainer() {
        if(isDetailsV2()) {
            return mediaBackgroundContainerV2;
        } else {
            return mediaBackgroundContainerV1;
        }
    }

    public ExtendedWebElement getPlayButton() {
        if(isDetailsV2()) {
            return standardButtonContainer;
        } else {
            return mediaPlayButton;
        }
    }

    public ExtendedWebElement getMediaDetailImage() {
        if(isDetailsV2()) {
            return mediaDetailImageV2;
        } else {
            return mediaDetailImageV1;
        }
    }

    public ExtendedWebElement getMediaDownloadButton() {
        if(isDetailsV2()) {
            return mediaDownloadButtonV2;
        } else {
            return mediaDownloadButtonV1;
        }
    }

    public ExtendedWebElement getContentRating() {
        if(isDetailsV2()) {
            return detailDetailsRatingTitle;
        } else {
            return contentRatingV1;
        }
    }

    public ExtendedWebElement getWatchlistButton() {
        if(isDetailsV2()) {
            return watchlistButtonV2;
        } else {
            return watchlistButtonV1;
        }
    }

    /**
     * Sets a boolean value dictating if the media downloaded is a series episode or a movie.
     * This is due to accessing the media dynamically on the downloads page of the app if
     * the test being run isn't design specifically to use one or the other.
     */
    public void beginDownload(){
        dismissPopup();
        ExtendedWebElement mediaDownloadButton = getMediaDownloadButton();
        if (mediaDownloadButton.isElementPresent()) {
            mediaDownloadButton.click();
            isEpisode = false;
        } else {
            new AndroidUtilsExtended().swipe(episodeDownloadButton);
            episodeDownloadButton.click();
            isEpisode = true;
        }
    }

    /**
     * Downloads the specified episode of a series.
     * @param downloadButtonContentDescription Content description of the download button of the episode.
     * Ex: Download season 1, episode 2, Escape To / From Atlantis!
     * @param season localized season.
     */
    public void downloadEpisodeByContentDescription(String downloadButtonContentDescription, String season){
        selectSeason(season);
        swipeToContentDesc(downloadButtonContentDescription);
        selectedEpisodeDownloadButton
                .format(downloadButtonContentDescription)
                .click();
    }

    public void playEpisodeByContentDescription(String contentDescription, String season){
        selectSeason(season);
        swipeToContentDesc(contentDescription);
        detailEpisodeRoot.format(contentDescription).click();
    }

    public void downloadSeason() {
        dismissPopup();
        new AndroidUtilsExtended().swipe(seasonDownloadButton);
        seasonDownloadButton.click();
        waitUntil(ExpectedConditions.visibilityOfElementLocated(seasonDownloadAction.getBy()), EXPLICIT_TIMEOUT);
        seasonDownloadAction.click();
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(seasonDownloadAction.getBy()), EXPLICIT_TIMEOUT);
    }

    public boolean isDownloadAnEpisode(){
        return isEpisode;
    }

    public void pauseDownload(String pauseText){
        int tries = 0;
        while(tries < 3) {
            try {
                beginDownload();
                String topAction = actionText.getText();
                if (topAction.equals(pauseText)) {
                    actionText.click();
                } else {
                    new AndroidUtilsExtended().pressBack();
                }
                break;
            } catch (NoSuchElementException e){
                LOGGER.info("Click did not bring up downloads submenu. Action may have been too early. Trying again...");
                tries++;
            }
        }
    }

    public String getDownloadEpisodeTitle(){
        String[] downloadTitle = episodeDownloadButton.getAttribute(CONTENT_DESC).split(", ");
        return downloadTitle[downloadTitle.length-1];
    }

    public boolean doesToggleGraphicUpdate(){
        dismissPopup();
        UniversalUtils util = new UniversalUtils();
        BufferedImage beforeClick = util.getElementImage(getWatchlistButton());
        getWatchlistButton().click();
        pause(1);
        BufferedImage afterClick = util.getElementImage(getWatchlistButton());
        return UniversalUtils.areImagesDifferent(beforeClick, afterClick);
    }

    public void addToWatchlist(){
        LOGGER.info("Adding media to Watchlist");
        swipeToWatchlistButton();
        getWatchlistButton().click();
    }

    public void swipeToWatchlistButton() {
        new AndroidUtilsExtended().swipe(getWatchlistButton());
    }

    public boolean isMediaOnWatchlist(String contentDesc){
        return (getWatchlistButton().getAttribute(CONTENT_DESC).equals(contentDesc));
    }

    public BufferedImage getTitleImage(){
        return new MobileUtilsExtended().getElementImage(getMediaDetailImage());
    }

    public boolean isContentTitleVisible(String title){
        waitUntil(ExpectedConditions.visibilityOfElementLocated(getMediaDetailImage().getBy()), 30);
        return getMediaDetailImage().getAttribute(CONTENT_DESC).equals(title);
    }

    public boolean isMediaPageTabSelected(String tab) {
        return genericTextElementExact.format(tab).getAttribute("selected").equals("true");
    }

    public void startPlayback() {
        dismissPopup();
        getPlayButton().click();
    }

    public void clickContinuePlayButton() { standardButtonContainer.click(); }

    public void playDesiredMedia(String media){
        dismissPopup();
        swipeToItem(media);
        genericTextElement.format(media).click();
    }

    //used for playing the designated item in a media page's Extras tab
    public void playMediaTrailer(String mediaItem){
        swipeToItem("EXTRAS");
        genericTextElementExact.format("EXTRAS").click();
        swipeToItem(mediaItem);
        extrasAssetItem.format(mediaItem).click();
    }

    //Used for playing the trailer for media that is unreleased or premier access only
    public void playUnreleasedMediaTrailer(){
        swipeToItem("TRAILER");
        genericTextElementExact.format("TRAILER").click();
    }

    public String getMediaTitle(){
        waitUntil(ExpectedConditions.visibilityOfElementLocated(getMediaDetailImage().getBy()), EXPLICIT_TIMEOUT);
        return getMediaDetailImage().getAttribute(CONTENT_DESC);
    }

    public boolean waitUntilMediaPageLoads(){
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(getMediaDetailImage().getBy()), EXPLICIT_TIMEOUT);
    }

    public boolean isWatchlistAvailable(){
        return getWatchlistButton().isElementPresent();
    }

    public String getMetadataRating() {
        String ratedText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.RATED.getText());
        if (isDetailsV2()) {
            String[] metadata = metadataTextViewV2.getAttribute(CONTENT_DESC).split(" ");
            for (int i = 0; i < metadata.length; i++) {
                if (metadata[i].equalsIgnoreCase(ratedText)) {
                    return StringUtils.substringBefore(metadata[i + 1], ".");
                }
            }
        } else {
            return metadataTextViewV1.getText().split(" ")[0];
        }
        return null;
    }

    public String getMediaDetailsRating(String localizedDetailsTab) {
        dismissPopup();
        MobileUtilsExtended mobileUtils = new MobileUtilsExtended();
        mobileUtils.swipe(tabLayoutView, 3);
        mobileUtils.swipeUp(1, 1000);
        if(!genericTextElementExact.format(localizedDetailsTab).isElementPresent(1)) {
            mobileUtils.swipeInContainer(tabLayoutView, IMobileUtils.Direction.LEFT, 1000);
        }
        genericTextElementExact.format(localizedDetailsTab).click();
        mobileUtils.swipe(getContentRating());
        if(isDetailsV2()) {
            String contentDesc = detailDetailsRatingTitle.getAttribute(CONTENT_DESC);
            String rating = StringUtils.substringAfter(contentDesc, ": ");
            return rating.split(",")[0].trim();
        } else {
            return contentRatingV1.getText().split(" ")[0].trim();
        }
    }

    public boolean isPremierAccessLogoVisible() {
        return eaLogo.isElementPresent();
    }

    public void selectSeason(String season) {
        new MobileUtilsExtended().swipe(seasonSelector);
        seasonSelector.click();
        genericTextElementExact.format(season).click();
    }

    public void isDownloadDeviceLimitDialogVisible() {
        if (dialogLayout.isVisible(ONE_SEC_TIMEOUT)) {
            Assert.fail("Device download limit has been reached");
        }
    }

    /**
     * Attempts to download entire season of episodes from a series if the season has 20+ episodes
     * Any attempt of download over 100+ may produce a storage dialog error
     *
     * @param downloadInSetsOfTwenty      - Any value in the increments of 20
     * @param verifyBadgeIncrementalValue - Flag if we want to assert download badge and value
     */
    public void downloadSeriesSeasonsOverTwentyEpisodes(
            int downloadInSetsOfTwenty,
            boolean verifyBadgeIncrementalValue,
            String seriesName,
            SoftAssert sa,
            DisneyLocalizationUtils disneyLanguageUtils,
            DisneySearchApi disneySearchApi,
            DisneyAccount disneyAccount,
            DisneyPlusContentMetadataPageBase contentMetadataPageBase) {
        ContentSeries series = contentMetadataPageBase.getSeriesObject(seriesName, disneyAccount, disneySearchApi, disneyLanguageUtils);
        List<JsonNode> metaNodes = series.getJsonNode().findValues("episodes_meta");

        int totalDownloads = 0;
        for (int i = 0; i < series.getNumberOfSeasons(); i++) {
            if (Integer.parseInt(metaNodes.get(i).findValue("hits").asText()) > 20) {
                totalDownloads += 20;
                downloadInSetsOfTwenty -= 20;
                selectSeason(disneyLanguageUtils.replaceValuePlaceholders(disneyLanguageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), String.valueOf(i + 1)));
                downloadSeason();
                isDownloadDeviceLimitDialogVisible();

                if (verifyBadgeIncrementalValue) { verifyDownloadBadge(totalDownloads, sa); }
                PAGEFACTORY_LOGGER.info("Total episodes in download: {}", totalDownloads);
            } else {
                PAGEFACTORY_LOGGER.info("Season does not have 20 episodes. Skipping season.");
                continue;
            }

            if (downloadInSetsOfTwenty == 0) { return; }
        }
        if (downloadInSetsOfTwenty != 0) { Assert.fail("Error downloading " + downloadInSetsOfTwenty); }
    }

    /**
     * Verify badge icon and download value
     *
     * @param totalDownloads - If total value exceeds 99 then generically 99+ badge is expected
     */
    public void verifyDownloadBadge(int totalDownloads, SoftAssert sa) {
        Assert.assertTrue(isDownloadBadgePresent(),
                "Downloads badge was not displayed.");
        if (totalDownloads <= 99) {
            sa.assertEquals(getDownloadBadgeValue(), String.valueOf(totalDownloads),
                    "Downloads Badge value was not correct.");
        } else {
            sa.assertEquals(getDownloadBadgeValue(), "99+",
                    "Downloads Badge value was not correct.");
        }
    }

    public boolean isNinetyNineDownloadBadgeVisible() {
        LOGGER.info("Attempting to wait for 99 download badge to be displayed");
        return (fluentWait(getDriver(), EXTRA_LONG_TIMEOUT, SHORT_TIMEOUT, "99 download badge is not visible").until(it -> downloadBadge.getText().equals("99")));
    }

    public ContentSeries getSeriesObject(String mediaTitle, DisneySearchApi disneySearchApi, DisneyAccount disneyAccount, DisneyLocalizationUtils disneyLanguageUtils) {
        SearchPageRequest searchPageRequest = SearchPageRequest.builder()
                .region(disneyAccount.getCountryCode())
                .language(disneyAccount.getProfileLang())
                .account(disneyAccount)
                .query(mediaTitle)
                .build();

        ContentSearch node = disneySearchApi.getContentSearchPageResults(searchPageRequest);
        String encodedSeriesId = null;
        List<String> titles = node.getContentTitlesFull();
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).equals(mediaTitle)) {
                encodedSeriesId = node.getEncodedSeriesId(i);
                break;
            }
        }
        return disneySearchApi.getSeries(encodedSeriesId, disneyLanguageUtils.getLocale(), disneyLanguageUtils.getUserLanguage());
    }

    /**
     * Client tab naming convention may differ from response
     * EPISODES -> Episodes
     * EXTRAS -> Details
     * RELATED -> Suggested
     * VIDEOS -> Extras
     */
    public enum DetailTabs {
        EPISODES("episodes"),
        EXTRAS("extras"),
        RELATED("related"),
        VIDEOS("videos");

        private final String tab;

        DetailTabs(String tab) {
            this.tab = tab;
        }

        public String getTab() {
            return tab;
        }
    }

    public List<String> availableMovieTabsFromResponse(
            String mediaTitle,
            DisneyAccount disneyAccount,
            DisneySearchApi searchApi) {
        ContentMovie movie = searchApi.getMovie(mediaTitle, disneyAccount);
        ArrayList<String> availableTabsList = new ArrayList<>();

        for (DetailTabs tab : DetailTabs.class.getEnumConstants()) {
            JsonNode tabJsonObject = movie.getJsonNode().findValue(tab.getTab());
            switch (tab) {
                case EXTRAS:
                case RELATED:
                    if (tabJsonObject == null) {
                        break;
                    }
                    availableTabsList.add(tab.getTab());
                    break;
                case VIDEOS:
                    if (tabJsonObject.isEmpty()) {
                        break;
                    }
                    availableTabsList.add(tab.getTab());
                    break;
                default:
                    break;
            }
        }
        return availableTabsList;
    }

    public List<String> availableSeriesTabsFromResponse(
            String mediaTitle,
            DisneyAccount disneyAccount,
            DisneySearchApi searchApi,
            DisneyLocalizationUtils localizationUtils) {
        DisneyPlusContentMetadataPageBase contentMetadataPageBase = initPage(DisneyPlusContentMetadataPageBase.class);
        ContentSeries series = contentMetadataPageBase.getSeriesObject(mediaTitle, disneyAccount, searchApi, localizationUtils);
        ArrayList<String> availableTabsList = new ArrayList<>();

        for (DetailTabs tab : DetailTabs.class.getEnumConstants()) {
            JsonNode tabJsonObject = series.getJsonNode().findValue(tab.getTab());
            switch (tab) {
                case EPISODES:
                case EXTRAS:
                    if (tabJsonObject == null) {
                        break;
                    }
                    availableTabsList.add(tab.getTab());
                    break;
                case RELATED:
                    if (tabJsonObject.isEmpty()) {
                        break;
                    }
                    availableTabsList.add(tab.getTab());
                    break;
                default:
                    break;
            }
        }
        return availableTabsList;
    }

    public void verifyAvailableContentTabsDeepLink(
            List<String> listOfDetailTabs,
            String deepLinkAddress,
            AndroidUtilsExtended androidUtils,
            DisneyPlusMediaPageBase mediaPageBase,
            DisneyLocalizationUtils localizationUtils,
            SoftAssert sa) {
        if (listOfDetailTabs.isEmpty()) {
            Assert.fail("Response did not return any tabs for this content");
        }

        String[] listOfTabs = new String[listOfDetailTabs.size()];
        listOfTabs = listOfDetailTabs.toArray(listOfTabs);

        for (DetailTabs tab : DetailTabs.class.getEnumConstants()) {
            if (Arrays.stream(listOfTabs).anyMatch(tab.getTab()::contains)) {
                androidUtils.closeApp();
                switch (tab) {
                    case EPISODES:
                        verifyDeepLinkTabSelected(
                                deepLinkAddress,
                                "/episodes",
                                mediaPageBase,
                                localizationUtils,
                                "nav_episodes",
                                "Episodes tab not selected", sa);
                        break;
                    case EXTRAS:
                        verifyDeepLinkTabSelected(
                                deepLinkAddress,
                                "/details",
                                mediaPageBase,
                                localizationUtils,
                                "nav_details",
                                "Details tab not selected", sa);
                        break;
                    case RELATED:
                        verifyDeepLinkTabSelected(
                                deepLinkAddress,
                                "/related",
                                mediaPageBase,
                                localizationUtils,
                                "nav_related",
                                "Suggested tab not selected", sa);
                        break;
                    case VIDEOS:
                        verifyDeepLinkTabSelected(
                                deepLinkAddress,
                                "/extras",
                                mediaPageBase,
                                localizationUtils,
                                "nav_extras",
                                "Extras tab not selected", sa);
                        break;
                    default:
                        Assert.fail("Undefined tab usage in test");
                }
            }
        }
    }

    public void verifyDeepLinkTabSelected(
            String deepLinkAddress,
            String deepLinkExtension,
            DisneyPlusMediaPageBase mediaPageBase,
            DisneyLocalizationUtils localizationUtils,
            String tabDictionaryKey,
            String errorMessage,
            SoftAssert sa) {
        String tab = localizationUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, tabDictionaryKey);
        new AndroidUtilsExtended().launchWithDeeplinkAddress(deepLinkAddress + deepLinkExtension);
        mediaPageBase.isOpened();
        sa.assertTrue(mediaPageBase.isMediaPageTabSelected(tab), errorMessage);
    }

    public boolean isMediaTabLayoutDisplayed() {
        return tabLayoutView.isElementPresent();
    }

    public boolean isDownloadScreenEmpty() {
        return emptyDownloadContainerMessageStub.isElementPresent();
    }
}
