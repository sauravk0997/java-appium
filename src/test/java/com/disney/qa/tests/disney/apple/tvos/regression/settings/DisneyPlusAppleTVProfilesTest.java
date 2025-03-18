package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.qa.api.client.requests.CreateUnifiedAccountProfileRequest;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

public class DisneyPlusAppleTVProfilesTest extends DisneyPlusAppleTVBaseTest {
    private static final String PROFILE_NAME = "Test";
    private static final String whoIsWatchingAssertMessage = "Who is watching page is not open after selecting Profile Name from global nav menu";
    private static final String globalNavMenuAssertMessage = "Global Nav menu is present";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90920"})
    @Test(description = "Profiles - Exit from Who's Watching view", groups = {TestGroup.PROFILES, TestGroup.SMOKE, US})
    public void exitFromWhoseWatching() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.openGlobalNavWithClickingMenu();
        homePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        homePage.clickSelect();
        Assert.assertTrue(whoseWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        homePage.clickMenuTimes(1, 1);
        sa.assertTrue(homePage.isOpened(), "Home page is not open after clicking menu on Profile selection page");

        homePage.openGlobalNavWithClickingMenu();
        homePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        homePage.clickSelect();

        Assert.assertTrue(whoseWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        homePage.clickProfileBtn(PROFILE_NAME);
        Assert.assertTrue(homePage.isOpened(), "Home page is not open after selecting a profile");
        sa.assertFalse(homePage.isGlobalNavPresent(), globalNavMenuAssertMessage);
        homePage.moveLeft(2,1); //stop carousel moving
        homePage.isCarouselFocused();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90924"})
    @Test(description = "Profiles - Exit from Add Profile view", groups = {TestGroup.PROFILES, US})
    public void exitFromAddProfile() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());

        logIn(getUnifiedAccount());

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickAddProfile();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        sa.assertTrue(chooseAvatarPage.isOpened(), "Choose avatar page is not open after clicking add profile");
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page is not open after clicking menu on Choose Avatar screen");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-65772"})
    @Test(description = "Profiles - Exit from Select profile to edit view", groups = {TestGroup.PROFILES, US})
    public void exitFromSelectProfileToEdit() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(getUnifiedAccount());

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();

        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME));
        disneyPlusAppleTVHomePage.clickMenuTimes(2, 1);
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on edit profile page");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90926"})
    @Test(description = "Profiles - Exit from Edit Profile view", groups = {TestGroup.PROFILES, US})
    public void verifyExitFromEditProfileView() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(getUnifiedAccount());
        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);

        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME), "Edit mode profile icon is not present");

        disneyPlusAppleTVHomePage.clickMenuTimes(2, 1);
        Assert.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on edit profile page");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);

        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME), "Edit mode profile icon is not present");
        //Accessibility ID of `Done` button is same as of `edit profile` button.
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page is not open after clicking Done on edit profile page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67258"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.SMOKE, US})
    public void verifyKidsProfileCuratedContent() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        setAccount(getUnifiedAccount());
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        logIn(getUnifiedAccount(), KIDS_PROFILE);
        Assert.assertTrue(homePage.isKidThemeBackgroudUIDisplayed(),
                "UI on home page is not in kid mode theme");
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_only_murders_in_the_building_deeplink"));
        Assert.assertFalse(detailsPage.isOpened(), "Adult content detail page displayed for kid profile");
    }
}
