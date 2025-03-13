package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.KR;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusKMRBRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75173"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB, KR})
    public void verifyRatingSystemSouthKoreaKMRB12() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TWELVE_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75174"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB, KR})
    public void verifyRatingSystemSouthKoreaKMRB15() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, FIFTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75175"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB, KR})
    public void verifyRatingSystemSouthKoreaKMRB18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, EIGHTEEN_PLUS.getContentRating(), getCountry(), true);
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76015"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB, KR})
    public void verifyRatingSystemSouthKoreaKMRB19() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, NINETEEN_PLUS.getContentRating(), getCountry(), true);
        confirmRegionalRatingsDisplays(NINETEEN_PLUS.getContentRating());
    }
}
