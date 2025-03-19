package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.util.TestGroup;
import org.testng.annotations.Listeners;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.JP;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAPACRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75201"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPACG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75202"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPACPG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75203"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPAC12() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TWELVE_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75204"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPAC15() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, FIFTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(FIFTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75205"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_APAC, JP})
    public void verifyRatingSystemAPAC18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, EIGHTEEN_PLUS.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN_PLUS.getContentRating());
    }
}
