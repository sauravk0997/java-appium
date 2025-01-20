package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusParentalConsentIOSPageBase extends DisneyPlusApplePageBase {

    private String consentText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_BODY.getText());

    private String  consentHeaderText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_HEADER.getText());

    private String consentMinorScrollBody = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_MOBILE_SCROLL_BODY.getText());

    private String consentMinorScrollHeader = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            DictionaryKeys.CONSENT_MINOR_MOBILE_SCROLL_HEADER.getText());

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement consentMinorHeader;

    @ExtendedFindBy(accessibilityId = "declineButton")
    protected ExtendedWebElement declineButton;

    @ExtendedFindBy(accessibilityId = "agreeButton")
    protected ExtendedWebElement agreeButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView")
    private ExtendedWebElement consentScrollView;

    //Functions
    public DisneyPlusParentalConsentIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isConsentHeaderPresent() {
        return consentMinorHeader.isPresent(THREE_SEC_TIMEOUT);
    }

    public void tapDeclineButton() {
        declineButton.click();
    }

    public void tapAgreeButton() {
        agreeButton.click();
    }

    /**
     * Scroll the text of the Consent for Child
     * @param times
     */
    public void scrollConsentContent(int times){
        swipeInContainer(consentScrollView, IMobileUtils.Direction.UP,times,500);
    }

    public boolean validateScrollPopup() {
        return getSystemAlertText().equalsIgnoreCase(consentMinorScrollHeader) &&
                getStaticTextByLabel(consentMinorScrollBody).isPresent();
    }

    public boolean validateConsentText() {
        SoftAssert sa = new SoftAssert();
        List<String> consentTextList = getTextViewItems(0);
        String modifiedString = consentTextList.get(0).split("policy")[0];
        consentTextList.set(0, modifiedString);
        consentTextList.forEach(item -> sa.assertTrue(consentText.contains(item), "Consent didn't match for" + item));
        return true;
    }

    public boolean verifyPrivacyPolicyLink() {
        return customHyperlinkByLabel.format("privacy policy").isPresent();
    }

    public boolean verifyChildrenPrivacyPolicyLink() {
        return customHyperlinkByLabel.format("Children\\'s Privacy Policy").isPresent();
    }

    public boolean validateConsentHeader() {
        return consentMinorHeader.getText().equalsIgnoreCase(consentHeaderText);
    }

    public boolean isMinorDisclaimerPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.CONSENT_MINOR_DISCLAIMER.getText())).isPresent();
    }

    public boolean isAgreeButtonPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.BTN_CONSENT_ACCEPT.getText())).isPresent();
    }

    public boolean isDeclineButtonPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.BTN_CONSENT_DECLINE.getText())).isPresent();
    }
}
