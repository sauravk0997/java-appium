package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVAccountSharingPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVLoginPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVOneTimePasscodePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVPasswordPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSettingsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.CONTINUE_BTN_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.IConstantHelper.WELCOME_SCREEN_NOT_DISPLAYED;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVAccountTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118407"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifySubCopyForExtraMemberAddOnUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        setAccount(createAccountSharingUnifiedAccounts().getReceivingAccount());
        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        settingsPage.clickSubscriptionsCell();
        Assert.assertTrue(settingsPage.isExtraMemberSubscriptionDetailTitlePresent(),
                "Extra member subscription detail title not displayed");
        Assert.assertTrue(settingsPage.isExtraMemberSubscriptionDetailSubCopyPresent(),
                "Extra member subscription detail sub copy not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116817"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHSoftBlockVerification() {
        String email = "accountsharingsofttest@disneyplustesting.com";
        String password = "Test1234!";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                "OOH Soft Block page headline not displayed");
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenSubCopyPresent(),
                "OOH Soft Block page subcopy not displayed");
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenSubCopyTwoPresent(),
                "OOH Soft Block page subcopy 2 not displayed");
        sa.assertTrue(accountSharingPage.getOOHSoftBlockContinueButton().isPresent(),
                CONTINUE_BTN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHSoftBlockLogOutButton().isPresent(),
                "Log out button not displayed");
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                "OOH Verify Device headline/screen not displayed");
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyPresent(),
                "OOH Verify Device subcopy not displayed");
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyTwoPresent(),
                "OOH Verify Device subcopy, 'learn more' not displayed");
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceButton().isPresent(),
                "Verify Device button not displayed");
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceDismissButton().isPresent(),
                "No Thanks/Dismiss button not displayed");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                "User not navigated to OTP page");
        sa.assertAll();
    }

    private void loginWithAccountSharingUser(String email, String password) {
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage passcodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreen.clickLogInButton();
        loginPage.proceedToLocalizedPasswordScreen(email);
        Assert.assertTrue(passcodePage.isOpened(), "Log In password screen did not launch");
        passcodePage.clickLoginWithPassword();
        passwordPage.logInWithPasswordLocalized(password);
    }
}
