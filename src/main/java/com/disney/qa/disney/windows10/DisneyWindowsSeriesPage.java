package com.disney.qa.disney.windows10;

import com.disney.qa.common.utils.UniversalUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsSeriesPage extends DisneyWindowsCommonPage {
    @FindBy(name = "Series")
    private ExtendedWebElement seriesTitle;
    @FindBy(name = "What If...?")
    private ExtendedWebElement whatIfTile;
    @ExtendedFindBy(accessibilityId = "LogoImage")
    private ExtendedWebElement logoImage;
    @FindBy(name = "EPISODES")
    private ExtendedWebElement episodes;
    @FindBy(name = "SUGGESTED")
    private ExtendedWebElement suggested;
    @FindBy(name = "EXTRAS")
    private ExtendedWebElement extras;
    @FindBy(name = "DETAILS")
    private ExtendedWebElement details;


    public DisneyWindowsSeriesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSeriesPresent() {
        return seriesTitle.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        return seriesTitle.isElementPresent();
    }

    public void seriesDetailsCheck() {
        whatIfTile.isElementPresent();
        whatIfTile.click();
        pause(5);
        UniversalUtils.captureAndUpload(getDriver());
        suggested.click();
        pause(5);
        UniversalUtils.captureAndUpload(getDriver());
        extras.click();
        pause(5);
        UniversalUtils.captureAndUpload(getDriver());
        details.click();
        pause(5);
        UniversalUtils.captureAndUpload(getDriver());
        pressBack(1, 5);
    }
}
