package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.NEW_ZEALAND;
import static com.disney.qa.common.constant.RatingConstant.Rating.M;

public class DisneyPlusOFLCRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingM() {
        ratingsSetup(M.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(M.getContentRating());
    }
}
