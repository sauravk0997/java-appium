package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;

public class DisneyPlusNonUSMoreMenuProfilesTest extends DisneyBaseTest {


    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69677"})
    @Test(description = "Verify the flows when Profile Creation is restricted", groups = {"NonUS More Menu", TestGroup.PRE_CONFIGURATION })
    public void verifyProfileCreationRestrictedFunctionality() {
        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);


        disneyPlusAccountIOSPageBase.toggleRestrictProfileCreation(IOSUtils.ButtonStatus.ON);

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon toggling 'Restrict Profile Creation'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase1 = new DisneyPlusAccountIOSPageBase(getDriver());
        sa.assertTrue(disneyPlusAccountIOSPageBase1.isRestrictProfileCreationEnabled(),
                "'Restrict Profile Creation' toggle was not enabled after submitting credentials");

        disneyPlusAccountIOSPageBase.getBackArrow().click();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon clicking 'Add Profile'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());

        disneyPlusEditProfileIOSPageBase.clickSkipBtn();
        disneyPlusEditProfileIOSPageBase.enterProfileName(RESTRICTED);
        disneyPlusEditProfileIOSPageBase.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        disneyPlusEditProfileIOSPageBase.tapJuniorModeToggle();
        disneyPlusEditProfileIOSPageBase.clickSaveBtn();
        sa.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        parentalConsent.scrollConsentContent(2);
        parentalConsent.tapAgreeButton();
        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed(RESTRICTED),
                "Profile created after submitting credentials was not saved");
        sa.assertAll();
    }

    private void setAppToAccountSettings() {
        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }
}
