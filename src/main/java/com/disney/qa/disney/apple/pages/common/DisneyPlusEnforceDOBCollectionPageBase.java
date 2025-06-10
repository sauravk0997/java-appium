package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEnforceDOBCollectionPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    //FUNCTIONS
    public DisneyPlusEnforceDOBCollectionPageBase(WebDriver driver) {
        super(driver);
    }

    /**
     * @return - true/false, to verify Enforce DOB Collection page is opened or not
     */
    @Override
    public boolean isOpened() {
        String enforceDateOfBirthPageTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EXISTING_SUBS_DATE_OF_BIRTH_TITLE.getText());
        return staticTextByLabel.format(enforceDateOfBirthPageTitle).isPresent();
    }

    /**
     * @return - true/false, to verify Enforce DOB Description is displayed or not
     */
    public boolean isDateOfBirthDescriptionPresent() {
        String enforceDateOfBirthPageDescription = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EXISTING_SUBS_DATE_OF_BIRTH_TITLE.getText());
        return staticTextByLabel.format(enforceDateOfBirthPageDescription).isPresent();
    }

    /**
     * Click Log Out button
     */
    public void tapLogOutButton() {
        String enforceDateOfBirthLogOutButton = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_DATE_OF_BIRTH_LOG_OUT.getText());
        dynamicBtnFindByLabel.format(enforceDateOfBirthLogOutButton).click();
    }
}