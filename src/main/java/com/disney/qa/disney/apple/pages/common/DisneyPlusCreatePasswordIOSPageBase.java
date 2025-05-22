package com.disney.qa.disney.apple.pages.common;

import com.amazonaws.services.applicationautoscaling.model.ObjectNotFoundException;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreatePasswordIOSPageBase extends DisneyPlusApplePageBase {

    private static final String CONSENT_TEXT_HEADER = "Yes, I would like to receive updates, special offers and other information from Disney+ and The Walt Disney Family of Companies.";
    private static final String CONSENT_SUBTEXT = "By clicking “Agree & Continue,” you agree to the Disney Terms of Use and Disney+ Subscriber Agreement, and acknowledge you have read our Privacy Policy and US State Privacy Rights Notice.";
    private static final String EDIT_LINK = "(edit)";
    protected ExtendedWebElement createNewPasswordPageHeader = getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CREATE_PASSWORD_HEADER.getText()));
    private static final String EMAIL = "email";
    private static final String LINK_1 = "link_1";

    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    protected ExtendedWebElement signUpBtn;

    protected ExtendedWebElement emailInUseText = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FAT_FINGER_EMAIL.getText()));

    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    private ExtendedWebElement showHideEyeIcon;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeSecureTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[1]")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeSecureTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[3]")
    private ExtendedWebElement myDisneyLogo;

    public DisneyPlusCreatePasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return headlineHeader.isPresent();
    }

    public String[] getPasswordBodyText() {
        return getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CREATE_PASSWORD_BODY.getText()).split("\n");
    }

    public boolean isHidePasswordIconPresent() {
        return showHideEyeIcon.isElementPresent();
    }

    public boolean isDisneyLogoDisplayed() {
        return dPlusLogo.isPresent();
    }

    public boolean isMyDisneyLogoDisplayed() {
        return myDisneyLogo.isPresent();
    }

    public boolean isPasswordBodyTextDisplayed() {
        return getTextViewByName(getPasswordBodyText()[0]).isElementPresent();
    }

    public boolean isPasswordBodySubTextDisplayed(String email) {
        String passwordBodySubText = getLocalizationUtils().formatPlaceholderString(getPasswordBodyText()[2], Map.of(EMAIL, email, LINK_1, EDIT_LINK));
        return getTextViewByName(passwordBodySubText).isElementPresent();
    }

    public void clickShowHidePassword() {
        showHideEyeIcon.click();
    }

    public boolean isPasswordEntryFieldPresent() {
        return passwordEntryField.isElementPresent();
    }

    public String getPasswordEntryText() {
        return passwordEntryField.getText();
    }

    public void tapSignUpButton() {
        staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SIGN_UP.getText())).click(THREE_SEC_TIMEOUT);
    }

    public void enterPasswordValue(String value) {
        passwordEntryField.type(value);
    }

    public void submitPasswordValue(String value) {
        enterPasswordValue(value);
        clickElementAtLocation(passwordEntryField, 0, 50);
        dismissKeyboardForPhone();
        clickPrimaryButton();
    }

    public boolean isEmailInUseDisplayed(String email) {
        return emailInUseText.isElementPresent() && getDynamicAccessibilityId(email).isElementPresent();
    }

    public boolean isInvalidPasswordErrorDisplayed() {
        ExtendedWebElement passwordLengthError = getStaticTextByLabel(getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2"))));
        return passwordLengthError.isElementPresent();
    }

    public boolean isEmptyPasswordErrorDisplayed() {
        ExtendedWebElement passwordEmptyError = getStaticTextByLabel((getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PASSWORD_CREATION_ERROR_EMPTY.getText())));
        return passwordEmptyError.isElementPresent();
    }

    public boolean isCreateNewPasswordPageOpened() {
        return createNewPasswordPageHeader.isElementPresent();
    }

    public ExtendedWebElement getCreateNewPasswordPageHeader() {
       return createNewPasswordPageHeader;
    }

    private void openHyperlink(ExtendedWebElement link) {
        swipeInContainerTillElementIsPresent(null, primaryButton, 2, Direction.UP);
        if (!link.isDisplayed()) {
            throw new ObjectNotFoundException("Link is not found on page");
        }

        Point location = link.getLocation();
        if (link.getSize().getWidth() > 150) {
            var dimension = link.getSize();
            tap(location.getX(), location.getY() + (dimension.getHeight() - 5));
        } else {
            tap(location.getX(), location.getY() + 5);
        }
    }

    private String getDictionaryItem(DisneyDictionaryApi.ResourceKeys dictionary, DictionaryKeys key) {
        boolean isSupported = getLocalizationUtils().getSupportedLangs().contains(getLocalizationUtils().getUserLanguage());
        return getLocalizationUtils().getDictionaryItem(dictionary, key.getText(), isSupported);
    }

    public void openPrivacyPolicyLink() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PRIVACY_POLICY)));
    }

    public void openSubscriberAgreement() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIBER_AGREEMENT_HEADER)));
    }

    public void openCookiesPolicyLink() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COOKIE_POLICY)));
    }

    public boolean isMarketingTextDisplayed() {
        return getTextViewByName(CONSENT_TEXT_HEADER).isElementPresent();
    }

    public boolean isConsentLegalTextDisplayed() {
        return getTextViewByName(CONSENT_SUBTEXT).isElementPresent();
    }
    public void openEuPrivacyLink() {
        openHyperlink(customHyperlinkByLabel.format(getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EU_PRIVACY)));
    }
}
