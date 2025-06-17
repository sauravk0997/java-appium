package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
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
}