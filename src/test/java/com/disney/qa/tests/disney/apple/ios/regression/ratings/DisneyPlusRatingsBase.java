package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import org.testng.SkipException;

import java.util.*;

public class DisneyPlusRatingsBase extends DisneyBaseTest {
    private List<String> CONTENT_TITLE;
    private boolean isMovie = false;

    public void ratingsSetup(String ratingValue, String lang, String locale) {
        setDesiredContentRating(ratingValue, lang, locale);
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

    private void setDesiredContentRating(String rating, String lang, String locale) {
        LOGGER.info("Scanning API for title with desired rating '{}'.", rating);
        List<String> titles = new ArrayList<>();
        String movieFilter = "program";
        String seriesFilter = "series";
        ArrayList<String> contentFilter = new ArrayList<>(Arrays.asList(movieFilter, seriesFilter));

        for (String contentType : contentFilter) {
            Map<String, String> item =
                    getContentApiChecker().findMediaByRating(rating, lang, locale, contentType, titles);

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
}
