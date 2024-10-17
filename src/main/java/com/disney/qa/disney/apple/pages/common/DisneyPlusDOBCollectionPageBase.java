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

    protected ExtendedWebElement dateOfBirthHeader = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DATE_OF_BIRTH_TITLE.getText()));
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

    public void clickConfirmBtn() { confirmButton.click(); }

    public void enterDOB(String dob) {
        dateTextField.type(dob);
        confirmButton.click();
    }
}
