package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.Rating.*;
import static com.disney.qa.common.constant.RatingConstant.TURKEY;

public class DisneyPlusNonUSRatingAITest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75189"})
    @Test(description = "Rating System - AI - Turkey - GA", groups = {"NonUS-Ratings"})
    public void verifyTurkeyAIRatingGA() {
        ratingsSetup(GA.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(GA.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75190"})
    @Test(description = "Rating System - AI - Turkey - 7+", groups = {"NonUS-Ratings"})
    public void verifyTurkeyAIRating7() {
        ratingsSetup(SEVEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75192"})
    @Test(description = "Rating System - AI - Turkey - 13+", groups = {"NonUS-Ratings"})
    public void verifyTurkeyAIRating13() {
        ratingsSetup(THIRTEEN_PLUS.getContentRating(), TURKEY_LANG, TURKEY);
        confirmRegionalRatingsDisplays(THIRTEEN_PLUS.getContentRating());
    }
}
