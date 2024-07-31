package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.JAPAN;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusAPACRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75201"})
    @Test(description = "Rating System - APAC Proprietary - G", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC})
    public void verifyRatingSystemAPACG() {
        ratingsSetup(G.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75202"})
    @Test(description = "Rating System - APAC Proprietary - PG", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC})
    public void verifyRatingSystemAPACPG() {
        ratingsSetup(PG.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75203"})
    @Test(description = "Rating System - APAC Proprietary - 12", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC})
    public void verifyRatingSystemAPAC12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75203"})
    @Test(description = "Rating System - APAC Proprietary - 15", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC})
    public void verifyRatingSystemAPAC15() {
        ratingsSetup(FIFTEEN_PLUS.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75203"})
    @Test(description = "Rating System - APAC Proprietary - 18", groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC})
    public void verifyRatingSystemAPAC18() {
        ratingsSetup(EIGHTEEN_PLUS.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}
