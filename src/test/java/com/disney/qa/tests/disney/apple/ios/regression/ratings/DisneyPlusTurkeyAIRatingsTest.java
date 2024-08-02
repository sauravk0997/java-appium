package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.Rating.*;
import static com.disney.qa.common.constant.RatingConstant.TURKEY;

public class DisneyPlusTurkeyAIRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75189"})
    @Test(description = "Rating System - AI - Turkey - GA", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI})
    public void verifyTurkeyAIRatingGA() {
        ratingsSetup(GA.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(GA.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75190"})
    @Test(description = "Rating System - AI - Turkey - 7+", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI})
    public void verifyTurkeyAIRating7() {
        ratingsSetup(SEVEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75191"})
    @Test(description = "Rating System - AI - Turkey - 10+", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI})
    public void verifyTurkeyAIRating10() {
        ratingsSetup(TEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(TEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75192"})
    @Test(description = "Rating System - AI - Turkey - 13+", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI})
    public void verifyTurkeyAIRating13() {
        ratingsSetup(THIRTEEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(THIRTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75193"})
    @Test(description = "Rating System - AI - Turkey - 16+", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI})
    public void verifyTurkeyAIRating16() {
        ratingsSetup(SIXTEEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(SIXTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75194"})
    @Test(description = "Rating System - AI - Turkey - 18+", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI})
    public void verifyTurkeyAIRating18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}
