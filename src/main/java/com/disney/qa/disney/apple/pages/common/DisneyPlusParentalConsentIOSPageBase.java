package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusParentalConsentIOSPageBase extends DisneyPlusApplePageBase {

    private String consentText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_BODY.getText());

    private String  consentHeaderText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_HEADER.getText());

    private String consentMinorScrollBody = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_MOBILE_SCROLL_BODY.getText());

    private String consentMinorScrollHeader = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_MOBILE_SCROLL_HEADER.getText());

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement consentMinorHeader;

    @ExtendedFindBy(accessibilityId = "contentTextView")
    protected ExtendedWebElement contentTextView;

    @ExtendedFindBy(accessibilityId = "declineButton")
    protected ExtendedWebElement declineButton;

    @ExtendedFindBy(accessibilityId = "agreeButton")
    protected ExtendedWebElement agreeButton;

    //Functions
    public DisneyPlusParentalConsentIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isConsentHeaderPresent() {
        return consentMinorHeader.isPresent(SHORT_TIMEOUT);
    }

    public boolean isContentTextViewPresent() {
        return contentTextView.isPresent(SHORT_TIMEOUT);
    }

    public void tapDeclineButton() {
        declineButton.click();
    }

    public void tapAgreeButton() {
        agreeButton.click();
    }

    public boolean validateScrollPopup() {
        return getSystemAlertText().equalsIgnoreCase(consentMinorScrollHeader) &&
        getStaticTextByLabel(consentMinorScrollBody).isPresent();
    }

    public boolean validateConsentText() {
        return contentTextView.getText().equalsIgnoreCase(consentText);
    }

    public boolean validateConsentHeader() {
        return consentMinorHeader.getText().equalsIgnoreCase(consentHeaderText);
    }
}
