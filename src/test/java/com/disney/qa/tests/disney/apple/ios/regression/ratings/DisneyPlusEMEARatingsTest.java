package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;

public class DisneyPlusEMEARatingsTest extends DisneyPlusRatingsBase {

    private static final String HAITI = "Haiti";
    private static final String MAURITIUS = "Mauritius";
    private static final String MAYOTTE = "Mayotte";
    private static final String REUNION = "Reunion";
    private static final String UNITED_KINGDOM = "United Kingdom";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73149"})
    @Test(description = "Rating System - Custom DisneyPlus: EMEA - 12+", groups = {TestGroup.NON_US_RATINGS})
    public void verifyRatingSystemEMEA12() {
        String country = getEMEACountry();
        ratingsSetup(TWELVE_PLUS.getContentRating(), getEMEACountryLanguage(country), getEMEACountryCode(country));
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    private String getEMEACountry() {
        List<String> countryList = Arrays.asList(HAITI, MAURITIUS, MAYOTTE, REUNION, UNITED_KINGDOM);
        LOGGER.info("Selecting random Country code");
        return countryList.get(new SecureRandom().nextInt(countryList.size() - 1));
    }

    private String getEMEACountryCode(String country) {
        switch (country.toUpperCase()) {
            case "HAITI":
                return "HT";
            case "MAURITIUS":
                return "MU";
            case "MAYOTTE":
                return "YT";
            case "REUNION":
                return "RE";
            case "UNITED KINGDOM":
                return "GB";
            default:
                throw new IllegalArgumentException(String.format("Country Code for %s is not found", country));
        }
    }

    private String getEMEACountryLanguage(String country) {
        switch (country.toUpperCase()) {
            case "HAITI":
                return "fr";
            case "MAURITIUS":
                return "fr";
            case "MAYOTTE":
                return "fr";
            case "REUNION":
                return "fr";
            case "UNITED KINGDOM":
                return "en";
            default:
                throw new IllegalArgumentException(String.format("Country language for %s is not found", country));
        }
    }
}