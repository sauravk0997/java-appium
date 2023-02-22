package com.disney.qa.tests.disney.web.appex.globalNav;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseNavPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusCreateProfilePage;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusNavProfileSwitcherTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyPlusBaseNavPage> baseNavPage = new ThreadLocal<>();
    private static final ThreadLocal<com.disney.qa.api.pojos.DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();
    private static final ThreadLocal<DisneyPlusCreateProfilePage> createProfilePage = new ThreadLocal<>();
    private static final ThreadLocal<DisneyPlusBaseProfileViewsPage> baseProfileViewsPage = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        baseNavPage.set(new DisneyPlusBaseNavPage(getDriver()));
        createProfilePage.set(new DisneyPlusCreateProfilePage(getDriver()));
        baseProfileViewsPage.set(new DisneyPlusBaseProfileViewsPage(getDriver()));
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19213"})
    @Test(description = "Active profile displayed", groups = {"US", "GB", "ES", "CA", "NL", TestGroup.DISNEY_APPEX})
    public void globalNavProfileActiveDisplayed() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        baseNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        baseNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        baseNavPage.get().getdPlusBaseNavAccountDropdown().hover();

        String activeProfileAssert = baseNavPage.get().getActiveProfileAsString();

        sa.assertEquals(activeProfileAssert, ProfileConstant.MAIN_TEST,
                String.format("Default Profile not selected, Expected: Profile, Actual: %s",
                        activeProfileAssert));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19215", "XWEBQAS-31380"})
    @Test(description = "Switching Profile", groups = {"US", TestGroup.DISNEY_APPEX, TestGroup.ARIEL_APPEX})
    public void globalNavProfileSwitchProfile() throws MalformedURLException, JSONException, URISyntaxException, InterruptedException {
        SoftAssert sa = new SoftAssert();


        baseNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));
        getAccountApi().addProfile(disneyAccount.get(), "Vader", language, null, false);

        baseNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        baseProfileViewsPage
                .get().clickOnPrimaryProfileOnEditPage();
        createProfilePage
                .get().hoverOnProfile();

        String primaryProfile = createProfilePage.get().getActiveProfileTextAttribute();

        sa.assertEquals(primaryProfile, ProfileConstant.MAIN_TEST, "Primary Profile not selected after login");

        createProfilePage
                .get().hoverOnProfile();
        baseProfileViewsPage
                .get().clickOnDropDownProfileFirst();
        waitForSeconds(5);
        sa.assertTrue(createProfilePage.get().getCurrentUrl().contains("/update-profile"), "Expected to see update-profile page");
        createProfilePage
                .get().clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, true, true, isMobile());
        //Need to force a sleep here for text attribute to update
        pause(3);

        String secondaryProfile = createProfilePage.get().getActiveProfileTextAttribute();

        sa.assertEquals(secondaryProfile, "Vader", "Secondary Profile not selected after switch");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19217"})
    @Test(description = "Adding Profile", groups = {"US", TestGroup.DISNEY_APPEX})
    public void globalNavProfileAddingProfile() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        baseNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        baseNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        createProfilePage.get().createNewProfile("Test2");
        createProfilePage.get().isSecondaryProfileFromEditProfilePageVisible();
        createProfilePage.get().clickOnSecondaryProfileOnEditPage();
        new DisneyPlusCommercePage(getDriver()).handleMatureContentOnboardingFlow(true);

        createProfilePage.get().hoverOnProfile();

        String secondaryProfile = createProfilePage.get().getActiveProfileTextAttribute();

        sa.assertEquals(secondaryProfile, "Test2", "Secondary Profile not selected after switch");

        sa.assertAll();
    }

}
