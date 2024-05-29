package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.exception.*;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Base ratings setup class
 * IF running on CI as a single class level: set lang/locale on Jenkins
 * IF running on CI as from test XML level: lang/locale is configured from associated test XML parameter
 * IF running locally: set lang/locale on config level
 */
public class DisneyPlusRatingsBase extends DisneyBaseTest {
    protected List<String> CONTENT_TITLE = new ArrayList<>();
    private boolean isMovie = false;
    static final String PAGE_IDENTIFIER = "page-";
    static final String ENTITY_IDENTIFIER = "entity-";
    static final String EPISODES = "episodes";
    static final String APAC_G = "G";
    static final String APAC_PG = "PG";
    static final String APAC_12 = "12+";
    static final String KCC_7 = "7+";
    static final String KCC_12 = "12+";
    static final String KCC_15 = "15+";
    static final String KCC_19 = "19+";
    static final String KMRB_12 = "12+";
    static final String KMRB_15 = "15+";
    static final String KMRB_18 = "18+";
    static final String KOREA_LOCALE = "KR";
    static final String KOREAN_LANG = "KO";
    static final String JAPAN_LOCALE = "JP";
    static final String JAPAN_LANG = "ja";
    static final String SINGAPORE_LOCALE = "SG";
    static final String SINGAPORE_LANG = "en";
    static final String R21 = "R21";

