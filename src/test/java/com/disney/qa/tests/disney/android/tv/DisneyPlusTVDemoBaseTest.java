package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusTVDemoBaseTest extends DisneyPlusAndroidTVBaseTest {

    @Test
    public void appLaunch() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);

        JsonNode appDictionary = new DisneyApiProvider().getFullDictionaryBody(language);

        sa.assertTrue(welcomePageBase.isOpened(),
                "Expected - Welcome Text to be present");

        sa.assertTrue(welcomePageBase.isSignUpButtonPresent(),
                "Expected - Sign Up button to be present");

        if (welcomePageBase.isPurchaseAvailable(appDictionary)) {
            sa.assertTrue(welcomePageBase.isLoginButtonPresent(),
                    "Expected - Log In button to present");
        } else {
            sa.assertFalse(welcomePageBase.isLoginButtonPresent(),
                    "Expected - Sign Up button to replace text with Log In button");
        }

        sa.assertAll();
    }
}
