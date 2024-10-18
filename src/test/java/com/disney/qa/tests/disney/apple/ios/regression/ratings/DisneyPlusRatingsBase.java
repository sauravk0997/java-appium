package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.config.DisneyParameters;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang3.exception.*;
import org.testng.*;
import org.testng.asserts.SoftAssert;
import com.amazonaws.services.applicationautoscaling.model.ObjectNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

/**
 * Base ratings setup class
 * IF running on CI as a single class level: set lang/locale on Jenkins
 * IF running locally: set lang/locale on config level
 */
public class DisneyPlusRatingsBase extends DisneyBaseTest {
    private final ThreadLocal<DisneyLocalizationUtils> LOCALIZATION_UTILS = new ThreadLocal<>();
    protected String contentTitle;
    protected String contentYearOfRelease;
    private boolean isMovie;
    String episodicRating;
    static final String PAGE_IDENTIFIER = "page-";
    static final String ENTITY_IDENTIFIER = "entity-";
    static final String EPISODES = "episodes";
    static final String AUSTRALIA_LANG = "en-GB";
    static final String BRAZIL_LANG = "pt-BR";
    static final String GERMANY_LANG = "de";
    static final String JAPAN_LANG = "ja";
    static final String KOREAN_LANG = "ko";
    static final String NETHERLANDS_LANG = "en-GB";
    static final String NEW_ZEALAND_LANG = "en";
    public static final String SINGAPORE_LANG = "en";
    static final String TURKEY_LANG = "tr";
    static final String LATAM_LANG = "es";

