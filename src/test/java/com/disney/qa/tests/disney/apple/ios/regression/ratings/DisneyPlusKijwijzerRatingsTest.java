package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.NL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusKijwijzerRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRatingAL() {
        ratingsSetup(AL.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(AL.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73167"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating6() {
        ratingsSetup(SIX.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73168"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating9() {
        ratingsSetup(NINE.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(NINE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73169"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating12() {
        ratingsSetup(TWELVE.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73170"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating16() {
        ratingsSetup(SIXTEEN.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73171"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating18() {
        ratingsSetup(EIGHTEEN.getContentRating(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }
}
