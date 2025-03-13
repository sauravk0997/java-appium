package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.KR;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusKCCRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75187"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, KR})
    public void verifyRatingSystemSouthKoreaKCC7() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, SEVEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75186"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, KR})
    public void verifyRatingSystemSouthKoreaKCC12() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TWELVE_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75185"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, KR})
    public void verifyRatingSystemSouthKoreaKCC15() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, FIFTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75184"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, KR})
    public void verifyRatingSystemSouthKoreaKCC19() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, NINETEEN_PLUS.getContentRating(), getCountry(), true);
        confirmRegionalRatingsDisplays(NINETEEN_PLUS.getContentRating());
    }
}
