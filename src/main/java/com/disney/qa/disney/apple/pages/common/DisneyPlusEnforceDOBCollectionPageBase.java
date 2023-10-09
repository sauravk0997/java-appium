package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEnforceDOBCollectionPageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    private String enforceDateOfBirthPageTitle = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EXISTING_SUBS_DATE_OF_BIRTH_TITLE.getText());
    private String enforceDateOfBirthPageDescription = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EXISTING_SUBS_DATE_OF_BIRTH_TITLE.getText());
    private String enforceDateOfBirthLogOutButton = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_DATE_OF_BIRTH_LOG_OUT.getText());

    //FUNCTIONS
    public DisneyPlusEnforceDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }

    /**
     * @return - true/false, to verify Enforce DOB Collection page is opened or not
     */
    @Override
    public boolean isOpened() {
        return staticTextByLabel.format(enforceDateOfBirthPageTitle).isPresent();
    }

    /**
     * @return - true/false, to verify Enforce DOB Description is displayed or not
     */
    public boolean isDateOfBirthDescriptionPresent() {
        return staticTextByLabel.format(enforceDateOfBirthPageDescription).isPresent();
    }

    //format: Month, day, year
    public void enterDOB(DateHelper.Month month, String day, String year) {
        new IOSUtils().setBirthDate(DateHelper.localizeMonth(month, getDictionary()), day, year);
        dismissPickerWheelKeyboard();
    }

    /**
     * Click Log Out button
     */
    public void tapLogOutButton() {
        dynamicBtnFindByLabel.format(enforceDateOfBirthLogOutButton).click();
    }
}