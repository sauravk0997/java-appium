package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
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

    private static final String BIRTHDATE_TEXT_FIELD = "dateTextField";

    protected ExtendedWebElement dateOfBirthHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DATE_OF_BIRTH_TITLE.getText()));

    //FUNCTIONS
    public DisneyPlusDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getDateOfBirthHeader(){
        return dateOfBirthHeader;
    }

    public void enterDOB(DateHelper.Month month, String day, String year) {
        getDynamicTextEntryFieldByName(BIRTHDATE_TEXT_FIELD).click();
        setBirthDate(DateHelper.localizeMonth(month, getDictionary()), day, year);
        dismissPickerWheelKeyboard();
        confirmButton.click();
    }
    @Override
    public boolean isOpened() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return dateOfBirthHeader.isPresent();
    }

    public boolean isInvalidDOBMessageDisplayed() { return labelError.isPresent(); }

    public void enterDOB(String dob) {
        dateTextField.type(dob);
        confirmButton.click();
    }
}
