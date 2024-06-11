package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEdnaDOBCollectionPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    //FUNCTIONS
    public DisneyPlusEdnaDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        String enforceDateOfBirthPageTitle = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_MISSING_INFO_HEADER.getText());
        return staticTextByLabel.format(enforceDateOfBirthPageTitle).isPresent();
    }

    public boolean isEdnaDateOfBirthDescriptionPresent() {
        String enforceDateOfBirthPageDescription = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_MISSING_INFO_BODY.getText());
        return staticTextByLabel.format(enforceDateOfBirthPageDescription).isPresent();
    }

    //format: Month, day, year
    public void enterDOB(DateHelper.Month month, String day, String year) {
        textEntryField.click();
        setBirthDate(DateHelper.localizeMonth(month, getDictionary()), day, year);
        dismissPickerWheelKeyboard();
    }

    public void tapSaveAndContinueButton() {
        String enforceDateOfBirthLogOutButton = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_SAVE_CONTINUE_BTN.getText());
        dynamicBtnFindByLabel.format(enforceDateOfBirthLogOutButton).click();
    }

    public void tapLogOutButton() {
        String enforceDateOfBirthLogOutButton = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_LOGOUT_BTN.getText());
        dynamicBtnFindByLabel.format(enforceDateOfBirthLogOutButton).click();
    }
}