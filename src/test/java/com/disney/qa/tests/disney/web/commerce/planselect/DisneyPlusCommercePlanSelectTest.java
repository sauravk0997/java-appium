package com.disney.qa.tests.disney.web.commerce.planselect;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusPlansPage;
import com.disney.qa.disney.web.common.DisneyPlusBaseModal;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class DisneyPlusCommercePlanSelectTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @Test(description = "Bundle plan cards", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void verifyBundlePlanCards() throws JSONException {

        SoftAssert sa = new SoftAssert();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52571", "XWEBQAP-52570", "XWEBQAP-52902"));

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        DisneyPlusBaseModal modal = new DisneyPlusBaseModal(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        plansPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        sa.assertTrue(plansPage.getBundleToggleBtnState().equals(WebConstant.TOGGLE_BUTTON_ACTIVE), "Bundle toggle button is not active");
        sa.assertTrue(plansPage.getPlanCardsCount() == 3, "Bundle plan card count is not 3");

        verifyPlanCardElements(sa, plansPage, PlanCardTypes.PlanSelectCard.DISNEY_BUNDLE_HULU_WITH_ADS);
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_BUNDLE_HULU_WITH_ADS);
        
        if(ENVIRONMENT.equalsIgnoreCase(QA)) {
            sa.assertTrue(Pattern.compile(WebConstant.REGEX_HULU_ACCOUNT_QA_URL).matcher(plansPage.collectNewTabLandingPageURL()).find(), "Page url doesn't contain hulu*/account");
        }
        else {
            sa.assertTrue(Pattern.compile(WebConstant.REGEX_HULU_ACCOUNT_URL).matcher(plansPage.collectNewTabLandingPageURL()).find(), "Page url doesn't contain hulu*/account");
        }

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        verifyPlanCardElements(sa, plansPage, PlanCardTypes.PlanSelectCard.DISNEY_BUNDLE_WITH_ADS);
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_BUNDLE_WITH_ADS)
                .isDplusBaseEmailFieldIdPresent();
        plansPage.verifyUrlText(sa, PageUrl.SIGNUP_TYPE_BUNDLE_SASH);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        verifyPlanCardElements(sa, plansPage, PlanCardTypes.PlanSelectCard.DISNEY_BUNDLE_NO_ADS);
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_BUNDLE_NO_ADS)
                .isDplusBaseEmailFieldIdPresent();
        plansPage.verifyUrlText(sa, PageUrl.SIGNUP_TYPE_BUNDLE_NOAH);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        plansPage.clickBundleTermsLink();
        sa.assertTrue(modal.verifyModal("Bundle Terms modal"), "Bundle Terms modal is not open");
        sa.assertTrue(modal.isModalBodyVisible(), "Bundle Terms modal body is not visible");
        modal.clickModalPrimaryButton();
        sa.assertFalse(modal.verifyModal("Bundle Terms modal"), "Bundle Terms modal did not close");

        sa.assertAll();
    }

    @Test(description = "Standalone plan cards", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void verifyStandalonePlanCards() throws JSONException {

        SoftAssert sa = new SoftAssert();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52572"));

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        plansPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        plansPage.clickStandaloneToggleButton();
        Assert.assertTrue(plansPage.isStandalonePlanCardVisible(), "Page not toggled to Standalone plan cards");

        sa.assertTrue(plansPage.getStandaloneToggleBtnState().equals(WebConstant.TOGGLE_BUTTON_ACTIVE), "Standalone toggle button is not active");
        sa.assertTrue(plansPage.getPlanCardsCount() == 2, "Standalone plan card count is not 2");
        sa.assertFalse(plansPage.isBundleLearnMoreLinkVisible(), "Bundle learn more link is visible on standalone page");
        sa.assertFalse(plansPage.isBundleTermsLinkVisible(), "Bundle terms link is visible on standalone page");

        verifyPlanCardElements(sa, plansPage, PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS_PROMO);
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS)
                .verifyUrlText(sa, PageUrl.SIGNUP);

        plansPage.navigateBack();
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        plansPage.clickStandaloneToggleButton();
        Assert.assertTrue(plansPage.isStandalonePlanCardVisible(), "Page not toggled to Standalone plan cards");

        verifyPlanCardElements(sa, plansPage, PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS)
                .verifyUrlText(sa, PageUrl.SIGNUP_TYPE_STANDALONE_NO_ADS);

        sa.assertAll();
    }

    @Test(description = "Plan card deeplinks", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void verifyPlanCardDeeplinks() throws JSONException {

        SoftAssert sa = new SoftAssert();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52573"));

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        plansPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS + PageUrl.QPS_DEFAULT_BUNDLE + PageUrl.QPS_TOGGLE_UI_SHOWN));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");
        sa.assertTrue(plansPage.getBundleToggleBtnState().equals(WebConstant.TOGGLE_BUTTON_ACTIVE), "Bundle toggle button is not active");
        sa.assertTrue(plansPage.isStandaloneToggleVisible(), "Standalone toggle is not visible");
        sa.assertTrue(plansPage.getPlanCardsCount() == 3, "Bundle plan card count is not 3");

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS + PageUrl.QPS_DEFAULT_BUNDLE + PageUrl.QPS_TOGGLE_UI_HIDDEN));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");
        sa.assertFalse(plansPage.isBundleToggleVisible(), "Bundle toggle button is visible");
        sa.assertFalse(plansPage.isStandaloneToggleVisible(), "Standalone toggle is visible");
        sa.assertTrue(plansPage.getPlanCardsCount() == 3, "Bundle plan card count is not 3");

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS + PageUrl.QPS_DEFAULT_STANDALONE + PageUrl.QPS_TOGGLE_UI_SHOWN));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");
        sa.assertTrue(plansPage.getStandaloneToggleBtnState().equals(WebConstant.TOGGLE_BUTTON_ACTIVE), "Standalone toggle button is not active");
        sa.assertTrue(plansPage.isBundleToggleVisible(), "Standalone toggle is not visible");
        sa.assertTrue(plansPage.getPlanCardsCount() == 2, "Standalone plan card count is not 3");

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS + PageUrl.QPS_DEFAULT_STANDALONE + PageUrl.QPS_TOGGLE_UI_HIDDEN));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");
        sa.assertFalse(plansPage.isBundleToggleVisible(), "Bundle toggle button is visible");
        sa.assertFalse(plansPage.isStandaloneToggleVisible(), "Standalone toggle is visible");
        sa.assertTrue(plansPage.getPlanCardsCount() == 2, "Bundle plan card count is not 2");

        sa.assertAll();
    }

    @Test(description = "Lapsed user sees plan select page in restart subscription flow", groups = { "US", TestGroup.DISNEY_COMMERCE,
            TestGroup.ARIEL_COMMERCE })
    public void lapsedUserSeesPlanSelect() throws JSONException, IOException, URISyntaxException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52888"));

        WebDriver driver = getDriver();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(
                AccountUtils.createExpiredAccountWithoutDob(locale, language, SUB_VERSION_V1));
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickSignUpNowBtn();
        Assert.assertTrue(plansPage.verifyPage(), "Plans page didn't load");
    }

    private void verifyPlanCardElements(SoftAssert sa, DisneyPlusPlansPage plansPage, PlanCardTypes.PlanSelectCard planCardType) {
        sa.assertTrue(plansPage.getPlanLogoAltTxt(planCardType).equals(planCardType.getPlanLogoAltTxt()), "Plan logo alt text is wrong");
        sa.assertTrue(plansPage.getPlanTitle(planCardType).equals(planCardType.getPlanTitle()), "Plan card title is wrong");
        sa.assertTrue(plansPage.getPlanSubCopy(planCardType).equals(planCardType.getPlanSubCopy()), "Plan card sub copy is wrong");
        sa.assertTrue(plansPage.getPlanPrice(planCardType).contains(planCardType.getPlanPrice()), "Price is wrong");
        sa.assertTrue(plansPage.getPlanBodyCopyLength(planCardType) > 0, "Plan body has no copy");
    }
}
