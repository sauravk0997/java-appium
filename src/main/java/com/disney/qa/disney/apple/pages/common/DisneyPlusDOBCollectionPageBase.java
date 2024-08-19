package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDOBCollectionPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "titleLabel")
    protected ExtendedWebElement enterYourDOBTitle;

    @ExtendedFindBy(accessibilityId = "subtitleLabel")
    private ExtendedWebElement dobSubTitle;

    @ExtendedFindBy(accessibilityId = "birthdateTextFieldHeaderLabel")
    private ExtendedWebElement birthdateTextFieldHeaderLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label ==\"Cancel\"`]")
    private ExtendedWebElement cancelBtn;

    @ExtendedFindBy(accessibilityId = "dateTextField")
    private ExtendedWebElement dateTextField;

    @ExtendedFindBy(accessibilityId = "primaryButton")
    private ExtendedWebElement confirmButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label ==\"Done\"`]")
    private ExtendedWebElement doneBtn;

    protected ExtendedWebElement dateOfBirthHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DATE_OF_BIRTH_TITLE.getText()));

    //FUNCTIONS
    public DisneyPlusDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getDateOfBirthHeader() { return dateOfBirthHeader; }

    @Override
    public boolean isOpened() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return dateOfBirthHeader.isPresent();
    }

    public boolean isInvalidDOBMessageDisplayed() { return labelError.isPresent(); }

    public boolean isEnterYourDOBTitleDisplayed() { return enterYourDOBTitle.isElementPresent(); }

    public boolean isDOBSubTitleDisplayed() { return dobSubTitle.isElementPresent(); }

    public boolean isBirthdateTextFieldHeaderDisplayed() { return birthdateTextFieldHeaderLabel.isElementPresent(); }

    public boolean isCancelBtnDisplayed() { return cancelBtn.isElementPresent(); }

    public boolean isDateTextFieldDisplayed() { return dateTextField.isElementPresent(); }

    public boolean isConfirmBtnDisplayed() { return confirmButton.isElementPresent(); }

    public boolean isDoneBtnDisplayed() { return doneBtn.isElementPresent(); }


    public void enterDOB(String dob) {
        dateTextField.type(dob);
        confirmButton.click();
    }
}
