package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.RatingConstant.Rating.FOURTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SEVEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIXTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.TEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.THIRTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.ZERO_PLUS;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusLATAMRatingsTest extends DisneyPlusRatingsBase {

    // LATAM countries of these test cases are selected by a random function in the 'overrideLocaleConfig' method
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM0() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, ZERO_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(ZERO_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73172"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM7() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, SEVEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73173"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM10() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73174"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM12() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TWELVE_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73175"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM13() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, THIRTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(THIRTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73176"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM14() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, FOURTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(FOURTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73177"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM, LATAM})
    public void verifyRatingSystemLATAM16() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, SIXTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN_PLUS.getContentRating());
    }
}
