package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusChangePasswordIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusChangePasswordIOSPageBase.class)
public class DisneyPlusAppleTVChangePasswordPage extends DisneyPlusChangePasswordIOSPageBase {
    public DisneyPlusAppleTVChangePasswordPage(WebDriver driver) {super(driver);}

    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    private ExtendedWebElement passwordTextField;

    @Override
    public boolean isOpened() {
        boolean isPresent = passwordTextField.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void clickPasswordField() { passwordTextField.click(); }

    public void enterPassword(String password) {
        passwordTextField.type(password);
    }

    public void clickSave() {
        primaryButton.click();
    }
}
