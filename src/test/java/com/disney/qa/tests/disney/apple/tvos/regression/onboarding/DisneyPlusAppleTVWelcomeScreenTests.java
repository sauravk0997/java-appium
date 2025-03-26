package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVWelcomeScreenTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89345"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void welcomeScreenNavigation() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), "Welcome Screen did not launch");
        Assert.assertTrue(welcomeScreen.isLoginBtnFocused(),
                "Login Button is not focused after navigating down from Sign up");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-99228"})
    @Test(groups = {TestGroup.SMOKE, TestGroup.ONBOARDING, US})
    public void welcomeScreenAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        selectAppleUpdateLaterAndDismissAppTracking();
        welcomePage.waitForWelcomePageToLoad();

        sa.assertTrue(welcomePage.isMainTextDisplayed(), "Main text is not displayed");
        sa.assertTrue(welcomePage.getLoginButton().isElementPresent(), "Log In button is not displayed");
        sa.assertTrue(welcomePage.isSubCopyDirectTextPresent(), "Welcome sub text is not present");
        sa.assertTrue(welcomePage.isDynamicAccessibilityIDElementPresent(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        MOBILE_LINK_TEXT.getText())), "Mobile link text not present");
        sa.assertAll();
    }
}
