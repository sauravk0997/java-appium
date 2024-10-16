package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.RatingConstant.KOREA;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusKCCRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75187"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, US})
    public void verifyRatingSystemSouthKoreaKCC7() {
        ratingsSetup(SEVEN_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75186"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, US})
    public void verifyRatingSystemSouthKoreaKCC12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75185"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, US})
    public void verifyRatingSystemSouthKoreaKCC15() {
        ratingsSetup(FIFTEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75184"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KCC, US})
    public void verifyRatingSystemSouthKoreaKCC19() {
        ratingsSetup(NINETEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA, true);
        confirmRegionalRatingsDisplays(NINETEEN_PLUS.getContentRating());
    }
}
