package com.disney.qa.tests.disney.android.mobile.ratings;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.*;

public class DisneyPlusAndroidRatingsTestBase extends BaseDisneyTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    ThreadLocal<String> backupRating = new ThreadLocal<>();
    List<String> ratings;
    List<String> ratingsSystem;
    List<String> usedTitles = new ArrayList<>();
    String countryToRun;

    TreeMap<String, String> mediaToCheck = new TreeMap<>();

    DisneyContentApiChecker apiChecker = new DisneyContentApiChecker(PLATFORM, ENV, PARTNER);
    DisneyCountryData countryData = new DisneyCountryData();

    boolean isEpisode;
    String episodeTitle;

    private static final String COUNTRY_LIST = "countries";

    boolean retry = true;

    /**
     * Scans the D+ Api for media matching the desired rating based on the returned list from the
     * country-specific resource file.
     * @throws JSONException
     */
    @BeforeClass(alwaysRun = true)
    public void testSetup(ITestContext context) throws IOException, ParseException, JSONException, URISyntaxException {
        LOGGER.info("Running BeforeSuite from Ratings test");
        if (context.getCurrentXmlTest().getParameter(COUNTRY_LIST) == null) {
            countryToRun = (String) countryData.searchAndReturnCountryData(R.CONFIG.get("locale"), "code", "country");
            LOGGER.info("Country to run returned: " + countryToRun);
        } else {
            LOGGER.info("Setting country from XML...");
            setRuntimeCountry(context.getCurrentXmlTest().getParameter(COUNTRY_LIST));
        }
        setLocalizationDataByCountryName(countryToRun, R.CONFIG.get("language"));
        try {
            ratings = Arrays.asList(context.getCurrentXmlTest().getParameter("ratings").split(","));
        } catch (NullPointerException npe) {
            ratings = Arrays.asList(R.CONFIG.get("custom_string2").split(","));
        }
        LOGGER.info("Setting up accounts...");

        if (countryToRun.equals("Germany") || countryToRun.equals("Austria")) {
            List<String> de_ratings = new ArrayList<>();
            ratingsSystem = Arrays.asList(languageUtils.get().getRatingSystem().split("And"));
            ratingsSystem.forEach(system -> {
                ratings.forEach(rating -> {
                    de_ratings.add(String.format("%s_%s", system, rating));
                });
            });
            ratings = de_ratings;
        }

        for (String rating : ratings) {
            getMediaForTest(rating, usedTitles);
        }
    }

    private void setAccountParameters() throws IOException {
        disneyAccount.set(accountApi.get().createAccount("Yearly", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), "V1"));
        accountApi.get().addFlex(disneyAccount.get());
        activateRatingsJarvisOverrides(languageUtils.get().getLocale(), initPage(JarvisAndroidBase.class), true);
        List<String> ratingSystemValues = disneyAccount.get().getProfile(DEFAULT_PROFILE).getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues();
        accountApi.get().editContentRatingProfileSetting(disneyAccount.get(),
                languageUtils.get().getRatingSystem(),
                ratingSystemValues.get(ratingSystemValues.size() - 1));
        accountApi.get().setAccountDownloadLimit(disneyAccount.get(), 99);
    }

    @AfterMethod(alwaysRun = true)
    public void changeMedia(ITestResult result){
        if((result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP) && retry){
            LOGGER.info("Previous media failed for some reason. Looking for new media for retry...");
            LOGGER.info("USED: " + usedTitles);
            getMediaForTest(backupRating.get(), usedTitles);
        }
    }


    /**
     * The regional ratings display test checks the app areas where rating metadata is displayed to ensure that
     * the correct regional values are displayed against media with that rating applied. These areas include:
     * - Metadata on media pages
     * - Details tab on media pages
     * - Search results
     * - Downloads
     * - Video Player
     *
     * Test is skipped for a given iteration if Disney+ does not have any media with the desired rating, such as NC-17.
     *
     * @param rating: Provided by the data provider. Consists of Country and Rating in test (ex. Canada | TV-PG)
     * @throws IOException
     */
    public void confirmRegionalRatingsDisplays(String rating) throws IOException {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        setAccountParameters();
        SoftAssert sa = new SoftAssert();
        backupRating.set(rating);

        //If the API fetch did not find a result for a given rating, test iteration is skipped.
        String mediaTitle = mediaToCheck.get(rating);
        if(mediaTitle == null){
            retry = false;
            skipExecution(String.format("Skipping test for rating '%s' as no media is available for it.", rating));
        }

        //Adds given title to usedTitles list so retry-attempts do not grab the same item.
        usedTitles.add(mediaTitle);
        setRetryAndLogin();
        commonPageBase.dismissTravelingDialog();
        rating = adjustRatingString(rating);

        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);

        searchPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        searchPageBase.searchForMedia(mediaTitle);
        searchPageBase.openSearchResult(mediaTitle);
        mediaPageBase.dismissPopup();

        //If media item is premier access restricted, it cannot be used for all areas so test is force failed to retry.
        Assert.assertFalse(mediaPageBase.isPremierAccessLogoVisible(),
                "Media Page opened is 'Premier Access' restricted. This media cannot be used.");

        String displayedRating = mediaPageBase.getMediaDetailsRating(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_DETAILS.getText()));
        Assert.assertEquals(displayedRating, rating,
                "Media page did not display rating value served in the API");

        androidUtils.get().pressBack();

        sa.assertEquals(searchPageBase.getStandardMediaListingMetadataRating(mediaTitle), displayedRating,
                String.format("Search Results metadata to display matching image or value as DETAILS view for '%s'", rating));

        //Attempt to reopen search result. Retry in case click attempt failed due to speed.
        int tries = 0;
        do {
            tries++;
            searchPageBase.openSearchResult(mediaTitle);
        } while (!mediaPageBase.isOpened() && tries < 3);

        mediaPageBase.beginDownload();
        if(mediaPageBase.isErrorPresent(10)) {
            Assert.fail("Something blocked the media download.");
        }

        isEpisode = mediaPageBase.isDownloadAnEpisode();
        if(isEpisode){
            episodeTitle = mediaPageBase.getDownloadEpisodeTitle();
        }
        mediaPageBase.pauseDownload(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PAUSE_DOWNLOAD.getText()));

        sa.assertEquals(mediaPageBase.getMetadataRating(), displayedRating,
                String.format("Media Page metadata to display matching image or value as DETAILS view for '%s", rating));

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        String videoRating = "RATING NOT CAPTURED";

        //Starts video playback to get media rating in video player. Attempts to scrub in case media with an advisory is
        //selected for the test and tries again.
        tries = 0;
        while(tries < 3) {
            try {
                mediaPageBase.startPlayback();
                videoRating = videoPageBase.getRatingId();
                videoPageBase.closeVideo();
                break;
            } catch (NoSuchElementException e) {
                LOGGER.info("Ratings and Advisories were not shown. Long buffering or Content Advisory may have blocked it. Scrubbing video forward and retrying...");
                videoPageBase.waitForVideoBuffering();
                videoPageBase.scrubToPlaybackPercentage(25);
                videoPageBase.closeVideo();
                tries++;
            }
        }

        sa.assertEquals(videoRating, displayedRating,
                "Video Playback to display correct rating value");

        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));

        if(isEpisode){
            sa.assertEquals(downloadsPageBase.getEpisodeMediaListingMetadataRating(mediaTitle, episodeTitle), displayedRating,
                    String.format("Episode Download entry metadata to display matching image or value as DETAILS view for '%s'", rating));
        } else {
            sa.assertEquals(searchPageBase.getStandardMediaListingMetadataRating(mediaTitle), displayedRating,
                    String.format("Movie Download entry metadata to display matching image or value as DETAILS view for '%s'", rating));
        }

        sa.assertAll();
    }

    //Sets retry boolean to true and logs in
    public void setRetryAndLogin(){
        retry = true;
        login(disneyAccount.get(), false);
    }

    /**
     * Updates the runtime log for regions with multiple countries.
     * Cycles forward in the list daily.
     *
     * If a manual selection is made, that country is used.
     */
    private void setRuntimeCountry(String countryList) {
        DateTime today = new DateTime();
        List<String> countries = Arrays.asList(countryList.split(","));
        Collections.sort(countries);
        countryToRun = R.CONFIG.get("custom_string3");

        switch (countryToRun){
            case "NULL":
            case "Default":
                countryToRun = countries.get(today.getDayOfYear() % countries.size());
                break;
            default:
                LOGGER.info("Manual country selection was made. Ignoring daily country...");
        }
    }

    public void getMediaForTest(String rating, List<String> ignoreList){
        Map<String, String> foundMedia;
        LOGGER.info(String.format("Searching for Movies with Rating: '%s'", rating));
        foundMedia = apiChecker.findMediaByRating(rating, languageUtils.get().getUserLanguage(), languageUtils.get().getLocale(), "program", ignoreList);
        if(foundMedia.get(rating) == null){
            LOGGER.info(String.format("Searching for Series with Rating: '%s'", rating));
            foundMedia = apiChecker.findMediaByRating(rating, languageUtils.get().getUserLanguage(), languageUtils.get().getLocale(), "series", ignoreList);
        }

        if(foundMedia.get(rating) == null){
            LOGGER.info("---------- NO MEDIA AVAILABLE FOR THIS RATING ----------");
        }

        mediaToCheck.putAll(foundMedia);
    }

    private String adjustRatingString(String apiRating){
        switch (languageUtils.get().getRatingSystem()){
            case "DisneyPlusEMEA":
                return languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.RATINGS, String.format("rating_custom:disneyplus:kijkwijzer_%s", apiRating).toLowerCase());
            case "NCS":
                return languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.RATINGS, String.format("rating_ncs_%s", apiRating).toLowerCase());
            case "FSKAndFSFAndE":
                return StringUtils.substringAfter(apiRating, "_");
            default:
                return apiRating;
        }
    }
}
