package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.common.constant.RatingConstant;
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
import static com.disney.qa.common.constant.RatingConstant.Rating.G;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_G;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG_13;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_14;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_MA;
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
        String locale = getMPAACountryCode();
        ratingsSetup(TV_Y.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73118"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_Y7() {
        String locale = getMPAACountryCode();
        ratingsSetup(TV_Y7.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y7.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73119"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_Y7_FV() {
        String locale = getMPAACountryCode();
        ratingsSetup(TV_Y7_FV.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y7_FV.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73127"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_G() {
        String locale = getMPAACountryCode();
        ratingsSetup(TV_G.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73128"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemG() {
        String locale = getMPAACountryCode();
        ratingsSetup(G.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73129"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_PG() {
        String locale = getMPAACountryCode();
        ratingsSetup(TV_PG.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73130"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemPG() {
        String locale = getMPAACountryCode();
        ratingsSetup(PG.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73131"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemPG_13() {
        String locale = getMPAACountryCode();
        ratingsSetup(PG_13.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(PG_13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73132"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_14() {
        String locale = getMPAACountryCode();
        ratingsSetup(TV_14.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_14.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73133"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemR() {
        String locale = getMPAACountryCode();
        ratingsSetup(RatingConstant.Rating.RESTRICTED.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(RatingConstant.Rating.RESTRICTED.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73134"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG})
    public void verifyRatingSystemTV_MA() {
        String locale = getMPAACountryCode();
        ratingsSetup(TV_MA.getContentRating(), getMPAACountryLanguage(locale), locale);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_MA.getContentRating());
    }

    private String getMPAACountryCode() {
        List<String> countryCodeList = Arrays.asList(CANADA, UNITED_STATES, UNITED_STATES_VIRGIN_ISLANDS, GUAM,
                PUERTO_RICO, AMERICAN_SAMOA, MARSHALL_ISLANDS, NORTHERN_MARINA_ISLANDS, UNITED_STATES_OUTLYING_ISLANDS);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size()));
    }

    private String getMPAACountryLanguage(String countryCode) {
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