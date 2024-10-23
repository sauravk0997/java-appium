package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.common.constant.RatingConstant;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.MPAA;
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

public class DisneyPlusMPAAandTVPGRatingsTest extends DisneyPlusRatingsBase {

    // Countries and language of these test cases are selected in the 'overrideLocaleConfig' method

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71631"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_Y() {
        ratingsSetup(TV_Y.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73118"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_Y7() {
        ratingsSetup(TV_Y7.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y7.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73119"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_Y7_FV() {
        ratingsSetup(TV_Y7_FV.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_Y7_FV.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73127"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_G() {
        ratingsSetup(TV_G.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73128"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemG() {
        ratingsSetup(G.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73129"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_PG() {
        ratingsSetup(TV_PG.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73130"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemPG() {
        ratingsSetup(PG.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73131"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemPG_13() {
        ratingsSetup(PG_13.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(PG_13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73132"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_14() {
        ratingsSetup(TV_14.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_14.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73133"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemR() {
        ratingsSetup(RatingConstant.Rating.RESTRICTED.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(RatingConstant.Rating.RESTRICTED.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73134"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_MPAATVPG, MPAA})
    public void verifyRatingSystemTV_MA() {
        ratingsSetup(TV_MA.getContentRating(), getLanguage(), getCountry());
        handleOneTrustPopUp();
        confirmRegionalRatingsDisplays(TV_MA.getContentRating());
    }
}