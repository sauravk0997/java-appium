package com.disney.qa.tests.disney.apple.ios.regression.basic;

import com.disney.alice.AliceDriver;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusTabletBasicTest extends DisneyBaseTest {

	private static final String TEST_USER = "test_user";
	private static final String ADULT_DOB = "1988-1-1";


	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62651"})
	@Test(description = "App Opens to Welcome Screen", groups = {"Smoke"})
	public void testAppLaunch(){
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
		handleAlert();

		DisneyPlusWelcomeScreenIOSPageBase paywallPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

		softAssert.assertTrue(paywallPageBase.isSignUpButtonDisplayed(),
				"Expected: Sign Up button should be present");

		softAssert.assertTrue(paywallPageBase.isLogInButtonDisplayed(),
				"Expected: Log In button should be present");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62653"})
	@Test(description = "Log In - Verify Display", groups = {"Smoke"})
	public void testLogInScreen() {
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
		handleAlert();

		initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
		initPage(DisneyPlusApplePageBase.class).dismissKeyboardIfIpadLandscapeDetected();

		DisneyPlusLoginIOSPageBase loginPageBase = initPage(DisneyPlusLoginIOSPageBase.class);

		softAssert.assertTrue(loginPageBase.isBackArrowDisplayed(),
				"Expected: Back Arrow should be present");

		softAssert.assertTrue(loginPageBase.isEmailFieldDisplayed(),
				"Expected: Email field should be present");

		softAssert.assertTrue(loginPageBase.isPrimaryButtonPresent(),
				"Expected: Continue button should be present");

		softAssert.assertTrue(loginPageBase.isSignUpButtonDisplayed(),
				"Expected: Sign Up button should be present");

		softAssert.assertTrue(loginPageBase.isDisneyLogoDisplayed(),
				"Expected: Disney+ logo image should be displayed");

		softAssert.assertTrue(loginPageBase.isLoginTextDisplayed(),
				"Expected: 'Log in with your email' text should be displayed");

		softAssert.assertTrue(loginPageBase.isNewToDPlusTextDisplayed(),
				"Expected: 'New to Disney+?' text should be displayed");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62655"})
	@Test(description = "Paywall is shown to unentitled user after Log In", groups = {"Smoke"})
	public void testLoginWithUnentitledAccount() {
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
		handleAlert();
		initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
		login(disneyAccountApi.get().createAccount("US", "en"));

		DisneyPlusWelcomeScreenIOSPageBase paywallPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

		softAssert.assertTrue(paywallPageBase.isLogOutButtonDisplayed(),
				"Expected: 'Log out' button should be present");

		softAssert.assertTrue(paywallPageBase.isCompleteSubscriptionButtonDisplayed(),
				"Expected: 'Complete Subscription' button should be present");

		paywallPageBase.clickCompleteSubscriptionButton();

		softAssert.assertTrue(paywallPageBase.isCancelButtonDisplayed(),
				"Expected: 'Cancel' button should be present");

		softAssert.assertTrue(paywallPageBase.isMonthlySubButtonDisplayed(),
				"Expected: Monthly Subscription button should be present");

		softAssert.assertTrue(paywallPageBase.isYearlySubButtonDisplayed(),
				"Expected: Yearly Subscription button should be present");

		softAssert.assertTrue(paywallPageBase.isRestoreButtonDisplayed(),
				"Expected: Restore button should be present");

		paywallPageBase.logOutFromUnentitledAccount();

		softAssert.assertTrue(paywallPageBase.isOpened(), "Expected: After logging out, main paywall page should be opened");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62657"})
	@Test(description = "Log In with entitled account lands on Home/Discover", groups = {"Smoke"})
	public void testLoginWithEntitledAccount() {
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
		handleAlert();

		loginToHome(disneyAccount.get());
		softAssert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
				"Expected: Home Screen should be opened");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62659"})
	@Test(description = "Verify Home/Discover", groups = {"Smoke"})
	public void testHome() {
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
		setAppToHomeScreen(disneyAccount.get());

        softAssert.assertTrue(disneyPlusHomeIOSPageBase.getHomeNav().isElementPresent(),
                "Expected: Home button should be present in nav bar");

		disneyPlusHomeIOSPageBase.getHomeNav().click();

        softAssert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(),
                "Expected: Home Screen should be opened");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62661"})
	@Test(description = "Verify Search", groups = {"Smoke"})
	public void testSearch() {
		setGlobalVariables();
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
		setAppToHomeScreen(disneyAccount.get());

        softAssert.assertTrue(disneyPlusSearchIOSPageBase.getSearchNav().isElementPresent(),
                "Expected: Search button should be present in nav bar");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);

        softAssert.assertTrue(disneyPlusSearchIOSPageBase.isOpened(),
                "Expected: Search/Explore page should be opened");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62663"})
	@Test(description = "Verify Downloads", groups = {"Smoke"})
	public void testDownloads() {
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
		setAppToHomeScreen(disneyAccount.get());

		softAssert.assertTrue(isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS),
				"Expected: Downloads button should be present in nav bar");

		navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);

		softAssert.assertTrue(initPage(DisneyPlusDownloadsIOSPageBase.class).isOpened(),
				"Expected: Downloads page should be opened");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62665"})
	@Test(description = "Verify More Menu", groups = {"Smoke"})
	public void testMoreMenu() {
		setGlobalVariables();
		SoftAssert softAssert = new SoftAssert();
		setAppToHomeScreen(disneyAccount.get());

		softAssert.assertTrue(isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs.MORE_MENU),
				"Expected: More button should be present in nav bar");

		navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
		DisneyPlusMoreMenuIOSPageBase morePage = initPage(DisneyPlusMoreMenuIOSPageBase.class);

		softAssert.assertTrue(morePage.isOpened(), "Expected: More Menu page should be opened");

		for (DisneyPlusMoreMenuIOSPageBase.MoreMenu menuItem : DisneyPlusMoreMenuIOSPageBase.MoreMenu.values()) {
			softAssert.assertTrue(morePage.isMenuOptionPresent(menuItem),
					"Expected: " + menuItem + " option should be present");
		}
		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62667"})
	@Test(description = "Verify Edit Profile Page", groups = {"Smoke"})
	public void testEditProfile() {
		setGlobalVariables();
		AliceDriver aliceDriver = new AliceDriver(getDriver());
		SoftAssert softAssert = new SoftAssert();
		setAppToHomeScreen(disneyAccount.get());
		navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
		DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

		softAssert.assertTrue(moreMenu.isEditProfilesBtnPresent(),
				"Expected - Edit Profiles button should be present in More Menu page");

		moreMenu.clickEditProfilesBtn();

		DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);

		softAssert.assertTrue(editProfile.isBackBtnPresent(),
				"Expected - Back button should be present in Edit Profiles page");

		softAssert.assertTrue(editProfile.isEditProfilesTitlePresent(),
				"Expected - 'Edit Profiles' title should be present in Edit Profiles page");

		pause(3);
		aliceDriver.screenshotAndRecognize().assertLabelContainsCaptionCaseInsensitive(softAssert, disneyAccount.get().getFirstName(), "round_tile", "round_tile_hovered");

		aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(softAssert, "Add Profile", "round_tile", "round_tile_hovered");

		softAssert.assertTrue(editProfile.clickBackBtn().isOpened(),
				"Expected - Back button should user to More Menu page");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62669"})
	@Test(description = "Verify Changing Profiles", groups = {"Smoke"})
	public void testChangingProfiles() {
		setGlobalVariables();
		disneyAccountApi.get().addProfile(disneyAccount.get(), TEST_USER, ADULT_DOB, disneyAccount.get().getProfileLang(), null, false, true);
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
		setAppToHomeScreen(disneyAccount.get(), TEST_USER);
		navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusWhoseWatchingIOSPageBase.clickProfile(disneyAccount.get().getFirstName());

		softAssert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
				"Expected - Home page should be opened after selecting non-active profile");

		softAssert.assertAll();
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62671"})
	@Test(description = "Verify Video Playback", groups = {"Smoke"})
	public void testVideoPlayback() {
		setGlobalVariables();
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        initPage(DisneyPlusHomeIOSPageBase.class).getSearchNav().click();
        disneyPlusSearchIOSPageBase.searchForMedia("Wall-E");

        for (ExtendedWebElement media : disneyPlusSearchIOSPageBase.getDisplayedTitles()) {
            media.click();
            if (disneyPlusDetailsIOSPageBase.isMediaPremierAccessLocked()) {
                disneyPlusSearchIOSPageBase.getBackArrow().click();
            } else {
                LOGGER.info("Title is not premier locked. Proceeding");
                break;
            }
        }

        disneyPlusDetailsIOSPageBase.dismissAlert();
        disneyPlusDetailsIOSPageBase.clickPlayButton();

        Assert.assertTrue(initPage(DisneyPlusVideoPlayerIOSPageBase.class).isOpened(),
                "Expected - Video player should be opened");
    }

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62673"})
	@Test(description = "Verify Video Player Controls", groups = {"Smoke"}, enabled = false)
	public void testVideoPlayerControls() {
		//Test not implemented. Video player not visible due to DRM
	}

	@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62675"})
	@Test(description = "Verify Video Player Back Nav", groups = {"Smoke"}, enabled = false)
	public void testVideoPlayerBackNav() {
		//Test not implemented. Video player not visible due to DRM
	}
}
