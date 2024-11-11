package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.Rating.EIGHTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.FOURTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.NINE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIXTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIX_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.ZERO_PLUS;

public class DisneyPlusEMEARatingsTest extends DisneyPlusRatingsBase {

    // Countries and language of these test cases are selected in the 'overrideLocaleConfig' method

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68351"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyRatingSystemEMEA0() {
        ratingsSetup(ZERO_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(ZERO_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73147"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyRatingSystemEMEA6() {
        ratingsSetup(SIX_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIX_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73148"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyRatingSystemEMEA9() {
        ratingsSetup(NINE_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(NINE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73149"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyEMEARating12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73150"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyRatingSystemEMEA14() {
        ratingsSetup(FOURTEEN_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(FOURTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73151"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyRatingSystemEMEA16() {
        ratingsSetup(SIXTEEN_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIXTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73152"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_EMEA, EMEA})
    public void verifyRatingSystemEMEA18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}