package com.disney.qa.tests.disney.apple.ios.aqa.iap;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.json.simple.JSONArray;
import com.disney.qa.hora.validationservices.EventChecklist;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;

import com.zebrunner.agent.core.annotation.TestLabel;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusIAPAnalyticsTest extends DisneyBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XAQA-2910"})
    @Test(description = "Standard purchase with a new account for Premium monthly sku", groups = {TestGroup.PRE_CONFIGURATION, US})
    public void testPurchaseFlowAnalytics() {
        setBuildType();
        if (buildType != BuildType.IAP) {
            LOGGER.info("buildtype {}",buildType);
            skipExecution("Test run is not against IAP compatible build.");
        }
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusAccountIsMinorIOSPageBase accountIsMinorPage = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
                .country("US").isStarOnboarded(false).build();
        setAccount(getAccountApi().createAccount(accountRequest));

        DisneyAccount account = getAccount();
        addHoraValidationSku(account);
        handleAlert();
        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(account.getEmail());
        passwordPage.submitPasswordForLogin(account.getUserPass());
        welcomeScreen.clickCompleteSubscriptionButton();

        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);

        paywallIOSPageBase.clickBasicPlanButton();
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton();
        paywallIOSPageBase.waitForSubscribeOverlay();


        paywallIOSPageBase.clickOverlaySubscribeButton();

        try {
            paywallIOSPageBase.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }

        acceptAlert();
        pause(10);
        acceptAlert();

        initPage(DisneyPlusWhoseWatchingIOSPageBase.class).isOpened();
        EventChecklist item1 = new EventChecklist("urn:dss:event:fed:purchase:completed-v2");
        checkList.add(item1);
        pause(5);
        checkAssertions(sa,account.getAccountId(),checkList);
    }
}
