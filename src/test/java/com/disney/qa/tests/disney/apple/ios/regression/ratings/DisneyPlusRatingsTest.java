package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.BRAZIL;
import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE;
import static com.disney.qa.common.constant.RatingConstant.USA;

public class DisneyPlusRatingsTest extends DisneyPlusRatingsBase{

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69549"})
    @Test(groups = {TestGroup.RATINGS})
    public void verifyRatingTravelingAlert() {
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
