package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusDJCTQRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRatingL() {
        ratingsSetup(L.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating10() {
        ratingsSetup(TEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating12() {
        ratingsSetup(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73144"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating14() {
        ratingsSetup(FOURTEEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(FOURTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73145"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating16() {
        ratingsSetup(SIXTEEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73146"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilRating18() {
        ratingsSetup(EIGHTEEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74582"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ})
    public void verifyBrazilSeasonLevelRating() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        ratingsSetup(BRAZIL_LANG, BRAZIL);
        launchDeeplink(R.TESTDATA.get("disney_prod_brazil_12_series_deeplink"));
        Assert.assertTrue(detailsPage.isSeasonRatingPresent(), "Season rating was not found");
    }
}
