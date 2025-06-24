package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMoreMenuArielProfilesKeepSessionAliveTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72953"})
    @Test(description = "Profiles > U13 profile, Password action grant for Welch", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyU13PasswordGrantForWelch() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        addProfile.clickSaveProfileButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            parentalConsent.scrollConsentContent(4);
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        softAssert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        softAssert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        softAssert.assertAll();
    }
}
