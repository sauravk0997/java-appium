package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusNonUSMoreMenuAccountSettingsTest  extends DisneyBaseTest {

    private static final String GOOGLE_URL = "accounts.google.com";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify monthly subscription details for Apple subscribers", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyAccountMonthlyToAnnualDisplays_Apple() {
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = new DisneyPlusPaywallIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was not displayed");

        disneyPlusAccountIOSPageBase.clickSwitchToAnnualButton();

        Assert.assertTrue(disneyPlusPaywallIOSPageBase.isOpened(),
                "User was not directed to the paywall after clicking Switch CTA");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isSwitchToAnnualHeaderDisplayed(),
                "'Switch to Annual' header was not shown");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isSwitchToAnnualCopyDisplayed(),
                "'Switch to Annual' copy was not shown");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75401"})
    @Test(description = "Verify monthly subscription details for Google subscribers", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyAccountMonthlyToAnnualDisplays_Google() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was not displayed");

        disneyPlusAccountIOSPageBase.clickSwitchToAnnualButton();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");
    }

    public void setAppToAccountSettings() {
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }
}
