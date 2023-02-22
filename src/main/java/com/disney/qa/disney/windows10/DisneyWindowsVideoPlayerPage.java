package com.disney.qa.disney.windows10;

import com.disney.qa.common.DisneyAbstractPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class DisneyWindowsVideoPlayerPage extends DisneyAbstractPage {
    public DisneyWindowsVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "2A.1405DC.4.44")
    private ExtendedWebElement closedCaptioning;
    @FindBy(name = "English [CC]")
    private ExtendedWebElement englishCC;

    @Override
    public boolean isOpened() {
        return false;
    }

    public void selectEnglishCC()
    {
        hoverCC();
        closedCaptioning.isElementPresent();
        closedCaptioning.click();
        englishCC.isElementPresent();
        englishCC.click();
        pause(30);
    }

    public void hoverCC()
    {
        closedCaptioning.hover();
    }
}
