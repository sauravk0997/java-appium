package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.GERMANY;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIX;
import static com.disney.qa.common.constant.RatingConstant.Rating.ZERO;

public class DisneyPlusGermanyFSKandFSFandERatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(description = "Ratings-Germany FSK0", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemGermanyFSK0() {
        ratingsSetup(ZERO.getContentRating(), GERMANY_LANG, GERMANY);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(ZERO.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73153"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyGermanyFSKRating6() {
        ratingsSetup(SIX.getContentRating(), GERMANY_LANG, GERMANY);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }
}
