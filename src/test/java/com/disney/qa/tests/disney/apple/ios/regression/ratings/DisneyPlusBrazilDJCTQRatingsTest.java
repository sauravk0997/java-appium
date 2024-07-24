package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.*;
import static com.disney.qa.common.constant.RatingConstant.USA;

public class DisneyPlusBrazilDJCTQRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68359"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRatingL() {
        ratingsSetup(L.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(L.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73142"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRating10() {
        ratingsSetup(TEN.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TEN.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73143"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRating12() {
        ratingsSetup(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        confirmRegionalRatingsDisplays(TWELVE.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69549"})
    @Test(groups = {TestGroup.NON_US_RATINGS})
    public void verifyBrazilRatingTraveling() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        ratingsSetupNoLogin(TWELVE.getContentRating(), BRAZIL_LANG, BRAZIL);
        getAccountApi().overrideLocations(getAccount(), USA);
        setAppToHomeScreen(getAccount());

        Assert.assertTrue(homePage.isTravelAlertTitlePresent(), "Travel alert title was not present");
        Assert.assertTrue(homePage.isTravelAlertBodyPresent(), "Travel alert body was not present");
        Assert.assertTrue(homePage.getTravelAlertOk().isPresent(), "Travel alert ok button was not present");
        homePage.getTravelAlertOk().click();
        Assert.assertFalse(homePage.isTravelAlertTitlePresent(), "Travel alert was not dismissed.");
    }
}
