package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.util.*;

/**
 * Base ratings setup class
 * IF running on CI as a single class level: set lang/locale on Jenkins
 * IF running on CI as from test XML level: lang/locale is configured from associated test XML parameter
 * IF running locally: set lang/locale on config level
 */
public class DisneyPlusRatingsBase extends DisneyBaseTest {
    private List<String> CONTENT_TITLE;
    private boolean isMovie = false;
    static final String APAC_G = "G";
    static final String KCC_7 = "7+";
    static final String KCC_12 = "12+";
    static final String KCC_15 = "15+";
    static final String KCC_19 = "19+";
    static final String KOREA_LOCALE = "KR";
    static final String KOREAN_LANG = "KO";
    static final String JAPAN_LOCALE = "JP";
    static final String JAPAN_LANG = "JP";

    public void ratingsSetup(String ratingValue, String lang, String locale) {
        getDesiredRatingContent(ratingValue, lang, locale);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, locale, lang));
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

    private void getDesiredRatingContent(String rating, String lang, String locale) {
        LOGGER.info("Scanning API for title with desired rating '{}'.", rating);
        List<String> titles = new ArrayList<>();
        String movieFilter = "program";
        String seriesFilter = "series";
        ArrayList<String> contentFilter = new ArrayList<>(Arrays.asList(movieFilter, seriesFilter));

        for (String contentType : contentFilter) {
            Map<String, String> item =
                    getContentApiChecker().findMediaByRating(rating, lang, locale, contentType, titles);
            if (item.isEmpty()) {
                continue;
            }
            if (!item.get(rating).isEmpty()) {
                LOGGER.info("Found rating {} content for filer type {}.", rating, contentType);
                titles.add(item.get(rating));
                CONTENT_TITLE = titles;
                if (Objects.equals(contentType, movieFilter)) {
                    LOGGER.info("Content under test is a Movie.");
                    isMovie = true;
                } else {
                    LOGGER.info("Content under test is Series.");
                }
                return;
            }
            LOGGER.info("No content found for filter type {} against rating system {}.", contentType, rating);
        }
        //no content found for desired rating value and skipping test
        throw new SkipException(
                String.format("Skipping test for rating '%s' as API returned no content.", rating));
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
        if(!detailsPage.getMovieDownloadButton().isElementPresent()) {
            swipe(detailsPage.getMovieDownloadButton(),3,500);
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
