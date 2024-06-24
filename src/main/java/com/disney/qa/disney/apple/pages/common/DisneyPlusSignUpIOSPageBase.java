package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSignUpIOSPageBase extends DisneyPlusApplePageBase {

    protected ExtendedWebElement signUpHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText()));

    @ExtendedFindBy(accessibilityId = "marketingCheckbox")
    protected ExtendedWebElement optInCheckbox;

    @FindBy(xpath = "//*[contains(@label, 'Disney will use your data')]")
    protected ExtendedWebElement termsOfUserDisclaimer;

    @FindBy(xpath = "//*[@name='marketingCheckbox']")
    protected ExtendedWebElement checkBoxItem;

    @ExtendedFindBy(accessibilityId = "buttonContinue")
    protected ExtendedWebElement agreeAndContinueSignUpBtn;

    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxCheckedUnfoc")
    protected ExtendedWebElement checkedBoxFocused;

    @ExtendedFindBy(accessibilityId = "checkboxCheckedNormal")
    protected ExtendedWebElement checkBoxNormal;

    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxUnCheckedFoc")
    protected ExtendedWebElement uncheckedBox;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$type='XCUIElementTypeTextField'$][12]/XCUIElementTypeImage")
    private ExtendedWebElement brandLogos;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"disneyAuthCheckboxUncheckedUnf\"`][1]")
    private ExtendedWebElement checkbox;

    public DisneyPlusSignUpIOSPageBase(WebDriver driver) {

        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = signUpHeader.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickAgreeAndContinue() {
        primaryButton.click();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
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

    public boolean isCheckBoxCheckedFocused() {
        return checkedBoxFocused.isPresent();
    }

    public boolean isCheckBoxFocused() {
        return uncheckedBox.isPresent();
    }

    public boolean isEmailFieldDisplayed() {
        return emailField.isPresent();
    }

    public String getEmailFieldText() {
        return emailField.getText();
    }

    public void clearEmailAddress() {
        textEntryField.type("");
    }

    public void submitEmailAddress(String email) {
        pause(2);
        emailField.type(email);
        continueButton.click();
    }

    public void enterEmailAddress(String email) {
        emailField.type(email);
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
        pressByElement(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIBER_AGREEMENT_HEADER)),1);
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
        findExtendedWebElements(checkBoxItem.getBy()).forEach(checkBox -> clickElementAtLocation(checkBox, 0, 0));
    }

    private String getDictionaryItem(DisneyDictionaryApi.ResourceKeys dictionary, DictionaryKeys key) {
        boolean isSupported = getDictionary().getSupportedLangs().contains(getDictionary().getUserLanguage());
        return getDictionary().getDictionaryItem(dictionary, key.getText(), isSupported);
    }

    private void openHyperlink(ExtendedWebElement link) {
        if (link.getSize().getWidth() > 150) {
            clickElementAtLocation(link, 10, 80);
        } else {
            link.click();
        }
    }

    public boolean isStep1LabelDisplayed() {
        String step1Label = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_STEPPER_TEXT.getText()), Map.of("current_step", "1"));
        return getStaticTextByLabel(step1Label).isPresent();
    }

    public boolean isEnterEmailHeaderDisplayed() {
        return getStaticTextByLabel((getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER))).isPresent();
    }

    public boolean isEnterEmailBodyDisplayed() {
        return getStaticTextByLabel((getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_BODY))).isPresent();
    }

    public boolean isLearnMoreHeaderDisplayed() {
        return getStaticTextByLabel((getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_LEARN_MORE_HEADER))).isPresent();
    }

    public boolean isLearnMoreBodyDisplayed() {
        String learnMoreBody = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_LEARN_MORE_BODY.getText()), Map.of("link_1", "and more"));
        return getStaticTextByLabel(learnMoreBody).isPresent();
    }

    public boolean isMultipleBrandLogosDisplayed() {
        List<ExtendedWebElement> logos = findExtendedWebElements(brandLogos.getBy());
        return brandLogos.isPresent() && logos.size() > 2;
    }
}
