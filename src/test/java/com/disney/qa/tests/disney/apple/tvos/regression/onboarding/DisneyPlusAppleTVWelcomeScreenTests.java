package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

public class DisneyPlusAppleTVWelcomeScreenTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89345", "XCDQA-89341"})
    @Test(groups = {TestGroup.ONBOARDING}, enabled = false)
    public void welcomeScreenNavigation() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusApplePageBase applePageBase = new DisneyPlusApplePageBase(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome Screen did not launch");
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isSignUpFocused(),
                "Sign up button is not focused post app launch");

        // Alice cannot detect this text in the screen: https://jira.disneystreaming.com/browse/QCE-2472
        // aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, "SIGN UP NOW", AliceLabels.BUTTON_HOVERED.getText());

        disneyPlusAppleTVWelcomeScreenPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isLoginBtnFocused(),
                "Login Button is not focused after navigating down from Sign up");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89343"})
    @Test(groups = {TestGroup.SMOKE, TestGroup.ONBOARDING}, enabled = false)
    public void welcomeScreenAppearance() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());

        var labelList = Stream.of(AliceLabels.DISNEY_LOGO, AliceLabels.NAT_GEO_LOGO, AliceLabels.MARVEL_LOGO,
                AliceLabels.PIXAR_LOGO, AliceLabels.STAR_WARS_LOGO, AliceLabels.CELL_PHONE_IMAGE).collect(Collectors.toList());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomePage.isOpened(), "Welcome screen did not launch");

        AliceAssertion aliceAssertion = aliceDriver.screenshotAndRecognize();
        labelList.forEach(item -> aliceAssertion.isLabelPresent(sa, item.getText()));

        sa.assertTrue(welcomePage.isMainTextDisplayed(), "Main text is not displayed");
        sa.assertTrue(welcomePage.getSignupButton().isElementPresent(), "Sign Up button is not displayed");
        sa.assertTrue(welcomePage.getLoginButton().isElementPresent(), "Log In button is not displayed");
        sa.assertTrue(welcomePage.isSubCopyDirectTextPresent(), "Welcome sub text is not present");
        sa.assertTrue(welcomePage.isDynamicAccessibilityIDElementPresent(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL,
                        MOBILE_LINK_TEXT.getText())), "Mobile link text not present");
        sa.assertAll();
    }
}
