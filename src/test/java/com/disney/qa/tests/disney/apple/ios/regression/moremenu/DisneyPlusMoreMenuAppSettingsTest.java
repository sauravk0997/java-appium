package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.ios_settings.NetworkHandler;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.SIXTY_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMoreMenuAppSettingsTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String customAppSettingLabel = "%s, %s ";
    private static final String VALUE = "value";
    private static final String AVATAR = "Avatar: The Way of Water";

    public void onboard() {
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(
                disneyPlusMoreMenuIOSPageBase.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS)).click();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75249"})
    @Test(description = "Cellular Data Usage Page UI test", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyCellularDataUsagePageUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard();
        String cellOption = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_USAGE.getText());
        String[] usage = disneyPlusMoreMenuIOSPageBase.getTypeCellLabelContains(cellOption).getText().split(",");
        String cellularDataUsage = usage[1].trim();
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(cellOption).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(cellOption).isElementPresent(),
                "Cellular Data Usage header was not present");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "Back Arrow was not present");

        String automaticLabel = String.format(customAppSettingLabel
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_AUTOMATIC.getText())
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_AUTOMATIC_BODY.getText()));
        String saveDataLabel = String.format(customAppSettingLabel
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_SAVE_DATA.getText())
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_SAVE_DATA_BODY.getText()));

        ExtendedWebElement automatic = disneyPlusMoreMenuIOSPageBase.getElementTypeCellByLabel(automaticLabel);
        sa.assertTrue(automatic.isElementPresent(), "XMOBQA-61201 - Automatic selection was not displayed");

        ExtendedWebElement saveData = disneyPlusMoreMenuIOSPageBase.getElementTypeCellByLabel(saveDataLabel);
        sa.assertTrue(saveData.isElementPresent(), "XMOBQA-61201 - Save Data selection was not displayed");

        String automaticValue = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CELLULAR_DATA_AUTOMATIC.getText());

        if (cellularDataUsage.equals(automaticValue)) {
            sa.assertTrue(automatic.getAttribute(VALUE).equals(CHECKED), "Automatic option is not checked by default");
            sa.assertTrue(saveData.getAttribute(VALUE).equals(UNCHECKED), "Save option is checked by default");
            disneyPlusMoreMenuIOSPageBase.getElementTypeCellByLabel(saveDataLabel).click();

            sa.assertTrue(automatic.isElementPresent() && automatic.getAttribute(VALUE).equals(UNCHECKED),
                    "XMOBQA-61207 - Selecting 'Save Data' did not uncheck 'Automatic' value");

            sa.assertTrue(saveData.isElementPresent() && saveData.getAttribute(VALUE).equals(CHECKED),
                    "XMOBQA-61207 - Selecting 'Save Data' did not update its checked value");
        } else {
            sa.assertTrue(automatic.getAttribute(VALUE).equals(UNCHECKED), "Automatic option is checked by default");
            sa.assertTrue(saveData.getAttribute(VALUE).equals(CHECKED), "Save Data option is not checked by default");
            disneyPlusMoreMenuIOSPageBase.getElementTypeCellByLabel(automaticLabel).click();

            sa.assertTrue(saveData.isElementPresent() && saveData.getAttribute(VALUE).equals(UNCHECKED),
                    "XMOBQA-61207 - Selecting 'Automatic' did not uncheck 'Save Data' value");

            sa.assertTrue(automatic.isElementPresent() && automatic.getAttribute(VALUE).equals(CHECKED),
                    "XMOBQA-61207 - Selecting 'Automatic' did not update its checked value");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66623"})
    @Test(description = "Wi-Fi Data Usage Page UI test", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyWiFiDataUsageUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard();
        String wifiOption = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_DATA_USAGE.getText());
        disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(wifiOption).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(wifiOption).isElementPresent(),
                "Wi-Fi Data Usage header was not present");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "Back Arrow was not present");

        String automaticLabel = String.format(customAppSettingLabel
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_AUTOMATIC.getText())
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_AUTOMATIC_BODY.getText()));
        String saveDataLabel = String.format(customAppSettingLabel
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_SAVE_DATA.getText())
                ,getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_SAVE_DATA_BODY.getText()));

        ExtendedWebElement automatic = disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(automaticLabel);
        sa.assertTrue(automatic.isElementPresent() && automatic.getAttribute(VALUE).equals(CHECKED),
                "Automatic selection was not displayed or is not checked by default");

        ExtendedWebElement saveData = disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(saveDataLabel);
        sa.assertTrue(saveData.isElementPresent() && saveData.getAttribute(VALUE).equals(UNCHECKED),
                "Save Data selection was not displayed or is checked by default");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66627"})
    @Test(description = "Verify that the user can only stream on Wi-Fi when Wi-Fi Only is selected", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyWiFiOnlyVideoPlayback() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);

        onboard();
        NetworkHandler networkHandler = new NetworkHandler();
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
                            .getStaticTextByLabel(getLocalizationUtils()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_REQUIRED_TITLE.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'WiFi Required' popup title was not displayed");

            sa.assertTrue(disneyPlusDetailsIOSPageBase
                            .getStaticTextByLabel(getLocalizationUtils()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_STREAMING_ONLY_MESSAGE.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'WiFi Required' descriptive text was not displayed");

            sa.assertTrue(disneyPlusDetailsIOSPageBase
                            .getDynamicAccessibilityId(getLocalizationUtils()
                                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'App Settings' button was not displayed");

            sa.assertTrue(disneyPlusDetailsIOSPageBase.getDynamicAccessibilityId(getLocalizationUtils()
                            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DISMISS_BTN.getText()))
                            .isElementPresent(),
                    "XMOBQA-61211 - 'Dismiss' button was not displayed");

            disneyPlusHomeIOSPageBase.getDynamicAccessibilityId(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DISMISS_BTN.getText())).click();

            sa.assertTrue(disneyPlusDetailsIOSPageBase.isOpened(),
                    "XMOBQA-61229/61231 - The user did not stay on the Media page after dismissing the modal");

            disneyPlusDetailsIOSPageBase.clickPlayButton();
            disneyPlusHomeIOSPageBase.getDynamicAccessibilityId(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText())).click();

            sa.assertTrue(disneyPlusMoreMenuIOSPageBase.getStaticTextByLabel(getLocalizationUtils()
                            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText())).isElementPresent()
                            && initPage(DisneyPlusEditProfileIOSPageBase.class).getBackArrow().isElementPresent(),
                    "XMOBQA-61235 - User was not redirected to App Settings from Modal navigation");
        } finally {
            networkHandler.toggleWifi(IOSUtils.ButtonStatus.ON);

            sa.assertAll();
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66641", "XMOBQA-66647"})
    @Test(description = "Download Quality Settings UI Elements and Navigation test", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadQualitySettingsUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard();

        String cellOption = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_QUALITY_TITLE.getText());
        moreMenu.getStaticTextByLabel(cellOption).click();
        sa.assertTrue(moreMenu.getStaticTextByLabel(cellOption).isElementPresent(),
                "XMOBQA-61217 - 'Video Quality' header was not present");
        sa.assertTrue(moreMenu.getBackArrow().isElementPresent(),
                "XMOBQA-61217 - Back Arrow was not present");

        String highQuality = String.format(customAppSettingLabel, moreMenu.findTitleLabel(0).getText(),
                moreMenu.findSubtitleLabel(0).getText());
        String mediumQuality = String.format(customAppSettingLabel, moreMenu.findTitleLabel(1).getText(),
                moreMenu.findSubtitleLabel(1).getText());
        String lowQuality = String.format(customAppSettingLabel, moreMenu.findTitleLabel(2).getText(),
                moreMenu.findSubtitleLabel(2).getText());
        List<String> options = Arrays.asList(highQuality, mediumQuality, lowQuality);
        options.forEach(option -> sa.assertTrue(moreMenu.getStaticCellByLabel(option).isElementPresent(),
                String.format("XMOBQA-61219 - '%s' option was not present", option)));

        options.forEach(optionEnabled -> {
            try {
                String enabledShorthand = StringUtils.substringBefore(optionEnabled, ",");
                LOGGER.info("Enabling: '{}'", enabledShorthand);
                moreMenu.getStaticCellByLabel(optionEnabled).click();
                sa.assertTrue(moreMenu.getStaticCellByLabel(optionEnabled).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(CHECKED),
                        String.format("XMOBQA-61221 - '%s' was not enabled after selection", optionEnabled));
                options.forEach(optionDisabled -> {
                    String disabledShorthand = StringUtils.substringBefore(optionDisabled, ",");
                    if (!disabledShorthand.equals(enabledShorthand)) {
                        sa.assertTrue(moreMenu.getStaticCellByLabel(optionDisabled).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals(UNCHECKED),
                                String.format("XMOBQA-61221 - '%s' was not disabled after selection of '%s'", disabledShorthand, enabledShorthand));
                    }
                });
            } catch (NoSuchElementException e) {
                LOGGER.debug("An expected option was not present. Continuing with other options");
            }
        });

        moreMenu.getBackArrow().click();
        sa.assertTrue(moreMenu.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText())).isElementPresent(),
                "User was not returned to the More Menu after closing Video Quality submenu");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67284"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.SMOKE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAppSettingsDefaultUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        onboard();

        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.APP_SETTINGS_TITLE.getText()))
                        .isElementPresent(),
                "App Settings header was not present");
        sa.assertTrue(moreMenuPage.getBackArrow().isElementPresent(),
                "Back Arrow was not present");
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_PLAYBACK_TITLE.getText()))
                        .isElementPresent(),
                "'Video Playback' section header was not present");
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_SETTINGS_TITLE.getText()))
                        .isElementPresent(),
                "'Downloads' section header was not present");
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.STREAM_WIFI_ONLY.getText()))
                        .isElementPresent(),
                "'Stream over Wi-Fi only' cell was not present");
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_DATA_USAGE.getText()))
                        .isElementPresent(),
                "'Wi-Fi Data Usage' cell was not present");
        String cellOption = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_USAGE.getText());
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(cellOption).isElementPresent(),
                "'Cellular Data Usage' cell was not present");
        String[] usage = moreMenuPage.getTypeCellLabelContains(cellOption).getText().split(",");
        String automatic = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CELLULAR_DATA_AUTOMATIC.getText());
        String saveData = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CELLULAR_SAVE_DATA.getText());
        String cellularDataUsage = usage[1].trim();
        sa.assertTrue(Arrays.asList(automatic, saveData).contains(cellularDataUsage),
                String.format("Cellular data default was not set to either %s or %s", automatic, saveData));
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_WIFI_ONLY.getText()))
                        .isElementPresent(),
                "'Download Over Wi-Fi Only' cell was not present");
        sa.assertTrue(moreMenuPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_QUALITY_TITLE.getText()))
                        .isElementPresent(),
                "'Video Quality' label was not present");
        ExtendedWebElement videoQualityCell = moreMenuPage.getTypeCellLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_QUALITY_TITLE.getText()));
        String selectedVideoQuality = videoQualityCell.getText().split(",")[1].trim();
        String standardQualityLabel = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,DictionaryKeys.VIDEO_QUALITY_LABEL_STANDARD.getText());
        String mediumQualityLabel = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,DictionaryKeys.VIDEO_QUALITY_LABEL_MEDIUM.getText());
        String highQualityLabel = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,DictionaryKeys.VIDEO_QUALITY_LABEL_HIGH.getText());
        List<String> expectedVideoQualityLabels = Arrays.asList(standardQualityLabel, mediumQualityLabel, highQualityLabel);
        sa.assertTrue(expectedVideoQualityLabels.contains(selectedVideoQuality),
                String.format("'Video Quality' selected value (%s) was not one of the following: %s",
                        selectedVideoQuality, expectedVideoQualityLabels));
        sa.assertTrue(moreMenuPage.getDeleteAllDownloadsCell().isPresent(),
                "'Delete All Downloads' cell was not present");
        sa.assertFalse(moreMenuPage.isDeleteDownloadsEnabled(),
                "'Delete All Downloads' cell was enabled");
        sa.assertTrue(moreMenuPage.isDeviceStorageCorrectlyDisplayed(),
                "'Device Storage' cell was not present");

        moreMenuPage.getBackArrow().click();
        Assert.assertTrue(moreMenuPage.isOpened(),
                "User was not redirected to More Menu screen");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75466"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeleteAllDownloadsSetting() {
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        String seasonNumber = "1";
        String episodeNumber = "1";
        String expectedAppStorageSize = "0 kB";
        onboard();
        Assert.assertFalse(moreMenuPage.isDeleteDownloadsEnabled(),
                "'Delete All Downloads' cell was enabled");

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));

        //Download one episode
        detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(SIXTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);

        detailsPage.clickMoreTab();
        moreMenuPage.clickDeleteAllDownloads();
        moreMenuPage.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_DELETE_BTN.getText()))
                .click();

        Assert.assertTrue(moreMenuPage.getDeleteAllDownloadsCell().isPresent(),
                "User was not redirected to More Menu screen");
        Assert.assertFalse(moreMenuPage.isDeleteDownloadsEnabled(),
                "'Delete All Downloads' cell was enabled");
        Assert.assertTrue(moreMenuPage.getAppStorageLabel().getText().contains(expectedAppStorageSize),
                String.format("Device storage was: '%s' when it was expected to be '%s'",
                        moreMenuPage.getAppStorageLabel().getText(), expectedAppStorageSize));

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.isDownloadsEmptyHeaderPresent(),
                "Downloads empty header is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61233"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAppSettingsUIDownloadsUpdates() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.getHomeNav().click();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(AVATAR);
        searchPage.getDynamicAccessibilityId(AVATAR).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.startDownload();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS)).click();

        sa.assertFalse(moreMenu.isDownloadOverWifiEnabled(),
                "XMOBQA-61233 - 'Download Over Wi-Fi Only' was not disabled during download");

        sa.assertTrue(moreMenu.isDeleteDownloadsEnabled(),
                "XMOBQA-62425 - 'Delete All Downloads' cell was not properly displayed");

        moreMenu.clickDeleteAllDownloads();

        boolean modalDisplayed = moreMenu.areAllDeleteModalItemsPresent();
        sa.assertTrue(modalDisplayed,
                "XMOBQA-62429 - Modal Items are not all properly displayed");

        if(modalDisplayed) {
            moreMenu.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText())).click();
            sa.assertTrue(moreMenu.isDeleteDownloadsEnabled(),
                    "XMOBQA-62427 - 'Delete All Downloads' did not remain enabled after cancel operation");
        } else {
            sa.fail("XMOBQA-62429 - Modal Items are not all properly displayed");
        }

        moreMenu.clickDeleteAllDownloads();
        if(moreMenu.areAllDeleteModalItemsPresent()) {
            moreMenu.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DELETE_DOWNLOADS_DELETE_BTN.getText())).click();

            sa.assertFalse(moreMenu.isDeleteDownloadsEnabled(),
                    "XMOBQA-62425 - 'Delete All Downloads' cell as not disabled after confirming deletion");
        }

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72183"})
    @Test(groups = {TestGroup.MORE_MENU, US})
    public void verifyAppSettingsDownloadSectionNotVisibleForAdTierUser() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAppSettingsIOSPageBase appSettingsPage = initPage(DisneyPlusAppSettingsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        SoftAssert sa = new SoftAssert();
        onboard();
        appSettingsPage.waitForAppSettingsPageToOpen();

        sa.assertTrue(appSettingsPage.getDownloadSettingsTitle().isElementNotPresent(SHORT_TIMEOUT),
                "Download Settings section title is present");
        sa.assertTrue(appSettingsPage.getDownloadWifiOnlyLabel().isElementNotPresent(SHORT_TIMEOUT),
                "Download Over Wi-Fi Only label is present");
        sa.assertTrue(appSettingsPage.getVideoQualityLabel().isElementNotPresent(SHORT_TIMEOUT),
                "Video Quality label is present");
        sa.assertTrue(appSettingsPage.getDeleteAllDownloadsButtonLabel().isElementNotPresent(SHORT_TIMEOUT),
                "Delete All Downloads button is present");
        sa.assertFalse(moreMenu.isDeviceStorageCorrectlyDisplayed(),
                "Device Storage is displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75469"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAlertWhenUserDeletesAllDownloads() {
        DisneyPlusAppSettingsIOSPageBase appSettingsPage = initPage(DisneyPlusAppSettingsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        //Download a title
        homePage.getHomeNav().click();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(AVATAR);
        searchPage.getDynamicAccessibilityId(AVATAR).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.startDownload();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS))
                .click();
        appSettingsPage.waitForAppSettingsPageToOpen();
        moreMenu.clickDeleteAllDownloads();
        Assert.assertTrue(moreMenu.areAllDeleteModalItemsPresent(),
                "Delete All Downloads alert was not properly displayed");

        moreMenu.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CANCEL_BTN_NORMAL.getText()))
                .click();
        Assert.assertTrue(moreMenu.isDeleteDownloadsEnabled(),
                "'Delete All Downloads' cell did not remain enabled after cancel operation");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66653"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyInternalStorageUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        onboard();
        Assert.assertTrue(moreMenuPage.getDeviceStorageTitle().isPresent(),
                "Device storage title was not present");

        sa.assertTrue(moreMenuPage.getUsedStorageLabel().isPresent(),
                "'Used' storage label was not present");
        sa.assertTrue(moreMenuPage.getAppStorageLabel().isPresent(),
                "'Disney+' storage label was not present");
        sa.assertTrue(moreMenuPage.getFreeStorageLabel().isPresent(),
                "'Free' storage label was not present");
        sa.assertTrue(moreMenuPage.isStorageSizeStringValid(moreMenuPage.getUsedStorageLabel().getText()),
                "'Used' storage label string format was not valid");
        sa.assertTrue(moreMenuPage.isStorageSizeStringValid(moreMenuPage.getAppStorageLabel().getText()),
                "'Disney+' storage label string format was not valid");
        sa.assertTrue(moreMenuPage.isStorageSizeStringValid(moreMenuPage.getFreeStorageLabel().getText()),
                "'Free' storage label string format was not valid");
        sa.assertAll();
    }
}
