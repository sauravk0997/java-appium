package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.AUSTRALIA;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusNCSRatingsTest extends DisneyPlusRatingsBase {
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS})
    public void verifyRatingSystemAustralia_NCS_G() {
        ratingsSetup(G.getContentRating(), AUSTRALIA_LANG, AUSTRALIA);
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73183"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS})
    public void verifyRatingSystemAustralia_NCS_PG() {
        ratingsSetup(PG.getContentRating(), AUSTRALIA_LANG, AUSTRALIA);
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73184"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS})
    public void verifyRatingSystemAustralia_NCS_M() {
        ratingsSetup(M.getContentRating(), AUSTRALIA_LANG, AUSTRALIA);
        confirmRegionalRatingsDisplays(M.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73185"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS})
    public void verifyRatingSystemAustralia_NCS_MA15_PLUS() {
        ratingsSetup(MA15_PLUS.getContentRating(), AUSTRALIA_LANG, AUSTRALIA);
        confirmRegionalRatingsDisplays(MA15_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73186"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS})
    public void verifyRatingSystemAustralia_NCS_R18_PLUS() {
        ratingsSetup(R18_PLUS.getContentRating(), AUSTRALIA_LANG, AUSTRALIA);
        confirmRegionalRatingsDisplays(R18_PLUS.getContentRating());
    }
}
