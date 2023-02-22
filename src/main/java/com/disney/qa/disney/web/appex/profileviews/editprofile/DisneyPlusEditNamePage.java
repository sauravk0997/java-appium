package com.disney.qa.disney.web.appex.profileviews.editprofile;

import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusEditNamePage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    DisneyContentApiChecker apiHandler = new DisneyContentApiChecker();

    @FindBy(css = "#logo")
    private ExtendedWebElement disneyPlusLogo;

    @FindBy(css = "#app_scene_content ul")
    private ExtendedWebElement allProfile;

    @FindBy(css = "#app_scene_content ul>div>h3")
    private List<ExtendedWebElement> avatarNamesOnProfileSectionPage;

    @FindBy(xpath = "//div[@class =  'slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(xpath = "//div[@class =  'slick-track']/div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(css = "#editProfile")
    private ExtendedWebElement editProfileNameTextBox;

    @FindBy(xpath = "//*[@data-testid='text-input-error']")
    private ExtendedWebElement errorMessage;

    @FindBy(xpath = "//*[@data-testid='profiles-wrapper']/h4")
    private ExtendedWebElement selectProfileToEditText;

    public DisneyPlusEditNamePage(WebDriver driver){
        super(driver);
    }

    public boolean isLogoPresent() {
        LOGGER.info("Verify Logo");
        return disneyPlusLogo.isElementPresent();
    }

    public String isEditProfileTextPresent(JsonNode dictionary){
        LOGGER.info("Verify 'Edit Profiles' Text is present");
        return getGenericEqualsText(apiHandler.getDictionaryItemValue(dictionary, DisneyWebKeys.ACCOUNT_EDIT_BTN.getText())).getText();
    }

    public boolean isSelectAProfileToEditTextPresent(){
        LOGGER.info("Verify 'Select a profile to edit' Text is present");
        return selectProfileToEditText.isElementPresent();
    }

    public void selectProfileName(String profileText) {
        LOGGER.info("Selecting Profile From Profile Selection Page");
        getGenericEqualsText(profileText).hover();
        getGenericEqualsText(profileText).click();
    }

    public boolean verifyAvatarNamesOnProfileSelectionPage(String[] avatarNames) {
        allProfile.hover();
        for (int i = 0; i < avatarNamesOnProfileSectionPage.size(); i++) {
            if (!avatarNamesOnProfileSectionPage.get(i).getText().contains(avatarNames[i])) {
                LOGGER.info("Avatar names: " + avatarNamesOnProfileSectionPage.get(i).getText());
                return false;
            }
        }
        LOGGER.info("All Avatar names are present");
        return true;
    }

    public void selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if(!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(0).click();
        }
    }

    public boolean verifyDuplicateNameErrorMessage(){
        LOGGER.info("Verify Duplicate Name Error Message");
        return errorMessage.isElementPresent();
    }

    public boolean verifyOnlySpaceErrorMessage(){
        LOGGER.info("Verify Only space Error Message");
        return errorMessage.isElementPresent();
    }

    public void addNewProfile(String profileName) {
        if (addProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Add new Profile Name");
            addProfileNameTextBox.doubleClick();
            addProfileNameTextBox.sendKeys(Keys.DELETE);
            addProfileNameTextBox.type(profileName);
        }
    }

    public String verifyProfileSelectedMatchOnHomePage(){
        LOGGER.info("Verify Selected Profile Name Match on Homepage");
        waitFor(getActiveProfile());
        getActiveProfile().hover();
        return getActiveProfile().getText();
    }

    public void editProfileName(String profileName) {
        if (editProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Edit new Profile name to: " + profileName);
            editProfileNameTextBox.doubleClick();
            editProfileNameTextBox.sendKeys(Keys.DELETE);
            editProfileNameTextBox.type(profileName);
            editProfileNameTextBox.sendKeys(Keys.TAB);
        }
    }

    public void editProfileNameWithOnlySpace() {
        if (editProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Edit new Profile name");
            editProfileNameTextBox.click();
            editProfileNameTextBox.doubleClick();
            editProfileNameTextBox.sendKeys(Keys.DELETE);
            editProfileNameTextBox.sendKeys(Keys.SPACE);
            editProfileNameTextBox.sendKeys(Keys.TAB);
        }
    }
}
