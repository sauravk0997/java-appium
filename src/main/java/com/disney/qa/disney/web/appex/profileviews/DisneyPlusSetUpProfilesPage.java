package com.disney.qa.disney.web.appex.profileviews;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSetUpProfilesPage extends DisneyPlusSetUpProfileViewPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusSetUpProfilesPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(css = "#kidsMode + span")
    private ExtendedWebElement juniorModeToggle;

    @FindBy(xpath = "//input[@id='kidsMode']")
    private ExtendedWebElement juniorModeToggleStatus;

    public void nameOfNewProfile(String nameOfNewProfile) {
        waitFor(addProfileNameTextBox);
        addProfileNameTextBox.type(nameOfNewProfile);
    }

    public void turnJuniorModeToggledOn() {
        LOGGER.info("Click on Toggle to turn junior-profile on");
        waitFor(juniorModeToggle);
        juniorModeToggle.click();
    }

    public boolean getJuniorToggleStatus(String status) {
        LOGGER.info("Verify junior toggle button :" + status);
        waitFor(juniorModeToggleStatus);
        return juniorModeToggleStatus.getAttribute("aria-checked").equals(status);
    }
}
