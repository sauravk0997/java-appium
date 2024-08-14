package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import static com.disney.qa.common.constant.RatingConstant.AMERICAN_SAMOA;
import static com.disney.qa.common.constant.RatingConstant.CANADA;
import static com.disney.qa.common.constant.RatingConstant.GUAM;
import static com.disney.qa.common.constant.RatingConstant.MARSHALL_ISLANDS;
import static com.disney.qa.common.constant.RatingConstant.NORTHERN_MARINA_ISLANDS;
import static com.disney.qa.common.constant.RatingConstant.PUERTO_RICO;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_Y;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_Y7;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_Y7_FV;
import static com.disney.qa.common.constant.RatingConstant.UNITED_STATES;
import static com.disney.qa.common.constant.RatingConstant.UNITED_STATES_OUTLYING_ISLANDS;
import static com.disney.qa.common.constant.RatingConstant.UNITED_STATES_VIRGIN_ISLANDS;

public class DisneyPlusMPAAandTVPGRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_Y() {
        String locale = getEMEACountryCode();
        ratingsSetup(TV_Y.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73118"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_Y7() {
        String locale = getEMEACountryCode();
        ratingsSetup(TV_Y7.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y7.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73119"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_Y7_FV() {
        String locale = getEMEACountryCode();
        ratingsSetup(TV_Y7_FV.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y7_FV.getContentRating());
    }

    private String getEMEACountryCode() {
        List<String> countryCodeList = Arrays.asList(CANADA, UNITED_STATES, UNITED_STATES_VIRGIN_ISLANDS, GUAM,
                PUERTO_RICO, AMERICAN_SAMOA, MARSHALL_ISLANDS, NORTHERN_MARINA_ISLANDS, UNITED_STATES_OUTLYING_ISLANDS);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size()));
    }

    private String getEMEACountryLanguage(String countryCode) {
        switch (countryCode.toUpperCase()) {
            case CANADA:
            case UNITED_STATES:
            case UNITED_STATES_VIRGIN_ISLANDS:
            case GUAM:
            case PUERTO_RICO:
            case AMERICAN_SAMOA:
            case MARSHALL_ISLANDS:
            case UNITED_STATES_OUTLYING_ISLANDS:
                return "en";
            case NORTHERN_MARINA_ISLANDS:
                return "fr";
            default:
                throw new IllegalArgumentException(String.format("Country language for %s is not found", countryCode));
        }
    }
}