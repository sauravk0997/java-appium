package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import static com.disney.qa.common.constant.RatingConstant.HAITI;
import static com.disney.qa.common.constant.RatingConstant.MAURITIUS;
import static com.disney.qa.common.constant.RatingConstant.MAYOTTE;
import static com.disney.qa.common.constant.RatingConstant.REUNION;
import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.UNITED_KINGDOM;

public class DisneyPlusEMEARatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73149"})
    @Test(description = "Rating System - Custom DisneyPlus: EMEA - 12+", groups = {TestGroup.NON_US_RATINGS})
    public void verifyRatingSystemEMEA12() {
        String locale = getEMEACountryCode();
        ratingsSetup(TWELVE_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    private String getEMEACountryCode() {
        List<String> countryCodeList = Arrays.asList(HAITI, MAURITIUS, MAYOTTE, REUNION, UNITED_KINGDOM);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size() - 1));
    }

    private String getEMEACountryLanguage(String countryCode) {
        switch (countryCode.toUpperCase()) {
            case MAURITIUS:
            case MAYOTTE:
            case REUNION:
                return "fr";
            case HAITI:
            case UNITED_KINGDOM:
                return "en";
            default:
                throw new IllegalArgumentException(String.format("Country language for %s is not found", countryCode));
        }
    }
}