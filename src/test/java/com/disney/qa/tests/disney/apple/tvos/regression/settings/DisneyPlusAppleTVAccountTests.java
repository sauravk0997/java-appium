package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVLoginPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVOneTimePasscodePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVPasswordPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSettingsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
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
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

        sa.assertTrue(homePage.getStaticTextByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_SOFT_BLOCK_HEADLINE.getText())).isPresent(),
                "OOH Soft Block page headline not displayed");
        sa.assertTrue(homePage.getStaticTextByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_SOFT_BLOCK_SUBCOPY.getText())).isPresent(),
                "OOH Soft Block page subcopy not displayed");
        sa.assertTrue(homePage.getStaticTextByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_SOFT_BLOCK_SUBCOPY_2.getText())).isPresent(),
                "OOH Soft Block page subcopy 2 not displayed");
        sa.assertTrue(homePage.getTypeButtonByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_VERIFY_DEVICE_CTA.getText())).isPresent(),
                CONTINUE_BTN_NOT_DISPLAYED);
        sa.assertTrue(homePage.getTypeButtonByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_LOGOUT_CTA.getText())).isPresent(),
                "Log out button not displayed");
        homePage.clickSelect();

        sa.assertTrue(homePage.getStaticTextByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_VERIFY_DEVICE_HEADLINE.getText())).isPresent(),
                "OOH Verify Device headline/screen not displayed");
        sa.assertTrue(homePage.getStaticTextByLabelContains(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_VERIFY_DEVICE_SUBCOPY.getText())).isPresent(),
                "OOH Verify Device subcopy not displayed");
        sa.assertTrue(homePage.getStaticTextByLabelContains(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_VERIFY_DEVICE_SUBCOPY_2.getText())).isPresent(),
                "OOH Verify Device subcopy, 'learn more' not displayed");
        sa.assertTrue(homePage.getTypeButtonByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_VERIFY_DEVICE_OTP_CTA.getText())).isPresent(),
                "Verify Device button not displayed");
        sa.assertTrue(homePage.getTypeButtonByLabel(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                                DictionaryKeys.OOH_VERIFY_DEVICE_DISMISS_CTA.getText())).isPresent(),
                "No Thanks/Dismiss button not displayed");
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
