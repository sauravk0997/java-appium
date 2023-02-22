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
public class DisneyPlusDeleteProfilePage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    DisneyContentApiChecker apiHandler = new DisneyContentApiChecker();

    @FindBy(xpath = "//*[@id='remove-main-padding_index']//h3[text()='%s']")
    private ExtendedWebElement profileLink;

    @FindBy(xpath = "//div[@class =  'slick-track']")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(xpath = "//div[@class =  'slick-track']/div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(xpath = "//*[@data-testid='delete-profile-button']")
    private ExtendedWebElement deleteProfileButton;

    @FindBy(xpath = "//*[contains(@class,'after-open Modal')]/p")
    private ExtendedWebElement deleteProfileMsg;

    public DisneyPlusDeleteProfilePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getDeleteProfileButton(){
        return deleteProfileButton;
    }

    public void selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(0).click();
        }
    }

    public void addNewProfileName(String profileName) {
        if (addProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Add new Profile");
            addProfileNameTextBox.doubleClick();
            addProfileNameTextBox.sendKeys(Keys.DELETE);
            addProfileNameTextBox.type(profileName);
        }
    }

    public void selectProfileName(String profileText) {
        LOGGER.info(String.format("Selecting Profile '%s' From Select-Profile Page", profileText));
        profileLink.format(profileText).click();
    }

    public void clickOnDeleteButtonOnEditPage() {
        LOGGER.info("Click on Delete button on Edit page");
        getDeleteProfileButton().click();
    }

    public String getDeleteMessage() {
        LOGGER.info("Verify Delete Message");
        return deleteProfileMsg.getText();
    }

    public boolean isDoneButtonPresent(JsonNode dictionary) {
        LOGGER.info("Verify 'Done' button on page");
        waitFor(getButtonContainText(apiHandler.getDictionaryItemValue(dictionary, DisneyWebKeys.DONE_BTN.getText())));
        return getButtonContainText(apiHandler.getDictionaryItemValue(dictionary, DisneyWebKeys.DONE_BTN.getText())).isElementPresent();
    }

    public void addProfileLogOutLogIn(String newProfileName, String profileToBeSelected, String userName, String pass) {
        clickOnAddProfileFromAccount();
        selectAvatarForProfile();
        addNewProfileName(newProfileName);
        clickOnSaveButton();
        selectProfileName(profileToBeSelected);
        getActiveProfile();
        clickOnLogout();
        dBaseUniversalLogin(userName, pass);
    }
}
