package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.BR;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusDJCTQRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRatingL() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, L.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating10() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating12() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, TWELVE.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73144"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating14() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, FOURTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(FOURTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73145"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating16() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, SIXTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73146"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating18() {
        ratingsSetup(DISNEY_PLUS_PREMIUM, EIGHTEEN.getContentRating(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74582"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilSeasonLevelRating() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        ratingsSetup(DISNEY_PLUS_PREMIUM, getCountry());
        launchDeeplink(R.TESTDATA.get("disney_prod_brazil_12_series_deeplink"));
        Assert.assertTrue(detailsPage.isSeasonRatingPresent(), "Season rating was not found");
    }
}
