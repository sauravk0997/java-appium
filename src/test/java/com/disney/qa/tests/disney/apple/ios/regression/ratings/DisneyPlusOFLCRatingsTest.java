package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.NEW_ZEALAND;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusOFLCRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingM() {
        ratingsSetup(M.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(M.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingG() {
        ratingsSetup(G.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingPG() {
        ratingsSetup(PG.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRating13() {
        ratingsSetup(THIRTEEN.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(THIRTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingRP13() {
        ratingsSetup(RP13.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(RP13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingR15() {
        ratingsSetup(R15.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(R15.getContentRating());
    }
}

