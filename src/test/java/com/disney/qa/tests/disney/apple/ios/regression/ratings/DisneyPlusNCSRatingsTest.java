package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.AU;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusNCSRatingsTest extends DisneyPlusRatingsBase {
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67724"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS, AU})
    public void verifyRatingSystemAustralia_NCS_G() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73183"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS, AU})
    public void verifyRatingSystemAustralia_NCS_PG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73184"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS, AU})
    public void verifyRatingSystemAustralia_NCS_M() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, M.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(M.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73185"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS, AU})
    public void verifyRatingSystemAustralia_NCS_MA15_PLUS() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, MA15_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(MA15_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73186"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_NCS, AU})
    public void verifyRatingSystemAustralia_NCS_R18_PLUS() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, R18_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(R18_PLUS.getContentRating());
    }
}
