package com.disney.qa.disney.web.appex.headermenu;

import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOriginalsPage extends DisneyPlusBaseTilesPage {

    public DisneyPlusOriginalsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "(//*[contains(@clip-path,'originals')])[1]")
    private ExtendedWebElement dPlusOriginalsLogo;

    @FindBy(id = "originals-collection")
    private ExtendedWebElement dPlusOriginalsCollectionId;

    @FindBy(xpath = "//*[@data-testid='navigation-item-3-ORIGINALS']")
    private ExtendedWebElement originalMenuOption;

    private ExtendedWebElement getdPlusOriginalsLogo() {
        waitFor(dPlusOriginalsLogo);
        return dPlusOriginalsLogo;
    }

    private ExtendedWebElement getdPlusOriginalsCollectionId() {
        waitFor(dPlusOriginalsCollectionId);
        return dPlusOriginalsCollectionId;
    }

    public ExtendedWebElement getOriginalMenuOption() {
        waitFor(originalMenuOption);
        return originalMenuOption;
    }

    public boolean isdPlusOriginalsLogoPresent() {
        return getdPlusOriginalsLogo().isElementPresent();
    }

    public boolean isdPlusOriginalsCollectionIdPresent() {
        return getdPlusOriginalsCollectionId().isElementPresent();
    }

    public void clickOnOriginalMenuOption() {
        PAGEFACTORY_LOGGER.info("Click 'Original' menu option");
        getOriginalMenuOption().click();
    }
}
