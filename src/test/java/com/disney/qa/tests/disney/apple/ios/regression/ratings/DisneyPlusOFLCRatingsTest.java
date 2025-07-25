package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;
import org.testng.annotations.Listeners;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.NZ;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusOFLCRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingM() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, M.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(M.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71630"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, G.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(G.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73187"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingPG() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, PG.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(PG.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73190"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRating13() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, THIRTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(THIRTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73191"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingRP13() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, RP13.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(RP13.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73192"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingR15() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, R15.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(R15.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73193"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingR16() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, R16.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(R16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73194"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRating16() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, SIXTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73195"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingRP16() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, RP16.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(RP16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73197"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingR18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, R18.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(R18.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73198"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRating18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, EIGHTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73199"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC, NZ})
    public void verifyNewZealandOFLCRatingRP18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM_MONTHLY, RP18.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(RP18.getContentRating());
    }
}
