package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.NETHERLANDS;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusKijwijzerRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyNetherlandsKijwijzerRatingAL() {
        ratingsSetup(AL.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(AL.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73167"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyNetherlandsKijwijzerRating6() {
        ratingsSetup(SIX.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73168"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyNetherlandsKijwijzerRating9() {
        ratingsSetup(NINE.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(NINE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73169"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyNetherlandsKijwijzerRating12() {
        ratingsSetup(TWELVE.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73170"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyNetherlandsKijwijzerRating16() {
        ratingsSetup(SIXTEEN.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73171"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER})
    public void verifyNetherlandsKijwijzerRating18() {
        ratingsSetup(EIGHTEEN.getContentRating(), NETHERLANDS_LANG, NETHERLANDS);
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }
}
