package com.disney.qa.disney.web.appex.profileviews;

import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusProfilePickerPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(id = "logo")
    private ExtendedWebElement disneyPlusLogo;

    public DisneyPlusProfilePickerPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoPresent() {
        LOGGER.info("Verify Logo");
        return disneyPlusLogo.isElementPresent();
    }

    public void verifyProfileSelection(String profileText) {
        waitFor(getGenericEqualsText(profileText));
        getGenericEqualsText(profileText).click();
        LOGGER.info("After Profile Selection: " + getCurrentUrl());
    }
}
