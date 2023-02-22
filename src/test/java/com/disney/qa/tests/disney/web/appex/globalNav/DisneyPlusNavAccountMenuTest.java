package com.disney.qa.tests.disney.web.appex.globalNav;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.common.DisneyPlusBaseNavPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusCreateProfilePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONException;
import org.openqa.selenium.Dimension;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusNavAccountMenuTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyPlusBaseNavPage>  dNavPage = new ThreadLocal<>();
    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    private static final ThreadLocal<DisneyPlusCreateProfilePage> dProfilePage = new ThreadLocal<>();
    private static final ThreadLocal<DisneyPlusBaseProfileViewsPage> dViewPage = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        dNavPage.set(new DisneyPlusBaseNavPage(getDriver()));
        dProfilePage.set(new DisneyPlusCreateProfilePage(getDriver()));
        dViewPage.set(new DisneyPlusBaseProfileViewsPage(getDriver()));
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19665"})
    @Test(description = "Design Review", priority = 1, groups = {"US", "GB", "ES", "CA" , "NL", TestGroup.DISNEY_APPEX })
    public void globalNavAccountMenuDesignReview() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        dProfilePage.get().hoverOnProfile();

        sa.assertTrue(dProfilePage.get().isAccountDropdownAddProfileVisible(), "Add Profile button not visible after account hover");
        sa.assertTrue(dProfilePage.get().isEditProfileBtnVisible(), "Edit Profile button not visible after account hover");
        sa.assertTrue(dProfilePage.get().isAccountDropdownAppSettingsVisible(), "App Settings button not visible after account hover");
        sa.assertTrue(dProfilePage.get().isAccountDropdownAccountBtnVisible(), "Account button not visible after account hover");
        sa.assertTrue(dProfilePage.get().isAccountDropdownLogoutVisible(), "Log Out button not visible after account hover");
        sa.assertTrue(dProfilePage.get().isAccountDropdownHelpVisible(), "Help button not visible after account hover");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20418"})
    @Test(description = "App Header Displayed for All Sections", priority = 2, groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavAccountDisplayAll() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(), String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        dNavPage.get().clickNavSearchBtn();

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(), String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        dNavPage.get().clickNavWatchListBtn();

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(), String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        dNavPage.get().clickNavOriginalsBtn();

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(), String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        dNavPage.get().clickNavMoviesBtn();

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(), String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        dNavPage.get().clickNavSeriesBtn();

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(), String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20420"})
    @Test(description = "Sticky on scroll", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME})
    public void globalNavAccountStickyOnScroll() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        SeleniumUtils util = new SeleniumUtils(getDriver());
        Dimension dAssert = new Dimension(0, 0);

        dNavPage.get().waitForPageToFinishLoading();

        dNavPage.get().waitFor(dNavPage.get().getDplusBaseSlickList());

        Dimension homepageDimension = util.getPageOffsets();

        sa.assertEquals(dAssert, homepageDimension,
                String.format("Page Offset not at %s on webapp homepage. Actual: %s", dAssert, homepageDimension));

        util.scrollToBottom();

        sa.assertTrue(dNavPage.get().isAppNavHeaderVisible(),
        String.format("App Nav header not visible on %s", dNavPage.get().getCurrentUrl()));

        sa.assertNotEquals(util.getPageOffsets(), dAssert,
                "Global Nav Did not scroll to bottom for assertion");

        sa.assertAll();
    }
}
