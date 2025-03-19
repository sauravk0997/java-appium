package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.*;
import com.disney.qa.common.constant.*;
import com.disney.qa.common.utils.helpers.IAPIHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.exception.*;
import org.testng.*;
import org.testng.asserts.SoftAssert;
import com.amazonaws.services.applicationautoscaling.model.ObjectNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;
import static com.disney.qa.common.constant.IConstantHelper.DETAILS_PAGE_NOT_DISPLAYED;

/**
 * Base ratings setup class
 * IF running on CI as a single class level: set lang/locale on Jenkins
 * IF running locally: set lang/locale on config level
 */
public class DisneyPlusRatingsBase extends DisneyBaseTest implements IAPIHelper {
    public static ThreadLocal<String> CONTENT_TITLE = new ThreadLocal<>();
    public static ThreadLocal<Boolean> IS_MOVIE = new ThreadLocal<>();
    public static ThreadLocal<String> EPISODIC_RATING = new ThreadLocal<>();;
    static final String PAGE_IDENTIFIER = "page-";
    static final String ENTITY_IDENTIFIER = "entity-";
    static final String EPISODES = "episodes";

    public void ratingsSetup(DisneyUnifiedOfferPlan planName, String locale) {
        LOGGER.info("Locale and language from getLocalizationUtils: {} {}", getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage());
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(planName)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);
        setAccountRatingsMax(getUnifiedAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());
    }

    public void ratingsSetup(DisneyUnifiedOfferPlan planName, String ratingValue, String locale, boolean... ageVerified) {
        LOGGER.info("Locale and language from getLocalizationUtils: {} {}",
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());

        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(planName,
                locale,
                getLocalizationUtils().getUserLanguage(),
                ageVerified)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);

        setAccountRatingsMax(getUnifiedAccount());
        getDesiredRatingContent(ratingValue, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage());
        initialSetup();
        handleAlert();
        //TEMP fix for ATT pop-up
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        setAppToHomeScreen(getUnifiedAccount());
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        //Dismiss ATT Popup
        if (isAlertPresent()) {
            handleGenericPopup(5, 1);
        }
    }

    public void ratingSetupWithPINForOTPAccount(DisneyUnifiedOfferPlan planName, String locale) {
        LOGGER.info("Locale and language from getLocalizationUtils: {} {}", getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage());
        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                planName,
                locale,
                getLocalizationUtils().getUserLanguage())));

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);
        try {
            getUnifiedAccountApi().updateProfilePin(
                    getUnifiedAccount(),
                    getUnifiedAccount().getProfileId(DEFAULT_PROFILE),
                    PROFILE_PIN);
        } catch (IOException e) {
            new Exception("Failed to update Profile pin: {}", e);
        }
        setAccountRatingsMax(getUnifiedAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());
    }

    public void ratingsSetupWithPINNew(DisneyUnifiedOfferPlan planName, String locale) {
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(
                planName,
                locale,
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);
        try {
            getUnifiedAccountApi().updateProfilePin(
                    getUnifiedAccount(),
                    getUnifiedAccount().getProfileId(DEFAULT_PROFILE),
                    PROFILE_PIN);
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }
        setAccountRatingsMax(getUnifiedAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());
    }

    public void ratingsSetupForOTPAccount(DisneyUnifiedOfferPlan planName, String locale) {
        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                planName,
                locale,
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);
        setAccountRatingsMax(getUnifiedAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());
    }

    public void handleOneTrustPopUp() {
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        LOGGER.info("Checking for one trust popup");
        if (oneTrustPage.isOpened())
            oneTrustPage.tapAcceptAllButton();
    }

    public void confirmRegionalRatingsDisplays(String rating) {
        LOGGER.info("Rating value under test: {}", rating);
        if (IS_MOVIE.get()) {
            LOGGER.info("Testing against Movie content.");
            validateMovieContent(rating);
        } else {
            LOGGER.info("Testing against Series content.");
            validateSeriesContent(rating);
        }
    }

    private void setAccountRatingsMax(UnifiedAccount account) {
        List<String> ratingSystemValues = account.getProfile(DEFAULT_PROFILE).getAttributes().getParentalControls().getMaturityRating()
                .getRatingSystemValues();
        LOGGER.info("Rating values: " + ratingSystemValues);
        getUnifiedAccountApi().editContentRatingProfileSetting(account,
                getLocalizationUtils().getRatingSystem(),
                ratingSystemValues.get(ratingSystemValues.size() - 1));
    }

    private void getDesiredRatingContent(String rating, String locale, String language) {
        LOGGER.info("Scanning API for title with desired rating parameters: '{}, {}, {}'.", rating, locale, language);
        IS_MOVIE.set(false);
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
            throw new ObjectNotFoundException(String.format("Exception occurred while scanning api for the desired rating %s", e.getMessage(), locale, language));
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
        CONTENT_TITLE.remove();
        for (String disneyCollectionsID : disneyCollectionsIDs) {
            List<Item> disneyCollectionItems = getExploreAPIItemsFromSet(disneyCollectionsID, locale, language);
            for (Item item : disneyCollectionItems) {
                if (item.getVisuals().getMetastringParts() != null) {
                    if (item.getVisuals().getMetastringParts().getRatingInfo() != null) {
                        if (item.getVisuals().getMetastringParts().getRatingInfo().getRating().getText().equals(rating)) {
                            LOGGER.info("Title returned: " + item.getVisuals().getTitle());
                            CONTENT_TITLE.set(item.getVisuals().getTitle());
                            Container pageContainer = getDisneyAPIPage(ENTITY_IDENTIFIER + item.getId(), locale, language).get(0);
                            if (pageContainer != null) {
                                if (!pageContainer.getType().equals(EPISODES)) {
                                    IS_MOVIE.set(true);
                                } else {
                                    if (pageContainer.getSeasons().get(0) != null) {
                                        IS_MOVIE.set(false);
                                        List<Item> seasonItems = pageContainer.getSeasons().get(0).getItems();
                                        if (seasonItems.get(0) != null) {
                                            EPISODIC_RATING.set(seasonItems.get(0).getVisuals().getMetastringParts().getRatingInfo().getRating().getText());
                                        } else {
                                            throw new NullPointerException("Episodic rating is null");
                                        }
                                    }
                                }
                                return CONTENT_TITLE.get();
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
        searchPage.searchForMedia(CONTENT_TITLE.get());
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(rating), "Rating was not found in search results");
        searchPage.getTitleContainer(CONTENT_TITLE.get(), rating).click();
        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, sa);
        videoPlayer.validateRatingsOnPlayer(EPISODIC_RATING.get(), sa, detailsPage);
        detailsPage.waitForRestartButtonToAppear();
        detailsPage.validateRatingsInDetailsTab(EPISODIC_RATING.get(), sa);

        swipe(detailsPage.getLogoImage(), Direction.DOWN, 2, 1000);
        swipe(detailsPage.getTabBar(), Direction.UP, 2, 1000);
        swipeInContainerTillElementIsPresent(detailsPage.getTabBar(), detailsPage.getEpisodesTab(), 1,
                Direction.RIGHT);
        detailsPage.getEpisodesTab().click();
        if (!detailsPage.getDownloadAllSeasonButton().isPresent()) {
            swipe(detailsPage.getDownloadAllSeasonButton());
        }
        detailsPage.getDownloadAllSeasonButton().click();
        detailsPage.clickDefaultAlertBtn();
        downloads.waitForDownloadToStart();
        detailsPage.getDownloadNav().click();
        downloads.getStaticTextByLabelContains(CONTENT_TITLE.get()).click();
        sa.assertTrue(downloads.isRatingPresent(EPISODIC_RATING.get()), rating + " Rating was not found on series downloads");
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
        searchPage.searchForMedia(CONTENT_TITLE.get());
        sa.assertTrue(searchPage.isRatingPresentInSearchResults(rating), "Rating was not found in search results");
        searchPage.getTitleContainer(CONTENT_TITLE.get(), rating).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);

        //ratings are shown on downloaded content
        if (!detailsPage.getMovieDownloadButton().isPresent()) {
            swipe(detailsPage.getMovieDownloadButton(), 3, 500);
        }
        detailsPage.getMovieDownloadButton().click();
        downloads.waitForDownloadToStart();
        detailsPage.getDownloadNav().click();
        detailsPage.waitForPresenceOfAnElement(downloads.getDownloadAssetFromListView(CONTENT_TITLE.get()));
        sa.assertTrue(downloads.isRatingPresent(rating), rating + " Rating was not found on movie downloads.");
        homePage.clickSearchIcon();
        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, sa);
        videoPlayer.validateRatingsOnPlayer(rating, sa, detailsPage);
        detailsPage.waitForRestartButtonToAppear();
        detailsPage.validateRatingsInDetailsTab(rating, sa);
        sa.assertAll();
    }
}
