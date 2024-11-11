package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSignUpIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusSignUpIOSPageBase.class)
public class DisneyPlusAppleTVSignUpPage extends DisneyPlusSignUpIOSPageBase {

    @ExtendedFindBy(accessibilityId = "secondaryButton")
    private ExtendedWebElement viewAgreementAndPolicies;
    @ExtendedFindBy(accessibilityId = "textFieldEmail")
    private ExtendedWebElement emailTextField;

    @Override
    public boolean isOpened() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CONTINUE_BTN.getText())).isElementPresent();
    }

    @Override
    public void clickAgreeAndContinue() {
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CONTINUE_BTN.getText())).click();
    }

    public DisneyPlusAppleTVSignUpPage(WebDriver driver) {
        super(driver);
    }

    public boolean isRestartSubBtnPresent() {
        return customButton.isPresent();
    }

    public void clickRestartSubscription() {
        customButton.click();
    }

    public void clickViewAgreementAndPolicies() {
        viewAgreementAndPolicies.click();
    }

    public void enterDateOfBirth(String dob) {
        for (char c : dob.toCharArray()) {
            dynamicBtnFindByName.format(c).click();
        }
    }

    public void selectCheckBoxesForKr(boolean isKr) {
        if(isKr) {
            keyPressTimes(IRemoteControllerAppleTV::clickDown, 2, 1);
            clickSelect();
            clickDown();
            clickSelect();
            keyPressTimes(IRemoteControllerAppleTV::clickUp, 3, 1);
        }
    }

    public void clickEmailButton() {
        emailTextField.click();
    }
}
