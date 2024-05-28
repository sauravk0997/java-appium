package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.SkipException;
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

        String movieFilter = "program";
        String seriesFilter = "series";
        ArrayList<String> contentFilter = new ArrayList<>(Arrays.asList(movieFilter, seriesFilter));
        try {
            ArrayList<Container> collections = getPageContent(DISNEY_PAGE_ID, locale, language);
            ArrayList<String> disneyCollectionsIDs = new ArrayList<>();
            boolean isContentFound = false;
            //Collections under Disney like originals etc
            collections.forEach(item -> disneyCollectionsIDs.add(item.getId()));
            //Items in the collection like Originals -> Monsters at work
            for (int i = 0, disneyCollectionsIDsSize = disneyCollectionsIDs.size(); i < disneyCollectionsIDsSize; i++ && !isContentFound) {
                String collectionID = disneyCollectionsIDs.get(i);
                List<Item> disneyCollectionItems = getItemsFromSet(collectionID, locale, language);
                System.out.println("In disneyCollectionsID loop for collectionID: " + collectionID);
                for (Item item : disneyCollectionItems) {
                    if (item.getVisuals().getMetastringParts().getRatingInfo().getRating().getText().equals(rating)) {
                        byte[] bytePayload = item.getVisuals().getTitle().getBytes(StandardCharsets.ISO_8859_1);

                        System.out.println("Title returned: " + new String(bytePayload, StandardCharsets.UTF_8));
                        CONTENT_TITLE.add(new String(bytePayload, StandardCharsets.UTF_8));
                        isMovie = true;
                        isContentFound = true;
                        return;
                    }
                }
                if (isContentFound) {
                    return;
                }
            }
        } catch (Exception e) {
            LOGGER.info(String.format("Exception:", e.getMessage()));
        }
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
        if(!detailsPage.getDownloadAllSeasonButton().isPresent()) {
            swipe(detailsPage.getDownloadAllSeasonButton());
        }
        pressByElement(detailsPage.getDownloadAllSeasonButton(), 1);
        detailsPage.clickDefaultAlertBtn();
        detailsPage.getDownloadNav().click();
        downloads.getStaticTextByLabelContains(contentTitle).click();
        sa.assertTrue(downloads.isRatingPresent(ratingsDictionaryKey), rating  + " Rating was not found on series downloads.");
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
        sa.assertTrue(downloads.isRatingPresent(ratingsDictionaryKey), rating  + " Rating was not found on movie downloads.");
        homePage.clickSearchIcon();
        detailsPage.verifyRatingsInDetailsFeaturedArea(rating, ratingsDictionaryKey, sa);
        videoPlayer.validateRatingsOnPlayer(rating, ratingsDictionaryKey, sa, detailsPage);
        detailsPage.validateRatingsInDetailsTab(rating, ratingsDictionaryKey, sa);
        sa.assertAll();
    }
}
