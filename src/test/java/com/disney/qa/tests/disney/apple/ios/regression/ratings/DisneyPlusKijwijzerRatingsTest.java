package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PREMIUM_YEARLY_NETHERLANDS;
import static com.disney.qa.common.constant.IConstantHelper.NL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusKijwijzerRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68349"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRatingAL() {
        ratingsSetup(DISNEY_PREMIUM_YEARLY_NETHERLANDS, AL.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(AL.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73167"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating6() {
        ratingsSetup(DISNEY_PREMIUM_YEARLY_NETHERLANDS, SIX.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73168"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating9() {
        ratingsSetup(DISNEY_PREMIUM_YEARLY_NETHERLANDS, NINE.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(NINE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73169"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating12() {
        ratingsSetup(DISNEY_PREMIUM_YEARLY_NETHERLANDS, TWELVE.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73170"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating16() {
        ratingsSetup(DISNEY_PREMIUM_YEARLY_NETHERLANDS, SIXTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73171"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_KIJKWIJZER, NL})
    public void verifyNetherlandsKijwijzerRating18() {
        ratingsSetup(DISNEY_PREMIUM_YEARLY_NETHERLANDS, EIGHTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }
}
