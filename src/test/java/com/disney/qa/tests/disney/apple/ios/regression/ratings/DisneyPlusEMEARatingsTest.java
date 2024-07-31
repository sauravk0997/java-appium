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
import static com.disney.qa.common.constant.RatingConstant.Rating.EIGHTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.FOURTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.NINE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIXTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIX_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.UNITED_KINGDOM;

public class DisneyPlusEMEARatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73147"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA})
    public void verifyRatingSystemEMEA6() {
        String locale = getEMEACountryCode();
        ratingsSetup(SIX_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIX_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73148"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA})
    public void verifyRatingSystemEMEA9() {
        String locale = getEMEACountryCode();
        ratingsSetup(NINE_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(NINE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73149"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA})
    public void verifyEMEARating12() {
        String locale = getEMEACountryCode();
        ratingsSetup(TWELVE_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73150"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA})
    public void verifyRatingSystemEMEA14() {
        String locale = getEMEACountryCode();
        ratingsSetup(FOURTEEN_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(FOURTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73151"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA})
    public void verifyRatingSystemEMEA16() {
        String locale = getEMEACountryCode();
        ratingsSetup(SIXTEEN_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIXTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73152"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA})
    public void verifyRatingSystemEMEA18() {
        String locale = getEMEACountryCode();
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), getEMEACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
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