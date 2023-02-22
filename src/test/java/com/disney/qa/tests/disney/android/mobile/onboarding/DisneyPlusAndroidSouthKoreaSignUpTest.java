package com.disney.qa.tests.disney.android.mobile.onboarding;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.DisneyPlusSignUpPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusCreatePasswordPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidSouthKoreaSignUpTest extends BaseDisneyTest {

    private static final String PASSWORD_LESS_THAN_SIX = "Abc12";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71852"})
    @Test(description = "Check Sign Up - Create Password South Korea Strong Password Requirements", groups = {"Onboarding", "Sign Up"})
    public void testCreatePasswordSouthKoreaRequirements() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        androidUtils.get().hideKeyboard();

        signUpPageBase.clickVisibleCheckBoxes();

        signUpPageBase.proceedToPasswordMode(generateNewUserEmail());

        sa.assertEquals(createPasswordPageBase.getPasswordInstructionsText(),
                languageUtils.get().replaceValuePlaceholders
                        (languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                        DictionaryKeys.PASSWORD_REQS_ENHANCED.getText()),
                                "3", "8"), "KR Password Hint Is Not Displayed Or Incorrect");

        createPasswordPageBase.submitNewPassword(PASSWORD_LESS_THAN_SIX);
        sa.assertEquals(createPasswordPageBase.getInputErrorText(),
                languageUtils.get().replaceValuePlaceholders
                        (languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                                        DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()),
                                "8", "3"), "KR Invalid Password Error Not Displayed Or Incorrect");

        sa.assertAll();
    }
}
