package com.disney.qa.disney.web.appex.profileviews;

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
public class DisneyPlusProfileSelectionViewPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//button[contains(text(), '%s')]")
    private ExtendedWebElement buttonContainText;

    @FindBy(xpath = "//span[contains(text(), '%s')]")
    private ExtendedWebElement spanContainsText;

    @FindBy(xpath = "//*[text()= '%s']")
    private ExtendedWebElement genericEqualsText;

    @FindBy(css = "#logo")
    private ExtendedWebElement disneyPlusLogo;

    @FindBy(css = "#active-profile")
    private ExtendedWebElement activeProfile;

    @FindBy(css = ".slick-track")
    private ExtendedWebElement avatarSelectionTrack;

    @FindBy(css = ".slick-track>div")
    private List<ExtendedWebElement> avatarSelection;

    @FindBy(css = "#addProfile")
    private ExtendedWebElement addProfileNameTextBox;

    @FindBy(css = "#app_scene_content ul")
    private ExtendedWebElement allProfile;

    @FindBy(css = "#app_scene_content ul>div>h3")
    private List<ExtendedWebElement> avatarNamesOnProfileSectionPage;

    @FindBy(xpath = "//h3[text()= '%s']")
    private ExtendedWebElement profileName;

    public DisneyPlusProfileSelectionViewPage(WebDriver driver) {
        super(driver);
    }

    public void clickLogin(String loginElementText) {
        LOGGER.info("Click on Login");
        buttonContainText.format(loginElementText).click();
        LOGGER.info("Url after login: " + getDriver().getCurrentUrl());
    }

    public boolean isLogoPresent() {
        LOGGER.info("Verify Logo");
        return disneyPlusLogo.isElementPresent();
    }

    public void selectAvatarForProfile() {
        avatarSelectionTrack.hover();
        if (!avatarSelection.isEmpty()) {
            LOGGER.info("Selecting Avatar");
            avatarSelection.get(0).click();
        }
    }

    public void addNewProfile(String profileName) {
        if (addProfileNameTextBox.isElementPresent()) {
            LOGGER.info("Add new Profile");
            addProfileNameTextBox.doubleClick();
            addProfileNameTextBox.sendKeys(Keys.DELETE);
            addProfileNameTextBox.type(profileName);
        }
    }

    public void clickOnSaveButton(JsonNode dictionary) {
        LOGGER.info("Click On Save");
        buttonContainText.format(apiChecker.getDictionaryItemValue(dictionary,
                DisneyWebKeys.SAVE_BTN.getText())).click();
    }

    public void selectProfile(String profileText) {
        LOGGER.info("Selecting Profile From Profile Selection Page");
        waitFor(profileName.format(profileText));
        profileName.format(profileText).hover();
        profileName.format(profileText).click();

    }

    public void clickOnLogout(JsonNode dictionary) {
        LOGGER.info("Click on Logout");
        genericEqualsText.format(apiChecker.getDictionaryItemValue(dictionary,
                DisneyWebKeys.ACCOUNT_LOGOUT.getText())).click();
    }

    public boolean getHomePageHomeMenuText(JsonNode dictionary) {
        LOGGER.info("Verify Home Menu on Homepage");
        return genericEqualsText.format(apiChecker.getDictionaryItemValue(dictionary,
                DisneyWebKeys.NAV_HOME.getText())).isElementPresent();
    }

    public String verifyProfileSelectedMatchOnHomePage() {
        LOGGER.info("Verify Selected Profile Name Match on Homepage");
        waitFor(activeProfile);
        activeProfile.hover();
        return activeProfile.getText();
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

    public void clickOnEditProfileOnProfileSelectionPage(JsonNode dictionary) {
        if (buttonContainText.format(apiChecker.getDictionaryItemValue(dictionary,
                DisneyWebKeys.PAGE_EDIT_BTN.getText())).isElementPresent()) {
            buttonContainText.format(apiChecker.getDictionaryItemValue(dictionary,
                    DisneyWebKeys.PAGE_EDIT_BTN.getText())).click();
        }
    }

    public boolean isDoneButtonPresent(JsonNode dictionary) {
        LOGGER.info("Verify 'Done' button on page");
        return buttonContainText.format(apiChecker.getDictionaryItemValue(dictionary,
                DisneyWebKeys.DONE_BTN.getText())).isElementPresent();
    }

    public void clickOnAddProfileOnProfileSelectionPage(JsonNode dictionary) {
        if (genericEqualsText.format(apiChecker.getDictionaryItemValue(dictionary,
                DisneyWebKeys.ACCOUNT_ADD_BTN.getText())).isElementPresent()) {
            genericEqualsText.format(apiChecker.getDictionaryItemValue(dictionary,
                    DisneyWebKeys.ACCOUNT_ADD_BTN.getText())).click();
        }
    }
}
