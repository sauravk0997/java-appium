package com.disney.qa.tests.disney.web.commerce.portability;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusPortabilityPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommercePortabilitySmokeTest extends DisneyPlusBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "CY - User has portability and enters app in EU country where we haven't launched", groups = {"US", TestGroup.DISNEY_SMOKE})
    public void portabilityNonLaunch() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByAppVersion(LIVE);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "en-GB", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "CY"));

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup(locale, CODE, COUNTRY, "CY");
        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());

        sa.assertTrue(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL does not contain `EU`, url: %s", portabilityPage.getCurrentUrl()));
        sa.assertTrue(portabilityPage.getCurrentUrl().contains("login"),
                String.format("URL does not contain `login`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());

        sa.assertTrue(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL does not contain `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        sa.assertTrue(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL does not contain `EU`, url: %s", portabilityPage.getCurrentUrl()));
        sa.assertTrue(portabilityPage.getCurrentUrl().contains("home"),
                String.format("URL does not contain `home`, url: %s", portabilityPage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "ES - User has portability and enters app in EU country where we have launched ", groups = {"US", TestGroup.DISNEY_SMOKE})
    public void portabilityLaunch() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "en-GB", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "ES"));
        getAccountApi().patchAccountAttributeForLocation(disneyAccount.get(), "NL", PatchType.ACCOUNT);

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup(locale, CODE, COUNTRY, "ES");
        String getHomeUrl = portabilityPage.getCurrentUrl();
        sa.assertFalse(getHomeUrl.contains("eu"),
                String.format("Disney + URL contains eu in URL, url: %s", getHomeUrl));

        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        sa.assertTrue(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL does not contain `EU`, url: %s", portabilityPage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "DE - User can grant themself portability within their home ", groups = {"US", TestGroup.DISNEY_SMOKE})
    public void portabilityGrant() throws URISyntaxException, JSONException, IOException {
        skipTestByEnv(QA);
        DisneyAccountApi api = getAccountApi();

        SoftAssert sa = new SoftAssert();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country("DE").language("en-GB").patchLocations(false).build();
        com.disney.qa.api.pojos.DisneyAccount account = api.createAccount(request);
        api.entitleAccount(account, api.lookupOfferToUse("DE", "Yearly"), "V2-ORDER");
        api.patchStarOnboardingStatus(account, true);
        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup(locale, CODE, COUNTRY, "DE");
        String getHomeUrl = portabilityPage.getCurrentUrl();
        sa.assertFalse(getHomeUrl.contains("eu"),
                String.format("Disney + URL contains eu in URL, url: %s", getHomeUrl));

        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(account.getEmail());

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(account.getUserPass());

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        LOGGER.info("Navigating to /portability.....");

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/en-gb/portability"));
        portabilityPage.isPortabilityTitleTextPresent();

        sa.assertTrue(portabilityPage.isPortabilityTitleTextPresent(),
                String.format("Portability title text not present on: %s", portabilityPage.getCurrentUrl()));
        sa.assertTrue(portabilityPage.isPortabilityBodyPresent(),
                String.format("Portability body not present on: %s", portabilityPage.getCurrentUrl()));
        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        sa.assertTrue(commercePage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is not clickable");

        sa.assertAll();
    }

    @Test(description = "NL - User can't grant themself portability outside their home", groups = {"US", TestGroup.DISNEY_SMOKE})
    public void portabilityBlockedGrant() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "en-GB", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "US"));

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup(locale, CODE, COUNTRY, "US");
        String getHomeUrl = portabilityPage.getCurrentUrl();
        sa.assertFalse(getHomeUrl.contains("eu"),
                String.format("Disney + URL contains eu in URL, url: %s", getHomeUrl));

        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));

        LOGGER.info("Navigating to /portability.....");

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/en-gb/portability"));
        portabilityPage.isPortabilityTitleTextPresent();

        sa.assertTrue(portabilityPage.isPortabilityTitleTextPresent(),
                String.format("Portability title text not present on: %s", portabilityPage.getCurrentUrl()));
        sa.assertTrue(portabilityPage.isPortabilityBodyPresent(),
                String.format("Portability body not present on: %s", portabilityPage.getCurrentUrl()));
        sa.assertFalse(portabilityPage.getCurrentUrl().contains("eu"),
                String.format("URL contains `EU`, url: %s", portabilityPage.getCurrentUrl()));
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        sa.assertFalse(commercePage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is clickable");

        sa.assertAll();
    }


}
