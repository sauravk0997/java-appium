package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.JP;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusAPACRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75201"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPACG() {
        ratingsSetup(G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75202"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPACPG() {
        ratingsSetup(PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75203"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPAC12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75204"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPAC15() {
        ratingsSetup(FIFTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75205"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPAC18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}
