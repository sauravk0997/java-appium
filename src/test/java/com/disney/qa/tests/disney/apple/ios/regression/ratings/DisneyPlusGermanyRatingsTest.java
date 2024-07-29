package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.GERMANY;
import static com.disney.qa.common.constant.RatingConstant.Rating.ZERO;

public class DisneyPlusGermanyRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "Ratings-Germany FSK0", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemGermanyFSK0() {
        ratingsSetup(ZERO.getContentRating(), GERMAN_LANGUAGE, GERMANY);
        confirmRegionalRatingsDisplays(ZERO.getContentRating());
    }
}
