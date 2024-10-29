package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.IConstantHelper.BR;
import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;

public class DisneyPlusDJCTQRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRatingL() {
        ratingsSetup(L.getContentRating(), getLanguage(), getCountry());
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating10() {
        ratingsSetup(TEN.getContentRating(), getLanguage(), getCountry());
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating12() {
        ratingsSetup(TWELVE.getContentRating(), getLanguage(), getCountry());
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73144"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating14() {
        ratingsSetup(FOURTEEN.getContentRating(), getLanguage(), getCountry());
        confirmRegionalRatingsDisplays(FOURTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73145"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating16() {
        ratingsSetup(SIXTEEN.getContentRating(), getLanguage(), getCountry());
        confirmRegionalRatingsDisplays(SIXTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73146"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilRating18() {
        ratingsSetup(EIGHTEEN.getContentRating(), getLanguage(), getCountry());
        confirmRegionalRatingsDisplays(EIGHTEEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74582"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_DJCTQ, BR})
    public void verifyBrazilSeasonLevelRating() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        ratingsSetup(getCountry());
        launchDeeplink(R.TESTDATA.get("disney_prod_brazil_12_series_deeplink"));
        Assert.assertTrue(detailsPage.isSeasonRatingPresent(), "Season rating was not found");
    }
}
