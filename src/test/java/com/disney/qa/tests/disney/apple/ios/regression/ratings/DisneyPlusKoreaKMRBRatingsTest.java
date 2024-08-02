package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.Rating.*;
import static com.disney.qa.common.constant.RatingConstant.*;

public class DisneyPlusKoreaKMRBRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75173"})
    @Test(description = "Ratings-South Korea 12+ KMRB", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB})
    public void verifyRatingSystemSouthKoreaKMRB12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75174"})
    @Test(description = "Ratings-South Korea 15+ KMRB", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB})
    public void verifyRatingSystemSouthKoreaKMRB15() {
        ratingsSetup(FIFTEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75175"})
    @Test(description = "Ratings-South Korea 18+ KMRB", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB})
    public void verifyRatingSystemSouthKoreaKMRB18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA, true);
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76015"})
    @Test(description = "Ratings-South Korea 19+ KMRB", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KMRB})
    public void verifyRatingSystemSouthKoreaKMRB19() {
        ratingsSetup(NINETEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA, true);
        confirmRegionalRatingsDisplays(NINETEEN_PLUS.getContentRating());
    }
}
