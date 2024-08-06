package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.NETHERLANDS;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusKijwijzerRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRatingALEnglish() {
        ratingsSetup(AL.getContentRating(), "en-GB", NETHERLANDS);
        confirmRegionalRatingsDisplays(AL.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRatingALnlNL() {
        ratingsSetup(AL.getContentRating(), "nl-NL", NETHERLANDS);
        confirmRegionalRatingsDisplays(AL.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRatingALnl() {
        ratingsSetup(AL.getContentRating(), "nl", NETHERLANDS);
        confirmRegionalRatingsDisplays(AL.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating6enGB() {
        ratingsSetup(SIX.getContentRating(), "en-GB", NETHERLANDS);
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating6nlNL() {
        ratingsSetup(SIX.getContentRating(), "nl-NL", NETHERLANDS);
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating6nl() {
        ratingsSetup(SIX.getContentRating(), "nl", NETHERLANDS);
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating9enGB() {
        ratingsSetup(NINE.getContentRating(), "en-GB", NETHERLANDS);
        confirmRegionalRatingsDisplays(NINE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating9nlNL() {
        ratingsSetup(NINE.getContentRating(), "nl-NL", NETHERLANDS);
        confirmRegionalRatingsDisplays(NINE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyKijwijzerRating9nl() {
        ratingsSetup(NINE.getContentRating(), "nl", NETHERLANDS);
        confirmRegionalRatingsDisplays(NINE.getContentRating());
    }
}
