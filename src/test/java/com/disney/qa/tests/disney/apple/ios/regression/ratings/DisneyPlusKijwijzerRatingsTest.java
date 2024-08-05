package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.NETHERLANDS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIX;

public class DisneyPlusKijwijzerRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating6() {
        ratingsSetup(SIX.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }
}
