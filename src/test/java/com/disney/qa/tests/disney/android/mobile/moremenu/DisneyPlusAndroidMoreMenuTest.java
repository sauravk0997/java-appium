package com.disney.qa.tests.disney.android.mobile.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.tests.disney.android.mobile.videoplayer.DisneyPlusAndroidVideoPlayerNoAdsTest.DISNEY_PROD_MOVIES_DEEPLINK;
import static com.disney.qa.tests.disney.android.mobile.videoplayer.DisneyPlusAndroidVideoPlayerNoAdsTest.DISNEY_PROD_SERIES_DEEPLINK;

/**
 * Test logic in this class are not specific to any particular
 * region and can be executed freely in any locale
 */
public class DisneyPlusAndroidMoreMenuTest extends BaseDisneyTest {
    private static final String SERIES_DUCK_TAILS_DEEPLINK = "/ducktales/tc6CG7H7lhCE";
    private static final String SERIES_DUCK_TAILS_TITLE = "DuckTales";
    private static final String SERIES_SIMPSONS_DEEPLINK = "/the-simpsons/3ZoBZ52QHb4x";
    private static final String MOVIE_DOCTOR_STRANGE_DEEPLINK = "/marvel-studios-doctor-strange/4GgMJ1aHKHA2";
    private static final String MOVIE_DOCTOR_STRANGE_TITLE = "Marvel Studios' Doctor Strange";
    private static final String SIMPSONS_SERIES_NAME = "The Simpsons";
    private static final String RESTRICTED_PROFILE_NAME = "Restricted Profile";
    private static final String DISNEY_HELP_URL = "help.disneyplus.com";
    private static final String FAKE_PASSWORD = "fakePassword";
    private static final String OVER18 = "03311985";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68442"})
    @Test(description = "Watchlist {Empty} - Verify UI and Return to More Menu", groups = {"More Menu"})
    public void testCheckEmptyWatchlistDisplay() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        String title = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.WatchlistItems.TITLE.getText());

        SoftAssert sa = new SoftAssert();

        signIn();
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.WATCHLIST.getText()));

        sa.assertTrue(moreMenuPageBase.isHeaderPresent(title),
                title + " header not displayed");

        sa.assertTrue(moreMenuPageBase.isBackButtonPresent(),
                "Back arrow not displayed");

        sa.assertTrue(moreMenuPageBase.isEmptyWatchlistIconPresent(),
                "Empty list icon not displayed");

        sa.assertTrue(moreMenuPageBase.isWatchlistEmptyTextTitlePresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.WatchlistItems.EMPTY_SHORT.getText())),
                "Watchlist empty title text not displayed");

        sa.assertTrue(moreMenuPageBase.isWatchlistEmptyTextBodyPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.WatchlistItems.EMPTY_LONG.getText())),
                "Watchlist empty description text not displayed");

        moreMenuPageBase.pressBackButton();

        sa.assertTrue(moreMenuPageBase.isOpened(),
                "More menu not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71632"})
    @Test(description = "Add series to watchlist then remove", groups = {"More Menu"})
    public void testCheckWatchlistSeriesFunctions() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        SoftAssert sa = new SoftAssert();

        signIn();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCK_TAILS_DEEPLINK);
        String contentTitle = mediaPageBase.getMediaTitle();

        sa.assertTrue(mediaPageBase.doesToggleGraphicUpdate(),
                "Watchlist UI button did not update");

        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.WATCHLIST.getText()));

        sa.assertTrue(moreMenuPageBase.isWatchlistTitleVisible(SERIES_DUCK_TAILS_TITLE),
                SERIES_DUCK_TAILS_TITLE + " not present in watchlist");

        moreMenuPageBase.clickFirstAvailableProfilePoster();

        sa.assertTrue(mediaPageBase.doesToggleGraphicUpdate(),
                "Watchlist UI button did not update");

        sa.assertTrue(mediaPageBase.isContentTitleVisible(contentTitle),
                "Content title does not match added content " + contentTitle);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68448"})
    @Test(description = "Navigate to Movie Detail Page and Add to Watchlist then Remove", groups = {"More Menu"})
    public void testCheckWatchlistMovieFunctions() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        SoftAssert sa = new SoftAssert();

        signIn();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_DOCTOR_STRANGE_DEEPLINK);
        String contentTitle = mediaPageBase.getMediaTitle();

        sa.assertTrue(mediaPageBase.doesToggleGraphicUpdate(),
                "Watchlist UI button did not update");

        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.WATCHLIST.getText()));

        sa.assertTrue(moreMenuPageBase.isWatchlistTitleVisible(MOVIE_DOCTOR_STRANGE_TITLE),
                MOVIE_DOCTOR_STRANGE_TITLE + " not present in watchlist");

        moreMenuPageBase.clickFirstAvailableProfilePoster();

        sa.assertTrue(mediaPageBase.doesToggleGraphicUpdate(),
                "Watchlist UI button did not update");

        sa.assertTrue(mediaPageBase.isContentTitleVisible(contentTitle),
                "Content title does not match added content " + contentTitle);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67284"})
    @Test(description = "App Settings - Verify UI and Return to More Menu", groups = {"More Menu"})
    public void testCheckAppSettingsSubMenuDisplay() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        //Video Section items
        String streamOverWifi = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.STREAM_OVER_WIFI.getText());
        String wifiDataUsage = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.WIFI_DATA_USAGE.getText());
        String cellularDataUsage = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.CELLULAR_DATA.getText());

        //Download Section items
        String videoPlayback = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.VIDEO_PLAYBACK.getText()).toUpperCase();
        String downloads = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.DOWNLOADS_TITLE.getText()).toUpperCase();
        String downloadLocation = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.DOWNLOAD_LOCATION.getText());
        String downloadOverWifi = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.WIFI_SETTING.getText());
        String downloadQuality = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.DOWNLOAD_QUALITY.getText());
        String defaultQualitySetting = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.DEFAULT_QUALITY.getText());
        String delete = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.DELETE_DOWNLOADS.getText());

        String appSettingsTitle = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AppSettingsList.APP_SETTINGS_TITLE.getText());

        signIn();

        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.SETTINGS.getText()));

        sa.assertTrue(moreMenuPageBase.isBackButtonPresent(),
                "Back Arrow not visible");

        sa.assertTrue(moreMenuPageBase.isHeaderPresent(appSettingsTitle),
                appSettingsTitle + " header not visible");

        sa.assertTrue(moreMenuPageBase.isSectionHeaderPresent(videoPlayback),
                videoPlayback + " not visible");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(videoPlayback, streamOverWifi),
                streamOverWifi + " not visible in " + videoPlayback + " section");

        sa.assertTrue(moreMenuPageBase.isOptionTogglePresent(streamOverWifi),
                streamOverWifi + " not visible");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(videoPlayback, wifiDataUsage),
                wifiDataUsage + " not visible in " + videoPlayback + " section");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(videoPlayback, cellularDataUsage),
                cellularDataUsage + " not visible in " + videoPlayback + " section");

        sa.assertTrue(moreMenuPageBase.isSectionHeaderPresent(downloads),
                downloads + " not visible");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(downloads, downloadOverWifi),
                downloadOverWifi + " not visible in " + downloads + " section");

        sa.assertTrue(moreMenuPageBase.isOptionTogglePresent(downloadOverWifi),
                downloadOverWifi + " is not visible");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(downloads, downloadQuality),
                downloadQuality + " not visible in " + downloads + " section");

        sa.assertTrue(moreMenuPageBase.isOptionSettingVisible(downloadQuality, defaultQualitySetting),
                defaultQualitySetting + " and " + downloadQuality + " is not visible");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(downloads, downloadLocation),
                downloadLocation + " not visible in " + downloads + " section");

        sa.assertTrue(moreMenuPageBase.isSectionOptionPresent(downloads, delete),
                delete + " not visible in " + downloads + " section");

        sa.assertTrue(moreMenuPageBase.isAppSettingsStorageHeaderProperlyDisplayed(moreMenuPageBase.getSettingsStorageLocationText(languageUtils.get())),
                moreMenuPageBase.getSettingsStorageLocationText(languageUtils.get()) + " not visible");

        sa.assertTrue(moreMenuPageBase.isAppSettingsStorageGraphDisplayed(),
                "Storage graph not visible");

        moreMenuPageBase.pressBackButton();

        sa.assertTrue(moreMenuPageBase.isOpened(),
                "More menu not in open state");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67128"})
    @Test(description = "Verify account and more menu screen", groups = {"More Menu"})
    public void testCheckAccountDisplay() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);

        String account = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.ACCOUNT.getText());
        String account_details = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AccountList.ACCOUNT_DETAILS.getText()).toUpperCase();
        String subscription = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AccountList.SUBSCRIPTION.getText()).toUpperCase();
        String hiddenPassword = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AccountList.HIDDEN_PASSWORD.getText());

        SoftAssert sa = new SoftAssert();

        signIn();
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(account);

        sa.assertTrue(moreMenuPageBase.isHeaderPresent(account),
                account + " header is not visible");

        sa.assertTrue(moreMenuPageBase.isBackButtonPresent(),
                "Back arrow not visible");

        sa.assertTrue(moreMenuPageBase.isSectionHeaderPresent(account_details),
                account_details + " not visible");

        sa.assertTrue(moreMenuPageBase.isSectionHeaderPresent(subscription),
                subscription + " not visible");

        sa.assertTrue(moreMenuPageBase.isUserCredentialDisplayed(disneyAccount.get().getEmail(), true),
                "User's email address not visible");

        sa.assertTrue(moreMenuPageBase.checkEditButtonVisibility(disneyAccount.get().getEmail(), true),
                "Edit button not visible");

        sa.assertTrue(moreMenuPageBase.isUserCredentialDisplayed(hiddenPassword, false),
                "Encoded password length is not visible");

        sa.assertTrue(moreMenuPageBase.checkEditButtonVisibility(hiddenPassword, false),
                "Edit button is not visible");

        sa.assertTrue(moreMenuPageBase.isAccountCtaTextVisible(),
                "Account CTA not visible");

        moreMenuPageBase.pressBackButton();

        sa.assertTrue(moreMenuPageBase.isOpened(),
                "More menu screen not visible");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68063"})
    @Test(description = "Verify legal screen and return to more menu", groups = {"More Menu"})
    public void testCheckLegalDisplay() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);

        String legal = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.LEGAL.getText());

        SoftAssert sa = new SoftAssert();

        signIn();
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(legal);

        sa.assertTrue(moreMenuPageBase.isHeaderPresent(legal),
                legal + " header is not displayed");

        sa.assertTrue(moreMenuPageBase.isBackButtonPresent(),
                "Back button not displayed");

        moreMenuPageBase.validateLegalScreenItems(sa);
        moreMenuPageBase.pressBackButton();

        sa.assertTrue(moreMenuPageBase.isOpened(),
                "More menu screen not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67286"})
    @Test(description = "Verify screen and more menu screen", groups = {"More Menu"})
    public void testCheckHelpWebView() {
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);

        SoftAssert sa = new SoftAssert();

        signIn();
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.HELP.getText()));

        sa.assertTrue(moreMenuPageBase.isWebviewClosePresent(),
                "Webview close button not displayed");

        sa.assertTrue(moreMenuPageBase.doesWebviewOpenCorrectAddress(DISNEY_HELP_URL),
                "Incorrect URL displayed");

        androidUtils.get().pressBack();

        sa.assertTrue(moreMenuPageBase.isOpened(),
                "More menu screen not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67288"})
    @Test(description = "Verify Modal if user has Downloads", groups = {"More Menu"})
    public void testCheckLogoutWithDownloads() {
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusMediaPageBase disneyPlusMediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusContentMetadataPageBase contentMetadataPageBase = initPage(DisneyPlusContentMetadataPageBase.class);

        String logoutText = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.LOGOUT.getText());

        SoftAssert sa = new SoftAssert();

        signIn();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_SIMPSONS_DEEPLINK);
        disneyPlusMediaPageBase.downloadSeriesSeasonsOverTwentyEpisodes(
                20,
                false,
                SIMPSONS_SERIES_NAME,
                sa,
                languageUtils.get(),
                searchApi.get(),
                disneyAccount.get(),
                contentMetadataPageBase);
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(logoutText);

        sa.assertTrue(moreMenuPageBase.isLogoutModalPresent(),
                "Logout modal not displayed");

        sa.assertTrue(moreMenuPageBase.isHeaderPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.LOGOUT_CONFIRMATION.getText())),
                "Confirmation header not displayed");

        sa.assertTrue(moreMenuPageBase.isHeaderPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.LOGOUT_WITH_DOWNLOADS.getText())),
                "Explanation text not displayed");

        moreMenuPageBase.cancelLogout();

        sa.assertFalse(moreMenuPageBase.isLogoutModalPresent(),
                "Logout modal is still displayed");

        sa.assertTrue(moreMenuPageBase.isOpened(),
                "User to remain in the More Menu");

        moreMenuPageBase.selectMenuItem(logoutText);
        moreMenuPageBase.confirmLogout();

        sa.assertTrue(initPage(DisneyPlusWelcomePageBase.class).isOpened(),
                "Welcome screen not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67290"})
    @Test(description = "Verify modal if user has no Downloads", groups = {"More Menu"})
    public void testCheckLogoutWithoutDownloads() {
        DisneyPlusWelcomePageBase disneyPlusWelcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);

        SoftAssert sa = new SoftAssert();

        signIn();
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.selectMenuItem(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.LOGOUT.getText()));

        sa.assertTrue(disneyPlusWelcomePageBase.isOpened(),
                "Welcome page not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71637"})
    @Test(description = "Verify debug and more menu screen", groups = {"More Menu"})
    public void testCheckDebugMenu() {
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);

        String debugText = DisneyPlusMoreMenuPageBase.MenuList.DEBUG.getText();

        signIn();
        disneyPlusCommonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));

        SoftAssert sa = new SoftAssert();

        if (moreMenuPageBase.isHeaderPresent(debugText)) {
            moreMenuPageBase.selectMenuItem(debugText);

            sa.assertTrue(moreMenuPageBase.isBackButtonPresent(),
                    "Back arrow not displayed");

            sa.assertTrue(moreMenuPageBase.isHeaderPresent("General"),
                    "Debug section header not displayed");

            moreMenuPageBase.pressBackButton();

            sa.assertTrue(moreMenuPageBase.isOpened(),
                    "More menu screen not displayed");

            sa.assertAll();
        } else {
            skipExecution("Debug menu not available on this build.");
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69535"})
    @Test(description = "Verify restricted profile creation toggle functions", groups = {"Smoke", "More Menu"})
    public void testCheckRestrictedProfileCreation() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusAccountPageBase accountPageBase = initPage(DisneyPlusAccountPageBase.class);
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        SoftAssert sa = new SoftAssert();

        signIn();
        commonPageBase.navigateToPage(
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));

        moreMenuPageBase.selectMenuItem(
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.ACCOUNT.getText()));

        Assert.assertFalse(accountPageBase.isRestrictProfileCreationSwitchEnabled(),
                "Profile creation is already in enabled state");

        accountPageBase.clickOnGenericTextElement(
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.RESTRICT_PROFILE_CREATION_TITLE.getText()));

        loginPageBase.logInWithPassword(disneyAccount.get().getUserPass());

        Assert.assertTrue(accountPageBase.isRestrictProfileCreationSwitchEnabled(),
                "Restrict profile creation switch is disabled state");

        commonPageBase.navigateToPage(
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));

        moreMenuPageBase.clickAddProfileButton();
        loginPageBase.logInWithPassword(FAKE_PASSWORD);

        sa.assertTrue(maturityPageBase.isPasswordErrorTextDisplayed(),
                "Password error text not displayed");

        moreMenuPageBase.clickBackButton();
        moreMenuPageBase.addArielNewProfileFromMoreMenu(
                RESTRICTED_PROFILE_NAME,
                true,
                false,
                OVER18,
                loginPageBase,
                disneyAccount.get(),
                androidUtils.get());

        moreMenuPageBase.fullCatalogLogIn(
                false,
                disneyAccount.get(),
                commonPageBase,
                loginPageBase,
                languageUtils.get());

        sa.assertTrue(disneyPlusCommonPageBase.isTextViewStringDisplayed(RESTRICTED_PROFILE_NAME),
                RESTRICTED_PROFILE_NAME + " is not displayed");

        sa.assertAll();
    }

    private void signIn() {
        login(disneyAccount.get(), true);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "Discover page not displayed");
    }
}