    public void ratingsSetup(String lang, String locale, boolean... ageVerified) {
        setDictionary(lang, locale);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), ageVerified));
        getAccountApi().overrideLocations(getAccount(), locale);
        setAccountRatingsMax(getAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }
    public void ratingsSetup(String ratingValue, String lang, String locale, boolean... ageVerified) {
        setDictionary(lang, locale);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), ageVerified));
        getAccountApi().overrideLocations(getAccount(), locale);
        setAccountRatingsMax(getAccount());
        getDesiredRatingContent(ratingValue, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }
    public void ratingSetupWithPINForOTPAccount(String lang, String locale) {
        setDictionary(lang, locale);
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().overrideLocations(getAccount(), locale);
        try {
            getAccountApi().updateProfilePin(getAccount(), getAccount().getProfileId(DEFAULT_PROFILE), PROFILE_PIN);
        } catch (IOException e) {
            new Exception("Failed to update Profile pin: {}", e);
        }
        setAccountRatingsMax(getAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }

    public void ratingsSetupWithPINNew(String lang, String locale, boolean... ageVerified) {
        setDictionary(lang, locale);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), ageVerified));
        getAccountApi().overrideLocations(getAccount(), locale);
        try {
            getAccountApi().updateProfilePin(getAccount(), getAccount().getProfileId(DEFAULT_PROFILE), PROFILE_PIN);
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }
        setAccountRatingsMax(getAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }

    public void ratingsSetupForOTPAccount(String lang, String locale) {
        setDictionary(lang, locale);
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().overrideLocations(getAccount(), locale);
        setAccountRatingsMax(getAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }

    public void handleOneTrustPopUp() {
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        LOGGER.info("Checking for one trust popup");
        if (oneTrustPage.isOpened())
            oneTrustPage.tapAcceptAllButton();
    }

    public void confirmRegionalRatingsDisplays(String rating) {
        if (isMovie) {
            LOGGER.info("Testing against Movie content.");
            validateMovieContent(rating);
        } else {
            LOGGER.info("Testing against Series content.");
            validateSeriesContent(rating);
        }
    }

    private void setAccountRatingsMax(DisneyAccount account) {
        List<String> ratingSystemValues = account.getProfile(DEFAULT_PROFILE).getAttributes().getParentalControls().getMaturityRating()
                .getRatingSystemValues();
        LOGGER.info("Rating values: " + ratingSystemValues);
        getAccountApi().editContentRatingProfileSetting(account,
                LOCALIZATION_UTILS.get().getRatingSystem(),
                ratingSystemValues.get(ratingSystemValues.size() - 1));
    }

    private void setDictionary(String lang, String locale) {
        getLocalizationUtils().setCountryDataByCode(locale);
        getLocalizationUtils().setLanguageCode(lang);
        DisneyLocalizationUtils disneyLocalizationUtils =
                new DisneyLocalizationUtils(
                        locale, lang, MobilePlatform.IOS,
                        DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY);

        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
        disneyLocalizationUtils.setLegalDocuments();
        LOCALIZATION_UTILS.set(disneyLocalizationUtils);
        DisneyPlusApplePageBase.setDictionary(LOCALIZATION_UTILS.get());
    }

    private void getDesiredRatingContent(String rating, String locale, String language) {
        LOGGER.info("Scanning API for title with desired rating '{}'.", rating);
        isMovie = false;
        episodicRating = null;
        String apiContentTitle = null;
        try {
            ArrayList<String> brandIDList = getHomePageBrandIDList(locale, language);
            for (String brandID : brandIDList) {
                LOGGER.info("Searching for content in brand collection: {}", brandID);
                apiContentTitle = getContentForBrand(brandID, rating, locale, language);
                if (apiContentTitle != null && !apiContentTitle.isEmpty()) {
                    break;
                }
                LOGGER.info("Couldn't find content for brand: {} region: {}, rating: {}", brandID, locale, rating);
            }
        } catch (Exception e) {
            throw new ObjectNotFoundException(String.format("Exception occurred while scanning api for the desired rating %s", e.getMessage()));
        }

        if (apiContentTitle == null) {
            throw new SkipException(String.format("Skipping test for rating '%s' as no media is available for it.", rating));
        }
    }

    private ArrayList<String> getHomePageBrandIDList(String locale, String language) {
        LOGGER.info("Preparing brand list for home page ID: {}", HOME_PAGE.getEntityId());
        try {
            ArrayList<Container> collections = getDisneyAPIPage(HOME_PAGE.getEntityId(), locale, language);
            //2nd index from the collections contains all the brand IDs displayed on the home page eg: Disney,Pixar etc
            List<Item> Items = getExploreAPIItemsFromSet(collections.get(1).getId(), locale, language);
            ArrayList<String> brandIDs = new ArrayList<>();
            Items.forEach(item -> brandIDs.add(item.getId()));
            return brandIDs;
        } catch (URISyntaxException | JsonProcessingException | IndexOutOfBoundsException exception) {
            LOGGER.info("Exception occurred while getting the brand IDs from the Home page");
            return ExceptionUtils.rethrow(exception);
        }
    }

    private String getContentForBrand(String brandID, String rating, String locale, String language) throws URISyntaxException, JsonProcessingException {
        ArrayList<String> disneyCollectionIDs = new ArrayList<>();
        ArrayList<Container> collections = getDisneyAPIPage(PAGE_IDENTIFIER + brandID, locale, language);
        collections.forEach(item -> disneyCollectionIDs.add(item.getId()));
        return getContentTitleFor(disneyCollectionIDs, rating, locale, language);
    }

    private String getContentTitleFor(ArrayList<String> disneyCollectionsIDs, String rating, String locale, String language) throws URISyntaxException, JsonProcessingException, IndexOutOfBoundsException {
        LOGGER.info("Rating requested: " + rating);
        for (String disneyCollectionsID : disneyCollectionsIDs) {
            List<Item> disneyCollectionItems = getExploreAPIItemsFromSet(disneyCollectionsID, locale, language);
            for (Item item : disneyCollectionItems) {
                if (item.getVisuals().getMetastringParts() != null) {
                    if (item.getVisuals().getMetastringParts().getRatingInfo() != null) {
                        if (item.getVisuals().getMetastringParts().getRatingInfo().getRating().getText().equals(rating)) {
                            LOGGER.info("Title returned: " + item.getVisuals().getTitle());
                            contentTitle = item.getVisuals().getTitle();
                            contentYearOfRelease =
                                    item.getVisuals().getMetastringParts().getReleaseYearRange().getStartYear();
                            Container pageContainer = getDisneyAPIPage(ENTITY_IDENTIFIER + item.getId(), locale, language).get(0);
                            if (pageContainer != null) {
                                if (!pageContainer.getType().equals(EPISODES)) {
                                    isMovie = true;
                                } else {
                                    if (pageContainer.getSeasons().get(0) != null) {
                                        List<Item> seasonItems = pageContainer.getSeasons().get(0).getItems();
                                        if (seasonItems.get(0) != null) {
                                            episodicRating = seasonItems.get(0).getVisuals().getMetastringParts().getRatingInfo().getRating().getText();
                                        } else {
                                            throw new NullPointerException("Episodic rating is null");
                                        }
                                    }
                                }
                                return contentTitle;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void validateSeriesContent(String rating) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(rating), "Rating was not found in search results");
        searchPage.getDynamicAccessibilityId(contentTitle).click();
        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, sa);
        videoPlayer.validateRatingsOnPlayer(episodicRating, sa, detailsPage);
        detailsPage.waitForRestartButtonToAppear();
        detailsPage.validateRatingsInDetailsTab(rating, sa);

        //ratings are shown on downloaded content
        if (!detailsPage.getEpisodesTab().isPresent()) {
            swipePageTillElementPresent(detailsPage.getEpisodesTab(), 2, detailsPage.getContentDetailsPage(), Direction.DOWN, 500);
        }
        detailsPage.getEpisodesTab().click();
        if (!detailsPage.getDownloadAllSeasonButton().isPresent()) {
            swipe(detailsPage.getDownloadAllSeasonButton());
        }
        detailsPage.getDownloadAllSeasonButton().click();
        detailsPage.clickDefaultAlertBtn();
        detailsPage.waitUntilDownloadsTabNotificationBadgeIsPresent();
        detailsPage.getDownloadNav().click();
        downloads.getStaticTextByLabelContains(contentTitle).click();
        sa.assertTrue(downloads.isRatingPresent(episodicRating), rating + " Rating was not found on series downloads");
        sa.assertAll();
    }

    private void validateMovieContent(String rating) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(rating), "Rating was not found in search results");
        searchPage.getDynamicAccessibilityId(contentTitle).click();

        //ratings are shown on downloaded content
        if (!detailsPage.getMovieDownloadButton().isPresent()) {
            swipe(detailsPage.getMovieDownloadButton(), 3, 500);
        }
        detailsPage.getMovieDownloadButton().click();
        detailsPage.waitUntilDownloadsTabNotificationBadgeIsPresent();
        detailsPage.getDownloadNav().click();
        sa.assertTrue(downloads.isRatingPresent(rating), rating + " Rating was not found on movie downloads.");
        homePage.clickSearchIcon();
        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, sa);
        videoPlayer.validateRatingsOnPlayer(rating, sa, detailsPage);
        detailsPage.waitForRestartButtonToAppear();
        detailsPage.validateRatingsInDetailsTab(rating, sa);
        sa.assertAll();
    }
}
