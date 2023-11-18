package com.disney.qa.tests.disney.apple.ios.regression.basic;

import com.disney.alice.AliceDriver;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class DisneyPlusBasicTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TEST_USER = "test_user";
    private static final String ADULT_DOB = "1988-1-1";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62687"})
    @Test(description = "App Opens to Welcome Screen", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testAppLaunch() {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusWelcomeScreenIOSPageBase paywallPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        softAssert.assertTrue(paywallPageBase.isSignUpButtonDisplayed(),
                "Expected: Sign Up button should be present");

        softAssert.assertTrue(paywallPageBase.isLogInButtonDisplayed(),
                "Expected: Log In button should be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62693"})
    @Test(description = "Log In with entitled account lands on Home/Discover", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testLoginWithEntitledAccount() {
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());

        softAssert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
                "Expected: Home Screen should be opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62695"})
    @Test(description = "Verify Home/Discover", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testHome() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());

        softAssert.assertTrue(isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs.HOME),
                "Expected: Home button should be present in nav bar");

        disneyPlusHomeIOSPageBase.getHomeNav().click();

        softAssert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
                "Expected: Home Screen should be opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62697"})
    @Test(description = "Verify Search", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testSearch() {
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        softAssert.assertTrue(isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs.SEARCH),
                "Expected: Search button should be present in nav bar");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);

        softAssert.assertTrue(initPage(DisneyPlusSearchIOSPageBase.class).isOpened(),
                "Expected: Search/Explore page should be opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62699"})
    @Test(description = "Verify Downloads", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testDownloads() {
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        softAssert.assertTrue(isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS),
                "Expected: Downloads button should be present in nav bar");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);

        softAssert.assertTrue(initPage(DisneyPlusDownloadsIOSPageBase.class).isOpened(),
                "Expected: Downloads page should be opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62701"})
    @Test(description = "Verify More Menu", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testMoreMenu() {
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62703"})
    @Test(description = "Verify Edit Profile Page", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testEditProfile() {
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
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            aliceDriver.screenshotAndRecognize().assertLabelContainsCaptionCaseInsensitive(softAssert, disneyAccount.get().getFirstName(), "round_tile", "round_tile_hovered");

            aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(softAssert, "Add Profile", "round_tile", "round_tile_hovered");
        } else {
            softAssert.assertTrue(disneyAccount.get().getFirstName().contains("Test"),
                    "Expected - First user name should be present in Edit Profile page");
            softAssert.assertTrue(moreMenu.isAddProfileButtonPresent(),
                    "Expected - Add profile button should be present in Edit Profile page");
        }
        softAssert.assertTrue(editProfile.clickBackBtn().isOpened(),
                "Expected - Back button should user to More Menu page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62705"})
    @Test(description = "Verify Changing Profiles", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testChangingProfiles() {
        disneyAccountApi.get().addProfile(disneyAccount.get(), TEST_USER, ADULT_DOB, disneyAccount.get().getProfileLang(), null, false, true);
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        setAppToHomeScreen(disneyAccount.get(), TEST_USER);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoseWatchingPage.clickProfile(disneyAccount.get().getFirstName());

        softAssert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
                "Expected - Home page should be opened after selecting non-active profile");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62707"})
    @Test(description = "Verify Video Playback", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testVideoPlayback() {
        SoftAssert softAssert = new SoftAssert();
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

        softAssert.assertTrue(initPage(DisneyPlusVideoPlayerIOSPageBase.class).isOpened(),
                "Expected - Video player should be opened");

        softAssert.assertAll();
    }
}
