package com.disney.qa.tests.disney.web.appex.olpSignup;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusOlpSignupTest extends DisneyPlusBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18634"})
    @Test(description = "Welcome Page Review", priority = 1, groups = {TestGroup.STAR_SMOKE,TestGroup.STAR_APPEX, TestGroup.DISNEY_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void welcomePageReview() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1510"));
        SoftAssert sa = new SoftAssert();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        String currentUrl = userPage.getCurrentUrl();

        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        sa.assertTrue(userPage.verifyLoginBtn(),
                "Login Button not Present on " + currentUrl);

        sa.assertTrue(userPage.isSignupCtasPresent(),
                "signup ctas not Present on " + currentUrl);

        sa.assertTrue(userPage.isFooterIdPresent(),
                "Footer not Present on " + currentUrl);

        if (!DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            sa.assertTrue(userPage.getBaseLogo().isElementPresent(), "Logo not Present on " + currentUrl);

                if (productData.searchAndReturnProductData("hasBrandLogos").equalsIgnoreCase("true")) {
                    sa.assertTrue(userPage.isContentBrandsPresent(),
                                "Brand Logos not Present on " + currentUrl);
                }

                if (userPage.isUSCountry(locale)) {
                    userPage.clickDisneyBundleTab();
                    sa.assertTrue(userPage.isSuperBundlePresent(), "Super Bundle not Present on " + currentUrl);
                }
        }

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18634", "XWEBQAS-20070"})
    @Test(description = "Sign Up Review", priority = 2, groups = {TestGroup.STAR_SMOKE,TestGroup.STAR_APPEX,TestGroup.DISNEY_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX", "JP"})
    public void signUpReview() {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1511"));
        SoftAssert sa = new SoftAssert();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();

        sa.assertTrue(userPage.getDssLoginWrapperId().isElementPresent(),
        "Login Wrapper not present on " + userPage.getCurrentUrl());

        sa.assertTrue(userPage.isSignUpLoginContinueBtnPresent(),
                "Sign up title not present on " + userPage.getCurrentUrl());

        new SeleniumUtils(getDriver()).scroll(0,100);

        sa.assertTrue(userPage.getOnboardingStepper().isElementPresent(),
                "First step for onboarding Stepper not Present " + userPage.getCurrentUrl());

        // Sends the logs to the validator and then adds the results to the soft assert
        analyticPause();
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31396", "XWEBQAS-31400"})
    @Test(description = "Verify LionsGate content details page for unauthenticated users", groups = {TestGroup.STAR_APPEX, TestGroup.LIONSGATE_APPEX, "MX"})
    public void verifyDetailsPageForLionsGateUnauthenticatedUser() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        String baseUrl = userPage.getHomeUrl();

        userPage
                .openURL(baseUrl.concat(PageUrl.LIONSGATE_SERIES_TITLE_POWER));
        waitForSeconds(15);
        softAssert
                .assertTrue(userPage.isPremierAccessCtaPresent(), "Expected to see Premier access CTA button");

        softAssert.assertAll();
    }
}