    public void ratingsSetup(String ratingValue, String lang, String locale, boolean... ageVerified) {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, locale, lang, ageVerified));
        getAccountApi().overrideLocations(getAccount(), locale);
        setAccountRatingsMax(getAccount());
        getDesiredRatingContent(ratingValue, locale, lang);
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }

    public void ratingsSetupForOTPAccount(String ratingValue, String lang, String locale) {
        //getDesiredRatingContent(ratingValue);
        setAccount(getAccountApi().createAccountForOTP(locale, lang));
        getAccountApi().overrideLocations(getAccount(), locale);
        setAccountRatingsMax(getAccount());
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
    }

    private void setAccountRatingsMax(DisneyAccount account) {
        List<String> ratingSystemValues = account.getProfile(DEFAULT_PROFILE).getAttributes().getParentalControls().getMaturityRating()
                .getRatingSystemValues();
        LOGGER.info("Rating values: " + ratingSystemValues);
        getAccountApi().editContentRatingProfileSetting(account,
                getLocalizationUtils().getRatingSystem(),
                ratingSystemValues.get(ratingSystemValues.size() - 1));
    }

    private void getDesiredRatingContent(String rating, String locale, String language) {
        LOGGER.info("Scanning API for title with desired rating '{}'.", rating);
        try {
            List<String> apiContentTitleList;
            ArrayList<String> brandIDList = getBrandIDList(HOME_PAGE_ID, locale, language);
            for (int i = 0, brandCollectionSize = brandIDList.size(); i < brandCollectionSize; i++) {
                LOGGER.info("Searching for content in brand collection: {}", brandIDList.get(i));
                apiContentTitleList = getContentForBrand(brandIDList.get(i), rating, locale, language);
                if (apiContentTitleList != null) {
                    break;
                }
                LOGGER.info("Couldn't find content for brand: {} region: {}, rating: {}", brandIDList.get(i), locale, rating);
            }
        } catch (Exception e) {
            LOGGER.info(String.format("Exception occurred while scanning api for the desired rating:", e.getMessage()));
        }
    }

    private ArrayList<String> getBrandIDList(String homePageID, String locale, String language) throws URISyntaxException, JsonProcessingException {
        LOGGER.info("Preparing brand list for home page ID: {}", homePageID);
        try {
            ArrayList<Container> collections = getPageContent(homePageID, locale, language);
            List<Item> Items = getItemsFromSet(collections.get(1).getId(), locale, language);
            ArrayList<String> brandIDs = new ArrayList<>();
            Items.forEach(item -> brandIDs.add(item.getId()));
            return brandIDs;
        } catch (URISyntaxException | JsonProcessingException exception) {
            LOGGER.info("Exception occurred while getting the brand IDs from the Home page");
            return ExceptionUtils.rethrow(exception);
        }
    }

    private List<String> getContentForBrand(String brandID, String rating, String locale, String language) throws URISyntaxException, JsonProcessingException {
        ArrayList<String> disneyCollectionIDs = new ArrayList<>();
        ArrayList<Container> collections = getPageContent(PAGE_IDENTIFIER + brandID, locale, language);
        collections.forEach(item -> disneyCollectionIDs.add(item.getId()));
        return getContentTitleFor(disneyCollectionIDs, rating, locale, language);
    }

    //TODO: since we are only interested in one content, return string instead of List
    private List<String> getContentTitleFor(ArrayList<String> disneyCollectionsIDs, String rating, String locale, String language) throws URISyntaxException, JsonProcessingException {
        LOGGER.info("Rating requested: " + rating);
        for (int i = 0, disneyCollectionsIDsSize = disneyCollectionsIDs.size(); i < disneyCollectionsIDsSize; i++) {
            List<Item> disneyCollectionItems = getItemsFromSet(disneyCollectionsIDs.get(i), locale, language);
            for (Item item : disneyCollectionItems) {
                if (item.getVisuals().getMetastringParts() != null) {
                    if (item.getVisuals().getMetastringParts().getRatingInfo().getRating().getText().equals(rating)) {
                        byte[] bytePayload = item.getVisuals().getTitle().getBytes(StandardCharsets.ISO_8859_1);
                        LOGGER.info("Title returned: " + new String(bytePayload, StandardCharsets.UTF_8));
                        CONTENT_TITLE.add(new String(bytePayload, StandardCharsets.UTF_8));
                        if (!(getPageContent(ENTITY_IDENTIFIER + item.getId(), locale, language).get(0).getType().equals(EPISODES))) {
                            isMovie = true;
                        }
                        return CONTENT_TITLE;
                    }
                } else {
                    //TODO: remove if all tests are good to go
                    //In case if we get the collections from the brand page we need to make another page call and do the same thing as in the if loop
                    //List<Container> containers =  getPageContent(PAGE_IDENTIFIER + item.getId(), locale, language);
                }
            }
        }
        return null;
    }

    public void confirmRegionalRatingsDisplays(String rating, String ratingsDictionaryKey) {
        if (isMovie) {
            LOGGER.info("Testing against Movie content.");
            validateMovieContent(rating, ratingsDictionaryKey);
        } else {
            LOGGER.info("Testing against Series content.");
            validateSeriesContent(rating, ratingsDictionaryKey);
        }
    }

    public void validateSeriesContent(String rating, String ratingsDictionaryKey) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String contentTitle = CONTENT_TITLE.get(0);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDisplayedTitles().get(0).click();

        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, ratingsDictionaryKey, sa);
        videoPlayer.validateRatingsOnPlayer(rating, ratingsDictionaryKey, sa, detailsPage);
        detailsPage.validateRatingsInDetailsTab(rating, ratingsDictionaryKey, sa);

        //ratings are shown on downloaded content
        detailsPage.getEpisodesTab().click();
        if (!detailsPage.getDownloadAllSeasonButton().isPresent()) {
            swipe(detailsPage.getDownloadAllSeasonButton());
        }
        pressByElement(detailsPage.getDownloadAllSeasonButton(), 1);
        detailsPage.clickDefaultAlertBtn();
        detailsPage.getDownloadNav().click();
        downloads.getStaticTextByLabelContains(contentTitle).click();
        sa.assertTrue(downloads.isRatingPresent(ratingsDictionaryKey), rating + " Rating was not found on series downloads.");
        sa.assertAll();
    }

    public void validateMovieContent(String rating, String ratingsDictionaryKey) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String contentTitle = CONTENT_TITLE.get(0);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDisplayedTitles().get(0).click();

        //ratings are shown on downloaded content
        if (!detailsPage.getMovieDownloadButton().isPresent()) {
            swipe(detailsPage.getMovieDownloadButton(), 3, 500);
        }
        detailsPage.getMovieDownloadButton().click();
        detailsPage.getDownloadNav().click();
        sa.assertTrue(downloads.isRatingPresent(ratingsDictionaryKey), rating + " Rating was not found on movie downloads.");
        homePage.clickSearchIcon();
        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, ratingsDictionaryKey, sa);
        videoPlayer.validateRatingsOnPlayer(rating, ratingsDictionaryKey, sa, detailsPage);
        detailsPage.validateRatingsInDetailsTab(rating, ratingsDictionaryKey, sa);
        sa.assertAll();
    }
}
