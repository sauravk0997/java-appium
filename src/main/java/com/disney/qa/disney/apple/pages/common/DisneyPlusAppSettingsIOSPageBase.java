package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAppSettingsIOSPageBase extends DisneyPlusApplePageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ExtendedWebElement pageTitle = getDynamicAccessibilityId(getLocalizationUtils()
            .getDictionaryItem(
                    DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.APP_SETTINGS_TITLE.getText()));

    public DisneyPlusAppSettingsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean waitForAppSettingsPageToOpen() {
        LOGGER.info("Waiting for App Settings page to load");
        return fluentWait(getDriver(), TWENTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "App Settings page is not opened")
                .until(it -> pageTitle.isPresent(THREE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getDownloadSettingsTitle() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DOWNLOADS_SETTINGS_TITLE.getText()));
    }

    public ExtendedWebElement getDownloadWifiOnlyLabel() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DOWNLOAD_WIFI_ONLY.getText()));
    }

    public ExtendedWebElement getVideoQualityLabel() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.VIDEO_QUALITY_TITLE.getText()));
    }

    public ExtendedWebElement getDeleteAllDownloadsButtonLabel() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.BTN_DELETE_ALL_DOWNLOADS.getText()));
    }

    public ExtendedWebElement getHighQualityTitle() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                        APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_HIGH_TITLE.getText()));
    }

    public ExtendedWebElement getHighQualitySubCopy() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_HIGH_COPY.getText()));
    }

    public ExtendedWebElement getMediumQualityTitle() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_MEDIUM_TITLE.getText()));
    }

    public ExtendedWebElement getMediumQualitySubCopy() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_MEDIUM_COPY.getText()));
    }

    public ExtendedWebElement getStandardQualityTitle() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_STANDARD_TITLE.getText()));
    }

    public ExtendedWebElement getStandardQualitySubCopy() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, DictionaryKeys.DOWNLOAD_QUALITY_STANDARD_COPY.getText()));
    }
}
