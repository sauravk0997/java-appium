package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class DisneyPlusSignUpPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "backButton")
    private ExtendedWebElement backButton;

    @FindBy(id = "editFieldEditText")
    private ExtendedWebElement editFieldEditText;

    @FindBy(id = "inputErrorTextView")
    private ExtendedWebElement inputErrorTextView;

    @FindBy(id = "inputHintTextView")
    private ExtendedWebElement inputHintTextView;

    @FindBy(id = "legalese_tv")
    private ExtendedWebElement legaleseTV;

    @FindBy(id = "onboardingStepperTextView")
    private ExtendedWebElement onboardingStepperTextView;

    @FindBy(id = "optInCheckbox")
    private ExtendedWebElement optInCheckboxes;

    @FindBy(id = "optInTextView")
    private ExtendedWebElement optInTextView;

    @FindBy(id = "signUpEmailLayout")
    private ExtendedWebElement signupPageContainer;

    @FindBy(id = "signUpTitle")
    private ExtendedWebElement signupTitle;

    @FindBy(id = "standardButtonContainer")
    private ExtendedWebElement standardButtonContainer;

    public DisneyPlusSignUpPageBase(WebDriver driver) {
        super(driver);
    }

    public String getSignupTitleText() {
        return getElementText(signupTitle);
    }

    public boolean isBackButtonDisplayed() {
        return backButton.isElementPresent();
    }

    public boolean isEditFieldEditTextDisplayed() {
        return editFieldEditText.isElementPresent();
    }

    public boolean isInputHintTextViewDisplayed() {
        return inputHintTextView.isElementPresent();
    }

    public boolean isInputErrorTextViewDisplayed() {
        return inputErrorTextView.isElementPresent();
    }

    public boolean isOnboardingStepperTextViewDisplayed() {
        return onboardingStepperTextView.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        return signupPageContainer.isElementPresent() && editTextByClass.isElementPresent();
    }

    public boolean isoptInCheckboxesDisplayed() {
        return optInCheckboxes.isElementPresent();
    }

    public boolean isOptInTextViewDisplayed() {
        return optInTextView.isElementPresent();
    }

    public boolean isLegaleseTVDisplayed() {
        return legaleseTV.isElementPresent();
    }

    public boolean isSignupTitleDisplayed() {
        return signupTitle.isElementPresent();
    }

    public void proceedToPasswordMode(String email) {
        editTextByClass.click();
        editTextByClass.type(email);
        new AndroidUtilsExtended().hideKeyboard();
        standardButtonContainer.click();
    }

    public void clickVisibleCheckBoxes(){
        List<WebElement> checkboxes = getDriver().findElements(By.id("optInCheckbox"));
        for (WebElement checkbox: checkboxes){
            checkbox.click();
        }
    }
}
