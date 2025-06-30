package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDOBCollectionPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "dateTextField")
    private ExtendedWebElement dateTextField;

    @ExtendedFindBy(accessibilityId = "primaryButton")
    private ExtendedWebElement confirmButton;

    protected ExtendedWebElement dateOfBirthHeader =
            getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                    DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DATE_OF_BIRTH_TITLE.getText()));
    //FUNCTIONS
    public DisneyPlusDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return dateOfBirthHeader.isPresent();
    }

    public void clickConfirmBtn() { confirmButton.click(); }

    public void enterDOB(String dob) {
        dateTextField.type(dob);
        confirmButton.click();
    }
}
