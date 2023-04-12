package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSignUpIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.appletv.IRemoteControllerAppleTV;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusSignUpIOSPageBase.class)
public class DisneyPlusAppleTVSignUpPage extends DisneyPlusSignUpIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"VIEW AGREEMENT & POLICIES\"`]")
    private ExtendedWebElement viewAgreementAndPolicies;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`label == \"Email\"`]")
    private ExtendedWebElement emailTextField;
    
    @ExtendedFindBy(accessibilityId = "legalDisclosureView")
    private ExtendedWebElement legalDisclosure;

    @Override
    public boolean isOpened() {
        boolean isPresent = primaryButton.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    @Override
    public void clickAgreeAndContinue() {
        primaryButton.click();
    }

    public boolean isAgreeAndContinueFocused() {
        return isFocused(primaryButton);
    }

    public DisneyPlusAppleTVSignUpPage(WebDriver driver) {
        super(driver);
    }

    public boolean isRestartSubBtnPresent() {
        return customButton.isPresent();
    }

    public boolean islegalDisclosureViewPresent() {
        return legalDisclosure.isPresent();
    }

    public void clickRestartSubscription() {
        customButton.click();
    }

    public boolean isViewAgreementAndPoliciesFocused() {
        return isFocused(viewAgreementAndPolicies);
    }

    public void clickViewAgreementAndPolicies() {
        viewAgreementAndPolicies.click();
    }

    @Override
    public String getEmailFieldText() {
        return emailTextField.getText();
    }

    public void waitUntilEmailFieldIsFocused() {
        fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT,"Unable to focus email field on sign up page")
                .until(it -> isFocused(emailTextField));
    }

    public void enterDateOfBirth(String dob) {
        for (char c : dob.toCharArray()) {
            dynamicBtnFindByName.format(c).click();
        }
    }

    public void proceedToLegalPage(boolean isKR) {
        // KR has some check boxes that are required to be selected for sign up
        if (isKR) {
            keyPressTimes(IRemoteControllerAppleTV::clickDown,  2, 1);
            clickSelect();
            clickDown();
            clickSelect();
            clickDown();
        } else {
            keyPressTimes(IRemoteControllerAppleTV::clickDown, 2, 1);
        }
        clickSelect();
    }

    public void selectCheckBoxesForKr(boolean isKr) {
        keyPressTimes(IRemoteControllerAppleTV::clickDown,  2, 1);
        clickSelect();
        clickDown();
        clickSelect();
        keyPressTimes(IRemoteControllerAppleTV::clickUp,  3, 1);
    }
}
