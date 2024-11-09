package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSignUpIOSPageBase extends DisneyPlusApplePageBase {

    protected ExtendedWebElement signUpHeader = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText()));

    @FindBy(xpath = "//*[@name='marketingCheckbox']")
    protected ExtendedWebElement checkBoxItem;

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

    //Clicks at 0,0 location due to iOS whole element not being clickable area for response
    public void clickUncheckedBoxes() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(checkBoxItem.getBy()), 30);
        findExtendedWebElements(checkBoxItem.getBy()).forEach(checkBox -> clickElementAtLocation(checkBox, 0, 0));
    }
}
