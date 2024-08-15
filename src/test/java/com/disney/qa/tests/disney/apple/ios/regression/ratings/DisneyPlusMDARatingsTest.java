package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.Rating.G;
import static com.disney.qa.common.constant.RatingConstant.Rating.M18;
import static com.disney.qa.common.constant.RatingConstant.Rating.NC16;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG13;
import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;
import static com.disney.qa.common.constant.RatingConstant.Rating.R21;

public class DisneyPlusMDARatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69568"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_G() {
        ratingsSetup(G.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73178"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_PG() {
        ratingsSetup(PG.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73179"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_PG13() {
        ratingsSetup(PG13.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(PG13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73180"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_NC16() {
        ratingsSetup(NC16.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(NC16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73181"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MDA})
    public void verifyRatingSystemSingaporeMDA_M18() {
        ratingsSetup(M18.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(M18.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73182"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.DETAILS, TestGroup.R21})
    public void verifyRatingR21() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        confirmRegionalRatingsDisplays(R21.getContentRating());
    }
}