package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;

public class DisneyPlusMoreMenuAppSettingsTest extends DisneyBaseTest {

    private final String customAppSettingLabel = "%s, %s ";
    private static final String VALUE = "value";
    private static final String DINNER_IS_SERVED = "Dinner Is Served";

    public void onboard() {
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(disneyAccount.get());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61201", "XMOBQA-61207"})
    @Test(description = "Cellular Data Usage Page UI test")
    public void verifyCellularDataUsagePageUI() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard();
        String cellOption = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_USAGE.getText());
        disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(cellOption).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(cellOption).isElementPresent(),
                "Cellular Data Usage header was not present");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "Back Arrow was not present");

        String automaticLabel = String.format(customAppSettingLabel
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_AUTOMATIC.getText())
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_AUTOMATIC_BODY.getText()));
        String saveDataLabel = String.format(customAppSettingLabel
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_SAVE_DATA.getText())
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_SAVE_DATA_BODY.getText()));

        ExtendedWebElement automatic = disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(automaticLabel);
        sa.assertTrue(automatic.isElementPresent() && automatic.getAttribute(VALUE).equals(CHECKED),
                "XMOBQA-61201 - Automatic selection was not displayed or is not checked by default");

        ExtendedWebElement saveData = disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(saveDataLabel);
        sa.assertTrue(saveData.isElementPresent() && saveData.getAttribute(VALUE).equals(UNCHECKED),
                "XMOBQA-61201 - Save Data selection was not displayed or is checked by default");

        disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(saveDataLabel).click();
        sa.assertTrue(automatic.isElementPresent() && automatic.getAttribute(VALUE).equals(UNCHECKED),
                "XMOBQA-61207 - Selecting 'Save Data' did not uncheck 'Automatic' value");

        sa.assertTrue(saveData.isElementPresent() && saveData.getAttribute(VALUE).equals(CHECKED),
                "XMOBQA-61207 - Selecting 'Save Data' did not update its checked value");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61203"})
    @Test(description = "Wi-Fi Data Usage Page UI test")
    public void verifyWiFiDataUsageUI() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard();
        String wifiOption = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_DATA_USAGE.getText());
        disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(wifiOption).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(wifiOption).isElementPresent(),
                "Wi-Fi Data Usage header was not present");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "Back Arrow was not present");

        String automaticLabel = String.format(customAppSettingLabel
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_AUTOMATIC.getText())
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_AUTOMATIC_BODY.getText()));
        String saveDataLabel = String.format(customAppSettingLabel
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_SAVE_DATA.getText())
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_SAVE_DATA_BODY.getText()));

        ExtendedWebElement automatic = disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(automaticLabel);
        sa.assertTrue(automatic.isElementPresent() && automatic.getAttribute(VALUE).equals(CHECKED),
                "Automatic selection was not displayed or is not checked by default");

        ExtendedWebElement saveData = disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(saveDataLabel);
        sa.assertTrue(saveData.isElementPresent() && saveData.getAttribute(VALUE).equals(UNCHECKED),
                "Save Data selection was not displayed or is checked by default");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61211", "XMOBQA-61229", "XMOBQA-61231", "XMOBQA-61235"})
    @Test(description = "Verify that the user can only stream on Wi-Fi when Wi-Fi Only is selected", enabled = false)
    public void verifyWiFiOnlyVideoPlayback() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);

        onboard();
        IOSUtils utils = new IOSUtils();
        IOSUtils.NetworkHandler networkHandler = utils.new NetworkHandler();
        try {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.ON);
            relaunch();

            disneyPlusMoreMenuIOSPageBase.toggleStreamOverWifiOnly(IOSUtils.ButtonStatus.ON);
            disneyPlusMoreMenuIOSPageBase.getHomeNav().click();
            disneyPlusHomeIOSPageBase.clickFirstCarouselPoster();
            disneyPlusDetailsIOSPageBase.clickPlayButton();
            Assert.assertTrue(new
                            DisneyPlusVideoPlayerIOSPageBase(getDriver()).isOpened(),
                    "XMOBQA-61211 - Video Player did not open");

            //Giving video player time to register bookmark
            pause(10);

            terminateApp(buildType.getDisneyBundle());
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.OFF);
            relaunch();
            disneyPlusMoreMenuIOSPageBase.getHomeNav().click();
            disneyPlusHomeIOSPageBase.clickFirstCarouselPoster();
            disneyPlusDetailsIOSPageBase.clickPlayButton();

            sa.assertTrue(disneyPlusDetailsIOSPageBase
                            .getStaticTextByLabel(languageUtils.get()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_REQUIRED_TITLE.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'WiFi Required' popup title was not displayed");

            sa.assertTrue(disneyPlusDetailsIOSPageBase
                            .getStaticTextByLabel(languageUtils.get()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_STREAMING_ONLY_MESSAGE.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'WiFi Required' descriptive text was not displayed");

            sa.assertTrue(disneyPlusDetailsIOSPageBase
                            .getDynamicAccessibilityId(languageUtils.get()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'App Settings' button was not displayed");

            sa.assertTrue(disneyPlusDetailsIOSPageBase.getDynamicAccessibilityId(languageUtils.get()
                            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DISMISS_BTN.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'Dismiss' button was not displayed");

            disneyPlusHomeIOSPageBase.getDynamicAccessibilityId(languageUtils.get()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DISMISS_BTN.getText())).click();

            sa.assertTrue(disneyPlusDetailsIOSPageBase.isOpened(),
                    "XMOBQA-61229/61231 - The user did not stay on the Media page after dismissing the modal");

            disneyPlusDetailsIOSPageBase.clickPlayButton();
            disneyPlusHomeIOSPageBase.getDynamicAccessibilityId(languageUtils.get()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText())).click();

            sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(languageUtils.get()
                            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText())).isElementPresent()
                            && initPage(DisneyPlusEditProfileIOSPageBase.class).getBackArrow().isElementPresent(),
                    "XMOBQA-61235 - User was not redirected to App Settings from Modal navigation");
        } finally {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.ON);

            sa.assertAll();
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61213"})
    @Test(description = "Verify functionality when the user enables Download Over Wi-Fi Only", enabled = false)
    public void verifyWiFiOnlyDownloads() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = new DisneyPlusSearchIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = new DisneyPlusDetailsIOSPageBase(getDriver());

        SoftAssert sa = new SoftAssert();
        onboard();
        IOSUtils utils = new IOSUtils();
        IOSUtils.NetworkHandler networkHandler = utils.new NetworkHandler();
        try {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.ON);
            relaunch();
            disneyPlusMoreMenuIOSPageBase.toggleDownloadOverWifiOnly(IOSUtils.ButtonStatus.ON);

            disneyPlusMoreMenuIOSPageBase.getSearchNav().click();
            disneyPlusSearchIOSPageBase.clickMoviesTab();
            pause(3);
            List<ExtendedWebElement> movies = disneyPlusSearchIOSPageBase.getDisplayedTitles();
            for (ExtendedWebElement movie : movies) {
                movie.click();
                if (disneyPlusDetailsIOSPageBase.isMediaPremierAccessLocked()) {
                    disneyPlusSearchIOSPageBase.getBackArrow().click();
                } else {
                    LOGGER.info("Title is not premier locked. Proceeding");
                    break;
                }
            }
            disneyPlusDetailsIOSPageBase.startDownload();
            pause(10);

            sa.assertTrue(disneyPlusDetailsIOSPageBase.isDownloadInProgressDisplayed(languageUtils.get(), 1),
                    "Download did not start while on WiFi");

            networkHandler.toggleWifi(IOSUtils.ButtonStatus.OFF);
            relaunch();

            disneyPlusDetailsIOSPageBase.pauseDownload();

            sa.assertTrue(disneyPlusDetailsIOSPageBase.isDownloadPaused(languageUtils.get()),
                    "Download was not paused after WiFi was disabled");

            disneyPlusDetailsIOSPageBase.removeDownload(languageUtils.get());
            disneyPlusDetailsIOSPageBase.startDownload();

            sa.assertTrue(disneyPlusDetailsIOSPageBase
                            .getStaticTextByLabel(languageUtils.get()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_REQUIRED_TITLE.getText()))
                            .isElementPresent(),
                    "'WiFi Required' popup title was not displayed");
        } finally {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.ON);

            sa.assertAll();
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61215"})
    @Test(description = "Verify functionality when the user disables Download Over Wi-Fi Only", enabled = false)
    public void verifyCarrierDownloads() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusBrandIOSPageBase disneyPlusBrandIOSPageBase = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);

        onboard();
        IOSUtils utils = new IOSUtils();
        IOSUtils.NetworkHandler networkHandler = utils.new NetworkHandler();
        try {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.OFF);
            relaunch();
            disneyPlusMoreMenuIOSPageBase.toggleDownloadOverWifiOnly(IOSUtils.ButtonStatus.OFF);
            disneyPlusMoreMenuIOSPageBase.getHomeNav().click();
            disneyPlusHomeIOSPageBase.clickPixarTile();
            disneyPlusBrandIOSPageBase.clickFirstCarouselPoster();
            disneyPlusDetailsIOSPageBase.startDownload();

            sa.assertTrue(disneyPlusDetailsIOSPageBase.isDownloadInProgressDisplayed(languageUtils.get(), 1),
                    "Download did not start");
        } finally {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.ON);

            sa.assertAll();
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61217", "XMOBQA-61219", "XMOBQA-61221"})
    @Test(description = "Download Quality Settings UI Elements and Navigation test")
    public void verifyDownloadQualitySettingsUI() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        onboard();
        String highQuality = String.format(customAppSettingLabel
                , languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_HIGH_TITLE.getText())
                , languageUtils.get().getValueBeforePlaceholder(languageUtils.get()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_HIGH_BODY.getText())));
        String mediumQuality = String.format(customAppSettingLabel
                , languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_MEDIUM_TITLE.getText())
                , languageUtils.get().getValueBeforePlaceholder(languageUtils.get()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_MEDIUM_BODY.getText())));
        String lowQuality = String.format(customAppSettingLabel
                ,languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_STANDARD_TITLE.getText())
                ,languageUtils.get().getValueBeforePlaceholder(languageUtils.get()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_STANDARD_BODY.getText())));

        List<String> options = Arrays.asList(highQuality, mediumQuality, lowQuality);
        String cellOption = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_QUALITY_TITLE.getText());

        disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(cellOption).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(cellOption).isElementPresent(),
                "XMOBQA-61217 - 'Video Quality' header was not present");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "XMOBQA-61217 - Back Arrow was not present");

        options.forEach(option -> sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(option).isElementPresent(),
                String.format("XMOBQA-61219 - '%s' option was not present", option)));

        options.forEach(optionEnabled -> {
            try {
                String enabledShorthand = StringUtils.substringBefore(optionEnabled, ",");
                LOGGER.info("Enabling: '{}'", enabledShorthand);
                disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(optionEnabled).click();
                sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(optionEnabled).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(CHECKED),
                        String.format("XMOBQA-61221 - '%s' was not enabled after selection", optionEnabled));
                options.forEach(optionDisabled -> {
                    String disabledShorthand = StringUtils.substringBefore(optionDisabled, ",");
                    if (!disabledShorthand.equals(enabledShorthand)) {
                        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(optionDisabled).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(UNCHECKED),
                                String.format("XMOBQA-61221 - '%s' was not disabled after selection of '%s'", disabledShorthand, enabledShorthand));
                    }
                });
            } catch (NoSuchElementException e) {
                LOGGER.debug("An expected option was not present. Continuing with other options");
            }
        });

        disneyPlusMoreMenuIOSPageBase.getBackArrow().click();
        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText())).isElementPresent(),
                "User was not returned to the More Menu after closing Video Quality submenu");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61205", "XMOBQA-61223", "XMOBQA-61225", "XMOBQA-62421", "XMOBQA-62423"})
    @Test(description = "App Settings Page UI test")
    public void verifyAppSettingsDefaultUI() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        onboard();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "Back Arrow is not displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_PLAYBACK_TITLE.getText())).isElementPresent(),
                "XMOBQA-61223 - 'Video Playback' section header was not properly displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_SETTINGS_TITLE.getText())).isElementPresent(),
                "XMOBQA-61223 - 'Downloads' section header was not displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.STREAM_WIFI_ONLY.getText())).isElementPresent(),
                "XMOBQA-61223 - 'Stream over Wi-Fi only' cell was not properly displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_DATA_USAGE.getText())).isElementPresent(),
                "XMOBQA-61223 - 'Wi-Fi Data Usage' cell was not properly displayed");

        String cellOption = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_USAGE.getText());

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(cellOption).isElementPresent(),
                "XMOBQA-61223 - 'Cellular Data Usage' cell was not properly displayed");

        String[] usage = disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(cellOption).getText().split(",");

        sa.assertEquals(usage[1].trim(),  languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_AUTOMATIC.getText()),
                "XMOBQA-61205 - Cellular data default was not set to 'Automatic'");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_WIFI_ONLY.getText())).isElementPresent(),
                "XMOBQA-61223 - 'Download Over Wi-Fi Only' cell was not properly displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicXpathContainsName(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_QUALITY_TITLE.getText())).isElementPresent(),
                "XMOBQA-61223 - 'Video Quality' cell was not properly displayed");

        sa.assertFalse(disneyPlusMoreMenuIOSPageBase.isDeleteDownloadsEnabled(),
                "XMOBQA-62421 - 'Delete All Downloads' cell was not properly displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isDeviceStorageCorrectlyDisplayed(),
                "XMOBQA-61223 - 'Device Storage' cell was not properly displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62425", "XMOBQA-62427", "XMOBQA-62429", "XMOBQA-61233"})
    @Test(description = "App Settings Page UI updates for Downloads test")
    public void verifyAppSettingsUIDownloadsUpdates() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        homePage.getHomeNav().click();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DINNER_IS_SERVED);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.startDownload();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();

        sa.assertTrue(moreMenu.isDeleteDownloadsEnabled(),
                "XMOBQA-62425 - 'Delete All Downloads' cell was not properly displayed");

        sa.assertFalse(moreMenu.isDownloadOverWifiEnabled(),
                "XMOBQA-61233 - 'Download Over Wi-Fi Only' was not disabled during download");

        moreMenu.clickDeleteAllDownloads();

        boolean modalDisplayed = moreMenu.areAllDeleteModalItemsPresent();
        sa.assertTrue(modalDisplayed,
                "XMOBQA-62429 - Modal Items are not all properly displayed");

        if(modalDisplayed) {
            moreMenu.getTypeButtonByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText())).click();
            sa.assertTrue(moreMenu.isDeleteDownloadsEnabled(),
                    "XMOBQA-62427 - 'Delete All Downloads' did not remain enabled after cancel operation");
        } else {
            sa.fail("XMOBQA-62429 - Modal Items are not all properly displayed");
        }

        moreMenu.clickDeleteAllDownloads();
        if(moreMenu.areAllDeleteModalItemsPresent()) {
            moreMenu.getTypeButtonByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_DELETE_BTN.getText())).click();

            sa.assertFalse(moreMenu.isDeleteDownloadsEnabled(),
                    "XMOBQA-62425 - 'Delete All Downloads' cell as not disabled after confirming deletion");
        }

        sa.assertAll();
    }
}
