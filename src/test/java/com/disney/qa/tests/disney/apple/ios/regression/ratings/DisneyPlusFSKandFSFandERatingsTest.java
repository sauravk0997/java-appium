package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.DE;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusFSKandFSFandERatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68353"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_FSK, DE})
    public void verifyGermanyFSKRating0() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, ZERO.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(ZERO.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73153"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_FSK, DE})
    public void verifyGermanyFSKRating6() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, SIX.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIX.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73154"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_FSK, DE})
    public void verifyGermanyFSKRating12() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TWELVE.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73155"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_FSK, DE})
    public void verifyGermanyFSKRating16() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, SIXTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73156"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_FSK, DE})
    public void verifyGermanyFSKRating18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, EIGHTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }
}
