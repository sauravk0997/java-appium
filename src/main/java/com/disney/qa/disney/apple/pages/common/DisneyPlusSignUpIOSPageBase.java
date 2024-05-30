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

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSignUpIOSPageBase extends DisneyPlusApplePageBase {

    protected ExtendedWebElement signUpHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText()));

    @ExtendedFindBy(accessibilityId = "marketingCheckbox")
    protected ExtendedWebElement optInCheckbox;

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
        pause(2);
        emailField.type(email);
        primaryButton.click();
    }

    public void enterEmailAddress(String email) {
        emailField.type(email);
    }

    public void clickContinueBtn() {
        continueButton.click();
    }


    public boolean isInvalidEmailErrorDisplayed() {
        return labelError.isElementPresent();
    }

    //Clicks at 0,0 location due to iOS whole element not being clickable area for response
    public void clickUncheckedBoxes() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(checkBoxItem.getBy()), 30);
        findExtendedWebElements(checkBoxItem.getBy()).forEach(checkBox -> clickElementAtLocation(checkBox, 0, 0));
    }
}
