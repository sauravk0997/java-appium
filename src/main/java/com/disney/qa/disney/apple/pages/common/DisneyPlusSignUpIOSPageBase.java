package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSignUpIOSPageBase extends DisneyPlusApplePageBase {

    protected ExtendedWebElement signUpHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SIGN_UP.getText()));

    @FindBy(xpath = "//*[contains(@label, 'Yes!')]")
    protected ExtendedWebElement optInCheckbox;

    @FindBy(xpath = "//*[contains(@label, 'Disney will use your data')]")
    protected ExtendedWebElement termsOfUserDisclaimer;

    @FindBy(xpath = "//*[@name='marketingCheckbox']")
    protected ExtendedWebElement checkBoxItem;

    @ExtendedFindBy(accessibilityId = "buttonContinue")
    protected ExtendedWebElement agreeAndContinueSignUpBtn;

    @ExtendedFindBy(accessibilityId = "checkboxCheckedFocused")
    protected ExtendedWebElement checkedBoxFocused;

    @ExtendedFindBy(accessibilityId = "checkboxCheckedNormal")
    protected ExtendedWebElement checkBoxNormal;

    @ExtendedFindBy(accessibilityId = "checkboxUncheckedFocused")
    protected ExtendedWebElement uncheckedBox;

    public DisneyPlusSignUpIOSPageBase(WebDriver driver) {

        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = signUpHeader.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void clickAgreeAndContinue() {
        primaryButton.click();
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void clickAgreeAndContinueIfPresent() {
        primaryButton.clickIfPresent(3);
    }

    public String getStepperDictValue(String val1 , String val2) {
        String text = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ONBOARDING_STEPPER.getText());
        return getDictionary().formatPlaceholderString(text, Map.of("current_step", val1, "total_steps", val2));
    }

    public boolean isConsentFormPresent() {
        return optInCheckbox.isElementPresent();
    }

    public boolean isCheckBoxChecked() {
        return checkBoxNormal.isPresent();
    }

    public boolean isCheckBoxUnChecked() {
        return uncheckedBox.isPresent();
    }

    public boolean isCheckBoxFocused() {
        return checkedBoxFocused.isPresent();
    }

    public boolean isEmailFieldDisplayed() {
        return emailField.isPresent();
    }

    public String getEmailFieldText() {
        return emailField.getText();
    }

    public void submitEmailAddress(String email) {
        emailField.type(email);
        primaryButton.click();
    }

    public boolean isTermsOfUserDisclaimerDisplayed() {
        return termsOfUserDisclaimer.isElementPresent();
    }

    public boolean arePrivacyPolicyLinksDisplayed() {
        return !findExtendedWebElements(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PRIVACY_POLICY)).getBy()).isEmpty();
    }

    public void openPrivacyPolicyLink() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PRIVACY_POLICY)));
    }

    public boolean isSubscriberAgreementLinkDisplayed() {
        return customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIBER_AGREEMENT_HEADER)).isPresent();
    }

    public void openSubscriberAgreement() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIBER_AGREEMENT_HEADER)));
    }

    public boolean isInvalidEmailErrorDisplayed() {
        return labelError.isElementPresent();
    }

    public boolean isCookiesPolicyLinkDisplayed() {
        return customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COOKIE_POLICY)).isElementPresent();
    }

    public void openCookiesPolicyLink() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COOKIE_POLICY)));
    }

    public boolean isEuPrivacyLinkPresent() {
        return customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EU_PRIVACY)).isElementPresent();
    }

    public void openEuPrivacyLink() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EU_PRIVACY)));
    }

    //Clicks at 0,0 location due to iOS whole element not being clickable area for response
    public void clickUncheckedBoxes() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(checkBoxItem.getBy()), 30);
        findExtendedWebElements(checkBoxItem.getBy()).forEach(checkBox -> new IOSUtils().clickElementAtLocation(checkBox, 0, 0));
    }

    private String getDictionaryItem(DisneyDictionaryApi.ResourceKeys dictionary, DictionaryKeys key) {
        boolean isSupported = getDictionary().getSupportedLangs().contains(getDictionary().getUserLanguage());
        return getDictionary().getDictionaryItem(dictionary, key.getText(), isSupported);
    }
    
    private void openHyperlink(ExtendedWebElement link) {
        if (link.getSize().getWidth() > 150) {
            new IOSUtils().clickElementAtLocation(link, 10, 80);
        } else {
            link.click();
        }
    }
}
