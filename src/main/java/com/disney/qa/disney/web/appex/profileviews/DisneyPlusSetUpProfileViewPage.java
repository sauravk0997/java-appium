package com.disney.qa.disney.web.appex.profileviews;

import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSetUpProfileViewPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusSetUpProfileViewPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".slick-track")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(css = ".slick-track>div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(css = "#editProfile")
    private ExtendedWebElement editProfileNameTextBox;

    @FindBy(xpath = "//*[@data-testid='profiles-wrapper']/h2")
    private ExtendedWebElement whoIsWatchingText;

    @FindBy(xpath = "//*[@data-route='%s']/p")
    private ExtendedWebElement homepageMenuText;

    @FindBy(xpath = "//*[@data-testid='text-input-error']")
    private ExtendedWebElement duplicateNameMsg;

    public void selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(0).click();
        }
    }

    public void addNewProfileName(String profileName) {
        if (addProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Add new Profile Name");
            addProfileNameTextBox.doubleClick();
            addProfileNameTextBox.sendKeys(Keys.DELETE);
            addProfileNameTextBox.type(profileName);
        }
    }

    public boolean isWhoIsWatchingPresent() {
        LOGGER.info("Verify 'Edit Profile' button on page");
        return whoIsWatchingText.isElementPresent();

    }

    public void editProfileName(String editProfileName) {
        if (editProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Edit new Profile name");
            editProfileNameTextBox.doubleClick();
            editProfileNameTextBox.sendKeys(Keys.DELETE);
            editProfileNameTextBox.type(editProfileName);
            editProfileNameTextBox.sendKeys(Keys.TAB);
        }
    }

    public boolean verifyDuplicateNameErrorMessage() {
        LOGGER.info("Verify Duplicate Name Error Message");
        return duplicateNameMsg.isElementPresent();
    }

    public void selectProfile(String profileText) {
        LOGGER.info("Selecting Profile From Profile Selection Page");
        getGenericEqualsText(profileText).hover();
        getGenericEqualsText(profileText).click(5);
    }

    public DisneyPlusSetUpProfileViewPage createProfile(int numberOfProfile, String... profileNames) {
        for(int i = 0; i < (numberOfProfile - 1); i++) {
            LOGGER.info("Creating profile with name: " + profileNames[i]);
            if(getAddProfileButtonOnPage().isElementPresent(5)){
                clickOnAddProfileButtonOnPage();
            } else {
                clickOnAddProfileFromAccount();
            }
            selectAvatarForProfile();
            addNewProfileName(profileNames[i]);
            enterDOB(ProfileEligibility.ELIGIBLE_DOB);
            clickOnGenderDropdown();
            selectGender();
            clickOnSaveButton();
            new DisneyPlusCommercePage(getDriver()).clickCancelButton();
        }
        return this;
    }
}
