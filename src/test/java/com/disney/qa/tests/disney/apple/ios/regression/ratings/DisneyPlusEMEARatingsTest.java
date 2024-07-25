package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.util.ArrayList;

import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;

public class DisneyPlusEMEARatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73149"})
    @Test(description = "Rating System - Custom DisneyPlus: EMEA - 12+", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemEMEA12() {
        String country = getEMEACountry();
        ratingsSetup(TWELVE_PLUS.getContentRating(), getEMEACountryLanguage(country), getEMEACountryCode(country));
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    private String getEMEACountry() {
        ArrayList<String> countryList = new ArrayList<>();
        countryList.add("Haiti");
        countryList.add("Mauritius");
        countryList.add("Mayotte");
        countryList.add("Reunion");
        countryList.add("United Kingdom");
        LOGGER.info("Selecting random Country");
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