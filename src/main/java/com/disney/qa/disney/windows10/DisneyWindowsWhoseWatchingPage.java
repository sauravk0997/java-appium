package com.disney.qa.disney.windows10;

import com.disney.qa.common.DisneyAbstractPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsWhoseWatchingPage extends DisneyAbstractPage {
    @ExtendedFindBy(accessibilityId = "ToggleEditingStateButton")
    private ExtendedWebElement editProfilesBtn;
    @FindBy(name = "Add Profile")
    private ExtendedWebElement addProfileButton;
    @FindBy(name = "Test")
    private ExtendedWebElement defaultProfile;
    @FindBy(name = "SKIP")
    private ExtendedWebElement skipButton;
    @FindBy(name = "Choose Avatar")
    private ExtendedWebElement chooseAvatarText;
    @FindBy(name = "Profile Name")
    private ExtendedWebElement profileName;
    @ExtendedFindBy(accessibilityId = "SaveProfileButton")
    private ExtendedWebElement saveProfileButton;
    @FindBy(name = "Edit %s's profile.")
    private ExtendedWebElement editProfile;
    @FindBy(name = "%s")
    private ExtendedWebElement profileByName;
    @ExtendedFindBy(accessibilityId = "DeleteProfileButton")
    private ExtendedWebElement deleteProfileButton;
    @ExtendedFindBy(accessibilityId = "ProfileIconButton")
    private ExtendedWebElement profileIconButton;
    @ExtendedFindBy(accessibilityId = "PrimaryButton")
    private ExtendedWebElement delete;
    @FindBy(name = "Delete")
    private ExtendedWebElement deleteButtonInsideProfileNameTextField;
    @ExtendedFindBy(accessibilityId = "ProfilesListView")
    private ExtendedWebElement profilesList;

    public DisneyWindowsWhoseWatchingPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAddProfilePresent() {
        return addProfileButton.isElementPresent();
    }

    public boolean isProfileByNamePresent(String name) {
        return profileByName.format(name).isElementPresent();
    }

    public void selectProfileByNamePresent(String name) {
        profileByName.format(name).click();
    }

    @Override
    public boolean isOpened() {
        return editProfilesBtn.isElementPresent();
    }

    public void selectDefaultProfile() {
        defaultProfile.click();
    }

    public void addProfile(String name) {
        addProfileButton.click();
        skipButton.click();
        profileName.type(name);
        saveProfileButton.click();
    }

    public void editProfile(String profile, String newProfileName) {
        editProfilesBtn.click();
        editProfile.format(profile).click();
        profileName.type(newProfileName);
        saveProfileButton.click();
    }

    public void deleteProfile(String profile) {
        editProfile.format(profile).click();
        deleteProfileButton.click();
        delete.click();
        editProfilesBtn.click();
    }
}
