package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.common.constant.RatingConstant;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.RatingConstant.Rating.G;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG_13;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_14;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_G;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_MA;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_PG;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_Y;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_Y7;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_Y7_FV;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMPAAandTVPGRatingsTest extends DisneyPlusRatingsBase {

    // Countries and language of these test cases are selected in the 'overrideLocaleConfig' method

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_Y() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_Y.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_Y.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73118"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_Y7() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_Y7.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_Y7.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73119"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_Y7_FV() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_Y7_FV.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_Y7_FV.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73127"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_G() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73128"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73129"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_PG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73130"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemPG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73131"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemPG_13() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, PG_13.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG_13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73132"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_14() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_14.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_14.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73133"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemR() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, RatingConstant.Rating.RESTRICTED.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(RatingConstant.Rating.RESTRICTED.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73134"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_MA() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TV_MA.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TV_MA.getContentRating());
    }
}
