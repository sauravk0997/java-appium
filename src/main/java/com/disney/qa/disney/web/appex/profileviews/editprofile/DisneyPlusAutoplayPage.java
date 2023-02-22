package com.disney.qa.disney.web.appex.profileviews.editprofile;

import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAutoplayPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='toggle-switch-autoplay']")
    private ExtendedWebElement autoplayBtn;

    @FindBy(xpath = "//div[@class =  'slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(xpath = "//div[@class =  'slick-track']/div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(css = "input+span")
    private ExtendedWebElement juniorModeToggle;

    @FindBy(xpath = "//*[@for='kidsMode' and @aria-pressed='%s']")
    private ExtendedWebElement juniorModeToggleStatus;

    private static final String SELECT_DARTH_VADER = "Darth Vade...";

    public ExtendedWebElement getJuniorModeToggleStatus(String status){
        return juniorModeToggleStatus.format(status);
    }

    public enum ButtonActions {
        ON, OFF;
    }

    public boolean isProfilePresent(String name) {
        waitFor(getGenericEqualsText(name));
        return getGenericEqualsText(name).isElementPresent();
    }

    public DisneyPlusAutoplayPage(WebDriver driver) {
        super(driver);
    }

    public void selectProfileToEdit(String profileToEdit) {
        LOGGER.info("Select Profile to Edit from Edit-Profile page");
        waitFor(getGenericEqualsText(profileToEdit));
        getGenericEqualsText(profileToEdit).click();
    }

    public void clickAutoplay(ButtonActions action, int maxAttemtps) {
        switch (action) {
            case ON:
                while (maxAttemtps-- > 0 && !isAutoplayToggleOn(true)) {
                LOGGER.info("Attempting to turn ON autoplay, max attempts remaining: " + maxAttemtps);
                autoplayBtn.click();
                }
                break;
            case OFF:
                while (maxAttemtps-- > 0 && isAutoplayToggleOn(true)) {
                    LOGGER.info("Attempting to turn OFF autoplay, max attempts remaining: " + maxAttemtps);
                    autoplayBtn.click();
                }
                break;
        }

    }

    public boolean isAutoplayToggleOn(boolean checked) {
        LOGGER.info("Verify autoplay toggle button :" + checked);
        return autoplayBtn.getAttribute("aria-checked").equals("true");
    }

    public void selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(0).click();
        }
    }

    public void nameOfNewProfile() {
        addProfileNameTextBox.type(ProfileConstant.JUNIOR);
    }

    public void juniorModeToggledOn() {
        LOGGER.info("Click on Toggle to turn on For junior Profile");
        waitFor(juniorModeToggle);
        juniorModeToggle.click();
    }

    public boolean verifyJuniorModeToggle(String toggleStatus) {
        LOGGER.info("Verify junior mode toggle status: {}", toggleStatus);
        return getJuniorModeToggleStatus(toggleStatus).isElementPresent();
    }

    public void selectProfileForWhoIsWatching(String profile) {
        getGenericEqualsText(profile).click();
    }

    public DisneyPlusAutoplayPage createJuniorProfile() {
        getActiveProfile();
        clickOnAddProfileFromAccount();
        selectAvatarForProfile();
        nameOfNewProfile();
        enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB);
        juniorModeToggledOn();
        clickOnSaveButton();
        return this;
    }

    public DisneyPlusAutoplayPage exitJuniorProfile() {
        LOGGER.info("Click on Exit junior profile option");
        getExitJuniorProfile().hover();
        getExitJuniorProfile().clickByJs();
        return this;
    }
}
