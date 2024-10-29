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

    protected ExtendedWebElement signUpHeader = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText()));

    @ExtendedFindBy(accessibilityId = "marketingCheckbox")
    protected ExtendedWebElement optInCheckbox;

    @FindBy(xpath = "//*[@name='marketingCheckbox']")
    protected ExtendedWebElement checkBoxItem;

    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxUnCheckedFoc")
    protected ExtendedWebElement checkboxUnChecked;

    @ExtendedFindBy(accessibilityId = "disneyAuthAppCheckboxCheckedFo")
    protected ExtendedWebElement checkboxCheckedFocused;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$type='XCUIElementTypeTextField'$][12]/XCUIElementTypeImage")
    private ExtendedWebElement brandLogos;

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

    public boolean isEmailFieldDisplayed() {
        return emailField.isPresent();
    }

    public String getEmailFieldText() {
        return emailField.getText();
    }

    public void submitEmailAddress(String email) {
        pause(2);
        emailField.type(email);
        continueButton.click();
    }

    public void enterEmailAddress(String email) {
        emailField.type(email);
    }

    //Clicks at 0,0 location due to iOS whole element not being clickable area for response
    public void clickUncheckedBoxes() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(checkBoxItem.getBy()), 30);
        findExtendedWebElements(checkBoxItem.getBy()).forEach(checkBox -> clickElementAtLocation(checkBox, 0, 0));
    }

    private String getDictionaryItem(DisneyDictionaryApi.ResourceKeys dictionary, DictionaryKeys key) {
        boolean isSupported = getLocalizationUtils().getSupportedLangs().contains(getLocalizationUtils().getUserLanguage());
        return getLocalizationUtils().getDictionaryItem(dictionary, key.getText(), isSupported);
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
        String learnMoreBody = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_LEARN_MORE_BODY.getText()), Map.of("link_1", "and more"));
        return getStaticTextByLabel(learnMoreBody).isPresent();
    }

    public boolean isMultipleBrandLogosDisplayed() {
        List<ExtendedWebElement> logos = findExtendedWebElements(brandLogos.getBy());
        return brandLogos.isPresent() && logos.size() > 2;
    }
}
