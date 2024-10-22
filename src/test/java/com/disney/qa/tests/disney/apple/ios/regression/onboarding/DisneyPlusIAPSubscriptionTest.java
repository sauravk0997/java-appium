package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusIAPSubscriptionTest extends DisneyBaseTest {
    private static final String DOB_ADULT = "01/01/1983";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66591"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SIGN_UP, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyValidPasswordSubmissions() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusApplePageBase disneyPlusApplePageBase = initPage(DisneyPlusApplePageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        verifySignUpButtonNavigation();
        SoftAssert sa = new SoftAssert();

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.enterPasswordValue("1234AB!@");
        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "1234AB!@",
                "XMOBQA-62237 - Show/Hide Password did not un-hide the password value");

        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "••••••••",
                "XMOBQA-62237 - Show/Hide Password did not hide the password value");

        disneyPlusApplePageBase.clickPrimaryButton();
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);

        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC));
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanHeaderPresent(), "XMOBQA-62241-Choose your plan card 'title' is not as expected");
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanSubHeaderPresent(), "XMOBQA-62241-Choose your plan card 'subtitle' is not as expected");

        sa.assertAll();
    }

    private void verifySignUpButtonNavigation() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
    }
}
