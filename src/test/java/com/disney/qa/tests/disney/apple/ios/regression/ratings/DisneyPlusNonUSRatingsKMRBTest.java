package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.Rating.*;
import static com.disney.qa.common.constant.RatingConstant.*;

public class DisneyPlusNonUSRatingsKMRBTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75173"})
    @Test(description = "Ratings-South Korea 12+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKMRB12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75174"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKMRB15() {
        ratingsSetup(FIFTEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA);
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75175"})
    @Test(description = "Ratings-South Korea 15+ KCC", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemSouthKoreaKMRB18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), KOREAN_LANG, KOREA, true);
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}
