package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAddProfileBannerIOSPageBase extends DisneyPlusApplePageBase {

    //Functions
    public DisneyPlusAddProfileBannerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isProfileHeaderPresent() {
        return staticTextByLabel.format(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_HEADER.getText()))
                .isPresent();
    }

    public boolean isProfileSubcopyPresent() {
        return staticTextByLabel.format(getLocalizationUtils()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_SUB.getText()))
                .isPresent(THREE_SEC_TIMEOUT);
    }

    public boolean isDismissButtonPresent() {
        return dynamicBtnFindByName.format("secondaryButton").isPresent(THREE_SEC_TIMEOUT);
    }
    public void tapDismissButton() {
        clickSecondaryButtonByCoordinates();
        //Sometime Need to click 2 time, due to Interactions issue
        if(isProfileHeaderPresent()){
            clickSecondaryButtonByCoordinates();
        }
    }

    public boolean isAddProfileButtonPresent() {
        return dynamicBtnFindByName.format("primaryButton").isPresent();
    }
    public void tapAddProfileButton() {
        clickPrimaryButtonByCoordinates();
    }
}
