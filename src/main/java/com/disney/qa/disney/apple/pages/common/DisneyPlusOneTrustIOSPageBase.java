package com.disney.qa.disney.apple.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

public class DisneyPlusOneTrustIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusOneTrustIOSPageBase(WebDriver driver) {
        super(driver);
    }

    private static String usPrivacyRights = "Your US State Privacy Rights";
    private static String californiaPrivacyRights = "Your California Privacy Rights";
    private static String pageTitleText = "Notice of Right to Opt Out of Sale/Sharing";
    private static String consentCellGroupNameTitle = "Selling, Sharing, Targeted Advertising";

    @ExtendedFindBy(accessibilityId = "Close")
    private ExtendedWebElement closeButton;
    @ExtendedFindBy(accessibilityId = "TWDC_Logo_Sheet_No_Mickey_Outlines-02.png")
    private ExtendedWebElement logo;
    @ExtendedFindBy(accessibilityId = "pcTitle")
    private ExtendedWebElement pageTitle;
    @ExtendedFindBy(accessibilityId = "pcEditableConsentCellConsentSwitch")
    private ExtendedWebElement consentSwitch;
    @ExtendedFindBy(accessibilityId = "pcEditableConsentCellGroupName")
    private ExtendedWebElement consentSwitchTitle;
    @ExtendedFindBy(accessibilityId = "pcConfirmMyChoiceButton")
    private ExtendedWebElement confirmMyChoiceButton;

    @Override
    public boolean isOpened() {
        return pageTitle.isPresent();
    }

    public void tapCloseButton(){
        closeButton.click();
    }


}
