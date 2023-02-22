package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHelpCenterPageBase extends DisneyPlusCommonPageBase {

    @FindBy(xpath = "//*[@text='help.disneyplus.com']")
    private ExtendedWebElement urlBar;

   public boolean getURLText() {
       return urlBar.isElementPresent();
   }

    public DisneyPlusHelpCenterPageBase(WebDriver driver) {
        super(driver);
    }
}
