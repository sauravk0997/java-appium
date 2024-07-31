package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusBrazilDJCTQRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(description = "DJCTQ - L", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRatingL() {
        ratingsSetup(L.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(description = "DJCTQ - 10", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating10() {
        ratingsSetup(TEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(description = "DJCTQ - 12", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating12() {
        ratingsSetup(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }
}
