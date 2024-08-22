package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.NEW_ZEALAND;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusOFLCRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73188"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingM() {
        ratingsSetup(M.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(M.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73193"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingR16() {
        ratingsSetup(R16.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(R16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73194"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRating16() {
        ratingsSetup(SIXTEEN.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73195"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingRP16() {
        ratingsSetup(RP16.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(RP16.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73197"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingR18() {
        ratingsSetup(R18.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(R18.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73198"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRating18() {
        ratingsSetup(EIGHTEEN.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73199"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_OFLC})
    public void verifyNewZealandOFLCRatingRP18() {
        ratingsSetup(RP18.getContentRating(), NEW_ZEALAND_LANG, NEW_ZEALAND);
        confirmRegionalRatingsDisplays(RP18.getContentRating());
    }
}
