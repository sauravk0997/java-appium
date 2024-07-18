package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusUpdateProfileIOSPageBase extends DisneyPlusEditProfileIOSPageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "editProfile")
    private ExtendedWebElement editProfile;

    @ExtendedFindBy(accessibilityId = "submitButtonCellIdentifier")
    private ExtendedWebElement saveButton;

    @ExtendedFindBy(accessibilityId = "genderFormButtonCellIdentifier")
    private ExtendedWebElement genderFormButtonCellIdentifier;

    private String updateProfileTitle = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMPLETE_PROFILE_TITLE.getText());
    private String completeProfileDescription = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMPLETE_PROFILE_DESCRIPTION.getText());

    //FUNCTIONS
    public DisneyPlusUpdateProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {return doesUpdateProfileTitleExist(); }

    public boolean isEditProfilePresent() { return editProfile.isElementPresent(); }


    public boolean doesUpdateProfileTitleExist() {
        return staticTextByLabel.format(updateProfileTitle).isPresent(THREE_SEC_TIMEOUT);
    }

    public boolean doesCompleteProfileDescExist() {
        return staticTextByLabel.format(completeProfileDescription).isPresent();
    }

    public void tapSaveButton(){
        saveButton.click();
    }
}
