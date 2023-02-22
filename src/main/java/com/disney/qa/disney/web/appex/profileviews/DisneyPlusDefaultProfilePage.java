package com.disney.qa.disney.web.appex.profileviews;

import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusDefaultProfilePage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(id = "addProfile")
    private ExtendedWebElement addNewProfileNameButton;

    @FindBy(xpath = "//*[@data-testid='delete-profile-button']")
    private ExtendedWebElement deleteProfileButton;

    private static final String DEFAULT_PROFILE = "Profile";

    public DisneyPlusDefaultProfilePage(WebDriver driver) {
        super(driver);
    }

    public String verifyDefaultProfileName() {
        LOGGER.info("Verify Default Profile Name");
        return getGenericEqualsText(DEFAULT_PROFILE).getText();
    }

    public boolean verifyDeleteProfileButton() {
        return deleteProfileButton.isElementPresent();
    }

    public boolean verifyTextOnAddProfileTextBoxIsEmpty() {
        LOGGER.info("Verify Profile Name Text Box Is Empty");
        return addNewProfileNameButton.getText().isEmpty();
    }
}
