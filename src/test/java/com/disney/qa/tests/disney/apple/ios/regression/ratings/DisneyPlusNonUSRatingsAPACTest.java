package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.JAPAN;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusNonUSRatingsAPACTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75201"})
    @Test(description = "Rating System - APAC Proprietary - G", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemAPACG() {
        ratingsSetup(G.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(G.getContentRating(), DictionaryKeys.RATING_APAC_G.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75202"})
    @Test(description = "Rating System - APAC Proprietary - PG", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemAPACPG() {
        ratingsSetup(PG.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(PG.getContentRating(), DictionaryKeys.RATING_APAC_PG.getText());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75203"})
    @Test(description = "Rating System - APAC Proprietary - PG", groups = {"NonUS-Ratings"})
    public void verifyRatingSystemAPAC12() {
        ratingsSetup(TWELVE_PLUS.getContentRating(), JAPAN_LANG, JAPAN);
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating(), DictionaryKeys.RATING_APAC_12.getText());
    }
}
