package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.RatingConstant.*;

public class DisneyPlusRatingsTest extends DisneyPlusRatingsBase{

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69549"})
    @Test(groups = {TestGroup.RATINGS})
    public void verifyRatingRestrictionTravelingMessage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setDictionary(SINGAPORE_LANG, SINGAPORE);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_STARHUB_SG_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().patchStarOnboardingStatus(getAccount(), true);
        getAccountApi().overrideLocations(getAccount(), BRAZIL);
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());

        Assert.assertTrue(homePage.isTravelAlertTitlePresent(), "Travel alert title was not present");
        Assert.assertTrue(homePage.isTravelAlertBodyPresent(), "Travel alert body was not present");
        Assert.assertTrue(homePage.getTravelAlertOk().isPresent(), "Travel alert ok button was not present");
        homePage.getTravelAlertOk().click();
        Assert.assertFalse(homePage.isTravelAlertTitlePresent(), "Travel alert was not dismissed.");
    }
}
