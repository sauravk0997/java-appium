package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.IConstantHelper.TR;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusAIRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75189"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI, TR})
    public void verifyTurkeyAIRatingGA() {
        ratingsSetup(GA.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(GA.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75190"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI, TR})
    public void verifyTurkeyAIRating7() {
        ratingsSetup(SEVEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75191"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI, TR})
    public void verifyTurkeyAIRating10() {
        ratingsSetup(TEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75192"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI, TR})
    public void verifyTurkeyAIRating13() {
        ratingsSetup(THIRTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(THIRTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75193"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI, TR})
    public void verifyTurkeyAIRating16() {
        ratingsSetup(SIXTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75194"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_AI, TR})
    public void verifyTurkeyAIRating18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}
