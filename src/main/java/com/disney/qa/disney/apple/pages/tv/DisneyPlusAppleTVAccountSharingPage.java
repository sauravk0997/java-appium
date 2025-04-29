package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVAccountSharingPage extends DisneyPlusApplePageBase {

    public DisneyPlusAppleTVAccountSharingPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOOHSoftBlockScreenHeadlinePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_SOFT_BLOCK_HEADLINE.getText())).isPresent();
    }

    public boolean isOOHSoftBlockScreenSubCopyPresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_SOFT_BLOCK_SUBCOPY.getText())).isPresent();
    }

    public boolean isOOHSoftBlockScreenSubCopyTwoPresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_SOFT_BLOCK_SUBCOPY_2.getText())).isPresent();
    }

    public ExtendedWebElement getOOHSoftBlockContinueButton() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_CONTINUE_CTA.getText()));
    }

    public ExtendedWebElement getOOHLogOutButton() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_LOGOUT_CTA.getText()));
    }

    public boolean isOOHVerifyDeviceHeadlinePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_VERIFY_DEVICE_HEADLINE.getText())).isPresent();
    }

    public boolean isOOHVerifyDeviceSubCopyPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_VERIFY_DEVICE_SUBCOPY.getText())).isPresent();
    }

    public boolean isOOHVerifyDeviceSubCopyTwoPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_VERIFY_DEVICE_SUBCOPY_2.getText())).isPresent();
    }

    public ExtendedWebElement getOOHVerifyDeviceButton() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_VERIFY_DEVICE_OTP_CTA.getText()));
    }

    public ExtendedWebElement getOOHVerifyDeviceDismissButton() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_VERIFY_DEVICE_DISMISS_CTA.getText()));
    }

    public boolean isOOHEnterOtpPagePresent() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CHECK_EMAIL_TITLE.getText())).isPresent();
    }

    public boolean isOOHConfirmationHeadlinePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_CONFIRMATION_HEADLINE.getText())).isPresent();
    }

    public ExtendedWebElement getOOHConfirmationPageCTA() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_CONFIRMATION_CTA.getText()));
    }

    public boolean isOOHHardBlockScreenHeadlinePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_HARD_BLOCK_HEADLINE.getText())).isPresent();
    }

    public ExtendedWebElement getOOHIAmAwayFromHomeCTA() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_CTA.getText()));
    }

    public boolean isOOHTravelModeScreenHeadlinePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_HEADLINE.getText())).isPresent();
    }

    public boolean isOOHTravelModeScreenSubCopyPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_SUB_COPY.getText())).isPresent();
    }

    public ExtendedWebElement getOOHTravelModeOTPCTA() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_SEND_CODE_CTA.getText()));
    }

    public ExtendedWebElement getOOHUpdateHouseHoldCTA() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_UPDATE_HOUSEHOLD_CTA.getText()));
    }

    public boolean isOOHUpdateHouseHoldHeadlinePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_UPDATE_HOUSEHOLD_HEADLINE.getText())).isPresent();
    }

    public ExtendedWebElement getOOHUpdateHouseHoldSendCodeCTA() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_UPDATE_HOUSEHOLD_SEND_CODE_CTA.getText()));
    }

    public ExtendedWebElement getResendEmailCopy() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.RESEND_EMAIL_COPY_2.getText()));
    }

    public ExtendedWebElement getOOHErrorPageHeadline() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_ERROR_PAGE_HEADLINE.getText()));
    }

    public ExtendedWebElement getOOHErrorActivationGenericCopy() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.ERROR_ACTIVATION_GENERIC_COPY.getText()));
    }

    public boolean isOOHTravelModeMaxedHeadlinePresent() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_MAXED_HEADLINE.getText())).isPresent();
    }

    public boolean isOOHTravelModeMaxedSubcopy() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_MAXED_SUBCOPY.getText())).isPresent();
    }

    public ExtendedWebElement getOOHTravelModeMaxedOKCTA() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.OOH_TRAVEL_MODE_MAXED_CTA.getText()));
    }

    public boolean isLogoutConfirmationTitlePresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOG_OUT_CONFIRMATION_TITLE.getText())).isPresent();
    }
}