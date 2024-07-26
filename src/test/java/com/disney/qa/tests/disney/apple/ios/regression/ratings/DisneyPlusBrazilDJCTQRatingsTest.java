package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusBrazilDJCTQRatingsTest extends DisneyPlusRatingsBase {
    private boolean isMovie;

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRatingL() {
        ratingsSetup(L.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRating10() {
        ratingsSetup(TEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRating12() {
        ratingsSetup(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

//XMOBQA-74643

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74643"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilSeasonLevelRating() {
        ratingsSetup(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }


    private void validateSeasonLevelRatigin(String rating) {
        if (isMovie) {
            LOGGER.info("Testing against Movie content.");
            validateMovieContent(rating);
        } else {
            LOGGER.info("Testing against Series content.");
            validateSeriesContent(rating);
        }
    }
}
