package com.disney.qa.tests.disney.android.mobile.onboarding;

import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.TestLabel;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidArielSignUpTest extends BaseDisneyTest {

    private static final String NEW_PASSWORD = "G0D1sn3yQ@";
    private static final String DOBU18 = "01012007";
    private static final String DOBU13 = "01012020";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72228"})
    @Test(description = "Ariel Onboarding - Sign up DOB Collection", groups = {"Onboarding", "ArielSignUp"})
    public void testSignUpDOBCollectionScreen() {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);

        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        loginPageBase.registerNewEmail(generateNewUserEmail());
        createPasswordPageBase.submitNewPassword(NEW_PASSWORD);

        sa.assertFalse(dobCollectionPageBase.isNonEntitledDOBCollectionDisplayed().containsValue(false),
                "Not Entitled DOB Collection Page Elements Are Not All Displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72232"})
    @Test(description = "Ariel Onboarding - Sign up - Minor Block - Sent to CCS Screen", groups = {"Onboarding", "ArielSignUp"})
    public void testSignUpMinorBlockCCS() {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusContactCustomerServicePageBase contactCustomerServicePageBase = initPage(DisneyPlusContactCustomerServicePageBase.class);
        DisneyPlusHelpCenterPageBase helpCenterPageBase = initPage(DisneyPlusHelpCenterPageBase.class);

        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        loginPageBase.registerNewEmail(generateNewUserEmail());
        createPasswordPageBase.submitNewPassword(NEW_PASSWORD);
        dobCollectionPageBase.clickDOBEditText();
        dobCollectionPageBase.submitDOBValue(DOBU18);

        sa.assertTrue(contactCustomerServicePageBase.isTitleDisplayed(),
                "DOB18: CSS Title is not displayed.");

        contactCustomerServicePageBase.clickHelpCenter();

        sa.assertTrue(helpCenterPageBase.getURLText(),
                "DOB18: Help Center URL is incorrect");

        androidUtils.get().pressBack();
        contactCustomerServicePageBase.clickDismiss();

        sa.assertTrue(welcomePageBase.isOpened(),
                "DOBU18: Welcome Screen was not opened after clicking DISMISS");

        welcomePageBase.proceedToSignUp();
        loginPageBase.registerNewEmail(generateNewUserEmail());
        createPasswordPageBase.submitNewPassword(NEW_PASSWORD);
        dobCollectionPageBase.clickDOBEditText();
        dobCollectionPageBase.submitDOBValue(DOBU13);

        sa.assertTrue(contactCustomerServicePageBase.isTitleDisplayed(),
                "DOBU13: CSS Title is not displayed.");

        contactCustomerServicePageBase.clickHelpCenter();

        sa.assertTrue(helpCenterPageBase.getURLText(),
                "DOBU13: Help Center URL is incorrect");

        androidUtils.get().pressBack();

        contactCustomerServicePageBase.clickDismiss();

        sa.assertTrue(welcomePageBase.isOpened(),
                "DOBU13: Welcome Screen was not opened after clicking DISMISS");

        sa.assertAll();
    }
}
