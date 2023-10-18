package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAddProfileBannerIOSPageBase extends DisneyPlusApplePageBase {

    //Functions
    public DisneyPlusAddProfileBannerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isProfileHeaderPresent() {
        return staticTextByLabel.format(getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_HEADER.getText()))
                .isPresent();
    }

    public boolean isProfileSubcopyPresent() {
        return staticTextByLabel.format(getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_SUB.getText()))
                .isPresent(SHORT_TIMEOUT);
    }

    public boolean isDismissButtonPresent() {
        return dynamicBtnFindByName.format("secondaryButton").isPresent(SHORT_TIMEOUT);
    }
    public void tapDismissButton() {
        clickElementAtLocation(secondaryButton,0,0);
        //Sometime Need to click 2 time, due to Interactions issue
        if(isProfileHeaderPresent()){
            clickElementAtLocation(secondaryButton,0,0);
        }
    }

    public boolean isAddProfileButtonPresent() {
        return dynamicBtnFindByName.format("primaryButton").isPresent();
    }
    public void tapAddProfileButton() {
        clickElementAtLocation(primaryButton, 0, 0);
    }
}
