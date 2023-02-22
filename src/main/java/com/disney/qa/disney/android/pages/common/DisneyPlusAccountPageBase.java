package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAccountPageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "restrictProfileCreationToggleSwitch")
    protected ExtendedWebElement restrictCreationSwitch;

    public DisneyPlusAccountPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isRestrictProfileCreationSwitchEnabled() {
        return Boolean.valueOf(restrictCreationSwitch.getAttribute("checked"));
    }

    public void toggleRestrictProfileCreation() {
        restrictCreationSwitch.click();
    }
}
